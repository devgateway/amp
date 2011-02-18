/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

/**
 * Group of fields for ME Indicator
 * 
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpIndicatorGroupField extends AmpFieldPanel<AmpIndicatorValue>{

	private static final long serialVersionUID = 0L;

	public AmpIndicatorGroupField(String id, IModel<AmpIndicatorValue>model, String fmName, String fieldPrefix) {
		super(id, model, fmName, true);
		
		AmpTextFieldPanel<Double> value = new AmpTextFieldPanel<Double>("value", new PropertyModel<Double>(model, "value"), fieldPrefix + " Value");
		add(value);
		
		AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("valueDate", new PropertyModel<Date>(model, "valueDate"), fieldPrefix + " Date");
		add(date);
		
		AmpTextAreaFieldPanel<String> comments = new AmpTextAreaFieldPanel<String>("comment", new PropertyModel<String>(model, "comment"), fieldPrefix + " Comments", false);
		add(comments);
	}
}