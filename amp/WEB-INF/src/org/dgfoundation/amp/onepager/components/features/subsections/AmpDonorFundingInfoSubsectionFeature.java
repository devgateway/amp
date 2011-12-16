/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
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

	
	private AmpCategorySelectFieldPanel financingInstrument;
	private AmpCategorySelectFieldPanel typeOfAssistance;

	public void checkChoicesRequired(int size) {
		if (size > 0) {
			financingInstrument.getChoiceContainer().setRequired(true);
			typeOfAssistance.getChoiceContainer().setRequired(true);
		} else {
			financingInstrument.getChoiceContainer().setRequired(false);
			typeOfAssistance.getChoiceContainer().setRequired(false);
		}
	}
	
	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpDonorFundingInfoSubsectionFeature(String id,
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, fmName, model);
		financingInstrument = new AmpCategorySelectFieldPanel(
				"financingInstrument",
				CategoryConstants.FINANCING_INSTRUMENT_KEY,
				new PropertyModel<AmpCategoryValue>(model,
						"financingInstrument"),
				CategoryConstants.FINANCING_INSTRUMENT_NAME, true, false);
		
		

		//financingInstrument.getChoiceContainer().setRequired(true);
		add(financingInstrument);

		typeOfAssistance = new AmpCategorySelectFieldPanel(
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

        AmpTextFieldPanel<Float> capitalSpendingPercentage = new AmpTextFieldPanel<Float>(
				"capitalSpendingPercentage",
				new PropertyModel<Float>(model, "capitalSpendingPercentage"),
				"Capital Spending Percentage");
        capitalSpendingPercentage.setVisible(true);
        capitalSpendingPercentage.getTextContainer().add(new MinimumValidator<Float>(0f));
        capitalSpendingPercentage.getTextContainer().add(new MaximumValidator<Float>(100f));
        add(capitalSpendingPercentage);

		if (model != null && model.getObject() != null && 
			model.getObject().getFundingDetails() != null &&
			model.getObject().getFundingDetails().size() > 0)
			checkChoicesRequired(model.getObject().getFundingDetails().size());
		else
			checkChoicesRequired(0);
	}
}
