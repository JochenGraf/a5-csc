package a5.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApiAdminController implements ApiController {

    def apiAdminService

    def indexService

    protected Logger log = LoggerFactory.getLogger(getClass())

    def config() {
        serialize(apiAdminService.config(params))
    }

    def indexConfig() {
        File configFile = new File(grailsApplication.config.a5.repository[params.repository].config)
        render configFile.text, conentType: "text/xml"
    }

    def mappingQuery() {
        serialize(apiAdminService.mappingQuery(params))
    }

    def reindexRepository() {
        indexService.reindex(params.repository)
        render "Reindex of ${params.repository} successfully completed."
    }

    def reindexLog() {
        File logFile = indexService.getLogFile(params.repository)
        def formatted = new StringBuilder()
        formatted << "<html><head></head><body><pre>"
        formatted << logFile.text
        formatted << "</pre></body></html>"
        render formatted, contentType: "text/html"
    }
}