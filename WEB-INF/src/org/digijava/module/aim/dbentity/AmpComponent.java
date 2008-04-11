/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Persister class for Components
 * @author Priyajith
 */
public class AmpComponent implements Serializable,Comparable<AmpComponent> {
	private static Logger logger = Logger.getLogger(AmpComponent.class);
	private Long ampComponentId;
	private String title;
	private String description;

	private String code;
	private String type;
	private Set activities;
	private String Url;
	
	public Set getActivities() {
		return activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public Long getAmpComponentId() {
		return ampComponentId;
	}
	public void setAmpComponentId(Long ampComponentId) {
		this.ampComponentId = ampComponentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getUrl() {
		return Url;
	}
	/**
	 * A simple string comparison to sort components by title
	 */
	public int compareTo(AmpComponent o) {
		try {
			if (this.title.compareToIgnoreCase(o.title) > 0) {
				return 1;
			} else if (this.title.compareToIgnoreCase(o.title) == 0) {
				return -0;
			}
		} catch (Exception e) {
			logger.error(e);
			return -1;
		}
		return -1;
	}	
	
}
