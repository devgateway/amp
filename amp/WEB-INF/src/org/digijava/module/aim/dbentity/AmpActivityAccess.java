package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.kernel.user.User;

public class AmpActivityAccess {
	private Long id;
	private Boolean viewed;
	private Boolean updated;
	private Date changeDate;
	private User user;
	private AmpActivityVersion activity;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getViewed() {
		return viewed;
	}
	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}
	public Boolean getUpdated() {
		return updated;
	}
	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public AmpActivityVersion getActivity() {
		return activity;
	}
	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}
}

