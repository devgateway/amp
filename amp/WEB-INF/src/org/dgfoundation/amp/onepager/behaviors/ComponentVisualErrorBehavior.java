package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.dgfoundation.amp.onepager.OnePagerUtil;

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
public class ComponentVisualErrorBehavior extends AjaxFormComponentUpdatingBehavior implements AmpComponentVisualErrorInterface {

	
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
     * @param updateComponent is the {@link Component} that must be updated  
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
        OnePagerUtil.changeCssClass(this,ajaxRequestTarget, false, INVALID_CLASS,true);
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
    	OnePagerUtil.changeCssClass(this,ajaxRequestTarget, true, previousClass,true);
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
