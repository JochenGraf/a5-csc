package a5.config

import a5.index.Index
import a5.index.IndexAnnotation
import a5.index.IndexAnnotationCanvas
import a5.index.IndexAnnotationLayer
import a5.index.IndexAnnotationManifest

class ConfigIndexAnnotation extends ConfigIndex {

    ConfigIndexAnnotation(String repositoryName) {
        super(repositoryName, Index.ANNOTATION)
        this.initFieldsGroovy([IndexAnnotationManifest.class, IndexAnnotationCanvas.class, IndexAnnotationLayer.class,
                               IndexAnnotation.class])
        initMapperGroovy()
    }
}