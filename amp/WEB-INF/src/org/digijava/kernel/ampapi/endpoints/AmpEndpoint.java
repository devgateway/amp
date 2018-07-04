package org.digijava.kernel.ampapi.endpoints;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * Specifies implicit parameters for all endpoint operations.
 *
 * @author Octavian Ciubotaru
 */
@ApiImplicitParams({
        @ApiImplicitParam(
                name = "translations",
                type = "string",
                value = "Pipe separated list of ISO2 language codes",
                paramType = "query",
                example = "en|fr",
                dataTypeClass = String.class),
        @ApiImplicitParam(
                name = "X-Auth-Token",
                type = "string",
                value = "Authentication Token",
                paramType = "header",
                dataTypeClass = String.class)})
public interface AmpEndpoint {
}
