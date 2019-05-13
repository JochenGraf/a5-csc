package a5.oai

trait OaiItem {

    String identifier

    String datestamp

    Map<String, String> metadata = [:]
}