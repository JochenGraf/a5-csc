package a5.index

import a5.config.ConfigIndexOai
import a5.elastic.ElasticMappingOai
import a5.elastic.ElasticQ
import a5.mapper.Mapper
import a5.mappable.Mappable

class IndexOaiService implements IndexTraitService {

    def elasticService

    def indexService

    void mapFile(String repositoryName, String indexType, File file, String docBase, Mapper mapper) {
        ArrayList<Mappable> mapped
        try {
            ElasticQ queryObject = new ElasticQ().filter(["fileUri", "eq", file.toString() - docBase])
            Map object = elasticService.read(repositoryName, Index.OBJECT, queryObject).firstHit
            if (object == null) {
                indexService.progress(repositoryName,
                        "Error [$repositoryName,$indexType] IndexObject missing for file $file.")
                return
            }
            mapped = mapper.map(file, object)
        } catch(any) {
            indexService.progress(repositoryName, "Error [$repositoryName,$indexType] ${any.toString()}")
        }
        if (mapped == null) {
            indexService.progress(repositoryName, "Error [$repositoryName,$indexType] Mapping failed for file $file.")
        } else {
            indexService.progress(repositoryName, "Indexing [$repositoryName,$indexType] $file")
            elasticService.create(repositoryName, indexType, mapped)
        }
    }

    void mapping(String repositoryName) {
        def config = new ConfigIndexOai(repositoryName)
        elasticService.indexMapping(repositoryName, Index.OAI, new ElasticMappingOai(config))
    }

    void reindex(String repositoryName) {
        def config = new ConfigIndexOai(repositoryName)
        if (config.isValid()) {
            fileSystemRead(config, Index.OAI, elasticService, indexService)
        } else {
            log.error "index config was invalid"
        }
    }
}