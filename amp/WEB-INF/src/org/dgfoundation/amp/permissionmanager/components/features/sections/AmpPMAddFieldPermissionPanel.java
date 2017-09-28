/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMAddFieldPermissionPanel extends AmpComponentPanel {

    public AmpPMAddFieldPermissionPanel(String id, IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityBeanModel, String fmName, IModel<Set<AmpTeam>> teamsModel, IModel<AmpTreeVisibility> ampTreeVisibilityModel) {
        super(id, ampTreeVisibilityBeanModel, fmName, AmpFMTypes.MODULE);
        super.setOutputMarkupId(true);

        AmpPMAssignFieldPermissionComponentPanel assignFieldPerm = new AmpPMAssignFieldPermissionComponentPanel("assignFieldPermission", ampTreeVisibilityBeanModel,"Assign Field Permission", teamsModel, ampTreeVisibilityModel);
        assignFieldPerm.setOutputMarkupId(true);
        add(assignFieldPerm);
        
    }

    public AmpPMAddFieldPermissionPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
    }

    public AmpPMAddFieldPermissionPanel(String id, String fmName, AmpFMTypes fmType) {
        super(id, fmName, fmType);
    }

    public AmpPMAddFieldPermissionPanel(String id, String fmName) {
        super(id, fmName);
    }

}
