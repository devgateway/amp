package org.dgfoundation.amp.gpi.reports.export.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.export.GPIReportExporter;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportPdfExporter implements GPIReportExporter {
    
    protected static final Logger logger = Logger.getLogger(GPIReportExporter.class);
    
    static final float FONT_SIZE_TITLE = 20f;
    static final float FONT_SIZE_SETTINGS = 10f;
    static final float FONT_SIZE_SUMMARY = 13f;

    static final float MINIMUM_ROW_HEIGHT = 10f;
    static final float SUMMARY_ROW_HEIGHT = 30f;
    
    static final float DEFAULT_TABLE_WIDTH_PERCENTAGE = 100f;

    protected GPIReport report;
    
    protected String reportStatisticsTitle = "Generated from the Aid Management Platform on";
    
    protected String reportTitle = "Indicator";
    
    protected float[] relativeWidths;

    @Override
    public byte[] exportReport(GPIReport report) throws Exception {
        this.report = report;
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document doc = createDocument();
        PdfWriter writer = PdfWriter.getInstance(doc, os);
        doc.open();
        
        try {
            generateReportTable(doc, writer, report);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            doc.add(new Paragraph("Error occured during creating the GPI Report in PDF."));
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

        return os.toByteArray();
    }

    private Document createDocument() {
        Document doc = new Document(calculateDocumentSize(calculateWidth()));

        return doc;
    }

    public void generateReportTable(Document doc, PdfWriter writer, GPIReport report) throws DocumentException {
        Paragraph body = new Paragraph();
        
        renderReportTitle(report, body);
        renderReportSettings(report, body);
        renderReportTableSummary(report, body);
        renderReportTable(report, body);
        renderReportStatistics(report, body);
        
        doc.add(body);
    }
    
    public void renderReportTitle(GPIReport report, Paragraph body) {
        float fntSize = FONT_SIZE_TITLE;
        
        Paragraph titleParagraph = new Paragraph(reportTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, fntSize));
        body.add(titleParagraph);
    }
    
    public void renderReportSettings(GPIReport report, Paragraph body) {
        Font bf10 = new Font(Font.HELVETICA, FONT_SIZE_SETTINGS);
        
        String units = report.getSpec().getSettings().getUnitsOption().userMessage;
        String currency = report.getSettings().getCurrencyCode();
        if (currency == null) {
            currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        }
        currency = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currency);
        
        body.add(new Paragraph(units, bf10));
        body.add(new Paragraph(String.format("%s: %s", "Currency", currency), bf10));
    }
    
    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(report.getSummary().keySet().size());
        
        // set table width a percentage of the page width
        table.setWidthPercentage(100f);
        
        Font bfBold14 = new Font(Font.HELVETICA, FONT_SIZE_SUMMARY, Font.BOLD, new Color(0, 0, 0));
        Color bkgColor = Color.ORANGE;

        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn column = report.getPage().getHeaders().get(i);
            if (report.getSummary().containsKey(column)) {
                String cellValue = String.format("%s\n%s", report.getSummary().get(column), column.columnName);
                insertCell(table, cellValue, Element.ALIGN_CENTER, 1, bfBold14, bkgColor);
            }
        }
        
        body.add(table);
        body.add(new Paragraph());
    }
    
    public void renderReportTable(GPIReport report, Paragraph body) {
        PdfPTable reportTable = new PdfPTable(report.getPage().getHeaders().size());
        
        if (relativeWidths != null) {
            reportTable = new PdfPTable(relativeWidths);
        }

        reportTable.setWidthPercentage(100f);
        
        renderReportTableHeader(report, reportTable);
        renderReportTableData(report, reportTable);

        body.add(reportTable);
    }

    public void renderReportTableHeader(GPIReport report, PdfPTable table) {
        Font bfBold11 = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0));
        Color bkgColor = Color.LIGHT_GRAY;
        
        List<GPIReportOutputColumn> columns = getDataTableColumns(report);
        columns.forEach(column -> insertCell(table, getHeaderColumnName(column), 
                Element.ALIGN_CENTER, 1, bfBold11, bkgColor));

        table.setHeaderRows(1);

    }

    protected void renderReportTableData(GPIReport report, PdfPTable table) {
        Font bf10 = new Font(Font.HELVETICA, 10);
        Color bkgColor = Color.WHITE;
        
        List<GPIReportOutputColumn> columns = getDataTableColumns(report);
        report.getPage().getContents().forEach(row -> {
            columns.forEach(col -> {
                insertCell(table, row.get(col), getCellAlignment(col.originalColumnName), 1, bf10, bkgColor);
            });
        });
    }
    
    public void renderReportStatistics(GPIReport report, Paragraph body) {
        float fntSize = 8f;
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        
        String statisticsInfo = String.format("%s %s", reportStatisticsTitle, dateFormat.format(date));
        Paragraph titleParagraph = new Paragraph(statisticsInfo, FontFactory.getFont(FontFactory.HELVETICA, fntSize));
        body.add(titleParagraph);
    }
    
    /**
     * 
     * @return float[] widths in relative percentages
     */
    public float[] getRelativeWidths() {
        return null;
    }
    
    protected String getHeaderColumnName(GPIReportOutputColumn column) {
        return column.columnName;
    }

    protected int calculateWidth() {
        if (relativeWidths != null) {
            return relativeWidths.length;
        }
        
        return report.getPage().getHeaders().size();
    }

    protected Rectangle calculateDocumentSize(int resultWidth) {
        Rectangle size = PageSize.A4;
        if (resultWidth >= 7) {
            size = PageSize.A4.rotate();
        }
        
        if (resultWidth >= 15) {
            size = PageSize.A3.rotate();
        }
        
        return size;
    }
    
    protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font, Color bkgColor) {
        insertCell(table, text, align, colspan, 1, font, bkgColor);
    }
    
    protected void insertCell(PdfPTable table, String text, int align, int colspan, int rowspan, Font font,
            Color bkgColor) {
        
        text = text == null ? "" : text.trim();
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = generatePdfCell(phrase, align, Element.ALIGN_MIDDLE, colspan, rowspan, bkgColor);
        
        insertCell(table, cell, MINIMUM_ROW_HEIGHT);
    }
    
    protected void insertCell(PdfPTable table, PdfPCell cell, float height) {
        
        if (height > 0) {
            cell.setMinimumHeight(height);
        }

        table.addCell(cell);
    }
    
    protected PdfPCell generatePdfCell(Phrase phrase, int align, int valign, int colspan, int rowspan, Color bkgColor) {

        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBackgroundColor(bkgColor);

        return cell;
    }
    
    public int getCellAlignment(String columnName) {
        return Element.ALIGN_LEFT;
    }
}
