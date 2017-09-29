/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author mpostelnicu@dgateway.org
 * since Nov 10, 2010
 */
public abstract class  AmpAddLinkField extends AmpLinkField {

    /**
     * @param id
     * @param fmName
     */
    public AmpAddLinkField(String id, String fmName) {
        super(id, fmName);
    }
}
