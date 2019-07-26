package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.algo.BooleanWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.common.util.DateTimeUtil;


/**
 * A utility class to transform a GeneratedReport to GPI Report 5a
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport5aOutputBuilder extends GPIReportOutputBuilder {

    public static final String ACTIVITY_BUDGET_ON = "On Budget";
    public static final String YES_VALUE = "yes";
    public static final String CONCESSIONAL_YES_VALUE = "1";

    public GPIReport5aOutputBuilder() {
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
        addColumn(new GPIReportOutputColumn(getColumnLabel(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS),
                GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS, null));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_CONCESSIONAL,
                GPIReportConstants.REPORT_5A_TOOLTIP.get(GPIReportConstants.COLUMN_CONCESSIONAL)));
        addColumn(new GPIReportOutputColumn(getColumnLabel(MeasureConstants.ACTUAL_DISBURSEMENTS), 
                MeasureConstants.ACTUAL_DISBURSEMENTS, null));
        addColumn(new GPIReportOutputColumn(getColumnLabel(MeasureConstants.PLANNED_DISBURSEMENTS), 
                MeasureConstants.PLANNED_DISBURSEMENTS, null));
        addColumn(new GPIReportOutputColumn(getColumnLabel(MeasureConstants.DISBURSED_AS_SCHEDULED), 
                MeasureConstants.DISBURSED_AS_SCHEDULED,
                GPIReportConstants.REPORT_5A_TOOLTIP.get(MeasureConstants.DISBURSED_AS_SCHEDULED)));
        addColumn(new GPIReportOutputColumn(getColumnLabel(MeasureConstants.OVER_DISBURSED),
                MeasureConstants.OVER_DISBURSED,
                GPIReportConstants.REPORT_5A_TOOLTIP.get(MeasureConstants.OVER_DISBURSED)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARK));
    }

    public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS,
                    MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED,
                    MeasureConstants.OVER_DISBURSED, GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS)));

    public final static Set<String> ON_BUDGET_MEASURES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS,
                    MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED,
                    MeasureConstants.OVER_DISBURSED)));

    public final static Map<String, ReportOutputColumn> headersMap = new HashMap<>();

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

        for (ReportOutputColumn roc : generatedReport.leafHeaders) {
            if (ColumnConstants.DONOR_AGENCY.equals(roc.originalColumnName)) {
                donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY, roc.description);
            } else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
                donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP, roc.description);
                isDonorAgency = false;
            }
            headersMap.putIfAbsent(roc.originalColumnName, roc);
        }

        if (donorColumn != null) {
            headers.add(donorColumn);
        }

        headers.add(getColumns().get(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS));
        headers.add(getColumns().get(GPIReportConstants.COLUMN_CONCESSIONAL));
        headers.add(getColumns().get(MeasureConstants.ACTUAL_DISBURSEMENTS));
        headers.add(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS));
        headers.add(getColumns().get(MeasureConstants.DISBURSED_AS_SCHEDULED));
        headers.add(getColumns().get(MeasureConstants.OVER_DISBURSED));
        headers.add(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARK));

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
        List<AmpGPINiDonorNotes> donorNotes =  GPIUtils.getNotesByCode(GPIReportConstants.REPORT_5a);
        List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();
        GPIReportOutputColumn yearColumn = getColumns().get(GPIReportConstants.COLUMN_YEAR);
        if (generatedReport.reportContents.getChildren() != null) {
            for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
                Map<GPIReportOutputColumn, String> columns = new HashMap<>();
                Map<String, Map<String, ReportCell>> years = new HashMap<>();

                for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                    ReportCell rc = reportArea.getContents().get(roc);
                    rc = rc != null ? rc : TextCell.EMPTY;
                    if ((roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS))
                            && !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
                        if (years.get(roc.parentColumn.columnName) == null) {
                            years.put(roc.parentColumn.columnName, getEmptyGPIRow(generatedReport.spec));
                        }
                        years.get(roc.parentColumn.columnName).put(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS,
                                rc);
                    } else if (roc.originalColumnName.equals(ColumnConstants.DONOR_AGENCY)
                            || roc.originalColumnName.equals(ColumnConstants.DONOR_GROUP)) {
                        columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
                    }
                }

                Set<Integer> concessional = new HashSet<>();
                for (ReportArea budgetArea : reportArea.getChildren()) {
                    ReportCell rc = budgetArea.getContents()
                            .get(headersMap.get(ColumnConstants.ON_OFF_TREASURY_BUDGET));
                    if (String.valueOf(rc.value).equals(ACTIVITY_BUDGET_ON)) {
                        concessional.add(1);
                    } else if (String.valueOf(rc.value).equals("Off Budget")) {
                        concessional.add(0);
                    }
                }

                columns.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_CONCESSIONAL), CONCESSIONAL_YES_VALUE);

                Optional<ReportArea> onBudgetAreaElement = reportArea.getChildren().stream()
                        .filter(budgetArea -> isOnBudget(budgetArea)).findAny();

                if (onBudgetAreaElement.isPresent()) {
                    for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                        ReportCell rc = onBudgetAreaElement.get().getContents().get(roc);
                        rc = rc != null ? rc : TextCell.EMPTY;
                        if ((ON_BUDGET_MEASURES.contains(roc.originalColumnName))
                                && !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
                            if (years.get(roc.parentColumn.originalColumnName) == null) {
                                years.put(roc.parentColumn.originalColumnName, new HashMap<>());
                            }

                            if (rc != TextCell.EMPTY) {
                                years.get(roc.parentColumn.originalColumnName).put(roc.originalColumnName, rc);
                            }
                        }
                    }

                    Optional<ReportArea> hasExecArea = onBudgetAreaElement.get().getChildren().stream()
                            .filter(execArea -> hasExecutingAgency(execArea)).findAny();

                    if (hasExecArea.isPresent()) {
                        for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                            ReportCell rc = hasExecArea.get().getContents().get(roc);
                            rc = rc != null ? rc : TextCell.EMPTY;
                            if ((MeasureConstants.ACTUAL_DISBURSEMENTS.equals(roc.originalColumnName))
                                    && !roc.parentColumn.originalColumnName
                                            .equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
                                if (years.get(roc.parentColumn.originalColumnName) == null) {
                                    years.put(roc.parentColumn.originalColumnName, new HashMap<>());
                                }                               
                            }
                        }
                    }
                }

                years.forEach((k, v) -> {
                    Map<GPIReportOutputColumn, String> row = new HashMap<>();
                    row.put(yearColumn, k);
                    final BooleanWrapper isRowEmpty = new BooleanWrapper(true);
                    v.forEach((x, y) -> {
                        if (MeasureConstants.DISBURSED_AS_SCHEDULED.equals(x)
                                || MeasureConstants.OVER_DISBURSED.equals(x)) {
                            BigDecimal percentage = ((AmountCell) y).extractValue();
                            row.put(getColumns().get(x), percentage.setScale(0, RoundingMode.HALF_UP) + "%");
                        } else {
                            row.put(getColumns().get(x), y.displayedValue);
                        }
                        if (YEAR_LEVEL_HIERARCHIES.contains(x)) {
                            if (y instanceof AmountCell
                                    && ((AmountCell) y).extractValue().compareTo(BigDecimal.ZERO) != 0) {
                                isRowEmpty.set(false);
                            }

                            if (y instanceof TextCell && StringUtils.isNotBlank(y.value.toString())) {
                                isRowEmpty.set(false);
                            }
                        }
                    });
                    row.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARK),
                            getRemarkEndpointURL(k, reportArea.getOwner().id));
                    row.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARKS_COUNT),
                            String.valueOf(getNumberOfRemarks(k, reportArea.getOwner().id, donorNotes)));
                    row.put(new GPIReportOutputColumn(ColumnConstants.DONOR_ID),
                            String.valueOf(reportArea.getOwner().id));

                    if (!isRowEmpty.value) {
                        row.putAll(columns);
                        contents.add(row);
                    }
                });
            }
        }

        contents.sort(getByYearDonorComparator(getYearColumn(), getDonorColumn()));

        return contents;
    }

    /**
     * Generate the remark endpoint url
     * 
     * @param year
     * @param id
     * @return
     */
    private String getRemarkEndpointURL(String year, long id) {
        String remarkEndpoint = GPIReportConstants.GPI_REMARK_ENDPOINT
                + "?indicatorCode=%s&donorId=%s&donorType=%s&from=%s&to=%s";

        String donorType = isDonorAgency ? GPIReportConstants.HIERARCHY_DONOR_AGENCY
                : GPIReportConstants.HIERARCHY_DONOR_GROUP;

        int y = parseYear(year);
        String min = Integer.toString(DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y, 1)));
        String max = Integer.toString(DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y + 1, 1)));

        return String.format(remarkEndpoint, GPIReportConstants.REPORT_5a, id, donorType, min, max);
    }
        
    private Integer getNumberOfRemarks(String year, long id, List<AmpGPINiDonorNotes> donorNotes) {
        String donorType = isDonorAgency ? GPIReportConstants.HIERARCHY_DONOR_AGENCY
                : GPIReportConstants.HIERARCHY_DONOR_GROUP;
        int y = parseYear(year);
        Integer from = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y, 1));
        Integer to = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y + 1, 1));
        List<Long> donorIds = new ArrayList<>();
        donorIds.add(id);
        List<AmpGPINiDonorNotes> filteredNotes = GPIUtils.filterNotes(donorNotes, donorIds, donorType, from.longValue(),
                to.longValue());
        return filteredNotes.size();
    }   

    private Map<String, ReportCell> getEmptyGPIRow(ReportSpecification spec) {

        DecimalFormat decimalFormatter = ReportsUtil.getDecimalFormatOrDefault(spec);
        AmountCell emptyCell = new AmountCell(BigDecimal.ZERO, decimalFormatter.format(BigDecimal.ZERO));
        Map<String, ReportCell> initMap = new HashMap<>();

        ON_BUDGET_MEASURES.forEach(m -> initMap.put(m, emptyCell));

        return initMap;
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

        List<ReportArea> onBudgetAreas = new ArrayList<>();

        if (generatedReport.reportContents.getChildren() != null) {
            onBudgetAreas = generatedReport.reportContents.getChildren().stream().filter(r -> r.getChildren() != null)
                    .flatMap(r -> r.getChildren().stream()).collect(Collectors.toList()).stream()
                    .filter(budgetArea -> isOnBudget(budgetArea)).collect(Collectors.toList());
        }

        // get the sum of actual disbursements for on-budget projects
        BigDecimal actDisbSum = onBudgetAreas.stream()
                .flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
                .filter(entry -> isTotalMeasureColumn(MeasureConstants.ACTUAL_DISBURSEMENTS, entry.getKey()))
                .map(entry -> entry.getValue()).filter(rc -> rc != null)
                .map(rc -> ((AmountCell) rc).extractValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

        // get the sum of planned disbursements for on-budget projects
        BigDecimal planDisbSum = onBudgetAreas.stream()
                .flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
                .filter(entry -> isTotalMeasureColumn(MeasureConstants.PLANNED_DISBURSEMENTS, entry.getKey()))
                .map(entry -> entry.getValue()).filter(rc -> rc != null)
                .map(rc -> ((AmountCell) rc).extractValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

        columns.put(new GPIReportOutputColumn(MeasureConstants.DISBURSED_AS_SCHEDULED),
                formatAmount(generatedReport, calculateDisbursedAsScheduled(actDisbSum, planDisbSum), false) + "%");
        columns.put(new GPIReportOutputColumn(MeasureConstants.OVER_DISBURSED),
                formatAmount(generatedReport, calculateOverDisbursed(actDisbSum, planDisbSum), false) + "%");

        return columns;
    }

    private boolean hasExecutingAgency(ReportArea execArea) {
        boolean match = execArea.getContents().entrySet().stream()
                .anyMatch(e -> e.getKey().originalColumnName.equals(ColumnConstants.HAS_EXECUTING_AGENCY)
                        && (String.valueOf(e.getValue().value)).equals(YES_VALUE));

        return match;
    }

    private boolean isTotalMeasureColumn(String columnName, ReportOutputColumn roc) {
        return columnName.equals(roc.originalColumnName)
                && NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName);
    }

    /**
     * Calculate % disbursed as scheduled (Actual Disbursements/Planned * Disbursements * 100)
     * 
     * @param actual
     * @param planned
     * @return
     */
    public BigDecimal calculateDisbursedAsScheduled(BigDecimal actual, BigDecimal planned) {
        BigDecimal result = BigDecimal.ZERO;
        if (actual == null || planned == null || (!actual.equals(BigDecimal.ZERO) && planned.equals(BigDecimal.ZERO))) {
            result = BigDecimal.ZERO;
        }

        if (actual.compareTo(planned) >= 0) {
            result = BigDecimal.ONE;
        } else {
            result = actual.divide(planned, NiFormula.DIVISION_MC);
        }

        return result.multiply(new BigDecimal(PERCENTAGE_MULTIPLIER)).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Calculate % over disbursed ((Actual Disbursements - Planned * Disbursements)/Actual Disbursements * 100)
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
                .multiply(new BigDecimal(PERCENTAGE_MULTIPLIER)).setScale(0, RoundingMode.HALF_UP);
    }
    
    public String getColumnLabel(String columnName) {
        return getColumnLabel(GPIReportConstants.INDICATOR_5A_COLUMN_LABELS, columnName);
    }
    
    private int parseYear(String year) {
        int y = Integer.parseInt(year.replaceAll("[^0-9-]", "").split("-")[0]);
        return y;        
    }

}
