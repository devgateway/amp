package org.dgfoundation.amp.reports.saiku.export;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.springframework.web.util.HtmlUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/** Renders the report to HTML used for PDF export. See {@link SaikuReportPdfExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportHtmlRenderer {
    
    private GeneratedReport report;
    
    public SaikuReportHtmlRenderer(GeneratedReport report) {
        this.report = report;
    }

    public StringBuilder renderFullHtml() {
        StringBuilder reportHtml = new StringBuilder();

        reportHtml.append("<!DOCTYPE html><html><head><title></title></head><body>");
        renderReportBody(reportHtml);
        reportHtml.append("</body></html>");

        return reportHtml;
    }

    public StringBuilder renderTable() {
        StringBuilder reportHtml = new StringBuilder();

        renderReportTable(reportHtml);

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
        
        if (!this.report.spec.isShowOriginalCurrency()) {
            res.append("<div><b>")
            .append(TranslatorWorker.translateText("Currency"))
            .append(": </b>")
            .append(currency)
            .append("</div>");
        }       
        
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
        
        report.generatedHeaders.forEach(headerCells -> {
            tableHeader.append("<tr>");
            headerCells.stream().filter(headerCell -> !isHiddenColumn(headerCell.originalName)).forEach(headerCell -> {
                tableHeader.append(String.format("<th class='col' rowSpan='%d' colSpan='%d'>%s</th>",
                        headerCell.getRowSpan(), headerCell.getColSpan(), HtmlUtils.htmlEscape(headerCell.getName())));
            });
            
            // AMP-22554 - ugly hack in order to make iText library render the generated html table correctly 
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
        if (report.reportContents.getChildren() == null) {
            renderTableRow(tblData, report.reportContents, 0, new ArrayList<>());
        } else {
            for (ReportArea subReportArea : report.reportContents.getChildren()) {
                renderTableRow(tblData, subReportArea, 0, new ArrayList<>());
            }
        }
        if (report.hasMeasures()) {
            renderTableTotals(tblData, report.reportContents);
        }
    }


    /**Renders the data row
     * @param tblData
     * @param reportContents
     * @param level - the hierarchy level
     */
    private void renderTableRow(StringBuilder tblData, ReportArea reportContents, int level, List<String> cells) {
        if (reportContents.getChildren() != null) {
            renderGroupRow(tblData, reportContents, level + 1, cells);
        } else {
            tblData.append("<tr>");

            cells.forEach(tblData::append);
            cells.clear();

            columns().skip(level).forEach(roc -> {
                tblData.append("<td class='").append(cellClassFor(roc)).append("'>");
                tblData.append(getCellValue(reportContents, roc));
                tblData.append("</td>");
            });

            tblData.append("</tr>");
        }
    }

    private String cellClassFor(ReportOutputColumn roc) {
        String cellClass = roc.getFormatType();
        if (!report.spec.getColumnNames().contains(roc.originalColumnName)) {
            cellClass = "measure";
        }
        return cellClass;
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
    private void renderGroupRow(StringBuilder tableData, ReportArea reportContents, int level, List<String> cells) {
        String cellText = HtmlUtils.htmlEscape(reportContents.getOwner().debugString);
        cells.add(String.format("<td rowspan='%d'>%s</td>", getRowsSpan(reportContents), cellText));

        for (ReportArea reportArea : reportContents.getChildren()) {
            renderTableRow(tableData, reportArea, level, cells);
        }
        
        if (report.hasMeasures()) {
            renderSubTotalRow(tableData, reportContents, level);
        }
    }
    
    /**Renders the subtotals hieararchy row
     * @param tableData
     * @param reportContents
     * @param level
     */
    private void renderSubTotalRow(StringBuilder tableData, ReportArea reportContents, int level) {
        tableData.append("<tr>");

        columns().skip(level).forEach(roc -> {
            tableData.append("<td class='total'>");
            tableData.append(getCellValue(reportContents, roc));
            tableData.append("</td>");
        });
        
        tableData.append("</tr>");
    }
    
    private int getRowsSpan(ReportArea reportContents) {
        if (reportContents.getChildren() != null) {
            int rowSpan = 0;
            if (report.hasMeasures()) {
                rowSpan++;
            }
            for (ReportArea reportArea : reportContents.getChildren()) {
                rowSpan += getRowsSpan(reportArea);
            }
            return rowSpan;
        } else {
            return 1;
        }
    }

    /**Renders the totals of the table (the last row)
     * @param tableData
     * @param reportContents
     */
    private void renderTableTotals(StringBuilder tableData, ReportArea reportContents) {
        tableData.append("<tr>");

        tableData.append("<td class='total measure'>");
        tableData.append("<b>").append(TranslatorWorker.translateText("Report Totals"));
        tableData.append("(").append(report.reportContents.getNrEntities()).append(")</b>");
        tableData.append("</td>");

        columns().skip(1).forEach(roc -> {
            tableData.append("<td class='total measure'>");
            tableData.append(getCellValue(reportContents, roc));
            tableData.append("</td>");
        });
        
        tableData.append("</tr>");
    }

    private Stream<ReportOutputColumn> columns() {
        return report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName));
    }

    private boolean isHiddenColumn(String columnName) {
        return columnName.equals("Draft") || columnName.equals("Approval Status");
    }
}
