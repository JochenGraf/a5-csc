package a5.config

import a5.elastic.ElasticMappingField
import a5.elastic.ElasticMappingFieldGroovy
import a5.mapper.Mapper
import grails.util.Holders
import java.util.regex.Pattern

class ConfigIndex extends Config {

    ArrayList<ElasticMappingField> fields = []

    Pattern fileFilter

    Pattern fileFilterExclude

    String mapperType

    Class mapperClass

    Mapper mapper

    String indexType

    ConfigIndex(String repositoryName, String indexType) {
        super(repositoryName)
        this.indexType = indexType
        this.fileFilter = ~this.parsed.index[indexType].fileFilter.text()
        this.fileFilterExclude = this.parsed.index[indexType].fileFilter.@exclude.size() == 1 ?
                ~this.parsed.index[indexType].fileFilter.@exclude.text() : null
        this.mapperType = this.parsed.index[indexType].mapper.@type.text()
    }

    protected void initFieldsGroovy(ArrayList<Class> indexClass) {
        indexClass.each { clazz ->
            clazz.declaredFields.findAll { !it.synthetic }.each {
                fields << new ElasticMappingFieldGroovy(it)
            }
        }
    }

    protected initMapperGroovy() {
        mapperClass = Holders.grailsApplication.classLoader.loadClass(parsed.index[indexType].mapper.text())
        mapper = mapperClass.newInstance()
        mapper.config = this
    }

    boolean isValid() {
        super.isValid()
    }
}