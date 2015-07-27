package org.digijava.kernel.ampapi.endpoints.common;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.apache.log4j.Logger;

import java.util.Map;


/**
 * Endpoint response post processor filer
 */
public class ApiReposeFilter implements ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(ApiReposeFilter.class);

    /**
     * Filter method called after a response has been provided for a request
     * @param request
     * @param response
     * @return
     */
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        Integer responseStatusMarker = EndpointUtils.getResponseStatusMarker();
        if (responseStatusMarker != null) {
            response.setStatus(responseStatusMarker);
        }

        Map<String, String> responseHeaderMarkers = EndpointUtils.getResponseHeaderMarkers();
        if (responseHeaderMarkers != null) {
            for (Map.Entry<String, String> headerMarker : responseHeaderMarkers.entrySet()) {
                response.getHttpHeaders().add(headerMarker.getKey(), headerMarker.getValue());
            }
        }

        EndpointUtils.cleanUpResponseMarkers();

        return response;
    }
}
