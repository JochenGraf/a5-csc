package a5.slave.ffmpeg

class ApiFfmpegException extends RuntimeException {

    public int status

    ApiFfmpegException(String message, int status) {
        super(message)
        this.status = status
    }
}
