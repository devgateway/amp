package org.digijava.module.aim.dbentity ;

public class AmpRole
{
	private Long ampRoleId ;
	private String roleCode ;
	private String name ;
	private String type ;
	private String description ;
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

}	
