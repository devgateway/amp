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
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public abstract class XLSExporter extends Exporter {
    
    protected static Logger logger = Logger.getLogger(XLSExporter.class);

    
//  public static void resetStyles() {
//        regularStyle = null;
//        amountStyle = null;
//        highlightedStyle = null;
//        hierarchyLevel1Style=null;
//        hierarchyOtherStyle=null;
//        amountHierarchyLevel1Style=null;
//        amountHierarchyOtherStyle=null;
//  }
    
    //private boolean autoSize = true;
    
    
    /********************************************************
     * When adding new CellStyles, don't forget to inherit 
     * the value in the constructor from the parent!
     * 
     * Not doing so will cause export to crash on big reports
     * due to the number of styles limitation in an 
     * excell sheet.
     */
    protected HSSFCellStyle regularStyle = null;
    protected HSSFCellStyle amountStyle = null;
    protected HSSFCellStyle highlightedStyle = null;
    protected HSSFCellStyle pledgeStyle = null;
    protected HSSFCellStyle hierarchyLevel1Style = null;
    protected HSSFCellStyle hierarchyOtherStyle = null;
    protected HSSFCellStyle amountHierarchyLevel1Style = null;
    protected HSSFCellStyle amountHierarchyOtherStyle = null;
    /*********************************************************/

    protected IntWrapper rowId;

    protected IntWrapper colId;

    protected Long ownerId;

    protected HSSFRow row;

    protected HSSFSheet sheet;

    protected HSSFWorkbook wb;
    
    public void resetStyles() {
        regularStyle = null;
        amountStyle = null;
        highlightedStyle = null;
        pledgeStyle = null;
    }
    
    protected HSSFCell getPledgeDisguisedAsAnActivityCell(HSSFRow row) {
        HSSFCell cell = row.createCell((int)colId.shortValue());
//      HSSFCellStyle cellstyle = wb.createCellStyle();
//      cellstyle.cloneStyleFrom(this.getAmountStyle());
        cell.setCellStyle(this.getDisquisedPledgeStyle());
        return cell;
    }

    protected HSSFCell getRegularCell(HSSFRow row) {
        HSSFCell cell = row.createCell((int)colId.shortValue());
//      HSSFCellStyle cellstyle = wb.createCellStyle();
//      cellstyle.cloneStyleFrom(this.getAmountStyle());
        cell.setCellStyle(this.getAmountStyle());
        return cell;
    }

    /**
     * gets a regular cell which has colour / style of a "pledge disguised as an activity" style
     * @return
     */
    protected HSSFCell getPledgeDisguisedAsAnActivityCell() {
        return getPledgeDisguisedAsAnActivityCell(getOrCreateRow());
    }
    
    protected HSSFCell getRegularCell() {
//      if (row == null)
//          row = sheet.createRow(rowId.shortValue());
        return getRegularCell(getOrCreateRow());
    }

    protected HSSFCell getCell(HSSFCellStyle style) {
//      if (row == null)
//          row = sheet.createRow(rowId.shortValue());      
        return getCell(getOrCreateRow(), style);
    }

    protected HSSFCell getCell(HSSFRow row, HSSFCellStyle style) {
        HSSFCell cell = row.createCell((int)colId.intValue());
//      HSSFCellStyle cellstyle = wb.createCellStyle();
//      cellstyle.cloneStyleFrom(style);
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
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            cs.setWrapText(true);
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            amountStyle = cs;
        }
        return amountStyle;
    }
    
    protected HSSFCellStyle getDisquisedPledgeStyle() {
        if (pledgeStyle == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            HSSFFont font= wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor(HSSFColor.BROWN.index);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            cs.setWrapText(true);
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            pledgeStyle = cs;
        }
        return pledgeStyle;
    }
    
    protected HSSFCellStyle getAmountHierarchyLevel1Style() {
        if (amountHierarchyLevel1Style == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            HSSFFont font= wb.createFont();
            cs.setFillForegroundColor(HSSFColor.TURQUOISE.index);
            cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor( HSSFColor.BLUE.index );
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            cs.setWrapText(true);
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            amountHierarchyLevel1Style = cs;
        }
        return amountHierarchyLevel1Style;
    }

    
    protected HSSFCellStyle getAmountHierarchyOtherStyle() {
        if (amountHierarchyOtherStyle == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            HSSFFont font= wb.createFont();
            cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor( HSSFColor.BLUE.index );
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            cs.setWrapText(true);
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            amountHierarchyOtherStyle = cs;
        }
        return amountHierarchyOtherStyle;
    }
    /* No point in having the border parameter, never false right now */
    protected HSSFCellStyle getHighlightedStyle() {
        if (highlightedStyle == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setWrapText(true);
            cs.setFillForegroundColor(HSSFColor.WHITE.index);
            cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //if (border) {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            /*
            } else {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
            }
            */
            cs.setFont(font);
            highlightedStyle = cs;
        }
        
        return highlightedStyle;
    }

    /* No point in having the border parameter, never false right now */
    protected HSSFCellStyle getHierarchyLevel1Style() {
        if (hierarchyLevel1Style == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.TURQUOISE.index);
            cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            //if (border) {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            /*          
            } else {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
            }
            */
            cs.setFont(font);
            cs.setWrapText(true);
            hierarchyLevel1Style = cs;
        }
        
        return hierarchyLevel1Style;
    }
    /* No point in having the border parameter, never false right now */
    protected HSSFCellStyle getHierarchyOtherStyle() {
        if (hierarchyOtherStyle == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            cs.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            //if (border) {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
                      cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            /*
            } else {
                      cs.setBorderBottom(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderLeft(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
                      cs.setBorderTop(HSSFCellStyle.BORDER_NONE);
            }
            */
            cs.setFont(font);
            cs.setWrapText(true);
            hierarchyOtherStyle = cs;
        }
        
        return hierarchyOtherStyle;
    }
    
    public HSSFCell createCenteredCell(String contents, HSSFCellStyle cellStyle, boolean borderRight, int rowSpan, int colSpan){
        row = getOrCreateRow();
        HSSFCell cell = this.getCell(row, cellStyle);
        cell.setCellValue(contents);
        makeColSpanAndRowSpan(1, rowSpan, borderRight);
        return cell;
    }
    
    public void makeColSpanAndRowSpan(int colSpan, int rowSpan, Boolean borderRight)
    {
        //System.out.format("making colspan (minX, minY, maxX, maxY) = (%d, %d, %d, %d)\n", colId.intValue(), rowId.intValue(), colId.intValue() + colSpan - 1, rowId.intValue() + rowSpan - 1);
        if (colSpan == 1 && rowSpan == 1)
            return;
        CellRangeAddress r = new CellRangeAddress(rowId.intValue(), rowId.intValue() + rowSpan - 1, colId.intValue(), colId.intValue() + colSpan - 1);
//      Region r=new Region(rowId.intValue(), colId.shortValue(), rowId.intValue() + rowSpan - 1, (short) (colId.shortValue() + colSpan - 1));
        HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, r, sheet, wb);
        HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, r, sheet, wb);
        if (borderRight)
            HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, r, sheet, wb);
        HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, r, sheet, wb);
        sheet.addMergedRegion(r);
        sheet.autoSizeColumn(colId.intValue());
        colId.inc(colSpan);
    }   
    
    public void makeColSpan(int size, Boolean border) {
        size--;
        if(size<0) size=0;
        CellRangeAddress r = new CellRangeAddress(rowId.intValue(), colId.intValue(), rowId.intValue(), colId.intValue() + size);
//      Region r=new Region(rowId.intValue(), colId.shortValue(),rowId.intValue(), (short) (colId.shortValue() + size));
        try {
            if (border){
                HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
            }else{
                HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try{
            sheet.addMergedRegion(r);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
//      try{
//          sheet.autoSizeColumn(r.getColumnFrom());
//      } catch(Exception e) {
//          logger.error(e);
//          e.printStackTrace();
//      }
    
//        if (this.autoSize) {
//            sheet.autoSizeColumn(r.getColumnFrom());
//        }
        colId.inc(++size);
    }

    /**
     * @deprecated Use {@link #makeRowSpan(int,boolean)} instead
     */
    public void makeRowSpan(int size) {
        makeRowSpan(size, true);
    }

    public void makeRowSpan(int size, boolean border) {
        CellRangeAddress r = new CellRangeAddress(rowId.intValue(), colId.intValue(), rowId.intValue() + size, colId.intValue());
//      Region r=new Region(rowId.intValue(), colId.shortValue(),
//              rowId.intValue() + size, colId.shortValue());
        try {
            if (border){
                HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
                HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
            }else{
                HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
                HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_NONE,r,sheet,wb);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        sheet.addMergedRegion(r);   
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
        
        this.regularStyle = xlsParent.getRegularStyle();
        this.amountStyle = xlsParent.getAmountStyle();
        this.highlightedStyle = xlsParent.getHighlightedStyle();
        this.hierarchyLevel1Style = xlsParent.getHierarchyLevel1Style();
        this.hierarchyOtherStyle = xlsParent.getHierarchyOtherStyle();
        this.amountHierarchyLevel1Style = xlsParent.getAmountHierarchyLevel1Style();
        this.amountHierarchyOtherStyle = xlsParent.getAmountHierarchyOtherStyle();
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
    
    public boolean isAutoSize()
    {
        return false;
    }
    
    public void setAutoSize(boolean autoSize)
    {
        // do nothing
    }

    /**
     * hack extracted from some places in order to at least not copy the mess
     * @param grd
     * @return
     */
    public String getDisplayedName(ReportData<?> grd){
        String modifiedName = (grd.getName() == null) ? "" : grd.getName();

        int pos = modifiedName.indexOf(':'); 
        if (pos >= 0)
            modifiedName = modifiedName.substring(pos + 1).trim();
                
        if (grd.getParent() != null && grd.getParent().getParent() == null)
            modifiedName = TranslatorWorker.translateText("TOTAL").toUpperCase();

        return modifiedName;
    }
    
    public HSSFRow getOrCreateRow(){
        row = sheet.getRow(rowId.shortValue());
        if (row == null)
            row = sheet.createRow(rowId.shortValue());
        return row;
    }
}
