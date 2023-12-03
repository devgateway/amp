package org.digijava.module.gpi.helper;

import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;

import java.util.Collection;

public abstract class GPIAbstractReport implements GPIOperations {

    /*
     * This field is changed by each report.
     */
    private final String reportCode = "";

    /*
     * This collection has all the rows to generate the report.
     */
    private Collection<GPIReportAbstractRow> reportRows;

    /*
     * Mini tables for reports 5a and 5b.
     */
    private int[][] miniTable;

    public String getReportCode() {
        return reportCode;
    }

    public Collection<GPIReportAbstractRow> getReportRows() {
        return reportRows;
    }

    public void setReportRows(Collection<GPIReportAbstractRow> reportRows) {
        this.reportRows = reportRows;
    }

    public int[][] getMiniTable() {
        return miniTable;
    }

    public void setMiniTable(int[][] miniTable) {
        this.miniTable = miniTable;
    }
}
