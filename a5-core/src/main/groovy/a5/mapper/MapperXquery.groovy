package a5.mapper

import a5.xml.XmlProcessor
import org.basex.query.value.Value

class MapperXquery extends Mapper {

    Value parsed

    Map<String,String> namedScripts = [:]

    Map namespaces = [:]

    void parse(File file) {
        parsed = XmlProcessor.parse(file)
    }
}