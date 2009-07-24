package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

public class ComputedCountingAmountCellPDF extends ComputedAmountCellPDF {
	public ComputedCountingAmountCellPDF(Exporter parent, Viewable item) {
		super(parent, item);
	}

	/**
	 * @param table
	 * @param item
	 */
	public ComputedCountingAmountCellPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

}
