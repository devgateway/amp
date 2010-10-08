package org.digijava.module.mondrian.serverlet;

/**
 * @author Diego Dimunzio
 * 
 */

import java.text.ParseException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

public class ExporterHelper {

	protected HSSFWorkbook wb;
	protected HSSFCell cell = null;

	public ExporterHelper(HSSFWorkbook wb) {
		super();
		this.wb = wb;
	}

	public void addCell(String str, int colid, HSSFRow row,String style) {
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
		if (style.equalsIgnoreCase("even")){
			cell.setCellStyle(getCellSteven());
		}else if (style.equalsIgnoreCase("odd")){
			cell.setCellStyle(getCellStOdd());
		}else{
			cell.setCellStyle(getCellSt());
		}
	}

	public void addCaption(String str, int colid,HSSFRow row,String style) {
		HSSFCell cell;
		cell = row.createCell((short) colid);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(str);
		
		if (style.equalsIgnoreCase("row-heading-even")){
			cell.setCellStyle(getCellSteven());
		}else if (style.equalsIgnoreCase("row-heading-odd")){
			cell.setCellStyle(getCellStOdd());
		}else{
			cell.setCellStyle(getCellSt());
		}
		
	}

	public void addMerge(int rowfrom, int colfrom, int rowto, int colto) {
		Region r = new Region(rowfrom, (short) colfrom, rowto, (short) colto);
		wb.getSheet("data").addMergedRegion(r);
		HSSFSheet sheet = wb.getSheet("data");
		try {
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,r,sheet,wb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	protected HSSFCellStyle getCellStOdd() {
		HSSFFont fdataitem = wb.createFont();
		fdataitem.setFontName("Arial Unicode MS");
		fdataitem.setFontHeightInPoints((short) 8);
		fdataitem.setBoldweight(fdataitem.BOLDWEIGHT_NORMAL);
		HSSFCellStyle dataitem = wb.createCellStyle();
		dataitem.setFont(fdataitem);
		dataitem.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		dataitem.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);;
		dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return dataitem;
	}
	
	protected HSSFCellStyle getCellSteven() {
		HSSFFont fdataitem = wb.createFont();
		fdataitem.setFontName("Arial Unicode MS");
		fdataitem.setFontHeightInPoints((short) 8);
		fdataitem.setBoldweight(fdataitem.BOLDWEIGHT_NORMAL);
		HSSFCellStyle dataitem = wb.createCellStyle();
		dataitem.setFont(fdataitem);
		dataitem.setFillForegroundColor(HSSFColor.WHITE.index);
		dataitem.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
		dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);	
		return dataitem;
	}
	
}
