package org.digijava.kernel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.springframework.util.Assert;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryTeamManager implements InMemoryManager<AmpTeam> {
    
    private static InMemoryTeamManager instance;
    
    private final Map<String, AmpTeam> teams = new HashMap<>();
    
    public final static String TEST_TEAM_NAME = "Test Team";
    
    public static InMemoryTeamManager getInstance() {
        if (instance == null) {
            instance = new InMemoryTeamManager();
        }
        
        return instance;
    }
    
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
    
    private InMemoryTeamManager() {
        AmpTeam team = new AmpTeam();
        team.setAmpTeamId(1L);
        team.setName(TEST_TEAM_NAME);
        addTeam(team);
    }
    
    @Override
    public AmpTeam get(Long id) {
        return teams.values().stream()
                .filter(t -> t.getAmpTeamId().equals(id))
                .findFirst().orElse(null);
    }
    
    @Override
    public List<AmpTeam> getAllValues() {
        return new ArrayList<>(teams.values());
    }
}
