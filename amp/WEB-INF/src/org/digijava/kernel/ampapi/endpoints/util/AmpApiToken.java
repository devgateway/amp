package org.digijava.kernel.ampapi.endpoints.util;

import java.io.Serializable;
import java.util.Objects;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.joda.time.DateTime;

public class AmpApiToken implements Serializable{

    private static final long serialVersionUID = 8372878787005618060L;

    private User user;
    private TeamMember teamMember;
    private DateTime expirationTime;
    private String token;

    public AmpApiToken(String token, User user, TeamMember teamMember, DateTime expirationTime) {
        this.token = Objects.requireNonNull(token);
        this.user = Objects.requireNonNull(user);
        this.teamMember = teamMember;
        this.expirationTime = Objects.requireNonNull(expirationTime);
    }

    public User getUser() {
        return user;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return expirationTime.isBeforeNow();
    }
}
