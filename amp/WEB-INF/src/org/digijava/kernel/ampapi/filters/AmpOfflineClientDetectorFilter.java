package org.digijava.kernel.ampapi.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

/**
 * Detects whenever a request was issued by AMP Offline and saves this value for the duration of the request.
 *
 * @author Octavian Ciubotaru
 */
public class AmpOfflineClientDetectorFilter implements Filter {

    private static final String AMP_OFFLINE_USER_AGENT_PREFIX = "AMPOffline";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            AmpOfflineModeHolder.setAmpOfflineMode(isAmpOfflineRequest((HttpServletRequest) servletRequest));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AmpOfflineModeHolder.setAmpOfflineMode(null);
        }
    }

    private boolean isAmpOfflineRequest(HttpServletRequest httpServletRequest) {
        Enumeration<String> headers = httpServletRequest.getHeaders(HttpHeaders.USER_AGENT);
        while (headers.hasMoreElements()) {
            String userAgent = headers.nextElement();
            if (isAmpOfflineAgent(userAgent)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAmpOfflineAgent(String userAgent) {
        return userAgent != null && userAgent.startsWith(AMP_OFFLINE_USER_AGENT_PREFIX);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
