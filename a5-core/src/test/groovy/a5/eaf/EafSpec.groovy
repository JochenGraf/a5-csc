package a5.eaf

import groovy.util.slurpersupport.GPathResult
import spock.lang.Shared

trait EafSpec {

    String fileUri1 = "../a5-testdata/a5-1/test_eaf/Annotations/Gutob_Komlas_Story01_20130228.eaf"

    String fileUri2 = "../a5-testdata/a5-1/test_eaf/Annotations/BAN_AM_NS_20121203_Space_Games_4_g.eaf"

    GPathResult element1

    GPathResult element2

    @Shared
    EafAnnotationDocument eaf1

    @Shared
    EafAnnotationDocument eaf2

    def setupSpec() {
        element1 = new XmlSlurper().parse(fileUri1)
        eaf1 = new EafAnnotationDocument(element1)
        element2 = new XmlSlurper().parse(fileUri2)
        eaf2 = new EafAnnotationDocument(element2)
    }
}