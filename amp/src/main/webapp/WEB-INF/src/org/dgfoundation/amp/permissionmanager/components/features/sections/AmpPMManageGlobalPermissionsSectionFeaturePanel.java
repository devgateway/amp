/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.core.Permission;

/**
 * @author dan
 *
 */
public class AmpPMManageGlobalPermissionsSectionFeaturePanel extends AmpPMSectionFeaturePanel {

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     * @throws Exception
     */
    public AmpPMManageGlobalPermissionsSectionFeaturePanel(final String id, final IModel<Set<Permission>> permissionsModel, final IModel<Set<AmpTeam>> tm,  String fmName, boolean hideLabel) throws Exception {
        super(id, permissionsModel, fmName, hideLabel);
        // TODO Auto-generated constructor stub
        List<ITab> globalPermissionsTabs = new ArrayList<ITab>();

        globalPermissionsTabs.add(new AbstractTab(new Model(TranslatorUtil.getTranslation("Add Global Permission"))){
              public Panel getPanel(String panelId)
              {
                  AmpPMAddGlobalPermissionPanel newGlobalPerm = null;
                try {
                    newGlobalPerm = new AmpPMAddGlobalPermissionPanel(panelId, permissionsModel, tm, "Add Global Permission");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return newGlobalPerm;
              }
        });
        
        AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("globalPermsTabs", "Global Permissions", globalPermissionsTabs,true);
        add(objTabs);
    }
    


}
