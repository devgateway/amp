package org.dgfoundation.amp.gpi.reports.export.excel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator9bXlsxExporter extends GPIReportXlsxExporter {

    public GPIReportIndicator9bXlsxExporter() {
        reportSheetName = "Indicator 9b";
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
        
        Map<String, GPIReportOutputColumn> columns = report.getSummary().keySet().stream()
                .collect(Collectors.toMap(GPIReportOutputColumn::getOriginalColumnName, Function.identity()));
        
        Row summaryRow = sheet.createRow(initSummaryRowOffset);
        IntWrapper colPos = new IntWrapper();
        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            String columnName = report.getPage().getHeaders().get(i).columnName;
            if (columns.containsKey(columnName)) {
                mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, colPos.value, columnName));
                colPos.inc();
            }
        }
        
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, colPos.value, 
                GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS));

        for (CellRangeAddress ca : mergedCells) {
            GPIReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
        }
    }

    public CellRangeAddress createSummaryCell(Sheet sheet, GPIReport report, 
            Map<String, GPIReportOutputColumn> columns, Row summaryRow, int pos, String columnName) {
        
        GPIReportOutputColumn ind9BColumn = columns.get(columnName);
        String cellValue = String.format("%s\n%s", report.getSummary().get(ind9BColumn), ind9BColumn.columnName);
        
        Cell cell = summaryRow.createCell(pos);
        cell.setCellValue(cellValue);
        cell.setCellStyle(template.getSummaryCellStyle());
        setMaxColWidth(sheet, cell, pos);
        CellRangeAddress summaryCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, pos, pos);
        
        return summaryCell;
    }

    /**
     * @param sheet
     * @param report
     */
    public void renderReportData(SXSSFSheet sheet, GPIReport report) {
        for (int i = 0; i < report.getPage().getContents().size(); i++) {
            Row row = sheet.createRow(initHeaderRowOffset + (i + 1));
            Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
            
            for (int j = 0; j < report.getPage().getHeaders().size(); j++) {
                GPIReportOutputColumn column = report.getPage().getHeaders().get(j);
                createCell(report, sheet, row, j, column.originalColumnName, rowData.get(column));
            }
        }
    }

    @Override
    public int getCellType(String columnName) {
        switch (columnName) {
            case MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES:
            case MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES:
            case MeasureConstants.NATIONAL_AUDITING_PROCEDURES:
            case MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES:
                return Cell.CELL_TYPE_NUMERIC;
        default:
            return super.getCellType(columnName);
        }
    }
}
