package a5.api

import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.index.IndexAnnotation
import a5.index.IndexAnnotationLayer
import a5.index.IndexAnnotationCanvas
import a5.index.IndexAnnotationManifest
import a5.index.IndexObject
import a5.odata.OdataCollection
import a5.odata.OdataEntity
import a5.com.OdataService
import a5.selector.Selector
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApiAnnotationService extends OdataService {
    protected Logger log = LoggerFactory.getLogger(getClass())

    String odataReadLink(String repository, String identifier, String name) {
        "/api/annotation/$repository/$name($identifier)"
    }

    def elasticService

    private static ArrayList<String> SELECTORS_MANIFEST = [
            "repository",
            "identifier",
            "\$expand",
            "pretty"
    ]

    private static ArrayList<String> EXPANDABLE_MANIFEST = ["within", "members"]

    OdataEntity manifest(Map params) {
        Selector selector = new Selector(params, SELECTORS_MANIFEST)
        ElasticQ query = new ElasticQ()
                .filter(["id", "eq", selector.identifier, "and", "type", "eq", "Manifest"])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        IndexAnnotationManifest manifest = new IndexAnnotationManifest(elasticResult.firstHit)
        OdataEntity odata = new OdataEntity(
                odataContext: odataContext(selector.repository, "Manifest"),
                odataReadLink: odataReadLink(selector.repository, selector.identifier, "Manifest"),
                a5Selector: selector,
                a5Expandable: EXPANDABLE_MANIFEST,
                index: manifest
        )
        expand(odata, Index.ANNOTATION, IndexAnnotationCanvas.class, ["members"], "Canvas")
        expand(odata, Index.OBJECT, IndexObject.class, ["within"], "Object")
        odata
    }

    private static ArrayList<String> SELECTORS_CANVAS = [
            "repository",
            "identifier",
            "\$expand",
            "pretty"
    ]

    private static ArrayList<String> EXPANDABLE_CANVAS = ["media"]

    OdataEntity canvas(Map params) {
        log.debug params.toString()
        Selector selector = new Selector(params, SELECTORS_CANVAS)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        IndexAnnotationCanvas canvas = new IndexAnnotationCanvas(elasticResult.firstHit)
        OdataEntity odata = new OdataEntity(
                odataContext: odataContext(selector.repository, "Canvas"),
                odataReadLink: odataReadLink(selector.repository, selector.identifier, "Canvas"),
                a5Selector: selector,
                a5Expandable: EXPANDABLE_CANVAS,
                index: canvas
        )
        expand(odata, Index.OBJECT, IndexObject.class, EXPANDABLE_CANVAS, "Object")
        odata
    }

    private static ArrayList<String> SELECTORS_LAYER = [
            "repository",
            "identifier",
            "pretty"
    ]

    OdataEntity layer(Map params) {
        Selector selector = new Selector(params, SELECTORS_LAYER)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        IndexAnnotationLayer layer = new IndexAnnotationLayer(elasticResult.firstHit)
        OdataEntity odata = new OdataEntity(
                odataContext: odataContext(selector.repository, "AnnotationLayer"),
                odataReadLink: odataReadLink(selector.repository, selector.identifier, "AnnotationLayer"),
                a5Selector: selector,
                index: layer
        )
        odata
    }

    private static ArrayList<String> SELECTORS_LAYERS = [
            "repository",
            "\$search",
            "\$filter",
            "\$skip",
            "\$top",
            "\$count",
            "pretty"
    ]

    OdataCollection layers(Map params) {
        Selector selector = new Selector(params, SELECTORS_LAYERS)
        ArrayList<String> filter = ["(", "type", "eq", "AnnotationLayer", ")"]
        if (selector.filter) {
            filter += ["and", "("]
            filter += selector.filter
            filter << ")"
        }
        ElasticQ query = new ElasticQ()
                .queryString(selector.search)
                .fields(["resources.bodyValue"])
                .filter(filter)
                .from(selector.skip)
                .size(selector.top)
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        ArrayList<OdataEntity> entities = []
        elasticResult.hits.eachWithIndex { it, idx ->
            IndexAnnotationLayer annotationLayer = new IndexAnnotationLayer(it)
            entities << new OdataEntity(
                    odataId: annotationLayer.id,
                    odataReadLink: odataReadLink(selector.repository, annotationLayer.id, "Layer"),
                    index: annotationLayer
            )
        }
        OdataCollection odata = new OdataCollection(
                odataContext: "a5:/api/annotation/$selector.repository/Layers",
                odataCount: elasticResult.total,
                a5Selector: selector,
                entities: entities
        )
        odata
    }

    private static ArrayList<String> SELECTORS_ANNOTATION = [
            "repository",
            "identifier",
            "pretty"
    ]

    OdataEntity annotation(Map params) {
        Selector selector = new Selector(params, SELECTORS_ANNOTATION)
        ElasticQ query = new ElasticQ()
                .filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        IndexAnnotation annotation = new IndexAnnotation(elasticResult.firstHit)
        OdataEntity odata = new OdataEntity(
                odataContext: odataContext(selector.repository, "Annotation"),
                odataReadLink: odataReadLink(selector.repository, selector.identifier, "Annotation"),
                a5Selector: selector,
                index: annotation
        )
        odata
    }

    private static ArrayList<String> SELECTORS_ANNOTATIONS = [
            "repository",
            "\$search",
            "\$filter",
            "\$skip",
            "\$top",
            "\$count",
            "\$orderby",
            "drill",
            "facets",
            "highlight",
            "pretty"
    ]

    OdataCollection annotations(Map params) {
        Selector selector = new Selector(params, SELECTORS_ANNOTATIONS)
        ArrayList<String> filter = ["type", "eq", "Annotation"]
        if (selector.filter) {
            filter += ["and", "("]
            filter += selector.filter
            filter << ")"
        }
        ElasticQ query = new ElasticQ()
                .queryString(selector.search)
                .fields(["bodyValue"])
                .filter(filter)
                .from(selector.skip)
                .size(selector.top)
                .sort(selector.orderby)
                .facets(selector.facets)
                .highlight(selector.highlight, true)
                .postFilter(selector.drill)
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.ANNOTATION, query)
        ArrayList<OdataEntity> entities = []
        elasticResult.hits.eachWithIndex { it, idx ->
            IndexAnnotation annotation = new IndexAnnotation(it)
            //annotation._query = null
            entities << new OdataEntity(
                    odataId: annotation.id,
                    odataReadLink: odataReadLink(selector.repository, annotation.id, "Annotation"),
                    a5Highlight: elasticResult.highlight[idx],
                    index: annotation
            )
        }
        OdataCollection odata = new OdataCollection(
                odataContext: "a5:/api/annotation/$selector.repository/Annotations",
                odataCount: elasticResult.total,
                a5Selector: selector,
                entities: entities,
                a5Facets: elasticResult.aggregations,
        )
        odata
    }
}