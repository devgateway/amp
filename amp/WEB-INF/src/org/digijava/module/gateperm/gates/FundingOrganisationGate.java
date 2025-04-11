package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Gives access to users who are members of the given organization
 */
public class FundingOrganisationGate extends AbstractOrgRoleGate {
    public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

    public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] { new MetaInfo("organizationId","the id of the organization that has access") };

    private static final String  DESCRIPTION = "gives access if the current user is part of the organization with the given id";

    public static Boolean enabled= false;

    public FundingOrganisationGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
    }
    public FundingOrganisationGate() {
        enabled=true;
    }
    @Override
    public boolean logic() throws Exception {
        enabled = fundingOrgGateEnabled();
        return enabled;
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
