package a5.oai

class OaiResumptionToken {

    static final String SEPARATOR1 = "="

    static final String SEPARATOR2 = "!"

    OaiRequestParams requestParams

    Integer skip = 0

    Integer top = 100

    boolean valid = true

    private boolean hasMoreResults(Integer completeListSize) {
        (skip + top) < completeListSize
    }

    String getNext(Integer completeListSize) {
        if (!hasMoreResults(completeListSize)) return null
        def s = skip + top
        def t = top
        if ((s + t) >= completeListSize) t = completeListSize - s
        def nextResumptionToken = new OaiResumptionToken(requestParams: requestParams)
        nextResumptionToken.skip = s
        nextResumptionToken.top = t
        nextResumptionToken
    }

    static OaiResumptionToken fromRequestParams(OaiRequestParams requestParams) {
        def oaiResumptionToken = new OaiResumptionToken(requestParams: requestParams)
        oaiResumptionToken.parse()
        oaiResumptionToken
    }

    private parse() {
        if (!requestParams.resumptionToken) {
            valid = false
            return
        }
        def decoded = new String(requestParams.resumptionToken.decodeBase64())
        def tokens = decoded.tokenize(SEPARATOR2)
        if (!tokens) {
            valid = false
            return
        }
        tokens.each {
            def t = it.tokenize(SEPARATOR1)
            if (!t || !t[0] || !t[1]) {
                valid = false
                return
            }
            switch(t[0]) {
                case "f":
                    try {
                        def date = (t[1].contains("Z") ? t[1] : t[1] + "T00:00:00Z")
                        requestParams.from = Date.parse(OaiPmh.DATE_FORMAT, date)
                    } catch(any) {
                        valid = false
                        return
                    }
                    break
                case "u":
                    try {
                        def date = (t[1].contains("Z") ? t[1] : t[1] + "T00:00:00Z")
                        requestParams.until = Date.parse(OaiPmh.DATE_FORMAT, date)
                    } catch(any) {
                        valid = false
                        return
                    }
                    break
                case "m":
                    requestParams.metadataPrefix = t[1]
                    break
                case "s":
                    requestParams.set = t[1]
                    break
                case "sp":
                    if (!t[1].isInteger()) {
                        valid = false
                        return
                    }
                    skip = t[1].toInteger()
                    break
                case "tp":
                    if (!t[1].isInteger()) {
                        valid = false
                        return
                    }
                    top = t[1].toInteger()
                    break
                default:
                    valid = false
                    return
                    break
            }
        }
    }

    String serialize() {
        def arr = []
        if (requestParams.from) {
            def formatted = requestParams.from.format(OaiPmh.DATE_FORMAT)
            arr << "f$SEPARATOR1$formatted"
        }
        if (requestParams.until) {
            def formatted = requestParams.until.format(OaiPmh.DATE_FORMAT)
            arr << "u$SEPARATOR1$formatted"
        }
        if (requestParams.metadataPrefix) arr << "m$SEPARATOR1$requestParams.metadataPrefix"
        if (requestParams.set) arr << "s$SEPARATOR1$requestParams.set"
        arr << "sp$SEPARATOR1$skip"
        arr << "tp$SEPARATOR1$top"
        arr.join(SEPARATOR2).bytes.encodeBase64().toString()
    }

    String toString() {
        serialize()
    }
}