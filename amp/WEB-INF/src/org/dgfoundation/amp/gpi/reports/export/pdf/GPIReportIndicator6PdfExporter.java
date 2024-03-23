package org.dgfoundation.amp.gpi.reports.export.pdf;

import com.itextpdf.text.Element;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator6PdfExporter extends GPIReportPdfExporter {
    
    public GPIReportIndicator6PdfExporter() {
        relativeWidths = new float [] {10f, 30f, 20f, 20f, 20f};
        reportTitle = "Indicator 6";
    }
    
    @Override
    public int getCellAlignment(String columnName) {
        switch (columnName) {
            case MeasureConstants.PLANNED_DISBURSEMENTS:
            case GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET:
            case GPIReportConstants.COLUMN_PLANNED_ON_BUDGET:
                return Element.ALIGN_RIGHT;
            case GPIReportConstants.COLUMN_YEAR:
                return Element.ALIGN_CENTER;
            default:
                return super.getCellAlignment(columnName);
        }
    }
}
