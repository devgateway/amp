package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;

public class ViewActivityHistoryForm extends ActionForm implements Serializable {

	private Long activityId;

	private List<AmpActivity> activities;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public List<AmpActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<AmpActivity> activities) {
		this.activities = activities;
	}
}