package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class DesktopForm extends ActionForm {
    // desktop filters

    private long fltrCalendar;
    private String fltrCurrency;
    private long fltrDonor[];
    private long fltrSector[];
    private long fltrStatus[];
    private int fltrFrmYear;
    private int fltrToYear;
    private Integer fltrActivityRisks;

    private int activityCount;
    
    private Integer currentPage;
    private Collection pages;
    private int stIndex;
    private int edIndex;    

    // desktop filter select box data collection
    private Collection calendars;
    private Collection currencies;
    private Collection donors;
    private Collection sectors;
    private Collection status;
    private int[] yearRange;
    private Collection activityRisks;

    private Long teamId;
    private boolean teamHead;
    private boolean totalCalculated;

    private String searchKey;

    private String totalCommitments;
    private String defCurrency;

    private byte srtFld;
    private boolean srtAsc;

    private boolean showAddActivityLink;


    private Collection selDonors;
    private Collection selSectors;
    private Collection selStatus;

    private String resetFliters;
    
    private String lineMinRank;
    
    private Collection actRankColl;

    /**
     * @return Returns the calendars.
     */
    public Collection getCalendars() {
        return calendars;
    }
    /**
     * @param calendars The calendars to set.
     */
    public void setCalendars(Collection calendars) {
        this.calendars = calendars;
    }
    /**
     * @return Returns the currencies.
     */
    public Collection getCurrencies() {
        return currencies;
    }
    /**
     * @param currencies The currencies to set.
     */
    public void setCurrencies(Collection currencies) {
        this.currencies = currencies;
    }
    /**
     * @return Returns the currentPage.
     */
    public Integer getCurrentPage() {
        return currentPage;
    }
    /**
     * @param currentPage The currentPage to set.
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    /**
     * @return Returns the defCurrency.
     */
    public String getDefCurrency() {
        return defCurrency;
    }
    /**
     * @param defCurrency The defCurrency to set.
     */
    public void setDefCurrency(String defCurrency) {
        this.defCurrency = defCurrency;
    }
    /**
     * @return Returns the donors.
     */
    public Collection getDonors() {
        return donors;
    }
    /**
     * @param donors The donors to set.
     */
    public void setDonors(Collection donors) {
        this.donors = donors;
    }

    /**
     * @return Returns the fltrCalendar.
     */
    public long getFltrCalendar() {
        return fltrCalendar;
    }
    /**
     * @param fltrCalendar The fltrCalendar to set.
     */
    public void setFltrCalendar(long fltrCalendar) {
        this.fltrCalendar = fltrCalendar;
    }
    /**
     * @return Returns the fltrCurrency.
     */
    public String getFltrCurrency() {
        return fltrCurrency;
    }
    /**
     * @param fltrCurrency The fltrCurrency to set.
     */
    public void setFltrCurrency(String fltrCurrency) {
        this.fltrCurrency = fltrCurrency;
    }
    /**
     * @return Returns the fltrDonor.
     */
    public long[] getFltrDonor() {
        return fltrDonor;
    }
    /**
     * @param fltrDonor The fltrDonor to set.
     */
    public void setFltrDonor(long fltrDonor[]) {
        this.fltrDonor = fltrDonor;
    }
    /**
     * @return Returns the fltrFrmYear.
     */
    public int getFltrFrmYear() {
        return fltrFrmYear;
    }
    /**
     * @param fltrFrmYear The fltrFrmYear to set.
     */
    public void setFltrFrmYear(int fltrFrmYear) {
        this.fltrFrmYear = fltrFrmYear;
    }
    /**
     * @return Returns the fltrSector.
     */
    public long[] getFltrSector() {
        return fltrSector;
    }
    /**
     * @param fltrSector The fltrSector to set.
     */
    public void setFltrSector(long fltrSector[]) {
        this.fltrSector = fltrSector;
    }
    /**
     * @return Returns the fltrStatus.
     */
    public long[] getFltrStatus() {
        return fltrStatus;
    }
    /**
     * @param fltrStatus The fltrStatus to set.
     */
    public void setFltrStatus(long fltrStatus[]) {
        this.fltrStatus = fltrStatus;
    }
    /**
     * @return Returns the fltrToYear.
     */
    public int getFltrToYear() {
        return fltrToYear;
    }
    /**
     * @param fltrToYear The fltrToYear to set.
     */
    public void setFltrToYear(int fltrToYear) {
        this.fltrToYear = fltrToYear;
    }
    /**
     * @return Returns the pages.
     */
    public Collection getPages() {
        return pages;
    }
    /**
     * @param pages The pages to set.
     */ 
    public void setPages(Collection pages) {
        this.pages = pages;
    }
    /**
     * @return Returns the searchKey.
     */
    public String getSearchKey() {
        return searchKey;
    }
    /**
     * @param searchKey The searchKey to set.
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
    /**
     * @return Returns the sectors.
     */
    public Collection getSectors() {
        return sectors;
    }
    /**
     * @param sectors The sectors to set.
     */
    public void setSectors(Collection sectors) {
        this.sectors = sectors;
    }
    /**
     * @return Returns the srtAsc.
     */
    public boolean isSrtAsc() {
        return srtAsc;
    }
    /**
     * @param srtAsc The srtAsc to set.
     */
    public void setSrtAsc(boolean srtAsc) {
        this.srtAsc = srtAsc;
    }
    /**
     * @return Returns the srtFld.
     */
    public byte getSrtFld() {
        return srtFld;
    }
    /**
     * @param srtFld The srtFld to set.
     */
    public void setSrtFld(byte srtFld) {
        this.srtFld = srtFld;
    }
    /**
     * @return Returns the status.
     */
    public Collection getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(Collection status) {
        this.status = status;
    }
    /**
     * @return Returns the totalCommitments.
     */
    public String getTotalCommitments() {
        return totalCommitments;
    }
    /**
     * @param totalCommitments The totalCommitments to set.
     */
    public void setTotalCommitments(String totalCommitments) {
        this.totalCommitments = totalCommitments;
    }
    /**
     * @return Returns the yearRange.
     */
    public int[] getYearRange() {
        return yearRange;
    }
    /**
     * @param yearRange The yearRange to set.
     */
    public void setYearRange(int[] yearRange) {
        this.yearRange = yearRange;
    }
    /**
     * @return Returns the showAddActivityLink.
     */
    public boolean isShowAddActivityLink() {
        return showAddActivityLink;
    }
    /**
     * @param showAddActivityLink The showAddActivityLink to set.
     */
    public void setShowAddActivityLink(boolean showAddActivityLink) {
        this.showAddActivityLink = showAddActivityLink;
    }
    /**
     * @return Returns the teamId.
     */
    public Long getTeamId() {
        return teamId;
    }
    /**
     * @param teamId The teamId to set.
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    /**
     * @return Returns the teamHead.
     */
    public boolean isTeamHead() {
        return teamHead;
    }
    /**
     * @param teamHead The teamHead to set.
     */
    public void setTeamHead(boolean teamHead) {
        this.teamHead = teamHead;
    }
    /**
     * @return Returns the totalCalculated.
     */
    public boolean isTotalCalculated() {
        return totalCalculated;
    }
    /**
     * @param totalCalculated The totalCalculated to set.
     */
    public void setTotalCalculated(boolean totalCalculated) {
        this.totalCalculated = totalCalculated;
    }
    /**
     * @return Returns the edIndex.
     */
    public int getEdIndex() {
        return edIndex;
    }
    /**
     * @param edIndex The edIndex to set.
     */
    public void setEdIndex(int edIndex) {
        this.edIndex = edIndex;
    }
    /**
     * @return Returns the stIndex.
     */
    public int getStIndex() {
        return stIndex;
    }
    /**
     * @param stIndex The stIndex to set.
     */
    public void setStIndex(int stIndex) {
        this.stIndex = stIndex;
    }
    /**
     * @return Returns the fltrActivityRisks.
     */
    public Integer getFltrActivityRisks() {
        return fltrActivityRisks;
    }
    /**
     * @param fltrActivityRisks The fltrActivityRisks to set.
     */
    public void setFltrActivityRisks(Integer fltrActivityRisks) {
        this.fltrActivityRisks = fltrActivityRisks;
    }
    /**
     * @return Returns the activityRisks.
     */
    public Collection getActivityRisks() {
        return activityRisks;
    }
    /**
     * @param activityRisks The activityRisks to set.
     */
    public void setActivityRisks(Collection activityRisks) {
        this.activityRisks = activityRisks;
    }
    /**
     * @return Returns the resetFliters.
     */
    public String getResetFliters() {
        return resetFliters;
    }

    public Collection getSelDonors() {
        return selDonors;
    }

    public Collection getSelStatus() {
        return selStatus;
    }

    public Collection getSelSectors() {
        return selSectors;
    }

    /**
     * @param resetFliters The resetFliters to set.
     */
    public void setResetFliters(String resetFliters) {
        this.resetFliters = resetFliters;
    }
    /**
     * @return Returns the activityCount.
     */
    public int getActivityCount() {
        return activityCount;
    }
    /**
     * @param activityCount The activityCount to set.
     */
    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public void setSelDonors(Collection selDonors) {
        this.selDonors = selDonors;
    }

    public void setSelSectors(Collection selSectors) {
        this.selSectors = selSectors;
    }

    public void setSelStatus(Collection selStatus) {
        this.selStatus = selStatus;
    }
    public Collection getActRankColl() {
        return actRankColl;
    }
    public void setActRankColl(Collection actRankColl) {
        this.actRankColl = actRankColl;
    }
    public String getLineMinRank() {
        return lineMinRank;
    }
    public void setLineMinRank(String lineMinRank) {
        this.lineMinRank = lineMinRank;
    }

}
