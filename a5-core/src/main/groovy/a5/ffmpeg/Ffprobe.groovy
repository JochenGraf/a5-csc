package a5.ffmpeg

import grails.util.Holders

class Ffprobe {

    private static String bin

    static {
        bin = Holders.grailsApplication.config.a5.bin?.ffprobe ?:
                Holders.servletContext.getRealPath("WEB-INF/bin/ffprobe").toString()
        new File(bin).setExecutable(true)
    }

    static Float duration(File file) {
        String command = bin + " -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " +
                file.toString()
        def proc = command.execute()
        proc.out.close()
        proc.waitFor()
        Float duration
        try {
            duration = (proc.text.trim() + "f").toFloat()
        } catch(any) {
            duration = null
        }
        duration
    }
}