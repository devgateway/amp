package org.digijava.kernel.ampapi.filters;

import org.digijava.kernel.translator.util.TrnUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Detects whenever a request was issued by AMP Offline or IATI Importer.
 * Stores this value for the duration of the request.
 *
 * @author Viorel Chihai
 */
public class TranslationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        String wsPrefix = request.getHeader("ws-prefix");
        if (wsPrefix != null) {
            TrnUtil.setTrnPrefix(wsPrefix);
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
