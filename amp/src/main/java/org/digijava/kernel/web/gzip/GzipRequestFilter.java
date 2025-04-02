package org.digijava.kernel.web.gzip;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * A servlet filter that can detect and decompress requests with Content-Encoding: gzip
 * @author Octavian Ciubotaru
 */
public class GzipRequestFilter implements Filter {

    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String GZIP_CONTENT_ENCODING = "gzip";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (GZIP_CONTENT_ENCODING.equals(httpServletRequest.getHeader(CONTENT_ENCODING_HEADER))) {
            httpServletRequest = new GzippedServletRequest(httpServletRequest);
        }

        chain.doFilter(httpServletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
