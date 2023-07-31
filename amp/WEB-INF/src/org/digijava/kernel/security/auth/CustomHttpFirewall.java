package org.digijava.kernel.security.auth;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class CustomHttpFirewall extends StrictHttpFirewall {

    public CustomHttpFirewall() {
        setAllowBackSlash(true); // Allow backslashes
        setAllowUrlEncodedSlash(true);

    }

    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
        // You can perform additional processing on the request before returning a FirewalledRequest
        return super.getFirewalledRequest(request);
    }


}
