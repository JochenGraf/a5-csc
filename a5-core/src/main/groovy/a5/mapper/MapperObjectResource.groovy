package a5.mapper

import a5.api.util.ApiUtil
import a5.config.ConfigIndexObject
import a5.ffmpeg.Ffprobe
import a5.index.IndexObject
import groovy.util.slurpersupport.NodeChild

trait MapperObjectResource implements MapperObject {

    Float mapDuration() {
        (this.file.exists() && (ApiUtil.isAudio(this.file) || ApiUtil.isVideo(this.file))) ?
                Ffprobe.duration(this.file) : null
    }

    Float mapWidth() {
        null
    }

    Float mapHeight() {
        null
    }

    ArrayList<IndexObject> map() {
        super.map()
        this.object.objectType = "a5:Resource"
        this.object.duration = mapDuration()
        this.object.width = mapWidth()
        this.object.height = mapHeight()
        [this.object]
    }
}