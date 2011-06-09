/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;

import bsh.This;

/**
 * This field can be used to count percentage items and show an error message when 100% is not reached.
 * This is done on the fly while the user types percentages...
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public abstract class AmpPercentageValidationHiddenField<T> extends
		AmpHiddenFieldPanel<Double> {


	/**
	 * Reloads {@link This} component through {@link AjaxRequestTarget}.
	 * This method clears the component input, so it is re-validated, it also simulates an 'onChange' event using jQuery
	 * @param target
	 */
	public void reloadValidationField(AjaxRequestTarget target) {
		target.addComponent(this);
		this.getHiddenContainer().clearInput();
		target.appendJavascript(String.format("$('#%s').trigger('change');", this.getHiddenContainer().getMarkupId()));
	}
	
	/**
	 * Constructs a new object, iterates through setModel<T> and gets each percentage from the <T> objects
	 * by the means of {@link #getPercentage(Object)}
	 * @param id {@link AmpComponentPanel#getId()}
	 * @param setModel the {@link Set} model holding the <T> objects 
	 * @param fmName
	 */
	public AmpPercentageValidationHiddenField(String id, final IModel<? extends Collection<T>> setModel,
			String fmName) {
		super(id, fmName);
		
		AbstractReadOnlyModel<Double> model=new AbstractReadOnlyModel<Double>() {
			@Override
			public Double getObject() {
				if(setModel.getObject().size()==0) return 100d;
				double total=0;
				for( T item : setModel.getObject()) 
					if(getPercentage(item)!=null) total+=getPercentage(item).doubleValue();
				return total;
			}
		};
		
		createHiddenContainer(model);
		
		hiddenContainer.setType(Double.class);
		hiddenContainer.add(new RangeValidator<Double>(100d, 100d));
	}
	
	/**
	 * Implement in subclasses to retrieve the specific percentage from the <T> item
	 * @param item
	 * @return the percentage, as a {@link Number}
	 */
	public abstract Number getPercentage(T item);
	

}
