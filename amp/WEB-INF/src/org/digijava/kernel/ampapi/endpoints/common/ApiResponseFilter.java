package org.digijava.kernel.ampapi.endpoints.common;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;


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
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        //clean session if it has been restored by the request filter
        if ("true".equals(TLSUtils.getRequest().getAttribute(SecurityUtil.REMOVE_SESSION))) {
            TLSUtils.getRequest().getSession().removeAttribute(Constants.CURRENT_USER);
            TLSUtils.getRequest().getSession().removeAttribute(Constants.CURRENT_MEMBER);
            TLSUtils.getRequest().removeAttribute(SecurityUtil.REMOVE_SESSION);
        }
        Integer responseStatusMarker = EndpointUtils.getResponseStatusMarker();
        // override only the default 200 status with custom one
        if (responseStatusMarker != null && response.getStatus() == HttpServletResponse.SC_OK) {
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
