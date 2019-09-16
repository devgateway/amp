package org.digijava.kernel.persistence;

import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.springframework.util.Assert;

public class InMemoryTeamManager {
    
    private final Map<String, AmpTeam> teams = new HashMap<>();
    
    public final static String TEST_TEAM_NAME = "Test Team";
    
    public void addTeam(AmpTeam team) {
        Assert.isTrue(!teamExists(team.getName()));
        teams.put(team.getName().toLowerCase(), team);
    }
    
    public AmpTeam getTeam(String name) {
        return teams.get(name.toLowerCase());
    }
    
    public boolean teamExists(String teamname) {
        return teams.containsKey(teamname.toLowerCase());
    }
    
    public void init() {
        AmpTeam team = new AmpTeam();
        team.setAmpTeamId(1L);
        team.setName(TEST_TEAM_NAME);
        addTeam(team);
    }
}
