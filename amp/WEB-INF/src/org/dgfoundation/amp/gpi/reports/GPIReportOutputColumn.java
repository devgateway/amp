package org.dgfoundation.amp.gpi.reports;

import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * class holding metadata about a gpi-report-output-column. A "column" in this
 * context might be either a Measure or a Column
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReportOutputColumn implements Comparable<GPIReportOutputColumn> {
    /**
     * the <strong>localized</strong> name of the column<br />
     */
    public final String columnName;

    /** the <strong> unlocalized</strong> name of the column */
    public final String originalColumnName;

    /** the description of the column */
    public final String description;

    public GPIReportOutputColumn(ReportOutputColumn roc) {
        this(roc.columnName, roc.originalColumnName, roc.description);
    }

    public GPIReportOutputColumn(String originalColumnName) {
        this(TranslatorWorker.translateText(originalColumnName), originalColumnName, null);
    }

    public GPIReportOutputColumn(String originalColumnName, String description) {
        this(TranslatorWorker.translateText(originalColumnName), originalColumnName,
                TranslatorWorker.translateText(description));
    }

    public GPIReportOutputColumn(String columnName, String originalColumnName, String description) {
        this.columnName = columnName;

        if (columnName == null || columnName.isEmpty()) {
            throw new NullPointerException("The translated name of the column is empty. "
                    + "The original column name is '" + originalColumnName + "'");
        }

        this.originalColumnName = originalColumnName;
        this.description = description;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOriginalColumnName() {
        return originalColumnName;
    }

    @Override
    public String toString() {
        return originalColumnName;
    }

    @Override
    public final int hashCode() {
        return getColumnName().hashCode();
    }

    @Override
    public boolean equals(Object oth) {
        return this.getColumnName().equals(((GPIReportOutputColumn) oth).getColumnName());
    }

    @Override
    public int compareTo(GPIReportOutputColumn oth) {
        return this.getColumnName().compareTo(oth.getColumnName());
    }
}
