package a5.http

import spock.lang.Specification

class HttpRangeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "range header null"() {
        expect: "returns null"
        HttpRange.parseRange(null, 12345678) == null
    }

    void "missing dash"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=0", 12345678) == null
    }

    void "leading dash"() {
        when:
        def ranges = HttpRange.parseRange("bytes=-12345677", 12345678)
        then:
        ranges.size() == 1
        ranges.get(0).start == 1
        ranges.get(0).end == 12345677
        ranges.get(0).length == 12345678
    }

    void "dash at end"() {
        when:
        def ranges = HttpRange.parseRange("bytes=0-", 12345678)
        then:
        ranges.size() == 1
        ranges.get(0).start == 0
        ranges.get(0).end == 12345677
        ranges.get(0).length == 12345678
    }

    void "catched number format exception 1"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=xxx-", 12345678) == null
    }

    void "catched number format exception 2"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=-xxx", 12345678) == null
    }

    void "invalid range: end < 0"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=--2", 12345678) == null
    }

    void "invalid range: end < start"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=200-100", 12345678) == null
    }

    void "invalid range: length <= 0"() {
        expect: "returns null"
        HttpRange.parseRange("bytes=0-", 0) == null
    }
}
