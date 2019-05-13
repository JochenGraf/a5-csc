package a5.elastic

class ElasticQFacets {

    static Map parse(Map facets) {
        Map terms = [:]
        facets.each { k, v ->
            terms[(k)] = [
                    "terms": [
                            "field": "${k}.raw",
                            "size": v
                    ]
            ]
        }
        terms
    }
}