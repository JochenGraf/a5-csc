package a5.api

import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.index.IndexObject
import a5.odata.OdataCollection
import a5.odata.OdataEntity
import a5.com.OdataService
import a5.selector.Selector

class ApiObjectService extends OdataService {

    def elasticService

    final ArrayList<String> SELECTORS_OBJECT = [
            "repository",
            "identifier",
            "\$expand",
            "pretty"
    ]

    final ArrayList<String> SELECTORS_OBJECTS = [
            "repository",
            "\$count",
            "\$filter",
            "\$orderby",
            "\$select",
            "\$skip",
            "\$top",
            "pretty"
    ]

    final ArrayList<String> EXPANDABLE = ["parentOf", "relatedTo"]

    final ArrayList<String> SYSTEM_SELECTOR = ["id", "fileExists", "fileUri"]

    OdataEntity object(Map params) {
        Selector selector = new Selector(params, SELECTORS_OBJECT)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.OBJECT, query)
        IndexObject indexObject = new IndexObject(elasticResult.firstHit)
        OdataEntity odataEntity = new OdataEntity(
                odataContext: odataContext(selector.repository, "Object"),
                odataId: selector.identifier,
                odataReadLink: odataReadLink(selector.repository, selector.identifier, "Object"),
                odataMediaReadLink: odataMediaReadLink(selector.repository, selector.identifier, indexObject),
                odataThumbnailMediaReadLink: odataThumbnailMediaReadLink(selector.repository, indexObject.id, indexObject),
                odataWaveformMediaReadLink: odataWaveformMediaReadLink(selector.repository, indexObject.id, indexObject),
                odataSpectrumMediaReadLink: odataSpectrumMediaReadLink(selector.repository, indexObject.id, indexObject),
                a5Selector: selector,
                a5Expandable: EXPANDABLE,
                index: indexObject
        )
        expand(odataEntity, Index.OBJECT, IndexObject.class, EXPANDABLE, "Object")
        odataEntity
    }

    OdataCollection objects(Map params){
        Selector selector = new Selector(params, SELECTORS_OBJECTS)
        ElasticQ query = new ElasticQ()
                .filter(selector.filter)
                .sort(selector.orderby)
                .from(selector.skip)
                .size(selector.top)
                .source(selector.select ? selector.select + SYSTEM_SELECTOR : selector.select)
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.OBJECT, query)
        ArrayList<OdataEntity> odataEntities = []
        IndexObject indexObject
        elasticResult.hits.each {
            indexObject = new IndexObject(it)
            odataEntities << new OdataEntity(
                odataId: indexObject.id,
                odataReadLink: odataReadLink(selector.repository, indexObject.id, "Object"),
                odataMediaReadLink: odataMediaReadLink(selector.repository, indexObject.id, indexObject),
                odataThumbnailMediaReadLink: odataThumbnailMediaReadLink(selector.repository, indexObject.id, indexObject),
                odataWaveformMediaReadLink: odataWaveformMediaReadLink(selector.repository, indexObject.id, indexObject),
                odataSpectrumMediaReadLink: odataSpectrumMediaReadLink(selector.repository, indexObject.id, indexObject),
                index: indexObject
            )
            SYSTEM_SELECTOR.each {
                if (selector.select && !selector.select.contains(it)) indexObject[it] = null
            }
        }
        OdataCollection odata = new OdataCollection(
            odataContext: odataContext(selector.repository, "Objects"),
            odataCount: elasticResult.total,
            a5Selector: selector,
            entities: odataEntities
        )
        odata
    }
}