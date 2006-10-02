package org.digijava.module.aim.auth;

import org.digijava.kernel.security.auth.DigiDaoAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.AuthenticationException;

public class AmpDaoAuthenticationProvider
    extends DigiDaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authentication) throws
        AuthenticationException {

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
