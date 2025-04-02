/**
 *
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.EXEMPT_ORGANIZATION_DOCUMENTS;

/**
 * Gives right if current user is assigned to an org that is related (by any role) to an object - such an activity
 *
 * @author mihai
 */
public class RelatedOrgExemptGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[]{GatePermConst.ScopeKeys.CURRENT_MEMBER};

    public static final MetaInfo[] PARAM_INFO = null;

    private static final String DESCRIPTION = "Gives right if current user is assigned to an org that is "
            + "configured in Exempt organization to see documents global setting";

    public RelatedOrgExemptGate() {

    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public boolean logic() throws Exception {

        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
        if (tm == null) {
            return false;
        }

        User user = PermissionUtil.getUser(scope, tm);


        return user.hasVerifiedOrganizationId(FeaturesUtil.getGlobalSettingValueLong(EXEMPT_ORGANIZATION_DOCUMENTS));
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
