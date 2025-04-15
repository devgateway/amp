/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.digijava.module.aim.helper.FormatHelper;

import java.text.NumberFormat;

/**
 * This wraps an {@link AmpTextFieldPanel} to encapsulate a Percentage field
 * used to capture percentages for locations, sectors, etc... This is always a
 * {@link Double} field and always {@link FormComponent#setRequired(boolean)} is
 * true When updated, this component notifies an external
 * {@link AmpPercentageCollectionValidatorField} so that it re-calculates itself
 * with the total of all percentages
 * 
 * @author mpostelnicu@dgateway.org
 * @since Feb 17, 2011
 */
public class AmpPercentageTextField extends AmpTextFieldPanel<Double> {

    private AmpPercentageCollectionValidatorField<?> validationHiddenField;
    
    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpPercentageTextField(String id, IModel<Double> model,
            String fmName,
            AmpPercentageCollectionValidatorField<?> validationHiddenField) {
        super(id, model, fmName, true, true, false, true);
        this.validationHiddenField = validationHiddenField;
        textContainer.setType(Double.class);
        textContainer.setRequired(true);
        textContainer.add(new RangeValidator<Double>(0.1d, null));
        textContainer.add(new AttributeModifier("style", "width: 40px;"));
    }
    
    //constructor added so we can include 0 value in range validator
    public AmpPercentageTextField(String id, IModel<Double> model,
            String fmName,
            AmpPercentageCollectionValidatorField<?> validationHiddenField,boolean required) {
        super(id, model, fmName, true, true, false, true);
        this.validationHiddenField = validationHiddenField;
        textContainer.setType(Double.class);
        textContainer.setRequired(required);
        textContainer.add(new RangeValidator<Double>(0.0d, null));
        textContainer.add(new AttributeModifier("style", "width: 40px;"));
    }
    
    @Override
    protected void onAjaxOnUpdate(AjaxRequestTarget target) {
        validationHiddenField.reloadValidationField(target);
    }
    
    public IConverter getInternalConverter(java.lang.Class<?> type) {
        DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
        NumberFormat formatter = FormatHelper.getPercentageDefaultFormat(true);
        
//      formatter.setMinimumFractionDigits(0);
        converter.setNumberFormat(getLocale(), formatter);
        return converter; 
    }

}
