package org.dgfoundation.amp.gpi.reports.export.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

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
     */
    private void initWorkbookStyles() {
        
        Font fontDefault = wb.createFont();
        fontDefault.setFontHeightInPoints((short)10);
        
        Font fontHeaderAndTotal = wb.createFont();
        fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
        fontHeaderAndTotal.setFontHeightInPoints((short)10);
        
        Font fontBold = wb.createFont();
        fontBold.setBold(true);
        fontBold.setFontHeightInPoints((short)10);
        
        Font fontSummary = wb.createFont();
        fontSummary.setBold(true);
        fontSummary.setFontHeightInPoints((short)10);

        headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setWrapText(true);
        
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setFont(fontHeaderAndTotal);
        
        summaryCellStyle = wb.createCellStyle();
        summaryCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        summaryCellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        summaryCellStyle.setAlignment(HorizontalAlignment.CENTER);
        summaryCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        summaryCellStyle.setWrapText(true);
        summaryCellStyle.setBorderTop(BorderStyle.THIN);
        summaryCellStyle.setBorderBottom(BorderStyle.THIN);
        summaryCellStyle.setBorderRight(BorderStyle.THIN);
        summaryCellStyle.setBorderLeft(BorderStyle.THIN);
        summaryCellStyle.setFont(fontSummary);

        headerCleanStyle = wb.createCellStyle();
        headerCleanStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCleanStyle.setWrapText(true);
        headerCleanStyle.setFont(fontHeaderAndTotal);

        hierarchyStyle = wb.createCellStyle();
        hierarchyStyle.setAlignment(HorizontalAlignment.CENTER);
        hierarchyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        hierarchyStyle.setFont(fontDefault);

        numberStyle = wb.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberStyle.setFont(fontDefault);
        
        centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centerStyle.setFont(fontDefault);
        
        wrappedStyle = wb.createCellStyle();
        wrappedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        wrappedStyle.setWrapText(true);
        wrappedStyle.setFont(fontDefault);
        
        defaultStyle = wb.createCellStyle();
        defaultStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        defaultStyle.setFont(fontDefault);

        settingsOptionStyle = wb.createCellStyle();
        settingsOptionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
    
    
    public static void fillHeaderRegionWithBorder(Sheet sheet, CellRangeAddress headerRegion) {
        RegionUtil.setBorderTop(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, headerRegion, sheet);
    }
}
