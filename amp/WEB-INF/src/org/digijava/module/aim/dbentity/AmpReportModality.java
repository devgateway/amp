package org.digijava.module.aim.dbentity;
public class AmpReportModality {
		  private Long ampReportId;
		  private Long ampActivityId;
		  private String activityName;
		  private String modalityName;
		  private String sectorName;
		  private String termAssistName;
		  private String donorName;
		  private String donorCode;
		  private Long ampFundingId;

		  public Long getAmpActivityId() {
					 return ampActivityId;
		  }

		  public Long getAmpReportId() {
					 return ampReportId;
		  }

		  public String getActivityName() {
					 return activityName;
		  }

		  public String getModalityName() {
					 return modalityName;
		  }

		  public String getSectorName() {
					 return sectorName;
		  }

		  public String getTermAssistName() {
					 return termAssistName;
		  }

		  public String getDonorName() {
					 return donorName;
		  }

		  public String getDonorCode() {
					 return donorCode;
		  }

		  public Long getAmpFundingId() {
					 return ampFundingId;
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

		  public void setModalityName(String modalityName) {
					 this.modalityName = modalityName;
		  }

		  public void setSectorName(String sectorName) {
					 this.sectorName = sectorName;
		  }

		  public void setTermAssistName(String termAssistName) {
					 this.termAssistName = termAssistName;
		  }

		  public void setDonorName(String donorName) {
					 this.donorName = donorName;
		  }

		  public void setDonorCode(String donorCode) {
					 this.donorCode = donorCode;
		  }

		  public void setAmpFundingId(Long ampFundingId) {
					 this.ampFundingId = ampFundingId;
		  }
}
