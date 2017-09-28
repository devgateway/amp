/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * This gate returns true if the {@link GatePermConst.ScopeKeys#CURRENT_MEMBER}
 * belongs to the workspace with the Id given in the
 * {@link Gate#getParameters()}
 * 
 * @author dan@developmentgateway.org
 * @since Jul 24, 2012
 */
public class StrategyPermSelectGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO = new MetaInfo[] { new MetaInfo("StrategyWorkspaceType","The type of the strategy that is assigned to each workspace regarding how and what permissions should be applied") };

    private static final String DESCRIPTION = "This gate returns true if the CURRENT_MEMBER belongs to an workspace with the permissionStrategy eq with the value given as parameter";

    /**
     * @param scope
     * @param parameters
     */
    public StrategyPermSelectGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public StrategyPermSelectGate() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        
        if (tm == null)
            return false;
        
        if (tm.getTeamId() == null)
            return false;
        
        String permStrategy = parameters.poll().trim();
        AmpTeam ampTeam = TeamUtil.getAmpTeam(tm.getTeamId());
        if (ampTeam.getPermissionStrategy()!=null && ampTeam.getPermissionStrategy().compareTo(permStrategy) == 0)
            return true;
        
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
     */
    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
     */
    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    @Override
    public String description() {
        return DESCRIPTION;
    }

}
