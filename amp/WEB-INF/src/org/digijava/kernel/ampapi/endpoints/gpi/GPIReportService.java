package org.digijava.kernel.ampapi.endpoints.gpi;

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
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * The service for building GPI reports
 * 
 * @author Viorel Chihai
 *
 */
public final class GPIReportService {

	private static GPIReportService service;

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
	 * @param formParams  form parameters, that must be in the following format: 	<br/>
	 * { 																			<br/>
	 * 	"page"          : 1, 														<br/>
	 *  "recordsPerPage": 10,														<br/>
	 *  "filters"       : //see filters 						 					<br/>
	 *  "settings"      : //see {@link EndpointUtils#applySettings}               	<br/>
	 *  "hierarchy"	 	: "donor-agency"											<br/>
	 *  "summary"		: true														<br/>
	 * } 																			<br/>
	 * 
	 * where: <br>
	 * <dl>
	 *   <dt>page</dt>        	<dd>optional, page number, starting from 1. Use 0 to retrieve only
	 *                        		pagination information, without any records. Default to 0</dd>
	 *   <dt>recordsPerPage</dt> <dd>optional, the number of records per page to return. Default
	 *   					  		will be set to the number configured in AMP. Set it to -1
	 *                        		to get the unlimited records, that will provide all records.</dd>
	 *   <dt>settings</dt>	   	<dd>Report settings</dd>
	 *   <dt>filters</dt>	   	<dd>Report filters</dd>
	 *   <dt>hierarchy</dt>		<dd>The hierarchy used. Donor Agency or Donor Group</dd>
	 *   <dt>summary</dt>		<dd>Specify if its a summary report used for retrieving summary numbers</dd>
	 *   
	 * </dl>
	 * @return GPIReport result for the requested page and pagination information
	 */
	public GPIReport getGPIReport(String indicatorCode, JsonBean formParams) {
		int page = EndpointUtils.getSingleValue(formParams, "page", 0);
		int recordsPerPage = EndpointUtils.getSingleValue(formParams, "recordsPerPage",
				ReportPaginationUtils.getRecordsNumberPerPage());

		int output = EndpointUtils.getSingleValue(formParams, "output", 1);

		GeneratedReport niReport = GPIReportUtils.getGeneratedReportForIndicator(indicatorCode, formParams);
		GPIReportOutputBuilder gpiReportOutputBuilder = getGPIReportOutputBuilder(indicatorCode, output);
		gpiReportOutputBuilder.setDonorAgency(GPIReportUtils.isDonorAgency(formParams));
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

}
