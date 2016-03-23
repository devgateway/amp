package org.dgfoundation.amp.reports.saiku.export;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;

/** renders the result of report to a plain Excel. See {@link SaikuReportXlsxExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportXlsxPlainExporter extends SaikuReportXlsxExporter {
	
	private final String reportSheetName = "Plain";
	
	private static final Logger logger = Logger.getLogger(SaikuReportXlsxPlainExporter.class);
	
	@Override
	protected void renderReportTableHeader(Workbook wb, Sheet sheet, GeneratedReport report) {
		int hiddenColumnsCnt = 0;
		for(int i=0; i < report.generatedHeaders.size(); i++) {
			Row row = sheet.createRow(i);
			for(HeaderCell headerCell : report.generatedHeaders.get(i)) {
				
				if (isHiddenColumn(headerCell.originalName)) {
					hiddenColumnsCnt++;
					continue;
				}
				
				int cellColumnPos = headerCell.getStartColumn() - hiddenColumnsCnt;
				Cell cell = row.createCell(cellColumnPos);
				cell.setCellValue(headerCell.getName());
				setMaxColWidth(sheet, cell, cellColumnPos);
				CellRangeAddress mergedHeaderCell = new CellRangeAddress(i, i + headerCell.getRowSpan() - 1, 
						cellColumnPos, cellColumnPos + headerCell.getColSpan() - 1);
				sheet.addMergedRegion(mergedHeaderCell);
			}
		}
	}
	
	
	protected int renderGroupRow(Sheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row) {
		for (ReportArea reportArea : reportContents.getChildren()) {
			if (reportArea.getNrEntities() > 0) {
				Cell cell = row.createCell(level);
				cell.setCellValue(reportArea.getOwner().debugString);
			} 
			
			int rowPosInit = row.getRowNum();
			int rowPos = renderTableRow(sheet, report, reportArea, level+1, row);
			
			// populate all cells of the hierarchy column with the name of the hierarchy
			for (int i = rowPosInit; i <= rowPos; i++) {
				if (reportArea.getNrEntities() > 0) {
					Row currRow = sheet.getRow(i);
					Cell cell = currRow.createCell(level);
					cell.setCellValue(reportArea.getOwner().debugString);
					setMaxColWidth(sheet, cell, level);
				} 
			}
			
			// Do not generate the row for sub-totals
			if (reportArea == reportContents.getChildren().get(reportContents.getChildren().size() - 1)) {
				return rowPos;
			}
			
			row = sheet.createRow(rowPos + 1);
		}
		
		return row.getRowNum();
	}
	
	protected void renderTableTotals(Sheet sheet, GeneratedReport report, ReportArea reportContents) {
		IntWrapper intWrapper = new IntWrapper();
		Row row = sheet.createRow(sheet.getLastRowNum());
		report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {	
			createTotalCell(sheet, row, intWrapper.value, report, reportContents.getContents().get(roc));
			intWrapper.inc();
		});
	}
	
	protected String getReportSheetName() {
		return this.reportSheetName;
	}
}
