/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMManageFieldPermissionsSectionFeaturePanel extends AmpPMSectionFeaturePanel {

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageFieldPermissionsSectionFeaturePanel(String id, String fmName) throws Exception {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageFieldPermissionsSectionFeaturePanel(String id, IModel model, String fmName) throws Exception {
        super(id, model, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param teamsModel 
     * @param hideLabel
     * @param ampTreeVisibilityModel2 
     * @throws Exception
     */
    public AmpPMManageFieldPermissionsSectionFeaturePanel(String id,final IModel<AmpTreeVisibilityModelBean>  ampTreeVisibilityBeanModel, String fmName, final IModel<Set<AmpTeam>> teamsModel,final IModel<Set<AmpPMFieldPermissionViewer>> permsModel,  boolean hideLabel, final IModel<AmpTreeVisibility> ampTreeVisibilityModel) throws Exception {
        super(id, ampTreeVisibilityBeanModel, fmName, hideLabel);
        List<ITab> fieldPermissionsTabs = new ArrayList<ITab>();

        fieldPermissionsTabs.add(new AbstractTab(new Model(TranslatorUtil.getTranslation("Add Field Permission"))){
              public Panel getPanel(String panelId)
              {
                AmpPMAddFieldPermissionPanel newGlobalPerm = null;
                try {
                    newGlobalPerm = new AmpPMAddFieldPermissionPanel(panelId, ampTreeVisibilityBeanModel, "Add Field Permission", teamsModel, ampTreeVisibilityModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return newGlobalPerm;
              }
        });
        
        fieldPermissionsTabs.add(new AbstractTab(new Model(TranslatorUtil.getTranslation("View Assigned Field Permissions"))){
              public Panel getPanel(String panelId)
              {
                AmpPMViewFieldPermissionPanel viewGlobalPerm = null;
                try {
                    viewGlobalPerm = new AmpPMViewFieldPermissionPanel(panelId, permsModel, "View Assigned Field Permission");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return viewGlobalPerm;
              }
        });
        
        AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("fieldPermsTabs", "Field Permissions", fieldPermissionsTabs,true);
        add(objTabs);
        
    }

}
