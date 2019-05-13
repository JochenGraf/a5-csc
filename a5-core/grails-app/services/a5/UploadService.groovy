package a5

import a5.http.HttpRange
import javax.servlet.http.HttpServletResponse

class UploadService {

    private final static int BUFFER_SIZE = 4096

    def progressService

    private void saveFile(requestFile, String targetPath) {
        prepareSave(targetPath, requestFile.getSize())
        save(requestFile, targetPath, 0)
    }

    private boolean checkChunkValid(File targetFile, HttpRange range) {
        def progressInfo = progressService.readInfo(targetFile)
        def oldRange = HttpRange.parseContentRange(progressInfo.trim())
        def valid = range.isNextRange(oldRange) // we only allow consecutive chunks
        if (!valid) {
            // User tries to upload one and the same file twice.
            // -> Abort the user's second upload request.
            throw new RuntimeException("Invalid Chunk. Aborting upload.")
        }
        valid
    }

    private void finishFirstChunk(String targetPath, HttpRange range) {
        def targetFile = new File(targetPath)
        progressService.start(targetFile)
        progressService.writeInfo(targetFile, range.toString())
    }

    private void finishLastChunk(String targetPath) {
        def targetFile = new File(targetPath)
        progressService.stop(targetFile)
    }

    private void finishNextChunk(String targetPath, HttpRange range) {
        def targetFile = new File(targetPath)
        progressService.writeInfo(targetFile, range.toString())
    }

    private void saveChunk(requestFile, String targetPath, HttpRange range, HttpServletResponse response) {
        def targetFile = new File(targetPath)
        switch(range) {
            case { it.isFirstRange() }:
                prepareSave(targetPath, range.length)
                save(requestFile, targetPath, range.start)
                finishFirstChunk(targetPath, range)
                break
            case { it.isLastRange() }:
                save(requestFile, targetPath, range.start)
                finishLastChunk(targetPath)
                break
            default:
                if (checkChunkValid(targetFile, range)) {
                    save(requestFile, targetPath, range.start)
                    finishNextChunk(targetPath, range)
                }
                break
        }
        response.setHeader("Range", range.toString())
    }

    private void resume(targetPath, HttpServletResponse response) {
        def targetFile = new File(targetPath)
        def oldRangeHeader = progressService.readInfo(targetFile)
        def oldRange = HttpRange.parseContentRange(oldRangeHeader)
        response.setHeader("Range", oldRange.toString())
    }

    private void resumeOrSaveChunk(requestFile, String targetPath, HttpRange range, HttpServletResponse response) {
        def targetFile = new File(targetPath)
        if (range.isFirstRange() && progressService.isInProgress(targetFile)) { // resume?
            resume(targetPath, response)
        } else {
            saveChunk(requestFile, targetPath, range, response)
        }
    }

    private void prepareSave(String targetPath, long fileLength) {
        RandomAccessFile randomAccessFile = null
        try {
            randomAccessFile = new RandomAccessFile(targetPath, "rw")
            randomAccessFile.write("".getBytes())
            randomAccessFile.setLength(fileLength)
        } finally {
            if (randomAccessFile != null) randomAccessFile.close()
        }
    }

    private void save(requestFile, String targetPath, long offset) {
        RandomAccessFile randomAccessFile = null
        BufferedInputStream requestBufInStream = null
        try {
            randomAccessFile = new RandomAccessFile(targetPath, "rw")
            randomAccessFile.seek(offset)
            byte[] buffer = new byte[BUFFER_SIZE]
            int numBytesRead
            requestBufInStream = new BufferedInputStream(requestFile.getInputStream(), BUFFER_SIZE)
            while ((numBytesRead = requestBufInStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, numBytesRead)
            }
        } finally {
            if (randomAccessFile != null) randomAccessFile.close()
            if (requestBufInStream != null) requestBufInStream.close()
        }
    }
}