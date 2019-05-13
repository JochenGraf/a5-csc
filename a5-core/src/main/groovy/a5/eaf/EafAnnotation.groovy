package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafAnnotation {

    NodeChild element

    String id

    String value

    long timeSlotValue1

    long timeSlotValue2

    String timeSlotRef1

    String timeSlotRef2

    String annotationRef

    EafAnnotation(NodeChild element) {
        this.element = element
        init()
    }

    private void initId() {
        this.id = this.element."*".@ANNOTATION_ID.text()
    }

    private void initValue() {
        this.value = this.element."*".ANNOTATION_VALUE.text()
    }

    private void initTimeSlotRef() {
        this.timeSlotRef1 = this.element."*".@TIME_SLOT_REF1.text()
        this.timeSlotRef2 = this.element."*".@TIME_SLOT_REF2.text()
    }

    private void initAnnotationRef() {
        this.annotationRef = this.element."*".@ANNOTATION_REF.text()
    }

    private void init() {
        initId()
        initValue()
        initTimeSlotRef()
        initAnnotationRef()
    }
}