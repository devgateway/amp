/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpComponentAnnualBudgetSubsectionFeature;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.events.ProposedProjectCostUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.ProposedProjectCostModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Set;



/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * 
 * @author aartimon@dginternational.org
 * @since Jan 11, 2012
 */
public class AmpProposedProjectCost extends AmpProjectCost {

    private static final long serialVersionUID = 3042844165981373432L;
    
    public AmpProposedProjectCost(String id, String fmName, IModel<AmpActivityVersion> am, 
            IModel<Set<AmpFundingAmount>> costsModel, AmpFundingAmount.FundingType type) throws Exception {
        super(id, fmName, am, costsModel, type);
        
        AmpComponentAnnualBudgetSubsectionFeature annualBudgets;
        try {
            annualBudgets = new AmpComponentAnnualBudgetSubsectionFeature("annualProposedCost", am,
                    "Annual Proposed Project Cost");
            add(annualBudgets);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected AmpTextFieldPanel<Double> getAmountField() {
        if (!FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost")) {
            return super.getAmountField();
        } else {
            AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("amount",
                    new ProposedProjectCostModel(new PropertyModel<>(projectCost, "currency"),
                    new PropertyModel<Double>(projectCost, "funAmount"), new PropertyModel<Set<AmpAnnualProjectBudget>>(am,
                            "annualProjectBudgets")), "Amount", false) {
                public IConverter getInternalConverter(java.lang.Class<?> type) {
                    return CustomDoubleConverter.INSTANCE;
                }

                @Override
                protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                    super.onAjaxOnUpdate(target);
                    if (!this.getTextContainer().isRequired()) {
                        ((AmpDatePickerFieldPanel) this.getParent().get("funDate")).getDate().setRequired(false);
                        String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
                                .get("funDate")).getDate().getMarkupId());
                        target.appendJavaScript(js);
                    } else if (this.getTextContainer().isRequired()
                            && this.getTextContainer().getModel().getObject() != null) {
                        ((AmpDatePickerFieldPanel) this.getParent().get("funDate")).getDate().setRequired(true);
                        String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
                                .get("funDate")).getDate().getMarkupId());
                        target.appendJavaScript(js);
                    }
                }

            };
            amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
            amount.add(UpdateEventBehavior.of(ProposedProjectCostUpdateEvent.class));
            amount.getTextContainer().add(new AttributeModifier("readonly", new Model<String>("readonly")));
            return amount;
        }
    }
    
}
