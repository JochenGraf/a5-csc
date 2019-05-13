package a5.elastic

import a5.elastic.ElasticMappingQuery

class ElasticQAutocomplete {

    static Map query(String prefix) {
        [
                "prefix": [
                        (ElasticMappingQuery.FIELD_AUTOCOMPLETE): [
                                "value": "$prefix"
                        ]
                ]
        ]
    }

    static Map aggregations(String prefix) {
        [
                "autocomplete": [
                        "terms": [
                                "field": (ElasticMappingQuery.FIELD_AUTOCOMPLETE),
                                "order": [
                                        "_count": "desc"
                                ],
                                "include": [
                                        "pattern": "$prefix.*"
                                ]
                        ]
                ]
        ]
    }
}