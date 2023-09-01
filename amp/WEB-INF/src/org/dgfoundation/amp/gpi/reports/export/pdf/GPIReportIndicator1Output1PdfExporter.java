package org.dgfoundation.amp.gpi.reports.export.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.gpi.reports.*;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIDataService;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator1Output1PdfExporter extends GPIReportPdfExporter {

    public static final int SUMMARY_TABLE_SIZE = 4;
    public static final int PRIMARY_SECTORS_SIZE = 3;
    public static final int PRIMARY_SECTORS_HEADER_SIZE = 3;
    public static final int DOCUMENTS_HEADER_SIZE = 3;
    
    private CalendarConverter calendarConverter;

    public GPIReportIndicator1Output1PdfExporter() {
        relativeWidths = new float[] { 2f, 5f, 5f, 3f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 3f,
                3f, 3f };
        reportTitle = "Indicator 1 Output 1";
    }
    
    public void generateReportTable(Document doc, PdfWriter writer, GPIReport report) throws DocumentException {
        ReportSettings reportSettings = report.getSpec().getSettings();
        calendarConverter = (reportSettings != null && reportSettings.getCalendar() != null) 
                ? reportSettings.getCalendar() : AmpARFilter.getDefaultCalendar();
        
        Paragraph body = new Paragraph();
        
        renderReportTitle(report, body);
        renderReportSettings(report, body);
        renderReportTableSummary(report, body);
        renderReportTable(report, body);
        doc.add(body);
        
        doc.newPage();
        body = new Paragraph();
        renderReportRemarks(report, body);
        renderReportStatistics(report, body);
        doc.add(body);
    }

    @Override
    public void renderReportTableSummary(GPIReport report, Paragraph body) {
        PdfPTable table = new PdfPTable(SUMMARY_TABLE_SIZE);

        // set table width a percentage of the page width
        table.setWidthPercentage(100f);

        Font bfBold14 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.ORANGE;

        Map<String, GPIReportOutputColumn> columns = report.getSummary().keySet().stream()
                .collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));

        insertSummaryCell(report, table, bfBold14, bkgColor, columns, GPIReportConstants.GPI_1_Q1);
        insertSummaryCell(report, table, bfBold14, bkgColor, columns, GPIReportConstants.GPI_1_Q2);
        insertSummaryCell(report, table, bfBold14, bkgColor, columns, GPIReportConstants.GPI_1_Q3);
        insertSummaryCell(report, table, bfBold14, bkgColor, columns, GPIReportConstants.GPI_1_Q4);

        body.add(table);
        body.add(new Paragraph(""));
    }

    /**
     * @param report
     * @param table
     * @param bfBold14
     * @param bkgColor
     * @param columns
     * @param columnName
     */
    private void insertSummaryCell(GPIReport report, PdfPTable table, Font bfBold14, BaseColor bkgColor,
            Map<String, GPIReportOutputColumn> columns, String columnName) {

        String summaryValue = report.getSummary().get(columns.get(columnName));
        String cellValue = String.format("%s\n%s", summaryValue == null ? "" : summaryValue,
                getColumnHeaderLabel(GPIReportConstants.INDICATOR_1_1_SUMMARY_LABELS, columnName));
        PdfPCell summaryCell = generatePdfCell(new Phrase(cellValue, bfBold14), Element.ALIGN_LEFT, Element.ALIGN_TOP,
                1, 1, bkgColor);
        insertCell(table, summaryCell, 30f);
    }

    @Override
    public void renderReportTableHeader(GPIReport report, PdfPTable table) {
        Font bfBold11 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.LIGHT_GRAY;

        insertCell(table, getHeaderColumnLabel(GPIReportConstants.COLUMN_YEAR), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.DONOR_AGENCY), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.PROJECT_TITLE), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q1), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q2), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q3), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q4), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q5), Element.ALIGN_CENTER, 
                PRIMARY_SECTORS_HEADER_SIZE, 1, bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q6), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q6_DESCRIPTION), Element.ALIGN_CENTER, 1, 1, 
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q7), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q8), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q9), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q10), Element.ALIGN_CENTER, 1, 1,
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(ColumnConstants.GPI_1_Q10_DESCRIPTION), Element.ALIGN_CENTER, 1, 1, 
                bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT),
                Element.ALIGN_CENTER, 1, 1, bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES),
                Element.ALIGN_CENTER, 1, 1, bfBold11, bkgColor);
        insertCell(table, getHeaderColumnLabel(GPIReportConstants.GPI_1_Q11), Element.ALIGN_CENTER, 
                DOCUMENTS_HEADER_SIZE, 1, bfBold11, bkgColor);

        table.setHeaderRows(1);
    }

    @Override
    protected void renderReportTableData(GPIReport report, PdfPTable table) {
        Font bf7 = new Font(Font.FontFamily.HELVETICA, 7);
        BaseColor bkgColor = BaseColor.WHITE;

        Map<String, GPIReportOutputColumn> columns = new HashMap<>();
        
        if (!report.getPage().getContents().isEmpty()) {
            columns = report.getPage().getContents().stream()
                    .findAny().get().keySet().stream()
                    .collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));
        }

        for (int i = 0; i < report.getPage().getContents().size(); i++) {
            Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.COLUMN_YEAR), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.DONOR_AGENCY), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.PROJECT_TITLE), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.GPI_1_Q1), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.GPI_1_Q2), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.GPI_1_Q3), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.GPI_1_Q4), rowData);
            insertPrimarySectorsCells(table, bf7, bkgColor, columns.get(GPIReportConstants.GPI_1_Q5), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q6), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q6_DESCRIPTION), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q7), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q8), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q9), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q10), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(ColumnConstants.GPI_1_Q10_DESCRIPTION), rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT),
                    rowData);
            insertDataCell(table, bf7, bkgColor, columns.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES),
                    rowData);
            insertSupportiveDocumentsCells(table, bkgColor, columns, rowData);
        }
    }

    private void insertDataCell(PdfPTable table, Font font, BaseColor bkgColor, GPIReportOutputColumn column,
            Map<GPIReportOutputColumn, String> rowData) {

        String value = rowData.get(column);

        if (column.originalColumnName.equals(ColumnConstants.GPI_1_Q6)
                || column.originalColumnName.equals(ColumnConstants.GPI_1_Q10)) {
            value = "Yes".equals(value) ? "1" : "0";
        }
        
        if (column.originalColumnName.equals(GPIReportConstants.GPI_1_Q2)) {
            value = GPIReportUtils.getApprovalDateForExports(rowData.get(column), calendarConverter);
        }
        
        insertCell(table, value, getCellAlignment(column.originalColumnName), 1, 1, font, bkgColor);
    }

    private void insertPrimarySectorsCells(PdfPTable table, Font font, BaseColor bkgColor, GPIReportOutputColumn column,
            Map<GPIReportOutputColumn, String> rowData) {

        String sectors = rowData.get(column);
        List<String> sectorList = new ArrayList<>();
        if (StringUtils.isNotBlank(sectors)) {
            sectorList = Arrays.asList(sectors.split("###"));
        }
        
        for (int i = 0; i < PRIMARY_SECTORS_SIZE; i++) {
            String value = i < sectorList.size() ? sectorList.get(i) : "";
            insertCell(table, value, getCellAlignment(column.originalColumnName), 1, 1, font, bkgColor);
        }
    }

    private void insertSupportiveDocumentsCells(PdfPTable table, BaseColor bkgColor,
            Map<String, GPIReportOutputColumn> columns, Map<GPIReportOutputColumn, String> rowData) {

        Font urlFont = new Font(Font.FontFamily.HELVETICA, 7);
        urlFont.setColor(BaseColor.BLUE);
        urlFont.setStyle(Font.UNDERLINE);

        String donorId = rowData.get(columns.get(ColumnConstants.DONOR_ID));
        String activityId = rowData.get(columns.get(ColumnConstants.ACTIVITY_ID));
        List<GPIDonorActivityDocument> gpiDocuments = GPIDataService.getGPIDocuments(donorId, activityId);

        List<GPIDocument> documents = gpiDocuments.stream().map(GPIDonorActivityDocument::getDocuments)
                .flatMap(docs -> docs.stream()).collect(Collectors.toList());

        Map<String, List<GPIDocument>> documentsMap = documents.stream()
                .collect(groupingBy(gpiDoc -> "Q" + gpiDoc.getQuestion()));

        insertDocumentCell(table, urlFont, bkgColor, documentsMap, GPIReportConstants.GPI_1_Q11a);
        insertDocumentCell(table, urlFont, bkgColor, documentsMap, GPIReportConstants.GPI_1_Q11b);
        insertDocumentCell(table, urlFont, bkgColor, documentsMap, GPIReportConstants.GPI_1_Q11c);
    }

    private void insertDocumentCell(PdfPTable table, Font font, BaseColor bkgColor,
            Map<String, List<GPIDocument>> documentsMap, String columnName) {

        Phrase cellPhrase = new Phrase("", font);
        if (documentsMap.containsKey(columnName)) {
            for (GPIDocument document : documentsMap.get(columnName)) {
                Chunk chunk = new Chunk(document.getTitle());
                chunk.setAnchor(document.getUrl());
                cellPhrase.add(chunk);
                cellPhrase.add(Chunk.NEWLINE);
            }
        }
        
        PdfPCell urlCell = generatePdfCell(cellPhrase, getCellAlignment(columnName), Element.ALIGN_MIDDLE, 1, 1,
                bkgColor);

        insertCell(table, urlCell, 0);
    }
    
    /*
     * Render Report Remarks
     */
    public void renderReportRemarks(GPIReport report, Paragraph body) {
        float fntSize = FONT_SIZE_TITLE;
        Font titleRemarkFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, fntSize);
        Paragraph remarkParagraph = new Paragraph("Donor Remarks", titleRemarkFont);
        body.add(remarkParagraph);
        
        float[] remarksRelativeWidths = new float[] {10f, 30f, 60f};
        PdfPTable table = new PdfPTable(remarksRelativeWidths);

        // set table width a percentage of the page width
        table.setWidthPercentage(100f);

        Font bfBold10 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 0, 0));
        BaseColor bkgColor = BaseColor.LIGHT_GRAY;
        
        insertCell(table, TranslatorWorker.translateText("Date"), Element.ALIGN_CENTER, 1, 1, bfBold10, bkgColor);
        insertCell(table, TranslatorWorker.translateText("Donor"), Element.ALIGN_CENTER, 1, 1, bfBold10, bkgColor);
        insertCell(table, TranslatorWorker.translateText("Remarks"), Element.ALIGN_CENTER, 1, 1, bfBold10, bkgColor);
        
        table.setHeaderRows(1);
        
        Font font = new Font(Font.FontFamily.HELVETICA, 9);
        bkgColor = BaseColor.WHITE;
        
        List<GPIRemark> remarks = GPIReportUtils.getRemarksForIndicator1(report);
        
        for (GPIRemark remark : remarks) {
            insertCell(table, remark.getDate(), Element.ALIGN_CENTER, 1, 1, font, bkgColor);
            insertCell(table, remark.getDonorAgency(), Element.ALIGN_LEFT, 1, 1, font, bkgColor);
            insertCell(table, remark.getRemark(), Element.ALIGN_LEFT, 1, 1, font, bkgColor);
        }

        body.add(table);
        body.add(new Paragraph(""));
    }

    @Override
    public int getCellAlignment(String columnName) {
        switch (columnName) {
        case GPIReportConstants.GPI_1_Q1:
            return Element.ALIGN_RIGHT;
        case GPIReportConstants.GPI_1_Q11a:
        case GPIReportConstants.GPI_1_Q11b:
        case GPIReportConstants.GPI_1_Q11c:
            return Element.ALIGN_LEFT;
        default:
            return Element.ALIGN_CENTER;
        }
    }
    
    protected String getHeaderColumnLabel(String columnName) {
        return getColumnHeaderLabel(GPIReportConstants.INDICATOR_1_1_COLUMN_LABELS, columnName);
    }
}
