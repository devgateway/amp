package org.digijava.kernel.web.gzip;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
