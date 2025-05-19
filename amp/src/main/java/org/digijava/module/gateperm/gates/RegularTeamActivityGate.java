/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.Map;
import java.util.Queue;

/**
 * This gate allows access on activities belonging to a workspace in which the current user belongs
 * {@link Gate#getParameters()}
 * 
 * @author mpostelnicu@dgateway.org
 * @since 10 apr 2013
 */
public class RegularTeamActivityGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO = new MetaInfo[] {};
    
    private static final String DESCRIPTION = "This gate allows access on activities that have their workspace same as the current member workspace";

    /**
     * @param scope
     * @param parameters
     */
    public RegularTeamActivityGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public RegularTeamActivityGate() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        
        AmpActivityVersion ampa = null;
        Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
        if (o instanceof AmpActivityVersion)
            ampa = (AmpActivityVersion) o;
        
        Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
        if (oo instanceof AmpActivityVersion)
            ampa =  (AmpActivityVersion) oo;        

        
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        
        if (tm == null || tm.getTeamId() == null) {
            /* no teammember or currentTeam is admin -> anonymous access (not logged in) -> gatekeeper says no */
            return false;
        }
        
        AmpTeam currentTeam = TeamUtil.getAmpTeam(tm.getTeamId());
        AmpTeam activityTeam=ampa.getTeam();
        
        //current team same as activity team=>always access
        if (activityTeam != null && currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId()))
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
        return null;
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
