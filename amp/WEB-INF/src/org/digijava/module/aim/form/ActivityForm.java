package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.*;
import java.io.Serializable;



public class ActivityForm extends ActionForm implements Serializable
{
	private Collection activityList;

	/**
	 * @return Returns the activityList.
	 */
	public Collection getActivityList() {
		return activityList;
	}

	/**
	 * @param activityList The activityList to set.
	 */
	public void setActivityList(Collection activityList) {
		this.activityList = activityList;
	}
	
}