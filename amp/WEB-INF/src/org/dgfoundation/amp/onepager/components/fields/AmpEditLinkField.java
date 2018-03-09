/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;

/**
 * {@link AmpLinkField} wrapping a delete icon image
 * 
 * @author dan
 * @see AmpLinkField
 */
public abstract class AmpEditLinkField extends AmpLinkField {

    /**
     * @param id
     * @param fmName
     */
    public AmpEditLinkField(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    public AmpEditLinkField(String id, String fmName, final IModel<String> qModel) {
        super(id, fmName, qModel);
    }
    
    public AmpEditLinkField(String id, String fmName, Boolean hideLabel, Boolean hideNewLine) {
        super(id, fmName,hideLabel, hideNewLine);
        // TODO Auto-generated constructor stub
    }

}
