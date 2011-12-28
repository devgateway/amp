/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import org.hibernate.Session;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * Gives right if current user is assigned to an org that is related (by any role) to an object - such an activity
 * @author mihai
 */
public class RelatedOrgGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] {};

    private static final String  DESCRIPTION = "Gives right if current user is assigned to an org that is related (by any role) to an object - such an activity";

    /**
         * @param scope
         * @param parameters
         */
    public RelatedOrgGate(Map scope, Queue<String> parameters) {
	super(scope, parameters);
	// TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public RelatedOrgGate() {
	// TODO Auto-generated constructor stub
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

    /*
         * (non-Javadoc)
         * 
         * @see org.digijava.module.gateperm.core.Gate#logic()
         */
    @Override
    public boolean logic() throws Exception {
	
	AmpActivity ampa = null;
//	Activity a = null;

	Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
	if (o instanceof AmpActivity)
	    ampa = (AmpActivity) o;
	Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
	if (oo instanceof AmpActivity)
	    ampa = (AmpActivity) oo;
//	if (oo instanceof Activity)
//	    a = (Activity) oo;

	TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
	//TODO AMP-2579 this IF was added to fix null pointer temporary.
	if (tm==null) return false; 
	//AmpTeamMember atm = (AmpTeamMember) session.get(AmpTeamMember.class, tm.getMemberId());
	AmpTeamMember atm=TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
	
	User user = atm.getUser();

	
	
	
	// iterate the assigned orgs:
	if (ampa != null) {	    
	    if (ampa.getOrgrole() == null)
		return false;
	    Iterator i = ampa.getOrgrole().iterator();
	    while (i.hasNext()) {
		AmpOrgRole element = (AmpOrgRole) i.next();
		String roleCode = element.getRole().getRoleCode();
		if (element.getOrganisation().getAmpOrgId().equals(user.getAssignedOrgId()))
		    return true;
	    }
	    

	}
//	if (a != null) {
//	    if (a.getRelOrgs() == null)
//		return false;
//	    Iterator i = a.getRelOrgs().iterator();
//	    while (i.hasNext()) {
//		RelOrganization element = (RelOrganization) i.next();
//		String roleCode = element.getRole();
//		if (element.getOrgId().equals(user.getAssignedOrgId()))
//		    return true;
//
//	    }
//	}
	

	return false;
    }

    @Override
    public MetaInfo[] mandatoryScopeKeys() {
	return SCOPE_KEYS;
    }

    @Override
    public MetaInfo[] parameterInfo() {
	return PARAM_INFO;
    }

}
