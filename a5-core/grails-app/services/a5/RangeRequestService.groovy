package a5

import a5.api.MultipartRangeNotSupportedException
import a5.api.RangeNotSatisfiableException
import a5.http.HttpRange
import a5.api.util.ApiUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletOutputStream

class RangeRequestService {

    private static int bufferSize = 2048
    protected Logger log = LoggerFactory.getLogger(getClass())

    public HttpServletResponse serveFileOrChunk(HttpServletRequest request, HttpServletResponse response, File file) {
        if(!file.exists()) {
            log.error "file not found: $file"
            response.sendError(404)
        }
        else {
            def contentType = ApiUtil.getContentType(file)
            // tell the client that we support range requests
            response.setHeader("Accept-Ranges", "bytes")
            // tell the client which content type we serve
            response.setContentType(contentType)
            // header request?
            if (request.method == "HEAD") {

            }
            // serve byte range if requested
            else if (request.getHeader("Range")) {
                try {
                    serveRange(request, response, file)
                } catch (any) {
                    log.info any.toString()
                    // Silent catch.
                    // Necessary, since range requests are long-lasting requests.
                    // If the user reloads the browser window during audio/video
                    // playback, it may happen that the former range request has
                    // not yet been completed. We catch a "response.getOutputStream()
                    // has already been called" exception here.
                }
            }
            // Used to serve files that are either not binary container formats
            // by nature, e.g. text or XML files, or used as the starting point of
            // a range range request: the client cannot know beforehand that this
            // controller supports range requests. Clients can send a "header-only"
            // request at first. The second request then can be a range request, if the
            // client decides in this way.
            else {
                try {
                    response.outputStream << file.newInputStream()
                } catch (any) {
                    log.info any.toString()
                    // Silent catch.
                    // Same as above, in the case, the former range request has not
                    // yet been completed.
                }
            }
        }
        response
    }

    public void serveRange(request, response, file) {
        def ranges = HttpRange.parseRange(request.getHeader("Range"), file.length())
        if (ranges == null) {
            response.addHeader("Content-Range", "bytes */" + file.length())
            throw new RangeNotSatisfiableException()
        } else if (ranges.size() == 1) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT)
            def range = ranges.get(0)
            response.addHeader("Content-Range", "bytes $range.start-$range.end/$range.length")
            long length = range.end - range.start + 1
            if (length < Integer.MAX_VALUE) {
                response.setContentLength((int) length)
            } else {
                response.setHeader("content-length", "" + length)
            }
            try {
                response.setBufferSize(bufferSize);
            } catch (IllegalStateException e) {
                log.info e.toString()
            }
            copy(file, response.getOutputStream(), range)
        } else {
            throw new MultipartRangeNotSupportedException()
        }
    }

    private void copy(File file, ServletOutputStream ostream, HttpRange range)
            throws IOException {
        def istream = file.newInputStream()
        copyRange(istream, ostream, range.start, range.end)
        istream.close()
    }

    private void copyRange(InputStream istream, ServletOutputStream ostream, long start, long end) {
        try {
            istream.skip(start)
        } catch (IOException e) {
            log.info e.toString()
        }
        long bytesToRead = end - start + 1
        byte[] buffer = new byte[bufferSize]
        int len = buffer.length
        while ((bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = istream.read(buffer)
                if (bytesToRead >= len) {
                    ostream.write(buffer, 0, len)
                    bytesToRead -= len
                } else {
                    ostream.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                log.info e.toString()
                len = -1;
            }
            if (len < buffer.length) break
        }
    }
}
