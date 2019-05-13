package a5.mapper.de.unikoeln.dch.blam

import a5.api.util.ApiUtil
import a5.mapper.MapperGroovy
import a5.mapper.MapperObjectResource

class BlamObjectResourceMapper extends MapperGroovy implements MapperObjectResource {

    String mapId() {
        this.parsed.FilePID.text()
    }

    String mapLabel() {
        this.parsed.FileName.text()
    }

    String mapContentType() {
        ApiUtil.getContentType(file)
    }

    ArrayList<String> mapRelatedTo() {
        def relatedTo = this.parsed.IsAnnotationOf.text()
        relatedTo ? [relatedTo] : null
    }
}