package a5.oai

enum OaiVerb {

    GET_RECORD("GetRecord"),

    IDENTIFY("Identify"),

    LIST_IDENTIFIERS("ListIdentifiers"),

    LIST_METADATA_FORMATS("ListMetadataFormats"),

    LIST_RECORDS("ListRecords"),

    LIST_SETS("ListSets")

    final String arg

    static final Map map

    static {
        map = [:] as TreeMap
        values().each { verb ->
            map.put(verb.arg, verb)
        }
    }

    private OaiVerb(String arg) {
        this.arg = arg
    }

    static getOaiVerb(String arg) {
        map[arg]
    }
}