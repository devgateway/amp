package org.digijava.kernel.security.csrf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CSRF protection for the /rest/** Jersey endpoints.
 *
 * <p>Spring Security is bypassed for /rest/** (security="none"), so this filter
 * provides Origin/Referer-based same-origin enforcement instead.
 *
 * <p>Strategy:
 * <ul>
 *   <li>Safe HTTP methods (GET, HEAD, OPTIONS, TRACE) are always allowed.</li>
 *   <li>If no Origin or Referer header is present the request is allowed — this
 *       covers non-browser clients (curl, server-to-server, mobile apps) that
 *       never send those headers.</li>
 *   <li>If Origin is present and matches the server host the request is allowed.</li>
 *   <li>If Origin is present and does NOT match the server host the request is
 *       rejected with 403.</li>
 *   <li>If Origin is absent but Referer is present and does not match, the request
 *       is rejected.</li>
 * </ul>
 */
public class RestCsrfOriginFilter implements Filter {

    private static final Set<String> SAFE_METHODS =
            new HashSet<>(Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no initialisation required
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String method = request.getMethod();
        if (method == null || SAFE_METHODS.contains(method.toUpperCase(Locale.ROOT))) {
            chain.doFilter(request, response);
            return;
        }

        String originHeader = request.getHeader("Origin");
        String refererHeader = request.getHeader("Referer");

        // No Origin and no Referer: non-browser client — allow
        if ((originHeader == null || originHeader.isEmpty())
                && (refererHeader == null || refererHeader.isEmpty())) {
            chain.doFilter(request, response);
            return;
        }

        String expectedHost = getServerHost(request);

        // Check Origin first (most reliable)
        if (originHeader != null && !originHeader.isEmpty()) {
            // "null" Origin comes from privacy-sensitive contexts (file://, data:) - allow
            if ("null".equals(originHeader)) {
                chain.doFilter(request, response);
                return;
            }
            if (hostMatches(originHeader, expectedHost)) {
                chain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "CSRF check failed: Origin header does not match server host");
            }
            return;
        }

        // Fall back to Referer if Origin is absent
        if (refererHeader != null && !refererHeader.isEmpty()) {
            if (hostMatches(refererHeader, expectedHost)) {
                chain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "CSRF check failed: Referer header does not match server host");
            }
            return;
        }

        // Should not reach here given earlier checks, but allow by default
        chain.doFilter(request, response);
    }

    /**
     * Returns "host[:port]" for the current request, normalising default ports
     * (80 for http, 443 for https) so they are omitted from comparison.
     */
    private String getServerHost(HttpServletRequest request) {
        String scheme = request.getScheme().toLowerCase(Locale.ROOT);
        String host = request.getServerName().toLowerCase(Locale.ROOT);
        int port = request.getServerPort();
        if (("http".equals(scheme) && port == 80)
                || ("https".equals(scheme) && port == 443)) {
            return host;
        }
        return host + ":" + port;
    }

    /**
     * Extracts the "host[:port]" from an Origin or Referer header value and
     * compares it against the expected server host string.
     */
    private boolean hostMatches(String headerValue, String expectedHost) {
        try {
            URI uri = new URI(headerValue.trim());
            String scheme = uri.getScheme() != null
                    ? uri.getScheme().toLowerCase(Locale.ROOT) : "";
            String uriHost = uri.getHost() != null
                    ? uri.getHost().toLowerCase(Locale.ROOT) : "";
            int uriPort = uri.getPort(); // -1 if absent

            String normalised;
            if (uriPort == -1
                    || ("http".equals(scheme) && uriPort == 80)
                    || ("https".equals(scheme) && uriPort == 443)) {
                normalised = uriHost;
            } else {
                normalised = uriHost + ":" + uriPort;
            }
            return expectedHost.equals(normalised);
        } catch (URISyntaxException e) {
            // Malformed header — treat as non-matching for safety
            return false;
        }
    }

    @Override
    public void destroy() {
        // nothing to clean up
    }
}
