package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ReportsForm extends ActionForm {

    private Boolean reportEdit = null;
    private Long currentMemberId = null;;

    private Collection reports = null;
    private Collection reportsList;
    private int tempNumResults = 10;
    private int defReportsPerPage = 10;
    private int defaultNumResults=10;
    private int pageSize = 10;
    private Long reportId = null;
    private String name = null;
    private String description = null;
    private String flag = null;
    private Long selReports[] = null;
    private String addReport = null;
    private String removeReports = null;
    private String assignReports = null;
    private Long teamId = null;
    private String teamName = null;
    private Long memberId = null;
    private String memberName = null;
    private int currentPage;
    private int totalPages;
    private int page;
    private int pagesToShow;
    private int offset;
    private boolean showReportList;

    private Boolean showTabs    = false;
    private String keyword;
    private String action;
    private boolean tabs;
    private int sortBy;
    private boolean reset;
    
    private Long selectedReportCategory= new Long(0);
    private Boolean onlyFavourites;

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
    

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public ReportSortBy getSortByColumn() {
        return sortByColumn;
    }

    public void setSortByColumn(ReportSortBy sortByColumn) {
        this.sortByColumn = sortByColumn;
    }
    private ReportSortBy sortByColumn;

   

    public boolean getTabs() {
        return tabs;
    }

    public void setTabs(boolean tabs) {
        this.tabs = tabs;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public int getOffset() {
        final int value;
        if (getCurrentPage() + getPagesToShow() / 2 < getTotalPages()){
            int add = getPagesToShow() % 2 == 0 ? 1 : 0;
            value = getCurrentPage() - getPagesToShow() / 2 + add;
        } else {
            value = getTotalPages() - getPagesToShow();
        }
        setOffset(value);
        return this.offset;
    }
 
    public void setOffset(int offset) {
        this.offset = offset;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long[] getSelReports() {
        return this.selReports;
    }

    public void setSelReports(Long selReports[]) {
        this.selReports = selReports;
    }

    public String getAddReport() {
        return this.addReport;
    }

    public void setAddReport(String addReport) {
        this.addReport = addReport;
    }

    public String getRemoveReports() {
        return this.removeReports;
    }

    public void setRemoveReports(String removeReports) {
        this.removeReports = removeReports;
    }

    public String getAssignReports() {
        return this.assignReports;
    }

    public void setAssignReports(String assignReports) {
        this.assignReports = assignReports;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Collection getReports() {
        return (this.reports);
    }

    public void setReports(Collection reports) {
        this.reports = reports;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getReportEdit() {
        return reportEdit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setReportEdit(Boolean reportEdit) {
        this.reportEdit = reportEdit;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Long getCurrentMemberId() {
        return currentMemberId;
    }

    public void setCurrentMemberId(Long currentMemberId) {
        this.currentMemberId = currentMemberId;
    }

    public Collection getReportsList() {
        return reportsList;
    }

    public void setReportsList(Collection reportsList) {
        this.reportsList = reportsList;
    }

    public int getTempNumResults() {
        return tempNumResults;
    }

    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }

    public int getDefReportsPerPage() {
        return defReportsPerPage;
    }

    public void setDefReportsPerPage(int defReportsPerPage) {
        this.defReportsPerPage = defReportsPerPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getShowTabs() {
        return showTabs;
    }

    public void setShowTabs(Boolean showTabs) {
        this.showTabs = showTabs;
    }

    public void setShowReportList(boolean showReportList) {
        this.showReportList = showReportList;
    }

    public boolean isShowReportList() {
        return showReportList;
    }
    
    public Long getSelectedReportCategory() {
        return selectedReportCategory;
    }

    public void setSelectedReportCategory(Long selectedReportCategory) {
        this.selectedReportCategory = selectedReportCategory;
    }

    public Boolean getOnlyFavourites() {
        return onlyFavourites;
    }

    public void setOnlyFavourites(Boolean onlyFavourites) {
        this.onlyFavourites = onlyFavourites;
    }

    public int getDefaultNumResults() {
        return defaultNumResults;
    }

    public void setDefaultNumResults(int defaultNumResults) {
        this.defaultNumResults = defaultNumResults;
    }

    public enum ReportSortBy{
     NAME_ASC(1),
     NAME_DESC(2),
     OWNER_ASC(3),
     OWNER_DESC(4),
     DATE_ASC(5),DATE_DESC(6),
     NONE(7);
     private final int sortBy;
     ReportSortBy(int sortBy) {
         this.sortBy = sortBy;
     }
     public int getSortBy(){
         return sortBy;
     }
     
 }
}

