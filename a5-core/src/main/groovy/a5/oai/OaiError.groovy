package a5.oai

enum OaiError {

    BAD_ARGUMENT('badArgument', 'The request includes illegal arguments, is missing required arguments, includes a' +
            ' repeated argument, or values for arguments have an illegal syntax.'),

    BAD_RESUMPTION_TOKEN('badResumptionToken', 'The value of the resumptionToken argument is invalid or expired.'),

    BAD_VERB('badVerb', 'Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the' +
            ' verb argument is repeated.'),

    CANNOT_DISSEMINATE_FORMAT('cannotDisseminateFormat', 'The metadata format identified by the value given for the' +
            ' metadataPrefix argument is not supported by the item or by the repository.'),

    ID_DOES_NOT_EXIST('idDoesNotExist', 'The value of the identifier argument is unknown or illegal in this' +
            ' repository.'),

    NO_RECORDS_MATCH('noRecordsMatch', 'The combination of the values of the from, until, set and metadataPrefix' +
            ' arguments results in an empty list.'),

    NO_METADATA_FORMATS('noMetadataFormats', 'There are no metadata formats available for the specified item.'),

    NO_SET_HIERARCHY('noSetHierarchy', 'The repository does not support sets.')

    final String code

    final String description

    private OaiError(String code, String description) {
        this.code = code
        this.description = description
    }
}