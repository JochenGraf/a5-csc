package a5.index

import a5.mappable.Mappable

class IndexAnnotationCanvas extends Mappable {

    String id

    String label

    ArrayList<String> type = ["Canvas", "sc:Canvas"]

    Float duration

    Integer width

    Integer height

    ArrayList<String> media = []

    ArrayList<String> otherContent = []
}