package a5.http

import javax.servlet.http.HttpServletRequest

class HttpRange {

    public long start

    public long end

    public long length

    public boolean isFirstRange() {
        start == 0
    }

    public boolean isLastRange() {
        (end + 1) == length
    }

    public boolean isNextRange(HttpRange other) {
        start == (other.end + 1)
    }

    private boolean isValid() {
        if (end >= length) end = length - 1
        return ((start >= 0) && (end >= 0) && (start <= end) && (length > 0))
    }

    public static ArrayList<HttpRange> parseRange(String rangeHeader, long fileLength) {
        if (rangeHeader == null) return null
        rangeHeader = rangeHeader.substring(6)
        ArrayList<HttpRange> ranges = new ArrayList<HttpRange>()
        StringTokenizer commaTokenizer = new StringTokenizer(rangeHeader, ",")
        while (commaTokenizer.hasMoreTokens()) {
            String rangeDefinition = commaTokenizer.nextToken().trim()
            HttpRange range = new HttpRange()
            range.length = fileLength
            int dashPos = rangeDefinition.indexOf('-')
            if (dashPos == -1) {
                return null
            } else if (dashPos == 0) {
                try {
                    long offset = Long.parseLong(rangeDefinition)
                    range.start = fileLength + offset
                    range.end = fileLength - 1
                } catch (NumberFormatException e) {
                    return null
                }
            } else {
                try {
                    range.start = Long.parseLong(rangeDefinition.substring(0, dashPos))
                    if (dashPos < rangeDefinition.length() - 1) {
                        def end = rangeDefinition.substring(dashPos + 1, rangeDefinition.length())
                        range.end = Long.parseLong(end)
                    }
                    else {
                        range.end = fileLength - 1
                    }
                } catch (NumberFormatException e) {
                    return null
                }
            }
            if (!range.isValid()) return null
            ranges.add(range)
        }
        return ranges
    }

    public static HttpRange parseContentRange(String rangeHeader) {
        rangeHeader = rangeHeader.substring(6).trim()
        int dashPos = rangeHeader.indexOf('-')
        int slashPos = rangeHeader.indexOf('/')
        if (dashPos == -1) return null
        if (slashPos == -1) return null
        HttpRange range = new HttpRange()
        try {
            range.start = Long.parseLong(rangeHeader.substring(0, dashPos))
            range.end = Long.parseLong(rangeHeader.substring(dashPos + 1, slashPos))
            range.length = Long.parseLong(rangeHeader.substring(slashPos + 1, rangeHeader.length()))
        } catch (NumberFormatException e) {
            return null
        }
        if (!range.isValid()) return null
        range
    }

    public static HttpRange parseContentRange(HttpServletRequest request) {
        String rangeHeader = request.getHeader("Content-Range")
        if (rangeHeader == null) return null
        if (!rangeHeader.startsWith("bytes")) return null
        parseContentRange(rangeHeader)
    }


    public String toString() {
        "bytes $start-$end/$length"
    }
}