package a5.mapper

import a5.index.IndexQuery
import a5.xml.XqueryProcessor

class MapperQuery extends MapperXquery {

    IndexQuery indexQuery

    private void mapNodes() {
        ArrayList<String> result
        namedScripts.each { name, script ->
            result = XqueryProcessor.evaluate(parsed, script, namespaces)
            if (result?.size() > 0) {
                indexQuery.fields[(name)] = result
            }
        }
    }

    ArrayList<Object> map(File file) {
        indexQuery = new IndexQuery()
        parse(file)
        mapNodes()
        [indexQuery]
    }
}