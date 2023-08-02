package org.dgfoundation.amp.gpi.reports.export.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.export.GPIReportMessages;

import java.util.Map;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator1Output2PdfExporter extends GPIReportPdfExporter {

    public GPIReportIndicator1Output2PdfExporter() {
        relativeWidths = new float[] { 15f, 70f, 15f };
        reportTitle = "Indicator 1 Output 2";
    }

    public void renderReportTableSummary(GPIReport report, Paragraph body) {
    }

    @Override
    public void renderReportTableHeader(GPIReport report, PdfPTable table) {
        Font bfBold11 = new Font(com.itextpdf.text.Font.FontFamily.valueOf(BaseFont.HELVETICA), 10, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.LIGHT_GRAY;

        insertCell(table, GPIReportConstants.COLUMN_YEAR, Element.ALIGN_CENTER, 1, bfBold11, bkgColor);
        insertCell(table, COLUMN_QUESTION, Element.ALIGN_CENTER, 1, bfBold11, bkgColor);
        insertCell(table, COLUMN_VALUE, Element.ALIGN_CENTER, 1, bfBold11, bkgColor);

        table.setHeaderRows(1);

    }

    @Override
    public void renderReportTableData(GPIReport report, PdfPTable table) {
        Font bf11 = new Font(Font.FontFamily.valueOf(BaseFont.HELVETICA), 10);
        BaseColor bkgColor = BaseColor.WHITE;

        GPIReportOutputColumn yearColumn = report.getPage().getHeaders().get(0);
        for (int i = 0; i < report.getPage().getContents().size(); i++) {
            Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
            insertCell(table, rowData.get(yearColumn), getCellAlignment(yearColumn.originalColumnName), 1, 4, bf11,
                    bkgColor);
            for (int j = 1; j < report.getPage().getHeaders().size(); j++) {
                GPIReportOutputColumn column = report.getPage().getHeaders().get(j);
                insertCell(table, GPIReportMessages.getString(column.originalColumnName),
                        getCellAlignment(COLUMN_QUESTION), 1, bf11, bkgColor);
                insertCell(table, rowData.get(column), getCellAlignment(COLUMN_VALUE), 1, bf11, bkgColor);

            }
        }
    }

    @Override
    public int getCellAlignment(String columnName) {
        if (columnName.equals(COLUMN_QUESTION)) {
            return Element.ALIGN_LEFT;
        }
        return Element.ALIGN_CENTER;
    }
}
