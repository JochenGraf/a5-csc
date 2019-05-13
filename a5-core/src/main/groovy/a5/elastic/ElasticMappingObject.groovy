package a5.elastic

import a5.config.ConfigIndexObject

class ElasticMappingObject extends ElasticMapping {

    ElasticMappingObject(ConfigIndexObject config) {
        this.initFieldsGroovy(config)
    }
}