package a5.api

/**
 * Created by graf on 02.06.16.
 */
class ApiErrorController {

    def index() {
        def err = grailsApplication.config.a5.api.error[params.code]
        def status = err.status
        def message = err.message
        def arg1 = params.arg1
        response.status = Integer.parseInt(status)
        render(status + ": " + message.replace("%1%", "'" + arg1 + "'"))
    }
}
