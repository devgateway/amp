/**
 * XLSExporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public abstract class XLSExporter extends Exporter {
	
	protected static Logger logger = Logger.getLogger(XLSExporter.class);

	public static void resetStyles() {
		regularStyle=null;
		amountStyle=null;
		highlightedStyle=null;
	}
	
	protected static HSSFCellStyle regularStyle = null;
	protected static HSSFCellStyle amountStyle = null;
	protected static HSSFCellStyle highlightedStyle = null;

	protected IntWrapper rowId;

	protected IntWrapper colId;

	protected Long ownerId;

	protected HSSFRow row;

	protected HSSFSheet sheet;

	protected HSSFWorkbook wb;

	protected HSSFCell getRegularCell(HSSFRow row) {
		HSSFCell cell = row.createCell(colId.shortValue());
		cell.setCellStyle(this.getRegularStyle());
		return cell;
	}

	protected HSSFCell getRegularCell() {
		return getRegularCell(row);
	}

	protected HSSFCell getCell(HSSFCellStyle style) {
		return getCell(row, style);
	}

	protected HSSFCell getCell(HSSFRow row, HSSFCellStyle style) {
		HSSFCell cell = row.createCell(colId.shortValue());
		cell.setCellStyle(style);
		return cell;
	}

	protected HSSFCellStyle getRegularStyle() {
		if (regularStyle == null) {
			HSSFCellStyle cs = wb.createCellStyle();
			HSSFFont font= wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints(new Short("8"));
			cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
			cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
			cs.setFont(font);
			cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			regularStyle = cs;
		}
		return regularStyle;
	}

	protected HSSFCellStyle getAmountStyle() {
		if (amountStyle == null) {
			HSSFCellStyle cs = wb.createCellStyle();
			HSSFFont font= wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setColor( HSSFColor.BLUE.index );
			cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
			cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
			HSSFDataFormat df = wb.createDataFormat();
			cs.setDataFormat(df.getFormat("General"));
			//cs.setDataFormat(df.getFormat("_(*###,###,###,###);_(*###,###,###,###.##)"));
		
			cs.setFont(font);
			cs.setWrapText(true);
			cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			amountStyle = cs;
		}
		return amountStyle;
	}

	
	protected HSSFCellStyle getHighlightedStyle(boolean border) {
		if(highlightedStyle==null) {
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setFillForegroundColor(HSSFColor.WHITE.index);
		cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		if (border){
			cs.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			cs.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			cs.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			cs.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			
		}else{
			cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
			cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
		}
			cs.setFont(font);		
		highlightedStyle=cs;
		}
		return highlightedStyle;
	}

	public void makeColSpan(int size,Boolean border) {
		size--;
		if(size<0) size=0;
		Region r=new Region(rowId.intValue(), colId.shortValue(),
				rowId.intValue(), (short) (colId.shortValue() + size));
		try {
			if (border){
				HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM,r,sheet,wb);
				HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM,r,sheet,wb);
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_MEDIUM,r,sheet,wb);
				HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_MEDIUM,r,sheet,wb);
			
			}else{
				HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
				HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
				HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
			
		try{
			sheet.addMergedRegion(r);
		} catch(Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		try{
			sheet.autoSizeColumn(r.getColumnFrom());
		} catch(Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	
		colId.inc(++size);
	}

	public void makeRowSpan(int size) {
		sheet.addMergedRegion(new Region(rowId.intValue(), colId.shortValue(),
				rowId.intValue() + size, colId.shortValue()));
	}

	/**
	 * @return Returns the row.
	 */
	public HSSFRow getRow() {
		return row;
	}

	/**
	 * @param row
	 *            The row to set.
	 */
	public void setRow(HSSFRow row) {
		this.row = row;
	}

	/**
	 * @return Returns the colId.
	 */
	public IntWrapper getColId() {
		return colId;
	}

	/**
	 * @param colId
	 *            The colId to set.
	 */
	public void setColId(IntWrapper colId) {
		this.colId = colId;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            The ownerId to set.
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the rowId.
	 */
	public IntWrapper getRowId() {
		return rowId;
	}

	/**
	 * @param rowId
	 *            The rowId to set.
	 */
	public void setRowId(IntWrapper rowId) {
		this.rowId = rowId;
	}

	/**
	 * @param parent
	 * @param item
	 */
	public XLSExporter(Exporter parent, Viewable item) {
		super(parent, item);
		XLSExporter xlsParent = (XLSExporter) parent;
		this.colId = xlsParent.getColId();
		this.ownerId = xlsParent.getOwnerId();
		this.rowId = xlsParent.getRowId();
		this.row = xlsParent.getRow();
		this.sheet = xlsParent.getSheet();
		this.wb = xlsParent.getWb();
	}

	public XLSExporter(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		this.sheet = sheet;
		this.row = row;
		this.rowId = rowId;
		this.colId = colId;
		this.ownerId = ownerId;
		this.item = item;
		this.wb = wb;
	}

	/**
	 * @return Returns the sheet.
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            The sheet to set.
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return Returns the wb.
	 */
	public HSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * @param wb
	 *            The wb to set.
	 */
	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

}
