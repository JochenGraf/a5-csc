package a5.apidoc

class ApidocAdminController {

    def index() {
        def exampleConfiguration = new File(grailsApplication.config.a5.repository."a5-1".config).text
        render(
                view: "index",
                model: [exampleConfiguration: exampleConfiguration]
        )
    }
}