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
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMAddGlobalPermissionPanel(String id,
			final IModel<Set<Permission>> globalPermissionsModel, String fmName)
			throws Exception {
		super(id, globalPermissionsModel, fmName, AmpFMTypes.MODULE);
		super.setOutputMarkupId(true);

		AmpPMAssignGlobalPermissionComponentPanel assignGlobalPerm = new AmpPMAssignGlobalPermissionComponentPanel(
				"assignGlobalPermission", globalPermissionsModel,
				"Assign Global Permission");
		assignGlobalPerm.setOutputMarkupId(true);
		add(assignGlobalPerm);

		// IModel<CompositePermission> cpModel = new Model(new
		// CompositePermission());
		// AmpPMAddPermissionFormFeaturePanel addPermTable = new
		// AmpPMAddPermissionFormFeaturePanel("addPermTable", cpModel,
		// "Add Permission Form", "Add New Permission", false);
		// addPermTable.setOutputMarkupId(true);
		// add(addPermTable);

	}

}
