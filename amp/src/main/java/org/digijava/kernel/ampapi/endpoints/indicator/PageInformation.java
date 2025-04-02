package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageInformation {
    
    @JsonProperty(IndicatorEPConstants.RECORDS_PER_PAGE)
    private Integer recordsPerPage;
    
    @JsonProperty(IndicatorEPConstants.CURRENT_PAGE_NUMBER)
    private Integer currentPageNumber;
    
    @JsonProperty(IndicatorEPConstants.TOTAL_PAGE_COUNT)
    private Integer totalPageCount;
    
    @JsonProperty(IndicatorEPConstants.TOTAL_RECORDS)
    private Integer totalRecords;
    
    public Integer getRecordsPerPage() {
        return recordsPerPage;
    }
    
    public void setRecordsPerPage(Integer recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }
    
    public Integer getCurrentPageNumber() {
        return currentPageNumber;
    }
    
    public void setCurrentPageNumber(Integer currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }
    
    public Integer getTotalPageCount() {
        return totalPageCount;
    }
    
    public void setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
    
    public Integer getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }
}
