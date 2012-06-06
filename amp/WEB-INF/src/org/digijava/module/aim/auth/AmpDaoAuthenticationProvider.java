package org.digijava.module.aim.auth;

import org.digijava.kernel.security.auth.DigiDaoAuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;

public class AmpDaoAuthenticationProvider
    extends DigiDaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authentication)  {

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
