/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

import java.util.Iterator;
import java.util.List;

/**
 * @author aartimon@dginternational.org
 * since Oct 19, 2010
 */
public class AmpCommentTabsFieldWrapper extends AmpFieldPanel {

    public AmpCommentTabsFieldWrapper(String id, String fmName, List<ITab> tabs) {
        super(id, fmName);
        this.fmType = AmpFMTypes.MODULE;
        
        Iterator<ITab> it = tabs.iterator();
        while (it.hasNext()) {
            AmpCommentTab tab = (AmpCommentTab) it.next();
            tab.setParent(this);            
        }
        
        AjaxTabbedPanel atp = new AjaxTabbedPanel("tabs", tabs);
        atp.setOutputMarkupId(true);
        add(atp);
    }

}
