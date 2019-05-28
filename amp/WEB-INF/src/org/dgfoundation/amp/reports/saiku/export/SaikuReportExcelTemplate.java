package org.dgfoundation.amp.reports.saiku.export;

import static org.apache.poi.ss.usermodel.ReadingOrder.LEFT_TO_RIGHT;
import static org.apache.poi.ss.usermodel.ReadingOrder.RIGHT_TO_LEFT;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.ReadingOrder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.digijava.kernel.util.SiteUtils;

/** Class containing the styles for the excel file. See {@link SaikuReportXlsxExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportExcelTemplate {
    
    private XSSFCellStyle headerCellStyle = null;
    private XSSFCellStyle totalCellStyle = null;
    private XSSFCellStyle totalNumberStyle = null;
    private XSSFCellStyle hierarchyStyle = null;
    private XSSFCellStyle headerCleanStyle = null;
    private XSSFCellStyle totalCleanStyle = null;
    private XSSFCellStyle numberStyle = null;
    private XSSFCellStyle settingsOptionStyle = null;
    private XSSFCellStyle settingsFilterStyle = null;
    private XSSFCellStyle readingOrderStyle = null;
    
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
     */
    private void initWorkbookStyles() {
    
        ReadingOrder readingOrder = SiteUtils.isEffectiveLangRTL() ? RIGHT_TO_LEFT : LEFT_TO_RIGHT;
        
        Font fontHeaderAndTotal = wb.createFont();
        fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
        Font fontBold = wb.createFont();
        fontBold.setBold(true);
        
        headerCellStyle = (XSSFCellStyle) wb.createCellStyle();
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setFont(fontHeaderAndTotal);

        headerCleanStyle = (XSSFCellStyle) wb.createCellStyle();
        headerCleanStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCleanStyle.setWrapText(true);
        headerCleanStyle.setFont(fontHeaderAndTotal);

        totalCellStyle = (XSSFCellStyle) wb.createCellStyle();
        totalCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        totalCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        totalCellStyle.setAlignment(HorizontalAlignment.CENTER);
        totalCellStyle.setFont(fontHeaderAndTotal);
        totalCellStyle.setWrapText(true);
        totalCellStyle.setReadingOrder(readingOrder);
        
        totalNumberStyle = (XSSFCellStyle) wb.createCellStyle();
        totalNumberStyle.cloneStyleFrom(totalCellStyle);
        totalNumberStyle.setAlignment(HorizontalAlignment.RIGHT);
        totalNumberStyle.setReadingOrder(readingOrder);

        // Important: DO NOT change this style (is used as a marker for total rows in plain export).
        totalCleanStyle = (XSSFCellStyle) wb.createCellStyle();
        totalCleanStyle.setAlignment(HorizontalAlignment.LEFT);
        totalCleanStyle.setFont(fontHeaderAndTotal);
        totalCleanStyle.setWrapText(true);
    
        XSSFCellStyle subTotalLvl1 = (XSSFCellStyle) wb.createCellStyle();
        subTotalLvl1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        subTotalLvl1.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        subTotalLvl1.setAlignment(HorizontalAlignment.RIGHT);
        subTotalLvl1.setWrapText(true);
        subTotalLvl1.setFont(fontHeaderAndTotal);
        subTotalLvl1.setReadingOrder(readingOrder);
        subTotals.put(0, subTotalLvl1);
    
        XSSFCellStyle subTotalLvl2 = (XSSFCellStyle) wb.createCellStyle();
        subTotalLvl2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        subTotalLvl2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        subTotalLvl2.setAlignment(HorizontalAlignment.RIGHT);
        subTotalLvl2.setWrapText(true);
        subTotalLvl2.setFont(fontHeaderAndTotal);
        subTotalLvl2.setReadingOrder(readingOrder);
        subTotals.put(1, subTotalLvl2);
    
        XSSFCellStyle subTotalLvl3 = (XSSFCellStyle) wb.createCellStyle();
        subTotalLvl3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        subTotalLvl3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subTotalLvl3.setAlignment(HorizontalAlignment.RIGHT);
        subTotalLvl3.setWrapText(true);
        subTotalLvl3.setFont(fontHeaderAndTotal);
        subTotalLvl3.setReadingOrder(readingOrder);
        subTotals.put(2, subTotalLvl3);

        hierarchyStyle = (XSSFCellStyle) wb.createCellStyle();
        hierarchyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        numberStyle = (XSSFCellStyle) wb.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setReadingOrder(readingOrder);

        settingsOptionStyle = (XSSFCellStyle) wb.createCellStyle();
        settingsOptionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        settingsOptionStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        settingsOptionStyle.setFont(fontBold);

        settingsFilterStyle = (XSSFCellStyle) wb.createCellStyle();
        
        readingOrderStyle = (XSSFCellStyle) wb.createCellStyle();
        readingOrderStyle.setReadingOrder(readingOrder);
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
    
    public CellStyle getReadingOrderStyle() {
        return readingOrderStyle;
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
