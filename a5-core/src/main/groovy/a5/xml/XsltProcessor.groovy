package a5.xml

import org.basex.core.Context
import org.basex.query.iter.Iter
import org.basex.query.QueryProcessor
import org.basex.query.value.Value

class XsltProcessor {

    static String evaluate(Value document, String script) {
        String result = ""
        String expression = """
                declare option output:indent \"no\";
                xslt:transform(/, $script)
        """
        try {
            QueryProcessor proc = new QueryProcessor(expression, new Context())
            proc.context(document)
            Iter iter = proc.iter()
            result = iter.next().serialize()
        } catch(any) {
            println any
        }
        result
    }
}