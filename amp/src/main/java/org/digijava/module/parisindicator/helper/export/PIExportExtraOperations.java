package org.digijava.module.parisindicator.helper.export;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

import java.util.Collection;

public interface PIExportExtraOperations {

    public abstract void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception;

    public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear)
            throws Exception;

    public Object[][] generateDataSource(int[][] rows, int startYear, int endYear) throws Exception;
}
