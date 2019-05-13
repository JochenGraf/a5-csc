package a5.api

import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.index.IndexQuery
import a5.mappable.Mappable
import a5.odata.OdataCollection
import a5.odata.OdataEntity
import a5.com.OdataService
import a5.selector.Selector

class ApiQueryService extends OdataService {

    def elasticService

    String odataContext(String repository, String name) {
        "a5:/api/query/$repository"
    }

    String odataReadLink(String repository, String identifier, String name) {
        null
    }

    String odataMediaReadLink(String repository, String identifier, Mappable indexQuery) {
        null
    }

    ArrayList<String> SELECTORS = [
            "repository",
            "\$count",
            "\$filter",
            "\$orderby",
            "\$search",
            "\$select",
            "\$skip",
            "\$top",
            "autocomplete",
            "drill",
            "facets",
            "fields",
            "highlight",
            "pretty"]

    OdataCollection query(Map params) {
        Selector selector = new Selector(params, SELECTORS)
        ElasticQ query = new ElasticQ()
                .queryString(selector.search)
                .filter(selector.filter)
                .from(selector.skip)
                .size(selector.top)
                .sort(selector.orderby)
                .source(selector.select)
                .highlight(selector.highlight, (selector.filter != null || selector.fields != null))
                .fields(selector.fields)
                .facets(selector.facets)
                .postFilter(selector.drill)
        Map aggregationsAutocomplete = aggregationsAutocomplete(selector)
        ElasticResult elasticResultQuery = elasticService.read(selector.repository, Index.QUERY, query)
        ArrayList<OdataEntity> odataEntities = []
        elasticResultQuery.hits.eachWithIndex { it, idx ->
            IndexQuery indexQuery = new IndexQuery(it)
            odataEntities << new OdataEntity(
                    a5Highlight: elasticResultQuery.highlight[idx],
                    a5Orderby: elasticResultQuery.sort[idx],
                    index: indexQuery
            )
        }
        OdataCollection odata = new OdataCollection(
            odataContext: odataContext(selector.repository, ""),
            odataCount: elasticResultQuery.total,
            a5Selector: selector,
            entities: odataEntities,
            a5Facets: elasticResultQuery.aggregations,
            a5Autocomplete: aggregationsAutocomplete
        )
        odata
    }

    private Map aggregationsAutocomplete(Selector selector) {
        if (!selector.autocomplete) return
        String searchAnalyzed = elasticService.indexAnalyze(selector.repository, "standard", selector.search)
        ElasticQ queryAutocomplete = new ElasticQ()
                .autocomplete(selector.autocomplete, searchAnalyzed)
        elasticService.read(selector.repository, Index.QUERY, queryAutocomplete).aggregations
    }
}