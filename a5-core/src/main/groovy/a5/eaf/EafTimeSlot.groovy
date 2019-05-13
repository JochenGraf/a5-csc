package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTimeSlot {

    NodeChild element

    long timeValue

    EafTimeSlot(NodeChild element) {
        this.element = element
        init()
    }

    private void init() {
        String value = this.element.@TIME_VALUE.text()
        this.timeValue = value ? value.toLong() : -1
    }
}