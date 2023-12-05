/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpRelatedOrganizationsOtherTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpRelatedOrganizationsResponsibleTableFeature;
import org.dgfoundation.amp.onepager.events.DonorFundingRolesEvent;
import org.dgfoundation.amp.onepager.events.OrganisationUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aartimon@dginternational.org since Oct 26, 2010
 */
public class AmpRelatedOrganizationsFormSectionFeature extends
        AmpFormSectionFeaturePanel {

    private static final long serialVersionUID = -6654390083784446344L;
    
    protected AmpDonorFundingFormSectionFeature fundingSection;

    public AmpRelatedOrganizationsFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am, AmpComponentPanel componentPanel) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;
        this.fundingSection=(AmpDonorFundingFormSectionFeature) componentPanel;

        AmpRelatedOrganizationsOtherTableFeature donorOrgRole = new AmpRelatedOrganizationsOtherTableFeature(
                "donorOrganization", "Donor Organization", am, Constants.FUNDING_AGENCY, fundingSection, null);
        donorOrgRole.add(UpdateEventBehavior.of(DonorFundingRolesEvent.class));
        add(donorOrgRole);
                
        add(new AmpRelatedOrganizationsResponsibleTableFeature(
                "responsibleOrganization", "Responsible Organization", am, Constants.RESPONSIBLE_ORGANISATION,fundingSection));
        
        add(new AmpRelatedOrganizationsOtherTableFeature(
                "executingAgency", "Executing Agency", am, Constants.EXECUTING_AGENCY,fundingSection, null));

        add(new AmpRelatedOrganizationsOtherTableFeature(
                "implementingAgency", "Implementing Agency", am, Constants.IMPLEMENTING_AGENCY, fundingSection, null));

        // AMP-17531: allow for Primary Beneficiary org selector to only show orgs of a certain type
        long primaryBeneficiaryOrgTypeId = Long.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ORGANISATION_TYPE_FOR_BENEFICIARY_AGENCY));
        AmpOrgType benefAgencyOrgType = (primaryBeneficiaryOrgTypeId > 0) ? DbUtil.getAmpOrgType(primaryBeneficiaryOrgTypeId) : null;
        List<AmpOrgGroup> filteredOrgGroups = benefAgencyOrgType == null ? null : new ArrayList<AmpOrgGroup>(benefAgencyOrgType.getOrgGroups()); 
        AmpRelatedOrganizationsOtherTableFeature ba = new AmpRelatedOrganizationsOtherTableFeature(
                "beneficiaryAgency", "Beneficiary Agency", am, Constants.BENEFICIARY_AGENCY, fundingSection, filteredOrgGroups);
        ba.getSearchOrganization().setFilteringOrgType(benefAgencyOrgType);
        
        add(ba);
        
        add(new AmpRelatedOrganizationsOtherTableFeature(
                "contractingAgency", "Contracting Agency", am, Constants.CONTRACTING_AGENCY, fundingSection, null));
        
        add(new AmpRelatedOrganizationsOtherTableFeature(
                "regionalGroup", "Regional Group", am, Constants.REGIONAL_GROUP,fundingSection, null));
        
        add(new AmpRelatedOrganizationsOtherTableFeature(
                "sectorGroup", "Sector Group", am, Constants.SECTOR_GROUP,fundingSection, null));
        
        this.add(UpdateEventBehavior.of(OrganisationUpdateEvent.class));
    }

}
