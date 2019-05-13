package a5.index

import a5.config.ConfigIndexAnnotation
import a5.config.ConfigIndexQuery
import a5.elastic.ElasticMappingAnnotation
import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.mapper.Mapper
import a5.mappable.Mappable

class IndexAnnotationService implements IndexTraitService {

    def elasticService
    def indexService

    private ArrayList<Mappable> associate(String repositoryName, ArrayList<Mappable> mapped, Map parent) {
        ElasticQ queryQuery = new ElasticQ()
                .filter(["id", "eq", parent.id])
        Map query = elasticService.read(repositoryName, Index.QUERY, queryQuery).firstHit
        Map _query = [:]
        def config = new ConfigIndexQuery(repositoryName)
        for (int i = 0; i < config.fields.size(); i++) {
            def field = config.fields[i]
            if (field.facet == true && query[(field.name)] != null) {
                _query[(field.name)] = query[(field.name)]
            }
        }
        mapped.each {
            if (it.type.contains("Annotation") || it.type.contains("AnnotationLayer")) {
                it._query = _query
            }
        }
        mapped
    }

    void mapFile(String repositoryName, String indexType, File file, String docBase, Mapper mapper) {
        ArrayList<Mappable> mapped
        ElasticResult parent
        try {
            ElasticQ queryObject = new ElasticQ()
                    .filter(["fileUri", "eq", file.toString() - docBase])
            Map object = elasticService.read(repositoryName, Index.OBJECT, queryObject).firstHit
            if (object == null) {
                indexService.progress(repositoryName,
                        "Error [$repositoryName,$indexType] IndexObject missing for file $file.")
                return
            }
            Map media
            if (object.relatedTo?.size() > 0) {
                ElasticQ queryMedia = new ElasticQ()
                    .filter(["id", "eq", object.relatedTo[0]])
                media = elasticService.read(repositoryName, Index.OBJECT, queryMedia).firstHit
            }
            ElasticQ queryParent = new ElasticQ()
                    .filter(["parentOf", "eq", object.id])
            parent = elasticService.read(repositoryName, Index.OBJECT, queryParent)
            if (parent == null) {
                indexService.progress(repositoryName, "Error [$repositoryName,$indexType] Parent IndexObject missing for file $file.")
                return
            }
            mapped = mapper.map(file, object, parent.firstHit, media)
            mapped = associate(repositoryName, mapped, parent.firstHit)
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
        def config = new ConfigIndexAnnotation(repositoryName)
        elasticService.indexMapping(repositoryName, Index.ANNOTATION, new ElasticMappingAnnotation(config))
    }

    void reindex(String repositoryName) {
        def config = new ConfigIndexAnnotation(repositoryName)
        if (config.isValid()) {
            fileSystemRead(config, Index.ANNOTATION, elasticService, indexService)
        } else {
            //TODO: throw exception
        }
    }
}