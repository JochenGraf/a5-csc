package a5.api

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ApiMediaInterceptor)
class ApiMediaInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "method valid"() {
        when:
        request.method = "GET"
        interceptor.validate()
        then:
        true
    }

    void "method invalid"() {
        when:
        request.method = "POST"
        interceptor.validate()
        then:
        thrown(MethodNotAllowedException)
    }

    void "version parameter valid"() {
        when:
        params.version = "latest"
        interceptor.validate()
        then:
        true
    }

    void "version parameter invalid"() {
        when:
        params.version = "xxx"
        interceptor.validate()
        then:
        thrown(VersionParameterNotSupportedException)
    }

    void "section parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        interceptor.validate()
        then:
        true

    }

    void "section parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "xxx"
        interceptor.validate()
        then:
        thrown(InvalidSectionParameterException)
    }

    void "region parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        interceptor.validate()
        then:
        true
    }

    void "region parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "xxx"
        interceptor.validate()
        then:
        thrown(InvalidRegionParameterException)
    }

    void "size parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        interceptor.validate()
        then:
        true
    }

    void "size parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "xxx"
        interceptor.validate()
        then:
        thrown(InvalidSizeParameterException)
    }

    void "rotation parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "90"
        interceptor.validate()
        then:
        true
    }

    void "rotation parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "10"
        interceptor.validate()
        then:
        thrown(InvalidRotationParameterException)
    }

    void "filter parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "90"
        params.filter = "gray"
        interceptor.validate()
        then:
        true
    }

    void "filter parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "90"
        params.filter = "xxx"
        interceptor.validate()
        then:
        thrown(InvalidFilterParameterException)
    }

    void "quality parameter valid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "90"
        params.filter = "gray"
        params.quality = "high"
        interceptor.validate()
        then:
        true
    }

    void "quality parameter invalid"() {
        when:
        params.version = "latest"
        params.section = "1"
        params.region = "1,2,3,4"
        params.size = "500,"
        params.rotation = "90"
        params.filter = "gray"
        params.quality = "xxx"
        interceptor.validate()
        then:
        thrown(InvalidQualityParameterException)
    }
}
