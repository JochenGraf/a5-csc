package a5.config

import a5.elastic.ElasticMappingFieldString
import a5.index.Index
import a5.xml.XmlProcessor

class ConfigIndexOai extends ConfigIndex {

    Map namespaces = [:]

    ConfigIndexOai(String repositoryName) {
        super(repositoryName, Index.OAI)
        initFields()
        initNamespaces()
        initMapper()
    }

    private void initFields() {
        ["identifier", "datestamp"].each {
            this.fields << new ElasticMappingFieldString(it)
        }
        parsed.index.oai.mapper."*".each {
            def field = new ElasticMappingFieldString(it.name())
            field.xpath = new File("$configFile.parentFile.absolutePath/${it.@script.text()}").text
            fields << field
        }
    }

    private void initNamespaces() {
        parsed.index.oai.metadataFormats.metadataFormat.each {
            namespaces[(it.prefix.text())] = it.namespace.text()
        }
    }

    private void initMapper() {
        mapperClass = a5.mapper.MapperOai.class
        mapper = mapperClass.newInstance()
        fields.each {
            if (it.xpath) {
                def cleanedScript = XmlProcessor.parse(it.xpath).serialize().toString()
                mapper.namedScripts[it.name] = cleanedScript
            }
        }
    }
}