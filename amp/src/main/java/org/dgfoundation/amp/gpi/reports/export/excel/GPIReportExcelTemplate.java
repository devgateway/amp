package org.dgfoundation.amp.gpi.reports.export.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.util.HashMap;
import java.util.Map;

/** 
 * Class containing the styles for the excel file. See {@link GPIReportXlsxExporter}
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReportExcelTemplate {
    
    private CellStyle summaryCellStyle = null;
    private CellStyle headerCellStyle = null;
    private CellStyle hierarchyStyle = null;
    private CellStyle headerCleanStyle = null;
    private CellStyle numberStyle = null;
    private CellStyle centerStyle = null;
    private CellStyle wrappedStyle = null;
    private CellStyle defaultStyle = null;
    private CellStyle settingsOptionStyle = null;
    private CellStyle settingsFilterStyle = null;
    
    private Map<Integer, CellStyle> subTotals = new HashMap<Integer, CellStyle>();
    
    private final short cellHeight = 230;
    private final float charWidth = 200;
    private final int maxColumnWidth = 20480; // 80 * 256
    private final int defaultColWidth = 20;
    
    private Workbook wb;
    
    public GPIReportExcelTemplate(Workbook wb) {
        this.wb = wb;
        initWorkbookStyles();
    }
    /**
     * Define all styles used in the report workbook.
     * 
     * @param wb
     */
    private void initWorkbookStyles() {
        
        Font fontDefault = wb.createFont();
        fontDefault.setFontHeightInPoints((short)10);
        
        Font fontHeaderAndTotal = wb.createFont();
        fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
        fontHeaderAndTotal.setFontHeightInPoints((short)10);
        
        Font fontBold = wb.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontBold.setFontHeightInPoints((short)10);
        
        Font fontSummary = wb.createFont();
        fontSummary.setBold(true);
        fontSummary.setFontHeightInPoints((short)10);

        headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //headerCellStyle.setFillForegroundColor(new XSSFColor(Color.decode("#4F81BD")).getIndexed());
        headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerCellStyle.setWrapText(true);
        
        headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerCellStyle.setFont(fontHeaderAndTotal);
        
        summaryCellStyle = wb.createCellStyle();
        summaryCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        summaryCellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        summaryCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        summaryCellStyle.setWrapText(true);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFont(fontSummary);

        headerCleanStyle = wb.createCellStyle();
        headerCleanStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCleanStyle.setWrapText(true);
        headerCleanStyle.setFont(fontHeaderAndTotal);

        hierarchyStyle = wb.createCellStyle();
        hierarchyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hierarchyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        hierarchyStyle.setFont(fontDefault);

        numberStyle = wb.createCellStyle();
        numberStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numberStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        numberStyle.setFont(fontDefault);
        
        centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        centerStyle.setFont(fontDefault);
        
        wrappedStyle = wb.createCellStyle();
        wrappedStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        wrappedStyle.setWrapText(true);
        wrappedStyle.setFont(fontDefault);
        
        defaultStyle = wb.createCellStyle();
        defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        defaultStyle.setFont(fontDefault);

        settingsOptionStyle = wb.createCellStyle();
        settingsOptionStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        settingsOptionStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        settingsOptionStyle.setFont(fontBold);

        settingsFilterStyle = wb.createCellStyle();
        settingsFilterStyle.setFont(fontDefault);
    }
    
    public CellStyle getHeaderCellStyle() {
        return headerCellStyle;
    }
    
    public CellStyle getSummaryCellStyle() {
        return summaryCellStyle;
    }
    
    public CellStyle getSubtotalStyle(int level) {
        return subTotals.get(level);
    }
    
    public CellStyle getNumberStyle() {
        return numberStyle;
    }
    
    public CellStyle getCenterStyle() {
        return centerStyle;
    }
    
    public CellStyle getHierarchyStyle() {
        return hierarchyStyle;
    }
    
    public CellStyle getFilterSettingsStyle() {
        return settingsFilterStyle;
    }
    
    public CellStyle getOptionSettingsStyle() {
        return settingsOptionStyle;
    }
    
    public CellStyle getWrappedStyle() {
        return wrappedStyle;
    }
    
    public CellStyle getDefaultStyle() {
        return defaultStyle;
    }
    
    public int getCellHeight() {
        return cellHeight;
    }
    
    public float getCharWidth() {
        return charWidth;
    }
    
    public float getDefaultColumnWidth() {
        return defaultColWidth;
    }
    
    public int getMaxColumnWidth() {
        return maxColumnWidth;
    }
    
    
    public static void fillHeaderRegionWithBorder(Workbook workbook, Sheet sheet, CellRangeAddress headerRegion) {
        RegionUtil.setBorderTop((int) CellStyle.BORDER_THIN, headerRegion, sheet, workbook);
        RegionUtil.setBorderLeft((int) CellStyle.BORDER_THIN, headerRegion, sheet, workbook);
        RegionUtil.setBorderRight((int) CellStyle.BORDER_THIN, headerRegion, sheet, workbook);
        RegionUtil.setBorderBottom((int) CellStyle.BORDER_THIN, headerRegion, sheet, workbook);
    }
}
