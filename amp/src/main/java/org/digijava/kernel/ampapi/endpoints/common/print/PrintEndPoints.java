package org.digijava.kernel.ampapi.endpoints.common.print;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

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
public class PrintEndPoints {

    /**
     * Creates an image from a html and retrieves the image as base64 string
     *
     * @param parameters The JSON parameters in the POST request
     *                   The json should look like:
     *                   {
     *                      content: "<html>...</html>",
     *                      width: 100, //optional
     *                      height: 100, // optional
     *                   }
     *
     * @return Response
     *  The HttpResponse with the status code, and the image as a base64 String as the response body
     */
    @POST
    @Path("/print")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces("image/png")
    @ApiMethod(ui = false, id = "print")
    public final Response createImage(final HtmlContent parameters) {
        return PrintImageService.createImage(parameters);
    }
}
