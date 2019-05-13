package a5.elastic

import a5.config.ConfigIndexOai

class ElasticMappingOai extends ElasticMapping {

    ElasticMappingOai(ConfigIndexOai config) {
        this.initFieldsString(config)
    }
}