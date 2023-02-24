package org.digijava.kernel.ampapi.endpoints.gpi;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReport1Output1Builder;
import org.dgfoundation.amp.gpi.reports.GPIReport1Output2Builder;
import org.dgfoundation.amp.gpi.reports.GPIReport5aOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReport5bOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReport6OutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReport9bOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportUtils;
import org.dgfoundation.amp.gpi.reports.export.GPIReportExportType;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;

/**
 * The service for building GPI reports
 * 
 * @author Viorel Chihai
 *
 */
public final class GPIReportService {

    private static GPIReportService service;
    
    protected static final Logger logger = Logger.getLogger(GPIReportService.class);

    private GPIReportService() {
    }

    public static GPIReportService getInstance() {
        if (service == null) {
            service = new GPIReportService();
        }

        return service;
    }

    /**
     * Retrieves the GPI page for the result for the specified indicatorCode and a given page number
     *  
     * @param indicatorCode indicatorCode
     * @param formParams  form parameters, that must be in the following format:    <br/>
     * {                                                                            <br/>
     *  "page"          : 1,                                                        <br/>
     *  "recordsPerPage": 10,                                                       <br/>
     *  "filters"       : //see filters                                             <br/>
     *  "settings"      : //see {@link SettingsUtils#applySettings}                 <br/>
     *  "hierarchy"     : "donor-agency"                                            <br/>
     * }                                                                            <br/>
     * 
     * where: <br>
     * <dl>
     *   <dt>page</dt>          <dd>optional, page number, starting from 1. Use 0 to retrieve only
     *                              pagination information, without any records. Default to 0</dd>
     *   <dt>recordsPerPage</dt> <dd>optional, the number of records per page to return. Default
     *                              will be set to the number configured in AMP. Set it to -1
     *                              to get the unlimited records, that will provide all records.</dd>
     *   <dt>settings</dt>      <dd>Report settings</dd>
     *   <dt>filters</dt>       <dd>Report filters</dd>
     *   <dt>hierarchy</dt>     <dd>The hierarchy used. Donor Agency or Donor Group (donor-agency|donor-group)</dd>
     *   
     * </dl>
     * @return GPIReport result for the requested page and pagination information
     */
    public GPIReport getGPIReport(String indicatorCode, GpiFormParameters formParams) {
        int page = EndpointUtils.getSingleValue(formParams.getPage(), 0);
        int recordsPerPage = EndpointUtils.getSingleValue(formParams.getRecordsPerPage(),
                ReportPaginationUtils.getRecordsNumberPerPage());

        int output = EndpointUtils.getSingleValue(formParams.getOutput(), 1);

        GeneratedReport niReport = GPIReportUtils.getGeneratedReportForIndicator(indicatorCode, formParams);
        GPIReportOutputBuilder gpiReportOutputBuilder = getGPIReportOutputBuilder(indicatorCode, output);
        gpiReportOutputBuilder.setOriginalFormParams(formParams);
        GPIReportBuilder gpiReportBuilder = new GPIReportBuilder(niReport, gpiReportOutputBuilder);
        GPIReport gpiReport = gpiReportBuilder.build(page, recordsPerPage);

        return gpiReport;
    }
    
    /**
     * Get the GPI report builder for specific indicator code
     * 
     * @param indicatorCode
     * @return gpiReportBuilder {@link @GPIReportOutputBuilder}
     */
    private GPIReportOutputBuilder getGPIReportOutputBuilder(String indicatorCode, int output) {
        switch (indicatorCode) {
            case GPIReportConstants.REPORT_1:
                if (output == 2) {
                    return new GPIReport1Output2Builder();
                }
                return new GPIReport1Output1Builder();
            case GPIReportConstants.REPORT_5a:
                return new GPIReport5aOutputBuilder();
            case GPIReportConstants.REPORT_5b:
                return new GPIReport5bOutputBuilder();
            case GPIReportConstants.REPORT_6:
                return new GPIReport6OutputBuilder();
            case GPIReportConstants.REPORT_9b:
                return new GPIReport9bOutputBuilder();
            default:
                return null;
        }
    }

    public Response exportGPIReport(String indicatorCode, GpiFormParameters formParams, String type) {
        formParams.setRecordsPerPage(Integer.MAX_VALUE);
        GPIReport gpiReport = getGPIReport(indicatorCode, formParams);
        
        return getExportAsResponse(indicatorCode, type, gpiReport, formParams);
    }

    public Response getExportAsResponse(String indicatorCode, String type, GPIReport report,
            GpiFormParameters formParams) {
        String fileName = String.format("Indicator_%s.%s", indicatorCode, type);
        try {
            byte[] doc = exportGPIReport(indicatorCode, report, formParams, type);
            
            if (doc != null) {
                logger.info("Send GPI export data to browser...");
                
                MediaType mediaType = EndpointUtils.getMediaType(type);

                return Response.ok(doc, mediaType)
                        .header("content-disposition", "attachment; filename = " + fileName)
                        .header("content-length", doc.length).build();
            } else {
                logger.error(type + " GPI report export is null");
                return Response.serverError().build();
            }
        } catch (Exception e) {
            logger.error("error while generating GPI report", e);
            return Response.serverError().build();
        }
    }
    
    /** Method used for exporting a NiReport. 
     * @param indicatorCode 
     * @param report
     * @param formParams
     * @param type
     * @return
     * @throws Exception
     */
    private byte[] exportGPIReport(String indicatorCode, GPIReport report, GpiFormParameters formParams,
            String type) throws Exception {
        
        GPIReportExportType exporter = null;
        int output = EndpointUtils.getSingleValue(formParams.getOutput(), 1);
        
        switch (type) {
            case AMPReportExportConstants.XLSX:
                switch (indicatorCode) {
                    case GPIReportConstants.REPORT_1 :
                        if (output == 2) {
                            exporter = GPIReportExportType.XLSX_1_2;
                        } else {
                            exporter = GPIReportExportType.XLSX_1_1;
                        }
                        break;
                    case GPIReportConstants.REPORT_5a :
                        exporter = GPIReportExportType.XLSX_5a;
                        break;
                    case GPIReportConstants.REPORT_5b :
                        exporter = GPIReportExportType.XLSX_5b;
                        break;
                    case GPIReportConstants.REPORT_6 :
                        exporter = GPIReportExportType.XLSX_6;
                        break;
                    case GPIReportConstants.REPORT_9b :
                        exporter = GPIReportExportType.XLSX_9b;
                        break;
                    default :
                        exporter = GPIReportExportType.XLSX;
                    } 
                break;
            case AMPReportExportConstants.PDF:
                switch (indicatorCode) {
                    case GPIReportConstants.REPORT_1 :
                        if (output == 2) {
                            exporter = GPIReportExportType.PDF_1_2;
                        } else {
                            exporter = GPIReportExportType.PDF_1_1;
                        }
                        break;
                    case GPIReportConstants.REPORT_5a :
                        exporter = GPIReportExportType.PDF_5a;
                        break;
                    case GPIReportConstants.REPORT_5b :
                        exporter = GPIReportExportType.PDF_5b;
                        break;
                    case GPIReportConstants.REPORT_6 :
                        exporter = GPIReportExportType.PDF_6;
                        break;
                    case GPIReportConstants.REPORT_9b :
                        exporter = GPIReportExportType.PDF_9b;
                        break;
                    default :
                    exporter = GPIReportExportType.PDF;
                } 
            break;
        }
        
        return exporter.executor.newInstance().exportReport(report);
    }

}
