package a5.eaf

import spock.lang.Specification

class EafTierTimeSubdivisionSpec extends Specification implements EafSpec {

    void "time subdivision positives"() {
        expect:
        tier.linguisticType == Eaf.TIER_TIME_SUBDIVISION
        where:
        tier << [eaf2.tiers.values()[1],
                 eaf2.tiers.values()[7]]
    }

    void "time subdivision negatives"() {
        expect:
        tier.linguisticType != Eaf.TIER_TIME_SUBDIVISION
        where:
        tier << [eaf2.tiers.values()[0],
                 eaf2.tiers.values()[2],
                 eaf2.tiers.values()[3],
                 eaf2.tiers.values()[4],
                 eaf2.tiers.values()[5],
                 eaf2.tiers.values()[6],
                 eaf2.tiers.values()[8],
                 eaf2.tiers.values()[9],
                 eaf2.tiers.values()[10],
                 eaf2.tiers.values()[11],
                 eaf2.tiers.values()[12]]
    }

    void "each annotation has a start and end time greater than 0"() {
        expect:
        annotation.timeSlotValue1 > 0 && annotation.timeSlotValue2 > 0
        where:
        annotation << eaf2.tiers.values()[1].annotations
    }
}