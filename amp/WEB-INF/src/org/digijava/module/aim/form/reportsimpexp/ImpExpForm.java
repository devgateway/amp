/**
 * 
 */
package org.digijava.module.aim.form.reportsimpexp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants;

/**
 * @author Alex Gartner
 *
 */
public class ImpExpForm extends ActionForm {
	
		private List<AmpReports> reportsList						= null;
		private String includedJsp											= null;
		
		/**
		 * This one has no role. Just here to fool and html:select tag
		 */
		private Object[] fakeIds												= null;
		
		/* Export Fields */
		private boolean showTabs											= false;
		
		private Map<Long, AmpReports> selectedReports		= null;
		private Long[] selectedReportIds								= null;
		private String selectedReportsString							= "";
		private Long[] displayedReportIds								= null;
		
		private Set<KeyValue> availableTeams						= null;
		private Long[] selectedTeamIds									= null;
		
		private Set<KeyValue> availableUsers						= null;
		private Long[] selectedUserIds									= null;
		
		private String exportReportsClass								= ReportsImpExpConstants.CSS_CLASS_ENABLED;
		private String exportTabsClass									= ReportsImpExpConstants.CSS_CLASS_ENABLED;
		
		private String exportReportsAction							= ReportsImpExpConstants.ACTION_NEW;
		private String exportTabsAction									= ReportsImpExpConstants.ACTION_NEW;
		
		/* Import Fields */
		private FormFile formFileReports								= null;
		private FormFile formFileTabs									= null;
		private Integer [] importReportIndexes						= null;
		private Long [] importTeamIds									= null;
		private Set<KeyValue> allAvailableTeams					= null;
		
		private String importClass											= ReportsImpExpConstants.CSS_CLASS_ENABLED;
		@Override
		public void reset(ActionMapping mapping, HttpServletRequest request) {
			
			String action		= request.getParameter(ReportsImpExpConstants.ACTION);
			if ( ReportsImpExpConstants.ACTION_NEW.equals(action) ) {
				this.reportsList													= null;
				this.selectedReports											= null;
				this.selectedReportsString									= "";
				this.selectedReportIds										= null;
				
				this.selectedReports											= null;
				this.selectedTeamIds											= null;
				this.selectedUserIds											= null;
				this.displayedReportIds										= null;
				
				this.exportReportsClass									= ReportsImpExpConstants.CSS_CLASS_ENABLED;
				this.exportTabsClass										= ReportsImpExpConstants.CSS_CLASS_ENABLED;
				this.importClass											= ReportsImpExpConstants.CSS_CLASS_ENABLED;
				
				this.exportReportsAction								= ReportsImpExpConstants.ACTION_NEW;
				this.exportTabsAction									= ReportsImpExpConstants.ACTION_NEW;
				
				
				this.formFileTabs												= null;
				this.formFileReports											= null;
				this.importReportIndexes										= null;
				this.importTeamIds											= null;
			}
			if ( ReportsImpExpConstants.ACTION_SELECTION_STEP.equals(action) ) {
				this.selectedTeamIds											= null;
				this.selectedUserIds											= null;
				this.displayedReportIds										= null;
			}
			
		}

		/**
		 * @return the reportsList
		 */
		public List<AmpReports> getReportsList() {
			return reportsList;
		}

		/**
		 * @param reportsList the reportsList to set
		 */
		public void setReportsList(List<AmpReports> reportsList) {
			this.reportsList = reportsList;
		}

		/**
		 * @return the showTabs
		 */
		public boolean getShowTabs() {
			return showTabs;
		}

		/**
		 * @param showTabs the showTabs to set
		 */
		public void setShowTabs(boolean showTabs) {
			this.showTabs = showTabs;
		}

		/**
		 * @return the availableTeams
		 */
		public Set<KeyValue> getAvailableTeams() {
			return availableTeams;
		}

		/**
		 * @param availableTeams the availableTeams to set
		 */
		public void setAvailableTeams(Set<KeyValue> availableTeams) {
			this.availableTeams = availableTeams;
		}

		/**
		 * @return the availableUsers
		 */
		public Set<KeyValue> getAvailableUsers() {
			return availableUsers;
		}

		/**
		 * @param availableUsers the availableUsers to set
		 */
		public void setAvailableUsers(Set<KeyValue> availableUsers) {
			this.availableUsers = availableUsers;
		}
		/**
		 * @return the selectedTeamIds
		 */
		public Long[] getSelectedTeamIds() {
			return selectedTeamIds;
		}

		/**
		 * @param selectedTeamIds the selectedTeamIds to set
		 */
		public void setSelectedTeamIds(Long[] selectedTeamIds) {
			this.selectedTeamIds = selectedTeamIds;
		}

		/**
		 * @return the selectedUserIds
		 */
		public Long[] getSelectedUserIds() {
			return selectedUserIds;
		}

		/**
		 * @param selectedUserIds the selectedUserIds to set
		 */
		public void setSelectedUserIds(Long[] selectedUserIds) {
			this.selectedUserIds = selectedUserIds;
		}

		/**
		 * @return the exportReportsClass
		 */
		public String getExportReportsClass() {
			return exportReportsClass;
		}

		/**
		 * @param exportReportsClass the exportReportsClass to set
		 */
		public void setExportReportsClass(String exportReportsClass) {
			this.exportReportsClass = exportReportsClass;
		}

		

		/**
		 * @return the exportTabsClass
		 */
		public String getExportTabsClass() {
			return exportTabsClass;
		}

		/**
		 * @param exportTabsClass the exportTabsClass to set
		 */
		public void setExportTabsClass(String exportTabsClass) {
			this.exportTabsClass = exportTabsClass;
		}

		/**
		 * @return the importClass
		 */
		public String getImportClass() {
			return importClass;
		}

		/**
		 * @param importClass the importClass to set
		 */
		public void setImportClass(String importClass) {
			this.importClass = importClass;
		}

		/**
		 * @return the exportReportsAction
		 */
		public String getExportReportsAction() {
			return exportReportsAction;
		}

		/**
		 * @param exportReportsAction the exportReportsAction to set
		 */
		public void setExportReportsAction(String exportReportsAction) {
			this.exportReportsAction = exportReportsAction;
		}

		/**
		 * @return the exportTabsAction
		 */
		public String getExportTabsAction() {
			return exportTabsAction;
		}

		/**
		 * @param exportTabsAction the exportTabsAction to set
		 */
		public void setExportTabsAction(String exportTabsAction) {
			this.exportTabsAction = exportTabsAction;
		}

		

		/**
		 * @return the selectedReports
		 */
		public Map<Long, AmpReports> getSelectedReports() {
			return selectedReports;
		}

		/**
		 * @param selectedReports the selectedReports to set
		 */
		public void setSelectedReports(Map<Long, AmpReports> selectedReports) {
			this.selectedReports = selectedReports;
		}

		/**
		 * @return the selectedReportIds
		 */
		public Long[] getSelectedReportIds() {
			return selectedReportIds;
		}

		/**
		 * @param selectedReportIds the selectedReportIds to set
		 */
		public void setSelectedReportIds(Long[] selectedReportIds) {
			this.selectedReportIds = selectedReportIds;
		}

		/**
		 * @return the selectedReportsString
		 */
		public String getSelectedReportsString() {
			return selectedReportsString;
		}

		/**
		 * @param selectedReportsString the selectedReportsString to set
		 */
		public void setSelectedReportsString(String selectedReportsString) {
			this.selectedReportsString = selectedReportsString;
		}

		/**
		 * @return the displayedReportIds
		 */
		public Long[] getDisplayedReportIds() {
			return displayedReportIds;
		}

		/**
		 * @param displayedReportIds the displayedReportIds to set
		 */
		public void setDisplayedReportIds(Long[] displayedReportIds) {
			this.displayedReportIds = displayedReportIds;
		}


		/**
		 * @return the formFileReports
		 */
		public FormFile getFormFileReports() {
			return formFileReports;
		}

		/**
		 * @param formFileReports the formFileReports to set
		 */
		public void setFormFileReports(FormFile formFileReports) {
			this.formFileReports = formFileReports;
		}

		/**
		 * @return the formFileTabs
		 */
		public FormFile getFormFileTabs() {
			return formFileTabs;
		}

		/**
		 * @param formFileTabs the formFileTabs to set
		 */
		public void setFormFileTabs(FormFile formFileTabs) {
			this.formFileTabs = formFileTabs;
		}

	

		/**
		 * @return the importReportIndexes
		 */
		public Integer[] getImportReportIndexes() {
			return importReportIndexes;
		}

		/**
		 * @param importReportIndexes the importReportIndexes to set
		 */
		public void setImportReportIndexes(Integer[] importReportIndexes) {
			this.importReportIndexes = importReportIndexes;
		}

		/**
		 * @return the importTeamIds
		 */
		public Long[] getImportTeamIds() {
			return importTeamIds;
		}

		/**
		 * @param importTeamIds the importTeamIds to set
		 */
		public void setImportTeamIds(Long[] importTeamIds) {
			this.importTeamIds = importTeamIds;
		}

		/**
		 * @return the allAvailableTeams
		 */
		public Set<KeyValue> getAllAvailableTeams() {
			return allAvailableTeams;
		}

		/**
		 * @param allAvailableTeams the allAvailableTeams to set
		 */
		public void setAllAvailableTeams(Set<KeyValue> allAvailableTeams) {
			this.allAvailableTeams = allAvailableTeams;
		}

		/**
		 * @return the includedJsp
		 */
		public String getIncludedJsp() {
			return includedJsp;
		}

		/**
		 * @param includedJsp the includedJsp to set
		 */
		public void setIncludedJsp(String includedJsp) {
			this.includedJsp = includedJsp;
		}

		/**
		 * @return the fakeIds
		 */
		public Object[] getFakeIds() {
			return fakeIds;
		}

		/**
		 * @param fakeIds the fakeIds to set
		 */
		public void setFakeIds(Object[] fakeIds) {
			this.fakeIds = fakeIds;
		}
		
		
		
}
