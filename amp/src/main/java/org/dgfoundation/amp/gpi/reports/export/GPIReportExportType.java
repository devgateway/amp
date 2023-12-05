package org.dgfoundation.amp.gpi.reports.export;

import org.dgfoundation.amp.gpi.reports.export.excel.*;
import org.dgfoundation.amp.gpi.reports.export.pdf.*;

/**
 * Enum that holds the gpi report exporters
 * 
 * @author Viorel Chihai
 *
 */
public enum GPIReportExportType {
    
    PDF(GPIReportPdfExporter.class),
    XLSX(GPIReportXlsxExporter.class),
    XLSX_1_1(GPIReportIndicator1Output1XlsxExporter.class),
    XLSX_1_2(GPIReportIndicator1Output2XlsxExporter.class),
    XLSX_5a(GPIReportIndicator5aXlsxExporter.class),
    XLSX_5b(GPIReportIndicator5bXlsxExporter.class),
    XLSX_6(GPIReportIndicator6XlsxExporter.class),
    XLSX_9b(GPIReportIndicator9bXlsxExporter.class),
    PDF_1_1(GPIReportIndicator1Output1PdfExporter.class),
    PDF_1_2(GPIReportIndicator1Output2PdfExporter.class),
    PDF_5a(GPIReportIndicator5aPdfExporter.class),
    PDF_5b(GPIReportIndicator5bPdfExporter.class),
    PDF_6(GPIReportIndicator6PdfExporter.class),
    PDF_9b(GPIReportIndicator9bPdfExporter.class);
    
    public final Class<? extends GPIReportExporter> executor;
    
    private GPIReportExportType(Class<? extends GPIReportExporter> clazz) {
        this.executor = clazz;
    }
}
