package a5.xml

import org.basex.core.Context
import org.basex.query.iter.Iter
import org.basex.query.QueryProcessor
import org.basex.query.value.Value

class SchemaProcessor {

    static String validate(Value document, String schema) {
        String result = ""
        String expression = "validate:xsd-info(/, $schema)"
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