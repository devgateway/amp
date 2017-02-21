package org.dgfoundation.amp.reports.converters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.reports.saiku.export.SaikuExportFilterUtils;
import org.dgfoundation.amp.reports.xml.AmountFormat;
import org.dgfoundation.amp.reports.xml.Cell;
import org.dgfoundation.amp.reports.xml.Children;
import org.dgfoundation.amp.reports.xml.Column;
import org.dgfoundation.amp.reports.xml.Config;
import org.dgfoundation.amp.reports.xml.Contents;
import org.dgfoundation.amp.reports.xml.Filter;
import org.dgfoundation.amp.reports.xml.Filters;
import org.dgfoundation.amp.reports.xml.Headers;
import org.dgfoundation.amp.reports.xml.Output;
import org.dgfoundation.amp.reports.xml.ParentColumn;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportArea;
import org.dgfoundation.amp.reports.xml.Settings;
import org.dgfoundation.amp.reports.xml.Sort;
import org.dgfoundation.amp.reports.xml.Sorting;
import org.dgfoundation.amp.reports.xml.Values;
import org.dgfoundation.amp.reports.xml.YearRange;

import clover.org.apache.commons.lang.StringEscapeUtils;

/**
 * A utility class to convert a GenerateReport to XML Document
 * 
 * @author Viorel Chihai
 *
 */
public class GeneratedReportToXmlConverter {

	private GeneratedReport generatedReport;

	public GeneratedReportToXmlConverter(GeneratedReport report) {
		super();
		this.generatedReport = report;
	}

	public Report convert() {
		Report xmlReport = new Report();
		xmlReport.setConfig(getReportConfig());
		xmlReport.setOutput(getReportOutput());

		return xmlReport;
	}

	private Output getReportOutput() {
		Output output = new Output();
		output.setHeaders(getReportHeaders());
		output.setReportArea(getOutputReportArea());

		return output;
	}

	private ReportArea getOutputReportArea() {
		return getReportArea(generatedReport.reportContents);
	}

	/**
	 * @param reportArea
	 */
	private ReportArea getReportArea(org.dgfoundation.amp.newreports.ReportArea reportArea) {
		ReportArea xmlReportArea = new ReportArea();

		xmlReportArea.setContents(getContents(reportArea));
		xmlReportArea.setChildren(getChildren(reportArea));

		return xmlReportArea;
	}

	/**
	 * @param reportArea
	 */
	private Contents getContents(org.dgfoundation.amp.newreports.ReportArea reportArea) {
		Contents contents = new Contents();

		generatedReport.leafHeaders.stream()
			.filter(roc -> !isHiddenColumn(roc.originalColumnName))
			.forEach(roc -> {
				ReportCell rc = reportArea.getContents().get(roc) != null ? reportArea.getContents().get(roc) : roc.emptyCell;
				Cell cell = new Cell();
				cell.setColumnName(roc.getHierarchicalName());
				cell.setValue(rc.displayedValue);
				contents.getCell().add(cell);
			});

		return contents;
	}

	/**
	 * 
	 * @param reportArea
	 * @return children of reportArea
	 */
	private Children getChildren(org.dgfoundation.amp.newreports.ReportArea reportArea) {
		Children children = new Children();

		if (reportArea.getChildren() != null) {
			reportArea.getChildren().forEach(ra -> children.getReportArea().add(getReportArea(ra)));
		}

		return children;
	}

	private Headers getReportHeaders() {
		Headers headers = new Headers();

		generatedReport.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName))
				.forEach(roc -> headers.getColumn().add(getHeaderColumn(roc)));

		return headers;
	}

	/**
	 * 
	 * @param reportOutputColumn
	 * @return column 
	 */
	private Column getHeaderColumn(ReportOutputColumn reportOutputColumn) {
		Column headerColumn = new Column();

		headerColumn.setName(reportOutputColumn.columnName);
		headerColumn.setDescription(reportOutputColumn.description);
		headerColumn.setHierarchicalName(reportOutputColumn.getHierarchicalName());

		if (reportOutputColumn.parentColumn != null) {
			ParentColumn parentColumn = new ParentColumn();
			parentColumn.setColumn(getHeaderColumn(reportOutputColumn.parentColumn));
			headerColumn.setParentColumn(parentColumn);
		}

		return headerColumn;
	}

	private Config getReportConfig() {
		Config reportConfig = new Config();

		reportConfig.setSorting(getReportSorting());
		reportConfig.setFilters(getReportFilters());
		reportConfig.setSettings(getReportSettings());

		return reportConfig;
	}

	private Sorting getReportSorting() {
		Sorting sorting = new Sorting();

		generatedReport.spec.getSorters().forEach(sorter -> {
			Sort sort = new Sort();
			String pathName = sorter.hierPath.get(0);

			if (!sorter.isHierarchySorter(generatedReport.spec.getHierarchyNames())) {
				pathName = sorter.buildPath("][", NiReportsEngine.FUNDING_COLUMN_NAME,
						NiReportsEngine.TOTALS_COLUMN_NAME);
			}

			sort.setAsc(Boolean.toString(sorter.ascending));
			sort.setValue(StringEscapeUtils.escapeXml("[" + pathName + "]"));
			sorting.getSort().add(sort);
		});

		return sorting;
	}

	private Settings getReportSettings() {
		Settings xmlSettings = new Settings();
		ReportSettings settings = generatedReport.spec.getSettings();

		xmlSettings.setCurrencyCode(settings.getCurrencyCode());
		xmlSettings.setCalendarId(settings.getCalendar().getIdentifier().intValue());

		YearRange yearRange = new YearRange();
		if (settings.getYearRangeFilter().min != null)
			yearRange.setFrom(Integer.parseInt(settings.getYearRangeFilter().min));
		if (settings.getYearRangeFilter().max != null)
			yearRange.setTo(Integer.parseInt(settings.getYearRangeFilter().max));

		xmlSettings.setYearRange(yearRange);

		AmountFormat xmlAmountFormat = new AmountFormat();
		DecimalFormat amountFormat = settings.getCurrencyFormat();
		DecimalFormatSymbols ds = amountFormat.getDecimalFormatSymbols();
		xmlAmountFormat.setNumberDivider(settings.getUnitsOption().divider);
		xmlAmountFormat.setMaxFracDigits(amountFormat.getMaximumFractionDigits());
		xmlAmountFormat.setDecimalSymbol(String.valueOf(ds.getDecimalSeparator()));
		xmlAmountFormat.setGroupSeparator(String.valueOf(ds.getGroupingSeparator()));
		xmlAmountFormat.setUseGrouping(amountFormat.isGroupingUsed());
		xmlAmountFormat.setGroupSize(amountFormat.getGroupingSize());

		xmlSettings.setAmountFormat(xmlAmountFormat);

		return xmlSettings;
	}

	private Filters getReportFilters() {
		Filters filters = new Filters();

		Map<String, List<String>> extractedFilters = SaikuExportFilterUtils
				.getFilterValuesForIds(generatedReport.spec.getFilters());

		for (Map.Entry<String, List<String>> filter : extractedFilters.entrySet()) {
			Filter xmlFilter = new Filter();
			xmlFilter.setName(filter.getKey());
			
			Values values = new Values();
			values.getValue().addAll(filter.getValue());
			
			xmlFilter.setValues(values);
			filters.getFilter().add(xmlFilter);
		}
		
		return filters;
	}

	protected boolean isHiddenColumn(String columnName) {
		return columnName.equals("Draft") || columnName.equals("Approval Status");
	}

	protected boolean hasReportGeneratedDummyColumn(GeneratedReport report) {
		return report.spec.isSummaryReport()
				&& (report.spec.getHierarchies() == null || report.spec.getHierarchies().isEmpty());
	}
}
