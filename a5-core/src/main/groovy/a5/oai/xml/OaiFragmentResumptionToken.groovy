package a5.oai.xml

import a5.oai.OaiResumptionToken

class OaiFragmentResumptionToken extends OaiFragment {

    OaiResumptionToken resumptionToken

    Integer completeListSize

    void process(StringBuilder stringBuilder) {
        def next = resumptionToken.getNext(completeListSize)
        stringBuilder << '<resumptionToken'
        stringBuilder << ' cursor="'
        stringBuilder << resumptionToken.skip
        stringBuilder << '"'
        stringBuilder << ' completeListSize="'
        stringBuilder << completeListSize
        stringBuilder << '"'
        stringBuilder << '>'
        if (next) stringBuilder << next
        stringBuilder << '</resumptionToken>'
    }
}