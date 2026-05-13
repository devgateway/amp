package org.dgfoundation.amp.gpi.reports;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.jdbc.Work;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

/**
 * A utility class to transform a GeneratedReport to GPI Report 6
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport6OutputBuilder extends GPIReportOutputBuilder {

    private boolean isDonorAgency = true;

    public GPIReport6OutputBuilder() {
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
        addColumn(new GPIReportOutputColumn(MeasureConstants.PLANNED_DISBURSEMENTS));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET,
                GPIReportConstants.REPORT_6_TOOLTIP.get(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET,
                GPIReportConstants.REPORT_6_TOOLTIP.get(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET)));

    }

    public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS)));

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
            if (ColumnConstants.DONOR_AGENCY.equals(roc.originalColumnName)) {
                headers.add(getColumns().get(ColumnConstants.DONOR_AGENCY));
            } else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
                headers.add(getColumns().get(ColumnConstants.DONOR_GROUP));
                isDonorAgency = false;
            }
        }

        headers.add(getColumns().get(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET));
        headers.add(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS));
        headers.add(getColumns().get(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET));

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
        Map<String, Map<String, GPIIndicator6Item>> reportCells = new HashMap<>();

        if (generatedReport.reportContents.getChildren() != null) {
            for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
                Map<String, GPIIndicator6Item> years = new HashMap<>();
                String donor = "";
                for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                    ReportCell rc = reportArea.getContents().get(roc);
                    rc = rc != null ? rc : TextCell.EMPTY;
                    if (MeasureConstants.PLANNED_DISBURSEMENTS.equals(roc.columnName)
                            && !roc.parentColumn.columnName.equals("Totals")) {
                        if (years.get(roc.parentColumn.columnName) == null) {
                            years.put(roc.parentColumn.columnName, new GPIIndicator6Item(
                                    ((AmountCell) rc).extractValue(), BigDecimal.ZERO));
                        }
                    } else if (roc.parentColumn == null) {
                        donor = rc.displayedValue;
                    }
                }

                reportCells.putIfAbsent(donor, years);
            }
        }

        List<GPIAmount> gpiBudgetAmounts = fetchGpiBudgetAmounts(generatedReport);

        Function<? super GPIAmount, ? extends String> donorNamePredicate = isDonorAgency ? GPIAmount::getDonorName
                : GPIAmount::getDonorGroup;

        Map<String, List<GPIAmount>> donorAmounts = gpiBudgetAmounts.stream()
                .filter(c -> isAcceptableDonor(generatedReport, c.getDonorId()))
                .filter(c -> isAcceptableDate(generatedReport, c.getTransactionMoment()))
                .collect(groupingBy(donorNamePredicate));

        donorAmounts.entrySet().forEach(donor -> {
            String donorName = donor.getKey();
            List<GPIAmount> gpiCells = donor.getValue();
            reportCells.putIfAbsent(donorName, new HashMap<>());
            Map<String, GPIIndicator6Item> govYearMap = reportCells.get(donorName);
            if (gpiCells != null) {
                Map<String, BigDecimal> yearValues = gpiCells.stream().collect(groupingBy(GPIAmount::getTranslatedYear,
                        mapping(GPIAmount::getAmount, reducing(BigDecimal.ZERO, BigDecimal::add))));

                yearValues.entrySet().forEach(entry -> {
                    if (govYearMap.containsKey(entry.getKey())) {
                        govYearMap.get(entry.getKey()).setAnnualGov(entry.getValue());
                    } else {
                        govYearMap.put(entry.getKey(), new GPIIndicator6Item(BigDecimal.ZERO, entry.getValue()));
                    }

                });
            }
        });

        List<Map<GPIReportOutputColumn, String>> rows = buildReportRows(generatedReport, getDonorColumn(), reportCells);
        contents.addAll(rows);
        contents.sort(getByYearDonorComparator(getYearColumn(), getDonorColumn()));

        return contents;
    }

    /**
     * @param generatedReport
     * @return
     */
    private List<GPIAmount> fetchGpiBudgetAmounts(GeneratedReport generatedReport) {
        List<GPIAmount> gpiBudgetAmounts = new ArrayList<>();
        try {
            gpiBudgetAmounts = fetchGPIGovData(generatedReport);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gpiBudgetAmounts;
    }

    /**
     * @param generatedReport
     * @param donorColumn
     * @param reportValues
     * @return
     */
    private List<Map<GPIReportOutputColumn, String>> buildReportRows(GeneratedReport generatedReport,
            GPIReportOutputColumn donorColumn, Map<String, Map<String, GPIIndicator6Item>> reportValues) {
        List<Map<GPIReportOutputColumn, String>> rows = new ArrayList<>();
        reportValues.entrySet().stream().forEach(donEntry -> {
            String donor = donEntry.getKey();
            donEntry.getValue().entrySet().stream().forEach(yearEntry -> {
                if (!yearEntry.getValue().isEmpty()) {
                    GPIIndicator6Item value = yearEntry.getValue();
                    Map<GPIReportOutputColumn, String> row = new HashMap<>();
                    row.put(getColumns().get(GPIReportConstants.COLUMN_YEAR), yearEntry.getKey());
                    row.put(donorColumn, donor);
                    row.put(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS),
                            formatAmount(generatedReport, value.getDisbAmount(), true));
                    row.put(getColumns().get(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET),
                            formatAmount(generatedReport, value.getAnnualGov(), true));
                    row.put(getColumns().get(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET),
                            formatAmount(generatedReport, value.getPercentage(), false) + "%");
                    rows.add(row);
                }
            });
        });
        return rows;
    }

    /**
     * build the totals of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {

        BigDecimal totalDisbursements = BigDecimal.ZERO;

        Map<GPIReportOutputColumn, String> summaryColumns = new HashMap<>();
        for (ReportOutputColumn roc : generatedReport.leafHeaders) {
            ReportCell rc = generatedReport.reportContents.getContents().get(roc);
            rc = rc != null ? rc : TextCell.EMPTY;
            if (MeasureConstants.PLANNED_DISBURSEMENTS.equals(roc.originalColumnName)
                    && NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName)) {
                summaryColumns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
                totalDisbursements = ((AmountCell) rc).extractValue();
                break;
            }
        }

        try {
            BigDecimal sum = fetchGPIGovData(generatedReport).stream()
                    .filter(c -> isAcceptableDate(generatedReport, c.getTransactionMoment()))
                    .filter(c -> isAcceptableDonor(generatedReport, c.getDonorId())).map(c -> c.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            summaryColumns.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET),
                    formatAmount(generatedReport, sum, true));
            summaryColumns.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET),
                    formatAmount(generatedReport, calculateIndicator6Percentage(sum, totalDisbursements), false) + "%");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return summaryColumns;
    }

    public List<GPIAmount> fetchGPIGovData(GeneratedReport generatedReport) throws SQLException {
        NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

        AmpCurrency usedCurrency = CurrencyUtil.getAmpcurrency(generatedReport.spec.getSettings().getCurrencyCode());
        CalendarConverter calendarConverter = generatedReport.spec.getSettings() != null
                && generatedReport.spec.getSettings().getCalendar() != null
                        ? generatedReport.spec.getSettings().getCalendar() : AmpARFilter.getDefaultCalendar();

        CachingCalendarConverter calendar = new CachingCalendarConverter(calendarConverter,
                calendarConverter.getDefaultFiscalYearPrefix(), Function.identity());

        String query = "SELECT aob.amp_gpi_ni_aid_on_budget_id AS budget_id,"
                + "aob.amount AS amount, aob.currency_id AS currency_id, aob.indicator_date AS indicator_date,"
                + "o.amp_org_id AS donor_id, o.name AS donor_name, grp.org_grp_name AS donor_group "
                + "FROM amp_gpi_ni_aid_on_budget aob " + "JOIN amp_organisation o ON aob.donor_id = o.amp_org_id "
                + "JOIN amp_org_group grp ON o.org_grp_id = grp.amp_org_grp_id";

        List<GPIAmount> gpiAmountCells = new ArrayList<>();
        PersistenceManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try (RsInfo rs = SQLUtils.rawRunQuery(connection, query, null)) {
                    while (rs.rs.next()) {
                        java.sql.Date transactionMoment = rs.rs.getDate("indicator_date");
                        LocalDate transactionDate = transactionMoment.toLocalDate();
                        BigDecimal transactionAmount = rs.rs.getBigDecimal("amount");

                        Long currencyId = rs.rs.getLong("currency_id");
                        AmpCurrency srcCurrency = CurrencyUtil.getAmpcurrency(currencyId);

                        Long donorId = rs.rs.getLong("donor_id");
                        String donorName = rs.rs.getString("donor_name");
                        String donorGroup = rs.rs.getString("donor_group");

                        BigDecimal usedExchangeRate = BigDecimal.valueOf(
                                AmpCurrencyConvertor.getInstance().getExchangeRate(srcCurrency.getCurrencyCode(),
                                        usedCurrency.getCurrencyCode(), null, transactionDate));
                        MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate),
                                transactionAmount, srcCurrency, transactionDate, precisionSetting);
                        GPIAmount cell = new GPIAmount(donorId, donorName, donorGroup, amount, transactionMoment,
                                calendar.translate(transactionMoment));
                        gpiAmountCells.add(cell);
                    }
                }
            }
        });

        return gpiAmountCells;
    }

    public static BigDecimal calculateIndicator6Percentage(BigDecimal a, BigDecimal b) {
        if (a == null || b == null || a.equals(BigDecimal.ZERO) || b.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        if (a.compareTo(b) <= 0) {
            return a.divide(b, NiFormula.DIVISION_MC).multiply(new BigDecimal(PERCENTAGE_MULTIPLIER))
                    .setScale(0, RoundingMode.HALF_UP);
        }

        return a.subtract(b).divide(a, NiFormula.DIVISION_MC).multiply(new BigDecimal(PERCENTAGE_MULTIPLIER))
                .setScale(0, RoundingMode.HALF_UP);
    }

    protected boolean isAcceptableDate(GeneratedReport generatedReport, LocalDate date) {
        FilterRule dateRule = GPIReportUtils.getDateFilterRule(generatedReport.spec);
        Predicate<Long> dateRangePredicate = dateRule == null ? (z -> true) : dateRule.buildPredicate();

        return dateRangePredicate.test((long) DateTimeUtil.toJulianDayNumber(date));
    }

    protected boolean isAcceptableDonor(GeneratedReport generatedReport, Long donorId) {
        Optional<Entry<ReportElement, FilterRule>> donorEntry = generatedReport.spec.getFilters().getFilterRules()
                .entrySet().stream().filter(entry -> entry.getKey().type.equals(ElementType.ENTITY))
                .filter(entry -> entry.getKey().entity.getEntityName().equals(ColumnConstants.DONOR_AGENCY)).findAny();

        FilterRule donorRule = donorEntry.isPresent() ? donorEntry.get().getValue() : null;
        Predicate<Long> donorRulePredicate = donorRule == null ? (z -> true) : donorRule.buildPredicate();

        return donorRulePredicate.test(donorId);
    }
}

class GPIIndicator6Item {

    private BigDecimal disbAmount = BigDecimal.ZERO;
    private BigDecimal annualGov = BigDecimal.ZERO;

    public GPIIndicator6Item(BigDecimal plannedDisbursements, BigDecimal annualGov) {
        this.disbAmount = plannedDisbursements;
        this.annualGov = annualGov;
    }

    public void setDisbAmount(BigDecimal plannedDisbursements) {
        this.disbAmount = plannedDisbursements;
    }

    public BigDecimal getDisbAmount() {
        return disbAmount;
    }

    public void setAnnualGov(BigDecimal annualGov) {
        this.annualGov = annualGov;
    }

    public BigDecimal getAnnualGov() {
        return annualGov;
    }

    public BigDecimal getPercentage() {
        return GPIReport6OutputBuilder.calculateIndicator6Percentage(annualGov, disbAmount);
    }

    public boolean isEmpty() {
        return disbAmount.equals(BigDecimal.ZERO) && annualGov.equals(BigDecimal.ZERO);
    }
}
