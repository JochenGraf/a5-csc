package a5.com

import a5.api.util.ApiUtil
import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.mappable.Mappable
import a5.odata.OdataEntity
import grails.converters.JSON

abstract class OdataService {

    protected String odataContext(String repository, String name) {
        "a5:/api/object/$repository/$name"
    }

    protected String odataReadLink(String repository, String identifier, String name) {
        "/api/object/$repository/$name($identifier)"
    }

    protected String odataMediaReadLink(String repository, String identifier, Mappable indexObject) {
        indexObject.properties.fileExists && indexObject.fileExists ? "/api/media/$repository/$identifier" : null
    }

    private String odataThumbnailMediaReadLink(String repository, String identifier, Mappable index) {
        if (!(index instanceof a5.index.IndexObject)) return null
        if (!index.fileExists) return null
        if (ApiUtil.isImage(index.fileUri)) {
            "/api/media/$repository/$identifier/full/full/{size}/0/none/default.jpg"
        } else if (ApiUtil.isVideo(index.fileUri)) {
            "/api/media/$repository/$identifier/1,0/full/{size}/0/thumbnail/default.jpg"
        } else {
            null
        }
    }

    private String odataWaveformMediaReadLink(String repository, String identifier, Mappable index) {
        if (!(index instanceof a5.index.IndexObject)) return null
        if (!index.fileExists) return null
        if (ApiUtil.isAudio(index.fileUri) || ApiUtil.isVideo(index.fileUri)) {
            "/api/media/$repository/$identifier/{section}/full/{size}/0/waveform/default.png"
        } else {
            null
        }
    }

    private String odataSpectrumMediaReadLink(String repository, String identifier, Mappable index) {
        if (!(index instanceof a5.index.IndexObject)) return null
        if (!index.fileExists) return null
        if (ApiUtil.isAudio(index.fileUri) || ApiUtil.isVideo(index.fileUri)) {
            "/api/media/$repository/$identifier/{section}/full/{size}/0/spectrum/default.png"
        } else {
            null
        }
    }

    private void expandArray(String field, OdataEntity entity, String indexType, mappable, String name) {
        if (entity.index[field]?.size() > 0) {
            ArrayList<String> filter = []
            entity.index[field].each {
                filter += ["or", "id", "eq", it]
            }
            ElasticQ query = new ElasticQ().filter(filter).size(entity.index[field].size())
            ElasticResult elasticResult = elasticService.read(entity.a5Selector.repository, indexType, query)
            ArrayList<Map> expanded = []
            elasticResult.hits.eachWithIndex { hit, idx ->
                Mappable index = mappable.newInstance(hit)
                expanded << new OdataEntity(
                        odataId: index.id,
                        odataReadLink: odataReadLink(entity.a5Selector.repository, index.id, name),
                        odataMediaReadLink: odataMediaReadLink(entity.a5Selector.repository, index.id, index),
                        odataThumbnailMediaReadLink: odataThumbnailMediaReadLink(entity.a5Selector.repository, index.id, index),
                        odataWaveformMediaReadLink: odataWaveformMediaReadLink(entity.a5Selector.repository, index.id, index),
                        odataSpectrumMediaReadLink: odataSpectrumMediaReadLink(entity.a5Selector.repository, index.id, index),
                        index: index
                ) as JSON

            }
            entity.index[field] = expanded
        }
    }

    private void expandField(String field, OdataEntity entity, String indexType, mappable, String name) {
        if (entity.index[field] != "") {
            ArrayList<String> filter = ["id", "eq", entity.index[field]]
            ElasticQ query = new ElasticQ().filter(filter).size(1)
            ElasticResult elasticResult = elasticService.read(entity.a5Selector.repository, indexType, query)
            Mappable index = mappable.newInstance(elasticResult.firstHit)
            def expanded = new OdataEntity(
                    odataId: index.id,
                    odataReadLink: odataReadLink(entity.a5Selector.repository, index.id, name),
                    odataMediaReadLink: odataMediaReadLink(entity.a5Selector.repository, index.id, index),
                    odataThumbnailMediaReadLink: odataThumbnailMediaReadLink(entity.a5Selector.repository, index.id, index),
                    odataWaveformMediaReadLink: odataWaveformMediaReadLink(entity.a5Selector.repository, index.id, index),
                    odataSpectrumMediaReadLink: odataSpectrumMediaReadLink(entity.a5Selector.repository, index.id, index),
                    index: index
            )
            entity.index[field] = expanded
        }
    }

    protected void expand(OdataEntity entity, String indexType, Class mappable, ArrayList<String> expandable,
                          String name) {
        if (entity.a5Selector.expand == null) return
        entity.a5Selector.expand.each { field ->
            if (expandable.contains(field)) {
                if (!entity.index[field]) {
                } else if (entity.index[field] instanceof ArrayList) {
                    expandArray(field, entity, indexType, mappable, name)
                } else {
                    expandField(field, entity, indexType, mappable, name)
                }
            }
        }
    }
}