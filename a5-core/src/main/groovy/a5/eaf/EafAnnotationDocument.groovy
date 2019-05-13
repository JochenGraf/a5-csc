package a5.eaf

import groovy.util.slurpersupport.GPathResult

class EafAnnotationDocument {

    GPathResult element

    LinkedHashMap<String, EafTier> tiers = [:]

    LinkedHashMap<String, EafTimeSlot> timeSlots = [:]

    LinkedHashMap<String, EafAnnotation> annotations = [:]

    EafAnnotationDocument(File eafFile) {
        this.element = new XmlSlurper().parse(eafFile)
        init()
    }

    EafAnnotationDocument(GPathResult element) {
        this.element = element
        init()
    }

    private void initTimeSlots() {
        String slotId
        this.element.TIME_ORDER.TIME_SLOT.each {
            slotId = it.@TIME_SLOT_ID.text()
            this.timeSlots[(slotId)] = new EafTimeSlot(it)
        }
    }

    private void initTiers() {
        String tierId
        String linguisticType
        this.element.TIER.each {
            tierId = it.@TIER_ID.text()
            linguisticType = EafTier.getLinguisticType(it, this.element)
            switch(linguisticType) {
                case Eaf.TIER_MAIN:
                    this.tiers[(tierId)] = new EafTierMain(this, it, tierId)
                    break
                case Eaf.TIER_TIME_SUBDIVISION:
                    this.tiers[(tierId)] = new EafTierTimeSubdivision(this, it, tierId)
                    break
                case Eaf.TIER_SYMBOLIC_SUBDIVISION:
                    this.tiers[(tierId)] = new EafTierSymbolicSubdivision(this, it, tierId)
                    break
                case Eaf.TIER_SYMBOLIC_ASSOCIATION:
                    this.tiers[(tierId)] = new EafTierSymbolicAssociation(this, it, tierId)
                    break
                case Eaf.TIER_INCLUDED_IN:
                    throw new RuntimeException("Tier Included_In currently not supported.")
                    break
                default:
                    throw new RuntimeException("Linguistic type unknown.")
                    break
            }
        }
    }

    private void initTierRelations() {
        this.element.TIER.each {
            String tierId = it.@TIER_ID.text()
            EafTier tier = this.tiers[tierId]
            if (it.@PARENT_REF != "") {
                tier.hasParent = it.@PARENT_REF
                EafTier parent = this.tiers[tier.hasParent]
                if (parent.isParentOf == null) parent.isParentOf = []
                parent.isParentOf << tier.id
            }
        }
    }

    private void initAnnotations() {
        EafAnnotation ann
        String annId
        this.tiers.each { k, v ->
            v.element.ANNOTATION.each {
                annId = it."*".@ANNOTATION_ID.text()
                ann = new EafAnnotation(it)
                this.annotations[(annId)] = ann
                v.annotations << ann
            }
        }
    }

    private void calculateTimeSlots() {
        this.tiers.each { k, v ->
            v.calculateTimeSlots()
        }
    }

    private void init() {
        initTimeSlots()
        initTiers()
        initTierRelations()
        initAnnotations()
        calculateTimeSlots()
    }
}