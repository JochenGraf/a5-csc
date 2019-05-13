package a5.config

import grails.util.Holders
import groovy.util.slurpersupport.GPathResult

class Config {

    String repositoryName

    File configFile

    GPathResult parsed

    String docBase

    String docBaseCache

    String docBaseDeposit

    Config(String repositoryName) {
        this.repositoryName = repositoryName
        this.configFile = new File(Holders.grailsApplication.config.a5.repository[repositoryName].config)
        this.parsed = new XmlSlurper().parse(configFile)
        this.docBase = Holders.config.a5.repository[repositoryName].docBase
        this.docBaseCache = Holders.config.a5.repository[repositoryName].docBaseCache
        this.docBaseDeposit = Holders.config.a5.repository[repositoryName].docBaseDeposit
    }

    boolean isValid() {
        true //TODO
    }
}