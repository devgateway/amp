/**
 * XLSExporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	
//	public static void resetStyles() {
//        regularStyle = null;
//        amountStyle = null;
//        highlightedStyle = null;
//        hierarchyLevel1Style=null;
//        hierarchyOtherStyle=null;
//        amountHierarchyLevel1Style=null;
//        amountHierarchyOtherStyle=null;
//	}
	
	private boolean autoSize = true;
	
	
	/********************************************************
	 * When adding new CellStyles, don't forget to inherit 
	 * the value in the constructor from the parent!
	 * 
	 * Not doing so will cause export to crash on big reports
	 * due to the number of styles limitation in an 
	 * excell sheet.
	 */
	protected static XSSFCellStyle regularStyle = null;
	protected static XSSFCellStyle amountStyle = null;
	protected static XSSFCellStyle highlightedStyle = null;
	protected XSSFCellStyle hierarchyLevel1Style = null;
	protected XSSFCellStyle hierarchyOtherStyle = null;
	protected XSSFCellStyle amountHierarchyLevel1Style = null;
	protected XSSFCellStyle amountHierarchyOtherStyle = null;	
	/*********************************************************/

	protected IntWrapper rowId;

	protected IntWrapper colId;

	protected Long ownerId;

	protected XSSFRow row;

	protected XSSFSheet sheet;

	protected XSSFWorkbook wb;
	
	public static void resetStyles() {
        regularStyle = null;
        amountStyle = null;
        highlightedStyle = null;
	}

	protected XSSFCell getRegularCell(XSSFRow row) {
		XSSFCell cell = row.createCell(colId.shortValue());
		XSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.cloneStyleFrom(this.getAmountStyle());
		cell.setCellStyle(cellstyle);
		return cell;
	}

	protected XSSFCell getRegularCell() {
		return getRegularCell(row);
	}

	protected XSSFCell getCell(XSSFCellStyle style) {
		return getCell(row, style);
	}

	protected XSSFCell getCell(XSSFRow row, XSSFCellStyle style) {
		XSSFCell cell = row.createCell(colId.intValue());
		XSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.cloneStyleFrom(style);
		cell.setCellStyle(cellstyle);
		return cell;
	}

	protected XSSFCellStyle getRegularStyle() {
		if (regularStyle == null) {
			XSSFCellStyle cs = wb.createCellStyle();
			XSSFFont font= wb.createFont();
			font.setFontName("arial");
			font.setFontHeightInPoints(new Short("8"));
			cs.setBorderBottom(XSSFCellStyle.BORDER_NONE);
			cs.setBorderLeft(XSSFCellStyle.BORDER_NONE);
			cs.setBorderRight(XSSFCellStyle.BORDER_NONE);
			cs.setBorderTop(XSSFCellStyle.BORDER_NONE);
			cs.setFont(font);
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			regularStyle = cs;
		}
		return regularStyle;
	}

	protected XSSFCellStyle getAmountStyle() {
		if (amountStyle == null) {
			XSSFCellStyle cs = wb.createCellStyle();
			XSSFFont font= wb.createFont();
			font.setFontName("arial");
			font.setFontHeightInPoints(new Short("8"));
			font.setColor(IndexedColors.BLUE.getIndex());
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
			XSSFDataFormat df = wb.createDataFormat();
			cs.setDataFormat(df.getFormat("General"));
		
			cs.setFont(font);
			cs.setWrapText(true);
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			amountStyle = cs;
		}
		return amountStyle;
	}

	
	protected XSSFCellStyle getAmountHierarchyLevel1Style() {
		if (amountHierarchyLevel1Style == null) {
			XSSFCellStyle cs = wb.createCellStyle();
			XSSFFont font= wb.createFont();
			cs.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
			cs.setFillPattern((short) XSSFCellStyle.SOLID_FOREGROUND);
			font.setFontName("arial");
			font.setFontHeightInPoints(new Short("8"));
			font.setColor(IndexedColors.BLUE.getIndex());
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
			XSSFDataFormat df = wb.createDataFormat();
			cs.setDataFormat(df.getFormat("General"));
		
			cs.setFont(font);
			cs.setWrapText(true);
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			amountHierarchyLevel1Style = cs;
		}
		return amountHierarchyLevel1Style;
	}

	
	protected XSSFCellStyle getAmountHierarchyOtherStyle() {
		if (amountHierarchyOtherStyle == null) {
			XSSFCellStyle cs = wb.createCellStyle();
			XSSFFont font= wb.createFont();
			cs.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            cs.setFillPattern((short) XSSFCellStyle.SOLID_FOREGROUND);
			font.setFontName("arial");
			font.setFontHeightInPoints(new Short("8"));
			font.setColor(IndexedColors.BLUE.getIndex());
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
			XSSFDataFormat df = wb.createDataFormat();
			cs.setDataFormat(df.getFormat("General"));
		
			cs.setFont(font);
			cs.setWrapText(true);
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			amountHierarchyOtherStyle = cs;
		}
		return amountHierarchyOtherStyle;
	}
	/* No point in having the border parameter, never false right now */
	protected XSSFCellStyle getHighlightedStyle() {
        if (highlightedStyle == null) {
            XSSFCellStyle cs = wb.createCellStyle();
            cs.setWrapText(true);
            cs.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cs.setFillPattern((short) XSSFCellStyle.SOLID_FOREGROUND);
            XSSFFont font = wb.createFont();
            font.setFontName("arial");
        	font.setFontHeightInPoints(new Short("8"));
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            
            cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
            cs.setFont(font);
            highlightedStyle = cs;
        }
		
		return highlightedStyle;
	}

	/* No point in having the border parameter, never false right now */
	protected XSSFCellStyle getHierarchyLevel1Style() {
        if (hierarchyLevel1Style == null) {
            XSSFCellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
            cs.setFillPattern((short) XSSFCellStyle.SOLID_FOREGROUND);
            XSSFFont font = wb.createFont();
            font.setFontName("arial");
        	font.setFontHeightInPoints(new Short("8"));
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
           
            cs.setFont(font);
            hierarchyLevel1Style = cs;
        }
		
		return hierarchyLevel1Style;
	}
	/* No point in having the border parameter, never false right now */
	protected XSSFCellStyle getHierarchyOtherStyle() {
        if (hierarchyOtherStyle == null) {
            XSSFCellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            cs.setFillPattern((short) XSSFCellStyle.SOLID_FOREGROUND);
            XSSFFont font = wb.createFont();
            font.setFontName("arial");
        	font.setFontHeightInPoints(new Short("8"));
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
            
            cs.setFont(font);
            hierarchyOtherStyle = cs;
        }
		
		return hierarchyOtherStyle;
	}
	
	protected XSSFCellStyle getSpanStyle(Boolean border) {
		XSSFCellStyle style = wb.createCellStyle();
		try {
			if (border){
				style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
				style.setBorderRight(XSSFCellStyle.BORDER_THIN);
				style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			}else{
				style.setBorderBottom(XSSFCellStyle.BORDER_NONE);
				style.setBorderLeft(XSSFCellStyle.BORDER_NONE);
				style.setBorderRight(XSSFCellStyle.BORDER_NONE);
				style.setBorderTop(XSSFCellStyle.BORDER_NONE);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return style;
		
	}
	public void makeColSpan(int size,Boolean border) {
		size--;
		if(size<0){
			size=0;
		}
		CellRangeAddress r=new CellRangeAddress(rowId.intValue(),rowId.intValue(), colId.shortValue(), 
				(short) (colId.shortValue() + size));
		
		try{
			sheet.addMergedRegion(r);
		} catch(Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		try{
			sheet.autoSizeColumn(r.getFirstColumn());
		} catch(Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	
        if (this.autoSize) {
            sheet.autoSizeColumn(r.getFirstColumn());
        }
		colId.inc(++size);
	}

	
	public void makeRowSpan(int size, boolean border) {
		CellRangeAddress r=new CellRangeAddress(rowId.intValue(),rowId.intValue() +
				size, colId.shortValue(),colId.shortValue());
		sheet.addMergedRegion(r);
	}

	/**
	 * @return Returns the row.
	 */
	public XSSFRow getRow() {
		return row;
	}

	/**
	 * @param row
	 *            The row to set.
	 */
	public void setRow(XSSFRow row) {
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
		
		this.regularStyle = xlsParent.getRegularStyle();
		this.amountStyle = xlsParent.getAmountStyle();
		this.highlightedStyle = xlsParent.getHighlightedStyle();
		this.hierarchyLevel1Style = xlsParent.getHierarchyLevel1Style();
		this.hierarchyOtherStyle = xlsParent.getHierarchyOtherStyle();
		this.amountHierarchyLevel1Style = xlsParent.getAmountHierarchyLevel1Style();
		this.amountHierarchyOtherStyle = xlsParent.getAmountHierarchyOtherStyle();
	}

	public XLSExporter(XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row,
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
	public XSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            The sheet to set.
	 */
	public void setSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return Returns the wb.
	 */
	public XSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * @param wb
	 *            The wb to set.
	 */
	public void setWb(XSSFWorkbook wb) {
		this.wb = wb;
	}
    public boolean isAutoSize() {
        return autoSize;
}
public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
}
}
