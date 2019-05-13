package a5.elastic

import a5.config.ConfigIndex
import a5.config.ConfigIndexQuery
import grails.converters.JSON

abstract class ElasticMapping {

    static final Map BOOLEAN_TYPE = [
            "type": "boolean",
            "fields": [
                    "raw": ["type": "boolean"]
            ]
    ]

    static final Map FLOAT_TYPE = [
            "type": "float",
            "fields": [
                    "raw": ["type": "float"]
            ]
    ]

    static final Map INTEGER_TYPE = [
            "type": "integer",
            "fields": [
                    "raw": ["type": "integer"]
            ]
    ]

    static final Map LONG_TYPE = [
            "type": "long",
            "fields": [
                    "raw": ["type": "long"]
            ]
    ]

    static final Map NESTED_TYPE = [
            "type": "nested",
            "include_in_parent": true,
            "properties": [:]
    ]

    static final Map OBJECT_TYPE = [:]

    static final Map STRING_TYPE = [
            "type": "string",
            "fields": [
                    "raw": ["type": "string", "index": "not_analyzed"]
            ]
    ]

    static final Map SIMPLE_TYPE = [
            "class java.lang.Boolean": BOOLEAN_TYPE,
            "class java.lang.Float": FLOAT_TYPE,
            "class java.lang.Integer": INTEGER_TYPE,
            "class java.lang.Long": LONG_TYPE,
            "class java.lang.String": STRING_TYPE,
            "java.util.ArrayList<java.lang.Boolean>": BOOLEAN_TYPE,
            "java.util.ArrayList<java.lang.Float>": FLOAT_TYPE,
            "java.util.ArrayList<java.lang.Integer>": INTEGER_TYPE,
            "java.util.ArrayList<java.lang.Long>": LONG_TYPE,
            "java.util.ArrayList<java.lang.String>": STRING_TYPE
    ]

    final static String FIELD_AUTOCOMPLETE = "_autocomplete"

    static Map FULLTEXT_TYPE = [
            "type": "string",
            "analyzer": ElasticSettingsQuery.ANALYZER_FULLTEXT,
            "copy_to": FIELD_AUTOCOMPLETE,
            "fields": [
                    "raw": ["type": "string", "index": "not_analyzed"]
            ]
    ]

    static Map AUTOCOMPLETE_TYPE = [
            "type": "string",
            "analyzer": ElasticSettingsQuery.ANALYZER_AUTOCOMPLETE
    ]

    Map fields = [:]

    Map parent

    private Map simpleType(ElasticMappingFieldGroovy field) {
        SIMPLE_TYPE[field.typeName]
    }

    private Map complexType(ElasticMappingFieldGroovy field) {
        Map type = OBJECT_TYPE
        type.properties = [:]
        field.clazz.declaredFields.findAll { !it.synthetic }.each {
            ElasticMappingFieldGroovy child = new ElasticMappingFieldGroovy(it)
            type.properties << [(it.name): getElasticType(child)]
        }
        type
    }

    private Map nestedType(ElasticMappingFieldGroovy field) {
        Map type = NESTED_TYPE
        field.clazz.declaredFields.findAll { !it.synthetic }.each {
            ElasticMappingFieldGroovy child = new ElasticMappingFieldGroovy(it)
            type.properties << [(it.name): getElasticType(child)]
        }
        type
    }

    protected Map getElasticType(ElasticMappingFieldGroovy field) {
        Map type
        if (field.isSimpleType()) {
            type = simpleType(field)
        } else if (field.isComplexType()) {
            type = complexType(field)
        } else if (field.isNestedType()) {
            type = nestedType(field)
        } else {
        }
        type
    }

    protected Map getElasticType(ElasticMappingFieldXquery field) {
        field.fulltext == false ? STRING_TYPE : FULLTEXT_TYPE
    }

    protected initFieldsGroovy(ConfigIndex config) {
        for (int i = 0; i < config.fields.size(); i++) {
            String name = config.fields[i].name
            if (config.fields[i].isXqueryType()) {
                this.fields[(name)] = [:]
                this.fields[(name)].properties = [:]
                initFieldsXquery(new ConfigIndexQuery(config.repositoryName), this.fields[(name)].properties)
            } else {
                Map elasticType = this.getElasticType(config.fields[i])
                this.fields[(name)] = elasticType
            }
        }
    }

    protected initFieldsString(ConfigIndex config) {
        config.fields.each {
            fields[it.name] = STRING_TYPE
        }
    }

    protected initFieldsXquery(ConfigIndex config, Map base) {
        for (int i = 0; i < config.fields.size(); i++) {
            Map elasticType = this.getElasticType(config.fields[i])
            String name = config.fields[i].name
            base[(name)] = elasticType
        }
    }

    protected initFieldsXquery(ConfigIndex config) {
        for (int i = 0; i < config.fields.size(); i++) {
            Map elasticType = this.getElasticType(config.fields[i])
            String name = config.fields[i].name
            this.fields[(name)] = elasticType
        }
        this.fields[(FIELD_AUTOCOMPLETE)] = AUTOCOMPLETE_TYPE
    }

    static {
        JSON.registerObjectMarshaller(ElasticMapping) {
            Map mapping = [:]
            if (it.parent != null) mapping["_parent"] = it.parent
            mapping["properties"] = it.fields
            mapping
        }
    }
}