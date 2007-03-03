package org.digijava.module.aim.dbentity;

import java.io.Serializable;
 
public class AmpModality implements Serializable, Comparable
{
	private Long ampModalityId ;
	private String modalityCode ;
	private String channelCode ;
	private String type ;
	private String description ;
	private String name ;

	/**
	 * @return
	 */
	public Long getAmpModalityId() {
		return ampModalityId;
	}

	/**
	 * @return
	 */
	public String getChannelCode() {
		return channelCode;
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
	public String getModalityCode() {
		return modalityCode;
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
	public String getType() {
		return type;
	}

	/**
	 * @param long1
	 */
	public void setAmpModalityId(Long long1) {
		ampModalityId = long1;
	}

	/**
	 * @param string
	 */
	public void setChannelCode(String string) {
		channelCode = string;
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
	public void setModalityCode(String string) {
		modalityCode = string;
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
	public void setType(String string) {
		type = string;
	}

	public int compareTo(Object o) {
		AmpModality b=(AmpModality) o;
		return this.getAmpModalityId().compareTo(b.getAmpModalityId());
		//return this.getName().compareTo(b.getName());
	}

}
		
