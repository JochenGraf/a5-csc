package a5.oai

import org.grails.web.util.WebUtils

class OaiRepository {

    String repositoryName

    String baseUrl

    Map<String, OaiMetadataFormat> metadataFormats = [:]

    ArrayList<String> adminEmails = []

    String getBaseUrl() {
        baseUrl ?: WebUtils.retrieveGrailsWebRequest().currentRequest.requestURL
    }
}