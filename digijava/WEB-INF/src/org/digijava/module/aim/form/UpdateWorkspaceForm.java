package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.module.aim.dbentity.AmpTeam;

public class UpdateWorkspaceForm extends ValidatorForm {

	private String id = null;
	private String teamName = null;
	private String description = null;
	private Long teamId = null;
	private String teamLead = null;
	private String actionEvent = null;
	private String category = null;				// 'DONOR' or 'MOFED', added for Donor-access
	private Long relatedTeam = null;			// MOFED team mapped to DONOR team
	private String relatedTeamName = null;
	// Available bilateral mofed-teams for mapping with donor-team
	private Collection relatedTeamBilatColl = new ArrayList();
	// Available multilateral mofed-teams for mapping with donor-team
	private Collection relatedTeamMutilatColl = new ArrayList();
	private String relatedTeamFlag = "no";		// 'yes', means relatedTeamColl collection is loaded, 'no' otherwise
	private String type = null;					// 'Multilateral' or 'Bilateral'
	private Integer relatedTeamBilatCollSize = null;
	private String deleteFlag = null;
	private boolean updateFlag = false;
	
	private String workspaceType = null;		// 'Team' or 'Management'
	private Collection childWorkspaces;

	private boolean addFlag = false;
	private boolean reset;

	private String mainAction;
	
	// for select child workspaces popup
	private String childWorkspaceType;
	private String childTeamCategory;
	private Collection availChildWorkspaces;
	private Long[] selChildWorkspaces;
	private boolean popupReset;
	private String dest;

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

	/**
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return Returns the relatedTeam.
	 */
	public Long getRelatedTeam() {
		return relatedTeam;
	}
	/**
	 * @param relatedTeam The relatedTeam to set.
	 */
	public void setRelatedTeam(Long relatedTeam) {
		this.relatedTeam = relatedTeam;
	}
	
	/**
	 * @return Returns the relatedTeamName.
	 */
	public String getRelatedTeamName() {
		return relatedTeamName;
	}
	/**
	 * @param relatedTeamName The relatedTeamName to set.
	 */
	public void setRelatedTeamName(String relatedTeamName) {
		this.relatedTeamName = relatedTeamName;
	}
	/**
	 * @return Returns the relatedTeamBilatColl.
	 */
	public Collection getRelatedTeamBilatColl() {
		return relatedTeamBilatColl;
	}
	/**
	 * @param relatedTeamBilatColl The relatedTeamBilatColl to set.
	 */
	public void setRelatedTeamBilatColl(Collection relatedTeamBilatColl) {
		this.relatedTeamBilatColl = relatedTeamBilatColl;
	}
	/**
	 * @return Returns the relatedTeamMutilatColl.
	 */
	public Collection getRelatedTeamMutilatColl() {
		return relatedTeamMutilatColl;
	}
	/**
	 * @param relatedTeamMutilatColl The relatedTeamMutilatColl to set.
	 */
	public void setRelatedTeamMutilatColl(Collection relatedTeamMutilatColl) {
		this.relatedTeamMutilatColl = relatedTeamMutilatColl;
	}
	
	/**
	 * @return Returns the relatedTeamFlag.
	 */
	public String getRelatedTeamFlag() {
		return relatedTeamFlag;
	}
	/**
	 * @param relatedTeamFlag The relatedTeamFlag to set.
	 */
	public void setRelatedTeamFlag(String relatedTeamFlag) {
		this.relatedTeamFlag = relatedTeamFlag;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the relatedTeamBilatCollSize.
	 */
	public Integer getRelatedTeamBilatCollSize() {
		return relatedTeamBilatCollSize;
	}
	/**
	 * @param relatedTeamBilatCollSize The relatedTeamBilatCollSize to set.
	 */
	public void setRelatedTeamBilatCollSize(Integer relatedTeamBilatCollSize) {
		this.relatedTeamBilatCollSize = relatedTeamBilatCollSize;
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
	
	/**
	 * @return Returns the addFlag.
	 */
	public boolean isAddFlag() {
		return addFlag;
	}
	/**
	 * @param addFlag The addFlag to set.
	 */
	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
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
	 * @return Returns the dest.
	 */
	public String getDest() {
		return dest;
	}
	/**
	 * @param dest The dest to set.
	 */
	public void setDest(String dest) {
		this.dest = dest;
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
			dest = null;
			
			category = null;
			relatedTeam = null;
			relatedTeamName = null;
			relatedTeamBilatColl = new ArrayList();
			relatedTeamMutilatColl = new ArrayList();
			relatedTeamFlag = "no";
			type = null;
			relatedTeamBilatCollSize = null;
			deleteFlag = null;
			updateFlag = false;
			workspaceType = null;
			
			addFlag = false;
			reset	= false;
		}		
		if (popupReset) {
			availChildWorkspaces = null;
			selChildWorkspaces = null;			
		}
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = null;
		
		if ("no".equals(relatedTeamFlag)) {
			errors = super.validate(mapping, request);
			
			if ("DONOR".equalsIgnoreCase(category) && "Team".equalsIgnoreCase(workspaceType)) {
				if ("edit".equalsIgnoreCase(actionEvent)) {
					if ("Bilateral".equalsIgnoreCase(type)) {
						if (relatedTeamBilatColl.size() > 0 )
							if (null == relatedTeam || "-1".equals(relatedTeam) || relatedTeam.toString().trim().length() < 1) {
								ActionError error = new ActionError("error.aim.updateWorkspace.noRelatedTeam");
								errors.add("relatedTeam", error);
								relatedTeamFlag = "set";
							}
					}
					if ("Multilateral".equalsIgnoreCase(type)) {
						if (relatedTeamMutilatColl.size() > 0)
							if (null == relatedTeam || "-1".equals(relatedTeam) || relatedTeam.toString().trim().length() < 1) {
								ActionError error = new ActionError("error.aim.updateWorkspace.noRelatedTeam");
								errors.add("relatedTeam", error);
								relatedTeamFlag = "set";
							}
					}
				}
				else if ("add".equalsIgnoreCase(actionEvent)) {
					if (relatedTeamBilatColl.size() > 0 && relatedTeamMutilatColl.size() > 0) {
						if (null == type || "-1".equals(type) || type.trim().length() < 1) {
							ActionError error = new ActionError("error.aim.updateWorkspace.noTeamType");
							errors.add("type", error);
						}
						else if (null == relatedTeam || "-1".equals(relatedTeam) || relatedTeam.toString().trim().length() < 1) {
							ActionError error = new ActionError("error.aim.updateWorkspace.noRelatedTeam");
							errors.add("relatedTeam", error);
						}
						relatedTeamFlag = "set";
					}
				}
			}
		}
		return errors;
	}
}
