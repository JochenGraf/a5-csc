package a5.api

import a5.elastic.ElasticLocalInstance
import a5.index.IndexService
import grails.test.mixin.integration.Integration
import grails.plugins.rest.client.RestBuilder
import grails.transaction.*
import org.springframework.beans.factory.annotation.*
import spock.lang.*

@Integration
class ApiQueryIntegrationSpec extends Specification {

    def rest = new RestBuilder()

    @Autowired
    IndexService indexService

    def base = "http://localhost:8080/api/query"

    def setupSpec() {
        ElasticLocalInstance.clear()
        ElasticLocalInstance.start()
    }

    def cleanupSpec() {
        ElasticLocalInstance.stop()
    }

    def setup() {
        def response1 = rest.get("http://localhost:9200/a5-1")
        def response2 = rest.get("http://localhost:9200/a5-2")
        if (response1.status != 200 || response2.status != 200) {
            indexService.reindex("a5-1")
            indexService.reindex("a5-2")
            sleep(4000) // wait, since elasticsearch works "nearly realtime" only
        }
    }

    void "Faceted Fulltext Search Request"() {
        given:
        def response1 = rest.get("$base/a5-1?\$search=communication&\$count")
        def response2 = rest.get("$base/a5-2?\$search=communication&\$count")
        expect:"16/17 hits for repository a5-1/a5-2"
        response1.status == 200
        response1.json["@odata.count"] == 16
        response2.status == 200
        response2.json["@odata.count"] == 17
    }

    void "Empty Faceted Fulltext Search Request"() {
        given:
        def response1 = rest.get("$base/a5-1?\$search=*&\$count")
        def response2 = rest.get("$base/a5-2?\$search=*&\$count")
        expect:"247 hits for repository a5-1/a5-2"
        response1.status == 200
        response1.json["@odata.count"] == 248
        response2.status == 200
        response2.json["@odata.count"] == 249
    }

    void "Search Refinement 1: Fields Parameter"() {
        given:
        def response1 = rest.get("$base/a5-1?\$search=audio&fields=Title,Description&\$count")
        def response2 = rest.get("$base/a5-2?\$search=audio&fields=ResourceType&\$count")
        expect:"36/162 hits for repository a5-1/a5-2"
        response1.status == 200
        response1.json["@odata.count"] == 36
        response2.status == 200
        response2.json["@odata.count"] == 162
    }

    void "Search Refinement 2: Drill Parameter"() {
        given:
        def response1 = rest.get("$base/a5-1?\$search=*&drill=Title eq 'Numerals'&\$count")
        def response2 = rest.get("$base/a5-2?\$search=*&drill=Title eq 'Numerals'&\$count")
        expect:"2 hits for repository a5-1/a5-2"
        response1.status == 200
        response1.json["@odata.count"] == 2
        response2.status == 200
        response2.json["@odata.count"] == 2
    }

    void "Search Refinement 3: Skip and Top Parameter"() {
        given:
        def response1 = rest.get("$base/a5-1?\$search=*&skip=10&\$top=30")
        def response2 = rest.get("$base/a5-2?\$search=communication&\$skip=5&\$top=1")
        expect:"Page size of 30/1 for repository a5-1/a5-2"
        response1.status == 200
        response1.json.value.size() == 30
        response2.status == 200
        response2.json.value.size() == 1
    }

    void "Search Refinement 4: Orderby Parameter"() {
        given:
        def response1 = rest.get("$base/a5-2?\$search=*&\$orderby=ProjectDisplayName asc")
        def response2 = rest.get("$base/a5-2?\$search=*&\$orderby=ProjectDisplayName desc,Country desc")
        expect:"Page size of 30/1 for repository a5-1/a5-2"
        response1.status == 200
        response1.json.value[0].ProjectDisplayName[0] == "Archive for Intercultural and Multilingual Communication"
        response2.status == 200
        response2.json.value[0].ProjectDisplayName[0] == "The Family Problems Picture Task in Yurakar\u00e9"
    }
}
