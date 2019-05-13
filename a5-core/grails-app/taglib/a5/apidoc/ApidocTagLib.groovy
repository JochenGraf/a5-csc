package a5.apidoc

import groovy.xml.*

class ApidocTagLib {

    static namespace = 'apidoc'

    static defaultEncodeAs = [taglib: 'text']

    def h2 = { attrs, body ->
        def mb = new MarkupBuilder(out)
        def method = attrs.request.method ?: "GET"
        mb.h2(attrs.request.title)
        mb.p {
            mb.samp("$method\t" + attrs.request.scheme)
        }
        mb.p(attrs.request.description)
    }

    def h3 = { attrs, body ->
        def mb = new MarkupBuilder(new IndentPrinter(out, "", false))
        def method = attrs.param.method ?: "GET"
        mb.h3(attrs.param.title)
        mb.p {
            def s = attrs.scheme
            def n = attrs.param.name
            def i = s.lastIndexOf(n)
            if (s && i > 0) {
                mb.samp("$method\t" + s.substring(0, i)) {
                    mb.strong(n)
                    out << s.substring(i + n.length())
                }
            }
        }
        mb.p(attrs.param.description)
    }

    def h4 = { attrs, body ->
        def mb = new MarkupBuilder(new IndentPrinter(out, "", false))
        def method = attrs.param.method ?: "GET"
        mb.h4(attrs.param.title)
        mb.p {
            def s = attrs.scheme
            def n = attrs.param.name
            def i = s.lastIndexOf(n)
            if (s && i > 0) {
                mb.samp("$method\t" + s.substring(0, i)) {
                    mb.strong(n)
                    out << s.substring(i + n.length())
                }
            }
        }
        mb.p(attrs.param.description)
    }

    def link = { attrs, body ->
        def mb = new MarkupBuilder(out)
        if ( attrs.pattern ) {
            def toks = attrs.href.split(attrs.pattern)
            if (toks.length > 1) {
                mb.a(href: request.contextPath + attrs.href, target: "_blank") {
                    mb.span(toks[0])
                    mb.strong(attrs.pattern)
                    mb.span(toks[1])
                }
            } else {
                mb.a(href: request.contextPath + attrs.href, target: "_blank") {
                    mb.span(attrs.href)
                }
            }
        } else {
            mb.a(href: request.contextPath + attrs.href, target: "_blank", attrs.href)
        }
    }

    def reference = { attrs, body ->
        def mb = new MarkupBuilder(out)
        if (!attrs.href) return
        mb.p("See: ") {
            mb.a(href: attrs.href, target: "_blank", attrs.href)
        }
    }

    def desctable = { attrs, body ->
        def mb = new MarkupBuilder(out)
        mb.table(class: "table table-bordered table-striped") {
            mb.thead {
                mb.tr {
                    mb.th("Example")
                    mb.th("Description")
                }
            }
            out << body()
        }
    }

    def errtable = { attrs, body ->
        def mb = new MarkupBuilder(out)
        mb.div {
            mb.h5 {
                mb.strong("Errors")
            }
            mb.table(class: "table table-bordered table-striped") {
                mb.thead {}
                out << body()
            }
        }
    }

    def errtablerow = { attrs, body ->
        def mb = new MarkupBuilder(out)
        if (attrs.heading) {
            mb.td(class: "text-center", colspan: "3") {
                mb.strong(attrs.heading)
            }
        } else {
            def statusdesc = grailsApplication.config.a5.api.error.status["_" + attrs.err.status]
            def message = attrs.err.message?.replace("%1%", "'" + attrs.arg + "'")
            mb.td(class: "text-danger", attrs.err.status)
            mb.td(class: "text-danger", statusdesc)
            mb.td(class: "text-danger", message)
        }
    }
}