package a5.selector

class SelectorCommaStringList {

    static ArrayList<String> parse(String paramValue) {
        switch(paramValue) {
            case null:
                return null
                break
            case "":
                return null
                break
            case { !it.contains(",") }:
                return [paramValue]
                break
            case { it.contains(",") }:
                return paramValue.split(",")
                break
            default:
                return null
                break
        }
        null
    }
}