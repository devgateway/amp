
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.items.AmpAgreementItemPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpFundingSummaryPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.FundingSectionSummaryEvent;
import org.dgfoundation.amp.onepager.models.ValueToSetModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;


public class AmpLoanDetailsSubsectionFeature extends
        AmpSubsectionFeaturePanel<AmpFunding> 
implements AmpRequiredComponentContainer{

    private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();
    
    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpLoanDetailsSubsectionFeature(String id,
            final IModel<AmpFunding> model, String fmName,final AmpFundingSummaryPanel sp) throws Exception {
        super(id, fmName, model, false, true);      
        
        final AmpTextFieldPanel<Float> interestRate = new AmpTextFieldPanel<Float>(
                "interestRate",
                new PropertyModel<Float>(model, "interestRate"), "Interest Rate", false, false);
        interestRate.getTextContainer().add(new RangeValidator<Float>(0f, 100f));
        interestRate.getTextContainer().add(new AttributeModifier("size", new Model<String>("5")));
        add(interestRate);

        final AmpTextFieldPanel<Integer> gracePeriod =  new AmpTextFieldPanel<Integer>("gracePeriod", new PropertyModel<Integer>(model, "gracePeriod"), "Grace Period", false, false);      
        add(gracePeriod);
        
        final PropertyModel<Date> ratificationDateModel = new PropertyModel<Date>(
                model, "ratificationDate");
        AmpDatePickerFieldPanel ratificationDate = new AmpDatePickerFieldPanel("ratificationDate", ratificationDateModel, null, "Ratification Date");
        add(ratificationDate);
        
        
        
        final PropertyModel<Date> maturityModel = new PropertyModel<Date>(
                model, "maturity");
        AmpDatePickerFieldPanel maturity = new AmpDatePickerFieldPanel("maturity", maturityModel, null, "Maturity");
        add(maturity);

    }

    public List<FormComponent<?>> getRequiredFormComponents() {
        return requiredFormComponents;
    }

    public void setRequiredFormComponents(List<FormComponent<?>> requiredFormComponents) {
        this.requiredFormComponents = requiredFormComponents;
    }
    
}
