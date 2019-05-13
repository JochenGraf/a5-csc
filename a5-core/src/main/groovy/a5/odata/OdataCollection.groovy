package a5.odata

import a5.selector.Selector
import grails.converters.JSON

class OdataCollection extends Odata {

    String odataContext

    Integer odataCount

    Selector a5Selector

    ArrayList<OdataEntity> entities = []

    Map a5Facets

    Map a5Autocomplete

    private boolean hasMoreEntities() {
        odataCount && a5Selector.skip + a5Selector.top < odataCount
    }

    private String odataNextLink() {
        "?\$skip=${a5Selector.skip + a5Selector.top}"
    }

    static {
        JSON.registerObjectMarshaller(OdataCollection) {
            Map collection = [:]
            if (it.odataContext) collection["@odata.context"] = it.odataContext
            if (it.a5Selector?.count == true) collection["@odata.count"] = it.odataCount
            if (it.hasMoreEntities()) collection["@odata.nextLink"] = it.odataNextLink()
            if (it.a5Selector) collection["@a5.selector"] = it.a5Selector.asMap()
            collection.value = []
            it.entities.each {
                collection.value << it as JSON
            }
            if (it.a5Facets) collection["@a5.facets"] = it.a5Facets
            if (it.a5Autocomplete) collection["@a5.autocomplete"] = it.a5Autocomplete
            collection
        }
    }
}