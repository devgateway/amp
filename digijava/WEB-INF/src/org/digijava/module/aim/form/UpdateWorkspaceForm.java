package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class UpdateWorkspaceForm extends ValidatorForm {

	private String id = null;
	private String teamName = null;
	private String description = null;
	private Long teamId = null;
	private String teamLead = null;
	private String actionEvent = null;
	private String type = null;
	private String deleteFlag = null;
	private boolean updateFlag = false;
	
	private String workspaceType;
	private Collection childWorkspaces;

	private boolean reset;

	private String mainAction;
	
	// for select child workspaces popup
	private String childWorkspaceType;
	private String childTeamCategory;
	private Collection availChildWorkspaces;
	private Long[] selChildWorkspaces;
	private boolean popupReset;

	public boolean getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean flag) {
		updateFlag = flag;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(String teamLead) {
		this.teamLead = teamLead;
	}

	public String getActionEvent() {
		return actionEvent;
	}

	public void setActionEvent(String actionEvent) {
		this.actionEvent = actionEvent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		if (teamName != null || description != null) {
			return super.validate(mapping, request);
		} else
			return null;
	}

	/**
	 * @return Returns the availChildWorkspaces.
	 */
	public Collection getAvailChildWorkspaces() {
		return availChildWorkspaces;
	}
	/**
	 * @param availChildWorkspaces The availChildWorkspaces to set.
	 */
	public void setAvailChildWorkspaces(Collection availChildWorkspaces) {
		this.availChildWorkspaces = availChildWorkspaces;
	}
	/**
	 * @return Returns the childTeamCategory.
	 */
	public String getChildTeamCategory() {
		return childTeamCategory;
	}
	/**
	 * @param childTeamCategory The childTeamCategory to set.
	 */
	public void setChildTeamCategory(String childTeamCategory) {
		this.childTeamCategory = childTeamCategory;
	}
	/**
	 * @return Returns the childWorkspaces.
	 */
	public Collection getChildWorkspaces() {
		return childWorkspaces;
	}
	/**
	 * @param childWorkspaces The childWorkspaces to set.
	 */
	public void setChildWorkspaces(Collection childWorkspaces) {
		this.childWorkspaces = childWorkspaces;
	}
	/**
	 * @return Returns the childWorkspaceType.
	 */
	public String getChildWorkspaceType() {
		return childWorkspaceType;
	}
	/**
	 * @param childWorkspaceType The childWorkspaceType to set.
	 */
	public void setChildWorkspaceType(String childWorkspaceType) {
		this.childWorkspaceType = childWorkspaceType;
	}
	/**
	 * @return Returns the selChildWorkspaces.
	 */
	public Long[] getSelChildWorkspaces() {
		return selChildWorkspaces;
	}
	/**
	 * @param selChildWorkspaces The selChildWorkspaces to set.
	 */
	public void setSelChildWorkspaces(Long[] selChildWorkspaces) {
		this.selChildWorkspaces = selChildWorkspaces;
	}
	/**
	 * @return Returns the workspaceType.
	 */
	public String getWorkspaceType() {
		return workspaceType;
	}
	/**
	 * @param workspaceType The workspaceType to set.
	 */
	public void setWorkspaceType(String workspaceType) {
		this.workspaceType = workspaceType;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest request) {
		if (reset) {
			id = null;
			teamName = null;
			description = null;
			teamId = null;
			teamLead = null;
			actionEvent = null;
			type = null;
			deleteFlag = null;
			updateFlag = false;
			workspaceType = null;
			childWorkspaces = null;
			popupReset = true;
			mainAction = null;
		}		
		if (popupReset) {
			childWorkspaceType = null;
			childTeamCategory = null;
			availChildWorkspaces = null;
			selChildWorkspaces = null;			
		}
	}
	/**
	 * @return Returns the popupReset.
	 */
	public boolean isPopupReset() {
		return popupReset;
	}
	/**
	 * @param popupReset The popupReset to set.
	 */
	public void setPopupReset(boolean popupReset) {
		this.popupReset = popupReset;
	}
	/**
	 * @return Returns the reset.
	 */
	public boolean isReset() {
		return reset;
	}
	/**
	 * @param reset The reset to set.
	 */
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getMainAction() {
		return mainAction;
	}

	public void setMainAction(String mainAction) {
		this.mainAction = mainAction;		  
	}
}
