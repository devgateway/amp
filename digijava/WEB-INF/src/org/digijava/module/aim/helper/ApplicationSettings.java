
package org.digijava.module.aim.helper;

public class ApplicationSettings {

		  private Long appSettingsId;
		  private int defRecsPerPage;
		  private String language;
		  private String perspective;
		  private Long currencyId;
		  private Long fisCalId;

		  public ApplicationSettings() {}

		  public Long getAppSettingsId() {
					 return this.appSettingsId;
		  }

		  public void setAppSettingsId(Long appSettingsId) {
					 this.appSettingsId = appSettingsId;
		  }

		  public int getDefRecsPerPage() {
					 return this.defRecsPerPage;
		  }

		  public void setDefRecsPerPage(int defRecsPerPage) {
					 this.defRecsPerPage = defRecsPerPage;
		  }

		  public String getLanguage() {
					 return this.language;
		  }

		  public void setLanguage(String language) {
					 this.language = language;
		  }

		  public String getPerspective() {
					 return this.perspective;
		  }

		  public void setPerspective(String perspective) {
					 this.perspective = perspective;
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
}
