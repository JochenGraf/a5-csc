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

Example: `./dist/testdata/a5-2/repository.xml`
```$xml
<repository>
    <index>
        <object>
            <fileFilter>.*.imdi</fileFilter>
            <mapper type="groovy">a5.mapper.de.unikoeln.dch.imdi.ImdiObjectCollectionMapper</mapper>
        </object>
        <query>
            <fileFilter>.*.imdi</fileFilter>
            <namespace>
                <imdi>http://www.mpi.nl/IMDI/Schema/IMDI</imdi>
            </namespace>
            <mapper>
                <id fulltext="false">
                    /imdi:METATRANSCRIPT/@ArchiveHandle
                </id>
                <Title>
                    //imdi:Session/imdi:Title
                </Title>
                <Country>
                    //imdi:Session/imdi:MDGroup/imdi:Location/imdi:Country
                </Country>
                <Region>
                    //imdi:Session/imdi:MDGroup/imdi:Location/imdi:Region
                </Region>
                <Description>
                    //imdi:Session/imdi:Description |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Description
                </Description>
                <Keywords>
                    //imdi:Session/imdi:MDGroup/imdi:Keys/imdi:Key |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Genre |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:SubGenre |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:CommunicationContext/imdi:Interactivity |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:CommunicationContext/imdi:Involvement |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:CommunicationContext/imdi:SocialContext |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:CommunicationContext/imdi:EventStructure |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:CommunicationContext/imdi:Channel |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Task |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Modalities |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Subject |
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Keys/imdi:Key
                </Keywords>
                <ProjectDisplayName>
                    //imdi:Session/imdi:MDGroup/imdi:Project/imdi:Title
                </ProjectDisplayName>
                <LegacyBlob>
                    //imdi:Session/imdi:MDGroup/imdi:Project/imdi:Contact/imdi:Organisation
                    //imdi:Session/imdi:MDGroup/imdi:Actors/imdi:Actor/imdi:Description
                </LegacyBlob>
                <ProjectDescription>
                    //imdi:Session/imdi:MDGroup/imdi:Project/imdi:Description
                </ProjectDescription>
                <ObjectLanguage>
                    //imdi:Session/imdi:MDGroup/imdi:Content/imdi:Languages/imdi:Language/imdi:Name
                </ObjectLanguage>
                <Creator>
                    //imdi:Session/imdi:MDGroup/imdi:Actors/imdi:Actor/imdi:FullName[../imdi:Role/text() = "Researcher"]
                </Creator>
                <ResourceType>
                    //imdi:Session/imdi:Resources/imdi:MediaFile/imdi:Type
                </ResourceType>
            </mapper>
        </query>
        <annotation>
            <fileFilter>.*.eaf</fileFilter>
            <mapper type="groovy">a5.mapper.nl.mpi.eaf.EafAnnotationMapper</mapper>
        </annotation>
        <oai>
            <fileFilter>.*.imdi</fileFilter>
            <repositoryName>A5 Test Repository 2</repositoryName>
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
                    <prefix>cmdi</prefix>
                    <schema>https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614023/xsd</schema>
                    <namespace>http://www.clarin.eu/cmd/1</namespace>
                </metadataFormat>
            </metadataFormats>
            <mapper type="xslt">
                <oai_dc script="imdi2oai_dc.xsl"></oai_dc>
                <cmdi script="imdi2cmdi.xsl"></cmdi>
            </mapper>
        </oai>
    </index>
</repository>
```
