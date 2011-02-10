/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;

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
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMManageFieldPermissionsSectionFeaturePanel(String id,final IModel<AmpTreeVisibilityModelBean>  ampTreeVisibilityModel, String fmName, boolean hideLabel) throws Exception {
		super(id, ampTreeVisibilityModel, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		List<ITab> fieldPermissionsTabs = new ArrayList<ITab>();

		fieldPermissionsTabs.add(new AbstractTab(new Model("Add Field Permission")){
		      public Panel getPanel(String panelId)
		      {
		    	  AmpPMAddFieldPermissionPanel newGlobalPerm = null;
		    	try {
					newGlobalPerm = new AmpPMAddFieldPermissionPanel(panelId, ampTreeVisibilityModel, "Add Field Permission");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return newGlobalPerm;
		      }
		});
		
		AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("fieldPermsTabs", "Field Permissions", fieldPermissionsTabs,true);
		add(objTabs); 
	}

}
