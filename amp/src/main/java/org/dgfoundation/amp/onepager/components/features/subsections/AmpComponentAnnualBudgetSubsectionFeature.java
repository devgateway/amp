package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentFormTableAnnualBudget;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.events.ProposedProjectCostUpdateEvent;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.util.CurrencyUtil;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

public class AmpComponentAnnualBudgetSubsectionFeature extends AmpSubsectionFeaturePanel<AmpActivityVersion> {

    protected AmpComponentFormTableAnnualBudget mtefTableFeature;

    public AmpComponentAnnualBudgetSubsectionFeature(String id, final IModel<AmpActivityVersion> model, String fmName)
            throws Exception {
        super(id, fmName, model);
        mtefTableFeature = new AmpComponentFormTableAnnualBudget("annualCostTable",
                "Annual Proposed Project Cost Table", model) {
            @Override
            protected void onFundingDetailChanged(AjaxRequestTarget target) {
                send(findParent(AmpProposedProjectCost.class), Broadcast.BREADTH, new ProposedProjectCostUpdateEvent(
                        target));
            }
        };
        add(mtefTableFeature);
        final IModel<Set<AmpAnnualProjectBudget>> setModel = new PropertyModel<Set<AmpAnnualProjectBudget>>(model,
                "annualProjectBudgets");

        AmpAjaxLinkField addMTEF = new AmpAjaxLinkField("addMTEF", "Add Projection", "Add Projection") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpAnnualProjectBudget projection = new AmpAnnualProjectBudget();
                projection.setAmount(0d);

                Calendar calendar = Calendar.getInstance();

                int currentYear = Util.getCurrentFiscalYear();

                Set<AmpAnnualProjectBudget> mtefSet = setModel.getObject();
            
                
                if (mtefSet != null) {
                    Iterator<AmpAnnualProjectBudget> it = mtefSet.iterator();
                    while (it.hasNext()) {
                        AmpAnnualProjectBudget mtefItem = (AmpAnnualProjectBudget) it.next();
                        calendar.setTime(mtefItem.getYear());
                        int mtefItemYear = calendar.get(Calendar.YEAR);
                        
                        if (mtefItemYear + 1 > currentYear)
                            currentYear = mtefItemYear + 1;
                    }
                }
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.YEAR, currentYear);
                projection.setYear(calendar.getTime());
                projection.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                mtefTableFeature.getEditorList().addItem(projection);
                
                target.add(mtefTableFeature);
            }
        };
        add(addMTEF);

    }

}
