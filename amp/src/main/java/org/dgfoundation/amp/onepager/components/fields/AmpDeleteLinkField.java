/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;

/**
 * {@link AmpLinkField} wrapping a delete icon image
 * 
 * @author mpostelnicu@dgateway.org since Nov 10, 2010
 * @see AmpLinkField
 */
public abstract class AmpDeleteLinkField extends AmpLinkField {

    /**
     * @param id
     * @param fmName
     */
    public AmpDeleteLinkField(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    public AmpDeleteLinkField(String id, String fmName,
            final IModel<String> qModel) {
        super(id, fmName, qModel);
    }

}
