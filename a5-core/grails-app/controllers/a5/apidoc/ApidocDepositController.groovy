package a5.apidoc

class ApidocDepositController {

    def index() {
        def requests = grailsApplication.config.a5.apideposit.request
        def params = grailsApplication.config.a5.apideposit.param
        def errors = grailsApplication.config.a5.api.error
        render (view: "index", model: [req: requests, par: params, err: errors])
    }
}