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

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * A utility class to transform a GeneratedReport to GPI Report 5b
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
			if (!getColumns().keySet().contains(roc.originalColumnName) 
					&& !roc.columnName.equals(MTEF_NAME) 
					&& !roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
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
					
					if (GPIReportUtils.getMTEFColumnsForIndicator5b(generatedReport.spec).contains(roc.columnName)) {
						if (((AmountCell) rc).extractValue() > 0) {
							columns.put(createGPIColumnFromMTEF(roc), MTEF_FUNDINGS_YES);
							sum++;
						} else {
							columns.put(createGPIColumnFromMTEF(roc), MTEF_FUNDINGS_NO);
						}
						
					} else if (roc.parentColumn == null) {
						columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
					}
				}
				
				BigDecimal percentageDisb =  new BigDecimal((sum / 3) * 100).setScale(0, RoundingMode.UP);
				columns.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), percentageDisb + "%");
				contents.add(columns);
				
			}
		}

		return contents;
	}

	/**
	 * Calculate the summary number (indicator 5b on country level)
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {
		Map<GPIReportOutputColumn, String> summaryColumns = new HashMap<>();
		
		if (generatedReport.reportContents.getChildren() != null) {
			double totalDisbursements = getTotalDisbursements(generatedReport);
			double indicator5bSum = 0;
			
			for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
				double mtefYearsWithYes = 0;
				double donorDisbursements = 0;
				for (ReportOutputColumn roc : generatedReport.leafHeaders) {
					ReportCell rc = reportArea.getContents().get(roc);
					rc = rc != null ? rc : TextCell.EMPTY;
					if (GPIReportUtils.getMTEFColumnsForIndicator5b(generatedReport.spec).contains(roc.originalColumnName)) {
						if (((AmountCell) rc).extractValue() > 0) {
							mtefYearsWithYes++;
						} 
					}
					
					if (roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)
							&& !roc.parentColumn.columnName.equals("Totals")) {
						ReportCell disbRc = reportArea.getContents().get(roc);
						donorDisbursements = ((AmountCell) disbRc).extractValue();
					}
				}
				
				indicator5bSum += donorDisbursements * mtefYearsWithYes;
			}
			
			double indicator5bResult = (indicator5bSum * 100) / (totalDisbursements * 3);
			BigDecimal percentageIndicator5b =  new BigDecimal(indicator5bResult).setScale(0, RoundingMode.UP);
			summaryColumns.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), percentageIndicator5b + "%");
		}
		
		return summaryColumns;
	}
	
	/**
	 * Get the total disbursements from the report trail cells
	 * 
	 * @param generatedReport
	 * @return
	 */
	private double getTotalDisbursements(GeneratedReport generatedReport) {
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)
					&& NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName)) {
				
				ReportCell rc = generatedReport.reportContents.getContents().get(roc);
				return ((AmountCell) rc).extractValue();
			}
		}
		
		throw new RuntimeException("The report doesnt't have the column " + MeasureConstants.ACTUAL_DISBURSEMENTS);
	}

	/**
	 * Replace the column name from 'MTEF xxxx' to 'xxxx'
	 * 
	 * @param roc
	 * @return
	 */
	private GPIReportOutputColumn createGPIColumnFromMTEF(ReportOutputColumn roc) {
		return new GPIReportOutputColumn(roc.columnName.replaceFirst(MTEF_NAME + " ", ""), 	roc.originalColumnName);
	}
}
