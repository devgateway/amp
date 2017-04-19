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

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;

/**
 * A utility class to transform a GeneratedReport to GPI Report 9b
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport9bOutputBuilder extends GPIReportOutputBuilder {

	public GPIReport9bOutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR, GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_COMMITMENTS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS));
	}

	public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS)));

	@Override
	public GPIReportOutput build(GeneratedReport generatedReport) {
		GPIReportOutput output = new GPIReportOutput();

		List<GPIReportOutputColumn> headers = buildHeaders(generatedReport);
		List<Map<GPIReportOutputColumn, String>> contents = buildContents(generatedReport);

		output.setHeaders(headers);
		output.setContents(contents);

		return output;
	}

	/**
	 *  build the headers of the report
	 *  
	 * @param generatedReport
	 * @return
	 */
	private List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
		List<GPIReportOutputColumn> headers = new ArrayList<>();
		headers.add(getColumns().get(GPIReportConstants.COLUMN_YEAR));

		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (!getColumns().keySet().contains(roc.originalColumnName)) {
				headers.add(new GPIReportOutputColumn(roc));
			}
		}
		
		headers.add(getColumns().get(MeasureConstants.ACTUAL_COMMITMENTS));
		headers.add(getColumns().get(MeasureConstants.ACTUAL_DISBURSEMENTS));
		
		return headers;
	}

	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	private List<Map<GPIReportOutputColumn, String>> buildContents(GeneratedReport generatedReport) {
		List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();
		GPIReportOutputColumn yearColumn = getColumns().get(GPIReportConstants.COLUMN_YEAR);

		for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
			Map<GPIReportOutputColumn, String> columns = new HashMap<>();
			Map<String, Map<String, String>> years = new HashMap<>();
			for (ReportOutputColumn roc : generatedReport.leafHeaders) {
				ReportCell rc = reportArea.getContents().get(roc);
				rc = rc != null ? rc : TextCell.EMPTY;
				if (YEAR_LEVEL_HIERARCHIES.contains(roc.columnName) && !roc.parentColumn.columnName.equals("Totals")) {
					if (years.get(roc.parentColumn.columnName) == null) {
						years.put(roc.parentColumn.columnName, new HashMap<>());
					}
					years.get(roc.parentColumn.columnName).put(roc.columnName, rc.displayedValue);
				} else if (roc.parentColumn == null) {
					columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
				}
			}

			years.forEach((k, v) -> {
				Map<GPIReportOutputColumn, String> row = new HashMap<>();
				row.put(yearColumn, k);
				v.forEach((x, y) -> {
					row.put(getColumns().get(x), y);
				});
				row.putAll(columns);
				contents.add(row);
			});
		}

		Comparator<Map<GPIReportOutputColumn, String>> byYear = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> o1.get(yearColumn).compareTo(o2.get(yearColumn));
		contents.sort(byYear);

		return contents;
	}
}
