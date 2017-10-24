package org.dgfoundation.amp.permissionmanager.components.features;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageFieldPermissionsSectionFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageGlobalPermissionsSectionFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageUsersSectionFeature;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageWorkspacesSectionFeature;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.um.exception.UMException;

public class PermissionManagerFormFeature  extends AmpFeaturePanel{

    public PermissionManagerFormFeature(String id, IModel<Set<AmpTeam>> teamsModel,IModel<Set<AmpPMFieldPermissionViewer>> permsModel, String fmName) throws Exception {
        super(id, teamsModel, fmName, true);
        
        Form adminPMForm = new Form("adminPMForm");
        
        //managing users
        Set<User> s = new TreeSet<User>();
        List<User> users = new ArrayList<User>();
        try {
            users = org.digijava.module.um.util.DbUtil.getList(User.class.getName(),"firstNames");
        } catch (UMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        s.addAll(users);
        final IModel<Set<User>> usersModel = new Model((Serializable)s);
        AmpPMManageUsersSectionFeature ampPMManageUsersSectionFeature = new AmpPMManageUsersSectionFeature("manageUsers", "Manage Users", usersModel);
        ampPMManageUsersSectionFeature.setVisibilityAllowed(true);
        ampPMManageUsersSectionFeature.setVisible(true);
        ampPMManageUsersSectionFeature.setOutputMarkupId(true);
        adminPMForm.add(ampPMManageUsersSectionFeature);
        
        AmpPMManageWorkspacesSectionFeature workspaceSection = new AmpPMManageWorkspacesSectionFeature("manageWorkspaces", teamsModel, "Manage Workspaces", false);
        adminPMForm.add(workspaceSection);
        
        Set<Permission> permissonsSet = new TreeSet<Permission>();
        final IModel<Set<Permission>> globalPermissionsModel = new Model((Serializable)permissonsSet);
        adminPMForm.add(new AmpPMManageGlobalPermissionsSectionFeaturePanel("manageGlobalPermissions", globalPermissionsModel, teamsModel, "Manage Global Permissions", false));
        
        AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
        ampTreeVisibility.buildAmpTreeVisibility(FeaturesUtil.getDefaultAmpTemplateVisibility());
        final IModel<AmpTreeVisibility> ampTreeVisibilityModel = new Model(ampTreeVisibility);
        
        AmpTreeVisibilityModelBean tree =   PMUtil.getAmpTreeFMPermissions();
        final IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityBeanModel =   new Model((Serializable)tree);
        adminPMForm.add(new AmpPMManageFieldPermissionsSectionFeaturePanel("manageFieldLevelPermissions", ampTreeVisibilityBeanModel, "Manage Field Permissions",teamsModel, permsModel, false, ampTreeVisibilityModel));
        add(adminPMForm);
    }

}
