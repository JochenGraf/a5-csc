package a5.eaf

import spock.lang.Specification

class EafTierSpec extends Specification implements EafSpec {

    void "each tier has the parsed flag"() {
        expect:
        tier.parsed == true
        where:
        tier << eaf1.tiers.values() + eaf2.tiers.values()
    }
}