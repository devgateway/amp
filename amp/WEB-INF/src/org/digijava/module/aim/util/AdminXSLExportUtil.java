package org.digijava.module.aim.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class AdminXSLExportUtil {
	public final static char BULLETCHAR = '\u2022';
	public final static char NEWLINECHAR = '\n';
	
	public static HSSFCellStyle  createTitleStyle(HSSFWorkbook wb){
		HSSFCellStyle titleCS = wb.createCellStyle();
		wb.createCellStyle();
		titleCS.setWrapText(true);
		titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setFontName(HSSFFont.FONT_ARIAL);
		fontHeader.setFontHeightInPoints((short) 10);
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCS.setFont(fontHeader);
		titleCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return titleCS;
	}
	public static HSSFCellStyle  createOrdinaryStyle(HSSFWorkbook wb){
		HSSFCellStyle  cs = wb.createCellStyle();
		cs.setWrapText(true);
		cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return cs;
	}

}
