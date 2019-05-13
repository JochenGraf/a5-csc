package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTierTimeSubdivision extends EafTier {

    ArrayList<EafAnnotation> cached = []

    private static final String START = "_START_"

    String lastRef = START

    EafTierTimeSubdivision(EafAnnotationDocument eaf, NodeChild element, String id) {
        super(eaf, element, id)
    }

    private boolean isSameSubdivision(EafAnnotation ann) {
        ann.getTimeSlotRef1() == this.lastRef
    }

    private boolean isAtStart() {
        this.lastRef == START
    }

    private long cachedStart() {
        this.eaf.getTimeSlots()[this.cached[0].timeSlotRef1].timeValue
    }

    private long cachedEnd() {
        this.eaf.getTimeSlots()[this.cached[this.cached.size() - 1].timeSlotRef2].timeValue
    }

    private void cache(EafAnnotation annotation) {
        this.cached << annotation
    }

    private void decache() {
        if (this.cached.size() == 0) return
        def start = cachedStart()
        def end = cachedEnd()
        int length = (end - start) / this.cached.size()
        int tmp
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
            this.lastRef = it.timeSlotRef2
        }
        decache()
        this.parsed = true
    }
}