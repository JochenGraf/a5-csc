package a5.index

import a5.mappable.Mappable
import a5.oai.OaiItem

class IndexOaiItem extends Mappable implements OaiItem {

    IndexOaiItem() {}

    IndexOaiItem(Map json) {
        json.each { key, value ->
            if (key == "identifier") {
                identifier = value
            } else if (key == "datestamp") {
                datestamp = value
            } else {
                metadata[key] = value
            }
        }
    }

    Map asMap() {
        [
            identifier: identifier,
            datestamp: datestamp,
        ] + metadata
    }
}