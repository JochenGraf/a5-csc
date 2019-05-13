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
            docBase = "%PWD%/dist/testdata/a5-1"
            docBaseCache = "%PWD%/dist/testdata/a5-1.cache"
            docBaseLog = "%PWD%/dist/testdata/a5-1.log"
            config = "%PWD%/dist/testdata/a5-1/repository.xml"
        }
        "a5-2" {
            docBase = "%PWD%/dist/testdata/a5-2"
            docBaseCache = "%PWD%/dist/testdata/a5-2.cache"
            docBaseLog = "%PWD%/dist/testdata/a5-2.log"
            config = "%PWD%/dist/testdata/a5-2/repository.xml"
        }
    }
}

