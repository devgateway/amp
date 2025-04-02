package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpGPINiOrgRoleItemFeaturePanel;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;

public class GpiOrgRoleGate extends AbstractOrgRoleGate {

    private static final String DESCRIPTION = "Implements logic for organization roles "
            + " user access filtering. Users are assigned "
            + "to organisations through the User Manager module (verified assigned organisation). "
            + " An user will have access if his verified organisation is the donor of the activity";

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    @Override
    public boolean logic() throws Exception {
        populateValues();
        Boolean canDo = checkIfDonorIsVerifiedOrganisation(true);
        return canDo == null ? false : canDo;
    }

    @Override
    public String description() {
        // TODO Auto-generated method stub
        return DESCRIPTION;
    }

}
