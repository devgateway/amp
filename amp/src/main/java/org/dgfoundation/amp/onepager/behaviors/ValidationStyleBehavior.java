/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author mpostelnicu@dgateway.org since Sep 25, 2010
 */
public class ValidationStyleBehavior extends Behavior {


    /**
     * 
     */
    private static final long serialVersionUID = -7695692087436307207L;

    // Check if the component is valid and add the corresponding CSS style
    // attribute
    @Override
    public void onComponentTag(final Component component, final ComponentTag tag) {
        FormComponent comp = (FormComponent) component;
        if (comp.isValid() && comp.getConvertedInput() != null) {
            tag.getAttributes().put("class", "inputx");
        } else if (!comp.isValid()) {
            tag.getAttributes().put("class", "inputxInvalid");
        }
    }
}
