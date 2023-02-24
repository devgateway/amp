package org.digijava.module.gpi.helper.export;

import java.util.Collection;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

public interface GPIExportExtraOperations {

    public abstract void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception;

    public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear) throws Exception;

    public Object[][] generateDataSource(int[][] rows, int startYear, int endYear) throws Exception;
}
