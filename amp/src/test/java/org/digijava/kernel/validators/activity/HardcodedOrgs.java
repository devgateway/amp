package org.digijava.kernel.validators.activity;

import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedOrgs {

    private final AmpOrgGroup wbGroup;
    private final AmpOrgGroup usaidGroup;
    private final AmpOrgGroup belgiumGroup;

    private final AmpOrganisation worldBank;
    private final AmpOrganisation usaid;
    private final AmpOrganisation belgium;

    public HardcodedOrgs() {
        wbGroup = new AmpOrgGroup();
        wbGroup.setAmpOrgGrpId(1L);
        wbGroup.setOrgGrpName("World Bank Group");
        wbGroup.setOrgGrpCode("WB");

        usaidGroup = new AmpOrgGroup();
        usaidGroup.setAmpOrgGrpId(2L);
        usaidGroup.setOrgGrpName("United States of America");
        usaidGroup.setOrgGrpCode("USA");

        belgiumGroup = new AmpOrgGroup();
        belgiumGroup.setAmpOrgGrpId(3L);
        belgiumGroup.setOrgGrpName("Belgium");

        worldBank = new AmpOrganisation();
        worldBank.setName("World Bank");
        worldBank.setAmpOrgId(1L);
        worldBank.setOrgGrpId(wbGroup);

        usaid = new AmpOrganisation();
        usaid.setName("USAID");
        usaid.setAmpOrgId(2L);
        usaid.setOrgGrpId(usaidGroup);

        belgium = new AmpOrganisation();
        belgium.setName("Belgium");
        belgium.setAmpOrgId(3L);
        belgium.setOrgGrpId(belgiumGroup);
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

    public AmpOrgGroup getWbGroup() {
        return wbGroup;
    }

    public AmpOrgGroup getUsaidGroup() {
        return usaidGroup;
    }

    public AmpOrgGroup getBelgiumGroup() {
        return belgiumGroup;
    }
}
