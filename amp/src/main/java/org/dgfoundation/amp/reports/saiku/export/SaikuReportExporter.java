package org.dgfoundation.amp.reports.saiku.export;

import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * @author Viorel Chihai
 *
 */
public interface SaikuReportExporter {
    
    /**
     * @param report
     * @param dualReport
     * @return
     * @throws Exception
     */
    public abstract byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception;
}
