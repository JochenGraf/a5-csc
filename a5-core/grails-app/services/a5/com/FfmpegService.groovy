package a5.com

import a5.api.SlaveFfmpegBusyException
import a5.api.SlaveFfmpegNotAvailableException
import grails.plugins.rest.client.RestBuilder
import grails.util.Holders
import grails.async.*
import static grails.async.Promises.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FfmpegService {

    protected Logger log = LoggerFactory.getLogger(getClass())

    def progressService

    private String ffmpegServer = Holders.config.a5.slave.ffmpeg.server

    private String ffmpegApi = ffmpegServer + "/api/ffmpeg"

    private RestBuilder restBuilder = new RestBuilder()

    void ffmpegSlaveAvailable() {
        RestBuilder rest = new RestBuilder()
        try {
            rest.get(ffmpegApi)
        } catch (any) {
            throw new SlaveFfmpegNotAvailableException()
        }
    }

    private static String composeSlaveQueryString(File input, File output, String identifier, Map params) {
        "?" + ["input=${input}",
               "output=${output}",
               "repository=${params.repository}",
               "identifier=${identifier}",
               "version=${params.version}",
               "section=${params.section}",
               "region=${params.region}",
               "size=${params.size}",
               "rotation=${params.rotation}",
               "filter=${params.filter}",
               "quality=${params.quality}",
               "format=${params.format}"].join("&")
    }

    void processAsync(File input, File output, String identifier, Map params) {
        ffmpegSlaveAvailable()
        Promise p = task {
            progressService.start(output)
            log.info "Slave FFmpeg (Request): $input $output $params"
            try {
                new RestBuilder().get(ffmpegApi + composeSlaveQueryString(input, output, identifier, params))
            } catch(any) {
                println "##"
            }

        }
        onComplete([p]) {
            log.info "Slave FFmpeg (Complete): $input $output $params"
            progressService.stop(output)
        }
        onError([p]) { Throwable err ->
            log.info "Slave FFmpeg (Error): $input $output $params"
            progressService.stop(output)
            log.info "Master FFmpeg: ${err.printStackTrace()}."
            throw new SlaveFfmpegBusyException()
        }
    }

    void processSync(File input, File output, String identifier, Map params) {
        ffmpegSlaveAvailable()
        progressService.start(output)
        RestBuilder rest = new RestBuilder()
        rest.get(ffmpegApi + composeSlaveQueryString(input, output, identifier, params))
        progressService.stop(output)
    }
}