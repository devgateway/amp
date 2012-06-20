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
import org.dgfoundation.amp.permissionmanager.components.features.PermissionManagerFormFeature;
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
	
		add(new PermissionManagerFormFeature("permission", teamsModel, "Permission Manager"));
	}

}
