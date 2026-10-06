package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import javax.ws.rs.core.Response;

import static org.digijava.kernel.ampapi.endpoints.util.ApiConstants.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.digijava.kernel.ampapi.endpoints.util.ApiConstants.ACCESS_CONTROL_ALLOW_METHODS;
import static org.digijava.kernel.ampapi.endpoints.util.ApiConstants.ACCESS_CONTROL_ALLOW_ORIGIN;

public final class PublicServices {

    private PublicServices() {

    }

    public static Response buildOkResponseWithOriginHeaders(Object entity) {
        return Response
                .ok()
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .header(ACCESS_CONTROL_ALLOW_HEADERS, "*")
                .header(ACCESS_CONTROL_ALLOW_METHODS, "*")
                .entity(entity)
                .build();
    }
}
