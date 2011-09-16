/**
 * 
 */
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.FeedbackLabel;

/**
 * @author mihai
 * This flavor of {@link AmpComponentVisualErrorInterface} is used to capture the update of {@link RadioChoice}S which act differently than the rest of components.
 * Thus, we use {@link AjaxFormChoiceComponentUpdatingBehavior} as superclass...
 * @see ComponentVisualErrorBehavior for all other components but {@link RadioChoice}
 */
public class ChoiceComponentVisualErrorBehavior extends AjaxFormChoiceComponentEventUpdatingBehavior implements AmpComponentVisualErrorInterface {

	
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
    public ChoiceComponentVisualErrorBehavior(String event,Component updateComponent) {
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
    	OnePagerUtil.changeCssClass(this,ajaxRequestTarget, false, INVALID_CLASS,false);
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
    	OnePagerUtil.changeCssClass(this,ajaxRequestTarget, true, previousClass,false);
    }

	@Override
	public String getPreviousClass() {
		return previousClass;
	}

	@Override
	public void setPreviousClass(String s) {
		this.previousClass=s;
		
	}

	@Override
	public Component getUpdateComponent() {
		return updateComponent;
	}

	@Override
	public FormComponent<?> getRelatedFormComponent() {
		return getFormComponent();
	}

}
