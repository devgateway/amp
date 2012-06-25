/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.permissionmanager.components.features.PermissionManagerFormFeature;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.um.exception.UMException;

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
