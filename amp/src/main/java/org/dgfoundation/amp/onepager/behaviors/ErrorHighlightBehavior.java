/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

/**
 * @author http://code.google.com/p/elephas/
 * since Sep 24, 2010
 */
public class ErrorHighlightBehavior extends AttributeAppender {
        private static final long serialVersionUID = 1L;


        public ErrorHighlightBehavior() {
                super("class", true, new Model("error"), " ");
        }


        @Override
        public boolean isEnabled(Component component) {
                return !((FormComponent) component).isValid();
        }
}
