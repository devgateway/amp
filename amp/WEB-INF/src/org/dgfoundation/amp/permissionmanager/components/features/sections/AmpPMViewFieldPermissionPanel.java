/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;

import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMViewFieldPermissionPanel extends AmpComponentPanel {

    public AmpPMViewFieldPermissionPanel(String id, IModel<Set<AmpPMFieldPermissionViewer>> permsModel, String fmName) throws Exception {
        super(id, permsModel, fmName, AmpFMTypes.MODULE);
        super.setOutputMarkupId(true);

        AmpPMViewFieldPermissionComponentPanel assignFieldPerm = new AmpPMViewFieldPermissionComponentPanel("viewFieldPermission", permsModel,"View Assign Field Permission");
        assignFieldPerm.setOutputMarkupId(true);
        add(assignFieldPerm);
        
    }

    public AmpPMViewFieldPermissionPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
    }

    public AmpPMViewFieldPermissionPanel(String id, String fmName, AmpFMTypes fmType) {
        super(id, fmName, fmType);
    }

    public AmpPMViewFieldPermissionPanel(String id, String fmName) {
        super(id, fmName);
    }

}
