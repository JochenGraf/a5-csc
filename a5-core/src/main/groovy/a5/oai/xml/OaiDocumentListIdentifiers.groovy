package a5.oai.xml

import a5.oai.OaiItemList
import a5.oai.OaiResumptionToken
import a5.oai.OaiVerb

class OaiDocumentListIdentifiers extends OaiDocument {

    Date from

    Date until

    String metadataPrefix

    OaiResumptionToken resumptionToken

    String baseUrl

    OaiItemList itemList

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: OaiVerb.LIST_IDENTIFIERS, from: from, until: until,
                metadataPrefix: metadataPrefix, resumptionToken: resumptionToken, baseUrl: baseUrl)
        def oaiFragmentResumptionToken = new OaiFragmentResumptionToken(resumptionToken: resumptionToken,
                completeListSize: itemList.completeListSize)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<ListIdentifiers>'
        itemList.items.each {
            def oaiFragmentHeader = new OaiFragmentHeader(identifier: it.identifier, datestamp: it.datestamp)
            oaiFragmentHeader.process(stringBuilder)
        }
        oaiFragmentResumptionToken.process(stringBuilder)
        stringBuilder << '</ListIdentifiers>'
        endDocument()
    }
}