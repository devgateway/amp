package org.digijava.module.aim.util;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.BROWN;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.CellRangeAddress;

public class AdminXSLExportUtil {
    public final static char BULLETCHAR = '\u2022';
    public final static char NEWLINECHAR = '\n';
    
    public static HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
        HSSFCellStyle titleCS = wb.createCellStyle();
        wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(BROWN.getIndex());
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 10);
        fontHeader.setBold(true);
        titleCS.setAlignment(HorizontalAlignment.CENTER);
        titleCS.setFont(fontHeader);
        titleCS.setBorderBottom(BorderStyle.THIN);
        titleCS.setBorderLeft(BorderStyle.THIN);
        titleCS.setBorderRight(BorderStyle.THIN);
        titleCS.setBorderTop(BorderStyle.THIN);
        
        return titleCS;
    }
    
    public static HSSFCellStyle createOrdinaryStyle(HSSFWorkbook wb) {
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        cs.setVerticalAlignment(VerticalAlignment.TOP);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        
        return cs;
    }
    
    public static void applyingStylesToRegion(CellRangeAddress region, HSSFSheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }
    
}
