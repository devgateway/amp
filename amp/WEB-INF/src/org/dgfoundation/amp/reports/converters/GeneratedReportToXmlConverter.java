package org.dgfoundation.amp.reports.converters;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import clover.org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Viorel Chihai
 *
 */
public class GeneratedReportToXmlConverter {
	
	private GeneratedReport report;
	
	public GeneratedReportToXmlConverter(GeneratedReport report) {
		super();
		this.report = report;
	}

	public Document convert() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document xmlDocument = docBuilder.newDocument();
		renderXmlReport(xmlDocument);
		
		return xmlDocument;
	}
	
	private void renderXmlReport(Document xmlDocument) {
		Element rootElement = xmlDocument.createElement("report");
		xmlDocument.appendChild(rootElement);
		
		buildReportOutput(xmlDocument, rootElement);
		buildReportConfig(xmlDocument, rootElement);
	}

	/**
	 * @param xmlDocument
	 * @param parentElement
	 */
	private void buildReportOutput(Document xmlDocument, Element parentElement) {
		Element outputElement = xmlDocument.createElement("output");
		parentElement.appendChild(outputElement);
		
		buildReportHeaders(xmlDocument, outputElement);
		buildReportContents(xmlDocument, outputElement);
	}

	/**
	 * 
	 * @param xmlDocument
	 * @param outputElement
	 */
	private void buildReportContents(Document xmlDocument, Element parentElement) {
		buildReportArea(xmlDocument, parentElement, report.reportContents);
	}

	/**
	 * @param xmlDocument
	 * @param parentElement
	 */
	private void buildReportArea(Document xmlDocument, Element parentElement, ReportArea reportArea) {
		Element reportAreaElement = xmlDocument.createElement("reportArea");
		parentElement.appendChild(reportAreaElement);
		
		Element contentsElement = xmlDocument.createElement("contents");
		reportAreaElement.appendChild(contentsElement);
		
		reportArea.getContents().forEach((roc, rc) -> {
			Element cellElement = xmlDocument.createElement("cell");
			contentsElement.appendChild(cellElement);
			
			createTextElement(xmlDocument, cellElement, "columnName", roc.getHierarchicalName());
			createTextElement(xmlDocument, cellElement, "value", rc.displayedValue);
		});
		
		Element childrenElement = xmlDocument.createElement("children");
		reportAreaElement.appendChild(childrenElement);
		
		if (reportArea.getChildren() != null) {
			reportArea.getChildren().forEach(ra -> buildReportArea(xmlDocument, childrenElement, ra));
		}
	}

	/**
	 * @param xmlDocument
	 * @param parentElement
	 */
	private void buildReportHeaders(Document xmlDocument, Element parentElement) {
		Element headersElement = xmlDocument.createElement("headers");
		parentElement.appendChild(headersElement);
		
		report.leafHeaders.stream()
						.filter(roc -> !isHiddenColumn(roc.originalColumnName))
						.forEach(roc -> buildHeaderElement(xmlDocument, roc, headersElement));
	}

	/**
	 * 
	 * @param xmlDocument 
	 * @param reportOutputColumn
	 * @param parentElement
	 */
	private void buildHeaderElement(Document xmlDocument, ReportOutputColumn reportOutputColumn, Element parentElement) {
		Element columnElement = xmlDocument.createElement("column");
		parentElement.appendChild(columnElement);
		
		createTextElement(xmlDocument, columnElement, "name", reportOutputColumn.columnName);
		createTextElement(xmlDocument, columnElement, "description", reportOutputColumn.description);
		createTextElement(xmlDocument, columnElement, "hierarchicalName", reportOutputColumn.getHierarchicalName());
		
		Element headerParentColumn = xmlDocument.createElement("parentColumn");
		columnElement.appendChild(headerParentColumn);
		
		if (reportOutputColumn.parentColumn != null) {
			buildHeaderElement(xmlDocument, reportOutputColumn.parentColumn, columnElement);
		}
	}

	/**
	 * @param xmlDocument
	 * @param parentElement
	 */
	private void buildReportConfig(Document xmlDocument, Element parentElement) {
		Element configElement = xmlDocument.createElement("config");
		parentElement.appendChild(configElement);
		
		Element sortingElement = xmlDocument.createElement("sorting");
		configElement.appendChild(sortingElement);
		
		Element filtersElement = xmlDocument.createElement("filters");
		configElement.appendChild(filtersElement);
		
		Element settingsElement = xmlDocument.createElement("settings");
		configElement.appendChild(settingsElement);
	}
	
	/**
	 * @param xmlDocument
	 * @param parentElement
	 * @param textContent
	 */
	private void createTextElement(Document xmlDocument, Element parentElement, String elementName, String textContent) {
		Element textElmenet = xmlDocument.createElement(elementName);
		textElmenet.setTextContent(StringEscapeUtils.escapeXml(textContent));
		parentElement.appendChild(textElmenet);
	}

	protected boolean isHiddenColumn(String columnName) {
		return columnName.equals("Draft") || columnName.equals("Approval Status");
	}
	
	protected boolean hasReportGeneratedDummyColumn(GeneratedReport report) {
		 return report.spec.isSummaryReport() && (report.spec.getHierarchies() == null || report.spec.getHierarchies().isEmpty());
	}
}
