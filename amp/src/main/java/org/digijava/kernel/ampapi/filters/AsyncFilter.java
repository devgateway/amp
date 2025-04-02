package org.digijava.kernel.ampapi.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.X_ASYNC_RESULT_ID;

/**
 * Detects whenever a request was issued by AMP Offline or IATI Importer.
 * Stores this value for the duration of the request.
 *
 * @author Viorel Chihai
 */
public class AsyncFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        String preferHeader = request.getHeader("Prefer");
        if (preferHeader != null && preferHeader.contains("respond-async")) {
            String resultId = UUID.randomUUID().toString();
            servletRequest.setAttribute(X_ASYNC_RESULT_ID, resultId);
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
}
