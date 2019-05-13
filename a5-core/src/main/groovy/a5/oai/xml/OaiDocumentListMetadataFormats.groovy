package a5.oai.xml

import a5.oai.OaiMetadataFormat
import a5.oai.OaiVerb

class OaiDocumentListMetadataFormats extends OaiDocument {

    String identifier

    String baseUrl

    Map<String, OaiMetadataFormat> metadataFormats

    void process() {
        def oaiFragmentRequest = new OaiFragmentRequest(verb: OaiVerb.LIST_METADATA_FORMATS, identifier: identifier,
                baseUrl: baseUrl)
        startDocument()
        oaiFragmentRequest.process(stringBuilder)
        stringBuilder << '<ListMetadataFormats>'
        metadataFormats?.each { key, value ->
            stringBuilder << '<metadataFormat>'
            stringBuilder << '<metadataPrefix>'
            stringBuilder << value.prefix
            stringBuilder << '</metadataPrefix>'
            stringBuilder << '<schema>'
            stringBuilder << value.schema
            stringBuilder << '</schema>'
            stringBuilder << '<metadataNamespace>'
            stringBuilder << value.namespace
            stringBuilder << '</metadataNamespace>'
            stringBuilder << '</metadataFormat>'
        }
        stringBuilder << '</ListMetadataFormats>'
        endDocument()
    }
}