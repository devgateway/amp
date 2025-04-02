package org.dgfoundation.amp.onepager.components;

import java.util.List;

import org.apache.wicket.markup.html.form.FormComponent;


/**
 * Marker interface for getting the FormComponents that will be required in a panel. 
 * All AMP panels that contain fields that should be made required/not required
 * when 'saving as draft' or 'submit and save' must implement this interface
 * @author eperez@dgateway.org  
 */
public interface AmpRequiredComponentContainer {

    
    public List<FormComponent<?>> getRequiredFormComponents ();
}
