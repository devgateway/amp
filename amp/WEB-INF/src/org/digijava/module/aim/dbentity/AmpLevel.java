package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


/**
 * @deprecated
 * @author alex
 *
 */
public class AmpLevel implements Serializable
{
	
	
	private Long ampLevelId ;
	private String levelCode ;
	private String name;
	private String levelType ;
	private String description ;
	private String language ;
	private String version ;	
			
	public AmpLevel()
	{
	}	
	
	/**
	 * @return
	 */
	public Long getAmpLevelId() {
		return ampLevelId;
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
	public String getLevelCode() {
		return levelCode;
	}

	/**
	 * @return
	 */
	public String getLevelType() {
		return levelType;
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
	public void setAmpLevelId(Long long1) {
		ampLevelId = long1;
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
	public void setLevelCode(String string) {
		levelCode = string;
	}

	/**
	 * @param string
	 */
	public void setLevelType(String string) {
		levelType = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}


}	
