package org.dgfoundation.amp.gpi.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.algo.BooleanWrapper;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;

/**
 * A utility class to transform a GeneratedReport to GPI Report 6
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport5aOutputBuilder extends GPIReportOutputBuilder {

	public GPIReport5aOutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(MeasureConstants.ACTUAL_DISBURSEMENTS));
	}

	public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					MeasureConstants.ACTUAL_DISBURSEMENTS
			)));
	
	public final static Set<String> SUMMARY_NUMBERS = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					MeasureConstants.ACTUAL_DISBURSEMENTS
			)));

	/**
	 * build the headers of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
		List<GPIReportOutputColumn> headers = new ArrayList<>();
		headers.add(getColumns().get(GPIReportConstants.COLUMN_YEAR));

		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (!getColumns().keySet().contains(roc.originalColumnName)) {
				headers.add(new GPIReportOutputColumn(roc));
			}
		}

		headers.add(getColumns().get(MeasureConstants.ACTUAL_DISBURSEMENTS));
		
		return headers;
	}

	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<Map<GPIReportOutputColumn, String>> getReportContents(GeneratedReport generatedReport) {
		List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();
		GPIReportOutputColumn yearColumn = getColumns().get(GPIReportConstants.COLUMN_YEAR);

		for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
			Map<GPIReportOutputColumn, String> columns = new HashMap<>();
			Map<String, Map<String, ReportCell>> years = new HashMap<>();
			for (ReportOutputColumn roc : generatedReport.leafHeaders) {
				ReportCell rc = reportArea.getContents().get(roc);
				rc = rc != null ? rc : TextCell.EMPTY;
				if (YEAR_LEVEL_HIERARCHIES.contains(roc.columnName) && !roc.parentColumn.columnName.equals("Totals")) {
					if (years.get(roc.parentColumn.columnName) == null) {
						years.put(roc.parentColumn.columnName, new HashMap<>());
					}
					years.get(roc.parentColumn.columnName).put(roc.columnName, rc);
				} else if (roc.parentColumn == null) {
					columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
				}
			}

			years.forEach((k, v) -> {
				Map<GPIReportOutputColumn, String> row = new HashMap<>();
				row.put(yearColumn, k);
				final BooleanWrapper isRowEmpty = new BooleanWrapper(true);
				v.forEach((x, y) -> {
					row.put(getColumns().get(x), y.displayedValue);
					if (YEAR_LEVEL_HIERARCHIES.contains(x) && (((AmountCell) y).extractValue() != 0)) {
						isRowEmpty.set(false);
					}
				});
				
				if (!isRowEmpty.value) {
					row.putAll(columns);
					contents.add(row);
				}
			});
		}

		Comparator<Map<GPIReportOutputColumn, String>> byYear = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> o2.get(yearColumn).compareTo(o1.get(yearColumn));
		
		contents.sort(byYear);
		
		return contents;
	}
	
	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<Map<GPIReportOutputColumn, String>> getReportSummary(GeneratedReport generatedReport) {
		List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();

		Map<GPIReportOutputColumn, String> columns = new HashMap<>();
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			ReportCell rc = generatedReport.reportContents.getContents().get(roc);
			rc = rc != null ? rc : TextCell.EMPTY;
			if (SUMMARY_NUMBERS.contains(roc.originalColumnName)) {
				columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
			}
		}
		
		contents.add(columns);
		
		return contents;
	}
}
