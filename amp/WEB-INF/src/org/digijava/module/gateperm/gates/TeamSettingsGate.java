/**
 * 
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.hibernate.Session;

import java.util.Map;
import java.util.Queue;

/**
 * @author mihai
 *
 */
public class TeamSettingsGate extends Gate {

public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER  };
    
    public static final MetaInfo[] PARAM_INFO  = null;

     private static final String  DESCRIPTION = "activity add checkbox overrides user level permissions in computed teams;" +
            " for management teams this button is never displayed;" +
            " for any other situations the button is always dispayed";
    /**
     * @param scope
     * @param parameters
     */
    public TeamSettingsGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public TeamSettingsGate() {
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
        if( ampTeam.getComputation()!=null && ampTeam.getComputation() && 
                (ampTeam.getAddActivity()==false || ampTeam.getAddActivity()==null) ) return false;
        
        if(Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(ampTeam.getAccessType())) return false;
            
        
        return true;
        
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
        // TODO Auto-generated method stub
        return null;//PARAM_INFO;
    }
    
    
}
