package a5.com

import a5.api.util.ApiUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgressService {

    protected Logger log = LoggerFactory.getLogger(getClass())

    boolean isInProgress(File file) {
        new File(file.absolutePath + ".progress").exists()
    }

    void start(File file) {
        def f = new File(file.absolutePath + ".progress")
        ApiUtil.preparePath(file)
        if (!f.exists()) {
            f.createNewFile()
        }
    }

    void stop(File file) {
        def f = new File(file.absolutePath + ".progress")
        if (f.exists()) {
            f.delete()
        }
    }

    void writeInfo(File file, String info) {
        def f = new File(file.absolutePath + ".progress")
        f.write info
    }

    String readInfo(File file) {
        def f = new File(file.absolutePath + ".progress")
        f.text
    }
}
