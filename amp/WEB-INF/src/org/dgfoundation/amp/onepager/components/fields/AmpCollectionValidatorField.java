/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.validators.AmpSemanticValidator;

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
        AmpSemanticValidatorField<H> implements IAjaxIndicatorAware {

    
    /**
     * Reloads {@link This} component through {@link AjaxRequestTarget}.
     * This method clears the component input, so it is re-validated, it also simulates an 'onChange' event using jQuery
     * @param target
     */
    public void reloadValidationField(AjaxRequestTarget target) {
        reloadValidationField(target, true);
    }
    
    /**
     * Constructs a new object, iterates through setModel<T> and gets each percentage from the <T> objects
     * by the means of {@link #getPercentage(Object)}
     * @param id {@link AmpComponentPanel#getId()}
     * @param setModel the {@link Set} model holding the <T> objects 
     * @param fmName
     */
    public AmpCollectionValidatorField(String id, final IModel<? extends Collection<T>> collectionModel,
            String fmName, AmpSemanticValidator<H> semanticValidator) {
        super(id, fmName,semanticValidator);
        
        hiddenContainer.setModel(getHiddenContainerModel(collectionModel));
    }
    
    /**
     * Reloads {@link This} component through {@link AjaxRequestTarget}.
     * This method clears the component input, so it is re-validated,
     * It also simulates an 'onChange' event using jQuery if the prependJS param is set to true
     * @param target
     * @param prependJS
     */
    public void reloadValidationField(AjaxRequestTarget target, boolean prependJS) {
        if (this.isVisible()) {
            target.add(this);
            this.getHiddenContainer().clearInput();
            
            if (prependJS) {
                target.prependJavaScript(String.format("$('#%s').blur();", this.getHiddenContainer().getMarkupId()));
            }
        }
    }
    
    public abstract IModel getHiddenContainerModel(IModel<? extends Collection<T>> collectionModel);

    
}
