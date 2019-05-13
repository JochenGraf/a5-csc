package a5.slave.ffmpeg

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApiFfmpegController {

    protected Logger log = LoggerFactory.getLogger(getClass())

    def apiFfmpegService

    def index() {
        params.debug ? debug() : process()
    }

    def process() {
        if (params.input == null || params.output == null) {
            throw new ApiFfmpegException("Parameter input and output are required.", 500)
            return
        }
        String text = apiFfmpegService.process(params.input, params.output, params.section, params.region, params.size,
                params.rotation, params.filter, params.quality, params.format)
        serialize(text)
    }

    def debug() {
        String text = apiFfmpegService.args(params.input, params.output, params.section, params.region, params.size,
                params.rotation, params.filter, params.quality, params.format)
        serialize(text)
    }

    def version() {
        serialize(apiFfmpegService.version())
    }

    def throwException(ApiFfmpegException exception) {
        log.info exception.message
        render(status: exception.status, text: exception.message)
    }

    private serialize(String text) {
        log.info text
        render(status: 200, text: text.trim().replaceAll(" +", " "), contentType: "text/plain")
    }
}
