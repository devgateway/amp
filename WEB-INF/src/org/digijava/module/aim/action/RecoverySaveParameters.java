package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.TeamMember;

/**
 * The RecoverySave method needed fewer parameters and the ability to
 * substitute the activity parameter
 * 
 * @author arty
 */

public class RecoverySaveParameters {
	private int noOfSteps;
	private String stepText[];
	private boolean stepFailure[];
	private EditActivityForm eaForm;
	private TeamMember tm;
	private AmpActivity activity;
	private boolean createdAsDraft;
	private ActionErrors errors;
	private HttpServletRequest request;
	private HttpSession session;
	private Long field;
	private Collection relatedLinks;
	private Set<Components<AmpComponentFunding>> tempComp;
	private boolean alwaysRollback;
	private Long oldActivityId;
	private boolean edit;
	
	public Long getOldActivityId() {
		return oldActivityId;
	}
	public void setOldActivityId(Long oldActivityId) {
		this.oldActivityId = oldActivityId;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public int getNoOfSteps() {
		return noOfSteps;
	}
	public void setNoOfSteps(int noOfSteps) {
		this.noOfSteps = noOfSteps;
	}
	public String[] getStepText() {
		return stepText;
	}
	public void setStepText(String[] stepText) {
		this.stepText = stepText;
	}
	public boolean[] getStepFailure() {
		return stepFailure;
	}
	public void setStepFailure(boolean[] stepFailure) {
		this.stepFailure = stepFailure;
	}
	public EditActivityForm getEaForm() {
		return eaForm;
	}
	public void setEaForm(EditActivityForm eaForm) {
		this.eaForm = eaForm;
	}
	public TeamMember getTm() {
		return tm;
	}
	public void setTm(TeamMember tm) {
		this.tm = tm;
	}
	public AmpActivity getActivity() {
		return activity;
	}
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	public boolean isCreatedAsDraft() {
		return createdAsDraft;
	}
	public void setCreatedAsDraft(boolean createdAsDraft) {
		this.createdAsDraft = createdAsDraft;
	}
	public ActionErrors getErrors() {
		return errors;
	}
	public void setErrors(ActionErrors errors) {
		this.errors = errors;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public Long getField() {
		return field;
	}
	public void setField(Long field) {
		this.field = field;
	}
	public Collection getRelatedLinks() {
		return relatedLinks;
	}
	public void setRelatedLinks(Collection relatedLinks) {
		this.relatedLinks = relatedLinks;
	}
	public Set<Components<AmpComponentFunding>> getTempComp() {
		return tempComp;
	}
	public void setTempComp(Set<Components<AmpComponentFunding>> tempComp) {
		this.tempComp = tempComp;
	}
	public boolean isAlwaysRollback() {
		return alwaysRollback;
	}
	public void setAlwaysRollback(boolean alwaysRollback) {
		this.alwaysRollback = alwaysRollback;
	}
}

/*
AmpActivity activity, Long oldActivityId,
boolean edit,
ArrayList commentsCol, boolean serializeFlag,
Long field,
Collection relatedLinks, Long memberId,
Collection indicators, Set<Components<AmpComponentFunding>> componentsFunding, 
List<IPAContract> contracts, boolean alwaysRollback
*/