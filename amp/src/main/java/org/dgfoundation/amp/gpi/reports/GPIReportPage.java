package org.dgfoundation.amp.gpi.reports;

import java.util.List;
import java.util.Map;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportPage {

    /**
     * Top report headers list
     */
    protected List<GPIReportOutputColumn> headers;

    /**
     * The report data. Each lines hold information for each
     * {@link GPIReportOutputColumn} column
     */
    protected List<Map<GPIReportOutputColumn, String>> contents;

    protected int recordsPerPage;

    protected int currentPageNumber;

    protected int totalPageCount;

    protected int totalRecords;

    public List<GPIReportOutputColumn> getHeaders() {
        return headers;
    }

    public void setHeaders(List<GPIReportOutputColumn> headers) {
        this.headers = headers;
    }

    public List<Map<GPIReportOutputColumn, String>> getContents() {
        return contents;
    }

    public void setContents(List<Map<GPIReportOutputColumn, String>> contents) {
        this.contents = contents;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

}
