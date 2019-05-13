package a5.elastic

import grails.converters.JSON

abstract class ElasticSettings {

    Map filters = [:]

    Map analyzers = [:]

    static {
        JSON.registerObjectMarshaller(ElasticSettingsQuery) {
            [
                    "settings": [
                            "analysis": [
                                    "filter": it.filters,
                                    "analyzer": it.analyzers
                            ]
                    ]
            ]
        }
    }
}