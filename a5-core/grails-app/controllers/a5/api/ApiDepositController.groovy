package a5.api

import grails.converters.JSON

class ApiDepositController {

    def apiDepositService

    def rangeRequestService

    def authService

    def upload() {
        def jqueryFileUploadResponse = apiDepositService.uploadFile(request, response, apiDepositService.getUserDir(), params.repository)
        render jqueryFileUploadResponse as JSON
    }

    def list() {
        def filesInfo = apiDepositService.listFiles()
        filesInfo
    }

    def stream() {
        if(authService.canRead(request, response, params.object.id)) {
            def file = new File(apiDepositService.getUserDir() + params.file)
            rangeRequestService.serveFileOrChunk(request, response, file)
        }
    }

    def delete() {
        //TODO: check if upload in progress
        def file = new File(apiDepositService.getUserDir() + params.file)
        apiDepositService.deleteFile(file)
        render "file deleted"
    }

    def info() {
        def userDir = apiDepositService.getUserDir()
        def file = new File(userDir + params.file)
        def fileInfo = new ApiDepositResponseFileInfo(file, userDir)
        render fileInfo as JSON
    }

    def throwException(ApiException exception) {
        response.setStatus(exception.status)
        render exception as JSON
    }
}