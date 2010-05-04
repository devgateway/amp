package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.itextpdf.text.pdf.PdfPTable;

public class ComputedDateCellPDF extends TextCellPDF {

	public ComputedDateCellPDF(Exporter parent, Viewable item) {
		super(parent, item);
	}

	/**
	 * @param table
	 * @param item
	 */
	public ComputedDateCellPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

}
