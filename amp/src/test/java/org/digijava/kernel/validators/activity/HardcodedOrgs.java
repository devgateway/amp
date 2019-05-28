package org.digijava.kernel.validators.activity;

import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedOrgs {

    private final AmpOrganisation worldBank;
    private final AmpOrganisation usaid;
    private final AmpOrganisation belgium;

    public HardcodedOrgs() {
        worldBank = new AmpOrganisation();
        worldBank.setName("World Bank");
        worldBank.setAmpOrgId(1L);

        usaid = new AmpOrganisation();
        usaid.setName("USAID");
        usaid.setAmpOrgId(2L);

        belgium = new AmpOrganisation();
        belgium.setName("Belgium");
        belgium.setAmpOrgId(3L);
    }

    public AmpOrganisation getWorldBank() {
        return worldBank;
    }

    public AmpOrganisation getUsaid() {
        return usaid;
    }

    public AmpOrganisation getBelgium() {
        return belgium;
    }
}
