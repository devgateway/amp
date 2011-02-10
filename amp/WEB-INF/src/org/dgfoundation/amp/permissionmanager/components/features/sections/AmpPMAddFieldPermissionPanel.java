/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;

/**
 * @author dan
 *
 */
public class AmpPMAddFieldPermissionPanel extends AmpComponentPanel {

	public AmpPMAddFieldPermissionPanel(String id, IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityModel, String fmName) {
		super(id, ampTreeVisibilityModel, fmName);
		// TODO Auto-generated constructor stub
		
		super.setOutputMarkupId(true);
		// TODO Auto-generated constructor stub
		
		AmpPMAssignFieldPermissionComponentPanel assignFieldPerm = new AmpPMAssignFieldPermissionComponentPanel("assignFieldPermission", ampTreeVisibilityModel,"Assign Field Permission");
		assignFieldPerm.setOutputMarkupId(true);
		add(assignFieldPerm);
		
	}

	public AmpPMAddFieldPermissionPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	public AmpPMAddFieldPermissionPanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	public AmpPMAddFieldPermissionPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

}
