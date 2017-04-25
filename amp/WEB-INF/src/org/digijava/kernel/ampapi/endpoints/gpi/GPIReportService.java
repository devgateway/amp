package org.digijava.kernel.ampapi.endpoints.gpi;

import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReport9bOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputBuilder;
import org.dgfoundation.amp.gpi.reports.GPIReportUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * The service for building gpi reports
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReportService {

	private static GPIReportService service;

	private GPIReportService() {
	}

	public static GPIReportService getInstance() {
		if (service == null) {
			service = new GPIReportService();
		}

		return service;
	}

	public GPIReport getGPIReport(String indicatorCode, JsonBean formParams) {
//		if (StringUtils.isNotEmpty(indicatorCode)) {
//			formParams.set(EPConstants.IS_CUSTOM, true);
//			JsonBean errorValidJson = ReportsUtil.validateReportConfig(formParams, true);
//			if (errorValidJson != null) {
//				throw new ApiRuntimeException(Response.Status.BAD_REQUEST, errorValidJson);
//			}
//		}
		//TODO refactor the way how each gpi report spec is created
		
		int page = (Integer) EndpointUtils.getSingleValue(formParams, "page", 0);
		int recordsPerPage = EndpointUtils.getSingleValue(formParams, "recordsPerPage", 
				ReportPaginationUtils.getRecordsNumberPerPage());
		
		GeneratedReport generatedReport = GPIReportUtils.getGeneratedReportForIndicator(indicatorCode, formParams);
		GPIReportBuilder gpiReportBuilder = new GPIReportBuilder(generatedReport, getGPIReportOutputBuilder(indicatorCode));
		GPIReport gpiReport = gpiReportBuilder.build(page, recordsPerPage);

		return gpiReport;
	}

	private GPIReportOutputBuilder getGPIReportOutputBuilder(String indicatorCode) {
		if (indicatorCode.equals(GPIReportConstants.REPORT_9b)) {
			return new GPIReport9bOutputBuilder();
		}
		
		return null;
	}

}
