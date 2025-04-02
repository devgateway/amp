/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentPanel;
import org.digijava.module.aim.dbentity.AmpActivity;

/**
 * @author dan
 *
 */
public class AmpPMTab extends AbstractTab {

    private Class<AmpPMGlobalPanel> panelClass;
    private AmpPMGlobalPanel panel;
    private String fmName;
    
    /**
     * @param title
     */
    public AmpPMTab(IModel<String> title) {
        super(title);
        // TODO Auto-generated constructor stub
    }

    
    @Override
    public Panel getPanel(String panelId) {
        try {
            Constructor c = panelClass.getConstructor(String.class, String.class);
            return (Panel) c.newInstance(panelId, fmName);
            //return panel;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AmpPMTab(String title, String fmName, Class<AmpPMGlobalPanel> panelClass) {
        super(new Model(title));
        this.panelClass = panelClass;
        this.fmName = fmName;
    }

    
    public AmpPMTab(String title, String fmName, AmpPMGlobalPanel panel) {
        super(new Model(title));
        this.panel = panel;
        this.fmName = fmName;
    }
}
