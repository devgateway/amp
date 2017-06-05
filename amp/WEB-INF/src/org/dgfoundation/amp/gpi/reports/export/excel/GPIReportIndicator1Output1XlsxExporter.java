package org.dgfoundation.amp.gpi.reports.export.excel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator1Output1XlsxExporter extends GPIReportXlsxExporter {
	
	public static final Map<String, String> COLUMN_LABELS = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS,
					String.format("%s %s", GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS, "(Q1)"));
			put(GPIReportConstants.COLUMN_CONCESSIONAL,
					String.format("%s?\n%s", GPIReportConstants.COLUMN_CONCESSIONAL, "(Yes=1 / No=0)"));
			put(MeasureConstants.ACTUAL_DISBURSEMENTS,
					String.format("%s %s", MeasureConstants.ACTUAL_DISBURSEMENTS, "(Q2)"));
			put(MeasureConstants.PLANNED_DISBURSEMENTS,
					String.format("%s %s", MeasureConstants.PLANNED_DISBURSEMENTS, "(Q3)"));
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

		Row row = sheet.createRow(initHeaderRowOffset);
		for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
			GPIReportOutputColumn gpiReportOutputColumn = report.getPage().getHeaders().get(i);
			if (!isHiddenColumn(gpiReportOutputColumn.originalColumnName)) {
				Cell cell = row.createCell(i);
				cell.setCellValue(gpiReportOutputColumn.columnName);
				setMaxColWidth(sheet, cell, i);
	
				CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, i, i);
				if (mergedHeaderCell.getNumberOfCells() > 1)
					sheet.addMergedRegion(mergedHeaderCell);
	
				mergedCells.add(mergedHeaderCell);
				cell.setCellStyle(template.getHeaderCellStyle());
			}
		}

		for (CellRangeAddress ca : mergedCells) {
			GPIReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
		}
	}
	
	/**
	 * @param sheet
	 * @param report
	 */
	public void renderReportData(SXSSFSheet sheet, GPIReport report) {
		for (int i = 0; i < report.getPage().getContents().size(); i++) {
			Row row = sheet.createRow(initHeaderRowOffset + (i+1));
			Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
			for (int j = 0; j < report.getPage().getHeaders().size(); j++) {
				GPIReportOutputColumn column = report.getPage().getHeaders().get(j);
				if (!isHiddenColumn(column.originalColumnName)) {
					createCell(report, sheet, row, j, column.originalColumnName, rowData.get(column));
				}
			}
		}
	}
	
	@Override
	public int getCellType(String columnName) {
		switch(columnName) {
			case MeasureConstants.ACTUAL_DISBURSEMENTS:
			case MeasureConstants.PLANNED_DISBURSEMENTS:
			case GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS:
			case GPIReportConstants.COLUMN_DISBURSEMENTS_OTHER_PROVIDERS:
				return Cell.CELL_TYPE_NUMERIC;
			default:
				return super.getCellType(columnName);
		}
	}
	
	@Override
	protected boolean hasSpecificStyle(String columnName) {
		switch(columnName) {
			case MeasureConstants.DISBURSED_AS_SCHEDULED:
			case MeasureConstants.OVER_DISBURSED:
			case GPIReportConstants.COLUMN_CONCESSIONAL:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	protected CellStyle getSpecificStyle(String columnName) {
		switch(columnName) {
			case MeasureConstants.DISBURSED_AS_SCHEDULED:
			case MeasureConstants.OVER_DISBURSED:
				return template.getNumberStyle();
			case GPIReportConstants.COLUMN_CONCESSIONAL:
				return template.getCenterStyle();
			default:
				return template.getNumberStyle();
		}
	}
}
