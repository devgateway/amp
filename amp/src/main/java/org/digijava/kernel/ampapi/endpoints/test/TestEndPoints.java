package org.digijava.kernel.ampapi.endpoints.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Random;

/**
 * 
 * @author Diego Sample class to test end points are working properly Root
 *         resource (exposed at "test" path)
 */

@Path("test")
@Api("test")
public class TestEndPoints {

    private ServletContext context;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Test HTTP GET for \"text/plain\" media type",
            notes = "Return a sample text as a text/plain response")
    public String ping() {
        return "Hey, This is AMP API Test Root Path";
    }

    @GET
    @Path("/testjson")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Test HTTP GET for \"application/json\" media type",
            notes = "Return a sample text as a application/json response")
    public final TestObj simplejson() {
        TestObj jsonobj = new TestObj(
                "AMP API Test End Point - Test JSON Format");
        return jsonobj;
    }
    
    @GET
    @Path("/testjsonauth")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "simpleJsonAuth", ui = false)
    @ApiOperation(
            value = "Test HTTP GET that requires user to be authenticated",
            notes = "Return a sample text as a application/json response on success or unauthorized error code")
    public final TestObj simpleJsonAuth() {
        TestObj jsonobj = new TestObj("AMP API Test End Point Authenticated - Test JSON Format");
        return jsonobj;
    }
    
    @GET
    @Path ("/testquery")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Test Mondrian API Query",
            notes = "Return a text that clarifies that Mondrian API was removed from AMP")
    @Deprecated
    public final String queryresult() {
        return "Mondrian API removed from AMP";
    }


    @Context
    public void setServletContext(ServletContext context) {
        System.out.println("servlet context set here");
        this.context = context;
    }

    /***
     * 
     * Auxiliary object that will be converted to JSON
     * 
     */

    private class TestObj {
        private Integer Id;
        private String Message;

        TestObj(String message) {
            super();
            this.setMessage(message);
            this.Id = this.getId();
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public Integer getId() {
            Random random = new Random();
            return random.nextInt((100000 - 0) + 1);
        }
    }

}
