package a5.xml

import org.basex.core.Context
import org.basex.query.QueryProcessor
import org.basex.query.value.Value

class XmlProcessor {

    static Value parse(String xml) {
        def context = new Context()
        def value
        try {
            value = new QueryProcessor(xml, context).value()
        } catch(any) {
            value = null
        }
        value
    }

    static Value parse(File file) {
        def context = new Context()
        def xqueryScript = "doc('$file')"
        def value
        try {
            value = new QueryProcessor(xqueryScript, context).value()
        } catch(any) {
            value = null
        }
        value
    }
}