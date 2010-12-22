/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMManageUsersSectionFeature;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMSectionFeaturePanel;

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
		add(new AmpPMManageUsersSectionFeature("manageUsers", "Manage Users"));
		add(new AmpPMSectionFeaturePanel("manageWorkspaces", "Manage Workspaces"));
		add(new AmpPMSectionFeaturePanel("manageGlobalPermissions", "Manage Global Permissions"));
		add(new AmpPMSectionFeaturePanel("manageFieldLevelPermissions", "Manage Field Level Permissions"));
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
