package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;


/**
 * This gate will verify that the current region present in the scope
 * is the same as the verified region assigned to the current user
 * 
 * @author aartimon@developmentgateway.org
 * @since Dec 11, 2012
 */

public class CurrentVerifiedRegionGate extends Gate {
    
    public static final MetaInfo<?>[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER,
                                                                    GatePermConst.ScopeKeys.CURRENT_REGION};

    private static final String DESCRIPTION = "This gate will verify that the current region present in the scope"
                                            + "is the same as the verified region assigned to the current user";

    @Override
    public boolean logic() throws Exception {
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        if (tm==null) 
            return false; 
        User user = PermissionUtil.getUser(scope, tm);

        //check if the scope has a region in it, if it does use that directly
        AmpCategoryValueLocations currentLocation = (AmpCategoryValueLocations) scope.get(GatePermConst.ScopeKeys.CURRENT_REGION);
        AmpCategoryValueLocations parentRegion = null;
        if (currentLocation != null)
            parentRegion = DynLocationManagerUtil.getAncestorByLayer(
                    currentLocation, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
        return currentLocation != null && user.getRegion() != null &&
                (currentLocation.getId().equals(user.getRegion().getId()) || 
                (parentRegion != null && user.getRegion().getId().equals(parentRegion.getId())));
    }

    @Override
    public MetaInfo<?>[] parameterInfo() {
        //no paramters needed in the gate yet
        return null;
    }

    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
