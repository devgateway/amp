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
     * Creates an image from a html content and retrieves the image as base64 String.
     * 
     * The parameters used for image generation:
     * <dl>
     * <b>content</b><dd>the html content</dd>
     * <b>width (optional)</b><dd>the width of the image (in px)</dd>
     * <b>height (optional)</b><dd>the height of the image (in px)</dd>
     * </dl>
     * The image will be generated in the PNG format.
     *
     * <h3>Sample Request:</h3><pre>
     * {
     *    "content" : "<html>...</html>",
     *    "width" : 100, //optional
     *    "height": 100, // optional
     *  }</pre>
     *  
     * @param parameters the JSON object containing the requested parameters
     *
     * @return Response - the HttpResponse with the status code and the image as a base64 String
     */
    @POST
    @Path("/print")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces("image/png") // hardcoded for image only. this could be dynamic also for PDF
    @ApiMethod(ui = false, id = "print")
    public final Response createImage(final HtmlContent parameters) {
        return PrintImageService.createImage(parameters);
    }
}
