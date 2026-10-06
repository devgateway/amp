package org.digijava.module.aim.helper;

import java.util.List;

public class Team {
    /**
     * team id
     */
    private Long id; 
    /**
     * team name
     */
    private String name;
    /**
     * team members
     */
    private List<TeamMember> members;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<TeamMember> getMembers() {
        return members;
    }
    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }
    
    
    
}
