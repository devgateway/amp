package org.dgfoundation.amp.gpi.reports.export.pdf;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;

import com.lowagie.text.Element;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator9bPdfExporter extends GPIReportPdfExporter {
	
	public GPIReportIndicator9bPdfExporter() {
		relativeWidths = new float [] {8f, 32f, 15f, 15f, 15f, 15f};
		reportTitle = "Indicator 9";
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
}
