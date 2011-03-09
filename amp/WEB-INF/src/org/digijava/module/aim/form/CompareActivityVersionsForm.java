package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.dbentity.AmpActivity;

public class CompareActivityVersionsForm extends ActionForm {

	private Long activityOneId;

	private Long activityTwoId;

	private List<CompareOutput> outputCollection;

	private AmpActivity activityOne;

	private AmpActivity activityTwo;

	private boolean showMergeColumn;

	private String method;

	private AmpActivity oldActivity;

	private String[] mergedValues = new String[] {};

	public Long getActivityOneId() {
		return activityOneId;
	}

	public void setActivityOneId(Long activityOneId) {
		this.activityOneId = activityOneId;
	}

	public Long getActivityTwoId() {
		return activityTwoId;
	}

	public void setActivityTwoId(Long activityTwoId) {
		this.activityTwoId = activityTwoId;
	}

	public AmpActivity getActivityOne() {
		return activityOne;
	}

	public void setActivityOne(AmpActivity activityOne) {
		this.activityOne = activityOne;
	}

	public AmpActivity getActivityTwo() {
		return activityTwo;
	}

	public void setActivityTwo(AmpActivity activityTwo) {
		this.activityTwo = activityTwo;
	}

	public List<CompareOutput> getOutputCollection() {
		return outputCollection;
	}

	public void setOutputCollection(List<CompareOutput> outputCollection) {
		this.outputCollection = outputCollection;
	}

	public boolean isShowMergeColumn() {
		return showMergeColumn;
	}

	public void setShowMergeColumn(boolean showMergeColumn) {
		this.showMergeColumn = showMergeColumn;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public AmpActivity getOldActivity() {
		return oldActivity;
	}

	public void setOldActivity(AmpActivity oldActivity) {
		this.oldActivity = oldActivity;
	}

	public void setMergedValues(String[] mergedValues) {
		this.mergedValues = mergedValues;
	}

	public String[] getMergedValues() {
		return mergedValues;
	}
}