package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

/**
 * @author Octavian Ciubotaru
 */
public class ApiCompatFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            ApiCompat.setRequestedMediaType(httpRequest.getHeader(HttpHeaders.ACCEPT));

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ApiCompat.setRequestedMediaType(null);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
