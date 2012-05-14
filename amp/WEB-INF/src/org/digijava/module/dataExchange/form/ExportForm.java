package org.digijava.module.dataExchange.form;



import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
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
	
	//DbUtil.getAllOrgTypesOfPortfolio();
	private Collection<AmpOrgType> donorTypeList = null;
	private Long[] donorTypeSelected = null;

	//ARUtil.filterDonorGroups(DbUtil.getAllOrgGroupsOfPortfolio());
	private Collection<AmpOrgGroup> donorGroupList = null;
	private Long[] donorGroupSelected = null;
	
	//ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR)
	private Collection<AmpOrganisation> donorAgencyList = null;
	private Long[] donorAgencySelected = null;
	
	//SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);
	private Collection<AmpSector> primarySectorsList = null; 
	private Long[] primarySectorsSelected = null;

	//SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);
	private Collection<AmpSector> secondarySectorsList =  null;
	private Long[] secondarySectorsSelected = null;
	
	private String language  = null;
	
	private Collection<String[]> exportLog = null;
	
	public ExportForm(){
	
	}
	
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		super.reset(mapping, request);
		
		setSelectedTeamId(null);
		setDonorTypeSelected(null);
		setDonorGroupSelected(null);
		setDonorAgencySelected(null);
		setPrimarySectorsSelected(null);
		setSecondarySectorsSelected(null);	
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


	public Collection<AmpOrgType> getDonorTypeList() {
		return donorTypeList;
	}


	public void setDonorTypeList(Collection<AmpOrgType> donorTypeList) {
		this.donorTypeList = donorTypeList;
	}


	public Collection<AmpOrgGroup> getDonorGroupList() {
		return donorGroupList;
	}


	public void setDonorGroupList(Collection<AmpOrgGroup> donorGroupList) {
		this.donorGroupList = donorGroupList;
	}


	public Collection<AmpOrganisation> getDonorAgencyList() {
		return donorAgencyList;
	}


	public void setDonorAgencyList(Collection<AmpOrganisation> donorAgencyList) {
		this.donorAgencyList = donorAgencyList;
	}


	public Collection<AmpSector> getPrimarySectorsList() {
		return primarySectorsList;
	}


	public void setPrimarySectorsList(Collection<AmpSector> primarySectorsList) {
		this.primarySectorsList = primarySectorsList;
	}


	public Collection<AmpSector> getSecondarySectorsList() {
		return secondarySectorsList;
	}


	public void setSecondarySectorsList(Collection<AmpSector> secondarySectorsList) {
		this.secondarySectorsList = secondarySectorsList;
	}

	public Long[] getDonorTypeSelected() {
		return donorTypeSelected;
	}

	public void setDonorTypeSelected(Long[] donorTypeSelected) {
		this.donorTypeSelected = donorTypeSelected;
	}


	public Long[] getDonorGroupSelected() {
		return donorGroupSelected;
	}


	public void setDonorGroupSelected(Long[] donorGroupSelected) {
		this.donorGroupSelected = donorGroupSelected;
	}


	public Long[] getDonorAgencySelected() {
		return donorAgencySelected;
	}


	public void setDonorAgencySelected(Long[] donorAgencySelected) {
		this.donorAgencySelected = donorAgencySelected;
	}


	public Long[] getPrimarySectorsSelected() {
		return primarySectorsSelected;
	}


	public void setPrimarySectorsSelected(Long[] primarySectorsSelected) {
		this.primarySectorsSelected = primarySectorsSelected;
	}


	public Long[] getSecondarySectorsSelected() {
		return secondarySectorsSelected;
	}


	public void setSecondarySectorsSelected(Long[] secondarySectorsSelected) {
		this.secondarySectorsSelected = secondarySectorsSelected;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public Collection<String[]> getExportLog() {
		return exportLog;
	}


	public void setExportLog(Collection<String[]> exportLog) {
		this.exportLog = exportLog;
	}

	public void addExportLog(String[] errorLog) {
		if (this.exportLog == null){
			this.exportLog = new ArrayList<String[]>();
		}
		this.exportLog.add(errorLog);
	}
	
	public boolean isError(){
		return this.exportLog != null;
	}
}

