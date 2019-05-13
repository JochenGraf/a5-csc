package a5.mapper.nl.mpi.cmdi

import a5.index.IndexObject
import a5.mapper.MapperGroovy
import a5.mapper.MapperObjectCollection

class CmdiObjectCollectionMapper extends MapperGroovy implements MapperObjectCollection {

    String mapId() {
        this.parsed.Header.MdSelfLink.text()
    }

    String mapLabel(){
        this.parsed.Components."*".Title.text()
    }

    String mapContentType() {
        "text/cmdi+xml"
    }

    ArrayList<String> mapParentOf() {
        ArrayList<String> parentOf = []
        this.parsed.Resources.ResourceProxyList.ResourceProxy.each {
            parentOf << it.ResourceRef.text()
        }
        parentOf.size() == 0 ? null : parentOf
    }

    ArrayList<String> mapRelatedTo() {
        null
    }

    ArrayList<IndexObject> mapChildObjects() {
        ArrayList<IndexObject> childs = []
        String parent = this.file.toPath().getParent().toString()
        this.parsed.Resources.ResourceProxyList.ResourceProxy.each {
            if (it.ResourceType.text() == "Resource") {
                def mapper = new CmdiObjectResourceMapper()
                mapper.parsed = it
                mapper.file = new File("$parent/${it.ResourceRef['@lat:localURI'].text()}")
                mapper.config = config
                childs += mapper.map()
            }
        }
        childs
    }
}