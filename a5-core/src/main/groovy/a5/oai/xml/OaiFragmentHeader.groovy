package a5.oai.xml

import a5.oai.OaiPmh

class OaiFragmentHeader extends OaiFragment {

    String identifier

    String datestamp

    void process(StringBuilder stringBuilder) {
        stringBuilder << '<header>'
        stringBuilder << '<identifier>'
        stringBuilder << identifier
        stringBuilder << '</identifier>'
        stringBuilder << '<datestamp>'
        stringBuilder << datestamp
        stringBuilder << '</datestamp>'
        stringBuilder << '</header>'
    }
}