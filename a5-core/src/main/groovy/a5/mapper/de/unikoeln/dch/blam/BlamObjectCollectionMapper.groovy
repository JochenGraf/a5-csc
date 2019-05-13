package a5.mapper.de.unikoeln.dch.blam

import a5.index.IndexObject
import a5.mapper.MapperObjectCollection
import a5.mapper.MapperGroovy

class BlamObjectCollectionMapper extends MapperGroovy implements MapperObjectCollection {

    String mapId() {
        this.parsed.Header.MdSelfLink.text()
    }

    String mapLabel(){
        def label1 = this.parsed.Components."*".CollectionGeneralInfo.CollectionDisplayTitle.text()
        def label2 = this.parsed.Components."*".BundleGeneralInfo.BundleDisplayTitle.text()
        label1 ?: label2
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
        this.parsed.Components."*".BundleStructuralInfo.BundleResources."*".each {
            def mapper = new BlamObjectResourceMapper()
            mapper.parsed = it
            mapper.file = new File("$parent/Resources/${it.FileName.text()}")
            mapper.config = config
            childs += mapper.map()
        }
        childs
    }
}