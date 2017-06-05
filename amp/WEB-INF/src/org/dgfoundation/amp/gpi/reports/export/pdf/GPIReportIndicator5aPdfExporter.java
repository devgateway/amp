package org.dgfoundation.amp.gpi.reports.export.pdf;

import java.awt.Color;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator5aPdfExporter extends GPIReportPdfExporter {
	
	public GPIReportIndicator5aPdfExporter() {
		relativeWidths = new float [] {5f, 25f, 10f, 10f, 10f, 10f, 10f, 10f, 10f};
		reportTitle = "Indicator 5a";
	}
	
	public void renderReportTableSummary(GPIReport report, Paragraph body) {
		PdfPTable table = new PdfPTable(report.getSummary().size());
		
		// set table width a percentage of the page width
		table.setWidthPercentage(50f);
		table.setHorizontalAlignment(0);
		
		Font bfBold14 = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(0, 0, 0));
		Color bkgColor = Color.ORANGE;
		
		for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
			GPIReportOutputColumn column = report.getPage().getHeaders().get(i);
			if (report.getSummary().containsKey(column)) {
				String cellValue = String.format("%s\n%s", report.getSummary().get(column), column.columnName);
				insertCell(table, cellValue, Element.ALIGN_LEFT, 1, 1, bfBold14, bkgColor, 30f);
			}
		}

		body.add(table);
		body.add(new Paragraph());
	}
	
	public void renderReportTableHeader(GPIReport report, PdfPTable table) {
		Font bfBold11 = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0));
		Color bkgColor = Color.LIGHT_GRAY;

		report.getPage().getHeaders().stream().filter(c -> !isHiddenColumn(c.originalColumnName))
				.forEach(column -> insertCell(table, getHeaderColumnName(column), 
						Element.ALIGN_CENTER, 1, bfBold11, bkgColor));

		table.setHeaderRows(1);

	}
	
	@Override
	protected void renderReportTableData(GPIReport report, PdfPTable table) {
		Font bf11 = new Font(Font.HELVETICA, 10);
		Color bkgColor = Color.WHITE;

		report.getPage().getContents().forEach(row -> {
			report.getPage().getHeaders().forEach(col -> {
				if (!isHiddenColumn(col.originalColumnName)) {
					insertCell(table, row.get(col), getCellAlignment(col.originalColumnName), 1, bf11, bkgColor);
				}
			});
		});
	}
	
	@Override
	public int getCellAlignment(String columnName) {
		switch(columnName) {
			case ColumnConstants.DONOR_AGENCY:
			case ColumnConstants.DONOR_GROUP:
				return Element.ALIGN_LEFT;
			case GPIReportConstants.COLUMN_CONCESSIONAL:
			case GPIReportConstants.COLUMN_YEAR:
				return Element.ALIGN_CENTER;
			default:
				return Element.ALIGN_RIGHT;
		}
	}
	
	protected boolean isHiddenColumn(String columnName) {
		return columnName.equals(GPIReportConstants.COLUMN_REMARK);
	}
	
	@Override
	protected String getHeaderColumnName(GPIReportOutputColumn column) {
		return getColumnHeaderLabel(column.originalColumnName);
	}
	
	private String getColumnHeaderLabel(String columnName) {
		return INDICATOR_5A_COLUMN_LABELS.containsKey(columnName) ? 
				INDICATOR_5A_COLUMN_LABELS.get(columnName) : columnName;
	}
}
