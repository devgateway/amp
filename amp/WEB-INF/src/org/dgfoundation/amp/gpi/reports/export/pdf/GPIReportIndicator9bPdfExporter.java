package org.dgfoundation.amp.gpi.reports.export.pdf;

import java.awt.Color;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator9bPdfExporter extends GPIReportPdfExporter {
    
    public GPIReportIndicator9bPdfExporter() {
        relativeWidths = new float [] {8f, 32f, 15f, 15f, 15f, 15f};
        reportTitle = "Indicator 9";
    }
    
    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(report.getSummary().keySet().size());
        
        // set table width a percentage of the page width
        table.setWidthPercentage(DEFAULT_TABLE_WIDTH_PERCENTAGE);
        
        Font bfBold14 = new Font(Font.HELVETICA, FONT_SIZE_SUMMARY, Font.BOLD, new Color(0, 0, 0));
        Color bkgColor = Color.ORANGE;
        
        Map<String, GPIReportOutputColumn> columns = report.getSummary().keySet().stream()
                .collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));

        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn column = report.getPage().getHeaders().get(i);
            if (columns.containsValue(column)) {
                addSummaryPdfCell(report, table, bfBold14, bkgColor, column);
            }
        }
        
        GPIReportOutputColumn ind9BColumn = columns.get(GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS);
        addSummaryPdfCell(report, table, bfBold14, bkgColor, ind9BColumn);
        
        body.add(table);
        body.add(new Paragraph());
    }

    /**
     * @param report
     * @param table
     * @param bfBold14
     * @param bkgColor
     * @param ind9BColumn
     */
    public void addSummaryPdfCell(GPIReport report, PdfPTable table, Font bfBold14, Color bkgColor,
            GPIReportOutputColumn ind9BColumn) {
        
        String cellValue = String.format("%s\n%s", report.getSummary().get(ind9BColumn), ind9BColumn.columnName);
        insertCell(table, cellValue, Element.ALIGN_CENTER, 1, bfBold14, bkgColor);
    }
    
    @Override
    public int getCellAlignment(String columnName) {
        switch (columnName) {
            case MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES:
            case MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES:
            case MeasureConstants.NATIONAL_AUDITING_PROCEDURES:
            case MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES:
                return Element.ALIGN_RIGHT;
            case GPIReportConstants.COLUMN_YEAR:
                return Element.ALIGN_CENTER;
            default:
                return super.getCellAlignment(columnName);
        }
    }
}
