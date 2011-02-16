/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageFieldPermissionsSectionFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageGlobalPermissionsSectionFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageUsersSectionFeature;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageWorkspacesSectionFeature;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.um.exception.UMException;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class PermissionManager extends AmpPMHeaderFooter {

	/**
	 * 
	 */
	public PermissionManager() throws Exception{
		// TODO Auto-generated constructor stub
		super();
		
		//final IModel<Set<User>> usersModel = new AmpPMUserModel();
		
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
		adminPMForm.add(new AmpPMManageUsersSectionFeature("manageUsers", "Manage Users", usersModel));
		
		
		//managing workspaces
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
		AmpPMManageWorkspacesSectionFeature workspaceSection = new AmpPMManageWorkspacesSectionFeature("manageWorkspaces", teamsModel, "Manage Workspaces", false);
		adminPMForm.add(workspaceSection);
		
		
		//Set<Permission> permissonsSet = new TreeSet<Permission>(PermissionUtil.getAllUnDedicatedPermissions());
		Set<Permission> permissonsSet = new TreeSet<Permission>();
		
		final IModel<Set<Permission>> globalPermissionsModel = new Model((Serializable)permissonsSet);
		adminPMForm.add(new AmpPMManageGlobalPermissionsSectionFeaturePanel("manageGlobalPermissions", globalPermissionsModel, "Manage Global Permissions", false));
		
		
		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		// get the default amp template!!!
		Session session = PersistenceManager.getRequestDBSession();
		AmpTemplatesVisibility currentTemplate = null;
		currentTemplate = FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),session);
		AmpTreeVisibilityModelBean tree	=	PMUtil.buildAmpTreeFMPermissions(currentTemplate);
		final IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityModel =	new Model((Serializable)tree);
		adminPMForm.add(new AmpPMManageFieldPermissionsSectionFeaturePanel("manageFieldLevelPermissions", ampTreeVisibilityModel, "Manage Field Permissions",teamsModel, false));
		
		
		add(adminPMForm);
	}



	/**
	 * @param model
	 */
	public PermissionManager(IModel<?> model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 */
	public PermissionManager(IPageMap pageMap) {
		super(pageMap);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 */
	public PermissionManager(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 * @param model
	 */
	public PermissionManager(IPageMap pageMap, IModel<?> model) {
		super(pageMap, model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 * @param parameters
	 */
	public PermissionManager(IPageMap pageMap, PageParameters parameters) {
		super(pageMap, parameters);
		// TODO Auto-generated constructor stub
	}

}
