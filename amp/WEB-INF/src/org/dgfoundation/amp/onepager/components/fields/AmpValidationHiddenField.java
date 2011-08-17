/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;

import bsh.This;

public abstract class AmpValidationHiddenField<T> extends
		AmpHiddenFieldPanel<Double> implements IAjaxIndicatorAware {

	private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

	
	/**
	 * Reloads {@link This} component through {@link AjaxRequestTarget}.
	 * This method clears the component input, so it is re-validated, it also simulates an 'onChange' event using jQuery
	 * @param target
	 */
	public void reloadValidationField(AjaxRequestTarget target) {
		target.addComponent(this);
		this.getHiddenContainer().clearInput();
		target.appendJavascript(String.format("$('#%s').change();", this.getHiddenContainer().getMarkupId()));
	}
	
	/**
	 * Constructs a new object, iterates through setModel<T> and gets each percentage from the <T> objects
	 * by the means of {@link #getPercentage(Object)}
	 * @param id {@link AmpComponentPanel#getId()}
	 * @param setModel the {@link Set} model holding the <T> objects 
	 * @param fmName
	 */
	public AmpValidationHiddenField(String id, final IModel<? extends Collection<T>> collectionModel,
			String fmName) {
		super(id, fmName);
		
		createHiddenContainer(getHiddenContainerModel(collectionModel));
		
		
		add(indicatorAppender);
	}
	
	public abstract AbstractReadOnlyModel getHiddenContainerModel(IModel<? extends Collection<T>> collectionModel);

	@Override
	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}

}
