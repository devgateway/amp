package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm;


public class StatusItemForm extends ActionForm
{
	
	private Long ampStatusId ;
	private String statusCode;
	private String name ;
	private String type;
	private String description;
	private Long originalAmpStatusId;
	
	
	public Long getOriginalAmpStatusId() {
		return originalAmpStatusId;
	}

	public void setOriginalAmpStatusId(Long originalAmpStatusId) {
		this.originalAmpStatusId = originalAmpStatusId;
	}

	/**
	 * @return
	 */
	public Long getAmpStatusId() {
		return ampStatusId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param string
	 */
	public void setAmpStatusId(Long long1) {
		ampStatusId = long1;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setStatusCode(String string) {
		statusCode = string;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

}