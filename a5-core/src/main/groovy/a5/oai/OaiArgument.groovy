package a5.oai

enum OaiArgument {

    FROM("from"),

    IDENTIFIER("identifier"),

    METADATA_PREFIX("metadataPrefix"),

    RESUMPTION_TOKEN("resumptionToken"),

    SET("set"),

    UNTIL("until")

    final String arg

    private OaiArgument(String arg) {
        this.arg = arg
    }
}