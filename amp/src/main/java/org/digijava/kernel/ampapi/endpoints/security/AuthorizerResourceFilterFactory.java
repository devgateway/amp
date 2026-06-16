package org.digijava.kernel.ampapi.endpoints.security;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class AuthorizerResourceFilterFactory implements ContainerRequestFilter {

    private static final Set<String> CSRF_SAFE_METHODS =
            new HashSet<>(Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE"));

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
        if (apiMethod != null) {
            // Enforce CSRF same-origin check for authenticated endpoints.
            // Public/unauthenticated endpoints (authTypes empty or absent) are exempt
            // because there is no privileged session to hijack.
            if (ActionAuthorizer.requiresAuthentication(apiMethod) && !isSameOrigin(requestContext)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("CSRF check failed: cross-origin request rejected").build());
                return;
            }
            ActionAuthorizer.authorize(method, apiMethod, (ContainerRequest) requestContext.getRequest());
        }
    }

    /**
     * Returns true when the request is same-origin or comes from a non-browser client.
     * Blocks only when {@code Origin} or {@code Referer} is present but does not match
     * the server host — the signature of a cross-site browser request.
     */
    private boolean isSameOrigin(ContainerRequestContext requestContext) {
        String httpMethod = requestContext.getMethod();
        if (httpMethod == null || CSRF_SAFE_METHODS.contains(httpMethod.toUpperCase(Locale.ROOT))) {
            return true;
        }

        String origin = requestContext.getHeaderString("Origin");
        String referer = requestContext.getHeaderString("Referer");

        // No Origin and no Referer: non-browser client (curl, server-to-server) — allow
        if ((origin == null || origin.isEmpty()) && (referer == null || referer.isEmpty())) {
            return true;
        }

        String expectedHost = getServerHost(requestContext);

        if (origin != null && !origin.isEmpty()) {
            // "null" Origin comes from privacy-sensitive contexts (file://, data:) — allow
            if ("null".equals(origin)) {
                return true;
            }
            return hostMatches(origin, expectedHost);
        }

        if (referer != null && !referer.isEmpty()) {
            return hostMatches(referer, expectedHost);
        }

        return true;
    }

    /**
     * Resolves the expected server host from the request, preferring the
     * {@code X-Forwarded-Host} and {@code Host} headers so the check works
     * correctly behind reverse proxies.
     */
    private String getServerHost(ContainerRequestContext requestContext) {
        String forwarded = requestContext.getHeaderString("X-Forwarded-Host");
        if (forwarded != null && !forwarded.isEmpty()) {
            String first = forwarded.split(",")[0].trim().toLowerCase(Locale.ROOT);
            if (!first.isEmpty()) {
                return first;
            }
        }

        String host = requestContext.getHeaderString("Host");
        if (host != null && !host.isEmpty()) {
            return host.trim().toLowerCase(Locale.ROOT);
        }

        // Fallback: derive from the JAX-RS request URI (works for direct connections)
        URI requestUri = requestContext.getUriInfo().getRequestUri();
        String scheme = requestUri.getScheme().toLowerCase(Locale.ROOT);
        String uriHost = requestUri.getHost().toLowerCase(Locale.ROOT);
        int port = requestUri.getPort();
        if (port == -1
                || ("http".equals(scheme) && port == 80)
                || ("https".equals(scheme) && port == 443)) {
            return uriHost;
        }
        return uriHost + ":" + port;
    }

    private boolean hostMatches(String headerValue, String expectedHost) {
        try {
            URI uri = new URI(headerValue.trim());
            String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase(Locale.ROOT) : "";
            String uriHost = uri.getHost() != null ? uri.getHost().toLowerCase(Locale.ROOT) : "";
            int uriPort = uri.getPort();

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
}
