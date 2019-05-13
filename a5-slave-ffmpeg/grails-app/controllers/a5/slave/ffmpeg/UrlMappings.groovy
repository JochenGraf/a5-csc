package a5.slave.ffmpeg

class UrlMappings {

    static mappings = {

        "/api/ffmpeg"(controller: "ApiFfmpeg")
        "/api/ffmpeg/version"(controller: "ApiFfmpeg", action: "version")

        "/"(view:"/index")

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
