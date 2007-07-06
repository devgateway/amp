/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.sql.Timestamp;

public class AmpAuditLogger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004856623866175824L;
	/**
	 * 
	 */
	

	private Long id;
	private String teamName;
	private String authorName;
	private String authorEmail;
	private Timestamp loggedDate;
	private String browser;
	private String ip;
	private String action;
	private String objectId;
	private String objectType;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public  Timestamp getLoggedDate() {
		return loggedDate;
	}
	public void setLoggedDate(Timestamp loggedDate) {
		this.loggedDate = loggedDate;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
}