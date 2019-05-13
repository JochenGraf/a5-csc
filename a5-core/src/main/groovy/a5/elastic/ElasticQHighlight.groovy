package a5.elastic

import a5.mappable.Mappable

class ElasticQHighlight extends Mappable {

    static Map parse(Boolean requireFieldMatch) {
        [
                "fields": [
                        "*": [
                                "require_field_match": "false"
                        ]
                ]
        ]
    }
}