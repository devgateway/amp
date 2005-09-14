
package org.digijava.module.aim.form ;

import java.util.Collection ;
import org.apache.struts.action.ActionForm ;

public class AdvancedReportForm extends ActionForm 
{
	private Collection ampMeasures = null; // Contains the avaliable measures from Database
	private Collection ampColumns = null; // Contains the columns got from the DB
	private Long selectedColumns[] = null; // list of columns after selecting.
	private Long removeColumns[] = null; // list of columns after selecting.
	private Long columnId;
	private Collection addedColumns = null; // contains the columns that have been added.
	private boolean isAdd = false;
	private String step = null;
	private Collection finalData = null;
	private String reportTitle = "";
	private String reportDescription = "";
	private String moveColumn="";
	private Collection intermediate = null;
	private Collection addedMeasures = null;
	private Collection columnHierarchie = null; // contains the columns that have been added.
	private Long selectedAdjustmentType[] = null;
	private Collection adjustType = null;
	private Collection selAdjustType = null;
	private Long removeAdjustType[] = null; 
	private String createdReportId = "";
	private String imageUrl="";

	private String donor ;
	private String title ;
	private String startDate ;
	private String closeDate ;
	private Collection report ;
	private Collection forecastYear ;
	private int forcastYear1 ;
	private int forcastYear2 ;
	private int forcastYear3 ;
	private String currency ;
	private String perspective ;
	private String perspectiveFilter ;
	private int fiscalCalId;
	private String region ;
	private int modality;
	private String donorName;
	private String sectorName;
	private String status;
	private Long startYear;
	private Long startMonth;
	private Long startDay;
	
	private Long closeYear;
	private Long closeMonth;
	private Long closeDay;
	
	private int pageCount;
	private Collection statusColl;
	private Long ampStatusId;
	private Collection sectorColl;
	private Long ampSectorId;
	private Collection regionColl;
	private String ampLocationId;
	private Collection donorColl;
	private Long ampOrgId;
	private Collection modalityColl;
	private Long ampModalityId;
	private Collection currencyColl;
	private Long ampCurrencyId;
	private String ampCurrencyCode;
	private Collection ampProjects;
	private Collection pages;
	private Integer page;
	private Long ampFromYear;
	private Long ampToYear;
	private Collection ampFromYears;
	private Collection ampToYears;
	private Collection ampStartYears;
	private Collection ampCloseYears;
	private Collection ampStartDays;
	private Collection ampCloseDays;
	private Collection fiscalYears;
	private int fromYear;
	private int toYear;
	private String workspaceName;
	private String workspaceType;
	private String reportName;
	private Integer ampAdjustmentId;
	private Collection fiscalYearRange;
	private int totalColumns;
	private int fundColumns;
	private Collection totDisbFund;
	private Collection totFund;
	private String totComm;
	private String totDisb;
	private String totUnDisb;

	private Collection titles;
	private String pieImageUrl="";
	private String barImageUrl="";

	private String acCommFlag;
	private String acDisbFlag;
	private String acExpFlag;
	private String plCommFlag;
	private String plDisbFlag;
	private String plExpFlag;

	
	public String getPieImageUrl()
	{
		return pieImageUrl;
	}
	public void setPieImageUrl(String str)
	{
		pieImageUrl=str;
	}

	public String getBarImageUrl()
	{
		return barImageUrl;
	}
	public void setBarImageUrl(String str)
	{
		barImageUrl=str;
	}

	public String getMoveColumn() {
		return moveColumn;
	}
	public void setMoveColumn(String moveColumn) {
		this.moveColumn = moveColumn;
	}
	
	public Collection getFinalData() {
		return finalData;
	}
	public void setFinalData(Collection finalData) {
		this.finalData = finalData;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public Collection getAddedColumns() {
		return addedColumns;
	}
	public void setAddedColumns(Collection addedColumns) {
		this.addedColumns = addedColumns;
	}
	public Collection getAmpColumns() {
		return ampColumns;
	}
	public void setAmpColumns(Collection ampColumns) {
		this.ampColumns = ampColumns;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public Long[] getSelectedColumns() {
		return selectedColumns;
	}
	public void setSelectedColumns(Long[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}
	
	public Long[] getRemoveColumns() {
		return removeColumns;
	}
	public void setRemoveColumns(Long[] removeColumns) {
		this.removeColumns = removeColumns;
	}
	public String getReportDescription() {
		return reportDescription;
	}
	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public Collection getColumnHierarchie() {
		return columnHierarchie;
	}
	public void setColumnHierarchie(Collection columnHierarchie) {
		this.columnHierarchie = columnHierarchie;
	}
	public Collection getIntermediate() {
		return intermediate;
	}
	public void setIntermediate(Collection intermediate) {
		this.intermediate = intermediate;
	}
	public Collection getAmpMeasures() {
		return ampMeasures;
	}
	public void setAmpMeasures(Collection ampMeasures) {
		this.ampMeasures = ampMeasures;
	}
	public Collection getAddedMeasures() {
		return addedMeasures;
	}
	public void setAddedMeasures(Collection addedMeasures) {
		this.addedMeasures = addedMeasures;
	}

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
	

	private String filterFlag;
	private String adjustmentFlag;
	private boolean calendarFlag;
	private boolean perspectiveFlag;
	private boolean yearFlag;
	private String goFlag;
// for reports	
	private int yrCount;

	public Collection getFiscalYearRange() {
		return fiscalYearRange;
	}

	public int getTotalColumns() {
		return totalColumns;
	}

	public void setFiscalYearRange(Collection collection) {
		fiscalYearRange = collection;
	}
	public void setTotalColumns(int i) {
		totalColumns = i;
	}
	
public int getYrCount() {
	return yrCount;
}
public void setYrCount(int yrCount) {
	this.yrCount = yrCount;
}

private int yrDiff;
	public int getYrDiff()
	{		return yrDiff;		}
	
	public void setYrDiff(int yy)
	{		yrDiff = yy;	}
	
	public Long getAmpFromYear() {
		return ampFromYear;
	}
	public Long getAmpToYear() {
		return ampToYear;
	}
	public void setAmpFromYear(Long i) {
		ampFromYear = i;
	}
	public void setAmpToYear(Long i) {
		ampToYear = i;
	}

	public int getFromYear() {
		return fromYear;
	}
	public int getToYear() {
		return toYear;
	}
	public void setFromYear(int i) {
		fromYear = i;
	}
	public void setToYear(int i) {
		toYear = i;
	}


// modified by priyajith
	private Collection allReports;

	public Collection getAllReports() {
		return allReports;
	}

	public void setAllReports(Collection allReports) {
		this.allReports = allReports;
	}

	public Collection getTitles() {
		return titles;
	}

	public void setTitles(Collection c) {
		this.titles = c;
	}



		public String getDonor() 
		{
			return donor;
		}

		public void setDonor(String string) 
		{
			donor = string;
		}

		
	
		public String getTitle() 
		{
			return title;
		}
	
		public void setTitle(String string) 
		{
			title = string;
		}

		public String getStartDate() 
		{
			return startDate;
		}

		public void setStartDate(String string) 
		{
			startDate = string;
		}

		public String getCloseDate() 
		{
			return closeDate;
		}

		public void setCloseDate(String string) 
		{
			closeDate = string;
		}
		public Collection getReport() 
		{
			return report;
		}
		public void setReport(Collection collection) 
		{
			report = collection;
		}
	
	/**
	 * @return
	 */
	public int getForcastYear1() {
		return forcastYear1;
	}

	/**
	 * @return
	 */
	public int getForcastYear2() {
		return forcastYear2;
	}

	/**
	 * @return
	 */
	public int getForcastYear3() {
		return forcastYear3;
	}

	public Collection getForecastYear() {
		return forecastYear;
	}

	/**
	 * @param i
	 */
	public void setForcastYear1(int i) {
		forcastYear1 = i;
	}

	/**
	 * @param i
	 */
	public void setForcastYear2(int i) {
		forcastYear2 = i;
	}

	/**
	 * @param i
	 */
	public void setForcastYear3(int i) {
		forcastYear3 = i;
	}

	public void setForecastYear(Collection c) {
		forecastYear = c;
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
	public String getPerspective() {
		return perspective;
	}

	/**
	 * @param string
	 */
	public void setPerspective(String string) {
		perspective = string;
	}

	/**
	 * @return
	 */
	public int getFiscalCalId() {
		return fiscalCalId;
	}

	/**
	 * @param i
	 */
	public void setFiscalCalId(int i) {
		fiscalCalId = i;
	}

	
	/**
	 * @return
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param string
	 */
	public void setRegion(String string) {
		region = string;
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
	public String getStatus() {
		return status;
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
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param i
	 */
	public void setPageCount(int i) {
		pageCount = i;
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
	public Long getAmpSectorId() {
		return ampSectorId;
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
	public void setAmpSectorId(Long long1) {
		ampSectorId = long1;
	}

	/**
	 * @param collection
	 */
	public void setSectorColl(Collection collection) {
		sectorColl = collection;
	}

	/**
	 * @return
	 */
	public Collection getRegionColl() {
		return regionColl;
	}

	/**
	 * @param collection
	 */
	public void setRegionColl(Collection collection) {
		regionColl = collection;
	}

	/**
	 * @return
	 */
	public String getAmpLocationId() {
		return ampLocationId;
	}

	/**
	 * @param long1
	 */
	public void setAmpLocationId(String s) {
		ampLocationId = s;
	}

	/**
	 * @return
	 */
	public Collection getDonorColl() {
		return donorColl;
	}

	/**
	 * @param collection
	 */
	public void setDonorColl(Collection collection) {
		donorColl = collection;
	}

	/**
	 * @return
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}

	/**
	 * @param long1
	 */
	public void setAmpOrgId(Long long1) {
		ampOrgId = long1;
	}

	
	/**
	 * @return
	 */
	public Collection getModalityColl() {
		return modalityColl;
	}

	/**
	 * @param collection
	 */
	public void setModalityColl(Collection collection) {
		modalityColl = collection;
	}

	/**
	 * @return
	 */
	public Long getAmpModalityId() {
		return ampModalityId;
	}

	/**
	 * @param long1
	 */
	public void setAmpModalityId(Long long1) {
		ampModalityId = long1;
	}

	/**
	 * @return
	 */
	public String getAmpCurrencyCode() {
		return ampCurrencyCode;
	}

	/**
	 * @return
	 */
	public Collection getCurrencyColl() {
		return currencyColl;
	}

	/**
	 * @param string
	 */
	public void setAmpCurrencyCode(String string) {
		ampCurrencyCode = string;
	}

	/**
	 * @param collection
	 */
	public void setCurrencyColl(Collection collection) {
		currencyColl = collection;
	}

	/**
	 * @return
	 */
	public Long getAmpCurrencyId() {
		return ampCurrencyId;
	}

	/**
	 * @param long1
	 */
	public void setAmpCurrencyId(Long long1) {
		ampCurrencyId = long1;
	}

	/**
	 * @return
	 */
	public Collection getAmpProjects() {
		return ampProjects;
	}

	/**
	 * @return
	 */
	public Collection getPages() {
		return pages;
	}

	public Integer getPage()
	{
		return page;
	}

	public String getFilterFlag() {
		return filterFlag;
	}

	public String getGoFlag() {
		return goFlag;
	}
	/**
	 * @param collection
	 */
	public void setAmpProjects(Collection collection) {
		ampProjects = collection;
	}

	/**
	 * @param collection
	 */
	public void setPages(Collection collection) {
		pages = collection;
	}

	public void setPage(Integer page)
	{
		this.page=page;
	}

//--------------Function for Filters-----------------------------
	public void setFilterFlag(String s)	{
		filterFlag=s;
	}
	public boolean isCalendarFlag() {
		return calendarFlag;
	}
	public void setCalendarFlag(boolean b) {
		calendarFlag = b;
	}
	public boolean isPerspectiveFlag() {
		return perspectiveFlag;
	}
	public boolean isYearFlag() {
		return yearFlag;
	}
	public void setPerspectiveFlag(boolean b) {
		perspectiveFlag = b;
	}
	public void setYearFlag(boolean b) {
		yearFlag = b;
	}
	public void setGoFlag(String s) {
		goFlag = s;
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

	/**
	 * @return
	 */
	public String getPerspectiveFilter() {
		return perspectiveFilter;
	}

	/**
	 * @param string
	 */
	public void setPerspectiveFilter(String string) {
		perspectiveFilter = string;
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
	public String getTotComm() {
		return totComm;
	}

	/**
	 * @return
	 */
	public String getTotDisb() {
		return totDisb;
	}

	/**
	 * @return
	 */
	public String getTotUnDisb() {
		return totUnDisb;
	}

	/**
	 * @param string
	 */
	public void setTotComm(String string) {
		totComm = string;
	}

	/**
	 * @param string
	 */
	public void setTotDisb(String string) {
		totDisb = string;
	}

	/**
	 * @param string
	 */
	public void setTotUnDisb(String string) {
		totUnDisb = string;
	}

	public void setTotDisbFund(Collection collection) {
		totDisbFund = collection;
	}

	public Collection getTotDisbFund() {
		return totDisbFund;
	}

	public void setTotFund(Collection collection) {
		totFund = collection;
	}

	public Collection getTotFund() {
		return totFund;
	}
	public Collection getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(Collection adjustType) {
		this.adjustType = adjustType;
	}
	public Collection getSelAdjustType() {
		return selAdjustType;
	}
	public void setSelAdjustType(Collection selAdjustType) {
		this.selAdjustType = selAdjustType;
	}
	public Long[] getSelectedAdjustmentType() {
		return selectedAdjustmentType;
	}
	public void setSelectedAdjustmentType(Long[] selectedAdjustmentType) {
		this.selectedAdjustmentType = selectedAdjustmentType;
	}
	public Long[] getRemoveAdjustType() {
		return removeAdjustType;
	}
	public void setRemoveAdjustType(Long[] removeAdjustType) {
		this.removeAdjustType = removeAdjustType;
	}

	
	public String getAcCommFlag() {
		return acCommFlag;
	}

	public void setAcCommFlag(String string) {
		acCommFlag = string;
	}

	public String getAcDisbFlag() {
		return acDisbFlag;
	}

	public void setAcDisbFlag(String string) {
		acDisbFlag = string;
	}

	public String getAcExpFlag() {
		return acExpFlag;
	}

	public void setAcExpFlag(String string) {
		acExpFlag = string;
	}

	public String getPlCommFlag() {
		return plCommFlag;
	}

	public void setPlCommFlag(String string) {
		plCommFlag = string;
	}

	public String getPlDisbFlag() {
		return plDisbFlag;
	}

	public void setPlDisbFlag(String string) {
		plDisbFlag = string;
	}

	public String getPlExpFlag() {
		return plExpFlag;
	}

	public void setPlExpFlag(String string) {
		plExpFlag = string;
	}

	public int getFundColumns() {
		return fundColumns;
	}

	public void setFundColumns(int i) {
		fundColumns = i;
	}
	public String getCreatedReportId() {
		return createdReportId;
	}
	public void setCreatedReportId(String createdReportId) {
		this.createdReportId = createdReportId;
	}

} //		End of Class

