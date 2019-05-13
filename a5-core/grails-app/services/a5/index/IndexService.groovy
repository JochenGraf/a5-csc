package a5.index

import a5.elastic.ElasticSettingsQuery
import a5.index.Index
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class IndexService {

    protected Logger log = LoggerFactory.getLogger(getClass())

    def elasticService

    def grailsApplication

    def indexAnnotationService

    def indexOaiService

    def indexObjectService

    def indexQueryService

    File getLogFile(String repositoryName) {
        String docBaseLog = grailsApplication.config.a5.repository[repositoryName].docBaseLog
        new File(new File(docBaseLog), "reindex.log")
    }

    void start(String repositoryName) {
        def f = getLogFile(repositoryName)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.write "${new Date()}\n"
    }

    void progress(String repositoryName, info) {
        println "$info"
        getLogFile(repositoryName) << "$info\n"
    }

    void stop(String repositoryName) {
        getLogFile(repositoryName) << "${new Date()}\n"
    }

    void reindex(String repositoryName) {
        start(repositoryName)
        elasticService.indexDelete(repositoryName)
        elasticService.indexSettings(repositoryName, new ElasticSettingsQuery())
        indexAnnotationService.mapping(repositoryName)
        indexQueryService.mapping(repositoryName)
        indexObjectService.mapping(repositoryName)
        indexOaiService.mapping(repositoryName)
        indexObjectService.reindex(repositoryName)
        elasticService.indexRefresh(repositoryName, Index.OBJECT, null)
        sleep(3000)
        indexQueryService.reindex(repositoryName)
        elasticService.indexRefresh(repositoryName, Index.QUERY, null)
        indexAnnotationService.reindex(repositoryName)
        elasticService.indexRefresh(repositoryName, Index.ANNOTATION, null)
        indexOaiService.reindex(repositoryName)
        elasticService.indexRefresh(repositoryName, Index.OAI, null)
        stop(repositoryName)
    }
}