package a5.index

import a5.mappable.Mappable

class IndexAnnotationManifest extends Mappable {

    String id

    String label

    ArrayList<String> type = ["Manifest", "sc:Manifest"]

    ArrayList<String> members = []

    ArrayList<String> within = []
}