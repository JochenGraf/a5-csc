package a5.elastic

import java.lang.reflect.Field

class ElasticMappingFieldGroovy extends ElasticMappingField {

    Field reflect

    Class clazz

    String typeName

    ElasticMappingFieldGroovy(Field reflect) {
        super(reflect.name)
        this.reflect = reflect
        this.typeName = reflect.genericType.toString()
        if (isSimpleType()) {
            this.clazz = reflect.type
        } else if (isComplexType()) {
            this.clazz = reflect.type
        } else if (isNestedType()) {
            this.clazz = Class.forName("${reflect.genericType}" - "java.util.ArrayList<" - ">")
        }
    }

    boolean isSimpleType() {
        this.typeName =~ /(java\.lang\.)/
    }

    boolean isXqueryType() {
        this.typeName =~ /^class a5\.index\.IndexQuery/
    }

    boolean isComplexType() {
        this.typeName =~ /^class a5\.index\..*/
    }

    boolean isNestedType() {
        this.typeName =~ /^java\.util\.ArrayList<a5\.index\..*/
    }
}