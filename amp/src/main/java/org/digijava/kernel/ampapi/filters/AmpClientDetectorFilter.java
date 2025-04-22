package org.digijava.kernel.ampapi.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Detects whenever a request was issued by AMP Offline or IATI Importer.
 * Stores this value for the duration of the request.
 *
 * @author Octavian Ciubotaru
 */
public class AmpClientDetectorFilter implements Filter {

    private static final String AMP_OFFLINE_USER_AGENT_PREFIX = "AMPOffline";
    private static final String IATI_IMPORTER_USER_AGENT_PREFIX = "IATIImporter";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            AmpClientModeHolder.setClientMode(getAmpClientRequest((HttpServletRequest) servletRequest));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AmpClientModeHolder.setClientMode(null);
        }
    }

    private ClientMode getAmpClientRequest(HttpServletRequest httpServletRequest) {
        Enumeration<String> headers = httpServletRequest.getHeaders(HttpHeaders.USER_AGENT);
        while (headers.hasMoreElements()) {
            String userAgent = headers.nextElement();
            if (isAmpOfflineAgent(userAgent)) {
                return ClientMode.AMP_OFFLINE;
            }
    
            if (isIatiImporterAgent(userAgent)) {
                return ClientMode.IATI_IMPORTER;
            }
        }
        
        return null;
    }

    private boolean isAmpOfflineAgent(String userAgent) {
        return userAgent != null && userAgent.startsWith(AMP_OFFLINE_USER_AGENT_PREFIX);
    }
    
    private boolean isIatiImporterAgent(String userAgent) {
        return userAgent != null && userAgent.startsWith(IATI_IMPORTER_USER_AGENT_PREFIX);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
