package org.digijava.kernel.web;

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
 * Servlet filter that adds browser-hardening HTTP security headers to every response.
 *
 * <p>Headers applied:
 * <ul>
 *   <li>{@code X-Content-Type-Options: nosniff} – prevents MIME-type sniffing.</li>
 *   <li>{@code X-Frame-Options: SAMEORIGIN} – blocks cross-origin iframe embedding.</li>
 *   <li>{@code X-XSS-Protection: 0} – disables the legacy browser XSS Auditor (which can
 *       introduce vulnerabilities of its own; modern browsers ignore it in favour of CSP).</li>
 *   <li>{@code Referrer-Policy: strict-origin-when-cross-origin} – limits referrer
 *       leakage on cross-origin navigations.</li>
 *   <li>{@code Content-Security-Policy} – restricts resource origins to reduce XSS and
 *       data-injection attack surface. {@code 'unsafe-inline'} and {@code 'unsafe-eval'}
 *       are permitted for scripts and styles because the legacy Wicket/Struts UI embeds
 *       inline code extensively; tightening these directives requires a separate
 *       front-end refactor.</li>
 *   <li>{@code Strict-Transport-Security: max-age=31536000; includeSubDomains} – instructs
 *       browsers to use HTTPS exclusively for one year. Applied only when the current
 *       request itself arrived over TLS, so plain-HTTP local environments are not
 *       immediately broken. See the cookie policy note below.</li>
 * </ul>
 *
 * <p><b>Cookie policy</b>: the session cookie is declared {@code HttpOnly} and
 * {@code Secure} through {@code <cookie-config>} in {@code web.xml}.
 * {@code HttpOnly} prevents client-side JavaScript from reading the session token.
 * {@code Secure} ensures the cookie is only transmitted over HTTPS.
 *
 * <p><b>HTTPS requirement</b>: the {@code Secure} cookie flag and HSTS together require
 * every deployment to terminate TLS. If the application sits behind a TLS-terminating
 * load-balancer or reverse-proxy, configure it to forward {@code X-Forwarded-Proto:
 * https}; Tomcat must then be configured with a {@code RemoteIpValve} (or equivalent)
 * so that {@link HttpServletRequest#isSecure()} returns {@code true} and
 * {@link javax.servlet.http.HttpServletRequest#getScheme()} returns {@code "https"}.
 * For local development over plain HTTP, remove the {@code <secure>true</secure>} entry
 * from the {@code <cookie-config>} in {@code web.xml}.
 */
public class SecurityHeadersFilter implements Filter {

    private static final String HSTS_VALUE = "max-age=31536000; includeSubDomains";

    /**
     * Content-Security-Policy directive applied to every response.
     *
     * <p>Directive notes:
     * <ul>
     *   <li>{@code frame-ancestors 'self'} is the CSP equivalent of {@code X-Frame-Options:
     *       SAMEORIGIN} and takes precedence in browsers that support CSP Level 2.</li>
     *   <li>{@code img-src 'self' data: blob:} is required because several UI components
     *       embed images as data URIs or object URLs.</li>
     *   <li>{@code font-src 'self' data:} covers web-fonts bundled as data URIs.</li>
     * </ul>
     */
    private static final String CSP_VALUE =
            "default-src 'self'; "
            + "script-src 'self' 'unsafe-inline' 'unsafe-eval'; "
            + "style-src 'self' 'unsafe-inline'; "
            + "img-src 'self' data: blob:; "
            + "font-src 'self' data:; "
            + "connect-src 'self'; "
            + "frame-ancestors 'self'; "
            + "object-src 'none';";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
        httpResponse.setHeader("X-XSS-Protection", "0");
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        httpResponse.setHeader("Content-Security-Policy", CSP_VALUE);

        // Only add HSTS when the connection is already secure; sending it over plain
        // HTTP would lock out users who cannot reach the HTTPS endpoint yet.
        if (((HttpServletRequest) request).isSecure()) {
            httpResponse.setHeader("Strict-Transport-Security", HSTS_VALUE);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
