package a5.config

import a5.elastic.ElasticMappingFieldXquery
import a5.index.Index
import grails.util.Holders

class ConfigIndexQuery extends ConfigIndex {

    ArrayList<ElasticMappingFieldXquery> fields = []

    Map namespaces = [:]

    ConfigIndexQuery(String repositoryName) {
        super(repositoryName, Index.QUERY)
        initFields()
        initNamespaces()
        initMapper()
    }

    protected void initFields() {
        parsed.index.query.mapper."*".each {
            boolean isFulltext = true
            if (it.@fulltext.text() == "false") isFulltext = false
            boolean isFacet = false
            if (it.@facet.text() == "true") isFacet = true
            fields << new ElasticMappingFieldXquery(
                    name: it.name(),
                    xpath: it.text(),
                    fulltext: isFulltext,
                    facet: isFacet
            )
        }
    }

    private void initNamespaces() {
        parsed.index.query.namespace."*".each {
            namespaces[(it.name())] = it.text()
        }
    }

    private void initMapper() {
        mapperClass = Holders.grailsApplication.classLoader.loadClass("a5.mapper.MapperQuery")
        mapper = mapperClass.newInstance()
        fields.each {
            mapper.namedScripts[it.name] = it.xpath
        }
        mapper.namespaces = namespaces
    }
}