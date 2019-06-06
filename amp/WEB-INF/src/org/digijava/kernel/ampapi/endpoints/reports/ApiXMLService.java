package org.digijava.kernel.ampapi.endpoints.reports;

import javax.ws.rs.core.Response;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.converters.GeneratedReportToXmlConverter;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportParameter;
import org.dgfoundation.amp.reports.xml.XmlReportUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;

/**
 * Provides an API for manipulating xml reports
 * 
 * @author Viorel Chihai
 */
public class ApiXMLService {
    
    /** Method used to create a xml Report for a specific amp report
     * @param reportParameter
     * @param reportId
     * @return report JAXB XML Report
     */
    public static Report getXmlReport(ReportParameter reportParameter, Long reportId) {
        ReportFormParameters formParams = XmlReportUtil.convertXmlCustomReportToJsonObj(reportParameter);
        
        if (reportId == null) {
            ApiErrorResponse errorResponse = ReportsUtil.validateReportConfig(formParams, true);
            if (errorResponse != null) {
                throw new ApiRuntimeException(Response.Status.BAD_REQUEST, errorResponse);
            }
            
            // we need reportId only to store the report result in cache
            reportId = (long) formParams.getReportName().hashCode();
            formParams.setCustom(true);
        }
        
        GeneratedReport generatedReport = ReportsUtil.getGeneratedReport(reportId, formParams);
        GeneratedReportToXmlConverter xmlConverter = new GeneratedReportToXmlConverter(generatedReport);
        Report xmlReport = xmlConverter.convert();

        return xmlReport;
    }
}
