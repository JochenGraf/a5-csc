package a5.selector

class SelectorTop {

    static Integer DEFAULT = 10

    static Integer parse(String paramValue) {
        switch(paramValue) {
            case null:
                return SelectorTop.DEFAULT
                break
            case "":
                return SelectorTop.DEFAULT
                break
            default:
                return paramValue.toInteger()
                break
        }
        return SelectorTop.DEFAULT
    }
}