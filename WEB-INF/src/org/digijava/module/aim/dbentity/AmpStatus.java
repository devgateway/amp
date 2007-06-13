package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;

/**
 * 
 * @deprecated
 *
 */
public class AmpStatus implements Serializable, Comparable, Identifiable
{
	private Long ampStatusId ;
	private String statusCode ;
	private String name ;
	private String type ;
	private String description ;
	private String language ;
	private String version ;	
			
	public AmpStatus() 
	{
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
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
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
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param long1
	 */
	public void setAmpStatusId(Long long1) {
		ampStatusId = long1;
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
	public void setLanguage(String string) {
		language = string;
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
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	public int compareTo(Object o) {
		return this.ampStatusId.compareTo(((AmpStatus)o).getAmpStatusId());
	}
	
	public String toString() {
		return name;
	}

	public Object getIdentifier() {
		return this.getAmpStatusId();
	}


}	


