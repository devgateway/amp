/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * Implements logic for organization roles user access filtering. users are
 * assigned to organizations through the um module (verified assigned
 * organization). An user will have access to an activity if he has been
 * assigned to an Organization that has a role in the current activity and if
 * that role corresponds with the parameter of this gate. this gate has only one
 * parameter that states the type of role of the organization (implementing,
 * executing,etc...)
 * 
 * @author mihai
 */
public class OrgRoleGate extends Gate {

	public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

	public static final MetaInfo[] PARAM_INFO = new MetaInfo[] { new MetaInfo("Role",
			"The name of the role. values: 'EA' for EXECUTING, 'IA' for IMPLEMENTING,etc...See amp_role table") };

	private static final String DESCRIPTION = "Implements logic for organization roles user access filtering. Users are assigned "
			+ "to organisations through the um module (verified assigned organisation). "
			+ " An user will have access to an activity if he has been assigned to an organisation that "
			+ " has a role in the current activity and if that role corresponds with the parameter of this "
			+ "gate. This gate has only one parameter that states the code of the role of the "
			+ "organisation. Use as parameter the role CODE associated with the role. Example: EA for Executing Agency...etc. Check the Org Role Manager";

	/**
	 * @param scope
	 * @param parameters
	 */
	public OrgRoleGate(Map scope, Queue<String> parameters) {
		super(scope, parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public OrgRoleGate() {
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
		AmpActivityVersion ampa = null;
		// Activity a = null;

		Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
		if (o instanceof AmpActivityVersion)
			ampa = (AmpActivityVersion) o;
		Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
		if (oo instanceof AmpActivityVersion)
			ampa = (AmpActivityVersion) oo;
		logger.debug("Object is:" + o.toString());
		// if (oo instanceof Activity)
		// a = (Activity) oo;

		TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
		// TODO AMP-2579 this IF was added to fix null pointer temporary.
		if (tm == null)
			return false;

		// AmpTeamMember atm=TeamMemberUtil.getAmpTeamMember(tm.getMemberId());

		User user = TeamMemberUtil.getUserEntityByTMId(tm.getMemberId());// atm.getUser();

		String paramRoleCode = parameters.poll().trim();

		// check if the scope has a funding organisation, if it does use that
		// directly
		FundingOrganization org = (FundingOrganization) scope.get(GatePermConst.ScopeKeys.CURRENT_ORG);
		if (org != null && "DN".equals(paramRoleCode)) {
			String roleCode = (String) scope.get(GatePermConst.ScopeKeys.CURRENT_ORG_ROLE);
			if (roleCode == null)
				throw new RuntimeException("CURRENT_ORG specified in scope without CURRENT_ORG_ROLE!");
			if (roleCode.equals(paramRoleCode) && user.hasVerifiedOrganizationId(org.getAmpOrgId()))
				return true;
			// an org was in the scope, do not continue with the logic and deny
			// access
			return false;
		}
		// iterate the assigned orgs:
		if (ampa != null) {
			if (ampa.getOrgrole() == null)
				return false;
			for (AmpOrgRole element : ampa.getOrgrole()) {
				String roleCode = element.getRole().getRoleCode();
				if (roleCode.equals(paramRoleCode)
						&& user.hasVerifiedOrganizationId(element.getOrganisation().getAmpOrgId()))
					return true;
			}
		}
		// if (a != null) {
		// if (a.getRelOrgs() == null)
		// return false;
		// Iterator i = a.getRelOrgs().iterator();
		// while (i.hasNext()) {
		// RelOrganization element = (RelOrganization) i.next();
		// String roleCode = element.getRole();
		// if (roleCode.equals(paramRoleCode) &&
		// element.getOrgId().equals(user.getAssignedOrgId()))
		// return true;
		//
		// }
		// }
		//

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
