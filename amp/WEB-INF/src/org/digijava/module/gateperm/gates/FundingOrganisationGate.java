package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.Set;

/**
 * Gives access to users who are members of the given organization
 */
public class FundingOrganisationGate extends AbstractOrgRoleGate {
    public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER  };

    public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] { new MetaInfo("organizationId","the id of the organization that has access") };

    private static final String  DESCRIPTION = "gives access if the current user is part of the organization with the given id";

    @Override
    public boolean logic() throws Exception {
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);


        if (tm == null)
            return false;

        Long id = tm.getMemberId();
        AmpTeamMember ampTeamMember = TeamMemberUtil.getAmpTeamMember(id);
        logger.info("Member" + ampTeamMember);

        if (ampTeamMember == null)
            return false;
        User user = ampTeamMember.getUser();
        logger.info("User" + user);
        if (user == null)
            return false;
        Set<AmpOrganisation>  orgs = user.getAssignedOrgs();
        logger.info("Assigned Orgs" + orgs);
        if (orgs == null || orgs.isEmpty())
            return false;

        //we get the team
        FundingOrganization org = (FundingOrganization) getObjectFromScope(GatePermConst.ScopeKeys.CURRENT_ORG,
                false);
        logger.info("Funding Organization"+org.getOrgName());
        for (AmpOrganisation ampOrg : orgs) {
            logger.info("Org" + ampOrg.getName());
            if (ampOrg.getAmpOrgId().equals(org.getAmpOrgId()))
            {
                logger.info("Access granted for user " + user.getName() + " to organization " + ampOrg.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }


    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
