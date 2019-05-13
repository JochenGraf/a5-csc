package a5.selector

class SelectorBool {

    static Boolean parse(String paramValue) {
        switch(paramValue) {
            case null:
                return false
            break
            case "false":
                return false
            break
            default:
                return true
            break
        }
        return true
    }
}