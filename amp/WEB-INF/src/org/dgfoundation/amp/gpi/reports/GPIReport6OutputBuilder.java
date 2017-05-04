package org.dgfoundation.amp.gpi.reports;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.hibernate.jdbc.Work;

/**
 * A utility class to transform a GeneratedReport to GPI Report 6
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport6OutputBuilder extends GPIReportOutputBuilder {

	private static final String ANNUAL_GOV_BUDGET = "Annual Government Budget";
	private static final String PLANNED_ON_BUDGET = "% of planned on budget";

	private boolean isDonorAgency = true;

	public GPIReport6OutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(MeasureConstants.PLANNED_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
		addColumn(new GPIReportOutputColumn(ANNUAL_GOV_BUDGET));
		addColumn(new GPIReportOutputColumn(PLANNED_ON_BUDGET));
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
				headers.add(new GPIReportOutputColumn(roc));
			} else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
				headers.add(new GPIReportOutputColumn(roc));
				isDonorAgency = false;
			}
		}

		headers.add(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS));
		headers.add(getColumns().get(ANNUAL_GOV_BUDGET));
		headers.add(getColumns().get(PLANNED_ON_BUDGET));

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
		GPIReportOutputColumn donorColumn = getColumns()
				.get(isDonorAgency ? ColumnConstants.DONOR_AGENCY : ColumnConstants.DONOR_GROUP);

		List<GPIAmount> gpiBudgetAmounts = new ArrayList<>();
		try {
			gpiBudgetAmounts = fetch(generatedReport);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Map<String, Map<String, GPIIndicator6Item>> reportValues = new HashMap<>();

		for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
			Map<GPIReportOutputColumn, String> columns = new HashMap<>();
			Map<String, GPIIndicator6Item> years = new HashMap<>();
			String donor = "";
			for (ReportOutputColumn roc : generatedReport.leafHeaders) {
				ReportCell rc = reportArea.getContents().get(roc);
				rc = rc != null ? rc : TextCell.EMPTY;
				if (MeasureConstants.PLANNED_DISBURSEMENTS.equals(roc.columnName)
						&& !roc.parentColumn.columnName.equals("Totals")) {
					if (years.get(roc.parentColumn.columnName) == null) {
						years.put(roc.parentColumn.columnName, new GPIIndicator6Item(
								new BigDecimal(((AmountCell) rc).extractValue()), BigDecimal.ZERO));
					}
				} else if (roc.parentColumn == null) {
					donor = rc.displayedValue;
				}
			}

			reportValues.putIfAbsent(donor, years);
		}

		Function<? super GPIAmount, ? extends String> donorNamePredicate = isDonorAgency ? GPIAmount::getDonorName
				: GPIAmount::getDonorGroup;
		
		Map<String, List<GPIAmount>> donorCells = gpiBudgetAmounts.stream().collect(groupingBy(donorNamePredicate));

		donorCells.entrySet().forEach(donor -> {
			String donorName = donor.getKey();
			List<GPIAmount> gpiCells = donor.getValue();
			reportValues.putIfAbsent(donorName, new HashMap<>());
			Map<String, GPIIndicator6Item> govYearMap = reportValues.get(donorName);
			if (gpiCells != null) {
				Map<String, BigDecimal> yearValues = gpiCells.stream().collect(groupingBy(GPIAmount::getYear,
						mapping(GPIAmount::getAmount, reducing(BigDecimal.ZERO, BigDecimal::add))));

				yearValues.entrySet().forEach(entry -> {
					if (govYearMap.containsKey(entry.getKey())) {
						govYearMap.get(entry.getKey()).setAnnualGov(entry.getValue());
					} else {
						govYearMap.put(entry.getKey(), new GPIIndicator6Item(BigDecimal.ZERO, entry.getValue()));
					}

				});
			}
			;
		});

		reportValues.entrySet().stream().forEach(donEntry -> {
			String donor = donEntry.getKey();
			donEntry.getValue().entrySet().stream().forEach(yearEntry -> {
				if (!yearEntry.getValue().isEmpty()) {
					GPIIndicator6Item value = yearEntry.getValue();
					Map<GPIReportOutputColumn, String> row = new HashMap<>();
					row.put(getColumns().get(GPIReportConstants.COLUMN_YEAR), yearEntry.getKey());
					row.put(donorColumn, donor);
					row.put(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS),
							formatAmount(generatedReport, value.getDisbAmount()));
					row.put(getColumns().get(ANNUAL_GOV_BUDGET), formatAmount(generatedReport, value.getAnnualGov()));
					row.put(getColumns().get(PLANNED_ON_BUDGET), formatAmount(generatedReport, value.getPercentage()));
					contents.add(row);
				}
			});
		});

		Comparator<Map<GPIReportOutputColumn, String>> byYearDonor = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> {
			if (o2.get(yearColumn).compareTo(o1.get(yearColumn)) == 0) {
				return o2.get(donorColumn).compareTo(o1.get(donorColumn));
			} else {
				return o2.get(yearColumn).compareTo(o1.get(yearColumn));
			}
		};

		contents.sort(byYearDonor);

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

		BigDecimal totalDisbursements = BigDecimal.ZERO;

		Map<GPIReportOutputColumn, String> columns = new HashMap<>();
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			ReportCell rc = generatedReport.reportContents.getContents().get(roc);
			rc = rc != null ? rc : TextCell.EMPTY;
			if (MeasureConstants.PLANNED_DISBURSEMENTS.equals(roc.originalColumnName)) {
				columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
				totalDisbursements = new BigDecimal(((AmountCell) rc).extractValue());
				break;
			}
		}
		try {

			BigDecimal sum = fetch(generatedReport).stream().map(c -> c.getAmount()).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			columns.put(new GPIReportOutputColumn(ANNUAL_GOV_BUDGET), formatAmount(generatedReport, sum));
			columns.put(new GPIReportOutputColumn(PLANNED_ON_BUDGET),
					formatAmount(generatedReport, calculateIndicator6Percentage(totalDisbursements, sum)));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		contents.add(columns);

		return contents;
	}

	public List<GPIAmount> fetch(GeneratedReport generatedReport) throws SQLException {
		NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

		AmpCurrency usedCurrency = CurrencyUtil.getAmpcurrency(generatedReport.spec.getSettings().getCurrencyCode());
		CalendarConverter calendarConverter = generatedReport.spec.getSettings() != null
				&& generatedReport.spec.getSettings().getCalendar() != null
						? generatedReport.spec.getSettings().getCalendar() : AmpARFilter.getDefaultCalendar();
		CachingCalendarConverter calendar = new CachingCalendarConverter(calendarConverter,
				calendarConverter.getDefaultFiscalYearPrefix(), Function.identity());

		String query = "SELECT aob.amp_gpi_ni_aid_on_budget_id AS budget_id,"
				+ "aob.amount AS amount, aob.currency_id AS currency_id, aob.indicator_date AS indicator_date,"
				+ "o.name AS donor_name, grp.org_grp_name AS donor_group " + "FROM amp_gpi_ni_aid_on_budget aob "
				+ "JOIN amp_organisation o ON aob.donor_id = o.amp_org_id "
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

						String donorName = rs.rs.getString("donor_name");
						String donorGroup = rs.rs.getString("donor_group");

						BigDecimal usedExchangeRate = BigDecimal.valueOf(
								AmpCurrencyConvertor.getInstance().getExchangeRate(srcCurrency.getCurrencyCode(),
										usedCurrency.getCurrencyCode(), null, transactionDate));
						MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate),
								transactionAmount, srcCurrency, transactionDate, precisionSetting);
						GPIAmount cell = new GPIAmount(donorName, donorGroup, amount,
								calendar.translate(transactionMoment));
						gpiAmountCells.add(cell);
					}
				}
			}
		});

		return gpiAmountCells;
	}

	private String formatAmount(GeneratedReport generatedReport, BigDecimal value) {
		DecimalFormat decimalFormat = generatedReport.spec.getSettings().getCurrencyFormat();

		return decimalFormat.format(value);
	}

	public static BigDecimal calculateIndicator6Percentage(BigDecimal a, BigDecimal b) {
		if (a.compareTo(b) <= 0) {
			return a.divide(b, NiFormula.DIVISION_MC).multiply(new BigDecimal(100));
		}

		return a.subtract(b).divide(a, NiFormula.DIVISION_MC).multiply(new BigDecimal(100));
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
		return GPIReport6OutputBuilder.calculateIndicator6Percentage(disbAmount, annualGov);
	}

	public boolean isEmpty() {
		return disbAmount.equals(BigDecimal.ZERO) && annualGov.equals(BigDecimal.ZERO);
	}
}
