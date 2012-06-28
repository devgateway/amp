package org.digijava.module.aim.form ;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
public class MulitlateralbyDonorForm extends ActionForm
{
	private Collection multiReport;
	private Collection sector;
	private String currency;
	private int fiscalCalId;
	private int pageNum;
	private int yearSet;
	private String donorName;
	private String sectorName;
	private String region;
	private String status;
	private int modality;
	
	private Long startYear;
	private Long startMonth;
	private Long startDay;
	
	private Long closeYear;
	private Long closeMonth;
	private Long closeDay;
	private int yearCount;
	private Collection statusColl;
	private Long ampStatusId;
	private Long ampSectorId;
	private Collection sectorColl;
	private Collection regionColl;
	private String ampLocationId;
	private Collection donorColl;
	private Long ampOrgId;
	private Collection modalityColl;
	private Long ampModalityId;
	private Collection currencyColl;
	private Long ampCurrencyId;
	private String ampCurrencyCode;
	private Long ampFromYear;
	private Long ampToYear;
	private Collection ampFromYears;
	private Collection ampToYears;
	private Collection ampStartYears;
	private Collection ampCloseYears;
	private Collection ampStartDays;
	private Collection ampCloseDays;
	private Collection fiscalYears;
	private String filterFlag;
	private String goFlag;
	private String workspaceName;
	private String workspaceType;
	private String reportName;
	private Integer ampAdjustmentId;
	private String adjustmentFlag;
	private int yrCount;
	private Collection fiscalYearRange;
	private int totalColumns;
	/**
	 * @return
	 */
	public Collection getMultiReport() {
		return multiReport;
	}

	public Collection getFiscalYearRange() {
		return fiscalYearRange;
	}

	public int getTotalColumns() {
		return totalColumns;
	}

	public void setFiscalYearRange(Collection collection) {
		fiscalYearRange = collection;
	}

	/**
	 * @param collection
	 */
	public void setMultiReport(Collection collection) {
		multiReport = collection;
	}

	public void setTotalColumns(int i) {
		totalColumns = i;
	}

	/**
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param string
	 */
	public void setCurrency(String string) {
		currency = string;
	}

	/**
	 * @return
	 */
	public int getFiscalCalId() {
		return fiscalCalId;
	}

	/**
	 * @return
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * @param i
	 */
	public void setFiscalCalId(int i) {
		fiscalCalId = i;
	}

	/**
	 * @param i
	 */
	public void setPageNum(int i) {
		pageNum = i;
	}

	/**
	 * @return
	 */
	public String getDonorName() {
		return donorName;
	}

	/**
	 * @param string
	 */
	public void setDonorName(String string) {
		donorName = string;
	}

	/**
	 * @return
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string
	 */
	public void setRegion(String string) {
		region = string;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @return
	 */
	public int getModality() {
		return modality;
	}

	/**
	 * @param i
	 */
	public void setModality(int i) {
		modality = i;
	}

	/**
	 * @return
	 */
	public Long getCloseDay() {
		return closeDay;
	}

	/**
	 * @return
	 */
	public Long getCloseMonth() {
		return closeMonth;
	}

	/**
	 * @return
	 */
	public Long getCloseYear() {
		return closeYear;
	}

	/**
	 * @return
	 */
	public Long getStartDay() {
		return startDay;
	}

	/**
	 * @return
	 */
	public Long getStartMonth() {
		return startMonth;
	}

	/**
	 * @return
	 */
	public Long getStartYear() {
		return startYear;
	}

	public Long getAmpFromYear()
	{
		return ampFromYear;
	}

	public Long getAmpToYear()
	{
		return ampToYear;
	}

	public Collection getAmpFromYears()
	{
		return ampFromYears;
	}

	public Collection getAmpToYears()
	{
		return ampToYears;
	}

	public Collection getFiscalYears()
	{
		return fiscalYears;
	}

	public Collection getAmpStartYears()
	{
		return ampStartYears;
	}

	public Collection getAmpCloseYears()
	{
		return ampCloseYears;
	}

	public Collection getAmpStartDays()
	{
		return ampStartDays;
	}

	public Collection getAmpCloseDays()
	{
		return ampCloseDays;
	}



	/**
	 * @param i
	 */
	public void setCloseDay(Long i) {
		closeDay = i;
	}

	/**
	 * @param i
	 */
	public void setCloseMonth(Long i) {
		closeMonth = i;
	}

	/**
	 * @param i
	 */
	public void setCloseYear(Long i) {
		closeYear = i;
	}

	/**
	 * @param i
	 */
	public void setStartDay(Long i) {
		startDay = i;
	}

	/**
	 * @param i
	 */
	public void setStartMonth(Long i) {
		startMonth = i;
	}

	/**
	 * @param i
	 */
	public void setStartYear(Long i) {
		startYear = i;
	}

	/**
	 * @return
	 */
	public String getSectorName() {
		return sectorName;
	}

	/**
	 * @param string
	 */
	public void setSectorName(String string) {
		sectorName = string;
	}

	/**
	 * @return
	 */
	public int getYearSet() {
		return yearSet;
	}

	/**
	 * @param i
	 */
	public void setYearSet(int i) {
		yearSet = i;
	}

	/**
	 * @return
	 */
	public Collection getSector() {
		return sector;
	}

	/**
	 * @param collection
	 */
	public void setSector(Collection collection) {
		sector = collection;
	}

	/**
	 * @return
	 */
	public Long getAmpSectorId() {
		return ampSectorId;
	}

	/**
	 * @param long1
	 */
	public void setAmpSectorId(Long long1) {
		ampSectorId = long1;
	}

	/**
	 * @return
	 */
	public Collection getStatusColl() {
		return statusColl;
	}

	/**
	 * @param collection
	 */
	public void setStatusColl(Collection collection) {
		statusColl = collection;
	}

	/**
	 * @return
	 */
	public Long getAmpStatusId() {
		return ampStatusId;
	}

	/**
	 * @param long1
	 */
	public void setAmpStatusId(Long long1) {
		ampStatusId = long1;
	}

	/**
	 * @return
	 */
	public Long getAmpCurrencyId() {
		return ampCurrencyId;
	}

	public String getAmpCurrencyCode() {
		return ampCurrencyCode;
	}

	/**
	 * @return
	 */
	public String getAmpLocationId() {
		return ampLocationId;
	}

	/**
	 * @return
	 */
	public Long getAmpModalityId() {
		return ampModalityId;
	}

	/**
	 * @return
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}

	/**
	 * @return
	 */
	public Collection getCurrencyColl() {
		return currencyColl;
	}

	/**
	 * @return
	 */
	public Collection getDonorColl() {
		return donorColl;
	}

	/**
	 * @return
	 */
	public Collection getModalityColl() {
		return modalityColl;
	}

	/**
	 * @return
	 */
	public Collection getRegionColl() {
		return regionColl;
	}

	/**
	 * @return
	 */
	public Collection getSectorColl() {
		return sectorColl;
	}

	/**
	 * @param long1
	 */
	public void setAmpCurrencyId(Long long1) {
		ampCurrencyId = long1;
	}

	public void setAmpCurrencyCode(String s) {
		ampCurrencyCode = s;
	}

	/**
	 * @param long1
	 */
	public void setAmpLocationId(String s) {
		ampLocationId = s;
	}

	/**
	 * @param long1
	 */
	public void setAmpModalityId(Long long1) {
		ampModalityId = long1;
	}

	/**
	 * @param long1
	 */
	public void setAmpOrgId(Long long1) {
		ampOrgId = long1;
	}

	/**
	 * @param collection
	 */
	public void setCurrencyColl(Collection collection) {
		currencyColl = collection;
	}

	/**
	 * @param collection
	 */
	public void setDonorColl(Collection collection) {
		donorColl = collection;
	}

	/**
	 * @param collection
	 */
	public void setModalityColl(Collection collection) {
		modalityColl = collection;
	}

	/**
	 * @param collection
	 */
	public void setRegionColl(Collection collection) {
		regionColl = collection;
	}

	/**
	 * @param collection
	 */
	public void setSectorColl(Collection collection) {
		sectorColl = collection;
	}

// Added by Ronald
	private int fromYY;
	public int getFromYear() {
		return fromYY;
	}
	public void setFromYear(int i) {
		fromYY = i;
	}
	private int cnt;
	public int getRowCount() {
		return cnt;
	}
	public void setRowCount(int i) {
		cnt = i;
	}
	public void setAmpFromYear(Long ampFromYear)
	{
		this.ampFromYear=ampFromYear;
	}

	public void setAmpToYear(Long ampToYear)
	{
		this.ampToYear=ampToYear;
	}

	public void setAmpFromYears(Collection ampFromYears)
	{
		this.ampFromYears=ampFromYears;
	}

	public void setAmpToYears(Collection ampToYears)
	{
		this.ampToYears=ampToYears;
	}

	public void setFiscalYears(Collection c)
	{
		fiscalYears=c;
	}

	public void setAmpStartYears(Collection ampStartYears)
	{
		this.ampStartYears=ampStartYears;
	}

	public void setAmpCloseYears(Collection ampCloseYears)
	{
		this.ampCloseYears=ampCloseYears;
	}

	public void setAmpStartDays(Collection ampStartDays)
	{
		this.ampStartDays=ampStartDays;
	}

	public void setAmpCloseDays(Collection ampCloseDays)
	{
		this.ampCloseDays=ampCloseDays;
	}

	public String getFilterFlag() {
		return filterFlag;
	}

	public void setFilterFlag(String s)	{
		filterFlag=s;
	}

	public String getGoFlag() {
		return goFlag;
	}

	public void setGoFlag(String s) {
		goFlag = s;
	}
// new added.
	
	private String filter[] = new String[2];
	public String[] getFilter() {
		return filter;
	}
	public void setFilter(String[] filter) {
		this.filter = filter;
	}
	
	
	private String filtersSet="";
	public String getFiltersNames() {
		return filtersSet;
	}
	public void setFiltersNames(String filtersSet) {
		this.filtersSet = filtersSet;
	}
	
	private int fiscalYrRange=0;
	public int getFiscalYrRange() {
		return fiscalYrRange;
	}
	public void setFiscalYrRange(int fiscalYrRange) {
		this.fiscalYrRange = fiscalYrRange;
	}
	public int getYearCount() {
		return yearCount;
	}

	/**
	 * @param i
	 */
	public void setYearCount(int i) {
		yearCount = i;
	}
// maintains the count of filter selected for the corresponidng report
	private int filterCnt;
	public int getFilterCnt() {
		return filterCnt;
	}
	public void setFilterCnt(int filterCnt) {
		this.filterCnt = filterCnt;
	}

	public String getWorkspaceName() 
	{
			return workspaceName;
	}

	public void setWorkspaceName(String s)	
	{
		workspaceName=s;
	}

	public String getWorkspaceType() 
	{
			return workspaceType;
	}

	public void setWorkspaceType(String s)	
	{
		workspaceType=s;
	}

	public String getReportName() 
	{
			return reportName;
	}

	public void setReportName(String s)	
	{
		reportName=s;
	}

	public Integer getAmpAdjustmentId() 
	{
			return ampAdjustmentId;
	}

	public void setAmpAdjustmentId(Integer ampAdjustmentId)	
	{
		this.ampAdjustmentId=ampAdjustmentId;
	}

	/**
	 * @return
	 */
	public String getAdjustmentFlag() {
		return adjustmentFlag;
	}

	/**
	 * @param string
	 */
	public void setAdjustmentFlag(String string) {
		adjustmentFlag = string;
	}

	/**
	 * @return
	 */
	public int getYrCount() {
		return yrCount;
	}

	/**
	 * @param string
	 */
	public void setYrCount(int yrCount) {
		this.yrCount = yrCount;
	}
}
