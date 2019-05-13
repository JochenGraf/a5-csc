package a5.api

class ApiObjectController implements ApiController {

    def apiObjectService

    def object() {
        serialize(apiObjectService.object(params))
    }

    def objects() {
        serialize(apiObjectService.objects(params))
    }
}