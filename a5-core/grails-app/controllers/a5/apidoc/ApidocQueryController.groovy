package a5.apidoc

class ApidocQueryController {

    def index() {
        def requests = grailsApplication.config.a5.apiquery.request
        def params = grailsApplication.config.a5.apiquery.param
        def errors = grailsApplication.config.a5.api.error
        render (view: "index", model: [req: requests, par: params, err: errors])
    }
}
