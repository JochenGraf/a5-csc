package a5.com

import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.elastic.ElasticSettings
import a5.elastic.ElasticMapping
import a5.mappable.Mappable
import grails.plugins.rest.client.RestBuilder
import grails.util.Holders
import grails.converters.JSON
import static grails.async.Promises.task
import static grails.async.Promises.waitAll

class ElasticService {

    int BULK_SIZE = 1000

    def esServer = Holders.config.a5.slave.elasticsearch.server

    void indexClose(String indexName) {
        new RestBuilder().post("$esServer/$indexName/_close")
        //TODO: throw elasticsearch exception
    }

    void indexOpen(String indexName) {
        new RestBuilder().post("$esServer/$indexName/_open")
        //TODO: throw elasticsearch exception
    }

    void indexDelete(String indexName) {
        new RestBuilder().delete("$esServer/$indexName")
        //TODO: throw elasticsearch exception
    }

    void indexSettings(String indexName, ElasticSettings settings) {
        println (settings as JSON)
        def elasticResponse = new RestBuilder().put("$esServer/$indexName") {
            body settings as JSON
        }
        println (elasticResponse.json)
    }

    void indexMapping(String indexName, String indexType, ElasticMapping mapping) {
        println "Index Mapping [$indexName,$indexType] " + (mapping as JSON)
        indexClose(indexName)
        def elasticResponse = new RestBuilder().put("$esServer/$indexName/_mapping/$indexType?update_all_types") {
            body mapping as JSON
        }
        indexOpen(indexName)
        println elasticResponse.json
        //TODO: throw elasticsearch exception
    }

    void indexRefresh(String indexName, String indexType, String parent) {
        String parentString = parent ? "?parent=$parent" : ""
        def elasticResponse = new RestBuilder().post("$esServer/$indexName/$indexType/_refresh$parentString") {}
        println elasticResponse.json
    }

    String indexAnalyze(String indexName, String analyzer, String queryString) {
        Map json = [
                analyzer: analyzer,
                text: queryString
        ]
        def elasticResponse = new RestBuilder().post("$esServer/$indexName/_analyze") {
            body json as JSON
        }
        if (elasticResponse.json.tokens) {
            elasticResponse.json.tokens.token.join(" ")
        } else {
            null
        }
    }

    ElasticResult read(String indexName, String indexType, String id) {
        def result = new RestBuilder().get("$esServer/$indexName/$indexType/$id") {}
        if (result.status != 200) println result.json
        new ElasticResult(result.json)
    }

    ElasticResult read(String indexName, String indexType, ElasticQ query) {
        def result = new RestBuilder().post("$esServer/$indexName/$indexType/_search") {
            body query.asMap() as JSON
        }
        if (result.status != 200) println result.json
        new ElasticResult(result.json)
    }

    void create(String indexName, String indexType, Mappable mappable) {
        createOne(indexName, indexType, mappable, null)
    }

    void create(String indexName, String indexType, Mappable mappable, String parent) {
        createOne(indexName, indexType, mappable, parent)
    }

    void create(String indexName, String indexType, ArrayList<Mappable> mappables) {
        if (mappables.size() == 1) {
            createOne(indexName, indexType, mappables[0], null)
        } else if (mappables.size() <= BULK_SIZE) {
            createBulk(indexName, indexType, mappables, null)
        } else {
            createBulkAsync(indexName, indexType, mappables, null)
        }
    }

    void create(String indexName, String indexType, ArrayList<Mappable> mappables, String parent) {
        if (mappables.size() == 1) {
            createOne(indexName, indexType, mappables[0], parent)
        } else if (mappables.size() <= BULK_SIZE) {
            createBulk(indexName, indexType, mappables, parent)
        } else {
            createBulkAsync(indexName, indexType, mappables, parent)
        }
    }

    private void createOne(String indexName, String indexType, Mappable mappable, String parent) {
        String parentString = parent == null ? "" : "?parent=$parent"
        new RestBuilder().post("$esServer/$indexName/$indexType/$parentString") {
            body mappable as JSON
        }
    }

    private void createBulk(String indexName, String indexType, ArrayList<Mappable> mappables, String parent) {
        Map methodMap = parent ? [create: [_parent: parent]] : [create: [:]]
        String jsonString = ""
        mappables.each {
            String method = (methodMap as JSON).toString()
            String data = (it as JSON).toString()
            jsonString += "$method\n$data\n"
        }
        new RestBuilder().post("$esServer/$indexName/$indexType/_bulk") {
            contentType "application/json;charset=UTF-8"
            json jsonString
        }
    }

    private void createBulkAsync(String indexName, String indexType, ArrayList<Mappable> mappables, String parent) {
        float iterations = mappables.size() / BULK_SIZE
        def threads = []
        for(int i = 0; i < iterations; i++) {
            threads << i
        }
        def tasks = threads.collect { i ->
            def first = i * BULK_SIZE
            def last = first + BULK_SIZE - 1
            if (last > mappables.size() - 1) last = mappables.size() - 1
            task {
                createBulk(indexName, indexType, mappables[first..last], parent)
            }
        }
        waitAll(tasks)
    }
}