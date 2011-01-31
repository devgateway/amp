/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.Permission;

/**
 * @author dan
 *
 */
public class AmpPMAddGlobalPermissionPanel extends AmpComponentPanel {

	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMAddGlobalPermissionPanel(String id, String fmName,
			AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMAddGlobalPermissionPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMAddGlobalPermissionPanel(String id, IModel model,
			String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception 
	 */
	public AmpPMAddGlobalPermissionPanel(String id, final IModel<Set<Permission>> globalPermissionsModel, String fmName) throws Exception {
		super(id, globalPermissionsModel, fmName);
		super.setOutputMarkupId(true);
		// TODO Auto-generated constructor stub
		
		AmpPMAssignGlobalPermissionComponentPanel assignGlobalPerm = new AmpPMAssignGlobalPermissionComponentPanel("assignGlobalPermission", globalPermissionsModel,"Assign Global Permission");
		assignGlobalPerm.setOutputMarkupId(true);
		add(assignGlobalPerm);
		
		IModel<CompositePermission> cpModel = new Model(new CompositePermission());
		AmpPMAddPermissionFormFeaturePanel addPermTable = new AmpPMAddPermissionFormFeaturePanel("addPermTable", cpModel, "Add Permission Form");
		addPermTable.setOutputMarkupId(true);
		add(addPermTable);
		
	}

}
