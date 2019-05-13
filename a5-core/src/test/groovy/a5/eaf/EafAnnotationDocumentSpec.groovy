package a5.eaf

import spock.lang.Specification

class EafAnnotationDocumentSpec extends Specification implements EafSpec {

    void "annotation document available"() {
        expect:
            inst instanceof EafAnnotationDocument
        where:
            inst << [eaf1, eaf2]
    }
}