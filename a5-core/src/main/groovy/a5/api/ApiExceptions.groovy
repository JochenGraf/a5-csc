package a5.api

import a5.api.util.ApiUtil
import grails.util.Holders
import javax.servlet.http.HttpServletResponse
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest.StandardMultipartFile

/**
 * Base Exception
 */

class ApiException extends RuntimeException {

    protected int status

    ApiException(String message, int status) {
        super(message)
        this.status = status
    }
}

/**
 * Request related exceptions
 */

class MethodNotAllowedException extends ApiException {

    MethodNotAllowedException(String method) {
        super("Method \"$method\" is not allowed.", HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }
}

class MultipartRangeNotSupportedException extends ApiException {

    MultipartRangeNotSupportedException() {
        super("Multipart byte range is not supported.", HttpServletResponse.SC_NOT_IMPLEMENTED)
    }
}

class RangeNotSatisfiableException extends ApiException {

    RangeNotSatisfiableException(String range) {
        super("Invalid range request \"$range\"", HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE)
    }
}

class UnsupportedMediaTypeException extends ApiException {

    UnsupportedMediaTypeException(String type) {
        super("Media type \"$type\" not supported.", HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE)
    }
}

/**
 * Cluster related exceptions
 */

class SlaveFfmpegNotAvailableException extends ApiException {

    SlaveFfmpegNotAvailableException() {
        super("Ffmpeg slave is not available.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    }
}

class SlaveFfmpegBusyException extends ApiException {

    SlaveFfmpegBusyException() {
        super("Ffmpeg slave is busy.", HttpServletResponse.SC_SERVICE_UNAVAILABLE)
    }
    SlaveFfmpegBusyException(String message) {
        super("Ffmpeg slave is busy: $message", HttpServletResponse.SC_SERVICE_UNAVAILABLE)
    }

}

/**
 * CRUD API Exceptions
 */

class RepositoryNotConfiguredException extends ApiException {

    RepositoryNotConfiguredException(String repository) {
        super("A repository named \"$repository\" is not configured.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    }
}

class RepositoryDataNotAvailable extends ApiException {

    RepositoryDataNotAvailable(String repository) {
        super("Repository \"$repository\" is configured, but no data are available.",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    }
}

class RessourceDoesNotExistException extends ApiException {

    RessourceDoesNotExistException(String identifier) {
        super("Ressource \"$identifier\" does not exist.", HttpServletResponse.SC_NOT_FOUND)
    }
}

class VersionParameterNotSupportedException extends ApiException {

    VersionParameterNotSupportedException(String version) {
        super("Version \"$version\" could not be retrieved. Currently, only \"latest\" is supported.",
                HttpServletResponse.SC_NOT_IMPLEMENTED )
    }
}

/**
 * API Media Exceptions
 */

class InvalidSectionParameterException extends ApiException {

    InvalidSectionParameterException(String section) {
        super("Malformed section parameter \"$section\", \"$Holders.config.a5.api.param.section.pattern\" is expected.",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}

class InvalidRegionParameterException extends ApiException {

    InvalidRegionParameterException(String region) {
        super("Malformed region parameter \"$region\", \"$Holders.config.a5.api.param.region.pattern\" is expected.",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}

class InvalidSizeParameterException extends ApiException {

    InvalidSizeParameterException(String size) {
        super("Malformed size parameter \"$size\", \"$Holders.config.a5.api.param.size.pattern\" is expected.",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}

class InvalidRotationParameterException extends ApiException {

    InvalidRotationParameterException(String rotation) {
        super("Malformed rotation parameter \"$rotation\", \"$Holders.config.a5.api.param.rotation.pattern\" is" +
                "expected.", HttpServletResponse.SC_BAD_REQUEST)
    }
}

class InvalidFilterParameterException extends ApiException {

    InvalidFilterParameterException(String filter) {
        super("Malformed filter parameter \"$filter\", \"$Holders.config.a5.api.param.filter.pattern\" is expected.",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}

class InvalidQualityParameterException extends ApiException {

    InvalidQualityParameterException(String quality) {
        super("Malformed quality parameter \"$quality\", \"$Holders.config.a5.api.param.quality.pattern\" is expected.",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}

/**
 * API Deposit Exceptions
 */

class NoFileWasUploadedException extends ApiException {

    NoFileWasUploadedException() {
        super("No file was uploaded.", HttpServletResponse.SC_NO_CONTENT)
    }
}

class UploadedFileExceedsMaxFileSizeException extends ApiException {

    String name

    long size

    UploadedFileExceedsMaxFileSizeException(StandardMultipartFile file, long size) {
        super("The uploaded file \"$file.originalFilename\" exceeds the maximum file size of " +
                "${ApiUtil.formatFileSize(size)}.",
                HttpServletResponse.SC_BAD_REQUEST)
        this.name = file.originalFilename
        this.size = file.size
    }
}

class UserSpaceLimitReached extends ApiException {

    UserSpaceLimitReached(long maxUserSpace) {
        super("No more space available. Maximum is \"${ApiUtil.formatFileSize(maxUserSpace)}\".",
                HttpServletResponse.SC_BAD_REQUEST)
    }
}
