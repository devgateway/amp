/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractBasicSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractDetailsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractFundingAllocationSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractOrganizationsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorCommitmentsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbOrdersSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorExpendituresSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorFundingInfoSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMTEFProjectionSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;

/**
 * @author aartimon@dginternational.org
 * @since Feb 7, 2011
 */
public class AmpContractsItemFeaturePanel extends AmpFeaturePanel<IPAContract> {
	

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpContractsItemFeaturePanel(String id, String fmName,
			IModel<IPAContract> contractModel){
		super(id, contractModel, fmName, true);
		
		IPAContract c = contractModel.getObject();
		
		if (c.getContractName() == null || c.getContractName().trim().compareTo("") == 0){
			c.setContractName("New Contract");
		}
		
		final Label contractNameLabel = new Label("contractName", new PropertyModel<String>(contractModel, "contractName"));
		contractNameLabel.setOutputMarkupId(true);
		add(contractNameLabel);

		AmpContractBasicSubsectionFeature basicInfo = new AmpContractBasicSubsectionFeature("basicInfo", contractModel, "Contract Info", contractNameLabel);
		add(basicInfo);
		
		AmpContractDetailsSubsectionFeature details = new AmpContractDetailsSubsectionFeature("details", contractModel, "Contract Details");
		add(details);

		AmpContractOrganizationsSubsectionFeature orgs = new AmpContractOrganizationsSubsectionFeature("organizations", contractModel, "Contract Organizations");
		add(orgs);
		
		AmpContractFundingAllocationSubsectionFeature fundingAlloc = new AmpContractFundingAllocationSubsectionFeature("fundAlloc", contractModel, "Funding Allocation");
		add(fundingAlloc);
		
		AmpContractDisbursementsSubsectionFeature disb = new AmpContractDisbursementsSubsectionFeature("disbursements", contractModel, "Contract Disbursements");
		add(disb);
	}

}
