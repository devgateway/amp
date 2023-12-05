package org.digijava.module.gateperm.gates;

/**
 * A gate that negates the RelatedOrgGate
 * @author aartimon@developmentgateway.org
 * @since Dec 11, 2012
 */

public class NotRelatedOrgGate extends RelatedOrgGate {

    public static final String DESCRIPTION = "Negates the RelatedOrgGate";
    
    @Override
    public boolean logic() throws Exception {
        return !super.logic();
    }
    
    @Override
    public String description() {
        return DESCRIPTION;
    }

}
