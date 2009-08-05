package org.digijava.module.parisindicator.helper.export;

import java.util.Collection;

import org.digijava.module.parisindicator.helper.PIReportAbstractRow;

public interface PIExportExtraOperations {

	public abstract void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception;

	public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear)
			throws Exception;
}
