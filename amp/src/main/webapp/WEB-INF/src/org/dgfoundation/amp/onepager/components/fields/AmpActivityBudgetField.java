/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.models.ActivityBudgetModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * Activity Budget Field
 * 
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpActivityBudgetField extends AmpFieldPanel {

    private final Panel budgetExtras;
    private final AmpBudgetClassificationField budgetClassification;

    private void toggleExtraFields(boolean b){
        budgetExtras.setVisible(b);
        budgetClassification.toggleActivityBudgetVisibility(b);
    }
    
    private void updateExtraFields(AjaxRequestTarget target){
        target.add(budgetExtras.getParent());
        budgetClassification.addToTargetActivityBudget(target);
    }
    
    public AmpActivityBudgetField(String id, final IModel model, String fmName, final Panel budgetExtras, final AmpBudgetClassificationField budgetClassification) {
        super(id, model, fmName);
        this.fmType = AmpFMTypes.MODULE;
        
        this.budgetExtras = budgetExtras;
        this.budgetClassification = budgetClassification;

        Integer currentSelection = (Integer) model.getObject();
        if (currentSelection == null){
            currentSelection = new Integer(-1);
            model.setObject(currentSelection);
        }
        if (currentSelection == 1) //"On" is selected
            toggleExtraFields(true);
        else
            toggleExtraFields(false);

        final String[] budgetElements = new String[] {"No answer", "Off", "On", "Treasury"};
        DropDownChoice budgetDD = new DropDownChoice("choice", new ActivityBudgetModel(model, budgetElements), Arrays.asList(budgetElements), new TranslatedChoiceRenderer<String>()); 
        budgetDD.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Integer obj = (Integer) model.getObject();
                boolean previousValue = budgetExtras.isVisible();
                if (obj == 1) // "On" was selected
                    toggleExtraFields(true);
                else
                    toggleExtraFields(false);
                updateExtraFields(target);
            }
        });
        
        add(budgetDD);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
