package org.dgfoundation.amp.gpi.reports.export.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.GPIReportUtils;


/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator5aPdfExporter extends GPIReportPdfExporter {
    
    static final float GPI_COLUMN_4 = 4f;
    static final float GPI_COLUMN_8 = 8f;
    static final float GPI_COLUMN_10 = 10f;
    static final float GPI_COLUMN_20 = 20f;
    static final float GPI_COLUMN_24 = 24f;

    public GPIReportIndicator5aPdfExporter() {
        relativeWidths = new float[] { GPI_COLUMN_4, GPI_COLUMN_20, GPI_COLUMN_10, GPI_COLUMN_10, GPI_COLUMN_8, 
                GPI_COLUMN_8, GPI_COLUMN_8, GPI_COLUMN_8, GPI_COLUMN_24 };
        reportTitle = "Indicator 5a";
    }

    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(report.getSummary().size());

        // set table width a percentage of the page width
        table.setWidthPercentage(50f);
        table.setHorizontalAlignment(0);

        Font bfBold14 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.ORANGE;

        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn column = report.getPage().getHeaders().get(i);
            if (report.getSummary().containsKey(column)) {
                String cellValue = String.format("%s\n%s", report.getSummary().get(column), column.columnName);

                Phrase summaryPhrase = new Phrase(cellValue, bfBold14);
                PdfPCell summaryCell = generatePdfCell(summaryPhrase, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1, 1,
                        bkgColor);
                insertCell(table, summaryCell, SUMMARY_ROW_HEIGHT);
            }
        }

        body.add(table);
        body.add(new Paragraph());
    }

    public void renderReportTableHeader(GPIReport report, PdfPTable table) {
        Font bfBold11 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.LIGHT_GRAY;

        report.getPage().getHeaders().forEach(
                column -> insertCell(table, getHeaderColumnName(column), Element.ALIGN_CENTER, 1, bfBold11, bkgColor));

        table.setHeaderRows(1);

    }

    @Override
    protected void renderReportTableData(GPIReport report, PdfPTable table) {
        Font bf11 = new Font(Font.FontFamily.HELVETICA, 9);
        BaseColor bkgColor = BaseColor.WHITE;

        report.getPage().getContents().forEach(row -> {
            report.getPage().getHeaders().forEach(col -> {
                if (!isRemarkColumn(col.originalColumnName)) {
                    insertCell(table, row.get(col), getCellAlignment(col.originalColumnName), 1, bf11, bkgColor);
                } else {
                    insertCell(table, GPIReportUtils.getRemarksForIndicator5a(row), getCellAlignment(col.originalColumnName),
                            1, bf11, bkgColor);
                }
            });
        });
    }

    @Override
    public int getCellAlignment(String columnName) {
        switch (columnName) {
        case ColumnConstants.DONOR_AGENCY:
        case ColumnConstants.DONOR_GROUP:
            return Element.ALIGN_LEFT;
        case GPIReportConstants.COLUMN_CONCESSIONAL:
        case GPIReportConstants.COLUMN_YEAR:
            return Element.ALIGN_CENTER;
        case GPIReportConstants.COLUMN_REMARK:
            return Element.ALIGN_LEFT;
        default:
            return Element.ALIGN_RIGHT;
        }
    }

    protected boolean isRemarkColumn(String columnName) {
        return columnName.equals(GPIReportConstants.COLUMN_REMARK);
    }

    @Override
    protected String getHeaderColumnName(GPIReportOutputColumn column) {
        return getColumnHeaderLabel(GPIReportConstants.INDICATOR_5A_COLUMN_LABELS, column.originalColumnName);
    }

}
