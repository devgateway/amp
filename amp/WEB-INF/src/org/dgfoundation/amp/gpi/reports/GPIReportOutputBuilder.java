package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * A utility class to transform a GeneratedReport to GPI Report Output (headers, report data, summary)
 * 
 * @author Viorel Chihai
 *
 */
public abstract class GPIReportOutputBuilder  {

	protected Map<String, GPIReportOutputColumn> columns = new HashMap<>();
	
	protected boolean isDonorAgency = true;
	
	protected JsonBean originalFormParams;
	
	public GPIReportOutputBuilder() {
	};
	
	protected Map<String, GPIReportOutputColumn> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	protected void addColumn(GPIReportOutputColumn col) {
		columns.put(col.originalColumnName, col);
	}
	
	public void setOriginalFormParams(JsonBean originalFormParams) {
		this.originalFormParams = originalFormParams;
		this.isDonorAgency = GPIReportUtils.isDonorAgency(originalFormParams);
	}
	
	public JsonBean getOriginalFormParams() {
		return originalFormParams;
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
	
	/**
	 * Format bigDecimal value using the generatedReprot spec settings
	 * 
	 * @param generatedReport
	 * @param value
	 * @return
	 */
	protected String formatAmount(GeneratedReport generatedReport, BigDecimal value, boolean toDivide) {
		ReportSettings settings = generatedReport.spec.getSettings();
		DecimalFormat decimalFormat = settings.getCurrencyFormat();
		AmountsUnits amountsUnits = (settings != null && settings.getUnitsOption() != null) ? 
				settings.getUnitsOption() : AmountsUnits.AMOUNTS_OPTION_UNITS;
		BigDecimal unitsDivider = BigDecimal.valueOf(amountsUnits.divider);
		value = toDivide ? value.divide(unitsDivider) : value;
		
		return decimalFormat.format(value);
	}
	
	/**
	 * @return donorColumn
	 */
	protected GPIReportOutputColumn getDonorColumn() {
		GPIReportOutputColumn donorColumn = getColumns()
				.get(isDonorAgency ? ColumnConstants.DONOR_AGENCY : ColumnConstants.DONOR_GROUP);

		return donorColumn;
	}

	/**
	 * @return yearColumn
	 */
	protected GPIReportOutputColumn getYearColumn() {
		return getColumns().get(GPIReportConstants.COLUMN_YEAR);
	}
	
	/**
	 * @param yearColumn
	 * @param donorColumn
	 * @return
	 */
	protected Comparator<Map<GPIReportOutputColumn, String>> getByYearDonorComparator(GPIReportOutputColumn yearColumn,
			GPIReportOutputColumn donorColumn) {
		Comparator<Map<GPIReportOutputColumn, String>> byYearDonorComparator = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> {
			if (o2.get(yearColumn).compareTo(o1.get(yearColumn)) == 0) {
				return o1.get(donorColumn).compareTo(o2.get(donorColumn));
			} else {
				return o2.get(yearColumn).compareTo(o1.get(yearColumn));
			}
		};
		
		return byYearDonorComparator;
	}
	
	public void setDonorAgency(boolean isDonorAgency) {
		this.isDonorAgency = isDonorAgency;
	}
}
