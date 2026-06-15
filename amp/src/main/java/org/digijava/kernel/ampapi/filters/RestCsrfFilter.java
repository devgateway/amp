package org.digijava.kernel.ampapi.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CSRF protection for the Jersey REST layer (/rest/**).
 *
 * <p>The Spring Security filter chain is configured with {@code security="none"} for /rest/**,
 * so Spring's built-in CSRF filter does not run there. This filter provides equivalent
 * protection using the Double Submit Cookie pattern:</p>
 * <ul>
 *   <li>Spring Security writes the CSRF token into the {@code XSRF-TOKEN} cookie
 *       (httpOnly=false) via {@code CookieCsrfTokenRepository.withHttpOnlyFalse()}.</li>
 *   <li>Axios (used by the reampv2 SPA) reads that cookie and echoes it as the
 *       {@code X-XSRF-TOKEN} request header automatically for same-origin requests.</li>
 *   <li>This filter rejects state-changing requests where the header is absent or
 *       does not match the cookie value.</li>
 * </ul>
 *
 * <p>Non-browser clients (AMP Offline, IATI Importer) are identified by
 * {@link AmpClientDetectorFilter} which must run before this filter in the chain.
 * Those clients do not have the cookie set and are therefore exempt.</p>
 */
public class RestCsrfFilter implements Filter {

    /** HTTP methods that do not change server state and are exempt from CSRF checks. */
    private static final Set<String> SAFE_METHODS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE")));

    static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!isSafeMethod(request) && isBrowserClient() && hasSession(request)) {
            String cookieValue = getCookieValue(request, CSRF_COOKIE_NAME);
            String headerValue = request.getHeader(CSRF_HEADER_NAME);

            if (cookieValue == null || cookieValue.isEmpty() || !cookieValue.equals(headerValue)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token validation failed");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /** Returns {@code true} when the request was issued by a browser (not a dedicated client). */
    private boolean isBrowserClient() {
        return !AmpClientModeHolder.isOfflineClient() && !AmpClientModeHolder.isIatiImporterClient();
    }

    /**
     * Returns {@code true} when the request carries a session cookie, indicating an authenticated
     * browser session that is susceptible to CSRF.  Unauthenticated public-API calls (the
     * intentionally cross-origin endpoints that respond with {@code Access-Control-Allow-Origin: *})
     * arrive without a session and therefore carry no CSRF risk.
     */
    private boolean hasSession(HttpServletRequest request) {
        return getCookieValue(request, "JSESSIONID") != null;
    }

    private boolean isSafeMethod(HttpServletRequest request) {
        return SAFE_METHODS.contains(request.getMethod().toUpperCase());
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no configuration needed
    }

    @Override
    public void destroy() {
        // nothing to release
    }
}
