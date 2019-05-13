package a5.api

class ApiQueryInterceptor {

    boolean before() {
        header("Access-Control-Allow-Origin", "*" )
        header("Access-Control-Allow-Methods", "GET")
        true
    }

    boolean after() { true }
}