/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.util;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author aartimon@dginternational.org
 * @since Aug 5, 2011 
 *
 */
public class AttributePrepender extends AttributeAppender{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public AttributePrepender(String attribute, IModel<?> appendModel,
            String separator) {
        super(attribute, appendModel, separator);
    }

    @Override
    protected String newValue(String currentValue, String replacementValue) {
        // swap currentValue and replacementValue in the call to the concatenator
        return super.newValue(replacementValue, currentValue);
    }
}
