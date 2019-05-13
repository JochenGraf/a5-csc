package a5.mapper

import groovy.util.slurpersupport.GPathResult

class MapperGroovy extends Mapper {

    GPathResult parsed

    void parse(File file) {
        parsed = new XmlSlurper().parse(file)
    }
}