package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
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
	private static CellStyle styleHeaderClean = null;
	private static CellStyle styleTotalClean = null;
	private static CellStyle styleNumber = null;

	private static final int TYPE_STYLED = 0;
	private static final int TYPE_PLAIN = 1;

	private static final short cellHeight = 300;

	public static byte[] generateExcel(JsonBean jb, String type, int hierarchies, int columns) throws IOException {
		// Generate html table.
		String content = AMPJSConverter.convertToHtml(jb, type);
		// Parse the string.
		Document doc = Jsoup.parse(content);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Workbook wb = new XSSFWorkbook();
		Sheet mainSheet = wb.createSheet(TranslatorWorker.translateText("Formatted"));
		Sheet plainSheet = wb.createSheet(TranslatorWorker.translateText("Plain"));
		createStyles(wb);

		generateSheet(wb, mainSheet, doc, hierarchies, columns, TYPE_STYLED);
		generateSheet(wb, plainSheet, doc, hierarchies, columns, TYPE_PLAIN);

		wb.write(os);
		os.flush();
		os.close();
		return os.toByteArray();
	}

	private static void generateSheet(Workbook wb, Sheet sheet, Document doc, int hierarchies, int columns, int type) {
		// Process header.
		Element headerRows = doc.getElementsByTag("thead").first();
		int i = 0;
		int headers;
		int totalColNumber = 0;
		for (Element headerRowElement : headerRows.getElementsByTag("tr")) {
			Row row = sheet.createRow(i);
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
						if (type == TYPE_PLAIN) {
							// "Unmerge" these header columns.
							if (colSpan > 1) {
								for (int k = 1; k < colSpan; k++) {
									Cell auxCell = row.createCell(j + k);
									auxCell.setCellValue(cellContent);
									auxCell.setCellStyle(styleHeaderClean);
								}
								j += colSpan - 1;
							}
						} else {
							// If this cell is being grouped with other cells on its right we merge them.
							if (colSpan > 1) {
								for (int k = 0; k < colSpan; k++) {
									// Create the merged cells to avoid problems.
									Cell mergedCell = row.createCell(j + k);
									mergedCell.setCellStyle(styleHeader);
								}
								sheet.addMergedRegion(new CellRangeAddress(i, i, j, j + colSpan - 1));
								j += colSpan - 1;
							}
						}
					}
					cell.setCellValue(cellContent);
					if (type == TYPE_PLAIN) {
						cell.setCellStyle(styleHeaderClean);
					} else {
						cell.setCellStyle(styleHeader);
					}
				}
				j++;
				totalColNumber++;
			}
			i++;
		}
		headers = i;

		// Check special case when summarized report has only 1 column and 1 row for "report totals".
		String reportTotalsString = TranslatorWorker.translateText("Report Totals");
		// Create a map with the styles we will be cloning so we can add or change them (without breaking the WorkBook
		// with too many styles).
		Map<String, CellStyle> clonedStyles = new HashMap<String, CellStyle>();
		String clonedSufixAlignRight = "_align_right";
		// Process data.
		Element contentRows = doc.getElementsByTag("tbody").first();
		int totalRows = contentRows.getElementsByTag("tr").size() + i - 1;
		for (Element contentRowElement : contentRows.getElementsByTag("tr")) {
			Row row = sheet.createRow(i);
			CellStyle styleForCurrentRow = null;
			int j = 0;
			for (Element contentColElement : contentRowElement.getElementsByTag("td")) {
				boolean isNumber = false;
				Cell cell = row.createCell(j);
				String cellContent = ((Element) contentColElement).text();
				if (j >= columns && !reportTotalsString.equals(cellContent)) {
					isNumber = true;
					try {
						cell.setCellValue(new Double(cellContent));
					} catch (NumberFormatException e) {
						cell.setCellValue(new Double(0));
					}
				} else {
					isNumber = false;
					cell.setCellValue(cellContent);
				}

				if (i == totalRows) {
					// Style last total row.
					if (type == TYPE_STYLED) {
						styleForCurrentRow = styleTotal;
					} else {
						styleForCurrentRow = styleTotalClean;
					}
				} else if (contentColElement.hasClass("total")) {
					if (type == TYPE_STYLED) {
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
				}
				if (styleForCurrentRow != null) {
					cell.setCellStyle(styleForCurrentRow);
				}
				if (isNumber) {
					// If this is a number then we need to keep the current format style (if any) and apply some extra
					// properties like 'align', if we create a new style for each cell that will break POI so we will
					// use a map to cache the few new styles we will create and use.
					if (styleForCurrentRow != null) {
						String key = styleForCurrentRow.toString() + clonedSufixAlignRight;
						if (clonedStyles.containsKey(key)) {
							CellStyle auxStyle = clonedStyles.get(key);
							cell.setCellStyle(auxStyle);
						} else {
							CellStyle auxStyle = wb.createCellStyle();
							auxStyle.cloneStyleFrom(cell.getCellStyle());
							auxStyle.setAlignment(CellStyle.ALIGN_RIGHT);
							cell.setCellStyle(auxStyle);
							clonedStyles.put(key, auxStyle);
						}
					} else {
						// Use a predefined basic number style with the align we need (and more properties if we need in
						// the future).
						cell.setCellStyle(styleNumber);
					}
				}
				j++;
			}
			row.setHeight(cellHeight);
			i++;
		}

		// Postprocess according to sheet type.
		switch (type) {
		case TYPE_STYLED:
			mergeHierarchyRows(sheet, hierarchies, headers);
			break;
		case TYPE_PLAIN:
			refillHierarchyRows(sheet, hierarchies, headers);
			deleteHierarchyTotalRows(sheet, hierarchies, headers, columns);
			break;
		}

		for (int l = 0; l < totalColNumber; l++) {
			sheet.autoSizeColumn(l, true);
		}
	}

	/**
	 * Delete hierarchy total rows (useful for plain excel export).
	 * 
	 * @param sheet
	 * @param hierarchies
	 * @param headers
	 */
	private static void deleteHierarchyTotalRows(Sheet sheet, int hierarchies, int headers, int columns) {
		if (sheet.getRow(0) != null) {
			int totalRows = sheet.getPhysicalNumberOfRows();
			if (columns > hierarchies) {
				// Iterate columns from right to left, then rows from bottom to top.
				// Notice 'j = hierarchies' instead of 'j = hierarchies - 1' because we start on the first non
				// hierarchical
				// column to get the last hierarchy empty total cells.
				for (int j = hierarchies; j > 0; j--) {
					for (int i = sheet.getPhysicalNumberOfRows() - 2; i >= headers - 1; i--) {
						Row row = sheet.getRow(i);
						if (row != null) {
							if (row.getCell(j).getStringCellValue().isEmpty()) {
								sheet.removeRow(row);
							}
						}
					}
				}
				// Now shift rows to fill the gaps created by removing columns with POI.
				int shift = 0;
				for (int i = headers; i < totalRows; i++) {
					Row row = sheet.getRow(i);
					// row is null when was deleted by POI, but still exists in the sheet.
					if (row == null || row.getCell(0).getStringCellValue().isEmpty()) {
						shift++;
					} else {
						if (shift > 0) {
							// Move up this row 'shift' positions and restart the loop.
							sheet.shiftRows(i, i, -1 * shift);
							i = headers;
							shift = 0;
						}
					}
				}
			}
		}
	}

	/**
	 * Postprocess the sheet by filling empty hierarchy cells with the right value.
	 * 
	 * @param sheet
	 * @param hierarchies
	 * @param headers
	 */
	private static void refillHierarchyRows(Sheet sheet, int hierarchies, int headers) {
		if (sheet.getRow(0) != null) {
			String prevCellValue = null;
			// Iterate columns then rows.
			for (int j = 0; j < hierarchies; j++) {
				for (int i = headers; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
					if (prevCellValue == null) {
						// Beginning a hierarchy.
						prevCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
					} else {
						String currentCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
						if (currentCellValue.isEmpty()) {
							// Empty cell means we are in the same hierarchy.
							sheet.getRow(i).getCell(j).setCellValue(prevCellValue);
						} else {
							// We reached the end of the hierarchy (its the total row).
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
			// Iterate columns then rows.
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
		// fontHeaderAndTotal.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleHeader = wb.createCellStyle();
		styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleHeader.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setWrapText(true);
		styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		styleHeader.setFont(fontHeaderAndTotal);

		styleHeaderClean = wb.createCellStyle();
		styleHeaderClean.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeaderClean.setWrapText(true);
		styleHeaderClean.setFont(fontHeaderAndTotal);

		styleTotal = wb.createCellStyle();
		styleTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleTotal.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleTotal.setAlignment(CellStyle.ALIGN_CENTER);
		styleTotal.setFont(fontHeaderAndTotal);
		styleTotal.setWrapText(true);

		styleTotalClean = wb.createCellStyle();
		styleTotalClean.setAlignment(CellStyle.ALIGN_LEFT);
		styleTotalClean.setFont(fontHeaderAndTotal);
		styleTotalClean.setWrapText(true);

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

		styleNumber = wb.createCellStyle();
		styleNumber.setAlignment(CellStyle.ALIGN_RIGHT);
	}
}