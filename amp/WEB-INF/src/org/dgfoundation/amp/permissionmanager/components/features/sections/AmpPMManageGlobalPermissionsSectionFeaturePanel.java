/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * @author dan
 *
 */
public class AmpPMManageGlobalPermissionsSectionFeaturePanel extends AmpPMSectionFeaturePanel {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageGlobalPermissionsSectionFeaturePanel(String id,
			String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageGlobalPermissionsSectionFeaturePanel(String id,
			IModel model, String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMManageGlobalPermissionsSectionFeaturePanel(final String id, final IModel<Set<Permission>> permissionsModel, String fmName, boolean hideLabel) throws Exception {
		super(id, permissionsModel, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		
		
		List<ITab> globalPermissionsTabs = new ArrayList<ITab>();

		

		
		globalPermissionsTabs.add(new AbstractTab(new Model("Manage Permissions")){
		      public Panel getPanel(String panelId)
		      {
		    	AmpPMManageGlobalPanel mgp = null;
		    	try {
					mgp = new AmpPMManageGlobalPanel(panelId, permissionsModel, "Global Permissions", true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return mgp;
		      }
		});
//		
//		globalPermissionsTabs.add(new AbstractTab(new Model("Add New Permission")){
//		      public Panel getPanel(String panelId)
//		      {
//		        return new AmpPMManageGlobalPanel(panelId, permissionsModel, globalPermissionMapForPermissibleClass, globalPermission,"Project Title", true);
//		      }
//		});
//
		AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("globalPermsTabs", "Global Permissions", globalPermissionsTabs,true);
		add(objTabs);
//		add(new AmpPMManageGlobalPanel("globalPermsTabs", permissionsModel, "Global Permissions", true));
	}

}
