/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentFormTableAnnualBudget;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.models.MTEFYearsModel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.*;

/**
 * Reusable component capturing an amount item in AMP (the tuple amount /
 * currency / date )
 * 
 * @author mpostelnicu@dgateway.org since Nov 2, 2010
 */
public class AmpFundingAmountComponent<T> extends Panel {

    private AmpTextFieldPanel<Double> amount;
    Boolean isMTEFProjection;
    private AmpStyledSelectFieldPanel<AmpCurrency> currency;
    private Component date;
    private final IModel<List<KeyValue>> mtefYearsChoices = new AbstractReadOnlyModel<List<KeyValue>>() {
        private List<KeyValue> list = null;
        @Override
        public List<KeyValue> getObject() {
            if (list != null) {
                return list;
            }

            int startYear = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.YEAR_RANGE_START);
            int range = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
            boolean fiscal = MTEFYearsModel.getFiscal();
            
            list = new ArrayList<KeyValue>(range);
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            
            for (int i = 0; i < range; i++) {
                calendar.set(Calendar.YEAR, startYear + i);
                list.add(MTEFYearsModel.convert(calendar.getTime(), fiscal));
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
        this.isMTEFProjection=isMTEFProjection;
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
                return CustomDoubleConverter.INSTANCE;
            }
        };
        amount.getTextContainer().setRequired(true);
        if(amountSize==null)
            amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("9")));
        else
            amount.getTextContainer().add(new AttributeModifier("size", new Model<String>(amountSize)));

        add(amount);


        PropertyModel<AmpCurrency> currentCurrency = new PropertyModel<AmpCurrency>(model, propertyCurrency);
        List<AmpCurrency> activeCurrencies = CurrencyUtil.getUsableNonVirtualCurrencies();
        boolean isCurrencyPresentInList = activeCurrencies.stream()
                .anyMatch(curr -> curr.getAmpCurrencyId().equals(currentCurrency.getObject().getAmpCurrencyId()));

        if (!isCurrencyPresentInList) {
            activeCurrencies.add(currentCurrency.getObject());
            activeCurrencies.sort(Comparator.comparing(AmpCurrency::getCurrencyCode));
        }

        AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
            @Override
            public List<AmpCurrency> getObject() {
                return activeCurrencies;
            }
        };

        currency = new AmpStyledSelectFieldPanel<AmpCurrency>("currency",
                new PropertyModel<AmpCurrency>(model, propertyCurrency),
                currencyList, fmCurrency, hideLabel, new IOptionRenderer<AmpCurrency>() {
            @Override
            public String getDisplayValue(AmpCurrency object) {
                return object.getCurrencyCode();
            }

            @Override
            public IModel<AmpCurrency> getModel(AmpCurrency value) {
                return Model.of(value);
            }
        }, hideNewLine) {

            @Override
            protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                onFundingDetailChanged(target);
            }

            @Override
            protected void customizeOption(SelectOption option, String text, IModel<? extends AmpCurrency> model) {
                super.customizeOption(option, text, model);

                if (!model.getObject().isActive()) {
                    option.add(new AttributeAppender("style", "color: gray;"));
                }
            }
        };

        currency.getSelectComponent().setRequired(true);
        currency.getSelectComponent().add(new AttributeModifier("class", "dropdwn_currency"));
        add(currency);


        if (!isMTEFProjection) {
            AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel(
                    "date", new PropertyModel<Date>(model, propertyDate),
                    fmDate, null, hideLabel, hideNewLine) {
                @Override
                protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                    onFundingDetailChanged(target);

                    FundingListEditor parentPanel = findParent(FundingListEditor.class);
                    parentPanel.visitChildren(AmpSimpleValidatorField.class,
                            new IVisitor<AmpSimpleValidatorField, Void>() {
                                @Override
                                public void component(AmpSimpleValidatorField component, IVisit<Void> visit) {
                                    component.reloadValidationField(target);
                                    visit.dontGoDeeper();
                                }
                            });
                    send(getPage(), Broadcast.BREADTH, new FreezingUpdateEvent(target));
                }
            };
            datetmp.getDate().setRequired(true);
            datetmp.getDate().add(new AttributeModifier("class", "inputx_date"));
            date = datetmp;
        } else if (!FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT)) {
            AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel(
                    "date", new PropertyModel<Date>(model, propertyDate), fmDate, null, hideLabel, hideNewLine);
            
            datetmp.getDate().setRequired(true);
            datetmp.getDate().add(new AttributeModifier("class", "inputx_date"));
            date = datetmp;
        } else {
            MTEFYearsModel yearModel = new MTEFYearsModel(new PropertyModel<Date>(model, propertyDate));

            AmpSelectFieldPanel<KeyValue> datetmp = new AmpSelectFieldPanel<KeyValue>("date", yearModel, 
                        mtefYearsChoices, fmDate, true, true, new ChoiceRenderer<KeyValue>("value", "key"));
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
        // when any of the fields has change we check if the three are not null
        // and if so
        // we trigger the event
        boolean dateValueNotNull = false;
        if (!isMTEFProjection) {
            dateValueNotNull = ((AmpDatePickerFieldPanel) date).getDate()
                    .getValue() != null;
        } else {
            dateValueNotNull = ((AmpSelectFieldPanel) date)
                    .getChoiceContainer().getValue() != null;
        }
        if (amount.getModel().getObject() != null
                && currency.getModel() != null && dateValueNotNull) {

            send(getPage(), Broadcast.BREADTH, new OverallFundingTotalsEvents(
                    target));
        }

    }

    public AmpTextFieldPanel<Double> getAmount() {
        return amount;
    }

    public AmpStyledSelectFieldPanel<AmpCurrency> getCurrency() {
        return currency;
    }

    public Component getDate() {
        return date;
    }
    private void sendEvent(AjaxRequestTarget target){
        
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
