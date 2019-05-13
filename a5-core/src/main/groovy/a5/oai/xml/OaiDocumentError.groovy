package a5.oai.xml

import a5.oai.OaiError
import a5.oai.OaiVerb

class OaiDocumentError extends OaiDocument {

    OaiVerb verb

    String baseUrl

    String message

    OaiError error

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: verb, baseUrl: baseUrl)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<error code="'
        stringBuilder << error.code
        stringBuilder << '">'
        stringBuilder << (message ?: '')
        stringBuilder << (message ? ' ' : '')
        stringBuilder << error.description
        stringBuilder << '</error>'
        endDocument()
    }
}
