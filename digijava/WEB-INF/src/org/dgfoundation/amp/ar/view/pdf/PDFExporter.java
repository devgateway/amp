/**
 * PDFExporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public abstract class PDFExporter extends Exporter {
	
	protected PdfPTable table;
	protected Long ownerId; 
	
	public PDFExporter(Exporter parent,Viewable item) {
		super(parent,item);
		PDFExporter pdfParent=(PDFExporter) parent;
		this.table=pdfParent.getTable();
		this.ownerId=pdfParent.getOwnerId();

	}
	
	public PDFExporter(PdfPTable table, Viewable item,Long ownerId) {
		this.table=table;
		this.item=item;
		this.ownerId=ownerId;
	}

	/**
	 * @return Returns the item.
	 */
	public Viewable getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(Viewable item) {
		this.item = item;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId The ownerId to set.
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the table.
	 */
	public PdfPTable getTable() {
		return table;
	}

	/**
	 * @param table The table to set.
	 */
	public void setTable(PdfPTable table) {
		this.table = table;
	}

	

}
