/**
 * AmountCellXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.math.BigDecimal;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class AmountCellXLS extends XLSExporter {

	
	/**	
	 * @param parent
	 * @param item
	 */
	public AmountCellXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public AmountCellXLS(XSSFWorkbook wb ,XSSFSheet sheet, XSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		AmountCell ac=(AmountCell) item;
		XSSFCellStyle amountStyle;
		
		if(parent instanceof TrailCellsXLS)
		if(parent.getItem().getNearestReportData().getLevelDepth()==2) 
			amountStyle = this.getAmountHierarchyLevel1Style();
		 else amountStyle = this.getAmountHierarchyOtherStyle();
		else
			amountStyle=this.getAmountStyle();
		
		XSSFCell cell=this.getCell(amountStyle);
		
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);

		//DecimalFormat mf = new DecimalFormat("###,###,###,###.##");
		//mf.setMaximumFractionDigits(2);
		double tempAm = ac.getAmount();
		BigDecimal bd = new BigDecimal(tempAm);
		bd = bd.setScale(2, BigDecimal.ROUND_UP);
		cell.setCellValue(new Double(bd.doubleValue()));
		colId.inc();
	}

}
