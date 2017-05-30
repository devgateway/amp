package org.dgfoundation.amp.gpi.reports.export;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.dgfoundation.amp.gpi.reports.GPIReport;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
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

	private GPIReport report;

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

	protected void generateReportTable(Document doc, PdfWriter writer, GPIReport report) {
		//float[] columnWidths = { 1.5f, 2f, 5f, 2f, 2f };
		PdfPTable reportTable = new PdfPTable(report.getPage().getHeaders().size());

		try {
			Paragraph paragraph = new Paragraph("Indicator x");

			// set table width a percentage of the page width
			reportTable.setWidthPercentage(90f);

			renderReportTableHeader(report, reportTable);
			renderReportData(report, reportTable);

			// add the PDF table to the paragraph
			paragraph.add(reportTable);
			doc.add(paragraph);
		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (doc != null) {
				// close the document
				doc.close();
			}
			if (writer != null) {
				// close the writer
				writer.close();
			}
		}
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param report
	 */
	protected void renderReportTableHeader(GPIReport report, PdfPTable table) {
		Font bfBold12 = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, new Color(0, 0, 0));

		report.getPage().getHeaders()
				.forEach(column -> insertCell(table, column.columnName, Element.ALIGN_CENTER, 1, bfBold12));

		table.setHeaderRows(1);

	}

	/**
	 * 
	 * @param report
	 * @param table
	 */
	protected void renderReportData(GPIReport report, PdfPTable table) {
		Font bf12 = new Font(Font.TIMES_ROMAN, 12);
		// insert an empty row

		report.getPage().getContents().forEach(row -> {
			report.getPage().getHeaders().forEach(col -> {
				insertCell(table, row.get(col), Element.ALIGN_CENTER, 1, bf12);
			});
		});

	}

	private int calculateWidth() {
		return report.getPage().getHeaders().size();
	}

	private Rectangle calculateDocumentSize(int resultWidth) {
		Rectangle size = PageSize.A4.rotate();
		if (resultWidth >= 5) {
			size = PageSize.A3.rotate();
		}
		if (resultWidth >= 10) {
			size = PageSize.A2.rotate();
		}
		if (resultWidth >= 15) {
			size = PageSize.A1.rotate();
		}
		if (resultWidth >= 20) {
			size = PageSize.A0.rotate();
		}

		return size;
	}

	private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}

		table.addCell(cell);
	}
}
