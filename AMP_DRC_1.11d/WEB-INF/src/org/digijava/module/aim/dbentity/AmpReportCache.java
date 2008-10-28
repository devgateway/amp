package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;

public class AmpReportCache {

		  private Long ampReportId;
		  private Long ampActivityId;
		  private String ampId;
		  private Long ampModalityId;
		  private Long ampTeamId;
		  private Long ampDonorId;
		  private Long ampStatusId;
		  private String activityName;
		  private String modalityName;
		  private String termAssistName;
		  private String donorName;
		  private String orgType;
		  private String statusName;
		  private Long ampFundingId;
		  private Double plannedCommitment;
		  private Double plannedDisbursement;
		  private Double plannedExpenditure;
		  private Double actualCommitment;
		  private Double actualDisbursement;
		  private Double actualExpenditure;
		  private String currencyCode;
		  private Date actualStartDate;
		  private Date actualCompletionDate;
		  private Date plannedStartDate;
		  private Date plannedCompletionDate;
		  private Integer fiscalYear;
		  private Integer fiscalQuarter;
		  private Date transactionDate;
		  private Long ampLevelId;
		  private String levelName;
		  private String activityDescription;
		  private Long ampComponentId;
		  private String componentName;
		  private Long ampRegionId;
		  private String regionName;
		  private Long reportType;

		  
		  public Long getAmpActivityId() {
					 return ampActivityId;
		  }

		  public String getAmpId() {
					 return ampId;
		  }

		  public Long getAmpReportId() {
					 return ampReportId;
		  }

		  public Long getAmpTeamId() {
					 return ampTeamId;
		  }

		  public Long getAmpModalityId() {
					 return ampModalityId;
		  }

		  public Long getAmpDonorId() {
					 return ampDonorId;
		  }

		  public Long getAmpStatusId() {
					 return ampStatusId;
		  }

		  public String getActivityName() {
					 return activityName;
		  }

		  public String getModalityName() {
					 return modalityName;
		  }

		  public String getTermAssistName() {
					 return termAssistName;
		  }

		  public String getDonorName() {
					 return donorName;
		  }

		  public String getStatusName() {
					 return statusName;
		  }

		  
		  public Long getAmpFundingId() {
					 return ampFundingId;
		  }

		  public Double getPlannedCommitment() {
					 return FeaturesUtil.applyThousandsForVisibility(plannedCommitment);
		  }

		  public Double getPlannedDisbursement() {
					 return FeaturesUtil.applyThousandsForVisibility(plannedDisbursement);
		  }

		  public Double getPlannedExpenditure() {
					 return FeaturesUtil.applyThousandsForVisibility(plannedExpenditure);
		  }

		  public Double getActualCommitment() {
					 return FeaturesUtil.applyThousandsForVisibility(actualCommitment);
		  }

		  public Double getActualDisbursement() {
					 return FeaturesUtil.applyThousandsForVisibility(actualDisbursement);
		  }

		  public Double getActualExpenditure() {
					 return FeaturesUtil.applyThousandsForVisibility(actualExpenditure);
		  }

		  public String getCurrencyCode() {
					 return currencyCode;
		  }

		  public Integer getFiscalYear() {
					 return fiscalYear;
		  }

		public Integer getFiscalQuarter() {
			 return fiscalQuarter;
		}

	    public Date getActualCompletionDate() {
			return actualCompletionDate;
		}

		public Date getTransactionDate() {
			return transactionDate;
		}

		public Date getActualStartDate() {
			return actualStartDate;
		}

		public Date getPlannedCompletionDate() {
			return plannedCompletionDate;
			}

			public Date getPlannedStartDate() {
			return plannedStartDate;
		}

		public String getOrgType() {
					 return orgType;
		  }

		  public Long getAmpLevelId() {
					 return ampLevelId;
		  }

		  public String getLevelName() {
					 return levelName;
		  }

		  public String getActivityDescription() {
					 return activityDescription;
		  }

		  public Long getAmpComponentId() {
					 return ampComponentId;
		  }

		  public String getComponentName() {
					 return componentName;
		  }

		  public Long getAmpRegionId() {
					 return ampRegionId;
		  }

		  public String getRegionName() {
					 return regionName;
		  }

		  public Long getReportType() {
					 return reportType;
		  }

		  
		  public void setAmpReportId(Long ampReportId) {
					 this.ampReportId = ampReportId;
		  }

		  public void setAmpModalityId(Long ampModalityId) {
					 this.ampModalityId = ampModalityId;
		  }

		  public void setAmpTeamId(Long ampTeamId) {
					 this.ampTeamId = ampTeamId;
		  }

		  public void setAmpDonorId(Long ampDonorId) {
					 this.ampDonorId = ampDonorId;
		  }

		  public void setAmpStatusId(Long ampStatusId) {
					 this.ampStatusId = ampStatusId;
		  }
		  
		  public void setAmpActivityId(Long ampActivityId) {
					 this.ampActivityId = ampActivityId;
		  }

		  public void setAmpId(String ampId) {
					 this.ampId = ampId;
		  }

		  public void setActivityName(String activityName) {
					 this.activityName = activityName;
		  }

		  public void setModalityName(String modalityName) {
					 this.modalityName = modalityName;
		  }

		  public void setTermAssistName(String termAssistName) {
					 this.termAssistName = termAssistName;
		  }

		  public void setDonorName(String donorName) {
					 this.donorName = donorName;
		  }

		  public void setStatusName(String statusName) {
					 this.statusName = statusName;
		  }

		  public void setAmpFundingId(Long ampFundingId) {
					 this.ampFundingId = ampFundingId;
		  }

		  public void setPlannedCommitment(Double plannedCommitment) {
					 this.plannedCommitment = FeaturesUtil.applyThousandsForEntry(plannedCommitment);
		  }

		  public void setPlannedDisbursement(Double plannedDisbursement) {
					 this.plannedDisbursement = FeaturesUtil.applyThousandsForEntry(plannedDisbursement);
		  }

		  public void setPlannedExpenditure(Double plannedExpenditure) {
					 this.plannedExpenditure = FeaturesUtil.applyThousandsForEntry(plannedExpenditure);
		  }

		  public void setActualCommitment(Double actualCommitment) {
					 this.actualCommitment = FeaturesUtil.applyThousandsForEntry(actualCommitment);
		  }

		  public void setActualDisbursement(Double actualDisbursement) {
					 this.actualDisbursement = FeaturesUtil.applyThousandsForEntry(actualDisbursement);
		  }

		  public void setActualExpenditure(Double actualExpenditure) {
					 this.actualExpenditure = FeaturesUtil.applyThousandsForEntry(actualExpenditure);
		  }

		  public void setCurrencyCode(String currencyCode) {
					 this.currencyCode = currencyCode;
		  }

		public void setActualCompletionDate(Date date) {
			actualCompletionDate = date;
		}

		public void setActualStartDate(Date date) {
			actualStartDate = date;
		}

		public void setPlannedCompletionDate(Date date) {
			plannedCompletionDate = date;
		}

		public void setPlannedStartDate(Date date) {
			plannedStartDate = date;
		}

		public void setTransactionDate(Date date) {
			transactionDate = date;
		}

		public void setFiscalYear(Integer i) {
			fiscalYear = i;
		}

		public void setFiscalQuarter(Integer i) {
			fiscalQuarter = i;
		}

		public void setOrgType(String orgType) {
					 this.orgType = orgType;
		  }

		  public void setAmpLevelId(Long ampLevelId) {
					 this.ampLevelId = ampLevelId;
		  }

		  public void setLevelName(String levelName) {
					 this.levelName = levelName;
		  }

		  public void setActivityDescription(String activityDescription) {
					 this.activityDescription = activityDescription;
		  }

		  public void setAmpComponentId(Long ampComponentId) {
					 this.ampComponentId = ampComponentId;
		  }

		  public void setComponentName(String componentName) {
					 this.componentName = componentName;
		  }
		  
		  public void setAmpRegionId(Long ampRegionId) {
					 this.ampRegionId = ampRegionId;
		  }

		  public void setRegionName(String regionName) {
					 this.regionName = regionName;
		  }

		  public void setReportType(Long reportType) {
					 this.reportType = reportType;
		  }

}
