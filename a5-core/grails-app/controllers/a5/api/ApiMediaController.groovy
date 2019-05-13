package a5.api

import a5.api.util.ApiUtil
import a5.elastic.ElasticQ
import a5.elastic.ElasticResult
import a5.index.Index
import a5.selector.Selector
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApiMediaController implements ApiController {

    protected Logger log = LoggerFactory.getLogger(getClass())

    def cacheService

    def elasticService

    def ffmpegService

    def progressService

    def rangeRequestService

    private ArrayList<String> fileSelectors = [
            "repository",
            "prefix",
            "identifier"
    ]

    def file() {
        Selector selector = new Selector(params, fileSelectors)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.OBJECT, query)
        String fileUri = grailsApplication.config.a5.repository[selector.repository].docBase + elasticResult.hits[0]?.fileUri
        rangeRequestService.serveFileOrChunk(request, response, new File(fileUri))
        response
    }

    private ArrayList<String> conversionSelectors = [
            "repository",
            "prefix",
            "identifier",
            "format"
    ]

    def conversion() {
        Selector selector = new Selector(params, conversionSelectors)
        forward(uri: "/api/media/$selector.repository/$selector.identifier/full/full/full/0/none/default." +
                "$selector.format")
    }

    private ArrayList<String> manipulationSelectors = [
            "repository",
            "prefix",
            "identifier",
            "section",
            "region",
            "size",
            "rotation",
            "filter",
            "quality",
            "format"
    ]

    def manipulation() {
        Selector selector = new Selector(params, manipulationSelectors)
        ElasticQ query = new ElasticQ().filter(["id", "eq", selector.identifier])
        ElasticResult elasticResult = elasticService.read(selector.repository, Index.OBJECT, query)
        String fileUri = grailsApplication.config.a5.repository[selector.repository].docBase + elasticResult.hits[0]?.fileUri
        def input = new File(fileUri)
        if (!input.exists()) {
            render(status: 404, text: "Not found.")
            return
        }
        def output = cacheService.getFile(selector.repository, input, request.requestURI)
        // if this manipulation request is currently in progress
        if (progressService.isInProgress(output)) {
            render(status: 202, message: "In progress.")
        }
        // if manipulated file already exists, serve it from cache
        else if (output.exists()) {
            progressService.stop(output)
            rangeRequestService.serveFileOrChunk(request, response, output)
        }
        // otherwise, manipulate the input file
        else {
            // asynchronously manipulate audio and video files
            if (ApiUtil.isAudio(input) || ApiUtil.isVideo(input)) {
                ffmpegService.processAsync(input, output, selector.identifier, params)
                render(status: 202, message: "In progress.")
            }
            // image processing is fast, no async
            else if (ApiUtil.isImage(input)) {
                ffmpegService.processSync(input, output, selector.identifier, params)
                rangeRequestService.serveFileOrChunk(request, response, output)
            }
            // we don't want to manipulate other types than audio, video or image
            else {
                throwException(new UnsupportedMediaTypeException(ApiUtil.getContentType(input)))
            }
        }
        response
    }
}
