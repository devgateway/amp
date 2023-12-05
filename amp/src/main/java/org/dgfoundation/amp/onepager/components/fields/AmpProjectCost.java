package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.events.ProposedProjectCostUpdateEvent;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Common Project Cost part, moved from {@link AmpProposedProjectCost}
 */
public class AmpProjectCost extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {

    private static final long serialVersionUID = 8966678577748925022L;
    protected IndicatingAjaxLink button;
    private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();
    
    protected IModel<AmpFundingAmount> projectCost;
    protected final IModel<AmpActivityVersion> am;
    protected final AmpDatePickerFieldPanel date;
    
    public AmpProjectCost(String id, String fmName, IModel<AmpActivityVersion> am, 
            IModel<Set<AmpFundingAmount>> costsModel, AmpFundingAmount.FundingType type) throws Exception {
        super(id, fmName);
        final boolean isPPC = AmpFundingAmount.FundingType.PROPOSED.equals(type);
        this.am = am;
        
        for (AmpFundingAmount afa : costsModel.getObject()) {
            if (type.equals(afa.getFunType())) {
                projectCost = new Model<AmpFundingAmount>(afa);
            }
        }
        if (projectCost == null) {
            projectCost = new Model<AmpFundingAmount>(new AmpFundingAmount());
            projectCost.getObject().setFunType(type);
            costsModel.getObject().add(projectCost.getObject());
        }
        
        
        final String cName = type.title + " Project Cost";
        add(new Label("costName",  new Model<String>(TranslatorWorker.translateText(cName))));
        
        final AmpTextFieldPanel<Double> amount = getAmountField();
        PropertyModel<AmpCurrency> currencyModel = new PropertyModel<>(projectCost, "currency");

        AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
            @Override
            public List<AmpCurrency> getObject() {
                return CurrencyUtil.getActiveAmpCurrencyByCode();
            }
        };

        if (currencyModel.getObject() == null) {
            currencyModel.setObject(CurrencyUtil.getWicketWorkspaceCurrency());
        }

        AmpSelectFieldPanel<AmpCurrency> currency = new AmpSelectFieldPanel<AmpCurrency>("currency", currencyModel,
                currencyList, "Currency", false, false) {
            protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                if (isPPC)
                    send(findParent(AmpProposedProjectCost.class), Broadcast.BREADTH, new ProposedProjectCostUpdateEvent(
                        target));
            }
        };
        currency.setOutputMarkupId(true);
        add(currency);
        
        final PropertyModel<Date> funDateModel = new PropertyModel<Date>(projectCost, CategoryConstants.PROPOSE_PRJC_DATE_KEY);
        date = new AmpDatePickerFieldPanel("funDate", funDateModel, null,
                CategoryConstants.PROPOSE_PRJC_DATE_NAME);

        // validator for poposed project cost amount AMP-17254
        add(new AmpComponentPanel("amountRequired", "Required Validator for Cost Amount") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()) {
                    amount.getTextContainer().setRequired(true);
                    date.getDate().setRequired(true);
                    requiredFormComponents.add(date.getDate());
                    requiredFormComponents.add(amount.getTextContainer());
                }
            }
        });
        // the amount should be added to the form AFTER the validator so it gets
        // the star
        add(amount);
        add(date);
    }
     
    /**
     * Return a list of FormComponent that are marked as required for this panel
     * 
     * @return List<FormComponent<?>> with the FormComponent
     */
    public List<FormComponent<?>> getRequiredFormComponents() {
        return requiredFormComponents;
    }
    
    protected AmpTextFieldPanel<Double> getAmountField() {
        AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("amount", new PropertyModel<Double>(projectCost,
                CategoryConstants.PROJECT_AMOUNT_KEY), CategoryConstants.PROJECT_AMOUNT_NAME, false,false,false) {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
                NumberFormat formatter = FormatHelper.getDecimalFormat(true);
                converter.setNumberFormat(getLocale(), formatter);
                return converter;
            }

            @Override
            protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                super.onAjaxOnUpdate(target);
                if (!this.getTextContainer().isRequired()) {
                    date.getDate().setRequired(false);
                    String js = String.format("$('#%s').change();", date.getDate().getMarkupId());
                    target.appendJavaScript(js);
                } else if (this.getTextContainer().isRequired()
                        && this.getTextContainer().getModel().getObject() != null) {
                    date.getDate().setRequired(true);
                    String js = String.format("$('#%s').change();", date.getDate().getMarkupId());
                    target.appendJavaScript(js);
                }
            }

        };
        amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
        return amount;
    }
}
