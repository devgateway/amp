package org.digijava.kernel.ampapi.endpoints;

import org.digijava.kernel.ampapi.endpoints.activity.TeamMemberService;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

public class AMPTeamMemberService implements TeamMemberService {
    
    @Override
    public AmpTeamMember getAmpTeamMember(Long id) {
        return TeamMemberUtil.getAmpTeamMember(id);
    }
}
