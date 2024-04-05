/**
 * 
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/**
 * Gives right if current user is assigned to an org that is related (by any role) to an object - such an activity
 * @author mihai
 */
public class RelatedOrgGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO  = null;//new MetaInfo[] {};

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
    
    AmpActivityVersion ampa = null;
//  Activity a = null;
    Object o1 = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
    if ( o1 instanceof AmpModulesVisibility )
    {
        if( ((AmpModulesVisibility)o1).getName().contains("Project Title") )
            logger.debug("---------------CompositePermission " + ((AmpModulesVisibility)o1).getName());
        
    }

    Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
    if (o instanceof AmpActivityVersion)
        ampa = (AmpActivityVersion) o;
    Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
    if (oo instanceof AmpActivityVersion)
        ampa = (AmpActivityVersion) oo;
//  if (oo instanceof Activity)
//      a = (Activity) oo;

    TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
    //TODO AMP-2579 this IF was added to fix null pointer temporary.
    if (tm==null) return false; 
    //AmpTeamMember atm = (AmpTeamMember) session.get(AmpTeamMember.class, tm.getMemberId());
    //AmpTeamMember atm=TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
    
    User user = PermissionUtil.getUser(scope, tm);

    
    
    
    // iterate the assigned orgs:
    if (ampa != null) {
        if (ampa.getOrgrole() == null)
        return false;
        Iterator i = ampa.getOrgrole().iterator();
        while (i.hasNext()) {
        AmpOrgRole element = (AmpOrgRole) i.next();
        String roleCode = element.getRole().getRoleCode();
        if (user.hasVerifiedOrganizationId(element.getOrganisation().getAmpOrgId()))
            return true;
        }
        

    }
//  if (a != null) {
//      if (a.getRelOrgs() == null)
//      return false;
//      Iterator i = a.getRelOrgs().iterator();
//      while (i.hasNext()) {
//      RelOrganization element = (RelOrganization) i.next();
//      String roleCode = element.getRole();
//      if (element.getOrgId().equals(user.getAssignedOrgId()))
//          return true;
//
//      }
//  }
    

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
