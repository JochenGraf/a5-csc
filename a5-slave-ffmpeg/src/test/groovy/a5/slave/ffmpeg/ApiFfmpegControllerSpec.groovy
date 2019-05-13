package a5.slave.ffmpeg

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ApiFfmpegController)
class ApiFfmpegControllerSpec extends Specification {

    def setup() {
        controller.apiFfmpegService = new ApiFfmpegService()
    }

    def cleanup() {
    }

    void "Debug Default Params"() {
        when:
        request.method = "GET"
        params.input = "test.wav"
        params.output = "test.mp3"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.wav test.mp3"
    }

    void "Debug Section Param"() {
        when:
        request.method = "GET"
        params.input = "test.wav"
        params.output = "test.mp3"
        params.section = "20,21"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-ss 20 -y -i test.wav -to 21 test.mp3"
    }

    void "Debug Region Param"() {
        when:
        request.method = "GET"
        params.input = "test.jpg"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "20,30,40,40"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.jpg -vf [in]crop=20:30:40:40[out] test.jpg"
    }

    void "Debug Size Param"() {
        when:
        request.method = "GET"
        params.input = "test.jpg"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "30,30"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.jpg -vf [in]scale=30:30[out] test.jpg"
    }

    void "Debug Rotation Param"() {
        when:
        request.method = "GET"
        params.input = "test.jpg"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "90"
        params.filter = "none"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.jpg -vf [in]transpose=2,transpose=2,transpose=2[out] test.jpg"
    }

    void "Debug Gray Filter Param"() {
        when:
        request.method = "GET"
        params.input = "test.jpg"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "gray"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.jpg -vf [in]format=gray[out] test.jpg"
    }

    void "Debug Waveform Filter Param"() {
        when:
        request.method = "GET"
        params.input = "test.wav"
        params.output = "test.png"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "waveform"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.wav -filter_complex compand,showwavespic=s=6000x250 test.png"
    }

    void "Debug Spectrum Filter Param"() {
        when:
        request.method = "GET"
        params.input = "test.wav"
        params.output = "test.png"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "spectrum"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.wav -filter_complex compand,showspectrumpic=s=6000x250 test.png"
    }

    void "Debug Thumbnail Filter Param"() {
        when:
        request.method = "GET"
        params.input = "test.wav"
        params.output = "test.jpg"
        params.section = "3,"
        params.region = "full"
        params.size = "500,"
        params.rotation = "0"
        params.filter = "thumbnail"
        params.quality = "default"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-ss 3 -y -i test.wav -vf [in]scale=500:-1[out] -vframes 1 test.jpg"
    }

    void "Debug Low Quality Param"() {
        when:
        request.method = "GET"
        params.input = "test.tif"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "low"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.tif -qscale 31 test.jpg"
    }

    void "Debug Medium Quality Param"() {
        when:
        request.method = "GET"
        params.input = "test.tif"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "medium"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.tif -qscale 16 test.jpg"
    }

    void "Debug High Quality Param"() {
        when:
        request.method = "GET"
        params.input = "test.tif"
        params.output = "test.jpg"
        params.section = "full"
        params.region = "full"
        params.size = "full"
        params.rotation = "0"
        params.filter = "none"
        params.quality = "high"
        params.format = "test"
        controller.debug()
        then:
        response.status == 200
        response.text == "-y -i test.tif -qscale 1 test.jpg"
    }
}
