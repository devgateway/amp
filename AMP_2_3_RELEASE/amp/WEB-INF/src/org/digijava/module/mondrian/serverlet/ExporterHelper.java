package org.digijava.module.mondrian.serverlet;

/**
 * @author Diego Dimunzio
 * 
 */

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang.exception.NestableException;
import org.apache.ecs.xhtml.font;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.digijava.module.aim.helper.FormatHelper;

import com.lowagie.text.Font;

public class ExporterHelper {

	protected HSSFWorkbook wb;
	protected HSSFCell cell = null;

	public ExporterHelper(HSSFWorkbook wb) {
		super();
		this.wb = wb;
	}
	
	public void addCell(String str, int colid, HSSFRow row) {
		HSSFCell cell;
		cell = row.createCell((short) colid);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		java.text.DecimalFormat df = new java.text.DecimalFormat("###,###.###");
		double val = Double.parseDouble(str);
		try {
			val = df.parse(df.format(val)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cell.setCellValue(val);
		cell.setCellStyle(getCellSt()); 
	}
	
	public void addCellBlue(String str, int colid, HSSFRow row) {
		HSSFCell cell;
		cell = row.createCell((short) colid);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		
		cell.setCellValue(str);
		cell.setCellStyle(getCellBlueSt()); 
	}
	
	public void addCaption(String str, int colid,HSSFRow row) {
		HSSFCell cell;
		cell = row.createCell((short) colid);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(str);
		cell.setCellStyle(getRowHeadingSt());
	}

	public void addMerge(int rowfrom, int colfrom, int rowto, int colto) {
		Region r = new Region(rowfrom, (short) colfrom, rowto, (short) colto);
		wb.getSheet("data").addMergedRegion(r);
		HSSFSheet sheet = wb.getSheet("data");
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
		
	}
	
	public void addMergenoBorder(int rowfrom, int colfrom, int rowto, int colto) {
		Region r = new Region(rowfrom, (short) colfrom, rowto, (short) colto);
		wb.getSheet("data").addMergedRegion(r);
		HSSFSheet sheet = wb.getSheet("data");
		
	}
	
	protected HSSFCellStyle getRowHeadingSt() {
		HSSFFont frowheading = wb.createFont();
		frowheading.setFontName("Arial Unicode MS");
		frowheading.setFontHeightInPoints((short) 8);
		frowheading.setBoldweight(frowheading.BOLDWEIGHT_BOLD);
		HSSFCellStyle rowheading = wb.createCellStyle();
		rowheading.setFont(frowheading);
		rowheading.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderRight(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderTop(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		rowheading.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		return rowheading;
	}

	protected HSSFCellStyle getCellSt() {
		HSSFFont fdataitem = wb.createFont();
		fdataitem.setFontName("Arial Unicode MS");
		fdataitem.setFontHeightInPoints((short) 8);
		fdataitem.setBoldweight(fdataitem.BOLDWEIGHT_NORMAL);
		HSSFCellStyle dataitem = wb.createCellStyle();
		dataitem.setFont(fdataitem);
		dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return dataitem;
	}
	
	protected HSSFCellStyle getCellBlueSt() {
		HSSFFont fdataitem = wb.createFont();
		fdataitem.setFontName("Arial Unicode MS");
		fdataitem.setFontHeightInPoints((short) 8);
		fdataitem.setColor(HSSFColor.BLUE.index);
		HSSFCellStyle dataitem = wb.createCellStyle();
		dataitem.setFont(fdataitem);
		dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return dataitem;
	}
}
