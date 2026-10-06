package org.digijava.module.gpi.helper;

import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.model.GPIFilter;

import java.util.Collection;

public interface GPIOperations {

    public abstract Collection<GPIReportAbstractRow> generateReport(Collection<Object[]> commonData, GPIFilter filter);

    public abstract Collection<GPIReportAbstractRow> reportPostProcess(Collection<GPIReportAbstractRow> baseReport, int startYear, int endYear) throws Exception;
}
