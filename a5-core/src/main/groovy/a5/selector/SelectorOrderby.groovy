package a5.selector

class SelectorOrderby {

    static Map parse(String paramValue) {
        if (paramValue == null || paramValue == "") return null
        Map map = [:]
        ArrayList<String> tokens = paramValue.contains(",") ? paramValue.split(",") : [paramValue]
        tokens.each {
            switch(it) {
                case { it.endsWith(" asc") }:
                    map << [(it.split(" ")[0]): "asc"]
                    break
                case { it.endsWith(" desc") }:
                    map << [(it.split(" ")[0]): "desc"]
                    break
                default:
                    map << [(it): "asc"]
                    break
            }
        }
        map
    }
}