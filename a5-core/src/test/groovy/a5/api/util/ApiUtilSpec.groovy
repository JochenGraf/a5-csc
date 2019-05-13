package a5.api.util

import spock.lang.Specification

class ApiUtilSpec extends Specification {

    void "normalize path"() {
        expect:
        ApiUtil.normalizePath(new File("/a/b/c/../d")) == "/a/b/d"
    }

    void "is audio"() {
        expect:
        ApiUtil.isAudio(".wav") == true
        ApiUtil.isAudio(".xml") == false
    }

    void "is video"() {
        expect:
        ApiUtil.isVideo(".mp4") == true
        ApiUtil.isVideo(".xml") == false
    }

    void "is image"() {
        expect:
        ApiUtil.isImage(".jpg") == true
        ApiUtil.isImage(".xml") == false
    }
}
