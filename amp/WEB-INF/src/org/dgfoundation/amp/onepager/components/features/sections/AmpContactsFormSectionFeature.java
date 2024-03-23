/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpContactsFromTableFeature;
import org.dgfoundation.amp.onepager.events.ContactChangedEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author dan
 *
 */
public class AmpContactsFormSectionFeature extends AmpFormSectionFeaturePanel {
    
    private static final long serialVersionUID = 30916024623677185L;
    
    public AmpContactsFormSectionFeature(String id, String fmName, IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;
        
        //AmpContactsSubsectionFeaturePanel donors =new AmpContactsSubsectionFeaturePanel("donorContactInformation", "Donor Contact Information", am, org.digijava.module.aim.helper.Constants.DONOR_CONTACT);
        //donors.setOutputMarkupId(true);
        //add(donors);
        add(new AmpContactsFromTableFeature("donorContactInformation", "Donor Contact Information", am, org.digijava.module.aim.helper.Constants.DONOR_CONTACT));
        add(new AmpContactsFromTableFeature("mofedContactInformation", "Mofed Contact Information", am, org.digijava.module.aim.helper.Constants.MOFED_CONTACT));
        add(new AmpContactsFromTableFeature("projectCoordinatorContactInformation", "Project Coordinator Contact Information", am, org.digijava.module.aim.helper.Constants.PROJECT_COORDINATOR_CONTACT));
        add(new AmpContactsFromTableFeature("sectorMinistryContactInformation", "Sector Ministry Contact Information", am, org.digijava.module.aim.helper.Constants.SECTOR_MINISTRY_CONTACT));
        add(new AmpContactsFromTableFeature("implExecAgencyContactInformation", "Implementing/Executing Agency Contact Information", am, org.digijava.module.aim.helper.Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT));
        this.add(UpdateEventBehavior.of(ContactChangedEvent.class));
        
                
    }
    

}
