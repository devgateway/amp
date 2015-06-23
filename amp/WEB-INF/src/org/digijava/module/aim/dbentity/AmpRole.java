package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

public class AmpRole implements Serializable, Comparable<AmpRole>
{
	@Interchangeable(fieldTitle="AMP Role ID", id = true)
	private Long ampRoleId ;
	@Interchangeable(fieldTitle="Role Code")
	private String roleCode ;
	@Interchangeable(fieldTitle="Name", value = true)
	private String name ;
	@Interchangeable(fieldTitle="Type")
	private String type ;
	@Interchangeable(fieldTitle="Description")
	private String description ;
	@Interchangeable(fieldTitle="Language")
	private String language ;
	
	
	/**
	 * @return
	 */
	public Long getAmpRoleId() {
		return ampRoleId;
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
	public String getRoleCode() {
		return roleCode;
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
	public void setAmpRoleId(Long long1) {
		ampRoleId = long1;
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
	public void setRoleCode(String string) {
		roleCode = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	@Override
	public String toString() {	
		return name;
	}

	@Override
	public int compareTo(AmpRole o) {
		if(this.getAmpRoleId()!=null && o.getAmpRoleId()!=null) return this.getAmpRoleId().compareTo(o.getAmpRoleId());
		return -1;
	}
	
}	
