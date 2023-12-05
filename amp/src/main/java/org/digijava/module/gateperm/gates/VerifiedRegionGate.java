package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;


/**
 * This gate checks if the current user has a verified region assigned
 * 
 * @author aartimon@developmentgateway.org
 * @since Dec 11, 2012
 */

public class VerifiedRegionGate extends Gate {
    
    public static final MetaInfo<?>[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER,
                                                                    GatePermConst.ScopeKeys.CURRENT_REGION};

    private static final String    DESCRIPTION = "This gate checks if the current user has a verified region assigned";

    @Override
    public boolean logic() throws Exception {
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        if (tm==null) 
            return false; 
        User user = PermissionUtil.getUser(scope, tm);

        //check if the scope has a region in it, if it does use that directly
        if (user.getRegion() != null)
            return true;
        return false;
    }

    @Override
    public MetaInfo<?>[] parameterInfo() {
        //no parameters needed in the gate yet
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
