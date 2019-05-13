package a5.index

import a5.config.ConfigIndex
import a5.mapper.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait IndexTraitService {

    Logger log = LoggerFactory.getLogger(getClass())

    def grailsApplication

    abstract void mapFile(String repositoryName, String indexType, File file, String docBase, Mapper mapper)

    void fileSystemRead(ConfigIndex config, String indexType, elasticService, indexService) {
        String docBase = grailsApplication.config.a5.repository[config.repositoryName].docBase
        Mapper mapper = config?.getMapper()
        ArrayList<File> files = []
        new File(config.docBase).eachDirRecurse() { dir ->
            dir.eachFileMatch(config.fileFilter) { file ->
                if (config.fileFilterExclude != null && file =~ config.fileFilterExclude){
                    progress(config.repositoryName, "Ignoring [$config.repositoryName,$indexType] $file")
                } else {
                    files << file
                }
            }
        }
        //TODO: progress
        files.each { file ->
            mapFile(config.repositoryName, indexType, file, docBase, mapper)
        }
    }

    void progress (repository, message){
        log.debug "repository $repository, Message: $message"
    }
}