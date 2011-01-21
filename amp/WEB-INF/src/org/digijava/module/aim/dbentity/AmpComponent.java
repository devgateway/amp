/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
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
	private java.sql.Timestamp creationdate;
	private String code;
	
	//private String type;
	private AmpComponentType type;
	
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
	
	public AmpComponentType getType() {
		return type;
	}
	public void setType(AmpComponentType type) {
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
	
	@Override
	public boolean equals(Object obj) {
		AmpComponent target=(AmpComponent) obj;
		if (this.ampComponentId == null)
			return super.equals(obj);
		
		if (target!=null && this.ampComponentId!=null){
			return (target.getAmpComponentId().doubleValue()==this.getAmpComponentId().doubleValue());
		}
		return false;
		
	}
	/**
	 * If overriding is really needed please fully comment on the reason!
	 *
	@Override
	public int hashCode() {
		if( this.ampComponentId ==null) return 0;
		return this.ampComponentId.hashCode();
	}
	 */
	
	public java.sql.Timestamp getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(java.sql.Timestamp creationdate) {
		this.creationdate = creationdate;
	}
}
