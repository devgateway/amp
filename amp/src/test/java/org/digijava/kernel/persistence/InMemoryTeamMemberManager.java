package org.digijava.kernel.persistence;

import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.springframework.util.Assert;

public class InMemoryTeamMemberManager {
    
    private final Map<Long, AmpTeamMember> teamMembers = new HashMap<>();
    
    public static final Long TEST_TEAM_MEMBER_ID = 1L;
    
    public void addTeamMember(AmpTeamMember teamMember) {
        Assert.isTrue(!teamExists(teamMember.getAmpTeamMemId()));
        teamMembers.put(teamMember.getAmpTeamMemId(), teamMember);
    }
    
    public AmpTeamMember getTeamMember(Long id) {
        return teamMembers.get(id);
    }
    
    public boolean teamExists(Long id) {
        return teamMembers.containsKey(id);
    }
    
    public void init(InMemoryUserManager userManager, InMemoryTeamManager teamManager) {
        AmpTeamMember atm = new AmpTeamMember();
        atm.setUser(userManager.getUser(InMemoryUserManager.TEST_USER_NAME));
        atm.setAmpTeam(teamManager.getTeam(InMemoryTeamManager.TEST_TEAM_NAME));
        atm.setAmpTeamMemId(TEST_TEAM_MEMBER_ID);
        addTeamMember(atm);
    }
}
