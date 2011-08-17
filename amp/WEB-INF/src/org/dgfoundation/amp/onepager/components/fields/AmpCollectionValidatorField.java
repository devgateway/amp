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

/**
 * Etend this class to implement validation for collections of items (like validation of sum of percentages, or size of a collection)
 * @author mihai
 *
 * @param <T> The collection's item type
 * @param <H> The hidden field's data type. For example if you count stuff, u only need Integer. If you want to do percentage
 * validation, you might want to use Float
 */
public abstract class AmpCollectionValidatorField<T,H> extends
		AmpHiddenFieldPanel<H> implements IAjaxIndicatorAware {

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
	public AmpCollectionValidatorField(String id, final IModel<? extends Collection<T>> collectionModel,
			String fmName) {
		super(id, fmName);
		
		hiddenContainer.setModel(getHiddenContainerModel(collectionModel));
		
		
		add(indicatorAppender);
	}
	
	public abstract AbstractReadOnlyModel getHiddenContainerModel(IModel<? extends Collection<T>> collectionModel);

	@Override
	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}

	
}
