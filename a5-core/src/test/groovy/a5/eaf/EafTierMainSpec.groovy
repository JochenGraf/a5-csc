package a5.eaf

import spock.lang.Shared
import spock.lang.Specification

class EafTierMainSpec extends Specification implements EafSpec {

    void "main tier positives"() {
        expect:
        tier.linguisticType == Eaf.TIER_MAIN
        where:
        tier << [eaf1.tiers.values()[0]] +
                [eaf2.tiers.values()[0],
                 eaf2.tiers.values()[6],
                 eaf2.tiers.values()[12]]
    }

    void "main tier negatives"() {
        expect:
        tier.linguisticType != Eaf.TIER_MAIN
        where:
        tier << [eaf1.tiers.values()[1],
                 eaf1.tiers.values()[2],
                 eaf1.tiers.values()[3],
                 eaf1.tiers.values()[4],
                 eaf1.tiers.values()[5]] +
                [eaf2.tiers.values()[1],
                 eaf2.tiers.values()[2],
                 eaf2.tiers.values()[3],
                 eaf2.tiers.values()[4],
                 eaf2.tiers.values()[5],
                 eaf2.tiers.values()[7],
                 eaf2.tiers.values()[8],
                 eaf2.tiers.values()[9],
                 eaf2.tiers.values()[10],
                 eaf2.tiers.values()[11]]
    }

    void "each main tier annotation has a start and end time greater than 0"() {
        expect:
        annotation.timeSlotValue1 > 0 && annotation.timeSlotValue2 > 0
        where:
        annotation << eaf1.tiers.values()[0].annotations +
                eaf2.tiers.values()[0].annotations + eaf2.tiers.values()[6].annotations +
                eaf2.tiers.values()[12].annotations
    }

    void "main tiers have no parent"() {
        expect:
        tier.hasParent == null
        where:
        tier << [eaf1.tiers.values()[0]] +
                [eaf2.tiers.values()[0],
                 eaf2.tiers.values()[6],
                 eaf2.tiers.values()[12]]
    }

    void "main tiers are parents"() {
        expect:
        tier.isParentOf == tierIds
        where:
        tier | tierIds
        eaf1.tiers.values()[0] | [eaf1.tiers.values()[1].id, eaf1.tiers.values()[2].id, eaf1.tiers.values()[5].id]
        eaf2.tiers.values()[0] | [eaf2.tiers.values()[1].id, eaf2.tiers.values()[5].id]
        eaf2.tiers.values()[6] | [eaf2.tiers.values()[7].id, eaf2.tiers.values()[11].id]
    }
}