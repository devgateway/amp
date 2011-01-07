package org.digijava.module.visualization.form;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.visualization.helper.DashboardFilter;

public class VisualizationForm extends ActionForm {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private DashboardFilter filter = new DashboardFilter();
	private SummaryInformation summaryInformation;
	private RanksInformation ranksInformation;

	public void setFilter(DashboardFilter filter) {
		this.filter = filter;
	}

	public DashboardFilter getFilter() {
		return filter;
	}
	

	public void setSummaryInformation(SummaryInformation summaryInformation) {
		this.summaryInformation = summaryInformation;
	}

	public SummaryInformation getSummaryInformation() {
		if(this.summaryInformation==null){
			this.summaryInformation=new SummaryInformation();
		}
		return this.summaryInformation;
	}

	public class SummaryInformation {
		private BigDecimal totalCommitments; 
		private BigDecimal totalDisbursements; 
		private Integer numberOfProjects; 
		private Integer numberOfSectors; 
		private Integer numberOfRegions; 
		private BigDecimal averageProjectSize;

		public BigDecimal getTotalCommitments() {
			return totalCommitments;
		}
		public void setTotalCommitments(BigDecimal totalCommitments) {
			this.totalCommitments = totalCommitments;
		}
		public BigDecimal getTotalDisbursements() {
			return totalDisbursements;
		}
		public void setTotalDisbursements(BigDecimal totalDisbursements) {
			this.totalDisbursements = totalDisbursements;
		}
		public Integer getNumberOfProjects() {
			return numberOfProjects;
		}
		public void setNumberOfProjects(Integer numberOfProjects) {
			this.numberOfProjects = numberOfProjects;
		}
		public Integer getNumberOfSectors() {
			return numberOfSectors;
		}
		public void setNumberOfSectors(Integer numberOfSectors) {
			this.numberOfSectors = numberOfSectors;
		}
		public Integer getNumberOfRegions() {
			return numberOfRegions;
		}
		public void setNumberOfRegions(Integer numberOfRegions) {
			this.numberOfRegions = numberOfRegions;
		}
		public BigDecimal getAverageProjectSize() {
			return averageProjectSize;
		}
		public void setAverageProjectSize(BigDecimal averageProjectSize) {
			this.averageProjectSize = averageProjectSize;
		} 

	}
	
	public void setRanksInformation(RanksInformation ranksInformation) {
		this.ranksInformation = ranksInformation;
	}

	public RanksInformation getRanksInformation() {
		if(this.ranksInformation==null){
			this.ranksInformation=new RanksInformation();
		}
		return this.ranksInformation;
	}

	public class RanksInformation {
		private Map<AmpOrganisation, BigDecimal> topDonors;
		private Map<AmpOrganisation, BigDecimal> fullDonors;
		private Map<AmpActivity, BigDecimal> topProjects;
		private Map<AmpActivity, BigDecimal> fullProjects;
		private Map<AmpCategoryValueLocations, BigDecimal> topRegions;
		private Map<AmpCategoryValueLocations, BigDecimal> fullRegions;
		
		public Map<AmpOrganisation, BigDecimal> getTopDonors() {
			return topDonors;
		}
		public void setTopDonors(Map<AmpOrganisation, BigDecimal> topDonors) {
			this.topDonors = topDonors;
		}
		public Map<AmpOrganisation, BigDecimal> getFullDonors() {
			return fullDonors;
		}
		public void setFullDonors(Map<AmpOrganisation, BigDecimal> fullDonors) {
			this.fullDonors = fullDonors;
		}
		public Map<AmpActivity, BigDecimal> getTopProjects() {
			return topProjects;
		}
		public void setTopProjects(Map<AmpActivity, BigDecimal> topProjects) {
			this.topProjects = topProjects;
		}
		public Map<AmpActivity, BigDecimal> getFullProjects() {
			return fullProjects;
		}
		public void setFullProjects(Map<AmpActivity, BigDecimal> fullProjects) {
			this.fullProjects = fullProjects;
		}
		public Map<AmpCategoryValueLocations, BigDecimal> getTopRegions() {
			return topRegions;
		}
		public void setTopRegions(Map<AmpCategoryValueLocations, BigDecimal> topRegions) {
			this.topRegions = topRegions;
		}
		public Map<AmpCategoryValueLocations, BigDecimal> getFullRegions() {
			return fullRegions;
		}
		public void setFullRegions(
				Map<AmpCategoryValueLocations, BigDecimal> fullRegions) {
			this.fullRegions = fullRegions;
		}
		
	}

}
