package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTierMain extends EafTier {

    EafTierMain(EafAnnotationDocument eaf, NodeChild element, String id) {
        super(eaf, element, id)
    }

    public void calculateTimeSlots() {
        NodeChild alignable
        String ref1
        String ref2
        this.annotations.each {
            alignable = it.element.ALIGNABLE_ANNOTATION[0]
            ref1 = alignable.@TIME_SLOT_REF1.text()
            ref2 = alignable.@TIME_SLOT_REF2.text()
            it.timeSlotValue1 = eaf.timeSlots[(ref1)].getTimeValue()
            it.timeSlotValue2 = eaf.timeSlots[(ref2)].getTimeValue()
        }
        this.parsed = true
    }
}