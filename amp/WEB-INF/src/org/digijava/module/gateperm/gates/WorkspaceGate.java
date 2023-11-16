/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.Map;
import java.util.Queue;

/**
 * This gate returns true if the {@link GatePermConst.ScopeKeys#CURRENT_MEMBER}
 * belongs to the workspace with the Id given in the
 * {@link Gate#getParameters()}
 * 
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public class WorkspaceGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO = new MetaInfo[] { new MetaInfo("WorkspaceId","The ID of the workspace to check the user membership") };

    private static final String DESCRIPTION = "This gate returns true if the CURRENT_MEMBER belongs to the workspace with the Id given as parameter";

    /**
     * @param scope
     * @param parameters
     */
    public WorkspaceGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public WorkspaceGate() {
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
        Long workspaceId = Long.parseLong(parameters.poll().trim());
        
        if (tm == null)
            return false; // no user logged in -> NO
        
        if (tm.getTeamId() == null)
            return false; // user.noTeamId -> NO
        
        if (tm.getTeamId().compareTo(workspaceId) == 0)
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
