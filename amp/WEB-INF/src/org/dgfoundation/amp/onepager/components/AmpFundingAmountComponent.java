/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
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
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentFormTableAnnualBudget;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpComponentField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.MTEFYearsModel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.KeyValue;
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
	private Component date;
    private final IModel<List<KeyValue>> mtefYearsChoices = new AbstractReadOnlyModel<List<KeyValue>>() {
        private List<KeyValue> list = null;
        @Override
        public List<KeyValue> getObject() {
            if (list != null)
                return list;

            list = new ArrayList<KeyValue>(21);
            boolean fiscal = MTEFYearsModel.getFiscal();
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            //go back 10 years for historical data
            currentYear -= 10;
            //10 years back + current year + 10 years forward
            for (int i = 0; i < 21; i++){
                calendar.set(Calendar.YEAR, currentYear);
                list.add(MTEFYearsModel.convert(calendar.getTime(), fiscal));
                currentYear++;
            }
            return list;
        }
    };
 
	private Collection<AmpCollectionValidatorField> validationFields = new ArrayList<AmpCollectionValidatorField>();

    public AmpFundingAmountComponent(String id, IModel<T> model, String fmAmount,
                                     String propertyAmount, String fmCurrency, String propertyCurrency,
                                     String fmDate, String propertyDate, boolean isMTEFProjection) {
        this(id, model, fmAmount, propertyAmount, fmCurrency, propertyCurrency, fmDate, propertyDate,
                isMTEFProjection, false,null);
    }
    
    public AmpFundingAmountComponent(String id, IModel<T> model, String fmAmount,
            String propertyAmount, String fmCurrency, String propertyCurrency,
            String fmDate, String propertyDate, boolean isMTEFProjection,String amountSize) {
    	this(id, model, fmAmount, propertyAmount, fmCurrency, propertyCurrency, fmDate, propertyDate,
    			isMTEFProjection, false,amountSize);
    }

    
    protected AmpFundingAmountComponent(String id, IModel<T> model, String fmAmount,
			String propertyAmount, String fmCurrency, String propertyCurrency,
			String fmDate, String propertyDate, boolean isMTEFProjection, boolean fundingComponentTableMode,String amountSize) {
		super(id, model);

        boolean hideLabel = fundingComponentTableMode;
        boolean hideNewLine = fundingComponentTableMode;

		amount = new AmpTextFieldPanel<Double>("amount",
				new PropertyModel<Double>(model, propertyAmount), fmAmount, hideLabel, hideNewLine) {
			
			@Override
			protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
				AmpComponentPanel parentPanel = findParent(AmpFundingItemFeaturePanel.class);
				if(parentPanel ==null)
					parentPanel = findParent(AmpComponentField.class);				
				if(parentPanel ==null)
					parentPanel = findParent(AmpRegionalFundingItemFeaturePanel.class);
				if(parentPanel==null)
					parentPanel = findParent(AmpComponentFormTableAnnualBudget.class);
				parentPanel.visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField, Void>() {
					@Override
					public void component(AmpCollectionValidatorField component,
							IVisit<Void> visit) {
						component.reloadValidationField(target);
						visit.dontGoDeeper();
					}
				});
				onFundingDetailChanged(target);
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
		if(amountSize==null)
			amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("9")));
		else
			amount.getTextContainer().add(new AttributeModifier("size", new Model<String>(amountSize)));
		
		add(amount);
		
		
		AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
			@Override
			public List<AmpCurrency> getObject() {
				return (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode() ;
			}
		};
		
		currency = new AmpSelectFieldPanel<AmpCurrency>("currency",
				new PropertyModel<AmpCurrency>(model, propertyCurrency),
				currencyList, fmCurrency, hideLabel, false, null, hideNewLine) {
			@Override
			protected void onAjaxOnUpdate(AjaxRequestTarget target) {
				onFundingDetailChanged(target);
			}
		};
		currency.getChoiceContainer().setRequired(true);
		currency.getChoiceContainer().add(new AttributeModifier("class", "dropdwn_currency"));
		add(currency);
		if (!isMTEFProjection){
			AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel(
					"date", new PropertyModel<Date>(model, propertyDate),
					fmDate, null, hideLabel, hideNewLine) {
				@Override
				protected void onAjaxOnUpdate(AjaxRequestTarget target) {
					onFundingDetailChanged(target);
				}
			};
			datetmp.getDate().setRequired(true);
			datetmp.getDate().add(new AttributeModifier("class", "inputx_date"));
			date = datetmp;
		}
		else{

			MTEFYearsModel yearModel = new MTEFYearsModel(new PropertyModel<Date>(model, propertyDate));
//			AmpTextFieldPanel<String> datetmp = new AmpTextFieldPanel<String>("date", yearModel, fmDate, true, true);
//			datetmp.getTextContainer().setEnabled(false);
//			datetmp.getTextContainer().add(new AttributeModifier("size", new Model<String>("10")));
//			date = datetmp;


            AmpSelectFieldPanel<KeyValue> datetmp = new AmpSelectFieldPanel<KeyValue>("date", yearModel, mtefYearsChoices, fmDate, true, true, new ChoiceRenderer<KeyValue>("value", "key"));
            date = datetmp;
		}
		add(date);

        QuarterInformationPanel quarterInfo = new QuarterInformationPanel("quarterInfo", "Quarter Information Panel", hideNewLine);
        add(quarterInfo);

		setRenderBodyOnly(true);
	}
    
	/**
	 * Method called when the amount field value has been changed
	 * 
	 * @param target
	 */
	protected void onFundingDetailChanged(AjaxRequestTarget target) {
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
