/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.hibernate.Session;

import java.util.Map;
import java.util.Queue;

/**
 * Gives access if the currenct workspace of the current user has the same id as the given parameter
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class WorkspaceIdGate extends Gate {

public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER  };
    
    public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] { new MetaInfo("WorkspaceId","the id of the workspace that has access") };

     private static final String  DESCRIPTION = "gives access if the current workspace of the current user has the same id as the given parameter";
    /**
     * @param scope
     * @param parameters
     */
    public WorkspaceIdGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public WorkspaceIdGate() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#description()
     *///
    @Override
    public String description() {
        // TODO Auto-generated method stub
        return DESCRIPTION;
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        Session session = PersistenceManager.getSession();
        
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        
        if (tm == null)
            return false;
        
        if (tm.getTeamId() == null)
            return false;
        
        //we get the team
        AmpTeam ampTeam = TeamUtil.getAmpTeam(tm.getTeamId());
        
        //we check if this team, which is the current team of the current workspace, has the same id as the input parameter
        int workspaceId = Integer.parseInt(parameters.poll().trim());
        if(ampTeam.getAmpTeamId()==workspaceId) return true;
        
        return false;
        
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
     */
    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        // TODO Auto-generated method stub
        return SCOPE_KEYS;
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
     */
    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }
    
    
}
