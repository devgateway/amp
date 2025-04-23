package org.digijava.kernel.ampapi.endpoints.security;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.reflect.Method;


public class AuthorizerResourceFilterFactory implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
        if (apiMethod != null) {
            ActionAuthorizer.authorize(method, apiMethod, (ContainerRequest) requestContext.getRequest());
        }
    }
}