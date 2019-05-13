package a5.mappable

import grails.converters.JSON

abstract class Mappable {

    Mappable() {}

    Mappable(Map json) {
        this.class.declaredFields.findAll { !it.synthetic }.each {
            this[(it.name)] = json[(it.name)]
        }
    }

    void fromMap(Map json) {
        this.class.declaredFields.findAll { !it.synthetic }.each {
            this[(it.name)] = json[(it.name)]
        }
    }

    ArrayList<String> declaredFieldNames() {
        ArrayList<String> fieldNames = []
        this.class.declaredFields.findAll { !it.synthetic }.each {
            fieldNames << it.name
        }
        fieldNames
    }

    Map asMap() {
        Map map = [:]
        this.class.declaredFields.findAll { !it.synthetic }.each {
            if (this[(it.name)] != null) {
                if (this[(it.name)] instanceof Mappable) {
                    map[(it.name)] = this[(it.name)].asMap()
                } else {
                    map[(it.name)] = this[(it.name)]
                }
            }
        }
        map
    }

    static {
        JSON.registerObjectMarshaller(Mappable) {
            it.asMap()
        }
    }
}