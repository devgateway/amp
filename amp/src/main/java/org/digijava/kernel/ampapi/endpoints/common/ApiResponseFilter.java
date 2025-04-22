package org.digijava.kernel.ampapi.endpoints.common;


import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.Map;


/**
 * Endpoint response post processor filer
 */
public class ApiResponseFilter implements ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(ApiResponseFilter.class);

    /**
     * Filter method called after a response has been provided for a request
     * @param request
     * @param response
     * @return
     */

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        Integer responseStatusMarker = EndpointUtils.getResponseStatusMarker();
        // override only the default 200 status with custom one
        if (responseStatusMarker != null && containerResponseContext.getStatus() == HttpServletResponse.SC_OK) {
            containerResponseContext.setStatus(responseStatusMarker);
        }

        Map<String, String> responseHeaderMarkers = EndpointUtils.getResponseHeaderMarkers();
        if (responseHeaderMarkers != null) {
            for (Map.Entry<String, String> headerMarker : responseHeaderMarkers.entrySet()) {
                containerResponseContext.getHeaders().add(headerMarker.getKey(), headerMarker.getValue());
            }
        }

        EndpointUtils.cleanUpResponseMarkers();

//        return containerResponseContext;
    }
}
