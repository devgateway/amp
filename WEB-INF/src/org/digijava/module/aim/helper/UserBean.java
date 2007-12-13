package org.digijava.module.aim.helper;

import java.util.*;

import org.digijava.module.aim.dbentity.*;

public class UserBean {
  private Long id;
  private String firstNames;
  private String lastName;
  private String email;
  private Collection<AmpTeam> teams;
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

  public Collection getTeams() {
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

  public void setTeams(Collection teams) {
    this.teams = teams;
  }

  public void setBan(boolean ban) {
    this.ban = ban;
  }

}
