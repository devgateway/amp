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
		final IModel<PermissionMap> globalPermissionMapForPermissibleClass=null;
		final IModel<Permission> globalPermission=null;
		
		globalPermissionsTabs.add(new AbstractTab(new Model("Manage Permissions")){
		      public Panel getPanel(String panelId)
		      {
		        return new AmpPMManageGlobalPanel(panelId, permissionsModel, globalPermissionMapForPermissibleClass, globalPermission, "Manage Permissions", true);
		      }
		});
		
		globalPermissionsTabs.add(new AbstractTab(new Model("Add New Permission")){
		      public Panel getPanel(String panelId)
		      {
		        return new AmpPMManageGlobalPanel(panelId, permissionsModel, globalPermissionMapForPermissibleClass, globalPermission,"Add New Permission", true);
		      }
		});

		
//		globalPermissionsTabs.add(new AmpPMTab("OV Indicators" , "Objective Objectively Verifiable Indicators", AmpPMGlobalPanel.class));
//		globalPermissionsTabs.add(new AmpPMTab("Assumption" , "Objective Assumption", AmpPMGlobalPanel.class));
//		globalPermissionsTabs.add(new AmpPMTab("Verification" , "Objective Verification", AmpPMGlobalPanel.class));
		
		AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("globalPermsTabs", "Global Permissions", globalPermissionsTabs,true);
		add(objTabs);
		
	}

}
