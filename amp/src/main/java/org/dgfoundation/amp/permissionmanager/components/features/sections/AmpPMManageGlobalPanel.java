/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMViewGlobalPermsTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.um.exception.UMException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends AmpComponentPanel {
    
    
    
    public AmpPMManageGlobalPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, String fmName, boolean hideLabel) throws Exception {
        super(id, fmName);
        super.setOutputMarkupId(true);
        
        Set<AmpTeam> w = new TreeSet<AmpTeam>();
        List<AmpTeam> teams = new ArrayList<AmpTeam>();
        try {
            teams = org.digijava.module.um.util.DbUtil.getList(AmpTeam.class.getName(),"name");
        } catch (UMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        w.addAll(teams);
        final IModel<Set<AmpTeam>> teamsModel = new Model((Serializable)w);
        
        AmpPMAssignGlobalPermissionComponentPanel assignGlobalPerm = new AmpPMAssignGlobalPermissionComponentPanel("assignGlobalPermission", globalPermissionsModel, teamsModel, "Assign Global Permission");
        assignGlobalPerm.setOutputMarkupId(true);
        add(assignGlobalPerm);
        
        AmpPMViewGlobalPermsTableFeaturePanel globalPermsTable = new AmpPMViewGlobalPermsTableFeaturePanel("viewGlobalPerms", globalPermissionsModel, "View Existing Global Permissions", true);
        globalPermsTable.setOutputMarkupId(true);
        add(globalPermsTable);
        AjaxPagingNavigator pager = new AjaxPagingNavigator("globalPermsNavigator", (PageableListView)globalPermsTable.getList()) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(AmpPMManageGlobalPanel.this);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpPMManageGlobalPanel.this));
            }
        };
        add(pager);
        
    }

}
