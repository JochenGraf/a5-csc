package a5.mapper.nl.mpi.eaf

import a5.eaf.EafAnnotationDocument
import a5.index.IndexAnnotation
import a5.index.IndexAnnotationCanvas
import a5.index.IndexAnnotationLayer
import a5.index.IndexAnnotationManifest
import a5.index.IndexAnnotationSelector
import a5.mapper.MapperAnnotation
import a5.mapper.MapperGroovy

class EafAnnotationMapper extends MapperGroovy implements MapperAnnotation {

    EafAnnotationDocument eaf

    IndexAnnotationManifest mapManifest() {
        IndexAnnotationManifest manifest = new IndexAnnotationManifest()
        manifest.id = this.object.id
        manifest.label = this.file.name
        manifest.type << "eaf:ANNOTATION_DOCUMENT"
        manifest.members << "${this.object.id}/canvas/0"
        manifest.within << this.parent.id
        manifest
    }

    ArrayList<IndexAnnotationCanvas> mapCanvases() {
        IndexAnnotationCanvas canvas = new IndexAnnotationCanvas()
        canvas.id = "${this.object.id}/canvas/0"
        canvas.label = this.object.label
        if (this.media != null) canvas.duration = media.duration
        this.object.relatedTo.each {
            canvas.media << it
        }
        this.eaf.tiers.eachWithIndex { tierId, tier, idx ->
            String id = "${this.object.id}/annotationlayer/$idx"
            canvas.otherContent << id
        }
        [canvas]
    }

    ArrayList<IndexAnnotationLayer> mapAnnotationLayers() {
        ArrayList<IndexAnnotationLayer> annotationLayers = []
        this.eaf.tiers.eachWithIndex { tierId, tier, idx ->
            IndexAnnotationLayer annotationLayer = new IndexAnnotationLayer()
            annotationLayer.type << "eaf:TIER"
            annotationLayer.type << "eaf:TIER@$tier.linguisticType"
            annotationLayer.id = "${this.object.id}/annotationlayer/${idx}"
            annotationLayer.label = "$tierId"
            annotationLayer.total = tier.annotations.size()
            int idHasParent = eaf.tiers.findIndexOf { it.key == tier.hasParent }
            if (idHasParent >= 0) annotationLayer.hasParent = "${this.object.id}/annotationlayer/${idHasParent}"
            tier.isParentOf.each { pof ->
                int idParentOf = eaf.tiers.findIndexOf { it.key == pof }
                if (annotationLayer.isParentOf == null) annotationLayer.isParentOf = []
                annotationLayer.isParentOf << "${this.object.id}/annotationlayer/${idParentOf}"
            }
            tier.annotations.each {
                IndexAnnotation annotation = new IndexAnnotation()
                annotation.type << "eaf:ANNOTATION"
                annotation.id = "${this.object.id}/annotation/$it.id"
                annotation.label = "$it.id"
                annotation.bodyValue = it.value
                annotation.selector = new IndexAnnotationSelector()
                annotation.selector.type = "a5:SectionSelector"
                annotation.selector.offset = it.timeSlotValue1
                annotation.selector.length = it.timeSlotValue2 - it.timeSlotValue1
                annotation.target = "${this.object.id}/canvas/0"
                annotationLayer.resources << annotation
            }
            annotationLayers << annotationLayer
        }
        annotationLayers
    }

    ArrayList<IndexAnnotation> mapAnnotations() {
        ArrayList<IndexAnnotation> annotations = []
        this.eaf.tiers.each { tierId, tier ->
            tier.annotations.each {
                IndexAnnotation annotation = new IndexAnnotation()
                annotation.id = "${this.object.id}/annotation/$it.id"
                annotation.type << "eaf:ANNOTATION"
                annotation.type << "eaf:TIER@$tier.linguisticType"
                annotation.label = "$it.id"
                annotation.bodyValue = it.value
                annotation.selector = new IndexAnnotationSelector()
                annotation.selector.type = "a5:SectionSelector"
                annotation.selector.offset = it.timeSlotValue1
                annotation.selector.length = it.timeSlotValue2 - it.timeSlotValue1
                annotation.target = "${this.object.id}/canvas/0"
                annotations << annotation
            }
        }
        annotations
    }

    void parse(File file) {
        super.parse(file)
        this.eaf = new EafAnnotationDocument(this.parsed)
    }
}