/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores activities references paths from the current field downwards
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityRefPath {
	private final Map<String, ActivityRefPath> activityRefsFromField = new HashMap<String, ActivityRefPath>();
	private String activityField = null;
	
	public ActivityRefPath() {
	}
	
	public boolean hasActivityRef() {
		return activityField != null;
	}
	
	public void addActivityRefPath(String fieldName, ActivityRefPath refPath) {
		activityRefsFromField.put(fieldName, refPath);
	}
	
	public Map<String, ActivityRefPath> getRefPaths() {
		return activityRefsFromField;
	}

	/**
	 * @return the activityField
	 */
	public String getActivityField() {
		return activityField;
	}

	/**
	 * @param activityField the activityField to set
	 */
	public void setActivityField(String activityField) {
		this.activityField = activityField;
	}
}
