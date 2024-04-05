package org.dgfoundation.amp.gpi.reports.export.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;


/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator5bPdfExporter extends GPIReportPdfExporter {
    
    public GPIReportIndicator5bPdfExporter() {
        relativeWidths = new float [] {50f, 10f, 10f, 10f, 20f};
        reportTitle = "Indicator 5b";
    }
    
    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(1);
        
        // set table width a percentage of the page width
        table.setWidthPercentage(45f);
        table.setHorizontalAlignment(0);
        
        Font bfBold14 = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.ORANGE;
        
        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn column = report.getPage().getHeaders().get(i);
            if (report.getSummary().containsKey(column)) {
                String cellValue = String.format("%s %s", INDICATOR_5B_SUMMARY_LABEL, report.getSummary().get(column));
                Phrase summaryPhrase = new Phrase(cellValue, bfBold14);
                PdfPCell summaryCell = generatePdfCell(summaryPhrase, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1, 1,
                        bkgColor);
                insertCell(table, summaryCell, SUMMARY_ROW_HEIGHT);
            }
        }

        body.add(table);
        body.add(new Paragraph());
    }
    
    @Override
    public int getCellAlignment(String columnName) {
        switch(columnName) {
            case ColumnConstants.DONOR_AGENCY:
            case ColumnConstants.DONOR_GROUP:
                return Element.ALIGN_LEFT;
            default:
                return Element.ALIGN_CENTER;
        }
    }
}
