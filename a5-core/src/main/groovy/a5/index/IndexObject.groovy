package a5.index

import a5.mappable.Mappable

class IndexObject extends Mappable {

    String id

    String label

    String objectType

    String contentType

    String fileUri

    Boolean fileExists

    String fileUpdated

    String fileCreated

    Long fileSize

    Float duration

    Integer width

    Integer height

    ArrayList<String> parentOf

    ArrayList<String> relatedTo
}