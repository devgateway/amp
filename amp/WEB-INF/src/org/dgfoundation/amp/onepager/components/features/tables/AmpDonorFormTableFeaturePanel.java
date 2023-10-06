/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;


import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ExpandableListEditor;
import org.dgfoundation.amp.onepager.components.ExpandableListNavigator;
import org.dgfoundation.amp.onepager.components.FundingListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpFreezingValidatorTransactionDateField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public abstract class AmpDonorFormTableFeaturePanel extends
    AmpFundingFormTableFeaturePanel<AmpFunding, AmpFundingDetail> {

    private static final int CURRENCY_RATE_MAXIMUM_FRACTION_DIGITS = 10;
    private static final int CURENCY_RATE_MINIMUM_INTEGER_DIGITS = 1;

    private static Logger logger = Logger.getLogger(AmpDonorFormTableFeaturePanel.class);
    
    protected IModel<Set<AmpFundingDetail>> parentModel;
    protected IModel<Set<AmpFundingDetail>> setModel;
    
    public AmpFundingItemFeaturePanel getParentFundingItem() {
        AmpFundingItemFeaturePanel parent=(AmpFundingItemFeaturePanel) this.getParent().getParent();
        return parent;
    }

    public AmpDonorFormTableFeaturePanel(String id,
            final IModel<AmpFunding> model, String fmName, final int transactionType,
            int titleHeaderColSpan) throws Exception {
        super(id, model, fmName);

        getTableId().add(new AttributeModifier("width", "620"));
        
        setTitleHeaderColSpan(titleHeaderColSpan);
        parentModel = new PropertyModel<Set<AmpFundingDetail>>(model,
                "fundingDetails");

        setModel = new AbstractMixedSetModel<AmpFundingDetail>(parentModel) {
            @Override
            public boolean condition(AmpFundingDetail item) {
                return item.getTransactionType().equals(transactionType);
            }
        };
    }

    protected AmpCategorySelectFieldPanel getAdjustmentTypeComponent(
            IModel<AmpFundingDetail> model, int transactionType) {
        
        String transactionTypeString = "";
        switch (transactionType) {
        case Constants.COMMITMENT:
            transactionTypeString = "Commitments";
            break;
        case Constants.DISBURSEMENT:
            transactionTypeString = "Disbursements";
            break;
        case Constants.DISBURSEMENT_ORDER:
            transactionTypeString = "Disbursement Orders";
            break;
        case Constants.EXPENDITURE:
            transactionTypeString = "Expenditures";
            break;
        case Constants.ESTIMATED_DONOR_DISBURSEMENT:
            transactionTypeString = "Estimated Donor Disbursements";
            break;
        case Constants.RELEASE_OF_FUNDS:
            transactionTypeString = "Release of Funds";
            break;
        case Constants.ARREARS:
            transactionTypeString = "Arrears";
            break;
        default:
            throw new RuntimeException("unsupported transaction type");
        }

        IModel<Set<AmpCategoryValue>> dependantModel = null;
        AmpCategoryClass categClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.TRANSACTION_TYPE_KEY);
        List<AmpCategoryValue> values = categClass.getPossibleValues();
        Iterator<AmpCategoryValue> it = values.iterator();
        while (it.hasNext()) {
            AmpCategoryValue val = it.next();
            if (val.getValue().compareTo(transactionTypeString) == 0){
                if (val.getUsedByValues() != null && val.getUsedByValues().size() > 0){
                    HashSet<AmpCategoryValue> tmp = new HashSet<AmpCategoryValue>();
                    tmp.add(val);
                    dependantModel = new Model(tmp);
                }
                break;
            }
        }
        
        try{
            AmpCategorySelectFieldPanel adjustmentTypes = new AmpCategorySelectFieldPanel(
                "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
                        new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
                        CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
                         false, false, false, dependantModel, false);
            adjustmentTypes.getChoiceContainer().setRequired(true);
            // adjustment type shouldn't be affected by overall freezing
            adjustmentTypes.setAffectedByFreezing(false);
            return adjustmentTypes;
        }catch(Exception e)
        {
            logger.error("AmpCategoryGroupFieldPanel initialization failed");
        }
        return null;

        
    }

    protected AmpFundingAmountComponent getFundingAmountComponent(
            IModel<AmpFundingDetail> model) {
        return new AmpFundingAmountComponent<AmpFundingDetail>("fundingAmount",
                model, "Amount", "displayedTransactionAmount", "Currency",
                "ampCurrencyId", "Transaction Date", "transactionDate", false);
    }

    public AmpComponentPanel getDisasterValidator(final AmpBooleanChoiceField disasterResponse) {
        return new AmpComponentPanel("disasterResponseRequired",
                "Required Validator for Disaster Response") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()) {
                    disasterResponse.getChoiceContainer().setRequired(true);
                    // requiredFormComponents.add(disasterResponse.getChoiceContainer());
                }
            }
        };
    }

    protected ListItem<AmpFundingDetail> appendFixedExchangeRateToItem(ListItem<AmpFundingDetail> item) {
        final PropertyModel<Double> fixedExchangeRateModel = new PropertyModel<Double>(item.getModel(), "fixedExchangeRate");
        @SuppressWarnings("serial")
        IModel<Boolean> fixedRate = new IModel<Boolean>(){
            @Override
            public Boolean getObject() {
                return fixedExchangeRateModel.getObject() != null;
            }
            @Override
            public void setObject(Boolean object) {}
            @Override
            public void detach() {}
        };

        final AmpTextFieldPanel<Double> exchangeRate = new AmpTextFieldPanel<Double>("fixedExchangeRate",
                fixedExchangeRateModel, "Exchange Rate", false, false) {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return new DoubleConverter() {
                    
                    @Override
                    public NumberFormat getNumberFormat(Locale locale) {
                        DecimalFormat format = FormatHelper.getDecimalFormat();
                        format.setMaximumFractionDigits(CURRENCY_RATE_MAXIMUM_FRACTION_DIGITS);
                        format.setMinimumIntegerDigits(CURENCY_RATE_MINIMUM_INTEGER_DIGITS);
                        DecimalFormatSymbols decimalFormatSymbols = format.getDecimalFormatSymbols();
                        
                        // org.apache.wicket.util.convert.converter.AbstractDecimalConverter.parse() 
                        // replace spaces with '\u00A0'. If the grouping separator is space ' ', the parse would fail.
                        if (decimalFormatSymbols.getGroupingSeparator() == ' ') {
                            decimalFormatSymbols.setGroupingSeparator('\u00A0');
                            format.setDecimalFormatSymbols(decimalFormatSymbols);
                        }
                        
                        return format;
                    }
                };
            }
            
            @Override
            protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
                exchangeRateOnAjaxOnUpdate(target);
            }
        };
        exchangeRate.getTextContainer().add(new RangeValidator<Double>(0.001d, null));
        exchangeRate.getTextContainer().add(new AttributeModifier("size", new Model<String>("6")));
        exchangeRate.setOutputMarkupId(true);
        exchangeRate.setIgnorePermissions(true);
        exchangeRate.setEnabled(fixedRate.getObject());
        exchangeRate.setAffectedByFreezing(false);
        item.add(exchangeRate);
    
        @SuppressWarnings("serial")
        AmpCheckBoxFieldPanel enableFixedRate = new AmpCheckBoxFieldPanel(
                "enableFixedRate", fixedRate, "Fixed exchange rate", false, false){
            @Override
            protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                Boolean state = this.getModel().getObject();
                fixedExchangeRateModel.setObject(state ? null : 0D);
                exchangeRate.setEnabled(!state);
                target.add(exchangeRate.getParent().getParent().getParent());
                enableFixedRateOnAjaxOnUpdate(target);
            }
            
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()) {
                    exchangeRate.setVisible(true);
                }
            }
        };
        item.add(enableFixedRate);
        return item;
    }

    protected void enableFixedRateOnAjaxOnUpdate(AjaxRequestTarget target) {

    }

    protected void exchangeRateOnAjaxOnUpdate(AjaxRequestTarget target) {

    }

    protected void addFreezingvalidator(ListItem<AmpFundingDetail> item) {
        item.add(new AmpFreezingValidatorTransactionDateField("freezingDateValidator", item.getModel(),
                "freezingDateValidator"));
    }

    protected void addExpandableList() {
        if (list instanceof FundingListEditor && ((FundingListEditor) list).isExpandable()) {
            final ExpandableListNavigator<AmpFundingDetail> pln = new ExpandableListNavigator<AmpFundingDetail>(
                    "expandableNavigator", (ExpandableListEditor) list);
            pln.setOutputMarkupId(true);
            add(pln);
        } else {
            add(new EmptyPanel("expandableNavigator"));
        }
    }
}
