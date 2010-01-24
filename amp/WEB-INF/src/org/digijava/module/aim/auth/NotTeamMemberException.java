package org.digijava.module.aim.auth;

import org.acegisecurity.AccessDeniedException;

public class NotTeamMemberException
    extends AccessDeniedException {

    public NotTeamMemberException(String email) {
        super("User " + email + " is not part of any team");
    }
}
