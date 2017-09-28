package org.dgfoundation.amp.gpi.reports.export.excel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
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
public class GPIReportIndicator6XlsxExporter extends GPIReportXlsxExporter {
    
    public GPIReportIndicator6XlsxExporter() {
        reportSheetName = "Indicator 6";
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

            Cell cell = row.createCell(i);
            cell.setCellValue(report.getPage().getHeaders().get(i).columnName);
            setMaxColWidth(sheet, cell, i);

            CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, i, i);
            if (mergedHeaderCell.getNumberOfCells() > 1)
                sheet.addMergedRegion(mergedHeaderCell);

            mergedCells.add(mergedHeaderCell);
            cell.setCellStyle(template.getHeaderCellStyle());
        }

        for (CellRangeAddress ca : mergedCells) {
            GPIReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
        }
    }
    
    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableSummary(Workbook wb, Sheet sheet, GPIReport report) {
        Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();
        
        Row summaryRow = sheet.createRow(initSummaryRowOffset);
        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn gpiReportOutputColumn = report.getPage().getHeaders().get(i);
            if (report.getSummary().containsKey(gpiReportOutputColumn)) {
                Cell cell = summaryRow.createCell(i);
                cell.setCellValue(String.format("%s\n%s", 
                        report.getSummary().get(gpiReportOutputColumn), gpiReportOutputColumn.columnName));
                setMaxColWidth(sheet, cell, i);

                CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, i, i);
                if (mergedHeaderCell.getNumberOfCells() > 1)
                    sheet.addMergedRegion(mergedHeaderCell);

                mergedCells.add(mergedHeaderCell);
                cell.setCellStyle(template.getSummaryCellStyle());
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
                createCell(report, sheet, row, j, column.originalColumnName, rowData.get(column));
            }
        }
    }
    
    @Override
    public int getCellType(String columnName) {
        switch(columnName) {
            case MeasureConstants.PLANNED_DISBURSEMENTS:
            case GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET:
                return Cell.CELL_TYPE_NUMERIC;
            default:
                return super.getCellType(columnName);
        }
    }
    
    @Override
    protected boolean hasSpecificStyle(String columnName) {
        return columnName.equals(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET);
    }
}
