package a5

import a5.api.util.ApiUtil
import grails.util.Holders

class CacheService {

    private String getFileSystemCachePath(String repository, String input, String requestPath) {
        def conf = Holders.config.a5.repository
        def docBase = conf[repository].docBase
        def docBaseCache = conf[repository].docBaseCache
        def resourcePath = input.replace(docBase, docBaseCache)
        def tokens = requestPath.split("/")[-6..-1]
        ApiUtil.normalizePath(new File(resourcePath + "/" + tokens.join("/")))
    }

    File getFile(String repository, File input, String requestPath) {
        new File(getFileSystemCachePath(repository, input.toString(), requestPath))
    }
}
