package a5.oai.xml

import a5.oai.OaiItemList
import a5.oai.OaiResumptionToken
import a5.oai.OaiVerb

class OaiDocumentListRecords extends OaiDocument {

    Date from

    Date until

    String metadataPrefix

    OaiResumptionToken resumptionToken

    OaiItemList itemList

    String baseUrl

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: OaiVerb.LIST_RECORDS, from: from, until: until,
                metadataPrefix: metadataPrefix, resumptionToken: resumptionToken, baseUrl: baseUrl)
        def oaiFragmentResumptionToken = new OaiFragmentResumptionToken(resumptionToken: resumptionToken,
                completeListSize: itemList.completeListSize)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<ListRecords>'
        itemList.items.each {
            def oaiFragmentRecord = new OaiFragmentRecord(identifier: it.identifier, datestamp: it.datestamp,
                    metadata: it.metadata[metadataPrefix])
            oaiFragmentRecord.process(stringBuilder)
        }
        oaiFragmentResumptionToken.process(stringBuilder)
        stringBuilder << '</ListRecords>'
        endDocument()
    }
}