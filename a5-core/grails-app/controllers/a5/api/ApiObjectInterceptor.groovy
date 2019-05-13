package a5.api

class ApiObjectInterceptor {

    boolean before() {
        header("Access-Control-Allow-Origin", "*" )
        header("Access-Control-Allow-Methods", "GET")
        true
    }

    boolean after() { true }
}