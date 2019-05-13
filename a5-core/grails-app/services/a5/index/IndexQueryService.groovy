package a5.index

import a5.config.ConfigIndexQuery
import a5.elastic.ElasticMappingQuery
import a5.elastic.ElasticQ
import a5.mapper.Mapper
import a5.mappable.Mappable

class IndexQueryService implements IndexTraitService {

    def elasticService
    def indexService

    void mapFile(String repositoryName, String indexType, File file, String docBase, Mapper mapper) {
        ArrayList<Mappable> mapped
        try {
            ElasticQ queryObject = new ElasticQ().filter(["fileUri", "eq", file.toString() - docBase])
            mapped = mapper.map(file)
        } catch(any) {
            indexService.progress(repositoryName, "Error [$repositoryName,$indexType] ${any.toString()}")
        }
        if (mapped == null) {
            indexService.progress(repositoryName, "Error [$repositoryName,$indexType] Mapping failed for file $file.")
        } else {
            indexService.progress(repositoryName, "Indexing [$repositoryName,$indexType] $file")
            elasticService.create(repositoryName, indexType, mapped[0])
        }
    }

    void mapping(String repositoryName) {
        def config = new ConfigIndexQuery(repositoryName)
        elasticService.indexMapping(repositoryName, Index.QUERY, new ElasticMappingQuery(config))
    }

    void reindex(String repositoryName) {
        def config = new ConfigIndexQuery(repositoryName)
        if (config.isValid()) {
            fileSystemRead(config, Index.QUERY, elasticService, indexService)
        } else {
            //TODO: throw exception
        }
    }
}