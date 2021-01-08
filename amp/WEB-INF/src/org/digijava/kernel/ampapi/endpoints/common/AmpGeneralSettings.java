package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

public class AmpGeneralSettings {

    @JsonProperty("use-icons-for-sectors-in-project-list")
    private Boolean useIconsForSectorsInProjectList;

    @JsonProperty("project-sites")
    private Boolean projectSites;

    @JsonProperty("max-locations-icons")
    private Integer maxLocationsIcons;

    @JsonProperty("number-format")
    private String numberFormat;

    @JsonProperty("gs-number-format")
    private String gsNumberFormat;

    @JsonProperty("number-group-separator")
    private String numberGroupSeparator;

    @JsonProperty("number-decimal-separator")
    private String numberDecimalSeparator;

    @JsonProperty("number-divider")
    private Integer numberDivider;

    @JsonProperty("amount-in-thousands")
    private Integer amountInThousands;

    @JsonProperty("language")
    private String language;

    @JsonProperty("default-language")
    private String defaultLanguage;

    @JsonProperty("rtl-direction")
    private Boolean rtlDirection;

    @JsonProperty("multilingual")
    private Boolean multilingual;

    @JsonProperty("default-date-format")
    private String defaultDateFormat;

    @JsonProperty("hide-editable-export-formats-public-view")
    private Boolean hideEditableExportFormatsPublicView;
    @JsonProperty("hide-contacts-public-view")
    private Boolean hideContactsPublicView;

    @JsonProperty("download-map-selector")
    private Boolean downloadMapSelector;

    @JsonProperty("gap-analysis-map")
    private Boolean gapAnalysisMap;

    @JsonProperty("has-ssc-workspaces")
    private Boolean hasSscWorkspaces;

    @JsonProperty("public-version-history")
    private Boolean publicVersionHistory;

    @JsonProperty("public-change-summary")
    private Boolean publicChangeSummary;

    @JsonProperty(SettingsConstants.EFFECTIVE_CURRENCY)
    private CurrencySettings effectiveCurrency;

    @JsonProperty(SettingsConstants.REORDER_FUNDING_ITEM_ID)
    private Long reorderFundingItemId;

    @JsonProperty("team-id")
    private String teamId;

    @JsonProperty("team-lead")
    private Boolean teamLead;

    @JsonProperty("team-validator")
    private Boolean teamValidator;

    @JsonProperty(SettingsConstants.SHOW_ACTIVITY_WORKSPACES)
    private Boolean showActivityWorkspaces;

    @JsonProperty("cross_team_validation")
    private Boolean crossTeamValidation;

    @JsonProperty("workspace-type")
    private String workspaceType;

    @JsonProperty("workspace-prefix")
    private String workspacePrefix;

    @JsonProperty(SettingsConstants.CALENDAR_TYPE_ID)
    private Long calendarId;

    @JsonProperty(SettingsConstants.CALENDAR_IS_FISCAL)
    private boolean calendarIsFiscal;

    @JsonProperty(SettingsConstants.DASHBOARD_DEFAULT_MAX_DATE)
    private String dashboardDefaultMaxDate;

    @JsonProperty(SettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE)
    private String dashboardDefaultMaxYearRange;

    @JsonProperty(SettingsConstants.DASHBOARD_DEFAULT_MIN_DATE)
    private String dashboardDefaultMinDate;

    @JsonProperty(SettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE)
    private String dashboardDefaultMinYearRange;

    @JsonProperty(SettingsConstants.GIS_DEFAULT_MAX_DATE)
    private String gisDefaultMaxDate;

    @JsonProperty(SettingsConstants.GIS_DEFAULT_MAX_YEAR_RANGE)
    private String gisDefaultMaxYearRange;

    @JsonProperty(SettingsConstants.GIS_DEFAULT_MIN_DATE)
    private String gisDefaultMinDate;

    @JsonProperty(SettingsConstants.GIS_DEFAULT_MIN_YEAR_RANGE)
    private String gisDefaultMinYearRange;

    public Boolean getUseIconsForSectorsInProjectList() {
        return useIconsForSectorsInProjectList;
    }

    public void setUseIconsForSectorsInProjectList(Boolean useIconsForSectorsInProjectList) {
        this.useIconsForSectorsInProjectList = useIconsForSectorsInProjectList;
    }

    public Boolean getProjectSites() {
        return projectSites;
    }

    public void setProjectSites(Boolean projectSites) {
        this.projectSites = projectSites;
    }

    public Integer getMaxLocationsIcons() {
        return maxLocationsIcons;
    }

    public void setMaxLocationsIcons(Integer maxLocationsIcons) {
        this.maxLocationsIcons = maxLocationsIcons;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getGsNumberFormat() {
        return gsNumberFormat;
    }

    public void setGsNumberFormat(String gsNumberFormat) {
        this.gsNumberFormat = gsNumberFormat;
    }

    public String getNumberGroupSeparator() {
        return numberGroupSeparator;
    }

    public void setNumberGroupSeparator(String numberGroupSeparator) {
        this.numberGroupSeparator = numberGroupSeparator;
    }

    public String getNumberDecimalSeparator() {
        return numberDecimalSeparator;
    }

    public void setNumberDecimalSeparator(String numberDecimalSeparator) {
        this.numberDecimalSeparator = numberDecimalSeparator;
    }

    public Integer getNumberDivider() {
        return numberDivider;
    }

    public void setNumberDivider(Integer numberDivider) {
        this.numberDivider = numberDivider;
    }

    public Integer getAmountInThousands() {
        return amountInThousands;
    }

    public void setAmountInThousands(Integer amountInThousands) {
        this.amountInThousands = amountInThousands;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Boolean getRtlDirection() {
        return rtlDirection;
    }

    public void setRtlDirection(Boolean rtlDirection) {
        this.rtlDirection = rtlDirection;
    }

    public Boolean getMultilingual() {
        return multilingual;
    }

    public void setMultilingual(Boolean multilingual) {
        this.multilingual = multilingual;
    }

    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public Boolean getHideEditableExportFormatsPublicView() {
        return hideEditableExportFormatsPublicView;
    }

    public void setHideEditableExportFormatsPublicView(Boolean hideEditableExportFormatsPublicView) {
        this.hideEditableExportFormatsPublicView = hideEditableExportFormatsPublicView;
    }

    public Boolean getDownloadMapSelector() {
        return downloadMapSelector;
    }

    public void setDownloadMapSelector(Boolean downloadMapSelector) {
        this.downloadMapSelector = downloadMapSelector;
    }

    public Boolean getGapAnalysisMap() {
        return gapAnalysisMap;
    }

    public void setGapAnalysisMap(Boolean gapAnalysisMap) {
        this.gapAnalysisMap = gapAnalysisMap;
    }

    public Boolean getHasSscWorkspaces() {
        return hasSscWorkspaces;
    }

    public void setHasSscWorkspaces(Boolean hasSscWorkspaces) {
        this.hasSscWorkspaces = hasSscWorkspaces;
    }

    public Boolean getPublicVersionHistory() {
        return publicVersionHistory;
    }

    public void setPublicVersionHistory(Boolean publicVersionHistory) {
        this.publicVersionHistory = publicVersionHistory;
    }

    public Boolean getPublicChangeSummary() {
        return publicChangeSummary;
    }

    public void setPublicChangeSummary(Boolean publicChangeSummary) {
        this.publicChangeSummary = publicChangeSummary;
    }

    public CurrencySettings getEffectiveCurrency() {
        return effectiveCurrency;
    }

    public void setEffectiveCurrency(CurrencySettings effectiveCurrency) {
        this.effectiveCurrency = effectiveCurrency;
    }

    public Long getReorderFundingItemId() {
        return reorderFundingItemId;
    }

    public void setReorderFundingItemId(Long reorderFundingItemId) {
        this.reorderFundingItemId = reorderFundingItemId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Boolean getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Boolean teamLead) {
        this.teamLead = teamLead;
    }

    public Boolean getTeamValidator() {
        return teamValidator;
    }

    public void setTeamValidator(Boolean teamValidator) {
        this.teamValidator = teamValidator;
    }

    public Boolean getCrossTeamValidation() {
        return crossTeamValidation;
    }

    public void setCrossTeamValidation(Boolean crossTeamValidation) {
        this.crossTeamValidation = crossTeamValidation;
    }

    public String getWorkspaceType() {
        return workspaceType;
    }

    public void setWorkspaceType(String workspaceType) {
        this.workspaceType = workspaceType;
    }

    public String getWorkspacePrefix() {
        return workspacePrefix;
    }

    public void setWorkspacePrefix(String workspacePrefix) {
        this.workspacePrefix = workspacePrefix;
    }

    public String getDashboardDefaultMaxDate() {
        return dashboardDefaultMaxDate;
    }

    public void setDashboardDefaultMaxDate(String dashboardDefaultMaxDate) {
        this.dashboardDefaultMaxDate = dashboardDefaultMaxDate;
    }

    public String getDashboardDefaultMaxYearRange() {
        return dashboardDefaultMaxYearRange;
    }

    public void setDashboardDefaultMaxYearRange(String dashboardDefaultMaxYearRange) {
        this.dashboardDefaultMaxYearRange = dashboardDefaultMaxYearRange;
    }

    public String getDashboardDefaultMinDate() {
        return dashboardDefaultMinDate;
    }

    public void setDashboardDefaultMinDate(String dashboardDefaultMinDate) {
        this.dashboardDefaultMinDate = dashboardDefaultMinDate;
    }

    public String getDashboardDefaultMinYearRange() {
        return dashboardDefaultMinYearRange;
    }

    public void setDashboardDefaultMinYearRange(String dashboardDefaultMinYearRange) {
        this.dashboardDefaultMinYearRange = dashboardDefaultMinYearRange;
    }

    public String getGisDefaultMaxDate() {
        return gisDefaultMaxDate;
    }

    public void setGisDefaultMaxDate(String gisDefaultMaxDate) {
        this.gisDefaultMaxDate = gisDefaultMaxDate;
    }

    public String getGisDefaultMaxYearRange() {
        return gisDefaultMaxYearRange;
    }

    public void setGisDefaultMaxYearRange(String gisDefaultMaxYearRange) {
        this.gisDefaultMaxYearRange = gisDefaultMaxYearRange;
    }

    public String getGisDefaultMinDate() {
        return gisDefaultMinDate;
    }

    public void setGisDefaultMinDate(String gisDefaultMinDate) {
        this.gisDefaultMinDate = gisDefaultMinDate;
    }

    public String getGisDefaultMinYearRange() {
        return gisDefaultMinYearRange;
    }

    public void setGisDefaultMinYearRange(String gisDefaultMinYearRange) {
        this.gisDefaultMinYearRange = gisDefaultMinYearRange;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public boolean isCalendarIsFiscal() {
        return calendarIsFiscal;
    }

    public void setCalendarIsFiscal(boolean calendarIsFiscal) {
        this.calendarIsFiscal = calendarIsFiscal;
    }

    public Boolean getShowActivityWorkspaces() {
        return showActivityWorkspaces;
    }

    public void setShowActivityWorkspaces(Boolean showActivityWorkspaces) {
        this.showActivityWorkspaces = showActivityWorkspaces;
    }

    public Boolean getHideContactsPublicView() {
        return hideContactsPublicView;
    }

    public void setHideContactsPublicView(Boolean hideContactsPublicView) {
        this.hideContactsPublicView = hideContactsPublicView;
    }
}
