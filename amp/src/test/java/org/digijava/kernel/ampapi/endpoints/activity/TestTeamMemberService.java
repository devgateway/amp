package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.persistence.InMemoryTeamMemberManager;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class TestTeamMemberService implements TeamMemberService {
    
    InMemoryTeamMemberManager teamMemberManager;
    
    public TestTeamMemberService(InMemoryTeamMemberManager teamMemberManager) {
        this.teamMemberManager = teamMemberManager;
    }
    
    @Override
    public AmpTeamMember getAmpTeamMember(Long id) {
        return teamMemberManager.getTeamMember(id);
    }
    
}
