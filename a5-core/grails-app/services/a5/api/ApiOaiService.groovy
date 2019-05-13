package a5.api

import a5.config.ConfigIndexOai
import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.index.IndexOaiItem
import a5.oai.OaiItemList
import a5.oai.OaiMetadataFormat
import a5.oai.OaiDataProviderService
import a5.oai.OaiPmh
import a5.oai.OaiRepository

class ApiOaiService implements OaiDataProviderService {

    def elasticService

    IndexOaiItem getItem(String repositoryName, String metadataPrefix, String identifier) {
        ElasticQ query = new ElasticQ().filter(["identifier", "eq", identifier])
        ElasticResult elasticResult = elasticService.read(repositoryName, Index.OAI, query)
        if (!elasticResult.firstHit) return null
        def indexOaiItem = new IndexOaiItem(elasticResult.firstHit)
        indexOaiItem
    }

    OaiItemList getItemList(String repositoryName, String metadataPrefix, Date from, Date until, Integer skip,
                            Integer top) {
        def filter = [metadataPrefix, "ne", "null"]
        if (from) filter += ["and", "datestamp", "ge", from.format(OaiPmh.DATE_FORMAT)]
        if (until) filter += ["and", "datestamp", "le", until.format(OaiPmh.DATE_FORMAT)]
        ElasticQ query = new ElasticQ().filter(filter).from(skip).size(top)
        ElasticResult elasticResult = elasticService.read(repositoryName, Index.OAI, query)
        if (!elasticResult.total) return null
        def oaiItemList = new OaiItemList(
                completeListSize: elasticResult.total
        )
        elasticResult.hits?.each {
            def indexOaiItem = new IndexOaiItem(it)
            oaiItemList.items << indexOaiItem
        }
        oaiItemList
    }

    OaiRepository getRepository(String repositoryName) {
        def configOai = new ConfigIndexOai(repositoryName)
        def repository = new OaiRepository()
        repository.repositoryName = configOai.parsed?.index?.oai?.repositoryName
        configOai.parsed?.index?.oai?.adminEmails?.adminEmail?.each {
            repository.adminEmails << it.text()
        }
        configOai.parsed?.index?.oai?.metadataFormats?.metadataFormat.each {
            def metadataFormat = new OaiMetadataFormat(
                    prefix: it.prefix.text(),
                    schema: it.schema.text(),
                    namespace: it.namespace.text()
            )
            repository.metadataFormats[(it.prefix.text())] = metadataFormat
        }
        repository
    }
}