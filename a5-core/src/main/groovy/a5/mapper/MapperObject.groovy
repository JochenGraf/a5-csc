package a5.mapper

import a5.config.ConfigIndexObject
import a5.index.IndexObject
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.Files

trait MapperObject {

    ConfigIndexObject config

    IndexObject object

    File file

    BasicFileAttributes fileAttributes

    abstract String mapId()

    abstract String mapLabel()

    abstract String mapContentType()

    String mapFileUri() {
        this.file.toPath().normalize().toString() - this.config.docBase
    }

    Boolean mapFileExists() {
        this.file.exists()
    }

    String mapFileUpdated() {
        this.file.exists() ? this.fileAttributes.lastModifiedTime() : null
    }

    String mapFileCreated() {
        this.file.exists() ? this.fileAttributes.creationTime() : null
    }

    Long mapFileSize() {
        this.file.exists() ? this.file.length() : null
    }

    abstract ArrayList<String> mapRelatedTo()

    ArrayList<IndexObject> map(File file) {
        this.file = file
        this.fileAttributes = this.file.exists() ? Files.readAttributes(this.file.toPath(), BasicFileAttributes) : null
        this.parse(file)
        this._map()
    }

    ArrayList<IndexObject> map() {
        this.fileAttributes = this.file.exists() ? Files.readAttributes(this.file.toPath(), BasicFileAttributes) : null
        this._map()
    }

    ArrayList<IndexObject> _map() {
        this.object = new IndexObject()
        this.object.id = mapId()
        this.object.label = mapLabel()
        this.object.contentType = mapContentType()
        this.object.fileUri = mapFileUri()
        this.object.fileExists = mapFileExists()
        this.object.fileUpdated = mapFileUpdated()
        this.object.fileCreated = mapFileCreated()
        this.object.fileSize = mapFileSize()
        this.object.relatedTo = mapRelatedTo()
    }
}