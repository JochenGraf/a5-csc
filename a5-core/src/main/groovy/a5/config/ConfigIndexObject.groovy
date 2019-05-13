package a5.config

import a5.index.Index
import a5.index.IndexObject
import grails.util.Holders

class ConfigIndexObject extends ConfigIndex {

    ConfigIndexObject(String repositoryName) {
        super(repositoryName, Index.OBJECT)
        initFieldsGroovy([IndexObject.class])
        initMapperGroovy()
    }
}