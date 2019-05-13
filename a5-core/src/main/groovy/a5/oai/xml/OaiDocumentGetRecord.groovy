package a5.oai.xml

import a5.oai.OaiVerb

class OaiDocumentGetRecord extends OaiDocument {

    String identifier

    String metadataPrefix

    String baseUrl

    String datestamp

    String metadata

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: OaiVerb.GET_RECORD, identifier: identifier,
                metadataPrefix: metadataPrefix, baseUrl: baseUrl)
        def oaiFragmentRecord = new OaiFragmentRecord(identifier: identifier, datestamp: datestamp, metadata: metadata)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<GetRecord>'
        oaiFragmentRecord.process(stringBuilder)
        stringBuilder << '</GetRecord>'
        endDocument()
    }
}