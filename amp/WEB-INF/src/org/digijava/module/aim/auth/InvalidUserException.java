package org.digijava.module.aim.auth;

import org.springframework.security.AccessDeniedException;

public class InvalidUserException
    extends AccessDeniedException {

    public InvalidUserException(String email) {
        super("User " + email + " is invalid");
//      invalid app settings please assign the user to a team or delete the user
        
    }
}
