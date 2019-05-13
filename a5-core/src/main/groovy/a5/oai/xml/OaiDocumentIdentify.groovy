package a5.oai.xml

import a5.oai.OaiPmh
import a5.oai.OaiVerb

class OaiDocumentIdentify extends OaiDocument {

    String repositoryName

    String baseUrl

    ArrayList<String> adminEmails = []

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: OaiVerb.IDENTIFY, baseUrl: baseUrl)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<Identify>'
        stringBuilder << '<repositoryName>'
        stringBuilder << repositoryName
        stringBuilder << '</repositoryName>'
        stringBuilder << '<baseURL>'
        stringBuilder << baseUrl
        stringBuilder << '</baseURL>'
        stringBuilder << '<protocolVersion>'
        stringBuilder << OaiPmh.PROTOCOL_VERSION
        stringBuilder << '</protocolVersion>'
        adminEmails.each {
            stringBuilder << '<adminEmail>'
            stringBuilder << it
            stringBuilder << '</adminEmail>'
        }
        if (!adminEmails) {
            stringBuilder << '<adminEmail/>'
        }
        stringBuilder << '<earliestDatestamp>'
        stringBuilder << OaiPmh.EARLIEST_DATESTAMP
        stringBuilder << '</earliestDatestamp>'
        stringBuilder << '<deletedRecord>'
        stringBuilder << OaiPmh.DELETED_RECORD
        stringBuilder << '</deletedRecord>'
        stringBuilder << '<granularity>'
        stringBuilder << OaiPmh.GRANULARITY
        stringBuilder << '</granularity>'
        stringBuilder << "</Identify>"
        endDocument()
    }
}