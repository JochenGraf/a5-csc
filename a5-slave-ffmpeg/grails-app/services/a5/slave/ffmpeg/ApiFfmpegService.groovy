package a5.slave.ffmpeg

import grails.util.GrailsStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Paths

class ApiFfmpegService {

    protected Logger log = LoggerFactory.getLogger(getClass())

    def servletContext

    String process(String input, String output, String section, String region,
                   String size, String rotation, String filter, String quality, String format) {
        prepare(output)
        def args = args(input, output, section, region, size, rotation, filter, quality, format)
        def command = bin() + " " + args
        log.info command
        def process = command.execute()
        def (procOutput, procError) = new StringWriter().with { o ->
            new StringWriter().with { e ->
                process.waitForProcessOutput(o, e)
                [ o, e ]*.toString()
            }
        }
        log.info procOutput
        log.info procError
        procOutput + procError
    }

    String args(String input, String output, String section, String region, String size,
                String rotation, String filter, String quality, String format) {
        def sectionMap = parseSection(section)
        def regionMap = parseRegion(region)
        def sizeMap = parseSize(size)
        def filterMap = parseFilter(filter)
        def argsSection = argsSection(sectionMap)
        def oneFrame = section != "full" && sectionMap.to == null ? "-vframes 1" : ""
        def argsRegion = argsRegion(regionMap)
        def argsRotation = argsRotation(rotation)
        def argsSize = argsSize(sizeMap)
        def argsFilter = argsFilter(filterMap)
        def argsQuality = argsQuality(quality)
        def argsPipe = argsPipe(argsRegion, argsSize, argsRotation, argsFilter, sizeMap, filterMap)
        [argsSection[0], argsSection[1], argsSection[2], argsSection[3], "-y", "-i", input, argsQuality.join(" "),
         argsPipe.join(" "), oneFrame, output].join(" ")
    }

    String version() {
        def command = bin() + " -version"
        def proc = command.execute()
        proc.waitFor()
        if (proc.exitValue() > 0) {
            log.info "$proc.errorStream"
            throw new ApiFfmpegException("$proc.errorStream", 500)
        }
        log.info proc.text
        proc.text
    }

    private Map parseSection(String param) {
        switch(param) {
            case "full":
                return [seek: null, to: null]
                break
            case { it?.getAt(0) == "," }:
                return [seek: null, to: param[1..param.length() - 1]]
                break
            case { it?.getAt((it?.length()?:1 as Integer)- 1) == "," }:
                return [seek: param[0..param.length() - 2], to: null]
                break
            case { it?.split(",")?.length == 2 }:
                def tokens = param.split(",")
                return [seek: tokens[0], to: tokens[1]]
                break
            default:
                throw new RuntimeException("Invalid parameter.")
                break
        }
    }

    private Map parseRegion(String param) {
        if (param == "full") {
            return [
                    offsetx: null,
                    offsety: null,
                    width: null,
                    height: null
            ]
        } else {
            def tokens = param.split(",")
            if (tokens.length == 4) {
                return [
                        offsetx: tokens[0],
                        offsety: tokens[1],
                        width: tokens[2],
                        height: tokens[3]
                ]
            } else {
                throw new RuntimeException("Invalid parameter.")
            }
        }
    }

    private Map parseSize(String param) {
        def tokens = param.split(",")
        if (param == "full") {
            return [width: null, height: null]
        } else if (param[0] == ",") {
            return [width: null, height: param[1..param.length() - 1]]
        } else if (param[param.length() - 1] == ",") {
            return [width: param[0..param.length() - 2], height: null]
        } else if (tokens.length == 2) {
            return [width: tokens[0], height: tokens[1]]
        } else {
            throw new RuntimeException("Invalid parameter.")
        }
    }

    private Map parseFilter(String param) {
        return [
                none: param.contains("none"),
                gray: param.contains("gray"),
                thumbnail: param.contains("thumbnail"),
                waveform: param.contains("waveform"),
                spectrum: param.contains("spectrum")
        ]
    }

    /**
     * https://trac.ffmpeg.org/wiki/Seeking
     */
    private ArrayList argsSection(Map sectionMap) {
        if (sectionMap.seek == null && sectionMap.to == null) {
            return ["", "", "", ""]
        } else if (sectionMap.seek == null && sectionMap.to != null) {
            return ["", "", "-t", sectionMap.to]
        } else if (sectionMap.seek != null && sectionMap.to == null) {
            return ["-ss", sectionMap.seek, "", ""]
        } else if (sectionMap.seek != null && sectionMap.to != null) {
            return ["-ss", sectionMap.seek, "-t", sectionMap.to]
        } else {
            return ["", "", "", ""]
            // silent catch
        }
    }

    private ArrayList argsRegion(Map regionMap) {
        if (regionMap.offsetx == null) {
            return [""]
        } else {
            return [
                    "crop=" +
                    [regionMap.offsetx, regionMap.offsety, regionMap.width, regionMap.height].join(":")
            ]
        }
    }

    /**
     * https://trac.ffmpeg.org/wiki/Scaling%20(resizing)%20with%20ffmpeg
     */
    private ArrayList argsSize(Map sizeMap) {
        if (sizeMap.width == null && sizeMap.height == null) {
            return [""]
        } else {
            def width = sizeMap.width == null ? "-1" : sizeMap.width
            def height = sizeMap.height == null ? "-1" : sizeMap.height
            return ["scale=" + width + ':' + height]
        }
    }

    private ArrayList argsRotation(String rotation) {
        if (rotation == "0") {
            return [""]
        } else if (rotation == "90") {
            return ["transpose=2,transpose=2,transpose=2"]
        } else if (rotation == "180") {
            return ["transpose=2,transpose=2"]
        } else if (rotation == "270") {
            return ["transpose=2"]
        }else{
            return [""]
        }
    }

    private ArrayList argsFilter(Map filterMap) {
        if (filterMap.none) {
            return [""]
        } else if (filterMap.gray) {
            return ["format=gray"]
        } else {
            [""]
        }
    }

    private ArrayList argsQuality(String quality) {
        if (quality == "default") {
            [""]
        } else if (quality == "high") {
            return ["-qscale", "1"]
        } else if (quality == "medium") {
            return ["-qscale", "16"]
        } else if (quality == "low") {
            return ["-qscale", "31"]
        } else
            [""]
    }

    private ArrayList argsPipe(ArrayList argsRegion, ArrayList argsSize, ArrayList argsRotation, ArrayList argsFilter,
                               Map sizeMap, Map filterMap) {
        def width = sizeMap.width == null ? "6000" : sizeMap.width
        def height = sizeMap.height == null ? "250" : sizeMap.height
        if (filterMap.spectrum) {
            return ["-filter_complex", "aresample=6000,lowpass=f=3000,showspectrumpic=s=" + width + "x" + height +
                    ":legend=0:gain=10,lutrgb=r=negval:g=negval:b=negval,format=gray"]
        } else if (filterMap.waveform) {
            return ["-filter_complex", "showwavespic=s=" + width + "x" + height +
                    ":colors=Gray|Black"]
        } else {
            def args = [argsRegion[0], argsSize[0], argsRotation[0], argsFilter[0]]
            args.removeAll([""])
            if (args.size != 0) {
                return ["-vf", "[in]" + args.join(",") + "[out]"]
            } else {
                return [""]
            }
        }
    }

    private String normalize(String path) {
        Paths.get(path)?.normalize()?.toString()
    }

    private void prepare(String output) {
        output = normalize(output)
        def folder = new File(GrailsStringUtils.substringBeforeLast(output, "/"))
        if (folder != null && !folder.exists()) {
            folder.mkdirs()
        }
    }

    private String bin() {
        def fsPath = servletContext.getRealPath("WEB-INF/bin/ffmpeg")
        fsPath = fsPath.startsWith("/") ? fsPath : System.getProperty("user.dir") + "/" + fsPath
        def executable = new File(fsPath)
        executable.setReadable(true, false)
        executable.setWritable(true, false)
        executable.setExecutable(true, false)
        if (!executable.exists()) {
            throw new ApiFfmpegException("FFmpeg executable does not exist.", 500)
        }
        if (!executable.canExecute()) {
            throw new ApiFfmpegException("Not allowed to execute FFmpeg.", 500)
        }
        fsPath
    }
}
