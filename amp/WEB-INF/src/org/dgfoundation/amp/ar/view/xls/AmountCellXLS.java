/**
 * AmountCellXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.module.aim.helper.FormatHelper;

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
	public AmountCellXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * returns Object[2]: res[0] = cell.getCellType(), res[1] = either Double or String
	 * @param amount
	 * @param format
	 * @return
	 */
	public static Object[] formatCell(double amount, DecimalFormat format)
	{
		boolean isNumericFormat = (!format.isGroupingUsed()) && 
				((format.getDecimalFormatSymbols().getDecimalSeparator() == '.') || (format.getDecimalFormatSymbols().getDecimalSeparator() == ','));
		
		if (isNumericFormat)
		{
		// possibility 1: NUMERIC cell, unformatted (e.g. might output cells with values like 1185797.24891408)		 
			long powerOfTen = 1;
			for(int i = 1; i <= format.getMaximumFractionDigits(); i++)
				powerOfTen *= 10;
			
			return new Object[] {HSSFCell.CELL_TYPE_NUMERIC, Math.round(amount * powerOfTen) / ((double) powerOfTen)};
		}
	
	
	{
		// possibility 2: TEXT cell, formatted according to the report-settings-specified format - DOES NOT REALLY WORK
		BigDecimal bd = new BigDecimal(amount);
		bd = bd.setScale(format.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
		String value = format.format(bd);
		return new Object[] {HSSFCell.CELL_TYPE_STRING, value};
	}

	}
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		AmountCell ac=(AmountCell) item;
		HSSFCellStyle amountStyle;
		
		if(parent instanceof TrailCellsXLS)
		if(parent.getItem().getNearestReportData().getLevelDepth()==2) 
			amountStyle = this.getAmountHierarchyLevel1Style();
		 else amountStyle = this.getAmountHierarchyOtherStyle();
		else
			amountStyle=this.getAmountStyle();
		
		HSSFCell cell=this.getCell(amountStyle);
		
		Object[] formatInfo = formatCell(ac.getAmount(), this.getFilter().getCurrentFormat());
		cell.setCellType((Integer) formatInfo[0]);
		
		if (formatInfo[1] instanceof String)
			cell.setCellValue((String) formatInfo[1]);
		
		if (formatInfo[1] instanceof Double)
			cell.setCellValue((Double) formatInfo[1]);
		
		//DecimalFormat mf = new DecimalFormat("###,###,###,###.##");
		//mf.setMaximumFractionDigits(2);

		colId.inc();
	}

}
