package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.util.Collection;

public class UserBean {
  private Long id;
  private String firstNames;
  private String lastName;
  private String email;
  @Deprecated private Collection<AmpTeam> teams;
  private Collection<AmpTeamMember> teamMembers;

  private boolean ban;
  
public UserBean() {
  }

  public String getEmail() {
    return email;
  }

  public String getFirstNames() {
    return firstNames;
  }

  public Long getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

@Deprecated  public Collection getTeams() {
    return teams;
  }

  public boolean isBan() {
    return ban;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFirstNames(String firstNames) {
    this.firstNames = firstNames;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

 @Deprecated public void setTeams(Collection teams) {
    this.teams = teams;
  }

  public void setBan(boolean ban) {
    this.ban = ban;
  }
  
  public Collection<AmpTeamMember> getTeamMembers() {
        return teamMembers;
    }

  public void setTeamMembers(Collection<AmpTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
