package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

public class ViewActivityHistoryForm extends ActionForm implements Serializable {

	private Long activityId;
	private Boolean enableadvanceoptions;
	private List<AmpActivityVersion> activities;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public List<AmpActivityVersion> getActivities() {
		return activities;
	}

	public void setActivities(List<AmpActivityVersion> activities) {
		this.activities = activities;
	}

	public Boolean getEnableadvanceoptions() {
		return enableadvanceoptions;
	}

	public void setEnableadvanceoptions(Boolean enableadvanceoptions) {
		this.enableadvanceoptions = enableadvanceoptions;
	}

}