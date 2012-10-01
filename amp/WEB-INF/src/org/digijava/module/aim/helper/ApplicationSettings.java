
package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpReports;

public class ApplicationSettings {

		  private Long appSettingsId;
		  private Integer defRecsPerPage;
		  private Integer numberOfPagesToDisplay;
		  private String language;
		  private Long currencyId;
		  private Long fisCalId;
		  private Integer reportStartYear;
		  private Integer reportEndYear;
		  private AmpReports defaultAmpReport;
          private int defReportsPerPage;
          private String validation;

  public String getValidation() {
			return validation;
		}

		public void setValidation(String validation) {
			this.validation = validation;
		}

public Long getAppSettingsId() {
					 return this.appSettingsId;
		  }

		  public void setAppSettingsId(Long appSettingsId) {
					 this.appSettingsId = appSettingsId;
		  }

		  public Integer getDefRecsPerPage() {
			  return this.defRecsPerPage;
		  }

		  public void setDefRecsPerPage(Integer defRecsPerPage) {
			  this.defRecsPerPage = defRecsPerPage;
		  }

		  public String getLanguage() {
					 return this.language;
		  }

		  public void setLanguage(String language) {
					 this.language = language;
		  }

		  public Long getCurrencyId() {
					 return this.currencyId;
		  }

		  public void setCurrencyId(Long currencyId) {
					 this.currencyId = currencyId;
		  }

		  public Long getFisCalId() {
					 return this.fisCalId;
		  }

		  public void setFisCalId(Long fisCalId) {
					 this.fisCalId = fisCalId;
		  }

		public AmpReports getDefaultAmpReport() {
			return defaultAmpReport;
		}

                public int getDefReportsPerPage() {
                  return defReportsPerPage;
                }

  public void setDefaultAmpReport(AmpReports defaultAmpReport) {
			this.defaultAmpReport = defaultAmpReport;
		}

                public void setDefReportsPerPage(int defReportsPerPage) {
                  this.defReportsPerPage = defReportsPerPage;
                }

		public Integer getReportStartYear() {
		    return reportStartYear;
		}

		public void setReportStartYear(Integer reportStartYear) {
		    this.reportStartYear = reportStartYear;
		}

		public Integer getReportEndYear() {
		    return reportEndYear;
		}

		public void setReportEndYear(Integer reportEndYear) {
		    this.reportEndYear = reportEndYear;
		}

		public Integer getNumberOfPagesToDisplay() {
			return numberOfPagesToDisplay;
		}

		public void setNumberOfPagesToDisplay(Integer numberOfPagesToDisplay) {
			this.numberOfPagesToDisplay = numberOfPagesToDisplay;
		}
}
