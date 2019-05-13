package a5.oai

import a5.oai.xml.OaiDocumentGetRecord
import a5.oai.xml.OaiDocumentIdentify
import a5.oai.xml.OaiDocumentListIdentifiers
import a5.oai.xml.OaiDocumentListMetadataFormats
import a5.oai.xml.OaiDocumentListRecords

class OaiResponse {

    OaiRequest oaiRequest

    private String body

    String getBody() {
        oaiRequest.validate()
        doRequest()
    }

    private doRequest() {
        if (oaiRequest.error) {
            doError()
        } else {
            "do${oaiRequest.params.verb}"()
        }
    }

    private doError() {
        body = oaiRequest.error.responseBody
    }

    private doGetRecord() {
        body = new OaiDocumentGetRecord(
                identifier: oaiRequest.params.identifier,
                metadataPrefix: oaiRequest.params.metadataPrefix,
                baseUrl: oaiRequest.repository.baseUrl,
                datestamp: oaiRequest.item.datestamp,
                metadata: oaiRequest.item.metadata[oaiRequest.params.metadataPrefix]
        ).responseBody
    }

    private doIdentify() {
        body = new OaiDocumentIdentify(
                repositoryName: oaiRequest.repository.repositoryName ?: oaiRequest.params.repository,
                baseUrl: oaiRequest.repository.baseUrl,
                adminEmails: oaiRequest.repository.adminEmails
        ).responseBody
    }

    private doListIdentifiers() {
        body = new OaiDocumentListIdentifiers(
                from: oaiRequest.requestParams.from,
                until: oaiRequest.requestParams.until,
                metadataPrefix: oaiRequest.params.metadataPrefix,
                baseUrl: oaiRequest.repository.baseUrl,
                resumptionToken: oaiRequest.resumptionToken,
                itemList: oaiRequest.itemList
        ).responseBody
    }

    private doListMetadataFormats() {
        def metadataFormats = [:]
        if (!oaiRequest.params.identifier) {
            metadataFormats = oaiRequest.repository.metadataFormats
        } else {
            oaiRequest.item.metadata.keySet().each {
                metadataFormats[(it)] = oaiRequest.repository.metadataFormats[(it)]
            }
        }
        body = new OaiDocumentListMetadataFormats(
                identifier: oaiRequest.params.identifier,
                baseUrl: oaiRequest.repository.baseUrl,
                metadataFormats: metadataFormats
        ).responseBody
    }

    private doListRecords() {
        body = new OaiDocumentListRecords(
                from: oaiRequest.requestParams.from,
                until: oaiRequest.requestParams.until,
                metadataPrefix: oaiRequest.requestParams.metadataPrefix,
                baseUrl: oaiRequest.repository.baseUrl,
                resumptionToken: oaiRequest.resumptionToken,
                itemList: oaiRequest.itemList
        ).responseBody
    }
}