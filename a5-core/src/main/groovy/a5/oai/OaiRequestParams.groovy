package a5.oai

class OaiRequestParams {

    static systemParams = ["controller", "repository", "verb"]

    static arguments = ["identifier", "from", "until", "metadataPrefix", "set", "resumptionToken"]

    def illegalParams = []

    Map params

    String identifier

    Date from

    Date until

    String metadataPrefix

    String set

    String resumptionToken

    static OaiRequestParams fromParams(Map params) {
        def oaiRequestParams = new OaiRequestParams()
        oaiRequestParams.params = params
        params.each { key, value ->
            if (params.list((key)).size() > 1) {

            } else if (key in OaiRequestParams.systemParams) {

            } else if (key in OaiRequestParams.arguments) {
                if (key in ["from", "until"]) {
                    try {
                        def date = (value.contains("Z") ? value : value + "T00:00:00Z")
                        oaiRequestParams[(key)] = Date.parse(OaiPmh.DATE_FORMAT, date)
                    } catch(any) {
                    }
                } else {
                    oaiRequestParams[(key)] = value
                }
            } else {
                oaiRequestParams.illegalParams << key
            }
        }
        oaiRequestParams
    }
}