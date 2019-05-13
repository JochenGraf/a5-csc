package a5.mapper.de.unikoeln.dch.imdi

import a5.index.IndexObject
import a5.mapper.MapperGroovy
import a5.mapper.MapperObjectCollection
import groovy.util.slurpersupport.NodeChild

class ImdiObjectCollectionMapper extends MapperGroovy implements MapperObjectCollection {

    String mapId() {
        this.parsed.@ArchiveHandle.text()
    }

    String mapLabel(){
        String label1 = this.parsed."*".ProjectDisplayName.text()
        String label2 = this.parsed."*".Title.text()
        label1 ?: label2
    }

    String mapContentType() {
        "text/imdi+xml"
    }

    ArrayList<String> mapParentOf() {
        ArrayList<String> parentOf = []
        this.parsed.Corpus.CorpusLink.each {
            parentOf << it.@ArchiveHandle.text()
        }
        this.parsed."*".Resources."*".ResourceLink.each {
            parentOf << it.@ArchiveHandle.text()
        }
        parentOf.size() == 0 ? null : parentOf
    }

    ArrayList<String> mapRelatedTo() {
        null
    }

    ArrayList<IndexObject> mapChildObjects() {
        ArrayList<IndexObject> childs = []
        String parent = this.file.toPath().getParent().toString()
        this.parsed."*".Resources."*".ResourceLink.each {
            if (!(isImdiResource(it))) {
                def mapper = new ImdiObjectResourceMapper()
                mapper.parsed = it
                mapper.file = new File("$parent/${it.text()}")
                mapper.config = config
                childs += mapper.map()
            }
        }
        childs
    }

    private boolean isImdiResource(NodeChild node) {
        node.text() =~ this.config.getFileFilter()
    }
}