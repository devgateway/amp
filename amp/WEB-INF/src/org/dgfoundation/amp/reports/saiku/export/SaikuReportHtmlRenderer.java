package org.dgfoundation.amp.reports.saiku.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.springframework.web.util.HtmlUtils;

/** Renders the report to HTML used for PDF export. See {@link SaikuReportPdfExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportHtmlRenderer {
	
	private GeneratedReport report;
	
	public SaikuReportHtmlRenderer(GeneratedReport report) {
		this.report = report;
	}
	
	public StringBuilder renderReportAsHtml() {
		StringBuilder reportHtml = new StringBuilder();
		
		reportHtml.append("<!DOCTYPE html><html><head><title></title></head><body>");
		renderReportBody(reportHtml);
		reportHtml.append("</body></html>");
		
		return reportHtml;
	}
	
	/** Renders the body of the Report HTML
	 * @param res
	 */
	private void renderReportBody(StringBuilder res) {
		
		String currency = report.spec.getSettings().getCurrencyCode();
		String units = report.spec.getSettings().getUnitsOption().userMessage;
		
		if(currency == null){
			//we get the default currency
			currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		
		res.append("<div>AMP Export - ").append(dateFormat.format(date)).append("</div><div>&nbsp;</div>");
		res.append("<div><b>").append(TranslatorWorker.translateText(units)).append("</b></div>");
		res.append("<div><b>").append(TranslatorWorker.translateText("Currency")).append(": </b>").append(currency).append("</div>");
		
		renderReportTable(res);
	}
	
	/** Renders the main table of the Report HTML
	 * @param resBody
	 */
	private void renderReportTable(StringBuilder resBody) {
		if (!report.generatedHeaders.isEmpty()) {
			resBody.append("<table>");
			renderReportTableHeader(resBody);
			renderTableBody(resBody);
			resBody.append("</table>");
		}
	}
	
	/**Renders the table header of the Report HTML
	 * @param tableHeader
	 */
	private void renderReportTableHeader(StringBuilder tableHeader) {
		tableHeader.append("<thead>");
		
		report.generatedHeaders.stream().forEach(headerCells -> {
			tableHeader.append("<tr>");
			headerCells.stream().filter(headerCell -> !isHiddenColumn(headerCell.originalName)).forEach(headerCell -> {
				tableHeader.append("<th class='col' ");
				tableHeader.append("rowSpan='" + headerCell.getRowSpan() + "' colSpan='" + headerCell.getColSpan() + "'>");
				tableHeader.append(headerCell.getName());
				tableHeader.append("</th>");
			});
			
			// AMP-22554 - ugly hack in order to make iText library to render correctly the generated html table 
			tableHeader.append("<th></th>");
			
			tableHeader.append("</tr>\n");
		});
		
		tableHeader.append("</thead>");
	}
	
	private void renderTableBody(StringBuilder tblData) {
		tblData.append("<tbody>");
		renderTableData(tblData);
		tblData.append("</tbody>");
	}
	
	private void renderTableData(StringBuilder tblData) {
		renderTableRow(tblData, report.reportContents, 0);
		renderTableTotals(tblData, report.reportContents);
	}


	/**Renders the data row
	 * @param tblData
	 * @param reportContents
	 * @param level - the hierarchy level
	 */
	private void renderTableRow(StringBuilder tblData, ReportArea reportContents, int level) {
		if (reportContents.getChildren() != null) {
			renderGroupRow(tblData, reportContents, level);
		} else {
			// Totals are rendered in another renderTableTotals
			if (level == 0) {
				return;
			}
			
			int hierCnt = report.spec.getHierarchies().size();
			for (int i = hierCnt; i < report.leafHeaders.size(); i++) {
				ReportOutputColumn roc = report.leafHeaders.get(i);
				
				if (isHiddenColumn(roc.originalColumnName)) {
					continue;
				}
				
				String cellClass = "data";
				if (!report.spec.getColumnNames().contains(roc.originalColumnName)) {
					cellClass = "measure";
				}
				
				tblData.append("<td class='" + cellClass + "'>");
				tblData.append(getCellValue(reportContents, roc));
				tblData.append("</td>");
			}
			
			tblData.append("</tr>");
			tblData.append("<tr>");
		}
	}
	
	private String getCellValue(ReportArea reportContents, ReportOutputColumn roc) {
		String value = reportContents.getContents().containsKey(roc) ? reportContents.getContents().get(roc).displayedValue : roc.emptyCell.displayedValue;
		
		return HtmlUtils.htmlEscape(value);
	}

	/**Renders the hierarchy group row
	 * @param tableData
	 * @param reportContents
	 * @param level - the hierarchy level
	 */
	private void renderGroupRow(StringBuilder tableData, ReportArea reportContents, int level) {
		if (level == 0) {
			tableData.append("<tr>");
		}
		
		for (ReportArea reportArea : reportContents.getChildren()) {
			if (reportArea.getNrEntities() > 0) {
				tableData.append(String.format("<td rowspan='%d'>%s</td>", getRowsSpan(reportArea), HtmlUtils.htmlEscape(reportArea.getOwner().debugString)));
			} 
			
			renderTableRow(tableData, reportArea, level+1);
		}
		
		if (level > 0)
			renderSubTotalRow(tableData, reportContents, level);
	}
	
	/**Renders the subtotals hieararchy row
	 * @param tableData
	 * @param reportContents
	 * @param level
	 */
	private void renderSubTotalRow(StringBuilder tableData, ReportArea reportContents, int level) {
		IntWrapper intWrapper = new IntWrapper();
		report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {
			if (intWrapper.value >= level) {
				tableData.append("<td class='total'>");
				tableData.append(getCellValue(reportContents, roc));
				tableData.append("</td>");
			}
			intWrapper.inc();
		});
		
		tableData.append("</tr><tr>");
	}
	
	private int getRowsSpan(ReportArea reportContents) {
		int rowSpan = 1;
		if (reportContents.getChildren() != null) {
			for (ReportArea reportArea : reportContents.getChildren()) {
				rowSpan += getRowsSpan(reportArea);
			}
		}
		
		return rowSpan;
	}

	/**Renders the totals of the table (the last row)
	 * @param tableData
	 * @param reportContents
	 */
	private void renderTableTotals(StringBuilder tableData, ReportArea reportContents) {
		if (reportContents.getChildren() == null) {
			tableData.append("<tr>");
		}
		
		IntWrapper intWrapper = new IntWrapper();
		report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {
			tableData.append("<td class='measure'>");
			if (intWrapper.value == 0 && report.spec.getColumns().size() > 0) {
				tableData.append("<b>" + TranslatorWorker.translateText("Report Totals") + "");
				tableData.append("(" + report.reportContents.getNrEntities() + ")</b>");
				intWrapper.inc();
			} else {
				tableData.append(getCellValue(reportContents, roc));
			}
			tableData.append("</td>");
		});
		
		tableData.append("</tr>");
	}
	
	private boolean isHiddenColumn(String columnName) {
		return columnName.equals("Draft") || columnName.equals("Approval Status");
	}
}
