package a5.oai

import a5.oai.OaiRequest
import a5.oai.OaiResponse

class OaiDataProviderController {

    static allowedMethods = [index: ['GET', 'POST']]

    def index() {
        def oaiRequest = new OaiRequest(params: params)
        def oaiResponse = new OaiResponse(oaiRequest: oaiRequest)
        render(text: oaiResponse.body, contentType: "text/xml")
    }
}
