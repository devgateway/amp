package org.digijava.kernel.ampapi.endpoints.reports;

import javax.ws.rs.core.Response;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.converters.GeneratedReportToXmlConverter;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportParameter;
import org.dgfoundation.amp.reports.xml.XmlReportUtil;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

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
        JsonBean formParams = XmlReportUtil.convertXmlCustomReportToJsonObj(reportParameter);
        
        if (reportId == null) {
            JsonBean errorValidJson = ReportsUtil.validateReportConfig(formParams, true);
            if (errorValidJson != null) {
                throw new ApiRuntimeException(Response.Status.BAD_REQUEST, errorValidJson);
            }
            
            // we need reportId only to store the report result in cache
            reportId = (long) formParams.getString(EPConstants.REPORT_NAME).hashCode();
            formParams.set(EPConstants.IS_CUSTOM, true);
        }
        
        GeneratedReport generatedReport = ReportsUtil.getGeneratedReport(reportId, formParams);
        GeneratedReportToXmlConverter xmlConverter = new GeneratedReportToXmlConverter(generatedReport);
        Report xmlReport = xmlConverter.convert();

        return xmlReport;
    }
}
