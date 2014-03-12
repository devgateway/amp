/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpAgreementItemPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.ValueToSetModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 4, 2010
 */
public class AmpDonorFundingInfoSubsectionFeature extends
		AmpSubsectionFeaturePanel<AmpFunding> {

	
	private AmpCategorySelectFieldPanel financingInstrument;
	private AmpCategorySelectFieldPanel typeOfAssistance;
	private AmpTextAreaFieldPanel loanTerms;
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
		super(id, fmName, model, false, true);
		financingInstrument = new AmpCategorySelectFieldPanel(
				"financingInstrument",
				CategoryConstants.FINANCING_INSTRUMENT_KEY,
				new PropertyModel<AmpCategoryValue>(model,
						"financingInstrument"),
				CategoryConstants.FINANCING_INSTRUMENT_NAME, true, false);		
		add(financingInstrument);
		
		// LoanTerms
		typeOfAssistance = new AmpCategorySelectFieldPanel(
				"typeOfAssistance", CategoryConstants.TYPE_OF_ASSISTENCE_KEY,
				new PropertyModel<AmpCategoryValue>(model, "typeOfAssistance"),
				CategoryConstants.TYPE_OF_ASSISTENCE_NAME, true, false);
		
		
		
		loanTerms =  new AmpTextAreaFieldPanel("loanTerms", 
				          new PropertyModel<String>(model, "loanTerms"), 
				          "Loan Terms", false, false, false);
		
		loanTerms.getTextAreaContainer().add(new AttributeModifier("style", "width: 225px; height: 65px;"));          	
			
		AmpCategoryValue value = (AmpCategoryValue) typeOfAssistance.getChoiceContainer().getModelObject();
		boolean isLoan = (value == null ? false : value.getValue().equals(CategoryConstants.TYPE_OF_ASSISTANCE_LOAN.getValueKey()));
		loanTerms.getTextAreaContainer().setVisible(isLoan);
	    loanTerms.getTitleLabel().setVisible(isLoan);  
	    add(loanTerms);
	    
		typeOfAssistance.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {        
			private static final long serialVersionUID = -6492252081340597543L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AmpCategoryValue value = (AmpCategoryValue) typeOfAssistance.getChoiceContainer().getModelObject();
				boolean isLoan = (value == null ? false : (value.getValue().equals(CategoryConstants.TYPE_OF_ASSISTANCE_LOAN.getValueKey())));
				loanTerms.getTextAreaContainer().setVisible(isLoan);
        	    loanTerms.getTitleLabel().setVisible(isLoan);            	
            	target.add(loanTerms);

            }
        });
		
		add(typeOfAssistance);

		final AmpCategorySelectFieldPanel modeOfPayment;

        PropertyModel<AmpCategoryValue> fundingStatusModel = new PropertyModel<AmpCategoryValue>(model, "fundingStatus");
        AmpCategorySelectFieldPanel fundingStatus = new AmpCategorySelectFieldPanel(
                "fundingStatus", CategoryConstants.FUNDING_STATUS_KEY,
                fundingStatusModel,
                CategoryConstants.FUNDING_STATUS_NAME, true, false);
        fundingStatus.getChoiceContainer().setRequired(false);
        add(fundingStatus);

        if ("true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.LINK_MODE_OF_PAYMENT_TO_FUNDING_STATUS))){
            ((DropDownChoice)fundingStatus.getChoiceContainer()).setNullValid(true);
            modeOfPayment = new AmpCategorySelectFieldPanel(
                    "modeOfPayment", CategoryConstants.MODE_OF_PAYMENT_KEY,
                    new PropertyModel<AmpCategoryValue>(model, "modeOfPayment"),
                    CategoryConstants.MODE_OF_PAYMENT_NAME, true, true, false,
                    new ValueToSetModel<AmpCategoryValue>(fundingStatusModel));

            fundingStatus.getChoiceContainer().add(
                new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(modeOfPayment);
                    }
                });
        } else {
            modeOfPayment = new AmpCategorySelectFieldPanel(
                    "modeOfPayment", CategoryConstants.MODE_OF_PAYMENT_KEY,
                    new PropertyModel<AmpCategoryValue>(model, "modeOfPayment"),
                    CategoryConstants.MODE_OF_PAYMENT_NAME, true, false);
        }
        modeOfPayment.getChoiceContainer().setRequired(false);
        add(modeOfPayment);

		AmpTextFieldPanel<String> financingId = new AmpTextFieldPanel<String>(
				"financingId",
				new PropertyModel<String>(model, "financingId"),
				"Funding Organization Id");
//		financingId.getNewLine().setVisible(false);
		add(financingId);
		
		 final PropertyModel<Date> funClassificationDateModel = new PropertyModel<Date>(
	                model, "fundingClassificationDate");
	        AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("fundingClassificationDate", funClassificationDateModel, null, "Funding Classification Date");
	        //date.getDate().setRequired(true);
			add(date);

		if (model != null && model.getObject() != null && 
			model.getObject().getFundingDetails() != null &&
			model.getObject().getFundingDetails().size() > 0)
			checkChoicesRequired(model.getObject().getFundingDetails().size());
		else
			checkChoicesRequired(0);
		
		add(new AmpAgreementItemPanel("agreement", model, "Agreement"));

	}
}
