package org.digijava.kernel.ampapi.endpoints;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author Diego
 * Sample class to test end points are working properly  
 * Root resource (exposed at "test" path)	
 */

@Path("test")
public class TestEndPoints {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 * 
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "Hey, This is AMP API Test Root Path";
	}

	@GET
	@Path("/testjson")
	@Produces(MediaType.APPLICATION_JSON)
	public testObj simplejson() {
		testObj jsonobj = new testObj("AMP API Test End Point - Test JSON Format");
		return jsonobj;
	}

	/***
	 * 
	 * Auxiliary object that will be converted to JSON 
	 *
	 */
	
	private class testObj {
		private Integer Id;
		private String Message;

		public testObj(String message) {
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