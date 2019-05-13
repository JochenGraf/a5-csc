package a5.selector

class SelectorFacets {

    static int DEFAULT_SIZE = 10

    static Map parse(String paramValue) {
        if (paramValue == null || paramValue == "") return null
        Map map = [:]
        ArrayList<String> facets = paramValue.contains(",") ? paramValue.split(",") : [paramValue]
        ArrayList<String> tokens
        facets.each {
            tokens = it.contains(":") ? it.split(":") : [it, "$DEFAULT_SIZE"]
            map[(tokens[0])] = tokens[1].toInteger()
        }
        map
    }
}