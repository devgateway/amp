/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.models.MTEFYearsModel;
import org.digijava.module.aim.action.AddFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.CurrencyUtil;

import java.text.NumberFormat;
import java.util.*;

/**
 * Reusable component capturing an amount item in AMP (the tuple amount /
 * currency / date )
 * 
 * @author mpostelnicu@dgateway.org since Nov 2, 2010
 */
public class AmpFundingAmountComponent<T> extends Panel {

	private AmpTextFieldPanel<Double> amount;
	private AmpSelectFieldPanel<AmpCurrency> currency;
	private Component date;
 
	private Collection<AmpCollectionValidatorField> validationFields = new ArrayList<AmpCollectionValidatorField>();
	/**
	 * @param id
	 */
	public AmpFundingAmountComponent(String id, IModel<T> model, String fmAmount,
			String propertyAmount, String fmCurrency, String propertyCurrency,
			String fmDate, String propertyDate, boolean isMTEFProjection) {
		super(id, model);
		amount = new AmpTextFieldPanel<Double>("amount",
				new PropertyModel<Double>(model, propertyAmount), fmAmount,true,true) {
			
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
		amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("9")));
		
		
		add(amount);
		
		
		AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
			@Override
			public List<AmpCurrency> getObject() {
				return CurrencyUtil.getActiveAmpCurrencyByCode();
			}
		};
		
		currency = new AmpSelectFieldPanel<AmpCurrency>("currency",
				new PropertyModel<AmpCurrency>(model, propertyCurrency),
				currencyList,
				fmCurrency, true, false);
		currency.getChoiceContainer().setRequired(true);
		currency.getChoiceContainer().add(new AttributeModifier("class", "dropdwn_currency"));
		add(currency);
		if (!isMTEFProjection){
			AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel("date", new PropertyModel<Date>(
					model, propertyDate), fmDate,true);
			datetmp.getDate().setRequired(true);
			datetmp.getDate().add(new AttributeModifier("class", "inputx_date"));
			date = datetmp;
		}
		else{
			IModel<List<String>> mtefYearsChoices = new AbstractReadOnlyModel<List<String>>() {
				@Override
				public List<String> getObject() {
					List<KeyValue> years = AddFunding.generateAvailableMTEFProjectionYears(null);
					List<String> ret = new ArrayList<String>();
					Iterator<KeyValue> it = years.iterator();
					while (it.hasNext()) {
						KeyValue keyValue = (KeyValue) it.next();
						ret.add(keyValue.getKey());
					}
					return ret;
				}
			};
			MTEFYearsModel yearModel = new MTEFYearsModel(new PropertyModel<Date>(model, propertyDate));
			AmpTextFieldPanel<String> datetmp = new AmpTextFieldPanel<String>("date", yearModel, fmDate, true, true);
			datetmp.getTextContainer().setEnabled(false);
			datetmp.getTextContainer().add(new AttributeModifier("size", new Model<String>("10")));
			date = datetmp;
		}
		add(date);
		setRenderBodyOnly(true);
	}

	public AmpTextFieldPanel<Double> getAmount() {
		return amount;
	}

	public AmpSelectFieldPanel<AmpCurrency> getCurrency() {
		return currency;
	}

	public Component getDate() {
		return date;
	}
}
