# a5

## Demo

https://grails-prod.rrz.uni-koeln.de/ka3-a5-core/

## Contents

* `/a5-core` Source Code of the APIs and API documentation
* `/a5-slave-ffmpeg` Source Code of the FFmpeg Microservice

## Prerequisites

* Ubuntu 64 bit
* Java 8

## Installation

### 1 Install all services

Get a running installation for testing purposes on Ubuntu.

```
$ ./a5.sh install
```

This does not mess up your Ubuntu installation! Packages are
installed locally only:
* `~/.sdkman` https://sdkman.io/, https://grails.org/
* `./dist/elasticsearch` https://www.elastic.co
* `./dist/testdata` CMDI Metadata, EAF Files, Media for testing purposes
* `./dist/tomcat` http://tomcat.apache.org/
* `~/a5-core.groovy` a5 configuration file

Removing those files and directories uninstalls all services.

### 2 Start all services

Make sure that there are no other services running on ports 8080 and 9200.

```
$ ./a5.sh start
```

Running services:
* http://localhost:8080/a5-core
* http://localhost:8080/a5-slave-ffmpeg
* http://localhost:9200 (ElasticSearch)

### 3 Ingest the Test Datasets

Ingests the test repositories `./dist/testdata/a5-1` and `./dist/testdata/a5-2` according to the
repository configuration files:
* `./dist/testdata/a5-1/repository.xml`
* `./dist/testdata/a5-2/repository.xml`

```
$ ./a5.sh ingest
```
This takes some time.

### 4 Stop all services

Stops all services.

```
$ ./a5.sh stop
```

## Configuration

### Grails Configuration

`~/a5.groovy`
```$groovy
a5 {
    slave {
        ffmpeg {
            server = "http://localhost:8080/a5-slave-ffmpeg"
        }
        elasticsearch {
            server = "http://localhost:9200"
        }
    }
    repository {
        "a5-1" {
            docBase = "/home/me/IdeaProjects/a5/dist/testdata/a5-1"
            docBaseCache = "/home/me/IdeaProjects/a5/dist/testdata/a5-1.cache"
            docBaseLog = "/home/me/IdeaProjects/a5/dist/testdata/a5-1.log"
            config = "/home/me/IdeaProjects/a5/dist/testdata/a5-1/repository.xml"
        }
        "a5-2" {
            docBase = "/home/me/IdeaProjects/a5/dist/testdata/a5-2"
            docBaseCache = "/home/me/IdeaProjects/a5/dist/testdata/a5-2.cache"
            docBaseLog = "/home/me/IdeaProjects/a5/dist/testdata/a5-2.log"
            config = "/home/me/IdeaProjects/a5/dist/testdata/a5-2/repository.xml"
        }
    }
}
```
New repositories can be added here.

### Repository Configuration

Example: `./dist/testdata/a5-1/repository.xml`
```$xml
<repository>
    <index>
        <object>
            <fileFilter>.*.cmdi</fileFilter>
            <mapper type="groovy">a5.mapper.nl.mpi.cmdi.CmdiObjectCollectionMapper</mapper>
        </object>
        <query>
            <fileFilter>.*.cmdi</fileFilter>
            <namespace>
                <cmd>http://www.clarin.eu/cmd/</cmd>
            </namespace>
            <mapper type="xquery">
                <id fulltext="false">
                    /cmd:CMD/cmd:Header/cmd:MdSelfLink
                </id>
                <Title facet="false">
                    /cmd:CMD/cmd:Components/cmd:lat-session/cmd:Title |
                    /cmd:CMD/cmd:Components/cmd:lat-session/cmd:Name |
                    /cmd:CMD/cmd:Components/cmd:lat-corpus/cmd:Corpus/cmd:Title |
                    /cmd:CMD/cmd:Components/cmd:lat-corpus/cmd:Corpus/cmd:Name
                </Title>
                <Description>
                    //cmd:lat-session//cmd:Description |
                    //cmd:lat-corpus//cmd:Description
                </Description>
                <Language facet="true">
                    /cmd:CMD/cmd:Components/cmd:Session/cmd:MDGroup/cmd:Content/cmd:Content_Languages/cmd:Content_Language/cmd:Name |
                    /cmd:CMD/cmd:Components/cmd:Song/cmd:Language/cmd:LanguageName |
                    //@xml:lang
                </Language>
                <Format facet="true">
                    /cmd:CMD/cmd:Resources/cmd:ResourceProxyList/cmd:ResourceProxy/cmd:ResourceType/@mimetype |
                    /cmd:CMD/cmd:Components/cmd:Session/cmd:Resources/cmd:WrittenResource/cmd:Format
                </Format>
            </mapper>
        </query>
        <annotation>
            <fileFilter>.*.eaf</fileFilter>
            <mapper type="groovy">a5.mapper.nl.mpi.eaf.EafAnnotationMapper</mapper>
        </annotation>
        <oai>
            <fileFilter>.*.cmdi</fileFilter>
            <repositoryName>A5 Test Repository 1</repositoryName>
            <adminEmails>
                <adminEmail>jochen.graf@uni-koeln.de</adminEmail>
                <adminEmail>diensteentwicklung-rrzk@uni-koeln.de</adminEmail>
            </adminEmails>
            <metadataFormats>
                <metadataFormat>
                    <prefix>oai_dc</prefix>
                    <schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</schema>
                    <namespace>http://www.openarchives.org/OAI/2.0/oai_dc/</namespace>
                </metadataFormat>
                <metadataFormat>
                    <prefix>other</prefix>
                    <schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</schema>
                    <namespace>http://www.openarchives.org/OAI/2.0/other/</namespace>
                </metadataFormat>
                <metadataFormat>
                    <prefix>nodata</prefix>
                    <schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</schema>
                    <namespace>http://www.openarchives.org/OAI/2.0/nodata/</namespace>
                </metadataFormat>
            </metadataFormats>
            <mapper type="xslt">
                <oai_dc script="cmdi2oai_dc.xsl"/>
                <other script="cmdi2oai_dc.xsl"/>
            </mapper>
        </oai>
    </index>
</repository>
```
The Media API depends on the Object API.

Object API configuration:
* define a `<fileFilter>` to match all CMDI metadata files that reference media files
* select a object mapper. `a5.mapper.nl.mpi.cmdi.CmdiObjectCollectionMapper` should work for the CMDI metadata at MPI,
see: `./a5-core/src/main/groovy/a5/mapper/nl/mpi/cmdi/CmdiObjectCollectionMapper.groovy`
* if the mapping was successful, media can be accessed by their `<cmd:ResourceRef>`, e.g.,
https://grails-prod.rrz.uni-koeln.de/ka3-a5-core/api/media/lac/hdl:11341/00-0000-0000-0000-1C93-8
