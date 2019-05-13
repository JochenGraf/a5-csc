package a5

class UrlMappings {

    static mappings = {

        "/"(view: "/index")

        "/angular2"(view: "/angular2")

        "/angular2/**"(view: "/angular2")

        group "/api/object/$repository", {
            "/Object\\($identifier**\\)" {
                controller = "ApiObject"
                action = "object"
            }
            "/Objects" {
                controller = "ApiObject"
                action = "objects"
            }
        }

        group "/api/query", {
            "/$repository" {
                controller = "ApiQuery"
                action = "query"
            }
        }

        group "/api/media/$repository", {
            "/$identifier" {
                controller = "ApiMedia"
                action = "file"
            }
            "/$prefix/$identifier" {
                controller = "ApiMedia"
                action = "file"
            }
            "/$identifier.$format" {
                controller = "ApiMedia"
                action = "conversion"
            }
            "/$prefix/$identifier.$format" {
                controller = "ApiMedia"
                action = "conversion"
            }
            "/$prefix/$identifier/$section/$region/$size/$rotation/$filter/$quality.$format" {
                controller = "ApiMedia"
                action = "manipulation"
            }
            "/$identifier/$section/$region/$size/$rotation/$filter/$quality.$format" {
                controller = "ApiMedia"
                action = "manipulation"
            }
        }

        group "/api/annotation/$repository", {
            "/Manifest\\($identifier**\\)" {
                controller = "ApiAnnotation"
                action = "manifest"
            }
            "/Canvas\\($identifier**\\)" {
                controller = "ApiAnnotation"
                action = "canvas"
            }
            "/Layer\\($identifier**\\)" {
                controller = "ApiAnnotation"
                action = "layer"
            }
            "/Layers" {
                controller = "ApiAnnotation"
                action = "layers"
            }
            "/Annotation\\($identifier**\\)" {
                controller = "ApiAnnotation"
                action = "annotation"
            }
            "/Annotations" {
                controller = "ApiAnnotation"
                action = "annotations"
            }
        }

        group "/api/deposit/$repository", {
            "/upload" {
                controller = "ApiDeposit"
                action = "upload"
            }
            "/list" {
                controller = "ApiDeposit"
                action = "list"
            }
            "/stream" {
                controller = "ApiDeposit"
                action = "stream"
            }
            "/delete" {
                controller = "ApiDeposit"
                action = "delete"
            }
            "/info" {
                controller = "ApiDeposit"
                action = "info"
            }
        }

        group "/api/oai", {
            "/$repository" {
                controller = "OaiDataProvider"
            }
        }

        group "/api/admin", {
            "/$repository/config" {
                controller = "ApiAdmin"
                action = "config"
            }
            "/$repository/index/config" {
                controller = "ApiAdmin"
                action = "indexConfig"
            }
            "/$repository/index/query/$identifier" {
                controller = "ApiAdmin"
                action = "mappingQuery"
            }
            "/$repository/index/query/$prefix/$identifier" {
                controller = "ApiAdmin"
                action = "mappingQuery"
            }
            "/$repository/reindex" {
                controller = "ApiAdmin"
                action = "reindexRepository"
            }
            "/$repository/reindex/log" {
                controller = "ApiAdmin"
                action = "reindexLog"
            }
        }

        group "/apidoc", {
            "/admin"(controller: "ApidocAdmin")
            "/annotation"(controller: "ApidocAnnotation")
            "/auth"(controller: "ApidocAuth")
            "/deposit"(controller: "ApidocDeposit")
            "/media"(controller: "ApidocMedia")
            "/oai"(controller: "ApidocOai")
            "/object"(controller: "ApidocObject")
            "/query"(controller: "ApidocQuery")
        }

        "500"(view: "/error")
        "404"(view: "/notFound")
        "202"(view: "/inProgress")
    }
}