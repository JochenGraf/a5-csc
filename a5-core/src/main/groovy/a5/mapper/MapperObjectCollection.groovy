package a5.mapper

import a5.index.IndexObject

trait MapperObjectCollection implements MapperObject {

    ArrayList<IndexObject> childObjects = []

    abstract ArrayList<String> mapParentOf()

    abstract ArrayList<IndexObject> mapChildObjects()

    abstract void parse(File file)

    ArrayList<IndexObject> map(File file) {
        super.map(file)
        this.object.objectType = "a5:Collection"
        this.object.parentOf = mapParentOf()
        this.childObjects = mapChildObjects()
        [this.object] + this.childObjects
    }
}