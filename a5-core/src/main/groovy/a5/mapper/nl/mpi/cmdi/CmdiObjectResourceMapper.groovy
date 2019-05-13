package a5.mapper.nl.mpi.cmdi

import a5.api.util.ApiUtil
import a5.mapper.MapperGroovy
import a5.mapper.MapperObjectResource
import groovy.util.slurpersupport.NodeChild

class CmdiObjectResourceMapper extends MapperGroovy implements MapperObjectResource {

    String mapId() {
        this.parsed.ResourceRef.text()
    }

    String mapLabel(){
        this.file.name
    }

    String mapContentType() {
        ApiUtil.getContentType(this.file)
    }

    ArrayList<String> mapRelatedTo() {
        ArrayList<String> related = []
        if (isEafResource()) {
            related = findRelatedMediaForEaf()
        }
        related.size() == 0 ? null : related
    }

    private boolean isEafResource() {
        ApiUtil.hasContentType(this.file, "text/eaf+xml")
    }

    private boolean isRelatedEafMedia(NodeChild related) {
        String eafFileName = this.file.toPath().getFileName().toString()
        String searchFileName = eafFileName - ~/\.eaf/
        String mediaUri = related.ResourceRef["@lat:localURI"].text()
        mediaUri.contains(searchFileName) && (ApiUtil.isAudio(mediaUri) || ApiUtil.isVideo(mediaUri))
    }

    private ArrayList<String> findRelatedMediaForEaf() {
        def related = []
        this.parsed.parent()."**".each {
            if (it.name() == "ResourceProxy") {
                if (isRelatedEafMedia(it)) {
                    related << it.ResourceRef.text()
                }
            }
        }
        related
    }
}