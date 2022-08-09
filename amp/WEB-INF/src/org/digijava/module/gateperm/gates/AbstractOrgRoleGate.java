package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.request.cycle.RequestCycle;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

public abstract class AbstractOrgRoleGate extends Gate {
    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };
    public static final MetaInfo[] PARAM_INFO = new MetaInfo[] { new MetaInfo("Role",
            "The name of the role. values: 'EA' for EXECUTING, 'IA' for IMPLEMENTING,etc...See amp_role table") };

    private AmpActivityVersion ampa;
    private User user;
    private String paramRoleCode;

    public AbstractOrgRoleGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
    }

    public AbstractOrgRoleGate() {
        super();
    }

    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }

    protected AmpActivityVersion getAmpa() {
        return ampa;
    }

    protected User getUser() {
        return user;
    }

    protected String getParamRoleCode() {
        return paramRoleCode;
    }

    protected void populateValues() {
        Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
        if (o instanceof AmpActivityVersion) {
            ampa = (AmpActivityVersion) o;
        }
        Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
        if (oo instanceof AmpActivityVersion) {
            ampa = (AmpActivityVersion) oo;
        }

        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        if (tm == null) {
            throw new RuntimeException("Team member not found in scope");
        }
        user = PermissionUtil.getUser(scope, tm);

        paramRoleCode = parameters.poll().trim();
    }

    protected Boolean checkIfDonorIsVerifiedOrganisation(boolean shouldCheckInRequest) {
        Boolean canDo = null;
        FundingOrganization org = (FundingOrganization) getObjectFromScope(GatePermConst.ScopeKeys.CURRENT_ORG,
                shouldCheckInRequest);
        if (org != null && "DN".equals(paramRoleCode)) {

            String roleCode = (String) getObjectFromScope(GatePermConst.ScopeKeys.CURRENT_ORG_ROLE,
                    shouldCheckInRequest);
            if (roleCode == null) {
                throw new RuntimeException("CURRENT_ORG specified in scope without CURRENT_ORG_ROLE!");
            }
            if (roleCode.equals(paramRoleCode) && user.hasVerifiedOrganizationId(org.getAmpOrgId())) {
                canDo = true;
            } else {
                canDo = false;
            }
        }
        return canDo;
    }

    protected Object getObjectFromScope(MetaInfo currentOrg, boolean shouldCheckInRequest) {
        Object objFromScope = scope.get(currentOrg);
        if (objFromScope != null) {
            return objFromScope;
        } else {
            if (RequestCycle.get() != null && shouldCheckInRequest) {
                HttpServletRequest request = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
                objFromScope = PermissionUtil.getScope(request).get(currentOrg);
            }
            return objFromScope;
        }
    }

    protected boolean checkVerifiedOrganisations() {
        if (ampa != null) {
            if (ampa.getOrgrole() == null) {
                return false;
            }
            for (AmpOrgRole element : ampa.getOrgrole()) {
                String roleCode = element.getRole().getRoleCode();
                if (roleCode.equals(paramRoleCode)
                        && user.hasVerifiedOrganizationId(element.getOrganisation().getAmpOrgId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
