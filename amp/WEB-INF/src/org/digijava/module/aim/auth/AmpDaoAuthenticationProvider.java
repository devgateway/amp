package org.digijava.module.aim.auth;

import org.digijava.kernel.security.auth.DigiDaoAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class AmpDaoAuthenticationProvider
    extends DigiDaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authentication)  {

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
