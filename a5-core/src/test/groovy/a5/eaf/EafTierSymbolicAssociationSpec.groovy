package a5.eaf

import spock.lang.Specification

class EafTierSymbolicAssociationSpec extends Specification implements EafSpec {

    void "symbolic association positives"() {
        expect:
        tier.linguisticType == Eaf.TIER_SYMBOLIC_ASSOCIATION
        where:
        tier << [eaf1.tiers.values()[2],
                 eaf1.tiers.values()[3],
                 eaf1.tiers.values()[4],
                 eaf1.tiers.values()[5]] +
                [eaf2.tiers.values()[3],
                 eaf2.tiers.values()[4],
                 eaf2.tiers.values()[5],
                 eaf2.tiers.values()[9],
                 eaf2.tiers.values()[10],
                 eaf2.tiers.values()[11]]
    }

    void "symbolic association negatives"() {
        expect:
        tier.linguisticType != Eaf.TIER_SYMBOLIC_ASSOCIATION
        where:
        tier << [eaf1.tiers.values()[0],
                 eaf1.tiers.values()[1]] +
                [eaf2.tiers.values()[0],
                 eaf2.tiers.values()[1],
                 eaf2.tiers.values()[2],
                 eaf2.tiers.values()[6],
                 eaf2.tiers.values()[7],
                 eaf2.tiers.values()[8],
                 eaf2.tiers.values()[12]]
    }

    void "each symbolic association annotation has a start and end time greater than 0"() {
        expect:
        annotation.timeSlotValue1 > 0 && annotation.timeSlotValue2 > 0
        where:
        annotation << eaf1.tiers.values()[3].annotations + eaf2.tiers.values()[3].annotations
    }

    void "symbolic associations always have a parent"() {
        expect:
        tier.hasParent == tierId
        where:
        tier | tierId
        eaf1.tiers.values()[2] | eaf1.tiers.values()[0].id
        eaf2.tiers.values()[3] | eaf2.tiers.values()[2].id
    }

    void "symbolic associations are no parents"() {
        expect:
        tier.isParentOf == null
        where:
        tier << [eaf1.tiers.values()[2],
                 eaf1.tiers.values()[3],
                 eaf1.tiers.values()[4],
                 eaf1.tiers.values()[5]] +
                [eaf2.tiers.values()[3],
                 eaf2.tiers.values()[4],
                 eaf2.tiers.values()[5],
                 eaf2.tiers.values()[9],
                 eaf2.tiers.values()[10],
                 eaf2.tiers.values()[11]]
    }
}