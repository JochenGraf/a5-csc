package a5.mapper

import a5.xml.XmlProcessor
import org.basex.query.value.Value

class MapperXslt extends Mapper {

    Value parsed

    Map<String,String> namedScripts = [:]

    void parse(File file) {
        parsed = XmlProcessor.parse(file)
    }
}