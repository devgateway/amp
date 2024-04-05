package org.digijava.kernel.persistence;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryTeamMemberManager implements InMemoryManager<AmpTeamMember> {
    
    private static InMemoryTeamMemberManager instance;
    
    private final Map<Long, AmpTeamMember> teamMembers = new HashMap<>();
    
    public static final Long TEST_TEAM_MEMBER_ID = 1L;
    
    public static InMemoryTeamMemberManager getInstance() {
        if (instance == null) {
            instance = new InMemoryTeamMemberManager();
        }
        
        return instance;
    }
    
    private InMemoryTeamMemberManager() {
        AmpTeamMember atm = new AmpTeamMember();
        atm.setUser(InMemoryUserManager.getInstance().getUser(InMemoryUserManager.TEST_USER_NAME));
        atm.setAmpTeam(InMemoryTeamManager.getInstance().getTeam(InMemoryTeamManager.TEST_TEAM_NAME));
        atm.setAmpTeamMemId(TEST_TEAM_MEMBER_ID);
        addTeamMember(atm);
    }
    
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
    
    @Override
    public AmpTeamMember get(Long id) {
        return teamMembers.get(id);
    }
    
    @Override
    public List getAllValues() {
        return new ArrayList<>(teamMembers.values());
    }
}
