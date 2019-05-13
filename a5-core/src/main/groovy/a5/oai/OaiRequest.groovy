package a5.oai

import a5.oai.xml.OaiDocumentError
import grails.util.Holders

class OaiRequest {

    Map params

    OaiRepository repository

    private OaiRequestParams requestParams

    private OaiResumptionToken resumptionToken

    private OaiItem item

    private OaiItemList itemList

    private OaiDocumentError error

    def validate() {
        initRepository()
        validateBadVerb()
        if (error) return
        requestParams = OaiRequestParams.fromParams(params)
        switch(OaiVerb.getOaiVerb(params.verb)) {
            case OaiVerb.GET_RECORD:
                validate1GetRecord()
                initItem()
                validate2GetRecord()
                break
            case OaiVerb.IDENTIFY:
                validateIdentify()
                break
            case OaiVerb.LIST_IDENTIFIERS:
                resumptionToken = OaiResumptionToken.fromRequestParams(requestParams)
                if (params.resumptionToken && !resumptionToken.valid) {
                    throwBadResumptionToken()
                    return
                }
                validate1ListIdentifiers()
                initItemList()
                validate2ListIdentifiers()
                break
            case OaiVerb.LIST_METADATA_FORMATS:
                validate1ListMetadataFormats()
                initItem()
                validate2ListMetadataFormats()
                break
            case OaiVerb.LIST_RECORDS:
                resumptionToken = OaiResumptionToken.fromRequestParams(requestParams)
                resumptionToken = OaiResumptionToken.fromRequestParams(requestParams)
                if (params.resumptionToken && !resumptionToken.valid) {
                    throwBadResumptionToken()
                    return
                }
                validate1ListRecords()
                initItemList()
                validate2ListRecords()
                break
            case OaiVerb.LIST_SETS:
                throwNoSetHierarchy()
                break
            default:
                throwBadVerb()
                break
        }
    }

    private getOaiPmhService() {
        def oaiPmhService = Holders.config.getProperty('a5.oai.oaiPmhService')
        Holders.grailsApplication.mainContext.getBean(oaiPmhService)
    }

    private initRepository() {
        repository = oaiPmhService.getRepository(params.repository)
    }

    private initItem() {
        if (error) return
        item = oaiPmhService.getItem(params.repository, params.metadataPrefix, params.identifier)
    }

    private initItemList() {
        if (error) return
        itemList = oaiPmhService.getItemList(params.repository, requestParams.metadataPrefix, requestParams.from,
                requestParams.until, resumptionToken.skip, resumptionToken.top)
    }

    private validate1GetRecord() {
        def allowed = [OaiArgument.IDENTIFIER, OaiArgument.METADATA_PREFIX]
        validateIllegalArguments(allowed)
        if (!error) validateRepeatedArguments(allowed)
        if (!error) validateRequiredArguments([OaiArgument.IDENTIFIER, OaiArgument.METADATA_PREFIX])
        if (!error) validateCannotDisseminateFormatRepository()
    }

    private validate2GetRecord() {
        if (error) return
        validateIdDoesNotExist()
        if (!error) validateCannotDisseminateFormatItem()
    }

    private validateIdentify() {
        validateIllegalArguments([])
    }

    private validate1ListIdentifiers() {
        def allowed = [OaiArgument.FROM, OaiArgument.UNTIL, OaiArgument.METADATA_PREFIX, OaiArgument.SET,
                       OaiArgument.RESUMPTION_TOKEN]
        validateIllegalArguments(allowed)
        if (!error) validateRepeatedArguments(allowed)
        if (!error) validateRequiredArguments([OaiArgument.METADATA_PREFIX])
        if (!error) validateExclusiveArgument(OaiArgument.RESUMPTION_TOKEN)
        if (!error) validateDateArguments([OaiArgument.FROM, OaiArgument.UNTIL])
        if (!error) validateCannotDisseminateFormatRepository()
        if (!error) validateNoSetHierarchy()
    }

    private validate2ListIdentifiers() {
        if (!error) validateNoRecordsMatch()
    }

    private validate1ListMetadataFormats() {
        validateIllegalArguments([OaiArgument.IDENTIFIER])
    }

    private validate2ListMetadataFormats() {
        if (!error) validateIdDoesNotExist()
        if (!error) validateNoMetadataFormats()
    }

    private validate1ListRecords() {
        validateIllegalArguments([OaiArgument.FROM, OaiArgument.UNTIL, OaiArgument.METADATA_PREFIX, OaiArgument.SET,
                                  OaiArgument.RESUMPTION_TOKEN])
        if (!error) validateRequiredArguments([OaiArgument.METADATA_PREFIX])
        if (!error) validateExclusiveArgument(OaiArgument.RESUMPTION_TOKEN)
        if (!error) validateDateArguments([OaiArgument.FROM, OaiArgument.UNTIL])
        if (!error) validateCannotDisseminateFormatRepository()
        if (!error) validateNoSetHierarchy()
    }

    private validate2ListRecords() {
        if (!error) validateNoRecordsMatch()
    }

    private validateBadVerb() {
        if (!params.verb || params.list("verb").size() > 1) {
            throwBadVerb()
        }
    }

    private validateIllegalArguments(ArrayList<OaiArgument> allowed) {
        def allowedList = allowed*.arg
        def p = params.clone()
        p.remove "controller"
        p.remove "repository"
        p.remove "verb"
        p.keySet().each {
            if (!(it in allowedList)) {
                throwBadArgument()
                return
            }
        }
    }

    private validateRepeatedArguments(ArrayList<OaiArgument> allowed) {
        allowed*.arg.each {
            if (params[(it)] != null && requestParams[(it)] == null) {
                throwBadArgument()
                return
            }
        }
    }

    private validateExclusiveArgument(OaiArgument exclusive) {
        if (!params[exclusive.arg]) return
        def p = params.clone()
        p.remove "controller"
        p.remove "repository"
        p.remove "verb"
        if (!(p[exclusive.arg] && p.size() == 1)) {
            throwBadArgument()
        }
    }

    private validateRequiredArguments(ArrayList<OaiArgument> arguments) {
        if (requestParams.resumptionToken != null) return
        arguments.each {
            if (params[it.arg] == null) {
                throwBadArgument()
                return
            }
        }
    }

    private validateDateArguments(ArrayList<OaiArgument> arguments) {
        arguments*.arg.each {
            if (params[(it)] != null && requestParams[(it)] == null) {
                throwBadArgument()
                return
            }
        }
    }

    private validateCannotDisseminateFormatRepository() {
        if (repository.metadataFormats[requestParams.metadataPrefix] == null) {
            throwCannotDisseminateFormat()
        }
    }

    private validateCannotDisseminateFormatItem() {
        if (item.metadata[requestParams.metadataPrefix] == null) {
            throwCannotDisseminateFormat()
        }
    }

    private validateIdDoesNotExist() {
        if (params.identifier && !item) {
            error = new OaiDocumentError(
                    verb: OaiVerb.getOaiVerb(params.verb),
                    baseUrl: repository.baseUrl,
                    error: OaiError.ID_DOES_NOT_EXIST
            )
        }
    }

    private validateNoMetadataFormats() {
        if (item.metadata?.size() == 0) {
            error = new OaiDocumentError(
                    verb: OaiVerb.getOaiVerb(params.verb),
                    baseUrl: repository.baseUrl,
                    error: OaiError.NO_METADATA_FORMATS
            )
        }
    }

    private validateNoRecordsMatch() {
        if (!itemList?.items) {
            throwNoRecordsMatch()
        }
    }

    private validateNoSetHierarchy() {
        if (params.set != null) {
            throwNoSetHierarchy()
        }
    }

    private throwBadVerb() {
        error = new OaiDocumentError(
                baseUrl: repository.baseUrl,
                error: OaiError.BAD_VERB
        )
    }

    private throwNoRecordsMatch() {
        if (requestParams.resumptionToken) {
            throwBadResumptionToken()
            return
        }
        error = new OaiDocumentError(
                verb: OaiVerb.getOaiVerb(params.verb),
                baseUrl: repository.baseUrl,
                error: OaiError.NO_RECORDS_MATCH
        )
    }

    private throwCannotDisseminateFormat() {
        if (requestParams.resumptionToken) {
            throwBadResumptionToken()
            return
        }
        error = new OaiDocumentError(
                verb: OaiVerb.getOaiVerb(params.verb),
                baseUrl: repository.baseUrl,
                error: OaiError.CANNOT_DISSEMINATE_FORMAT
        )
    }

    private throwBadArgument() {
        if (requestParams.resumptionToken) {
            throwBadResumptionToken()
            return
        }
        error = new OaiDocumentError(
                verb: OaiVerb.getOaiVerb(params.verb),
                baseUrl: repository.baseUrl,
                error: OaiError.BAD_ARGUMENT
        )
    }

    private throwNoSetHierarchy() {
        if (requestParams.resumptionToken) {
            throwBadResumptionToken()
            return
        }
        error = new OaiDocumentError(
                verb: OaiVerb.getOaiVerb(params.verb),
                baseUrl: repository.baseUrl,
                error: OaiError.NO_SET_HIERARCHY
        )
    }

    private throwBadResumptionToken() {
        error = new OaiDocumentError(
                verb: OaiVerb.getOaiVerb(params.verb),
                baseUrl: repository.baseUrl,
                error: OaiError.BAD_RESUMPTION_TOKEN
        )
    }
}