package org.dgfoundation.amp.gpi.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * A utility class to transform a GeneratedReport to GPI Report Output (headers, report data, summary)
 * 
 * @author Viorel Chihai
 *
 */
public abstract class GPIReportOutputBuilder  {

	protected Map<String, GPIReportOutputColumn> columns = new HashMap<>();
	
	public GPIReportOutputBuilder() {
	};
	
	protected Map<String, GPIReportOutputColumn> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	protected void addColumn(GPIReportOutputColumn col) {
		columns.put(col.columnName, col);
	}
	
	protected abstract List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport);
	
	protected abstract List<Map<GPIReportOutputColumn, String>> getReportContents(GeneratedReport generatedReport);
	
	protected abstract Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport);

	/**
	 * Build the report page data based on generatedReport
	 * 
	 * @param generatedReport
	 * @param page
	 * @param recordsPerPage
	 * @return
	 */
	public GPIReportPage buildGPIReportPage(GeneratedReport generatedReport, int page, int recordsPerPage) {
		GPIReportPage output = new GPIReportPage();
		
		List<GPIReportOutputColumn> headers = buildHeaders(generatedReport);
		output.setHeaders(headers);
		
		List<Map<GPIReportOutputColumn, String>> allContents = new ArrayList<>();
		allContents = getReportContents(generatedReport);
		output.setTotalRecords(allContents.size());
		
		int start = page > 0 ? (page - 1) * recordsPerPage : page;
		int pages = page;
		
		if (recordsPerPage != -1) {
			pages = (allContents.size() + recordsPerPage - 1) / recordsPerPage;
			allContents = paginate(allContents, start, recordsPerPage);
			if (allContents.size() > 0 && page == 0) {
				page = 1;
			}
		}
		
		output.setContents(allContents);
		output.setCurrentPageNumber(page);
		output.setRecordsPerPage(recordsPerPage);
		output.setTotalPageCount(pages);

		return output;
	}
	
	/**
	 * Build the report summary data based on generatedReport
	 * 
	 * @param generatedReport
	 * @return summary info as a map of {@link GPIReportOutputColumn, @link String} values
	 */
	public Map<GPIReportOutputColumn, String> buildGPIReportSummary(GeneratedReport generatedReport) {
		return getReportSummary(generatedReport);
	}
	
	/**
	 * Method that returns a sublist (pagination)
	 * 
	 * @param collection
	 * @param index
	 * @param pageSize
	 * @return {@link List<T>}
	 */
	public static <T> List<T> paginate(List<T> collection, int index, int pageSize) {
		return collection.stream().skip(index).limit(pageSize).collect(Collectors.toList());
	}
}
