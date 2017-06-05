package org.dgfoundation.amp.gpi.reports.export.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.export.GPIReportExporter;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportPdfExporter implements GPIReportExporter {

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

		generateReportTable(doc, writer, report);

		doc.close();

		return os.toByteArray();
	}

	private Document createDocument() {
		Document doc = new Document(calculateDocumentSize(calculateWidth()));

		return doc;
	}

	public void generateReportTable(Document doc, PdfWriter writer, GPIReport report) {
		try {
			Paragraph body = new Paragraph();
			
			renderReportTitle(report, body);
			renderReportSettings(report, body);
			renderReportTableSummary(report, body);
			renderReportTable(report, body);
			renderReportStatistics(report, body);
			
			doc.add(body);
		} catch (Exception e) {
			throw new RuntimeException("Error during creating the GPI Report in PDF", e);
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void renderReportTitle(GPIReport report, Paragraph body) {
		float fntSize = 20f;
		
		Paragraph titleParagraph = new Paragraph(reportTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, fntSize));
		body.add(titleParagraph);
	}
	
	public void renderReportSettings(GPIReport report, Paragraph body) {
		Font bf10 = new Font(Font.HELVETICA, 10);
		
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
		
		Font bfBold14 = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(0, 0, 0));
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

		report.getPage().getHeaders()
				.forEach(column -> insertCell(table, getHeaderColumnName(column), 
						Element.ALIGN_CENTER, 1, bfBold11, bkgColor));

		table.setHeaderRows(1);

	}

	protected void renderReportTableData(GPIReport report, PdfPTable table) {
		Font bf11 = new Font(Font.HELVETICA, 10);
		Color bkgColor = Color.WHITE;

		report.getPage().getContents().forEach(row -> {
			report.getPage().getHeaders().forEach(col -> {
				insertCell(table, row.get(col), getCellAlignment(col.originalColumnName), 1, bf11, bkgColor);
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
		return report.getPage().getHeaders().size();
	}

	protected Rectangle calculateDocumentSize(int resultWidth) {
		Rectangle size = PageSize.A4;
		if (resultWidth >= 7) {
			size = PageSize.A4.rotate();
		}
		
		if (resultWidth >= 15) {
			size = PageSize.A3;
		}

		return size;
	}
	
	protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font, Color bkgColor) {
		insertCell(table, text, align, colspan, 1, font, bkgColor, 0);
	}
	
	protected void insertCell(PdfPTable table, String text, int align, int colspan, int rowsPan, Font font,
			Color bkgColor) {
		insertCell(table, text, align, colspan, rowsPan, font, bkgColor, 0);
	}
	
	
	protected void insertCell(PdfPTable table, String text, int align, int colspan, int rowspan, Font font,
			Color bkgColor, float height) {
		
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(colspan);
		cell.setRowspan(rowspan);
		cell.setBackgroundColor(bkgColor);
		
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}
		
		if (height > 0) {
			cell.setMinimumHeight(height);
		}

		table.addCell(cell);
	}
	
	public int getCellAlignment(String columnName) {
		return Element.ALIGN_LEFT;
	}
}
