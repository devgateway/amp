package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.dbentity.AmpTeamMember;

public interface TeamMemberService {
    
    AmpTeamMember getAmpTeamMember(Long id);
}
