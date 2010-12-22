/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageUsersTableFeaturePanel;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersSectionFeature extends AmpPMSectionFeaturePanel {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, String fmName)
			throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
		
		add(new AmpPMManageUsersTableFeaturePanel("users", null, "Users"));
		//$users
		
		
		//$searchUsers
		add(new Label("searchUsers"," seaach"));
		
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, IModel model, String fmName)
			throws Exception {
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
	public AmpPMManageUsersSectionFeature(String id, IModel model,
			String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

}
