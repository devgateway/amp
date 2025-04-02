package org.digijava.module.gateperm.gates;

/**
 * Negates the VerifiedRegionGate
 * 
 * @author aartimon@developmentgateway.org
 * @since Dec 11, 2012
 */

public class NotVerifiedRegionGate extends VerifiedRegionGate {
    
    public static final String DESCRIPTION = "Negates the VerifiedRegionGate";
    
    @Override
    public boolean logic() throws Exception {
        return !super.logic();
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
