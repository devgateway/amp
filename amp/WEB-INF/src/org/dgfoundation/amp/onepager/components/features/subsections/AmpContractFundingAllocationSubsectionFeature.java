/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractFundingAllocationSubsectionFeature extends
		AmpSubsectionFeaturePanel<IPAContract> {
	
	private static Logger logger = Logger.getLogger(AmpContractFundingAllocationSubsectionFeature.class);

	private void calculateTotalAmount(IModel<IPAContract> model){
		IPAContract c = model.getObject();
		Double total = (double) 0;
		if (c.getTotalECContribIBAmount() != null)
			total += c.getTotalECContribIBAmount();
		if (c.getTotalECContribINVAmount() != null)
			total += c.getTotalECContribINVAmount();
		
		if (c.getTotalNationalContribCentralAmount() != null)
			total += c.getTotalNationalContribCentralAmount();
		if (c.getTotalNationalContribIFIAmount() != null)
			total += c.getTotalNationalContribIFIAmount();
		if (c.getTotalNationalContribRegionalAmount() != null)
			total += c.getTotalNationalContribRegionalAmount();
		
		if (c.getTotalPrivateContribAmount() != null)
			total += c.getTotalPrivateContribAmount();		
		model.getObject().setTotalAmount(total);
	}
	
	public AmpContractFundingAllocationSubsectionFeature(String id,
			final IModel<IPAContract> model, String fmName){
		super(id, fmName, model);
		
		
		AmpTextFieldPanel<Double> totalValue = new AmpTextFieldPanel<Double>("totalValue", new PropertyModel<Double>(model, "contractTotalValue"), "Contract Total Value");
		add(totalValue);
		
		AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
			@Override
			public List<AmpCurrency> getObject() {
				return (List<AmpCurrency>) CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE);
			}
		};
		
		AmpSelectFieldPanel<AmpCurrency> valueCurrency = new AmpSelectFieldPanel<AmpCurrency>("valueCurrency",
				new PropertyModel<AmpCurrency>(model, "totalAmountCurrency"),
				currencyList,
				"Currency", true, false);
		add(valueCurrency);

		final AmpTextFieldPanel<Double> totalAmount = new AmpTextFieldPanel<Double>("totalAmount", new PropertyModel<Double>(model, "totalAmount"), "Contract Total Amount");
		totalAmount.getTextContainer().setEnabled(false);
		add(totalAmount);
		
		AmpTextFieldPanel<Double> ibAmount = new AmpTextFieldPanel<Double>("ibAmount", new PropertyModel<Double>(model, "totalECContribIBAmount"), "IB Amount");
		ibAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(ibAmount);
		AmpDatePickerFieldPanel ibDate = new AmpDatePickerFieldPanel("ibDate", new PropertyModel(model, "totalECContribIBAmountDate"), "IB Date");
		add(ibDate);
		
		AmpTextFieldPanel<Double> invAmount = new AmpTextFieldPanel<Double>("invAmount", new PropertyModel<Double>(model, "totalECContribINVAmount"), "INV Amount");
		invAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(invAmount);
		AmpDatePickerFieldPanel invDate = new AmpDatePickerFieldPanel("invDate", new PropertyModel(model, "totalECContribINVAmountDate"), "INV Date");
		add(invDate);
		
		AmpTextFieldPanel<Double> centralAmount = new AmpTextFieldPanel<Double>("centralAmount", new PropertyModel<Double>(model, "totalNationalContribCentralAmount"), "Central Amount");
		centralAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(centralAmount);
		AmpDatePickerFieldPanel centralDate = new AmpDatePickerFieldPanel("centralDate", new PropertyModel(model, "totalNationalContribCentralAmountDate"), "Central Date");
		add(centralDate);
		
		AmpTextFieldPanel<Double> ifiAmount = new AmpTextFieldPanel<Double>("ifiAmount", new PropertyModel<Double>(model, "totalNationalContribIFIAmount"), "IFI Amount");
		ifiAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(ifiAmount);
		AmpDatePickerFieldPanel ifiDate = new AmpDatePickerFieldPanel("ifiDate", new PropertyModel(model, "totalNationalContribIFIAmountDate"), "IFI Date");
		add(ifiDate);
		
		AmpTextFieldPanel<Double> regionalAmount = new AmpTextFieldPanel<Double>("regionalAmount", new PropertyModel<Double>(model, "totalNationalContribRegionalAmount"), "Regional Amount");
		regionalAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(regionalAmount);
		AmpDatePickerFieldPanel regionalDate = new AmpDatePickerFieldPanel("regionalDate", new PropertyModel(model, "totalNationalContribRegionalAmountDate"), "Regional Date");
		add(regionalDate);
		
		AmpTextFieldPanel<Double> privIbAmount = new AmpTextFieldPanel<Double>("privIbAmount", new PropertyModel<Double>(model, "totalPrivateContribAmount"), "IB Amount");
		privIbAmount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				calculateTotalAmount(model);
				target.addComponent(totalAmount);
			}
		});
		add(privIbAmount);
		AmpDatePickerFieldPanel privIbDate = new AmpDatePickerFieldPanel("privIbDate", new PropertyModel(model, "totalPrivateContribAmountDate"), "IB Date");
		add(privIbDate);
		
	}

}
