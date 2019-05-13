package a5.index

import a5.elastic.ElasticQ
import grails.converters.JSON
import grails.util.Holders
import grails.converters.JSON

class Index {

    static final String OBJECT = "object"

    static final String QUERY = "query"

    static final String ANNOTATION = "annotation"

    static final String OAI = "oai"

    static void create(String indexName, String indexType, Object obj) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
        elastic.documentCreate(indexName, indexType, obj as JSON)
    }

    static void create(String indexName, String indexType, String id, Object obj) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
        elastic.documentCreate(indexName, indexType, id, obj as JSON)
    }

    static Map read(String indexName, String indexType, String id) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
        elastic.read(indexName, indexType, id).firstHit
    }

    static Map read(String indexName, String indexType, ElasticQ query) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
        elastic.read(indexName, indexType, query).firstHit
    }

    static void update(String indexName, String id, IndexObject obj) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
    }

    static void delete(String indexName, String id) {
        def elastic = Holders.grailsApplication.mainContext.getBean("elasticService")
    }
}