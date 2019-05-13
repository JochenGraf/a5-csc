package a5.api

class ApiDepositInterceptor {

    private void validate() {
    }

    boolean before() {
        validate()
        true
    }

    boolean after() {
        true
    }
}