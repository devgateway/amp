package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.BooleanWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.formulas.NiFormula;

/**
 * A utility class to transform a GeneratedReport to GPI Report 6
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport5aOutputBuilder extends GPIReportOutputBuilder {
	
	public static final String TOTAL_ACTUAL_DISBURSEMENTS = "Total Actual Disbursements";
	public static final String CONCESSIONAL = "Concessional";
	public static final String DISBURSEMENTS_OTHER_PROVIDERS = "Disbursements through other providers";
	public static final String OVER_DISBURSED = "% over Disbursed";
	public static final String REMARK = "Remark"; 

	public GPIReport5aOutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(TOTAL_ACTUAL_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(CONCESSIONAL));
		addColumn(new GPIReportOutputColumn(DISBURSEMENTS_OTHER_PROVIDERS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.ACTUAL_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.PLANNED_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.DISBURSED_AS_SCHEDULED));
		addColumn(new GPIReportOutputColumn(REMARK));
	}

	public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS,
					MeasureConstants.DISBURSED_AS_SCHEDULED, MeasureConstants.OVER_DISBURSED
			)));
	
	public final static Set<String> SUMMARY_NUMBERS = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					MeasureConstants.DISBURSED_AS_SCHEDULED, MeasureConstants.OVER_DISBURSED
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

		GPIReportOutputColumn donorColumn = null;
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (ColumnConstants.DONOR_AGENCY.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY);
			} else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP);
			}
		}
		
		if (donorColumn != null) {
			headers.add(donorColumn);
		}

		headers.add(getColumns().get(TOTAL_ACTUAL_DISBURSEMENTS));
		headers.add(getColumns().get(CONCESSIONAL));
		headers.add(getColumns().get(MeasureConstants.ACTUAL_DISBURSEMENTS));
		headers.add(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS));
		headers.add(new GPIReportOutputColumn(DISBURSEMENTS_OTHER_PROVIDERS));
		headers.add(getColumns().get(MeasureConstants.DISBURSED_AS_SCHEDULED));
		headers.add(new GPIReportOutputColumn(OVER_DISBURSED));
		headers.add(new GPIReportOutputColumn(REMARK));
		
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
		List<ReportArea> mockList = new ArrayList<>();
		for (ReportArea reportArea : mockList) {
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
	protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {

		Map<GPIReportOutputColumn, String> columns = new HashMap<>();
		
        List<ReportArea> onBudgetAreas = generatedReport.reportContents.getChildren()
		.stream().flatMap(r -> r.getChildren().stream())
		.collect(Collectors.toList()).stream()
		.filter(budgetArea -> isOnBudget(budgetArea))
		.collect(Collectors.toList());
        
        // get the sum of actual disbursements for on-budget projects
        BigDecimal actDisbSum = onBudgetAreas.stream()
				.flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
				.filter(entry -> isTotalMeasureColumn(MeasureConstants.ACTUAL_DISBURSEMENTS, entry.getKey()))
				.map(entry -> entry.getValue())
				.filter(rc -> rc != null)
				.map(rc -> new BigDecimal(((AmountCell) rc).extractValue()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
        
     // get the sum of planned disbursements for on-budget projects
        BigDecimal planDisbSum = onBudgetAreas.stream()
        		.flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
        		.filter(entry -> isTotalMeasureColumn(MeasureConstants.PLANNED_DISBURSEMENTS, entry.getKey()))
        		.map(entry -> entry.getValue())
        		.filter(rc -> rc != null)
        		.map(rc -> new BigDecimal(((AmountCell) rc).extractValue()))
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
		
        
        columns.put(new GPIReportOutputColumn(MeasureConstants.DISBURSED_AS_SCHEDULED), 
        		formatAmount(generatedReport, calculateDisbursedAsScheduled(actDisbSum, planDisbSum)) + "%");
        columns.put(new GPIReportOutputColumn(OVER_DISBURSED), 
        		formatAmount(generatedReport, calculateOverDisbursed(actDisbSum, planDisbSum)) + "%");
		
		return columns;
	}

	private boolean isOnBudget(ReportArea budgetArea) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private boolean isTotalMeasureColumn(String columnName, ReportOutputColumn roc) {
		return columnName.equals(roc.originalColumnName) 
				&& NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName);
	}
	
	/**
	 * Calculate % disbursed as scheduled (Actual Disbursements/Planned Disbursements * 100)
	 * 
	 * @param actual
	 * @param planned
	 * @return
	 */
	public BigDecimal calculateDisbursedAsScheduled(BigDecimal actual, BigDecimal planned) {
		BigDecimal result = BigDecimal.ZERO;
		if (actual == null || planned == null || 
				(!actual.equals(BigDecimal.ZERO) && planned.equals(BigDecimal.ZERO))) {
			result = BigDecimal.ZERO;
		}
		
		if (actual.compareTo(planned) >= 0) {
			result = BigDecimal.ONE;
		} else {
			result = actual.divide(planned, NiFormula.DIVISION_MC);
		}
		
		return result.multiply(new BigDecimal(100)).setScale(0, RoundingMode.UP);
	}
	
	/** 
	 * Calculate % over disbursed ((Actual Disbursements - Planned Disbursements)/Actual Disbursements * 100)
	 * 
	 * @param actual
	 * @param planned
	 * @return
	 */
	public BigDecimal calculateOverDisbursed(BigDecimal actual, BigDecimal planned) {
		if (actual == null || planned == null || actual.equals(BigDecimal.ZERO) || planned.equals(BigDecimal.ZERO) 
				|| actual.compareTo(planned) < 0) {
			return BigDecimal.ZERO;
		}

		return actual.subtract(planned).divide(actual, NiFormula.DIVISION_MC)
				.multiply(new BigDecimal(100))
				.setScale(0, RoundingMode.UP);
	}
	
}
