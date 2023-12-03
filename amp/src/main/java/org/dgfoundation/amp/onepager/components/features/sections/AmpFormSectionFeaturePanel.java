/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.commons.codec.binary.Hex;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Implements the aspect of a form section. A section is actually a {@link AmpFMTypes#MODULE}
 * but it has a special way of displaying itself. Its title is with blue background and the whole section
 * is a white table on the gray background of the form
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpFormSectionFeaturePanel extends AmpFeaturePanel {
    
    
    protected final IModel<AmpActivityVersion> am;
    private TransparentWebMarkupContainer mrk;

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpFormSectionFeaturePanel(String id, String fmName,final IModel<AmpActivityVersion> am)
            throws Exception {
        super(id, fmName);  
        this.am = am;
        this.labelContainer.add(new AttributeModifier("id", Hex.encodeHexString(fmName.getBytes())));
        
        mrk = new TransparentWebMarkupContainer("foldable");
        add(mrk);
    }

    public void setFolded(boolean folded){
        if (folded)
            mrk.add(new AttributeAppender("style", new Model("display: none;"), ""));
        else
            mrk.add(new AttributeAppender("style", new Model("display: block;"), ""));
            
    }
}
