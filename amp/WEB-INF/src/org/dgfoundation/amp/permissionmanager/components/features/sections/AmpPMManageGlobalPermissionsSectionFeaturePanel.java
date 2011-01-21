/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;

/**
 * @author dan
 *
 */
public class AmpPMManageGlobalPermissionsSectionFeaturePanel extends
		AmpPMSectionFeaturePanel {

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
	public AmpPMManageGlobalPermissionsSectionFeaturePanel(String id,
			IModel model, String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		
		
		List<ITab> globalPermissionsTabs = new ArrayList<ITab>();
//		AmpPMGlobalPanel a = new AmpPMGlobalPanel("asd", "asd");
//		AmpPMGlobalPanel b = new AmpPMGlobalPanel("123", "123");
		globalPermissionsTabs.add(new AmpPMTab("OV Indicators" , "Objective Objectively Verifiable Indicators", AmpPMGlobalPanel.class));
		globalPermissionsTabs.add(new AmpPMTab("Assumption" , "Objective Assumption", AmpPMGlobalPanel.class));
//		globalPermissionsTabs.add(new AmpPMTab("Verification" , "Objective Verification", AmpPMGlobalPanel.class));
		
		AmpPMTabsFieldWrapper objTabs = new AmpPMTabsFieldWrapper("objectiveTabs", "Objective Comments", globalPermissionsTabs);
		add(objTabs);
		
	}

}
