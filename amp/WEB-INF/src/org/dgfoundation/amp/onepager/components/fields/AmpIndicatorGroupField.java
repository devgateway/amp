/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Date;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

/**
 * Group of fields for ME Indicator
 * 
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpIndicatorGroupField extends AmpFieldPanel<AmpIndicatorValue>{

	private static final long serialVersionUID = 0L;

	public AmpIndicatorGroupField(String id, IModel<AmpIndicatorValue> model, String fmName, String fieldPrefix) {
		super(id, model, fmName, true);
		
		AmpTextFieldPanel<Double> value = new AmpTextFieldPanel<Double>("value", new PropertyModel<Double>(model, "value"), fieldPrefix + " Value");
		add(value);
		
		AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("valueDate", new PropertyModel<Date>(model, "valueDate"), fieldPrefix + " Date");
		add(date);
		
		AmpTextAreaFieldPanel<String> comments = new AmpTextAreaFieldPanel<String>("comment", new PropertyModel<String>(model, "comment"), fieldPrefix + " Comments", false);
		add(comments);
	}


	public AmpIndicatorGroupField(String id, IModel<Double> value, IModel<Date> valueDate, IModel<String> comment, String fmName, String fieldPrefix) {
		super(id, fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		AmpTextFieldPanel<Double> val = new AmpTextFieldPanel<Double>("value", value, fieldPrefix + " Value", false, false, Double.class);
		add(val);
		
		AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("valueDate", valueDate, fieldPrefix + " Date");
		add(date);
		
		AmpTextAreaFieldPanel<String> com = new AmpTextAreaFieldPanel<String>("comment", comment, fieldPrefix + " Comments", false);
		add(com);
	}
}