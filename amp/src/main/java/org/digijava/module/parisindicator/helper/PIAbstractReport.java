package org.digijava.module.parisindicator.helper;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

import java.util.Collection;

public abstract class PIAbstractReport implements PIOperations {

    /*
     * This field is changed by each report.
     */
    private final String reportCode = "";

    /*
     * This collection has all the rows to generate the report.
     */
    private Collection<PIReportAbstractRow> reportRows;

    /*
     * Mini tables for reports 5a and 5b.
     */
    private int[][] miniTable;

    public String getReportCode() {
        return reportCode;
    }

    public Collection<PIReportAbstractRow> getReportRows() {
        return reportRows;
    }

    public void setReportRows(Collection<PIReportAbstractRow> reportRows) {
        this.reportRows = reportRows;
    }

    public int[][] getMiniTable() {
        return miniTable;
    }

    public void setMiniTable(int[][] miniTable) {
        this.miniTable = miniTable;
    }
}
