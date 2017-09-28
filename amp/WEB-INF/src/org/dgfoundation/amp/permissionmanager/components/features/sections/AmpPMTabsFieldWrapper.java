/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;

/**
 * @author dan
 *
 */
public class AmpPMTabsFieldWrapper extends Panel {

    /**
     * @param id
     * @param fmName
     * @param hideLabel
     */
    public AmpPMTabsFieldWrapper(String id, String fmName, boolean hideLabel) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     * @param model
     */
    public AmpPMTabsFieldWrapper(String id, String fmName, IModel model) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     */
    public AmpPMTabsFieldWrapper(String id, IModel model, String fmName,
            boolean hideLabel) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpPMTabsFieldWrapper(String id, String fmName) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpPMTabsFieldWrapper(String id, IModel model, String fmName) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    public AmpPMTabsFieldWrapper(String id, String fmName, List<ITab> tabs, boolean hideLabel) {
        super(id);
        AjaxTabbedPanel atp = new AjaxTabbedPanel("tabs", tabs);
        atp.setOutputMarkupId(true);
        add(atp);
    }
 
}
