/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 4, 2010
 */
public class AmpDonorFundingInfoSubsectionFeature extends
		AmpSubsectionFeaturePanel<AmpFunding> {

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpDonorFundingInfoSubsectionFeature(String id,
			IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, fmName, model);
		AmpCategorySelectFieldPanel financingInstrument = new AmpCategorySelectFieldPanel(
				"financingInstrument",
				CategoryConstants.FINANCING_INSTRUMENT_KEY,
				new PropertyModel<AmpCategoryValue>(model,
						"financingInstrument"),
				CategoryConstants.FINANCING_INSTRUMENT_NAME, true, false);
		//financingInstrument.getChoiceContainer().setRequired(true);
		add(financingInstrument);

		AmpCategorySelectFieldPanel typeOfAssistance = new AmpCategorySelectFieldPanel(
				"typeOfAssistance", CategoryConstants.TYPE_OF_ASSISTENCE_KEY,
				new PropertyModel<AmpCategoryValue>(model, "typeOfAssistance"),
				CategoryConstants.TYPE_OF_ASSISTENCE_NAME, true, false);
		//typeOfAssistance.getChoiceContainer().setRequired(true);
		add(typeOfAssistance);

		AmpCategorySelectFieldPanel modeOfPayment = new AmpCategorySelectFieldPanel(
				"modeOfPayment", CategoryConstants.MODE_OF_PAYMENT_KEY,
				new PropertyModel<AmpCategoryValue>(model, "modeOfPayment"),
				CategoryConstants.MODE_OF_PAYMENT_NAME, true, false);
		modeOfPayment.getChoiceContainer().setRequired(false);
		add(modeOfPayment);

		AmpCategorySelectFieldPanel fundingStatus = new AmpCategorySelectFieldPanel(
				"fundingStatus", CategoryConstants.FUNDING_STATUS_KEY,
				new PropertyModel<AmpCategoryValue>(model, "fundingStatus"),
				CategoryConstants.FUNDING_STATUS_NAME, true, false);
		fundingStatus.getChoiceContainer().setRequired(false);
		add(fundingStatus);

		AmpTextFieldPanel<String> financingId = new AmpTextFieldPanel<String>(
				"financingId",
				new PropertyModel<String>(model, "financingId"),
				"Funding Organization Id");
//		financingId.getNewLine().setVisible(false);
		add(financingId);
	}

}
