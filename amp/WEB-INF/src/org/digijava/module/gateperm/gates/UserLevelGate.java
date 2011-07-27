/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.hibernate.Session;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * @author mihai
 *
 */
public class UserLevelGate extends Gate {

	public static final String PARAM_EVERYONE="everyone";
	public static final String PARAM_GUEST="guest";
	public static final String PARAM_OWNER="owner";
	public static final String PARAM_WORKSPACE_MANAGER="worskpacemanager";
	
	public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER  };
	
	public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] { new MetaInfo("Level",
    "The name of the user level. Eg: everyone(public user), guest(user logged in but no rights)") };

	 private static final String  DESCRIPTION = "Returns access based on the rights level of the current user";
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

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#description()
	 */
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
		String param = parameters.poll().trim();
		
		TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
		
		
		//AMP-9768 - apply permissions to teamHead for other workspaces than his own 
		Object o = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
		AmpActivityVersion act = null; 
		if (o instanceof AmpActivityVersion) {
			act = (AmpActivityVersion) o;
		}
		if(tm!=null && tm.getTeamHead() && act!=null && act.getTeam() != null && act.getTeam().getAmpTeamId().equals(tm.getTeamId())) return true;
		if(act==null){
			o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
			if (o instanceof AmpActivityVersion)
				act = (AmpActivityVersion) o;
		}
		boolean owner=false;
		if (act.getActivityCreator()==null){
			logger.warn("Activity without owner ... ID: "+act.getAmpActivityId());
			owner=false;
		}else{
		
		if( tm!=null && act.getActivityCreator().getAmpTeamMemId().equals(tm.getMemberId()) ){ 
			owner=true;
		  }
		}
		//if im the owner and this gate checks for ownership....
		if(owner && PARAM_OWNER.equals(param)) return true;
		
		//if im not even a team member 
		if(tm==null) 
			if(PARAM_EVERYONE.equals(param)) return true; else return false;
		
		//if i am a guest and not the owner of the current object i will have guest access
		//if(!owner && PARAM_GUEST.equals(param)) return true;
		
		Gate relatedOrgGate = Gate.instantiateGate(scope, null, RelatedOrgGate.class.getName());
		//if i am a guest and not the owner AND not related to the current object i will have guest access
		if(!owner && !relatedOrgGate.logic() && PARAM_GUEST.equals(param) ) return true;
		
		
		
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
		// TODO Auto-generated method stub
		return PARAM_INFO;
	}
	
	
}
