package a5.eaf

import spock.lang.Specification

class EafTimeSlotSpec extends Specification implements EafSpec {

    void "number of time slots"() {
        expect:
            timeSlots.size() == size
        where:
            timeSlots | size
            eaf1.timeSlots.values() | 354
            eaf2.timeSlots.values() | 843
    }

    void "each time slot has a time value > 0 or time value == -1"() {
        expect:
            timeSlot.timeValue > 0 || timeSlot.timeValue == -1
        where:
            timeSlot << eaf1.timeSlots.values() + eaf2.timeSlots.values()
    }
}