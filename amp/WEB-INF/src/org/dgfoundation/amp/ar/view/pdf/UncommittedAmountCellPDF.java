package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

public class UncommittedAmountCellPDF extends AmountCellPDF  {
	/**
	 * @param parent
	 * @param item
	 */
	public UncommittedAmountCellPDF(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public UncommittedAmountCellPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}
}
