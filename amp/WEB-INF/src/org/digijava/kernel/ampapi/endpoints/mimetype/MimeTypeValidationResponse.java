package org.digijava.kernel.ampapi.endpoints.mimetype;

/**
 *  This class represents a response obtained from the mime type validation
 * 
 * @author Viorel Chihai
 *
 */
public class MimeTypeValidationResponse {

	MimeTypeValidationStatus status;
	
	String contentName;
	String description;
	String extension;
	
	public MimeTypeValidationResponse(MimeTypeValidationStatus status) {
		this.status = status;
	}
	
	public MimeTypeValidationResponse(MimeTypeValidationStatus status, String contentName, String description) {
		this.status = status;
		this.contentName = contentName;
		this.description = description;
	}
	
	public MimeTypeValidationResponse(MimeTypeValidationStatus status, String contentName, String description, String extension) {
		super();
		this.status = status;
		this.contentName = contentName;
		this.description = description;
		this.extension = extension;
	}

	public MimeTypeValidationStatus getStatus() {
		return status;
	}

	public void setStatus(MimeTypeValidationStatus status) {
		this.status = status;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String conentName) {
		this.contentName = conentName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}
