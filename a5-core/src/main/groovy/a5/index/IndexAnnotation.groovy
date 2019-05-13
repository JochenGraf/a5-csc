package a5.index

import a5.mappable.Mappable

class IndexAnnotation extends Mappable {

    ArrayList<String> type = ["Annotation", "oa:Annotation"]

    String id

    String label

    String motivation

    String bodyValue

    IndexAnnotationSelector selector

    String target

    IndexQuery _query
}