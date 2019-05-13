package a5.api

import a5.config.Config
import a5.config.ConfigIndexQuery
import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.index.IndexQuery
import a5.mapper.Mapper
import a5.odata.OdataEntity
import a5.com.OdataService
import a5.selector.Selector

class ApiAdminService extends OdataService {

    def elasticService

    def grailsApplication

    private ArrayList<String> selectorConfig = [
            "repository",
            "pretty"
    ]

    OdataEntity config(Map params) {
        Selector selector = new Selector(params, selectorConfig)
        Map config = grailsApplication.config.a5.repository[selector.repository]
        OdataEntity odataEntity = new OdataEntity(
                odataContext: "a5:/api/admin/$selector.repository/config",
                a5Selector: selector,
                map: config
        )
        odataEntity
    }

    private ArrayList<String> selectorConfigIndex = [
            "repository",
            "api",
            "pretty"
    ]

    private convertToMap(nodes) {
        nodes.children().collectEntries {
            [
                    it.name(),
                    it.childNodes() ? convertToMap(it) : it.text().trim().replaceAll("\\s", "")
            ]
        }
    }

    OdataEntity configIndex(Map params) {
        Selector selector = new Selector(params, selectorConfigIndex)
        Config config = new Config(selector.repository)
        def node = config.parsed.index[selector.api]
        OdataEntity odataEntity = new OdataEntity(
                odataContext: "a5:/api/admin/$selector.repository/config/$selector.api",
                a5Selector: selector,
                map: convertToMap(node)
        )
        odataEntity
    }

    private ArrayList<String> selectorMappingQuery = [
            "repository",
            "prefix",
            "identifier",
            "pretty"
    ]

    OdataEntity mappingQuery(Map params) {
        Selector selector = new Selector(params, selectorMappingQuery)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.OBJECT, query)
        String fileUri = grailsApplication.config.a5.repository[selector.repository].docBase + elasticResult.firstHit?.fileUri
        ConfigIndexQuery config = new ConfigIndexQuery(selector.repository)
        Mapper mapper = config.getMapper().newInstance()
        mapper.init(config)
        ArrayList<IndexQuery> indexQuery = mapper.map(new File(fileUri))
        OdataEntity odataEntity = new OdataEntity(
                odataContext: "a5:/api/admin/$selector.repository/mapping/query",
                odataId: selector.identifier,
                a5Selector: selector,
                index: indexQuery[0]
        )
        odataEntity
    }
}