package a5.mapper

import a5.index.IndexOaiItem
import a5.xml.SchemaProcessor
import a5.xml.XsltProcessor

class MapperOai extends MapperXslt {

    IndexOaiItem indexOaiItem

    Map namedSchemas = [:] //TODO!!!!!

    private void mapNodes() {
        String result
        namedScripts.each { name, script ->
            result = XsltProcessor.evaluate(parsed, script)
            if (result?.size() > 0) {
                //def valid = SchemaProcessor.validate(result, schemas[(name)])
                indexOaiItem.metadata[(name)] = result
            }
        }
    }

    ArrayList<Object> map(File file, Map object) {
        indexOaiItem = new IndexOaiItem()
        indexOaiItem.identifier = object.id
        indexOaiItem.datestamp = object.fileUpdated
        parse(file)
        mapNodes()
        [indexOaiItem]
    }
}