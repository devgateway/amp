/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Diego Dimunzio Ago 15, 2009
 */
public class TotalComputedMeasureColumnXLS extends TotalAmountColumnXLS {
	public TotalComputedMeasureColumnXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	public TotalComputedMeasureColumnXLS(XSSFWorkbook wb, XSSFSheet sheet,
			XSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
			Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

}
