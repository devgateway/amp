package org.dgfoundation.amp.reports.saiku.export;

/**Enum that holds the exporters
 * @author Viorel Chihai
 *
 */
public enum SaikuReportExportType {
    
    PDF(SaikuReportPdfExporter.class),
    XLSX(SaikuReportXlsxExporter.class),
    XLSX_PLAIN(SaikuReportXlsxPlainExporter.class),
    CSV(SaikuReportCsvExporter.class),
    XML(SaikuReportXmlExporter.class);
    
    public final Class<? extends SaikuReportExporter> executor;
    
    private SaikuReportExportType(Class<? extends SaikuReportExporter> clazz) {
        this.executor = clazz;
    }
}
