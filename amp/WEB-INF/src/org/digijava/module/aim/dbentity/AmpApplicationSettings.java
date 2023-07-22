/*
 *  AmpApplicationSettings.java
 *  @Author Priyajith C
 *  Created: 18-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.security.serializers.AmpApplicationSettingsSerializer;
import javax.persistence.*;

@Entity
@Table(name = "AMP_APPLICATION_SETTINGS")
@SuppressWarnings("serial")

@JsonSerialize(using = AmpApplicationSettingsSerializer.class)
public class AmpApplicationSettings implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_application_settings_seq_generator")
    @SequenceGenerator(name = "amp_application_settings_seq_generator", sequenceName = "amp_application_settings_seq", allocationSize = 1)
    @Column(name = "amp_app_settings_id")
    private Long ampAppSettingsId;

    @Column(name = "def_rec_per_page")
    private Integer defaultRecordsPerPage;

    @Column(name = "num_pages_to_display")
    private Integer numberOfPagesToDisplay;

    @Column(name = "def_rep_per_page")
    private Integer defaultReportsPerPage;

    @Column(name = "report_start_year")
    private Integer reportStartYear;

    @Column(name = "report_end_year")
    private Integer reportEndYear;

    @Column(name = "language")
    private String language;

    @Column(name = "validation")
    private String validation;

    @Column(name = "show_all_countries")
    private Boolean showAllCountries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_team_ampreport_id")
    private AmpReports defaultTeamReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private AmpTeam team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_currency_id")
    private AmpCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fis_cal_id")
    private AmpFiscalCalendar fiscalCalendar;

    @Column(name = "allow_add_team_res")
    private Integer allowAddTeamRes;

    @Column(name = "allow_share_res_globally")
    private Integer allowShareTeamRes;

    @Column(name = "allow_publishing_resources")
    private Integer allowPublishingResources;

    public Boolean getShowAllCountries() {
        return showAllCountries;
    }

    public void setShowAllCountries(Boolean showAllCountries) {
        if (showAllCountries == null)
            return;
        this.showAllCountries = showAllCountries;
    }

    public AmpReports getDefaultTeamReport() {
        return defaultTeamReport;
    }

    public void setDefaultTeamReport(AmpReports defaultTeamReport) {
        this.defaultTeamReport = defaultTeamReport;
    }

    public Long getAmpAppSettingsId() {
        return this.ampAppSettingsId;
    }

    public void setAmpAppSettingsId(Long ampAppSettingsId) {
        this.ampAppSettingsId = ampAppSettingsId;
    }

    public AmpTeam getTeam() {
        return this.team;
    }

    public void setTeam(AmpTeam team) {
        this.team = team;
    }

    public Integer getDefaultRecordsPerPage() {
        return this.defaultRecordsPerPage;
    }

    public void setDefaultRecordsPerPage(Integer defaultRecordsPerPage) {
        this.defaultRecordsPerPage = defaultRecordsPerPage;
    }

    public AmpCurrency getCurrency() {
        return this.currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public AmpFiscalCalendar getFiscalCalendar() {
        return this.fiscalCalendar;
    }

    public void setFiscalCalendar(AmpFiscalCalendar fiscalCalendar) {
        this.fiscalCalendar = fiscalCalendar;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDefaultReportsPerPage() {
      return defaultReportsPerPage;
    }

    public void setDefaultReportsPerPage(Integer  defaultReportsPerPage) {
      this.defaultReportsPerPage = defaultReportsPerPage;
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

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    /**
     * @return the allowAddTeamRes
     */
    public Integer getAllowAddTeamRes() {
        return allowAddTeamRes;
    }

    /**
     * @param allowAddTeamRes the allowAddTeamRes to set
     */
    public void setAllowAddTeamRes(Integer allowAddTeamRes) {
        this.allowAddTeamRes = allowAddTeamRes;
    }

    public Integer getAllowShareTeamRes() {
        return allowShareTeamRes;
    }

    public void setAllowShareTeamRes(Integer allowShareTeamRes) {
        this.allowShareTeamRes = allowShareTeamRes;
    }

    public Integer getAllowPublishingResources() {
        return allowPublishingResources;
    }

    public void setAllowPublishingResources(Integer allowPublishingResources) {
        this.allowPublishingResources = allowPublishingResources;
    }

    public Integer getNumberOfPagesToDisplay() {
        return numberOfPagesToDisplay;
    }

    public void setNumberOfPagesToDisplay(Integer numberOfPagesToDisplay) {
        this.numberOfPagesToDisplay = numberOfPagesToDisplay;
    }

}
