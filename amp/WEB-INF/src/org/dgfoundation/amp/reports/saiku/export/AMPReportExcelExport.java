package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AMPReportExcelExport {

	private static CellStyle styleHeader = null;
	private static CellStyle styleSubTotalLvl1 = null;
	private static CellStyle styleSubTotalLvl2 = null;
	private static CellStyle styleSubTotalLvl3 = null;
	private static CellStyle styleTotal = null;
	private static CellStyle styleHierarchy = null;

	public static byte[] generateExcel(JsonBean jb, String type, int hierarchies) throws IOException {
		// Generate html table.
		String content = AMPJSConverter.convertToHtml(jb, type);
		// Parse the string.
		Document doc = Jsoup.parse(content);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Workbook wb = new XSSFWorkbook();
		Sheet mainSheet = wb.createSheet();
		createStyles(wb);

		// Process header.
		Element headerRows = doc.getElementsByTag("thead").first();
		int i = 0;
		int headers;
		int totalColNumber = 0;
		for (Element headerRowElement : headerRows.getElementsByTag("tr")) {
			Row row = mainSheet.createRow(i);
			int j = 0;
			int colSpan;
			for (Element headerColElement : headerRowElement.getElementsByTag("th")) {
				Cell cell = row.createCell(j);
				String cellContent = "";
				if (headerColElement.hasClass("all_null")) {
					// Empty header cell, do nothing.
				} else if (headerColElement.hasClass("col")) {
					// This is a header cell with a <div> inside.
					cellContent = ((Element) headerColElement.getElementsByTag("div").toArray()[0]).text();
					if (headerColElement.hasAttr("colspan")) {
						colSpan = Integer.valueOf(headerColElement.attr("colspan").toString()).intValue();
						// If this cell is being grouped with other cells on its right we merge them.
						if (colSpan > 1) {
							for (int k = 0; k < colSpan; k++) {
								// Create the merged cells to avoid problems.
								Cell mergedCell = row.createCell(j + k);
								mergedCell.setCellStyle(styleHeader);
							}
							mainSheet.addMergedRegion(new CellRangeAddress(i, i, j, j + colSpan - 1));
							j += colSpan - 1;
						}
					}
					cell.setCellValue(cellContent);
					cell.setCellStyle(styleHeader);
				}
				j++;
				totalColNumber++;
			}
			i++;
		}
		headers = i;

		// Process data.
		Element contentRows = doc.getElementsByTag("tbody").first();
		int totalRows = contentRows.getElementsByTag("tr").size() + i - 1;
		for (Element contentRowElement : contentRows.getElementsByTag("tr")) {
			Row row = mainSheet.createRow(i);
			CellStyle styleForCurrentRow = null;
			int j = 0;
			for (Element contentColElement : contentRowElement.getElementsByTag("td")) {
				Cell cell = row.createCell(j);
				String cellContent = ((Element) contentColElement).text();
				cell.setCellValue(cellContent);
				if (i == totalRows) {
					// Style last total row.
					cell.setCellStyle(styleTotal);
				} else if (contentColElement.hasClass("total")) {
					// Start applying the subtotal style in the right column (not the first).
					if (styleForCurrentRow == null && !cellContent.equals("")) {
						switch (j) {
						case 0:
							styleForCurrentRow = styleSubTotalLvl1;
							break;
						case 1:
							styleForCurrentRow = styleSubTotalLvl2;
							break;
						case 2:
							styleForCurrentRow = styleSubTotalLvl3;
							break;
						}
					}
				}
				if (styleForCurrentRow != null) {
					cell.setCellStyle(styleForCurrentRow);
				}
				j++;
			}
			i++;
		}

		mergeHierarchyRows(mainSheet, hierarchies, headers);

		for (int l = 0; l < totalColNumber; l++) {
			mainSheet.autoSizeColumn(l, true);
		}

		wb.write(os);
		os.flush();
		os.close();
		return os.toByteArray();
	}

	/**
	 * Postprocess the sheet by merging vertically all empty hierarchy cells.
	 * 
	 * @param sheet
	 * @param hierarchies
	 * @param headers
	 */
	private static void mergeHierarchyRows(Sheet sheet, int hierarchies, int headers) {
		if (sheet.getRow(0) != null) {
			String prevCellValue = null;
			int group = 0;
			int groupStart = 0;
			for (int j = 0; j < hierarchies; j++) {
				for (int i = headers; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
					if (prevCellValue == null) {
						// Beginning a hierarchy.
						prevCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
						group = 0;
						groupStart = i;
					} else {
						String currentCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
						if (currentCellValue.isEmpty()) {
							// Empty cell means we are in the same hierarchy.
							group++;
						} else {
							// We reached the end of the hierarchy (its the total row).
							if (group > 0) {
								sheet.addMergedRegion(new CellRangeAddress(groupStart, groupStart + group, j, j));
								sheet.getRow(groupStart).getCell(j).setCellStyle(styleHierarchy);
							}
							prevCellValue = null;
							if (j > 0) {
								// If this is not the first column then we need to check if below this 'total' row there
								// are 1 or more rows that are also the 'total' for a higher hierarchy (ie: sector ->
								// sub sector -> sub sub sector, etc).
								int mustSkipRows = 0;
								for (int k = 1; k < hierarchies; k++) {
									if (sheet.getRow(i + k).getCell(j).getStringCellValue().isEmpty()) {
										mustSkipRows++;
									} else {
										break;
									}
								}
								i += mustSkipRows;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Define all styles we will use in the Workbook.
	 * 
	 * @param wb
	 */
	private static void createStyles(Workbook wb) {
		Font fontHeaderAndTotal = wb.createFont();
		fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
		fontHeaderAndTotal.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleHeader = wb.createCellStyle();
		styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleHeader.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setWrapText(true);
		styleHeader.setFont(fontHeaderAndTotal);

		styleTotal = wb.createCellStyle();
		styleTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleTotal.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleTotal.setAlignment(CellStyle.ALIGN_CENTER);
		styleTotal.setFont(fontHeaderAndTotal);
		styleTotal.setWrapText(true);

		styleSubTotalLvl1 = wb.createCellStyle();
		styleSubTotalLvl1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleSubTotalLvl1.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		styleSubTotalLvl1.setAlignment(CellStyle.ALIGN_LEFT);
		styleSubTotalLvl1.setWrapText(true);
		styleSubTotalLvl1.setFont(fontHeaderAndTotal);

		styleSubTotalLvl2 = wb.createCellStyle();
		styleSubTotalLvl2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleSubTotalLvl2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		styleSubTotalLvl2.setAlignment(CellStyle.ALIGN_LEFT);
		styleSubTotalLvl2.setWrapText(true);
		styleSubTotalLvl2.setFont(fontHeaderAndTotal);

		styleSubTotalLvl3 = wb.createCellStyle();
		styleSubTotalLvl3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleSubTotalLvl3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleSubTotalLvl3.setAlignment(CellStyle.ALIGN_LEFT);
		styleSubTotalLvl3.setWrapText(true);
		styleSubTotalLvl3.setFont(fontHeaderAndTotal);

		styleHierarchy = wb.createCellStyle();
		styleHierarchy.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	}
}