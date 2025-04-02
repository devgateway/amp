/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMInfo;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.util.FMUtil.PathException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Tab wrapper for AmpCommentPanel to be used with AjaxTabbedPanel
 * 
 * @author aartimon@dginternational.org
 * since Oct 7, 2010
 */
public class AmpCommentTab<T extends AmpFieldPanel<?>> extends AbstractTab {

    private static final long serialVersionUID = 1L;
    
    private Class<AmpCommentPanel> panelClass;
    private String fmName;
    private IModel<AmpActivityVersion> activityModel;
    private Component parent;

    @Override
    public Panel getPanel(String panelId) {
        try {
            Constructor c = panelClass.getConstructor(String.class, String.class, IModel.class);
            return (Panel) c.newInstance(panelId, fmName, activityModel);
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

    public AmpCommentTab(String title, String fmName, IModel<AmpActivityVersion> activityModel, Class<AmpCommentPanel> panelClass) {
        super(new Model(title));
        this.panelClass = panelClass;
        this.fmName = fmName;
        this.activityModel = activityModel;
    }

    @Override
    public boolean isVisible() {
        if (((AmpAuthWebSession)Session.get()).isFmMode())
            return true;
        try {
            LinkedList<FMInfo> path = FMUtil.getFmPath(parent);
            path.add(new FMInfo(AmpFMTypes.MODULE, fmName));
            String pathString = FMUtil.getFmPathString(path);
            return FMUtil.isFmVisible(pathString, AmpFMTypes.MODULE);
        } catch (PathException ignored) {
        }
        return super.isVisible();
    }
    
    public void setParent(Component parent) {
        this.parent = parent;
    }
    
}
