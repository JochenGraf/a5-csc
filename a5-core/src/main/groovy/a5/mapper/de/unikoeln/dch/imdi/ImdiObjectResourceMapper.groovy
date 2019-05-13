package a5.mapper.de.unikoeln.dch.imdi

import a5.api.util.ApiUtil
import a5.mapper.MapperGroovy
import a5.mapper.MapperObjectResource
import grails.util.GrailsStringUtils
import groovy.util.slurpersupport.GPathResult

class ImdiObjectResourceMapper extends MapperGroovy implements MapperObjectResource {

    String mapId() {
        this.parsed.@ArchiveHandle.text()
    }

    String mapLabel(){
        this.file.name
    }

    String mapContentType() {
        ApiUtil.getContentType(this.file)
    }

    ArrayList<String> mapRelatedTo() {
        ArrayList<String> related = []
        if (isEafResource()) {
            related = findRelatedMediaForEaf1()
        }
        related.size() == 0 ? null : related
    }

    private boolean isEafResource() {
        ApiUtil.hasContentType(this.file, "text/eaf+xml")
    }

    private String normalizedFileName(String fileName) {
        String normalize1 = new File(fileName).toPath().fileName
        String normalize2 = GrailsStringUtils.substringBeforeLast(normalize1, ".")
        String normalize3 = normalize2.replace(" ", "").replace(".", "_")
        normalize3
    }

    private ArrayList<String> findRelatedMediaForEaf1() {
        def related = []
        File eafFile = new File(this.config.docBase + this.object.fileUri)
        ArrayList<String> eafMediaFiles = []
        if (eafFile.exists()) {
            GPathResult eaf = new XmlSlurper().parse(eafFile)
            eaf.HEADER.MEDIA_DESCRIPTOR.each {
                eafMediaFiles << normalizedFileName(it.@MEDIA_URL.text())
            }
            this.parsed.parent().parent()."*".ResourceLink.each {
                def fileName = normalizedFileName(it.text())
                if (eafMediaFiles.contains(fileName)) {
                    if (ApiUtil.isAudio(it.text()) || ApiUtil.isVideo(it.text())) related << it.@ArchiveHandle.text()
                }
            }
        }
        related.size() > 0 ? related : findRelatedMediaForEaf2()
    }

    private ArrayList<String> findRelatedMediaForEaf2() {
        def related = []
        def eafFileName = normalizedFileName(this.parsed.text())
        this.parsed.parent().parent()."*".ResourceLink.each {
            if (normalizedFileName(it.text()) == eafFileName) {
                if (ApiUtil.isAudio(it.text()) || ApiUtil.isVideo(it.text())) related << it.@ArchiveHandle.text()
            }
        }
        related
    }
}