package org.digijava.module.aim.helper;

import java.util.Date;


/** bean used for holding information about user (first and last name) who modified the activity
 * 
 * @author Viorel Chihai
 *
 */
public class ActivityHistory {

	private Long activityId;
	private String modifiedBy;
	private Date modifiedDate;
	
	public ActivityHistory() {
		
	}
	
	public ActivityHistory(Long activityId, String modifiedBy, Date modifiedDate) {
		super();
		this.activityId = activityId;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
