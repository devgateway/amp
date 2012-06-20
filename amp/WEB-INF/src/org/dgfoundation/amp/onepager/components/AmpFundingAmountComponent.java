/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.DoubleConverter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpComponentField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * Reusable component capturing an amount item in AMP (the tuple amount /
 * currency / date )
 * 
 * @author mpostelnicu@dgateway.org since Nov 2, 2010
 */
public class AmpFundingAmountComponent<T> extends Panel {

	private AmpTextFieldPanel<Double> amount;
	private AmpSelectFieldPanel<AmpCurrency> currency;
	private AmpDatePickerFieldPanel date;
 
	private Collection<AmpCollectionValidatorField> validationFields = new ArrayList<AmpCollectionValidatorField>();
	/**
	 * @param id
	 */
	public AmpFundingAmountComponent(String id, IModel<T> model, String fmAmount,
			String propertyAmount, String fmCurrency, String propertyCurrency,
			String fmDate, String propertyDate) {
		super(id, model);
		amount = new AmpTextFieldPanel<Double>("amount",
				new PropertyModel<Double>(model, propertyAmount), fmAmount,true) {
			
			@Override
			protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
				AmpComponentPanel parentPanel = findParent(AmpFundingItemFeaturePanel.class);
				if(parentPanel ==null)
					parentPanel = findParent(AmpComponentField.class);				
				if(parentPanel ==null)
					parentPanel = findParent(AmpRegionalFundingItemFeaturePanel.class);
				parentPanel.visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField, Void>() {
					@Override
					public void component(AmpCollectionValidatorField component,
							IVisit<Void> visit) {
						component.reloadValidationField(target);
						visit.dontGoDeeper();
					}
				});
			}
			
			public IConverter getInternalConverter(java.lang.Class<?> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat formatter = FormatHelper.getDecimalFormat(true);
				
//				formatter.setMinimumFractionDigits(0);
				converter.setNumberFormat(getLocale(), formatter);
				return converter; 
			}
		};
		amount.getTextContainer().setRequired(true);
		amount.getTextContainer().add(new AttributeModifier("size", true, new Model<String>("9")));
		
		
		add(amount);
		
		
		AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
			@Override
			public List<AmpCurrency> getObject() {
				return (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode() ;
			}
		};
		
		currency = new AmpSelectFieldPanel<AmpCurrency>("currency",
				new PropertyModel<AmpCurrency>(model, propertyCurrency),
				currencyList,
				fmCurrency, true, false);
		currency.getChoiceContainer().setRequired(true);
		currency.getChoiceContainer().add(new SimpleAttributeModifier("class", "dropdwn_currency"));
		add(currency);
		date = new AmpDatePickerFieldPanel("date", new PropertyModel<Date>(
				model, propertyDate), fmDate,true);
		date.getDate().setRequired(true);
		date.getDate().add(new SimpleAttributeModifier("class", "inputx_date"));
		add(date);
		setRenderBodyOnly(true);
	}

	public AmpTextFieldPanel<Double> getAmount() {
		return amount;
	}

	public AmpSelectFieldPanel<AmpCurrency> getCurrency() {
		return currency;
	}

	public AmpDatePickerFieldPanel getDate() {
		return date;
	}
	
	public void setAmountValidator(final AmpCollectionValidatorField validationHiddenField){
		validationFields.add(validationHiddenField);
		/*
		amount.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur") {
			@Override
			protected void onUpdate(final AjaxRequestTarget target) {
				AmpComponentPanel parentPanel = findParent(AmpFundingItemFeaturePanel.class);
				if(parentPanel ==null)
					parentPanel = findParent(AmpComponentField.class);				
				if(parentPanel ==null)
					parentPanel = findParent(AmpRegionalFundingItemFeaturePanel.class);
				parentPanel.visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField, Void>() {
					@Override
					public void component(AmpCollectionValidatorField component,
							IVisit<Void> visit) {
						component.reloadValidationField(target);
						visit.dontGoDeeper();
					}
				});
			}
		});
		 */
	}
}
