package org.dgfoundation.amp.reports.converters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.reports.saiku.export.SaikuExportFilterUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import clover.org.apache.commons.lang.StringEscapeUtils;

/** A utility class to convert a GenerateReport to XML Document
 * @author Viorel Chihai
 *
 */
public class GeneratedReportToXmlConverter {
	
	private static final String ELEMENT_REPORT_ROOT = "report";
	private static final String ELEMENT_REPORT_OUTPUT = "output";
	private static final String ELEMENT_REPORT_AREA = "reportArea";
	private static final String ELEMENT_REPORT_CONTENTS = "contents";
	private static final String ELEMENT_REPORT_AREA_CHILDREN = "children";
	private static final String ELEMENT_REPORT_CELL = "cell";
	private static final String ELEMENT_REPORT_CELL_COLUMN_NAME = "columnName";
	private static final String ELEMENT_REPORT_CELL_VALUE = "value";
	
	private static final String ELEMENT_REPORT_HEADERS = "headers";
	private static final String ELEMENT_REPORT_HEADER_COLUMN = "column";
	private static final String ELEMENT_REPORT_HEADER_PARENT_COLUMN = "parentColumn";
	private static final String ELEMENT_REPORT_HEADER_COLUMN_HIERARCHICAL_NAME = "hierarchicalName";
	private static final String ELEMENT_REPORT_HEADER_COLUMN_DESCRIPTION = "description";
	private static final String ELEMENT_REPORT_HEADER_COLUMN_NAME = "name";

	private static final String ELEMENT_REPORT_CONFIG = "config";
	private static final String ELEMENT_REPORT_CONFIG_SETTINGS = "settings";
	private static final String ELEMENT_REPORT_CONFIG_SORTING = "sorting";
	private static final String ELEMENT_REPORT_CONFIG_FILTERS = "filters";
	private static final String ELEMENT_REPORT_CONFIG_FILTERS_FILTER = "filter";
	private static final String ELEMENT_REPORT_FILTERS_FILTER_NAME = "name";
	private static final String ELEMENT_REPORT_FILTERS_FILTER_VALUE = "value";
	
	private static final String ATTRIBUTE_REPORT_CONFIG_SORT = "sort";
	
	private GeneratedReport report;
	private Document xmlDocument;
	
	
	public GeneratedReportToXmlConverter(GeneratedReport report) {
		super();
		this.report = report;
	}

	public Document convert() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		xmlDocument = docBuilder.newDocument();
		
		buildXmlReport();
		
		return xmlDocument;
	}
	
	private void buildXmlReport() {
		Element rootElement = xmlDocument.createElement(ELEMENT_REPORT_ROOT);
		xmlDocument.appendChild(rootElement);
		
		buildReportOutput(rootElement);
		buildReportConfig(rootElement);
	}

	/**
	 * @param parentElement
	 */
	private void buildReportOutput(Element parentElement) {
		Element outputElement = xmlDocument.createElement(ELEMENT_REPORT_OUTPUT);
		parentElement.appendChild(outputElement);
		
		buildReportHeaders(outputElement);
		buildReportContents(outputElement);
	}

	/**
	 * 
	 * @param outputElement
	 */
	private void buildReportContents(Element parentElement) {
		buildReportArea(parentElement, report.reportContents);
	}

	/**
	 * @param parentElement
	 */
	private void buildReportArea(Element parentElement, ReportArea reportArea) {
		Element reportAreaElement = xmlDocument.createElement(ELEMENT_REPORT_AREA);
		parentElement.appendChild(reportAreaElement);
		
		Element contentsElement = xmlDocument.createElement(ELEMENT_REPORT_CONTENTS);
		reportAreaElement.appendChild(contentsElement);
		
		reportArea.getContents().forEach((roc, rc) -> {
			Element cellElement = xmlDocument.createElement(ELEMENT_REPORT_CELL);
			contentsElement.appendChild(cellElement);
			
			createTextElement(cellElement, ELEMENT_REPORT_CELL_COLUMN_NAME, roc.getHierarchicalName());
			createTextElement(cellElement, ELEMENT_REPORT_CELL_VALUE, rc.displayedValue);
		});
		
		Element childrenElement = xmlDocument.createElement(ELEMENT_REPORT_AREA_CHILDREN);
		reportAreaElement.appendChild(childrenElement);
		
		if (reportArea.getChildren() != null) {
			reportArea.getChildren().forEach(ra -> buildReportArea(childrenElement, ra));
		}
	}

	/**
	 * @param parentElement
	 */
	private void buildReportHeaders(Element parentElement) {
		Element headersElement = xmlDocument.createElement(ELEMENT_REPORT_HEADERS);
		parentElement.appendChild(headersElement);
		
		report.leafHeaders.stream()
						.filter(roc -> !isHiddenColumn(roc.originalColumnName))
						.forEach(roc -> buildHeaderElement(roc, headersElement));
	}

	/**
	 * 
	 * @param reportOutputColumn
	 * @param parentElement
	 */
	private void buildHeaderElement(ReportOutputColumn reportOutputColumn, Element parentElement) {
		Element columnElement = xmlDocument.createElement(ELEMENT_REPORT_HEADER_COLUMN);
		parentElement.appendChild(columnElement);
		
		createTextElement(columnElement, ELEMENT_REPORT_HEADER_COLUMN_NAME, reportOutputColumn.columnName);
		createTextElement(columnElement, ELEMENT_REPORT_HEADER_COLUMN_DESCRIPTION, reportOutputColumn.description);
		createTextElement(columnElement, ELEMENT_REPORT_HEADER_COLUMN_HIERARCHICAL_NAME, reportOutputColumn.getHierarchicalName());
		
		Element headerParentColumn = xmlDocument.createElement(ELEMENT_REPORT_HEADER_PARENT_COLUMN);
		columnElement.appendChild(headerParentColumn);
		
		if (reportOutputColumn.parentColumn != null) {
			buildHeaderElement(reportOutputColumn.parentColumn, columnElement);
		}
	}

	/**
	 * @param parentElement
	 */
	private void buildReportConfig(Element parentElement) {
		Element configElement = xmlDocument.createElement(ELEMENT_REPORT_CONFIG);
		parentElement.appendChild(configElement);
		
		buildSortingElements(configElement);
		buildFiltersElements(configElement);
		buildSettingsElements(configElement);
	}
	
	/**
	 * @param configElement
	 */
	private void buildSortingElements(Element configElement) {
		Element sortingElement = xmlDocument.createElement(ELEMENT_REPORT_CONFIG_SORTING);
		configElement.appendChild(sortingElement);
		report.spec.getSorters().forEach(sorter -> {
			Element sortElement = xmlDocument.createElement(ATTRIBUTE_REPORT_CONFIG_SORT);
			String pathName = sorter.hierPath.get(0);
			
			if (!sorter.isHierarchySorter(report.spec.getHierarchyNames())) {
				pathName = sorter.buildPath("][", NiReportsEngine.FUNDING_COLUMN_NAME, NiReportsEngine.TOTALS_COLUMN_NAME);
			}
			
			sortElement.setAttribute("asc", Boolean.toString(sorter.ascending));
			sortElement.setTextContent(StringEscapeUtils.escapeXml("[" + pathName + "]"));
			sortingElement.appendChild(sortElement);
		});
	}

	/**
	 * @param configElement
	 */
	private void buildSettingsElements(Element configElement) {
		Element settingsElement = xmlDocument.createElement(ELEMENT_REPORT_CONFIG_SETTINGS);
		configElement.appendChild(settingsElement);
		
		ReportSettings settings = report.spec.getSettings();
		
		createTextElement(settingsElement, SettingsConstants.CURRENCY_ID, settings.getCurrencyCode());
		createTextElement(settingsElement, SettingsConstants.CALENDAR_TYPE_ID, Long.toString(settings.getCalendar().getIdentifier()));
		
		Element yearRangeElement = xmlDocument.createElement(SettingsConstants.YEAR_RANGE_ID);
		settingsElement.appendChild(yearRangeElement);
		createTextElement(yearRangeElement, SettingsConstants.YEAR_FROM, settings.getYearRangeFilter().min);
		createTextElement(yearRangeElement, SettingsConstants.YEAR_TO, settings.getYearRangeFilter().max);
		
		Element amountFormatElement = xmlDocument.createElement(SettingsConstants.AMOUNT_FORMAT_ID);
		settingsElement.appendChild(amountFormatElement);
		
		DecimalFormat amountFormat = settings.getCurrencyFormat();
		DecimalFormatSymbols ds = amountFormat.getDecimalFormatSymbols();
		createTextElement(amountFormatElement, SettingsConstants.AMOUNT_UNITS, String.valueOf(settings.getUnitsOption().divider));
		createTextElement(amountFormatElement, SettingsConstants.MAX_FRACT_DIGITS, String.valueOf(amountFormat.getMaximumFractionDigits()));
		createTextElement(amountFormatElement, SettingsConstants.DECIMAL_SYMBOL, String.valueOf(ds.getDecimalSeparator()));
		createTextElement(amountFormatElement, SettingsConstants.USE_GROUPING, String.valueOf(amountFormat.isGroupingUsed()));
		createTextElement(amountFormatElement, SettingsConstants.GROUP_SEPARATOR, String.valueOf(ds.getGroupingSeparator()));
		createTextElement(amountFormatElement, SettingsConstants.GROUP_SIZE, String.valueOf(amountFormat.getGroupingSize()));
	}

	/**
	 * @param configElement
	 */
	private void buildFiltersElements(Element configElement) {
		Element filtersElement = xmlDocument.createElement(ELEMENT_REPORT_CONFIG_FILTERS);
		configElement.appendChild(filtersElement);
		
		Map<String, List<String>> extractedFilters = SaikuExportFilterUtils.getFilterValuesForIds(report.spec.getFilters());
		
		//TODO create objects holding the information about the columnNames, ReportElements and list of values
		
		for (Map.Entry<String, List<String>> filter : extractedFilters.entrySet()) {
			Element filterElement = xmlDocument.createElement(ELEMENT_REPORT_CONFIG_FILTERS_FILTER);
			filtersElement.appendChild(filterElement);
			
			createTextElement(filterElement, ELEMENT_REPORT_FILTERS_FILTER_NAME, filter.getKey());
			//createTextElement(filterElement, "type", filter.getKey());
			
			Element filterValuesElement = xmlDocument.createElement("values");
			filterElement.appendChild(filterValuesElement);
			
			for (String filterValue : filter.getValue()) {
				createTextElement(filterValuesElement, ELEMENT_REPORT_FILTERS_FILTER_VALUE, filterValue);
			}
		}
	}

	/**
	 * @param parentElement
	 * @param textContent
	 */
	private void createTextElement(Element parentElement, String elementName, String textContent) {
		Element textElmenet = xmlDocument.createElement(elementName);
		//TODO update commons.lang to 3.3 in order to use StringEscapeUtils.escapeXml(textContent);
		textElmenet.setTextContent(textContent);
		parentElement.appendChild(textElmenet);
	}

	protected boolean isHiddenColumn(String columnName) {
		return columnName.equals("Draft") || columnName.equals("Approval Status");
	}
	
	protected boolean hasReportGeneratedDummyColumn(GeneratedReport report) {
		 return report.spec.isSummaryReport() && (report.spec.getHierarchies() == null || report.spec.getHierarchies().isEmpty());
	}
}
