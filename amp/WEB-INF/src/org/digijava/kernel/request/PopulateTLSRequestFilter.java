package org.digijava.kernel.request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Viorel Chihai
 */
public class PopulateTLSRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            TLSUtils.populate((HttpServletRequest) request);
            
            chain.doFilter(request, response);
        } finally {
            TLSUtils.clean();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
    
}
