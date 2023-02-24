/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;
import org.hibernate.Session;

/**
 * @author mihai
 *
 */
public class UserLevelGate extends Gate {

    public static final String PARAM_EVERYONE = "everyone";
    public static final String PARAM_GUEST = "guest";
    public static final String PARAM_OWNER = "owner";
    public static final String PARAM_EVERYONE_LOGGED_IN = "everyone_logged_in";
    public static final String PARAM_WORKSPACE_MANAGER = "worskpacemanager";

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO = new MetaInfo[] { new MetaInfo("Level",
            "The name of the user level. Eg: everyone(public user), guest(user logged in but no rights)") };

    private static final String DESCRIPTION = "Returns access based on the rights level of the current user";

    /**
     * @param scope
     * @param parameters
     */
    public UserLevelGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public UserLevelGate() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    @Override
    public String description() {
        // TODO Auto-generated method stub
        return DESCRIPTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        Session session = PersistenceManager.getSession();
        String param = parameters.poll().trim();

        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);

        AmpActivityVersion act = (AmpActivityVersion) scope.get(GatePermConst.ScopeKeys.ACTIVITY);

        // AMP-9768 - apply permissions to teamHead for other workspaces than
        // his own
        if (tm != null && tm.getTeamHead() && act != null && act.getTeam() != null
                && act.getTeam().getAmpTeamId().equals(tm.getTeamId())) {
            return true;
        }

        Permissible permissible = (Permissible) scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
        if (act == null && permissible instanceof AmpActivityVersion) {
            act = (AmpActivityVersion) scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
        }
        boolean owner = false;
        logger.debug("Object is:" + permissible.toString());
        if (act != null && act.getActivityCreator() == null) {
            logger.warn("Activity without owner ... ID: " + act.getAmpActivityId());
            owner = false;
        } else {

            if (tm != null && act != null && act.getActivityCreator().getAmpTeamMemId().equals(tm.getMemberId())) {
                owner = true;
            }
        }
        // if im the owner and this gate checks for ownership....
        if (owner && PARAM_OWNER.equals(param)) {
            return true;
        }

        // if im not even a team member
        if (tm == null) {
            return PARAM_EVERYONE.equals(param);
        } else {
            // if the user is a team member and its logged in we check for param
            // logged in
            if (PARAM_EVERYONE_LOGGED_IN.equals(param)) {
                return true;
            }
        }

        // if i am a guest and not the owner of the current object i will have
        // guest access
        // if(!owner && PARAM_GUEST.equals(param)) return true;

        Gate relatedOrgGate = Gate.instantiateGate(scope, null, RelatedOrgGate.class.getName());
        Gate computedActivityGate = Gate.instantiateGate(scope, null, ComputedTeamActivityGate.class.getName());
        // if i am a guest and not the owner AND not related to the current
        // object i will have guest access
        if (!owner && !relatedOrgGate.logic() && PARAM_GUEST.equals(param)) {
            return true;
        }

        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
     */
    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        // TODO Auto-generated method stub
        return SCOPE_KEYS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
     */
    @Override
    public MetaInfo[] parameterInfo() {
        // TODO Auto-generated method stub
        return PARAM_INFO;
    }

}
