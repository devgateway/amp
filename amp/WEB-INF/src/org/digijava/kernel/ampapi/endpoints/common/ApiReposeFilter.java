package org.digijava.kernel.ampapi.endpoints.common;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;

import javax.ws.rs.core.Response;

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

        Object responseStatusValue = TLSUtils.getRequest().getAttribute(EPConstants.RESPONSE_STATUS);
        if (responseStatusValue != null) {
            try {
                response.setStatus(Integer.parseInt(responseStatusValue.toString()));
            } catch (NumberFormatException ex) {
                logger.warn("Setting response status: invalid, " + responseStatusValue, ex);
            }
        }

        return response;
    }
}
