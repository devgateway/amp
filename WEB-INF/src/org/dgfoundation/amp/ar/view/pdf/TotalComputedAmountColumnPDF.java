/**
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.UndisbursedAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedAmountColumnPDF extends TotalAmountColumnPDF {

	/**
	 * @param parent
	 * @param item
	 */
	public TotalComputedAmountColumnPDF(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public TotalComputedAmountColumnPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

}
