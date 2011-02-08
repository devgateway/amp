/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

/**
 * @author dan
 *
 */
public class AmpPMAssignFieldPermissionComponentPanel extends AmpComponentPanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, IModel<AmpTreeVisibility> ampTreeVisibilityModel, String fmName) {
		super(id, ampTreeVisibilityModel, fmName);
		// TODO Auto-generated constructor stub
		
		
		
		
	}

	
	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName,AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}


}
