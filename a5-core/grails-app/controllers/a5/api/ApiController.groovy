package a5.api

import a5.odata.OdataEntity
import a5.odata.OdataCollection
import grails.converters.JSON

trait ApiController {

    def serialize(OdataEntity odata) {
        JSON json = odata as JSON
        json.prettyPrint = odata.a5Selector.pretty
        render json
    }

    def serialize(OdataCollection odata) {
        JSON json = odata as JSON
        json.prettyPrint = odata.a5Selector.pretty
        render json
    }
    def serialize(String error){
        render error
    }

    def throwException(ApiException exception) {
        render(status: exception.status, message: exception.getMessage())
    }
}
