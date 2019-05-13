package a5.odata

import a5.mappable.Mappable
import a5.selector.Selector
import grails.converters.JSON

class OdataEntity extends Odata {

    String odataContext

    String odataId

    String odataReadLink

    String odataMediaReadLink

    String odataThumbnailMediaReadLink

    String odataWaveformMediaReadLink

    String odataSpectrumMediaReadLink

    Selector a5Selector

    ArrayList<String> a5Expandable

    Map a5Highlight

    ArrayList<String> a5Orderby

    Mappable index

    Map map

    static {
        JSON.registerObjectMarshaller(OdataEntity) {
            Map entity = [:]
            if (it.odataContext) entity["@odata.context"] = it.odataContext
            if (it.odataId) entity["@odata.id"] = it.odataId
            if (it.odataReadLink) entity["@odata.readLink"] = it.odataReadLink
            if (it.odataMediaReadLink) entity["@odata.mediaReadLink"] = it.odataMediaReadLink
            if (it.odataThumbnailMediaReadLink) entity["Thumbnail@odata.mediaReadLink"] = it.odataThumbnailMediaReadLink
            if (it.odataWaveformMediaReadLink) entity["Waveform@odata.mediaReadLink"] = it.odataWaveformMediaReadLink
            if (it.odataSpectrumMediaReadLink) entity["Spectrum@odata.mediaReadLink"] = it.odataSpectrumMediaReadLink
            if (it.a5Selector) entity["@a5.selector"] = it.a5Selector.asMap()
            it.a5Expandable?.each { field ->
                if (it.index.properties[field] != null) {
                    if (entity["@a5.expandable"] == null) entity["@a5.expandable"] = []
                    entity["@a5.expandable"] << field
                }
            }
            if (it.a5Highlight) entity["@a5.highlight"] = it.a5Highlight
            if (it.a5Orderby) entity["@a5.orderby"] = it.a5Orderby
            if (it.index) entity << it.index.asMap()
            if (it.map) entity << it.map
            entity
        }
    }
}