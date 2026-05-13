package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * immutable mirror of AmpApplicationSettings
 * @author Constantin Dolghier
 *
 */
public class ApplicationSettings {

    public ApplicationSettings(AmpApplicationSettings ampAppSettings) {
        this.appSettingsId = ampAppSettings.getAmpAppSettingsId();
        this.defRecsPerPage = ampAppSettings.getDefaultRecordsPerPage();
        this.defReportsPerPage = (ampAppSettings.getDefaultReportsPerPage()==null)?0:ampAppSettings.getDefaultReportsPerPage();
        this.currencyId = ampAppSettings.getCurrency() != null ? ampAppSettings.getCurrency().getAmpCurrencyId() : 
            CurrencyUtil.getAmpcurrency(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY)).getAmpCurrencyId();
        
        this.fisCalId = ampAppSettings.getFiscalCalendar() != null ? ampAppSettings.getFiscalCalendar().getAmpFiscalCalId() : null;         
        this.language = ampAppSettings.getLanguage();
        this.defaultAmpReport = ampAppSettings.getDefaultTeamReport();
        this.validation = ampAppSettings.getValidation();
        
        this.crossteamvalidation = ampAppSettings.getTeam().getCrossteamvalidation();
        this.reportStartYear = ampAppSettings.getReportStartYear();
        this.reportEndYear = ampAppSettings.getReportEndYear();
        this.numberOfPagesToDisplay = ampAppSettings.getNumberOfPagesToDisplay();
    }

    private final Long appSettingsId;
    private final Integer defRecsPerPage;
    private final Integer numberOfPagesToDisplay;
    private String language;
    private final Long currencyId;
    private final Long fisCalId;
    private final Integer reportStartYear;
    private final Integer reportEndYear;
    private final AmpReports defaultAmpReport;
    private final int defReportsPerPage;
    private final String validation;
    private final Boolean crossteamvalidation;

    public String getValidation() {
        return validation;
    }

    public Long getAppSettingsId() {
        return this.appSettingsId;
    }

    public Integer getDefRecsPerPage() {
        return this.defRecsPerPage;
    }

    public String getLanguage() {
        return this.language;
    }

    public Long getCurrencyId() {
        return this.currencyId;
    }

    public Long getFisCalId() {
        return this.fisCalId;
    }

    public AmpReports getDefaultAmpReport() {
        return defaultAmpReport;
    }

    public int getDefReportsPerPage() {
        return defReportsPerPage;
    }

    public Integer getReportStartYear() {
        return reportStartYear;
    }

    public Integer getReportEndYear() {
        return reportEndYear;
    }

    public Integer getNumberOfPagesToDisplay() {
        return numberOfPagesToDisplay;
    }

    public Boolean getCrossteamvalidation() {
        return crossteamvalidation;
    }

    public boolean isCrossTeamValidationEnabled() {
        return getCrossteamvalidation() != null ? getCrossteamvalidation() : false;
    }
    
    /**
     * hack - to be removed once the language-setting mess is sorted out
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
