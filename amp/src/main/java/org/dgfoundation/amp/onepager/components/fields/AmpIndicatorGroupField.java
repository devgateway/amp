/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

import java.util.Date;

/**
 * Group of fields for ME Indicator
 * 
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpIndicatorGroupField extends AmpFieldPanel<AmpIndicatorValue>{

    private static final long serialVersionUID = 0L;
    private AmpTextFieldPanel<Double> value;
    private AmpDatePickerFieldPanel date;
    
    public AmpTextFieldPanel<Double> getValue() {
        return value;
    }


    public AmpDatePickerFieldPanel getDate() {
        return date;
    }


    
    public AmpIndicatorGroupField(String id, IModel<AmpIndicatorValue> model, String fmName, String fieldPrefix) {
        super(id, model, fmName, true);

        value = new AmpTextFieldPanel<Double>("value", new PropertyModel<Double>(model, "value"), fieldPrefix + " "
                + "Value") {


            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        value.getTextContainer().setRequired(true);
        add(value);
        
        date = new AmpDatePickerFieldPanel("valueDate", new PropertyModel<Date>(model, "valueDate"), fieldPrefix + " Date");
        date.getDate().setRequired(true);
        add(date);
        
        AmpTextAreaFieldPanel comments  = new AmpTextAreaFieldPanel("comment", new PropertyModel<String>(model, "comment"), fieldPrefix + " Comments", false);
        add(comments);
    }


    public AmpIndicatorGroupField(String id, IModel<Double> val, IModel<Date> valueDate, IModel<String> comment, String fmName, String fieldPrefix, boolean isRequired) {
        super(id, fmName, true);
        this.fmType = AmpFMTypes.MODULE;
        
        value = new AmpTextFieldPanel<Double>("value", val, fieldPrefix + " Value", false, false) {


            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        value.getTextContainer().setRequired(isRequired);
        value.getTextContainer().setType(Double.class);
        add(value);
        
        date = new AmpDatePickerFieldPanel("valueDate", valueDate, fieldPrefix + " Date");
        if (fieldPrefix.compareTo("Revised") != 0)
            date.getDate().setRequired(isRequired);
        add(date);
        
        AmpTextAreaFieldPanel comments = new AmpTextAreaFieldPanel("comment", comment, fieldPrefix + " Comments", false);
        add(comments);
    }
}
