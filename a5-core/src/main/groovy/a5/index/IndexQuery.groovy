package a5.index

import a5.mappable.Mappable

class IndexQuery extends Mappable {

    Map fields = [:]

    Map json

    IndexQuery() {}

    IndexQuery(Map json) {
        this.json = json
    }

    Map asMap() {
        this.json ?: this.fields
    }
}