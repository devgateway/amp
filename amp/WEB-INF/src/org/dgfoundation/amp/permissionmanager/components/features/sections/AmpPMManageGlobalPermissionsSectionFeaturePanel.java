/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

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
		
		System.out.println("aaaaaaaaaaaaaaaaaaaaa");
	}

}
