package org.digijava.kernel.persistence;

import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Octavian Ciubotaru
 */
public class InMemoryOrganisationManager implements InMemoryManager<AmpOrganisation> {
    
    private static InMemoryOrganisationManager instance;
    
    private final AmpOrgGroup wbGroup;
    private final AmpOrgGroup usaidGroup;
    private final AmpOrgGroup belgiumGroup;
    
    private final AmpOrganisation worldBank;
    private final AmpOrganisation usaid;
    private final AmpOrganisation belgium;
    
    Map<Long, AmpOrganisation> organisations = new HashMap<>();
    
    public static InMemoryOrganisationManager getInstance() {
        if (instance == null) {
            instance = new InMemoryOrganisationManager();
        }
        
        return instance;
    }
    
    private InMemoryOrganisationManager() {
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
        
        organisations.put(worldBank.getAmpOrgId(), worldBank);
        organisations.put(belgium.getAmpOrgId(), belgium);
        organisations.put(usaid.getAmpOrgId(), usaid);
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
    
    @Override
    public AmpOrganisation get(Long id) {
        return organisations.get(id);
    }
    
    @Override
    public List<AmpOrganisation> getAllValues() {
        return new ArrayList<>(organisations.values());
    }
}
