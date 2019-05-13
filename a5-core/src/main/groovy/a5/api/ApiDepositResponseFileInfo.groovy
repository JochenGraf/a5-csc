package a5.api

import a5.api.util.ApiUtil
import grails.util.GrailsStringUtils

class ApiDepositResponseFileInfo {

    String name

    String path

    String lastModified

    long size

    String sizeFormatted

    boolean progress

    String mediaType

    ApiDepositResponseFileInfo(File file, String userDir) {
        name = file.name
        path = GrailsStringUtils.substringAfter(file.absolutePath, userDir)
        size = file.length()
        sizeFormatted = ApiUtil.formatFileSize(file.length())
        lastModified = new Date(file.lastModified())
        progress = new File(file.absolutePath + ".progress").exists()
        mediaType = ApiUtil.getContentType(file)
    }
}