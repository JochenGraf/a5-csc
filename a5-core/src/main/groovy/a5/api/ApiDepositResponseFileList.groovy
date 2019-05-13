package a5.api

class ApiDepositResponseFileList {

    private final static String[] SYSTEM_FILES = [".progress"]

    ApiDepositResponseFileInfo[] files

    ApiDepositResponseFileList(File[] files_, String userDir) {
        def infos = []
        files_.each {
            if (!it.name.endsWith(".progress")) {
                infos << new ApiDepositResponseFileInfo(it, userDir)
            }
        }
        files = infos
    }
}