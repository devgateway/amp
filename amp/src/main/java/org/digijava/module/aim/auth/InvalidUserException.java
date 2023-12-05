package org.digijava.module.aim.auth;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserException
    extends AuthenticationException {

    public InvalidUserException(String email) {
        super("User " + email + " is invalid");
//      invalid app settings please assign the user to a team or delete the user
        
    }
}
