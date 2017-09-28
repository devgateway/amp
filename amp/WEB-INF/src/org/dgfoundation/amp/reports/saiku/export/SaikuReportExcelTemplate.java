package org.dgfoundation.amp.reports.saiku.export;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/** Class containing the styles for the excel file. See {@link SaikuReportXlsxExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportExcelTemplate {
    
    private CellStyle headerCellStyle = null;
    private CellStyle totalCellStyle = null;
    private CellStyle totalNumberStyle = null;
    private CellStyle hierarchyStyle = null;
    private CellStyle headerCleanStyle = null;
    private CellStyle totalCleanStyle = null;
    private CellStyle numberStyle = null;
    private CellStyle settingsOptionStyle = null;
    private CellStyle settingsFilterStyle = null;
    
    private Map<Integer, CellStyle> subTotals = new HashMap<Integer, CellStyle>();
    
    private final short cellHeight = 300;
    private final float charWidth = 300;
    private final int maxColumnWidth = 65280; // 255 * 256
    private final int defaultColWidth = 25;
    
    private Workbook wb;
    
    public SaikuReportExcelTemplate(Workbook wb) {
        this.wb = wb;
        initWorkbookStyles();
    }
    /**
     * Define all styles used in the report workbook.
     * 
     * @param wb
     */
    private void initWorkbookStyles() {
        
        Font fontHeaderAndTotal = wb.createFont();
        fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
        Font fontBold = wb.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerCellStyle.setFont(fontHeaderAndTotal);

        headerCleanStyle = wb.createCellStyle();
        headerCleanStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCleanStyle.setWrapText(true);
        headerCleanStyle.setFont(fontHeaderAndTotal);

        totalCellStyle = wb.createCellStyle();
        totalCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        totalCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        totalCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        totalCellStyle.setFont(fontHeaderAndTotal);
        totalCellStyle.setWrapText(true);
        
        totalNumberStyle = wb.createCellStyle();
        totalNumberStyle.cloneStyleFrom(totalCellStyle);
        totalNumberStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        // Important: DO NOT change this style (is used as a marker for total rows in plain export).
        totalCleanStyle = wb.createCellStyle();
        totalCleanStyle.setAlignment(CellStyle.ALIGN_LEFT);
        totalCleanStyle.setFont(fontHeaderAndTotal);
        totalCleanStyle.setWrapText(true);

        CellStyle subTotalLvl1 = wb.createCellStyle();
        subTotalLvl1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        subTotalLvl1.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        subTotalLvl1.setAlignment(CellStyle.ALIGN_RIGHT);
        subTotalLvl1.setWrapText(true);
        subTotalLvl1.setFont(fontHeaderAndTotal);
        subTotals.put(0, subTotalLvl1);

        CellStyle subTotalLvl2 = wb.createCellStyle();
        subTotalLvl2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        subTotalLvl2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        subTotalLvl2.setAlignment(CellStyle.ALIGN_RIGHT);
        subTotalLvl2.setWrapText(true);
        subTotalLvl2.setFont(fontHeaderAndTotal);
        subTotals.put(1, subTotalLvl2);

        CellStyle subTotalLvl3 = wb.createCellStyle();
        subTotalLvl3.setFillPattern(CellStyle.SOLID_FOREGROUND);
        subTotalLvl3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subTotalLvl3.setAlignment(CellStyle.ALIGN_RIGHT);
        subTotalLvl3.setWrapText(true);
        subTotalLvl3.setFont(fontHeaderAndTotal);
        subTotals.put(2, subTotalLvl3);

        hierarchyStyle = wb.createCellStyle();
        hierarchyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        numberStyle = wb.createCellStyle();
        numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        settingsOptionStyle = wb.createCellStyle();
        settingsOptionStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        settingsOptionStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        settingsOptionStyle.setFont(fontBold);

        settingsFilterStyle = wb.createCellStyle();
    }
    
    public CellStyle getHeaderCellStyle() {
        return headerCellStyle;
    }
    
    public CellStyle getSubtotalStyle(int level) {
        return subTotals.get(level);
    }
    
    public CellStyle getTotalStyle() {
        return totalCellStyle;
    }
    
    public CellStyle getNumberStyle() {
        return numberStyle;
    }
    
    public CellStyle getHierarchyStyle() {
        return hierarchyStyle;
    }
    
    public CellStyle getTotalNumberStyle() {
        return totalNumberStyle;
    }
    
    public CellStyle getFilterSettingsStyle() {
        return settingsFilterStyle;
    }
    
    public CellStyle getOptionSettingsStyle() {
        return settingsOptionStyle;
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
