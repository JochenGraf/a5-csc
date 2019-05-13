package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTier {

    EafAnnotationDocument eaf

    NodeChild element

    String id

    ArrayList<EafAnnotation> annotations = []

    boolean parsed = false

    String linguisticType

    String linguisticTypeId

    ArrayList<String> isParentOf

    String hasParent

    EafTier(EafAnnotationDocument eaf, NodeChild element, String id) {
        this.eaf = eaf
        this.element = element
        this.id = id
        this.linguisticType = EafTier.getLinguisticType(element, eaf.element)
        this.linguisticTypeId = EafTier.getLinguisticTypeId(element, eaf.element)
    }

    static String getLinguisticType(NodeChild tier, NodeChild eaf) {
        String linguisticTypeRef = tier.@LINGUISTIC_TYPE_REF.text()
        NodeChild linguisticType = eaf.LINGUISTIC_TYPE.find { node ->
            node.@LINGUISTIC_TYPE_ID == linguisticTypeRef
        }
        if (linguisticType.@CONSTRAINTS.size() == 0) {
            Eaf.TIER_MAIN
        } else {
            linguisticType.@CONSTRAINTS.text()
        }
    }

    static String getLinguisticTypeId(NodeChild tier, NodeChild eaf) {
        String linguisticTypeRef = tier.@LINGUISTIC_TYPE_REF.text()
        NodeChild linguisticType = eaf.LINGUISTIC_TYPE.find { node ->
            node.@LINGUISTIC_TYPE_ID == linguisticTypeRef
        }
        linguisticType.@LINGUISTIC_TYPE_ID.text()
    }
}