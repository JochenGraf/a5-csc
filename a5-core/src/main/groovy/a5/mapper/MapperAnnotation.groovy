package a5.mapper

import a5.config.ConfigIndexAnnotation
import a5.index.IndexAnnotation
import a5.index.IndexAnnotationCanvas
import a5.index.IndexAnnotationLayer
import a5.index.IndexAnnotationManifest

trait MapperAnnotation {

    ConfigIndexAnnotation config

    File file

    Map object

    Map parent

    Map media

    IndexAnnotationManifest manifest

    ArrayList<IndexAnnotationCanvas> canvases

    ArrayList<IndexAnnotationLayer> annotationLayers

    ArrayList<IndexAnnotation> annotations

    abstract IndexAnnotationManifest mapManifest()

    abstract ArrayList<IndexAnnotationCanvas> mapCanvases()

    abstract ArrayList<IndexAnnotationLayer> mapAnnotationLayers()

    abstract ArrayList<IndexAnnotation> mapAnnotations()

    abstract void parse(File file)

    ArrayList<Object> map(File file, Map object, Map parent, Map media) {
        this.file = file
        this.object = object
        this.parent = parent
        this.media = media
        this.parse(file)
        this.manifest = mapManifest()
        this.canvases = mapCanvases()
        this.annotationLayers = mapAnnotationLayers()
        this.annotations = mapAnnotations()
        ([this.manifest] << [this.canvases] << this.annotationLayers << this.annotations).flatten()
    }
}