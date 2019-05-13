package a5.oai.xml

class OaiFragmentRecord extends OaiFragment {

    String identifier

    String datestamp

    String metadata

    void process(StringBuilder stringBuilder) {
        def oaiFragmentHeader = new OaiFragmentHeader(identifier: identifier, datestamp: datestamp)
        stringBuilder << '<record>'
        oaiFragmentHeader.process(stringBuilder)
        stringBuilder << '<metadata>'
        stringBuilder << metadata
        stringBuilder << '</metadata>'
        stringBuilder << '</record>'
    }
}