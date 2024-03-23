/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.models.ActivityFYModel;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpActivityBudgetExtrasPanel extends AmpFieldPanel<AmpActivityVersion>
implements AmpRequiredComponentContainer{
    private static final long serialVersionUID = 1L;
    private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>> ();

    public AmpActivityBudgetExtrasPanel(String id, IModel<AmpActivityVersion> model, String fmName) {
        super(id, model, fmName, true);
        this.fmType = AmpFMTypes.MODULE;
        

        AmpCheckBoxFieldPanel indirectOnBudget = new AmpCheckBoxFieldPanel("indirectOnBudget", "Indirect On Budget", new PropertyModel<Boolean>(model, "indirectOnBudget"));
        add(indirectOnBudget);
        List<String> years = ActivityUtil.getFiscalYearsRange();
        final AmpSelectFieldPanel fy = new AmpSelectFieldPanel<String>("fy", new ActivityFYModel(new PropertyModel<String>(model, "FY")), years, "FY", false, true, false);

        fy.getChoiceContainer().setOutputMarkupId(true);
        fy.setOutputMarkupId(true);
        fy.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange"){
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(fy);     
            }       
          });
        // add(new AmpTextFieldPanel<String>("fy", new PropertyModel(model,
        // "FY"), "FY", false, false));
        add(new AmpComponentPanel("fyRequired", "Required Validator for fy" ) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()){
                    fy.getChoiceContainer().setRequired(true);
                    requiredFormComponents.add(fy.getChoiceContainer());
                    
                }
            }
        });     
        
        add(fy);
        final AmpTextFieldPanel<String> projectCode = new AmpTextFieldPanel<String>("projectCode", new PropertyModel<String>(model, "projectCode"), "Project Code", false, false,false,true);
        projectCode.setTextContainerDefaultMaxSize();
        add(projectCode);
        
        final AmpTextFieldPanel<String> vote = new AmpTextFieldPanel<String>("vote", new PropertyModel<String>(model, "vote"), "Vote", false, false,false,true);
        final AmpTextFieldPanel<String> subVote = new AmpTextFieldPanel<String>("subVote", new PropertyModel<String>(model, "subVote"), "Sub-Vote", false, false,false,true);
        final AmpTextFieldPanel<String> subProgram = new AmpTextFieldPanel<String>("subProgram", new PropertyModel<String>(model, "subProgram"), "Sub-Program", false, false,false,true);
        final AmpTextFieldPanel<String> ministryCode = new AmpTextFieldPanel<String>("ministryCode", new PropertyModel<String>(model, "ministryCode"), "Ministry Code", false, false,false,true);
        vote.getTextContainer().setRequired(true);
        subVote.getTextContainer().setRequired(true);
        subProgram.getTextContainer().setRequired(true);
        ministryCode.getTextContainer().setRequired(true);
        projectCode.getTextContainer().setRequired(true);
        requiredFormComponents.add(vote.getTextContainer());
        requiredFormComponents.add(subVote.getTextContainer());
        requiredFormComponents.add(subProgram.getTextContainer());
        requiredFormComponents.add(ministryCode.getTextContainer());
        requiredFormComponents.add(projectCode.getTextContainer());
        vote.setTextContainerDefaultMaxSize();
        add(vote);
        subVote.setTextContainerDefaultMaxSize();
        add(subVote);
        subProgram.setTextContainerDefaultMaxSize();
        add(subProgram);
        ministryCode.setTextContainerDefaultMaxSize();
        add(ministryCode);
        
    }

    @Override
    public List<FormComponent<?>> getRequiredFormComponents() {
        return requiredFormComponents;
    }
}
