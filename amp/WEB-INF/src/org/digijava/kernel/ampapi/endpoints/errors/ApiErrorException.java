package org.digijava.kernel.ampapi.endpoints.errors;

import javax.ws.rs.core.Response.Status;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Defines API Error Exception. Stores @Response.Status and JsonBean with error messages
 * 
 * @author Viorel Chihai
 */
public class ApiErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Status responseStatus;
	private JsonBean error;
	
	public ApiErrorException(Status status, JsonBean error) {
		this.responseStatus = status;
		this.error = error;
	}
	
	public Status getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Status responseStatus) {
		this.responseStatus = responseStatus;
	}

	public JsonBean getError() {
		return error;
	}

	public void setError(JsonBean error) {
		this.error = error;
	}
}
