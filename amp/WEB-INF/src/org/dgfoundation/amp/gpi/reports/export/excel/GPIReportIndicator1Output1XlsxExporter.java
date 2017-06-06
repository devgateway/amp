package org.dgfoundation.amp.gpi.reports.export.excel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator1Output1XlsxExporter extends GPIReportXlsxExporter {
	
	public final Map<String, String> HEADER_LABELS = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(GPIReportConstants.COLUMN_YEAR, "Year");
			put(ColumnConstants.DONOR_AGENCY, "Provider Name");
			put(ColumnConstants.PROJECT_TITLE, "Project Title");
			put(GPIReportConstants.GPI_1_Q1, String.format("%s\n%s", GPIReportConstants.GPI_1_Q1, "Project Amount"));
			put(GPIReportConstants.GPI_1_Q2, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q2, "Approval date, (month/year)"));
			put(GPIReportConstants.GPI_1_Q3, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q3, "Type of Intervention"));
			put(GPIReportConstants.GPI_1_Q4, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q4, "Implementing Entity"));
			put(GPIReportConstants.GPI_1_Q5, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q5, "What is the sector that the intervention targets?"));
			put(ColumnConstants.GPI_1_Q6, String.format("%s\n%s", ColumnConstants.GPI_1_Q6, 
					"The objective is drawn from government result frame work's or other splanning document"
					+ "\nYes=1, N0=0"));
			put(ColumnConstants.GPI_1_Q6_DESCRIPTION, String.format("%s\n%s", ColumnConstants.GPI_1_Q6, 
					"The objective is drawn from government result frame work's or other planning document"));
			put(ColumnConstants.GPI_1_Q7, String.format("%s\n%s", ColumnConstants.GPI_1_Q7, 
					"Total number of outcome indicators included in the projects result framework"));
			put(ColumnConstants.GPI_1_Q8, String.format("%s\n%s", ColumnConstants.GPI_1_Q8, 
					"Number of outcome indicators drawn from existing Gov’s result framework "
					+ "and/or other planning documents"));
			put(ColumnConstants.GPI_1_Q9, String.format("%s\n%s", ColumnConstants.GPI_1_Q9, 
					"Number of outcome indication to be tracked using Gov’t ongoing statistical data source "
					+ "or M&E system"));
			put(ColumnConstants.GPI_1_Q10, String.format("%s\n%s", ColumnConstants.GPI_1_Q10, 
					"The project plans a final evaluative\nYes=1, N0=0"));
			put(ColumnConstants.GPI_1_Q10_DESCRIPTION, String.format("%s\n%s", ColumnConstants.GPI_1_Q10, 
					"To what extent will the Gov't participate in carrying out the final evaluation?"
					+ "\n(if there is one planned)"));
			put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT, "Extent of use of country owned result "
					+ "framework or similar planning document\nCalculation = Q8/Q7");
			put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES, "Extent of use of Gov’t sources and M&E systems"
					+ " to track project progress\nCalculation = Q9/Q7");
			put(GPIReportConstants.GPI_1_Q11, "Supportive Documents");
			put(GPIReportConstants.GPI_1_Q11a, "Electronic link to project document");
			put(GPIReportConstants.GPI_1_Q11b, "Electronic link to gov. planning doc. or results framework "
					+ "used for project design");
			put(GPIReportConstants.GPI_1_Q11c, "Electronic link to gov. existing data source, statistical database "
					+ "or M&E system that will be used to track project progress");
			
		}
	});
	
	public GPIReportIndicator1Output1XlsxExporter() {
		reportSheetName = "Indicator 1 Output 1";
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param report
	 */
	protected void renderReportTableHeader(Workbook wb, Sheet sheet, GPIReport report) {
		Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();
		int firstCol = 0;
		int firstRow = initHeaderRowOffset;
		Row row = sheet.createRow(firstRow);

		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.COLUMN_YEAR), 
				firstRow, firstRow + 1, firstCol, firstCol));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.DONOR_AGENCY), 
				firstRow, firstRow + 1, firstCol + 1, firstCol + 1));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.PROJECT_TITLE), 
				firstRow, firstRow + 1, firstCol + 2, firstCol + 2));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q1), 
				firstRow, firstRow + 1, firstCol + 3, firstCol + 3));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q2), 
				firstRow, firstRow + 1, firstCol + 4, firstCol + 4));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q3), 
				firstRow, firstRow + 1, firstCol + 5, firstCol + 5));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q4), 
				firstRow, firstRow + 1, firstCol + 6, firstCol + 6));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q5), 
				firstRow, firstRow + 1, firstCol + 7, firstCol + 9));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q6), 
				firstRow, firstRow + 1, firstCol + 10, firstCol + 10));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q6_DESCRIPTION), 
				firstRow, firstRow + 1, firstCol + 11, firstCol + 11));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q7), 
				firstRow, firstRow + 1, firstCol + 12, firstCol + 12));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q8), 
				firstRow, firstRow + 1, firstCol + 13 , firstCol + 13));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q9), 
				firstRow, firstRow + 1, firstCol + 14, firstCol + 14));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q10), 
				firstRow, firstRow + 1, firstCol + 15, firstCol + 15));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(ColumnConstants.GPI_1_Q10_DESCRIPTION), 
				firstRow, firstRow + 1, firstCol + 16, firstCol + 16));
		mergedCells.add(createHeaderCell(sheet, row, 
				HEADER_LABELS.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT), 
				firstRow, firstRow + 1, firstCol + 17, firstCol + 17));
		mergedCells.add(createHeaderCell(sheet, row, 
				HEADER_LABELS.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES), 
				firstRow, firstRow + 1, firstCol + 18, firstCol + 18));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q11), 
				firstRow, firstRow, firstCol + 19, firstCol + 21));
		
		row = sheet.createRow(firstRow + 1);
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q11a), 
				firstRow + 1, firstRow + 1, firstCol + 19, firstCol + 19));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q11b), 
				firstRow + 1, firstRow + 1, firstCol + 20, firstCol + 20));
		mergedCells.add(createHeaderCell(sheet, row, HEADER_LABELS.get(GPIReportConstants.GPI_1_Q11c), 
				firstRow + 1, firstRow + 1, firstCol + 21, firstCol + 21));
		
		for (CellRangeAddress ca : mergedCells) {
			GPIReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
		}
	}

	/**
	 * @param sheet
	 * @param headerLabel
	 * @param initColPos
	 * @param initRowPos
	 * @return
	 */
	private CellRangeAddress createHeaderCell(Sheet sheet, Row row, String headerLabel, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		Cell cell = row.createCell(firstCol);
		cell.setCellValue(headerLabel);
		cell.setCellStyle(template.getHeaderCellStyle());
		setMaxColWidth(sheet, cell, firstCol);

		CellRangeAddress mergedHeaderCell = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		
		if (mergedHeaderCell.getNumberOfCells() > 1)
			sheet.addMergedRegion(mergedHeaderCell);
		
		return mergedHeaderCell;
	}
	
	/**
	 * @param sheet
	 * @param report
	 */
	public void renderReportData(SXSSFSheet sheet, GPIReport report) {
		
		Map<String, GPIReportOutputColumn> columns = report.getPage().getContents().stream()
				.findAny().get()
				.keySet().stream()
				.collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));
		
		for (int i = 0; i < report.getPage().getContents().size(); i++) {
			Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
			Row row = sheet.createRow(initHeaderRowOffset + i + 2);
			createDataCell(sheet, row, report, 0, columns.get(GPIReportConstants.COLUMN_YEAR), rowData);
			createDataCell(sheet, row, report, 1, columns.get(ColumnConstants.DONOR_AGENCY), rowData);
			createDataCell(sheet, row, report, 2, columns.get(ColumnConstants.PROJECT_TITLE), rowData);
			createDataCell(sheet, row, report, 3, columns.get(GPIReportConstants.GPI_1_Q1), rowData);
			createDataCell(sheet, row, report, 4, columns.get(GPIReportConstants.GPI_1_Q2), rowData);
			createDataCell(sheet, row, report, 5, columns.get(GPIReportConstants.GPI_1_Q3), rowData);
			createDataCell(sheet, row, report, 6, columns.get(GPIReportConstants.GPI_1_Q4), rowData);
			createDataCell(sheet, row, report, 7, columns.get(GPIReportConstants.GPI_1_Q5), rowData);
			createDataCell(sheet, row, report, 8, columns.get(GPIReportConstants.GPI_1_Q5), rowData);
			createDataCell(sheet, row, report, 9, columns.get(GPIReportConstants.GPI_1_Q5), rowData);
			createDataCell(sheet, row, report, 10, columns.get(ColumnConstants.GPI_1_Q6), rowData);
			createDataCell(sheet, row, report, 11, columns.get(ColumnConstants.GPI_1_Q6_DESCRIPTION), rowData);
			createDataCell(sheet, row, report, 12, columns.get(ColumnConstants.GPI_1_Q7), rowData);
			createDataCell(sheet, row, report, 13, columns.get(ColumnConstants.GPI_1_Q8), rowData);
			createDataCell(sheet, row, report, 14, columns.get(ColumnConstants.GPI_1_Q9), rowData);
			createDataCell(sheet, row, report, 15, columns.get(ColumnConstants.GPI_1_Q10), rowData);
			createDataCell(sheet, row, report, 16, columns.get(ColumnConstants.GPI_1_Q10_DESCRIPTION), rowData);
			createDataCell(sheet, row, report, 17, 
					columns.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT), rowData);
			createDataCell(sheet, row, report, 18, 
					columns.get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES), rowData);
			
			
//			createDataCell(sheet, report, i, 19, "http://11a.doc", rowData);
//			createDataCell(sheet, report, i, 20, "http://11b.doc", rowData);
//			createDataCell(sheet, report, i, 21, "http://11c.doc", rowData);
		}
	}

	/**
	 * @param sheet
	 * @param report
	 * @param i
	 * @param columns
	 * @param rowData
	 */
	private void createDataCell(SXSSFSheet sheet, Row row, GPIReport report, int colPos, 
			GPIReportOutputColumn column, Map<GPIReportOutputColumn, String> rowData) {
		
		createCell(report, sheet, row, colPos, column.originalColumnName, rowData.get(column));
	}
	
	@Override
	protected void calculateColumnsWidth(Sheet sheet, GPIReport report) {
		Map<Integer, Integer> sheetWidths = cachedWidths.get(sheet.getSheetName());

		for (int i = 0; i < 22; i++) {
			try {
				if (sheetWidths.containsKey(i)) {
					int width = (int) ((sheetWidths.get(i)) * template.getCharWidth());
					width = width < template.getMaxColumnWidth() ? width : template.getMaxColumnWidth() - 1;
					sheet.setColumnWidth(i, width);
				} else {
					sheet.setColumnWidth(i, (int) (template.getDefaultColumnWidth() * template.getCharWidth()));
				}
			} catch (Exception e) {
				// Alternative slow method.
				sheet.autoSizeColumn(i);
			}
		}
	}
	
	@Override
	public int getCellType(String columnName) {
		switch(columnName) {
			case GPIReportConstants.GPI_1_Q1:
			case ColumnConstants.GPI_1_Q7:
			case ColumnConstants.GPI_1_Q8:
			case ColumnConstants.GPI_1_Q9:
				return Cell.CELL_TYPE_NUMERIC;
			default:
				return super.getCellType(columnName);
		}
	}
	
	@Override
	protected boolean hasSpecificStyle(String columnName) {
		switch(columnName) {
			case GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT:
			case GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES:
			case ColumnConstants.GPI_1_Q6:
			case ColumnConstants.GPI_1_Q6_DESCRIPTION:
			case ColumnConstants.GPI_1_Q7:
			case ColumnConstants.GPI_1_Q8:
			case ColumnConstants.GPI_1_Q9:
			case ColumnConstants.GPI_1_Q10:
			case ColumnConstants.GPI_1_Q10_DESCRIPTION:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	protected CellStyle getSpecificStyle(String columnName) {
		switch(columnName) {
			case GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT:
			case GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES:
				return template.getNumberStyle();
			case ColumnConstants.GPI_1_Q6:
			case ColumnConstants.GPI_1_Q6_DESCRIPTION:
			case ColumnConstants.GPI_1_Q7:
			case ColumnConstants.GPI_1_Q8:
			case ColumnConstants.GPI_1_Q9:
			case ColumnConstants.GPI_1_Q10:
			case ColumnConstants.GPI_1_Q10_DESCRIPTION:
				return template.getCenterStyle();
			default:
				return template.getNumberStyle();
		}
	}
}
