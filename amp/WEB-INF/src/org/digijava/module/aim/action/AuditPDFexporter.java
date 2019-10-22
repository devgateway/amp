
package org.digijava.module.aim.action;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.versioning.ActivityComparisonResult;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AuditPDFexporter {

    private static final Integer VALUE_NAME = 0;
    private static final Integer HEADER_COLSPAN = 3;
    private static final Integer HEADER_COLUMNS = 3;
    private static final Integer TABLE_WIDTH = 100;
    private static AuditPDFexporter auditPDFexporter;
    private static final Font PLAIN_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(0, 0, 0));
    private static final BaseColor HEADER_BRACKGROUND_COLOR = new BaseColor(244, 244, 242);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 0, 0));

    public static AuditPDFexporter getInstance() {
        if (auditPDFexporter == null) {
            auditPDFexporter = new AuditPDFexporter();
        }
        return auditPDFexporter;
    }

    private AuditPDFexporter() {

    }

    public ByteArrayOutputStream buildPDFexport(List<ActivityComparisonResult> activityComparisonResult) {
        return generatePDFExport(activityComparisonResult);
    }

    private ByteArrayOutputStream generatePDFExport(List<ActivityComparisonResult> activityComparisonResult) {
        ByteArrayOutputStream baos = null;
        try {
            Document document = new Document();
            baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            PdfPTable mainLayout = createHeader();
            for (ActivityComparisonResult cr : activityComparisonResult) {
                generateSimpleCell(mainLayout, TranslatorWorker.translateText("Activity") + " : " + cr.getName(),
                        Element.ALIGN_CENTER, HEADER_FONT, HEADER_BRACKGROUND_COLOR, HEADER_COLSPAN);
                Map<String, List<CompareOutput>> outputCollectionGrouped = cr.getCompareOutput();
                createValues(outputCollectionGrouped, mainLayout);
            }
            document.add(mainLayout);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            //TODO properly handle exception
        }
        return baos;
    }

    private PdfPTable createHeader() throws DocumentException {

        PdfPTable mainLayout = buildPdfTable(HEADER_COLUMNS);
        mainLayout.setWidths(new float[]{1f, 2f, 2f});
        mainLayout.setWidthPercentage(TABLE_WIDTH);
        mainLayout.getDefaultCell().setBorder(0);

        generateSimpleCell(mainLayout, TranslatorWorker.translateText("Activity difference",
                TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()), Header.ALIGN_CENTER, HEADER_FONT,
                HEADER_BRACKGROUND_COLOR, HEADER_COLSPAN);


        generateSimpleCell(mainLayout, TranslatorWorker.translateText("Value Name",
                TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()), Header.ALIGN_CENTER, HEADER_FONT,
                HEADER_BRACKGROUND_COLOR, null);
        generateSimpleCell(mainLayout, TranslatorWorker.translateText("Previous Version",
                TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()), Header.ALIGN_CENTER, HEADER_FONT,
                HEADER_BRACKGROUND_COLOR, null);
        generateSimpleCell(mainLayout, TranslatorWorker.translateText("New Version",
                TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()), Header.ALIGN_CENTER, HEADER_FONT,
                HEADER_BRACKGROUND_COLOR, null);

        return mainLayout;


    }

    private void createValues(Map<String, List<CompareOutput>> outputCollectionGrouped, PdfPTable mainLayout) {
        Set<String> keyset = outputCollectionGrouped.keySet();
        for (String key : keyset) {
            List<CompareOutput> nameList = outputCollectionGrouped.get(key);
            CompareOutput comp = nameList.get(VALUE_NAME);
            generateSimpleCell(mainLayout, ActivityVersionUtil.sanitizeHtmlForExport(key), Element.ALIGN_CENTER, PLAIN_FONT,
                    null, null);
            generateSimpleCell(mainLayout, ActivityVersionUtil.sanitizeHtmlForExport(comp.getStringOutput()[1]));
            generateSimpleCell(mainLayout, ActivityVersionUtil.sanitizeHtmlForExport(comp.getStringOutput()[0]));

        }
    }

    private void generateSimpleCell(PdfPTable mainLayout, String value) {
        generateSimpleCell(mainLayout, value, null, PLAIN_FONT, null, null);
    }

    private void generateSimpleCell(PdfPTable mainLayout, String value, Integer alignment, Font font,
                                    BaseColor backgroundColor, Integer colSpan) {

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(value, font);
        if (alignment != null) {
            p.setAlignment(alignment);
        }
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (colSpan != null) {
            cell.setColspan(colSpan);
        }
        cell.addElement(p);
        mainLayout.addCell(cell);
    }

    private static PdfPTable buildPdfTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        return table;
    }

}