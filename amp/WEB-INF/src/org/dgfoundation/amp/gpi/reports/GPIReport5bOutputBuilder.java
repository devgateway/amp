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
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
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
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
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

		GPIReportOutputColumn donorColumn = null;
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (!getColumns().keySet().contains(roc.originalColumnName) 
					&& !roc.originalColumnName.equals(MTEF_NAME) 
					&& !roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
				headers.add(createGPIColumnFromMTEF(roc));
			} else if (ColumnConstants.DONOR_AGENCY.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY);
			} else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP);
			}
		}
		
		headers.add(donorColumn);
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
			for (ReportArea donorGroupArea : generatedReport.reportContents.getChildren()) {
				if (isDonorAgency) {
					for (ReportArea reportArea : donorGroupArea.getChildren()) {
						Map<GPIReportOutputColumn, String> columns = getGPIDataFromNiReport(generatedReport,
								reportArea, ColumnConstants.DONOR_AGENCY);
						contents.add(columns);
					}
				} else {
					Map<GPIReportOutputColumn, String> columns = getGPIDataFromNiReport(generatedReport,
							donorGroupArea, ColumnConstants.DONOR_GROUP);
					contents.add(columns);
				}
			}
		}

		return contents;
	}

	/**
	 * @param generatedReport
	 * @param reportArea
	 * @return
	 */
	public Map<GPIReportOutputColumn, String> getGPIDataFromNiReport(GeneratedReport generatedReport,
			ReportArea reportArea, String donorColumnName) {
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
				
			} else if (roc.parentColumn == null && roc.originalColumnName.equals(donorColumnName)) {
				columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
			}
		}
		
		BigDecimal percentageDisb =  new BigDecimal((sum / 3) * 100).setScale(0, RoundingMode.HALF_UP);
		columns.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), percentageDisb + "%");
		return columns;
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
		BigDecimal percentageIndicator5b = BigDecimal.ZERO;
		if (generatedReport.reportContents.getChildren() != null) {
			int numberOfDonors = generatedReport.reportContents
					.getChildren().stream()
					.filter(r -> r.getChildren() != null)
					.flatMap(r -> r.getChildren().stream())
					.collect(Collectors.toList()).size();
			
			double totalDisbursements = getTotalDisbursements(generatedReport);
			double indicator5bSum = 0;
			for (ReportArea donorGroupArea : generatedReport.reportContents.getChildren()) {
				for (ReportArea reportArea : donorGroupArea.getChildren()) {
					double mtefYearsWithYes = 0;
					double donorDisbursements = 0;
					for (ReportOutputColumn roc : generatedReport.leafHeaders) {
						ReportCell rc = reportArea.getContents().get(roc);
						rc = rc != null ? rc : TextCell.EMPTY;
						if (GPIReportUtils.getMTEFColumnsForIndicator5b(generatedReport.spec)
								.contains(roc.originalColumnName)) {
							if (((AmountCell) rc).extractValue() > 0) {
								mtefYearsWithYes++;
							} 
						}
						
						if (roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)
								&& !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
							ReportCell disbRc = reportArea.getContents().get(roc);
							donorDisbursements = ((AmountCell) disbRc).extractValue();
						}
					}
					
					indicator5bSum += donorDisbursements * mtefYearsWithYes / numberOfDonors;
				}
			}
			double indicator5bResult = (indicator5bSum * 100) / (totalDisbursements * 3);
			percentageIndicator5b =  new BigDecimal(indicator5bResult).setScale(0, RoundingMode.HALF_UP);
		}
		summaryColumns.put(new GPIReportOutputColumn(COLUMN_INDICATOR_5B), percentageIndicator5b + "%");
		
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
		return new GPIReportOutputColumn(roc.originalColumnName.replaceFirst(MTEF_NAME + " ", ""), 	
				roc.originalColumnName);
	}
}
