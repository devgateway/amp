package org.dgfoundation.amp.gpi.reports.export.pdf;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator9bPdfExporter extends GPIReportPdfExporter {
    
    private static final int SUMMARY_TABLE_SIZE = 4;
    private static final int COUNTRY_SYSTEMS_SUMMARY_COLSPAN = 4;

    public GPIReportIndicator9bPdfExporter() {
        relativeWidths = new float [] {8f, 32f, 15f, 15f, 15f, 15f};
        reportTitle = "Indicator 9";
    }
    
    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(SUMMARY_TABLE_SIZE);
        
        // set table width a percentage of the page width
        table.setWidthPercentage(DEFAULT_TABLE_WIDTH_PERCENTAGE);
        
        Font bfBold14 = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, FONT_SIZE_SUMMARY, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.ORANGE;
        
        Map<String, GPIReportOutputColumn> columns = report.getSummary().keySet().stream()
                .collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));

        addSummaryPdfCell(report, table, bfBold14, bkgColor, COUNTRY_SYSTEMS_SUMMARY_COLSPAN, 
                columns.get(GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS));
        addSummaryPdfCell(report, table, bfBold14, bkgColor, 1, 
                columns.get(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
        addSummaryPdfCell(report, table, bfBold14, bkgColor, 1, 
                columns.get(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
        addSummaryPdfCell(report, table, bfBold14, bkgColor, 1, 
                columns.get(MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
        addSummaryPdfCell(report, table, bfBold14, bkgColor, 1, 
                columns.get(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));
        
        body.add(table);
        body.add(new Paragraph());
    }

    /**
     * 
     * @param report
     * @param table
     * @param bfBold14
     * @param bkgColor
     * @param colspan
     */
    public void addSummaryPdfCell(GPIReport report, PdfPTable table, Font bfBold14, BaseColor bkgColor, int colspan,
            GPIReportOutputColumn ind9BColumn) {
        
        String cellValue = String.format("%s\n%s", report.getSummary().get(ind9BColumn), ind9BColumn.columnName);
        insertCell(table, cellValue, Element.ALIGN_CENTER, colspan, bfBold14, bkgColor);
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
    
    public Predicate<GPIReportOutputColumn> getColumnTableFilter() {
        return column -> !column.originalColumnName.equals(GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS);
    }
}
