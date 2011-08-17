package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.FeedbackLabel;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpNewResourceFieldPanel;

/**
 * Behavior that checks if a {@link FormComponent} is valid. Valid {@link FormComponent} objects get the CSS class
 * 'formcomponent valid' and invalid {@link FormComponent} objects get the CSS class 'formcomponent invalid'.
 *
 * See {@link AjaxFormComponentUpdatingBehavior} for more details over the parent class.
 *
 * You can use this code under Apache 2.0 license, as long as you retain the copyright messages.
 *
 * Tested with Wicket 1.3.4, Wicket 1.4.13
 * @author Daan, StuQ.nl
 * @author mpostelnicu@dgateway.org
 */
public class ComponentVisualErrorBehavior extends AjaxFormComponentUpdatingBehavior {

	private static final String INVALID_CLASS="formcomponent invalid";
	private String previousClass;
    /**
	 * 
	 */
	private static final long serialVersionUID = -75237005862925157L;
	/** Field updateComponent holds the component that must be updated when validation is done.*/
    private Component updateComponent = null;

    /**
     * Constructor.
     *
     * @param event of type {@link String} (for example 'onblur', 'onkeyup', etc.)
     * @param updateComponent is the {@link Component} that must be updated (for example the {@link FeedbackLabel}
     *        containing the error message for this {@link FormComponent})  
     */
    public ComponentVisualErrorBehavior(String event, Component updateComponent) {
        super(event);
        this.updateComponent=updateComponent;
    }

    /**
     * Listener invoked on the ajax request. This listener is invoked after the {@link Component}'s model has been
     * updated. Handles the change of a css class when an error has occurred.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     * @param e of type RuntimeException
     */
    @Override
    public void onError(final AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
    	if(updateComponent!=null) 
    		updateComponent.setVisible(true);
        changeCssClass(ajaxRequestTarget, false, INVALID_CLASS);
    }

  
    
    /**
     * Listener invoked on the ajax request. This listener is invoked after the {@link Component}'s model has been
     * updated. Handles the change of a css class when validation was succesful.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     */
    @Override
    public void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
    	if(updateComponent!=null) 
    		updateComponent.setVisible(false);
        changeCssClass(ajaxRequestTarget, true, previousClass);
    }

    /**
     * Changes the CSS class of the linked {@link FormComponent} via AJAX.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     * @param valid Was the validation successful?
     * @param cssClass The CSS class that must be set on the linked {@link FormComponent}
     */
    private void changeCssClass(AjaxRequestTarget ajaxRequestTarget, boolean valid, String cssClass) {
    	if(cssClass==null) return;
        FormComponent formComponent = getFormComponent();
        if(formComponent.isValid() == valid){
        	formComponent.add(
			new AttributeModifier("class", true, new Model(cssClass)) {
				@Override
				protected String newValue(final String currentValue,
						final String replacementValue) {
					if(!currentValue.equals(INVALID_CLASS)) previousClass=currentValue;
					return replacementValue;
				}
			});
      	
            ajaxRequestTarget.addComponent(formComponent);
        }

        if(updateComponent!=null){
            ajaxRequestTarget.addComponent(updateComponent.getParent());
        }
    }
}
