package a5.xml

import org.basex.core.Context
import org.basex.query.iter.Iter
import org.basex.query.QueryProcessor
import org.basex.query.value.Value
import org.basex.query.value.item.Item

class XqueryProcessor {

    static ArrayList<String> evaluate(Value document, String script, Map namespaces) {
        ArrayList<String> result = []
        try {
            QueryProcessor proc = new QueryProcessor(script, new Context())
            proc.context(document)
            namespaces.each { k, v ->
                proc.namespace(k, v)
            }
            Iter iter = proc.iter()
            for(Item item; (item = iter.next()) != null;) {
                String str = new String(item.string(), "UTF-8")
                if (str != "") result << str
            }
        } catch(any) {
            println any
        }
        result
    }
}