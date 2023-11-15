/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.core.Permission;

import java.util.Set;

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
    public AmpPMAddGlobalPermissionPanel(String id, final IModel<Set<Permission>> globalPermissionsModel, IModel<Set<AmpTeam>> teamsModel, String fmName)
            throws Exception {
        super(id, globalPermissionsModel, fmName, AmpFMTypes.MODULE);
        super.setOutputMarkupId(true);

        AmpPMAssignGlobalPermissionComponentPanel assignGlobalPerm = new AmpPMAssignGlobalPermissionComponentPanel(
                "assignGlobalPermission", globalPermissionsModel, teamsModel, 
                "Assign Global Permission");
        assignGlobalPerm.setOutputMarkupId(true);
        add(assignGlobalPerm);

    }

}
