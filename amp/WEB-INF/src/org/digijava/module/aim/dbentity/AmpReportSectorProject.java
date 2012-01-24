package org.digijava.module.aim.dbentity;
public class AmpReportSectorProject {
		  private Long ampReportId;
		  private String activityName;
		  private String sectorName;
		  private String donorName;
		  private Long ampFundingId;

		  public Long getAmpReportId() {
					 return ampReportId;
		  }

		  public String getActivityName() {
					 return activityName;
		  }

		  public String getSectorName() {
					 return sectorName;
		  }

		  public String getDonorName() {
					 return donorName;
		  }

		  public Long getAmpFundingId() {
					 return ampFundingId;
		  }

		  public void setAmpReportId(Long ampReportId) {
					 this.ampReportId = ampReportId;
		  }
		  
		  public void setActivityName(String activityName) {
					 this.activityName = activityName;
		  }

		  public void setSectorName(String sectorName) {
					 this.sectorName = sectorName;
		  }

		  public void setDonorName(String donorName) {
					 this.donorName = donorName;
		  }

		  public void setAmpFundingId(Long ampFundingId) {
					 this.ampFundingId = ampFundingId;
		  }
}
