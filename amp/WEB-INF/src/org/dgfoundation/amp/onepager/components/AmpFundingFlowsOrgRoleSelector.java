package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;

public class AmpFundingFlowsOrgRoleSelector extends AmpComponentPanel{

    public AmpFundingFlowsOrgRoleSelector(String id, IModel<AmpFunding> model,IModel itemModel, String fmName) {
        super(id, model, fmName);

        AmpOrgRoleSelectorComponent orgRoleSelector=new AmpOrgRoleSelectorComponent("fundingFlowsOrgRoleSelector", 
                new PropertyModel<AmpActivityVersion>(model,"ampActivityId"), new PropertyModel<AmpRole>(itemModel,"recipientRole"),
                new PropertyModel<AmpOrganisation>(itemModel,"recipientOrg"),
                true, AmpDonorFundingFormSectionFeature.FUNDING_FLOW_ROLE_FILTER, true);
        add(orgRoleSelector);
    }

}
