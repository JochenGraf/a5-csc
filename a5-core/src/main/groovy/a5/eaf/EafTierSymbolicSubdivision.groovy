package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTierSymbolicSubdivision extends EafTier {

    ArrayList<EafAnnotation> cached = []

    private static final String START = "_START_"

    String lastRef = START

    EafTierSymbolicSubdivision(EafAnnotationDocument eaf, NodeChild element, String id) {
        super(eaf, element, id)
    }

    protected boolean isSameSubdivision(EafAnnotation ann) {
        ann.annotationRef == this.lastRef
    }

    protected boolean isAtStart() {
        this.lastRef == START
    }

    protected long cachedStart() {
        this.eaf.annotations[this.cached[0].annotationRef].timeSlotValue1
    }

    protected long cachedEnd() {
        this.eaf.annotations[this.cached[0].annotationRef].timeSlotValue2
    }

    protected void cache(EafAnnotation annotation) {
        this.cached << annotation
    }

    protected void decache() {
        if (this.cached.size() == 0) return
        def start = cachedStart()
        def end = cachedEnd()
        long length = (end - start) / this.cached.size()
        long tmp
        this.cached.eachWithIndex { ann, pos ->
            tmp = start + pos * length
            ann.timeSlotValue1 = tmp
            ann.timeSlotValue2 = tmp + length
        }
        this.cached.clear()
    }

    public void calculateTimeSlots() {
        this.lastRef = START
        this.annotations.each {
            if (isSameSubdivision(it) || isAtStart()) {
                cache(it)
            } else {
                decache()
                cache(it)
            }
            this.lastRef = it.annotationRef
        }
        decache()
        this.parsed = true
    }
}