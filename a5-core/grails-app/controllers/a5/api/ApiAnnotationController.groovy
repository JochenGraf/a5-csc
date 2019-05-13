package a5.api

class ApiAnnotationController implements ApiController {

    def apiAnnotationService

    def manifest() {
        serialize(apiAnnotationService.manifest(params)?:"invalid Parameters for serializing")
    }

    def canvas() {
        serialize(apiAnnotationService.canvas(params)?:"invalid Parameters for serializing")
    }

    def layer() {
        serialize(apiAnnotationService.layer(params)?:"invalid Parameters for serializing")
    }

    def layers() {
        serialize(apiAnnotationService.layers(params)?:"invalid Parameters for serializing")
    }

    def annotation() {
        serialize(apiAnnotationService.annotation(params)?:"invalid Parameters for serializing")
    }

    def annotations() {
        serialize(apiAnnotationService.annotations(params)?:"invalid Parameters for serializing")
    }
}
