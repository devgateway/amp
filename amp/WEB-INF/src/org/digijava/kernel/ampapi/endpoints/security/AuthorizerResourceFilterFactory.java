package org.digijava.kernel.ampapi.endpoints.security;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * Factory for resource and sub-resource method filters for authorization purposes.
 *
 * @author Octavian Ciubotaru
 */
public class AuthorizerResourceFilterFactory implements ResourceFilterFactory {

    private static class Filter implements ResourceFilter, ContainerRequestFilter {

        private Method method;
        private ApiMethod apiMethod;

        Filter(Method method, ApiMethod apiMethod) {
            this.method = method;
            this.apiMethod = apiMethod;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return this;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            ActionAuthorizer.authorize(method, apiMethod, request);
            return request;
        }
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        ApiMethod apiMethod = am.getAnnotation(ApiMethod.class);
        if (apiMethod != null) {
            return Collections.singletonList(new Filter(am.getMethod(), apiMethod));
        }
        return null;
    }
}
