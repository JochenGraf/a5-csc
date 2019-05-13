package a5.oai.xml

import a5.oai.OaiResumptionToken
import a5.oai.OaiVerb

class OaiFragmentRequest extends OaiFragment {

    OaiVerb verb

    String identifier

    String metadataPrefix

    String baseUrl

    Date from

    Date until

    OaiResumptionToken resumptionToken

    void process(StringBuilder stringBuilder) {
        stringBuilder << '<request'
        if (verb) {
            stringBuilder << ' verb="'
            stringBuilder << verb.arg
            stringBuilder << '"'
        }
        if (identifier) {
            stringBuilder << ' identifier="'
            stringBuilder << identifier
            stringBuilder << '"'
        }
        if (metadataPrefix) {
            stringBuilder << ' metadataPrefix="'
            stringBuilder << metadataPrefix
            stringBuilder << '"'
        }
        if (from) {
            stringBuilder << ' from="'
            stringBuilder << from
            stringBuilder << '"'
        }
        if (until) {
            stringBuilder << ' until="'
            stringBuilder << until
            stringBuilder << '"'
        }
        if (resumptionToken && resumptionToken.skip > 0) {
            stringBuilder << ' resumptionToken="'
            stringBuilder << resumptionToken
            stringBuilder << '"'
        }
        stringBuilder << '>'
        stringBuilder << baseUrl
        stringBuilder << '</request>'
    }
}