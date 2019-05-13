package a5.elastic

import a5.config.ConfigIndexQuery
import a5.index.Index

class ElasticMappingQuery extends ElasticMapping {

    ElasticMappingQuery(ConfigIndexQuery config) {
        this.initFieldsXquery(config)
    }
}