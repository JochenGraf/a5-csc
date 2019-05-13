package a5.eaf

import groovy.util.slurpersupport.NodeChild

class EafTierSymbolicAssociation extends EafTierSymbolicSubdivision {

    EafTierSymbolicAssociation(EafAnnotationDocument eaf, NodeChild element, String id) {
        super(eaf, element, id)
    }
}