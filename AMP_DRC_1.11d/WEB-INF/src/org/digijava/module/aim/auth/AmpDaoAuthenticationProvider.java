package org.digijava.module.aim.auth;

import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.digijava.kernel.security.auth.DigiDaoAuthenticationProvider;

public class AmpDaoAuthenticationProvider
    extends DigiDaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authentication)  {

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
