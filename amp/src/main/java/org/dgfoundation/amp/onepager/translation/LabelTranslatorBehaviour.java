/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.AttributeModifier;

/**
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */
public class LabelTranslatorBehaviour extends AttributeModifier {

    private static final long serialVersionUID = 1L;
    
    private CharSequence key;

    public LabelTranslatorBehaviour() {
        super("onclick", "spawnEditBox(this.id)");
        
    }
    
}
