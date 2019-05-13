package a5.slave.ffmpeg

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ApiFfmpegService)
class ApiFfmpegServiceSpec extends Specification {

    void "Parse Section"() {
        expect:
        service.parseSection("full")    == [seek: null, to: null]
        service.parseSection(",200")    == [seek: null, to: "200"]
        service.parseSection("200,")    == [seek: "200", to: null]
        service.parseSection("200,200") == [seek: "200", to: "200"]

        service.argsSection(service.parseSection("full"))     == ["", "", "", ""]
        service.argsSection(service.parseSection(",200"))     == ["", "", "-to", "200"]
        service.argsSection(service.parseSection("200,"))     == ["-ss", "200", "", ""]
        service.argsSection(service.parseSection("200,200"))  == ["-ss", "200", "-to", "200"]
    }

    void "Parse Region"() {
        expect:
        service.parseRegion("full")     == [offsetx: null, offsety: null, width: null, height: null]
        service.parseRegion("1,2,3,4")  == [offsetx: "1", offsety: "2", width: "3", height: "4"]

        service.argsRegion(service.parseRegion("full"))      == [""]
        service.argsRegion(service.parseRegion("1,2,3,4"))   == ["crop=1:2:3:4"]
    }

    void "Parse Size"() {
        expect:
        service.parseSize("full")       == [width: null, height: null]
        service.parseSize(",200")       == [width: null, height: "200"]
        service.parseSize("200,")       == [width: "200", height: null]
        service.parseSize("200,200")    == [width: "200", height: "200"]

        service.argsSize(service.parseSize("full"))        == [""]
        service.argsSize(service.parseSize(",200"))        == ["scale=-1:200"]
        service.argsSize(service.parseSize("200,"))        == ["scale=200:-1"]
        service.argsSize(service.parseSize("200,200"))     == ["scale=200:200"]
    }

    void "Parse Rotation"() {
        expect:
        service.argsRotation("0")       ==  [""]
        service.argsRotation("90")      ==  ["transpose=2,transpose=2,transpose=2"]
        service.argsRotation("180")     ==  ["transpose=2,transpose=2"]
        service.argsRotation("270")     ==  ["transpose=2"]
    }

    void "Parse Filter"() {
        expect:
        service.parseFilter("none")     == [none: true, gray: false, thumbnail: false, waveform: false, spectrum: false]
        service.parseFilter("gray")     == [none: false, gray: true, thumbnail: false, waveform: false, spectrum: false]
        service.parseFilter("thumbnail")== [none: false, gray: false, thumbnail: true, waveform: false, spectrum: false]
        service.parseFilter("waveform") == [none: false, gray: false, thumbnail: false, waveform: true, spectrum: false]
        service.parseFilter("spectrum") == [none: false, gray: false, thumbnail: false, waveform: false, spectrum: true]
    }

    void "Parse Quality"() {
        expect:
        service.argsQuality("default")  == [""]
        service.argsQuality("high")     == ["-qscale", "1"]
        service.argsQuality("medium")   == ["-qscale", "16"]
        service.argsQuality("low")      == ["-qscale", "31"]
    }
}
