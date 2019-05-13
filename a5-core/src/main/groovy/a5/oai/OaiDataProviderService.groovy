package a5.oai

interface OaiDataProviderService {

    OaiItem getItem(String repositoryName, String metadataPrefix, String identifier)

    OaiItemList getItemList(String repositoryName, String metadataPrefix, Date from, Date until, Integer skip,
                            Integer top)

    OaiRepository getRepository(String repositoryName)
}