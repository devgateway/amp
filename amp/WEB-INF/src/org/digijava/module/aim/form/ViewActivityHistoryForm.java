package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;

public class ViewActivityHistoryForm extends ActionForm implements Serializable {

	private Long activityId;

	private Set<AmpActivity> activities;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Set<AmpActivity> getActivities() {
		return activities;
	}

	public void setActivities(Set<AmpActivity> activities) {
		this.activities = activities;
	}
}