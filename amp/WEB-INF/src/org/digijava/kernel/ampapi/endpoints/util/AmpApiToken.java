package org.digijava.kernel.ampapi.endpoints.util;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

public class AmpApiToken implements Serializable {

    // Amount of time in minutes until the token expires
    private static final int TIMEOUT = 30;

    private User user;
    private TeamMember teamMember;
    private DateTime expirationTime;
    private String token;

    public AmpApiToken(String token, User user, TeamMember teamMember) {
        this.token = Objects.requireNonNull(token);
        this.user = Objects.requireNonNull(user);
        this.teamMember = teamMember;
        touch();
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

    public void touch() {
        expirationTime = new DateTime().plusMinutes(TIMEOUT);
    }
}
