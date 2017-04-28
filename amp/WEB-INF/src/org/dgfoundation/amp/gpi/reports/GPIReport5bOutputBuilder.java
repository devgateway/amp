package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.newreports.AmountCell;
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
public class GPIReport5bOutputBuilder extends GPIReportOutputBuilder {

	private static final String COLUMN_INDICATOR_5B = "Indicator 5b";

	private static final String MTEF_FUNDINGS_YES = "1";
	private static final String MTEF_FUNDINGS_NO = "0";
	
	private static final String MTEF_NAME = "MTEF";


	public GPIReport5bOutputBuilder() {
		addColumn(new GPIReportOutputColumn(COLUMN_INDICATOR_5B));
	}

	public final static Set<String> SUMMARY_NUMBERS = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					COLUMN_INDICATOR_5B
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

		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (!getColumns().keySet().contains(roc.originalColumnName) && !roc.columnName.equals(MTEF_NAME)) {
				headers.add(createGPIColumnFromMTEF(roc));
			}
		}

		headers.add(getColumns().get(COLUMN_INDICATOR_5B));
		
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
		
		if (generatedReport.reportContents.getChildren() != null) {
			for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
				Map<GPIReportOutputColumn, String> columns = new HashMap<>();
				
				double sum = 0;
				for (ReportOutputColumn roc : generatedReport.leafHeaders) {
					ReportCell rc = reportArea.getContents().get(roc);
					rc = rc != null ? rc : TextCell.EMPTY;
					if (GPIReportUtils.getMTEFColumnsForIndicator5b().contains(roc.columnName)) {
						if ((((AmountCell) rc).extractValue() > 0)) {
							columns.put(createGPIColumnFromMTEF(roc), MTEF_FUNDINGS_YES);
							sum++;
						} else {
							columns.put(createGPIColumnFromMTEF(roc), MTEF_FUNDINGS_NO);
						}
						
					} else if (roc.parentColumn == null) {
						columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
					}
				}
				
				BigDecimal percentage =  new BigDecimal((sum / 3) * 100).setScale(0, RoundingMode.UP);
				columns.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), percentage + "%");
				
				contents.add(columns);
				
			}
		}

		return contents;
	}

	/**
	 * @param roc
	 * @return
	 */
	private GPIReportOutputColumn createGPIColumnFromMTEF(ReportOutputColumn roc) {
		return new GPIReportOutputColumn(roc.columnName.replaceFirst(MTEF_NAME + " ", ""), 	roc.originalColumnName);
	}
	
	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<Map<GPIReportOutputColumn, String>> getReportContentsSummary(GeneratedReport generatedReport) {
		List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();

		Map<GPIReportOutputColumn, String> indicator = new HashMap<>();
		indicator.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), "20%");
		
		contents.add(indicator);
		
		return contents;
	}
}
