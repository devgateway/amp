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
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;

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
                "donorOrganization", "Donor Organization", am, Constants.FUNDING_AGENCY, fundingSection);
        donorOrgRole.add(UpdateEventBehavior.of(DonorFundingRolesEvent.class));
        add(donorOrgRole);
				
		add(new AmpRelatedOrganizationsResponsibleTableFeature(
				"responsibleOrganization", "Responsible Organization", am, Constants.RESPONSIBLE_ORGANISATION,fundingSection));
		
		add(new AmpRelatedOrganizationsOtherTableFeature(
				"executingAgency", "Executing Agency", am, Constants.EXECUTING_AGENCY,fundingSection));

		add(new AmpRelatedOrganizationsOtherTableFeature(
				"implementingAgency", "Implementing Agency", am, Constants.IMPLEMENTING_AGENCY,fundingSection));

		add(new AmpRelatedOrganizationsOtherTableFeature(
				"beneficiaryAgency", "Beneficiary Agency", am, Constants.BENEFICIARY_AGENCY,fundingSection));
		
		add(new AmpRelatedOrganizationsOtherTableFeature(
				"contractingAgency", "Contracting Agency", am, Constants.CONTRACTING_AGENCY,fundingSection));
		
		add(new AmpRelatedOrganizationsOtherTableFeature(
				"regionalGroup", "Regional Group", am, Constants.REGIONAL_GROUP,fundingSection));
		
		add(new AmpRelatedOrganizationsOtherTableFeature(
				"sectorGroup", "Sector Group", am, Constants.SECTOR_GROUP,fundingSection));
		
	}

}
