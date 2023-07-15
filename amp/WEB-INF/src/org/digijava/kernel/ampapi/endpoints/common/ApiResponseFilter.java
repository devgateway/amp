package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampregistry.JerseyAmpRegistryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.util.Map;

public class ApiResponseFilter implements ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseFilter.class);

    /**
     * Filter method called after a response has been provided for a request
     * @param requestContext
     * @param responseContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

        Integer responseStatusMarker = EndpointUtils.getResponseStatusMarker();
        // override only the default 200 status with custom one
        if (responseStatusMarker != null && responseContext.getStatus() == HttpServletResponse.SC_OK) {
            responseContext.setStatus(responseStatusMarker);
        }

        Map<String, String> responseHeaderMarkers = EndpointUtils.getResponseHeaderMarkers();
        if (responseHeaderMarkers != null) {
            for (Map.Entry<String, String> headerMarker : responseHeaderMarkers.entrySet()) {
                responseContext.getHeaders().add(headerMarker.getKey(), headerMarker.getValue());
            }
        }

        EndpointUtils.cleanUpResponseMarkers();
    }
}
