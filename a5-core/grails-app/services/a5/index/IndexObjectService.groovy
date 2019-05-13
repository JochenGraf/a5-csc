package a5.index

import a5.config.ConfigIndexObject
import a5.elastic.ElasticMappingObject
import a5.mapper.Mapper
import a5.mappable.Mappable

class IndexObjectService implements IndexTraitService {

    def elasticService

    def indexService

    void mapFile(String repositoryName, String indexType, File file, String docBase, Mapper mapper) {
        ArrayList<Mappable> mapped
        try {
            mapped = mapper.map(file)
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
        def config = new ConfigIndexObject(repositoryName)
        elasticService.indexMapping(repositoryName, Index.OBJECT, new ElasticMappingObject(config))
    }

    void reindex(String repositoryName) {
        def config = new ConfigIndexObject(repositoryName)
        if (config.isValid()) {
            fileSystemRead(config, Index.OBJECT, elasticService, indexService)
        } else {
            log.error "index config was invalid"
        }
    }
}