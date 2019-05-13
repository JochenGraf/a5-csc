package a5.elastic

import a5.config.ConfigIndexAnnotation
import a5.index.Index

class ElasticMappingAnnotation extends ElasticMapping {

    ElasticMappingAnnotation(ConfigIndexAnnotation config) {
        this.initFieldsGroovy(config)
    }
}