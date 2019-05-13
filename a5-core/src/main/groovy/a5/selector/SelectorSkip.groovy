package a5.selector

class SelectorSkip {

    static Integer DEFAULT = 0

    static Integer parse(String paramValue) {
        switch(paramValue) {
            case null:
                return SelectorSkip.DEFAULT
                break
            case "":
                return SelectorSkip.DEFAULT
                break
            default:
                return paramValue.toInteger()
                break
        }
        return SelectorSkip.DEFAULT
    }
}