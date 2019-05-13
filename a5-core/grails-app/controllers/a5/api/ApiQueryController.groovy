package a5.api

class ApiQueryController implements ApiController {

    def apiQueryService

    def query() {
        serialize(apiQueryService.query(params))
    }
}
