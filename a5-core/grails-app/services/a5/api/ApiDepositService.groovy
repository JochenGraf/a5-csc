package a5.api

import a5.http.HttpRange
import grails.util.Holders
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiDepositService {

    def uploadService
    def progressService
    def springSecurityService
    protected Logger log = LoggerFactory.getLogger(getClass())


    String getUserDir(repo) {
        def username = springSecurityService.principal.username
        def depository= repo ?: "demo"
        log.debug "user directory is ${Holders.config.a5.repository[depository].docBaseDeposit}/$username/"
        "${Holders.config.a5.repository[depository].docBaseDeposit}/$username/"
    }
    private void checkFileSize(requestFile, String targetDir, long uploadedFileSize, String repositoryName) {
        def maxFileSize = Holders.config.a5.repository[repositoryName].maxFileSize ?: -1
        def maxUserSpace = Holders.config.a5.repository[repositoryName].maxUserSpace ?: -1
        def currentDirectorySize = new File(targetDir).directorySize()
        if (uploadedFileSize > maxFileSize) { // file too large?
            throw new UploadedFileExceedsMaxFileSizeException(requestFile, maxFileSize)
        }
        if (currentDirectorySize + uploadedFileSize > maxUserSpace) { // user space full?
            throw new UserSpaceLimitReached(maxUserSpace)
        }
    }

    Object uploadFile(HttpServletRequest request, HttpServletResponse response, String targetDir,
                             String repositoryName) {
        if (!request.getFileNames()) {
            throw new NoFileWasUploadedException()
        }
        def range = HttpRange.parseContentRange(request)
        def requestFile = request.getFile(request.getFileNames()[0]) //TODO: multiple file upload
        def targetPath = targetDir + requestFile.originalFilename
        new File(targetDir).mkdirs()
        if (request.getHeader("Content-Range") != null && range == null) {
            throw new RangeNotSatisfiableException(request.getHeader("Content-Range"))
        } else if (request.getHeader("Content-Range") == null) { // file upload
            checkFileSize(requestFile, targetDir, requestFile.size, repositoryName)
            uploadService.saveFile(requestFile, targetPath)
        } else { // chunk upload
            checkFileSize(requestFile, targetDir, range.length, repositoryName)
            uploadService.resumeOrSaveChunk(requestFile, targetPath, range, response)
        }
        new ApiDepositResponseFileInfo(new File(targetPath), targetDir)
    }

    Object listFiles(repository) {
        def directory = getUserDir(repository)
        def filesList = new File(directory).listFiles()
        log.debug "user directory is ${directory}, it includes ${filesList}"
        return new ApiDepositResponseFileList(filesList, directory)
    }

    void deleteFile(File file) {
        progressService.stop(file)
        if (file.exists()) file.delete()
    }
}