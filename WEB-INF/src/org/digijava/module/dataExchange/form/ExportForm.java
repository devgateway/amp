package org.digijava.module.dataExchange.form;



import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

public class ExportForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private AmpColumnEntry activityTree = null;
	
	private Collection<AmpTeam> teamList = null;	

	private Long selectedTeamId = null;
	
	public ExportForm(){
	
	}


	public AmpColumnEntry getActivityTree() {
		return activityTree;
	}


	public void setActivityTree(AmpColumnEntry activityTree) {
		this.activityTree = activityTree;
	}


	public Collection<AmpTeam> getTeamList() {
		return teamList;
	}


	public void setTeamList(Collection<AmpTeam> teamList) {
		this.teamList = teamList;
	}


	public Long getSelectedTeamId() {
		return selectedTeamId;
	}


	public void setSelectedTeamId(Long selectedTeamId) {
		this.selectedTeamId = selectedTeamId;
	}

	
	
	
}

