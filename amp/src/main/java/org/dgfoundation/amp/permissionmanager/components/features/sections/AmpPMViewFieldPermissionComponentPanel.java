/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMViewFieldPermissionTableFeaturePanel;

import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMViewFieldPermissionComponentPanel extends AmpComponentPanel {

    
    /**
     * @param id
     * @param permsModel
     * @param fmName
     * @param teamsModel 
     * @param ampTreeVisibilityModel2 
     * @throws Exception 
     */
    public AmpPMViewFieldPermissionComponentPanel(String id ,final IModel<Set<AmpPMFieldPermissionViewer>> permsModel, String fmName) throws Exception {
        super(id, permsModel, fmName, AmpFMTypes.MODULE);
        // TODO Auto-generated constructor stub
        
        AmpPMViewFieldPermissionTableFeaturePanel permListTable = new AmpPMViewFieldPermissionTableFeaturePanel("viewFieldPermTable", permsModel, "Field Permission Table");
        add(permListTable);
    }

    
    /**
     * @param id
     * @param fmName
     * @param fmType
     */
    public AmpPMViewFieldPermissionComponentPanel(String id, String fmName,AmpFMTypes fmType) {
        super(id, fmName, fmType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpPMViewFieldPermissionComponentPanel(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param fmBehavior
     */
    public AmpPMViewFieldPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
        // TODO Auto-generated constructor stub
    }


}
