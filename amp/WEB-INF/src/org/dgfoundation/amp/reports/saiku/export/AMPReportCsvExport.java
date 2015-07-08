package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AMPReportCsvExport {

	public static byte[] generateCSV(JsonBean jb, String type, ReportSpecificationImpl report,
			LinkedHashMap<String, Object> queryModel, String separator) throws IOException {

		// Generate html table.
		String content = AMPJSConverter.convertToHtml(jb, type);
		// Parse the string.
		Document doc = Jsoup.parse(content);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int columns = report.getColumns().size();

		StringBuilder csvContent = parseDocument(doc, columns, separator);

		os.write(csvContent.toString().getBytes("UTF-8"));
		os.flush();
		os.close();
		return os.toByteArray();
	}

	private static StringBuilder parseDocument(Document doc, int columns, String separator) {
		StringBuilder header = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		boolean emptyAsZero = FeaturesUtil
				.getGlobalSettingValueBoolean(GlobalSettingsConstants.REPORTS_EMPTY_VALUES_AS_ZERO_XLS);

		// Process header (we assume only 1 row, otherwise the data coming from JS is broken).
		Element headerRows = doc.getElementsByTag("thead").first();
		Element headerRowElement = headerRows.getElementsByTag("tr").first();
		int j = 0;
		for (Element headerColElement : headerRowElement.getElementsByTag("th")) {
			// This is a header cell with a single <div> inside.
			String cellContent = ((Element) headerColElement.getElementsByTag("div").first()).text();
			if (j > 0) {
				header.append(separator);
			}
			header.append("\"")
					.append(cellContent.replaceAll("\\]\\[", " ").replaceAll("\\[", "").replaceAll("\\]", ""))
					.append("\"");
			j++;
		}

		// Process content.
		StringBuilder content = new StringBuilder();
		Element contentRows = doc.getElementsByTag("tbody").first();
		for (Element contentRowElement : contentRows.getElementsByTag("tr")) {
			j = 0;
			Elements rows = new Elements();
			rows.addAll(contentRowElement.getElementsByTag("th"));
			rows.addAll(contentRowElement.getElementsByTag("td"));
			for (Element contentColElement : rows) {
				String cellContent = ((Element) contentColElement).text();
				if (j == 0) {
					content.append(lineSeparator);
				} else {
					content.append(separator);
				}
				if (j < columns) {
					// Use double quotes for all non measure columns.
					content.append("\"").append(cellContent.replaceAll("\"", "'")).append("\"");
				} else {
					if (!cellContent.equals("null")) {
						content.append(cellContent);
					} else {
						if (emptyAsZero) {
							content.append("0");
						} else {
							content.append("");
						}
					}
				}
				j++;
			}
		}

		return header.append(content);
	}
}
