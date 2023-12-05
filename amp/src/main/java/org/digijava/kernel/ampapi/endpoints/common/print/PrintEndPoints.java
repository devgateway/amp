package org.digijava.kernel.ampapi.endpoints.common.print;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by esoliani on 22/07/16.
 */
@Path("commons")
@Api("commons")
public class PrintEndPoints {

    @POST
    @Path("/print")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces("image/png") // hardcoded for image only. this could be dynamic also for PDF
    @ApiMethod(ui = false, id = "print")
    @ApiOperation("Creates an image from a html content and retrieves the image as base64 String.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            response = String.class, message = "PNG in base 64 format."))
    public final Response createImage(final HtmlContent parameters) {
        return PrintImageService.createImage(parameters);
    }
}
