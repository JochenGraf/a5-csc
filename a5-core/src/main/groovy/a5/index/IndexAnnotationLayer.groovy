package a5.index

import a5.mappable.Mappable

class IndexAnnotationLayer extends Mappable {

    ArrayList<String> type = ["AnnotationLayer", "sc:AnnotationLayer"]

    String id

    String label

    Integer total

    String hasParent

    ArrayList<String> isParentOf

    ArrayList<IndexAnnotation> resources = []

    IndexQuery _query

    IndexAnnotationLayer() {}

    IndexAnnotationLayer(Map obj) {
        id = obj.id
        label = obj.label
        type = obj.type
        total = obj.total
        hasParent = obj.hasParent
        isParentOf = obj.isParentOf
        resources = [obj.resources].flatten()
    }
}