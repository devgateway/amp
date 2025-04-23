package org.digijava.module.gpi.helper.export;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

import java.util.Collection;

public interface GPIExportExtraOperations {

    public abstract void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception;

    public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear) throws Exception;

    public Object[][] generateDataSource(int[][] rows, int startYear, int endYear) throws Exception;
}
