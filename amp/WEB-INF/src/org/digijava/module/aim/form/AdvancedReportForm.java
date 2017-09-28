package org.digijava.module.aim.form ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AdvancedReportForm extends ActionForm 
{
    
    public String getFlag(int type) {
        switch(type) {
        case AmpFund.COMM: return acCommFlag;
        case AmpFund.DISB: return acDisbFlag;
        case AmpFund.EXP: return acExpFlag;
        case AmpFund.PL_COMM: return plCommFlag;
        case AmpFund.PL_DISB: return plDisbFlag;
        case AmpFund.PL_EXP: return plExpFlag;
        case AmpFund.UNDISB: return acBalFlag;
        default: return null;
        }
        
    }

    private Boolean reportIsModified = false;
    private Boolean reportEdit=null;
    private String currentTabName;
    private Boolean blankReportName = false;
    
    private String levelPicked=null;
    private String levelSorter=null;
    private String levelSortOrder=null;
    
    private String pdfPageSize=null;
    private long donors[];
    private long statuses[];
    private long sectors[];
    private String regions[];
    private Boolean hideActivities = false;
    private Boolean drilldownTab = null;
    private Boolean publicReport = null;
    private Collection ampMeasures = null; // Contains the avaliable measures from Database
    private Collection ampColumns = null; // Contains the columns got from the DB
    private Long selectedColumns[] = null; // list of columns after selecting.
    private Long removeColumns[] = null; // list of columns after selecting.
    private Long columnId;
    private Collection addedColumns = null; // contains the columns that have been added.
    private Collection notHierarchyColumns = null; //Columns that are in step 3 but are not selected as hierarchy
    private boolean isAdd = false;
    private String step = null;
    private Collection finalData = null;
    private String reportTitle = "";
    private String reportDescription = "";
    private String reportType = "";
    private String arReportType = "";
    private String reportOption;
    private String moveColumn="";
    private Collection intermediate = null;
    private Collection addedMeasures = null;
    private Collection columnHierarchie = null; // contains the columns that have been added.
    private Long selectedAdjustmentType[] = null;
    private Collection adjustType = null;
    private Collection selAdjustType = null;
    private Long removeAdjustType[] = null; 
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
    private String option;
    private Collection options;
    private boolean canMakePublic=false;
    
    private boolean inEditingMode   = false; // true if in editing mode
    private long dbReportId         = 0; // If in editing mode, it represents the id of the AmpReports object in the database
    
    private Integer maxStep = new Integer(0);

    private HashMap ampTreeColumns;
    
    private boolean duplicatedReportName;
    private String duplicatedReportOwner;
    
    /* For X-Level architecture */
    private Long activityLevel;
    private HashMap<Long, Collection<AmpCategoryValue> > columnToLevel  = new HashMap<Long, Collection<AmpCategoryValue>>();
    private HashMap<Long, Collection<AmpCategoryValue> > measureToLevel = new HashMap<Long, Collection<AmpCategoryValue>>();
    private List<AmpReportColumn> columnsSelection                      = new ArrayList<AmpReportColumn>();
    private List<AmpReportMeasures> measuresSelection                   = new ArrayList<AmpReportMeasures>();
    private List<AmpReportHierarchy> hierarchiesSelection               = new ArrayList<AmpReportHierarchy>();
    private HashMap<String, Long> selectedColumnToLevel                 = new HashMap<String, Long> ();
    private HashMap<String, Long> selectedMeasureToLevel                    = new HashMap<String, Long> ();
    
    private Integer [] removeReportColumnsLevel                                 = null;
    /* END - For X-Level architecture */
    
    public HashMap getAmpTreeColumns() {
        return ampTreeColumns;
    }
    public void setAmpTreeColumns(HashMap ampTreeColumns) {
        this.ampTreeColumns = ampTreeColumns;
    }
    public Integer getMaxStep() {
        return maxStep;
    }
    public void setMaxStep(Integer maxStep) {
        this.maxStep = maxStep;
    }
    /**
     * @return Returns the multiReport.
     */
    public Collection getMultiReport() {
        return multiReport;
    }
    /**
     * @param multiReport The multiReport to set.
     */
    public void setMultiReport(Collection multiReport) {
        this.multiReport = multiReport;
    }
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
    private String ampComponentId;
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
    private Collection risks;
    private int fromYear;
    private int toYear;
    private String workspaceName;
    private String workspaceType;
    private String reportName;
    private Integer ampAdjustmentId;
    private Collection fiscalYearRange;
    private int totalColumns;
    private int fundColumns;
    private int dimColumns;
    private Collection totDisbFund;
    private Collection totFund;
    private String totComm;
    private String totDisb;
    private String totUnDisb;

    private Collection titles;
    private Collection multiReport;
    private String pieImageUrl="";
    private String barImageUrl="";

    private String acCommFlag;
    private String acDisbFlag;
    private String acExpFlag;
    private String plCommFlag;
    private String plDisbFlag;
    private String plExpFlag;
    private String acBalFlag;
    private String hierarchyFlag;

    private int measureCount = 0;  
    private int quarterColumns;
        
    public int getMeasureCount() {
        return measureCount;
    }
    public void setMeasureCount(int measureCount) {
        this.measureCount = measureCount;
    }

    public int getQuarterColumns() {
        return quarterColumns;
    }
    public void setQuarterColumns(int quarterColumns) {
        this.quarterColumns = quarterColumns;
    }
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
    {       return yrDiff;      }
    
    public void setYrDiff(int yy)
    {       yrDiff = yy;    }
    
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
    public void setFilterFlag(String s) {
        filterFlag=s;
    }
    public boolean isCalendarFlag() {
        return calendarFlag;
    }
    public void setCalendarFlag(boolean b) {
        calendarFlag = b;
    }
    public boolean isYearFlag() {
        return yearFlag;
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

    public String getAcBalFlag() {
        return acBalFlag;
    }

    public void setAcBalFlag(String string) {
        acBalFlag = string;
    }

    public String getHierarchyFlag() {
        return hierarchyFlag;
    }

    public void setHierarchyFlag(String string) {
        hierarchyFlag = string;
    }

    public int getFundColumns() {
        return fundColumns;
    }

    public void setFundColumns(int i) {
        fundColumns = i;
    }

    public int getDimColumns() {
        return dimColumns;
    }

    public void setDimColumns(int i) {
        dimColumns = i;
    }

    /**
     * @return Returns the reportType.
     */
    public String getReportType() {
        return reportType;
    }
    /**
     * @param reportType The reportType to set.
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String string) {
        option = string;
    }
    public String getReportOption() {
        return reportOption;
    }

    public void setReportOption(String string) {
        reportOption = string;
    }
    public Collection getOptions() {
        return options;
    }

    public void setOptions(Collection c) {
        options = c;
    }

    
    /**
     * @return Returns the arReportType.
     */
    public String getArReportType() {
        return arReportType;
    }
    /**
     * @param arReportType The arReportType to set.
     */
    public void setArReportType(String arReportType) {
        this.arReportType = arReportType;
    }
    /**
     * @return Returns the ampComponentId.
     */
    public String getAmpComponentId() {
        return ampComponentId;
    }
    /**
     * @param ampComponentId The ampComponentId to set.
     */
    public void setAmpComponentId(String ampComponentId) {
        this.ampComponentId = ampComponentId;
    }
    /**
     * @return Returns the hideActivities.
     */
    public Boolean getHideActivities() {
        return hideActivities;
    }
    /**
     * @param hideActivities The hideActivities to set.
     */
    public void setHideActivities(Boolean hideActivities) {
        this.hideActivities = hideActivities;
    }
    public Collection getRisks() {
        return risks;
    }
    public void setRisks(Collection risks) {
        this.risks = risks;
    }
    /**
     * @return Returns the donors.
     */
    public long[] getDonors() {
        return donors;
    }
    /**
     * @param donors The donors to set.
     */
    public void setDonors(long[] donors) {
        this.donors = donors;
    }
    /**
     * @return Returns the sectors.
     */
    public long[] getSectors() {
        return sectors;
    }
    /**
     * @param sectors The sectors to set.
     */
    public void setSectors(long[] sectors) {
        this.sectors = sectors;
    }
    /**
     * @return Returns the statuses.
     */
    public long[] getStatuses() {
        return statuses;
    }
    /**
     * @param statuses The statuses to set.
     */
    public void setStatuses(long[] statuses) {
        this.statuses = statuses;
    }
    /**
     * @return Returns the regions.
     */
    public String[] getRegions() {
        return regions;
    }
    /**
     * @param regions The regions to set.
     */
    public void setRegions(String[] regions) {
        this.regions = regions;
    }
    /**
     * @return Returns the pdfPageSize.
     */
    public String getPdfPageSize() {
        return pdfPageSize;
    }
    /**
     * @param pdfPageSize The pdfPageSize to set.
     */
    public void setPdfPageSize(String pdfPageSize) {
        this.pdfPageSize = pdfPageSize;
    }
    public Boolean getDrilldownTab() {
        return drilldownTab;
    }
    public void setDrilldownTab(Boolean drilldownTab) {
        this.drilldownTab = drilldownTab;
    }
    public String getLevelPicked() {
        return levelPicked;
    }
    public void setLevelPicked(String levelPicked) {
        this.levelPicked = levelPicked;
    }
    public String getLevelSorter() {
        return levelSorter;
    }
    public void setLevelSorter(String levelSorter) {
        this.levelSorter = levelSorter;
    }
    public Boolean getPublicReport() {
        return publicReport;
    }
    public void setPublicReport(Boolean publicReport) {
        this.publicReport = publicReport;
    }
    public String getLevelSortOrder() {
        return levelSortOrder;
    }
    public void setLevelSortOrder(String levelsortOrder) {
        this.levelSortOrder = levelsortOrder;
    }
    
    public void setInEditingMode (boolean inEditingMode) {
        this.inEditingMode  = inEditingMode;
    }
    public boolean getInEditingMode () {
        return this.inEditingMode;
    }
    
    public void setDbReportId (long dbReportId) {
        this.dbReportId     = dbReportId;
    }
    public long getDbReportId () {
        return this.dbReportId;
    }
    public void setDuplicatedReportName(boolean duplicatedReportName) {
        this.duplicatedReportName = duplicatedReportName;
    }
    public boolean isDuplicatedReportName() {
        return duplicatedReportName;
    }
    public void setDuplicatedReportOwner(String duplicatedReportOwner) {
        this.duplicatedReportOwner = duplicatedReportOwner;
    }
    public String getDuplicatedReportOwner() {
        return duplicatedReportOwner;
    }
    
    public void reset (ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
        String wasSelectMeasuresStep    = request.getParameter("wasSelectMeasuresStep");
        if (wasSelectMeasuresStep != null )
        {
        this.setPublicReport(false);
        this.setDrilldownTab(false);
        this.setHideActivities(false);
        this.setBlankReportName(false);
        }
    }
    public Boolean getReportEdit() {
        return reportEdit;
    }
    public void setReportEdit(Boolean reportEdit) {
        this.reportEdit = reportEdit;
    }
    public String getCurrentTabName() {
        return currentTabName;
    }
    public void setCurrentTabName(String currentTabName) {
        this.currentTabName = currentTabName;
    }
    public Long getActivityLevel() {
        return activityLevel;
    }
    public void setActivityLevel(Long activityLevel) {
        this.activityLevel = activityLevel;
    }
    public HashMap<Long, Collection<AmpCategoryValue>> getColumnToLevel() {
        return columnToLevel;
    }
    public void setColumnToLevel(
            HashMap<Long, Collection<AmpCategoryValue>> columnToLevel) {
        this.columnToLevel = columnToLevel;
    }
    

    public HashMap<Long, Collection<AmpCategoryValue>> getMeasureToLevel() {
        return measureToLevel;
    }
    public void setMeasureToLevel(
            HashMap<Long, Collection<AmpCategoryValue>> measureToLevel) {
        this.measureToLevel = measureToLevel;
    }
    
    
    
    
    
    public Integer[] getRemoveReportColumnsLevel() {
        return removeReportColumnsLevel;
    }
    public void setRemoveReportColumnsLevel(Integer[] removeReportColumnsLevel) {
        this.removeReportColumnsLevel = removeReportColumnsLevel;
    }
    public List<AmpReportColumn> getColumnsSelection() {
        return columnsSelection;
    }
    public void setColumnsSelection(List<AmpReportColumn> columnsSelection) {
        this.columnsSelection = columnsSelection;
    }
    public List<AmpReportMeasures> getMeasuresSelection() {
        return measuresSelection;
    }
    public void setMeasuresSelection(List<AmpReportMeasures> measuresSelection) {
        this.measuresSelection = measuresSelection;
    }
    
        
    public List<AmpReportHierarchy> getHierarchiesSelection() {
        return hierarchiesSelection;
    }
    public void setHierarchiesSelection(
            List<AmpReportHierarchy> hierarchiesSelection) {
        this.hierarchiesSelection = hierarchiesSelection;
    }
    public HashMap<String,Long> getSelectedColumnToLevelHM() {
        return selectedColumnToLevel;
    }
    public Long getSelectedColumnToLevel(String columnId) {
        return selectedColumnToLevel.get(columnId);
    }
    public void setSelectedColumnToLevel(String columnId, Long levelId) {
        selectedColumnToLevel.put(columnId, levelId);
    }
    
    public HashMap<String,Long> getSelectedMeasureToLevelHM() {
        return selectedMeasureToLevel;
    }
    public Long getSelectedMeasureToLevel(String measureId) {
        return selectedMeasureToLevel.get(measureId);
    }
    public void setSelectedMeasureToLevel(String measureId, Long levelId) {
        selectedMeasureToLevel.put(measureId, levelId);
    }
    public Collection getNotHierarchyColumns() {
        return notHierarchyColumns;
    }
    public void setNotHierarchyColumns(Collection notHierarchyColumns) {
        this.notHierarchyColumns = notHierarchyColumns;
    }
    public boolean isCanMakePublic() {
        return canMakePublic;
    }
    public void setCanMakePublic(boolean canMakePublic) {
        this.canMakePublic = canMakePublic;
    }
    /**
     * @return the reportIsModified
     */
    public Boolean getReportIsModified() {
        return reportIsModified;
    }
    /**
     * @param reportIsModified the reportIsModified to set
     */
    public void setReportIsModified(Boolean reportIsModified) {
        this.reportIsModified = reportIsModified;
    }
    public Boolean getBlankReportName() {
        return blankReportName;
    }
    public void setBlankReportName(Boolean blankReportName) {
        this.blankReportName = blankReportName;
    }

    private String statementPositionOptions;
    private String logoPositionOptions;
    private String statementOptions;
    private String dateOptions;
    private String logoOptions;
    
    
    /**
     * @return the statementPositionOptions
     */
    public String getStatementPositionOptions() {
        return this.statementPositionOptions;
    }
    /**
     * @param statementPositionOptions the statementPositionOptions to set
     */
    public void setStatementPositionOptions(String statementPositionOptions) {
        this.statementPositionOptions = statementPositionOptions;
    }
    /**
     * @return the logoPositionOptions
     */
    public String getLogoPositionOptions() {
        return this.logoPositionOptions;
    }
    /**
     * @param logoPositionOptions the logoPositionOptions to set
     */
    public void setLogoPositionOptions(String logoPositionOptions) {
        this.logoPositionOptions = logoPositionOptions;
    }
    /**
     * @return the statementOptions
     */
    public String getStatementOptions() {
        return this.statementOptions;
    }
    /**
     * @param statementOptions the statementOptions to set
     */
    public void setStatementOptions(String statementOptions) {
        this.statementOptions = statementOptions;
    }
    /**
     * @return the dateOptions
     */
    public String getDateOptions() {
        return this.dateOptions;
    }
    /**
     * @param dateOptions the dateOptions to set
     */
    public void setDateOptions(String dateOptions) {
        this.dateOptions = dateOptions;
    }
    /**
     * @return the logoOptions
     */
    public String getLogoOptions() {
        return this.logoOptions;
    }
    /**
     * @param logoOptions the logoOptions to set
     */
    public void setLogoOptions(String logoOptions) {
        this.logoOptions = logoOptions;
    }
    
    
} //        End of Class

