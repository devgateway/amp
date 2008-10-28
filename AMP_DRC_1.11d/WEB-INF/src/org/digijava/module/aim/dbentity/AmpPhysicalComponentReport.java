package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;

public class AmpPhysicalComponentReport {

			private Long ampReportId;
			private Long ampActivityId;
			private String activityName;	
			private Long ampDonorId;
			private String donorName;	
			private Long ampComponentId;
			private String componentName;
			private String objective;
			private Date signatureDate;
			private Date actualStartDate;
			private Date plannedCompletionDate;
			private Double actualCommitment;
			private String commCurrencyCode;
			private Double actualExpenditure;
			private String expCurrencyCode;
			private String status;	
			private Date reportingDate;
		    private String mofedContact;
			private String donorContact;
			private Long ampTeamId;
			private Date transactionDate;
			private Integer fiscalYear;
			private Long ampStatusId;
			private Long ampModalityId;
		
			public Long getAmpReportId() {
					 return ampReportId;
			}  
			
			public Long getAmpActivityId() {
					 return ampActivityId;
			}

			public String getActivityName() {
					 return activityName;
		  }

			public Long getAmpDonorId() {
					 return ampDonorId;
			}

			public String getDonorName() {
					 return donorName;
		  }

		  public Long getAmpComponentId() {
					 return ampComponentId;
		  }

		  public String getComponentName() {
					 return componentName;
		  }

		  public Date getActualStartDate() {
			return actualStartDate;
		}

		  public Date getPlannedCompletionDate() {
			return plannedCompletionDate;
		}

		public Date getSignatureDate() {
			return signatureDate;
		}

		public Double getActualCommitment() {
					 return FeaturesUtil.applyThousandsForVisibility(actualCommitment);
		  }

		   public String getCommCurrencyCode() {
					 return commCurrencyCode;
		  }

		 
		  public Double getActualExpenditure() {
					 return FeaturesUtil.applyThousandsForVisibility(actualExpenditure);
		  }

		   public String getExpCurrencyCode() {
					 return expCurrencyCode;
		  }

		 
		  public String getStatus() {
					 return status;
		  }

		  public Date getReportingDate() {
			return reportingDate;
		}


		  public String getMofedContact() {
					 return mofedContact;
		  }

		  public String getDonorContact() {
					 return donorContact;
		  }


		   public String getObjective() {
					 return objective;
		  }

		  public Long getAmpTeamId() {
					 return ampTeamId;
		  }

		  public Date getTransactionDate() {
			return transactionDate;
			}

			public Long getAmpStatusId() {
					 return ampStatusId;
		  }

		  public Long getAmpModalityId() {
					 return ampModalityId;
		  }

		public Integer getFiscalYear() {
			return fiscalYear;
			}	

		  
		  public void setAmpReportId(Long ampReportId) {
					 this.ampReportId = ampReportId;
		  }

		  public void setAmpActivityId(Long ampActivityId) {
					 this.ampActivityId = ampActivityId;
		  }

		  public void setActivityName(String activityName) {
					 this.activityName = activityName;
		  }

		  public void setAmpDonorId(Long ampDonorId) {
					 this.ampDonorId = ampDonorId;
		  }

		  public void setDonorName(String donorName) {
					 this.donorName = donorName;
		  }

		  public void setAmpComponentId(Long ampComponentId) {
					 this.ampComponentId = ampComponentId;
		  }

		  public void setComponentName(String componentName) {
					 this.componentName = componentName;
		  }

		  public void setActualStartDate(Date date) {
			actualStartDate = date;
		}

		  public void setPlannedCompletionDate(Date date) {
			plannedCompletionDate = date;
		}

		public void setSignatureDate(Date date) {
			signatureDate = date;
		}

		  public void setActualCommitment(Double actualCommitment) {
					 this.actualCommitment = FeaturesUtil.applyThousandsForEntry(actualCommitment);
		  }

		   public void setCommCurrencyCode(String commCurrencyCode) {
					 this.commCurrencyCode = commCurrencyCode;
		  }

	
		  public void setActualExpenditure(Double actualExpenditure) {
					 this.actualExpenditure = FeaturesUtil.applyThousandsForEntry(actualExpenditure);
		  }

		   public void setExpCurrencyCode(String expCurrencyCode) {
					 this.expCurrencyCode = expCurrencyCode;
		  }

		 


		  public void setStatus(String status) {
					 this.status = status;
		  }
 
		  

		  public void setMofedContact(String mofedContact) {
					 this.mofedContact = mofedContact;
		  }

		  public void setDonorContact(String donorContact) {
					 this.donorContact = donorContact;
		  }
		

		  public void setObjective(String objective) {
					 this.objective = objective;
		  }

		  public void setReportingDate(Date date) {
			reportingDate = date;
		}

		public void setAmpTeamId(Long ampTeamId) {
					 this.ampTeamId = ampTeamId;
		  }

		  public void setTransactionDate(Date date) {
			transactionDate = date;
		}

		public void setFiscalYear(Integer i) {
			fiscalYear = i;
		}

		public void setAmpStatusId(Long l) {
			ampStatusId = l;
		}

		public void setAmpModalityId(Long l) {
			ampModalityId = l;
		}


}
