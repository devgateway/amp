package org.dgfoundation.amp.gpi.reports.export.excel;

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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator9bXlsxExporter extends GPIReportXlsxExporter {
    
    private static final int SUMMARY_COLUMN_POS_2 = 2;
    private static final int SUMMARY_COLUMN_POS_3 = 3;
    private static final int SUMMARY_COLUMN_POS_5 = 5;
    private static final int SUMMARY_COLUMN_POS_4 = 4;
    private static final int SUMMARY_ROW_OFFSET = 4;
    
    private static final short DEFAULT_HEIGHT_OF_SUMMARY_MERGED_REGION = 800;
    
    public GPIReportIndicator9bXlsxExporter() {
        reportSheetName = "Indicator 9b";
        initSummaryRowOffset = SUMMARY_ROW_OFFSET;
    }

    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableHeader(Workbook wb, Sheet sheet, GPIReport report) {
        Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();
        
        Row row = sheet.createRow(initHeaderRowOffset);
        
        List<GPIReportOutputColumn> colums = getDataTableColumns(report);
        
        for (int i = 0; i < colums.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(colums.get(i).columnName);
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
        
        Row summaryRow = sheet.createRow(initSummaryRowOffset - 1);
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, 
                SUMMARY_COLUMN_POS_2, SUMMARY_COLUMN_POS_5, GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS));
        
        summaryRow = sheet.createRow(initSummaryRowOffset);
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, SUMMARY_COLUMN_POS_2, 
                MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, SUMMARY_COLUMN_POS_3, 
                MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, SUMMARY_COLUMN_POS_4, 
                MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
        mergedCells.add(createSummaryCell(sheet, report, columns, summaryRow, SUMMARY_COLUMN_POS_5, 
                MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));

        for (CellRangeAddress ca : mergedCells) {
            GPIReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
        }
    }
    
    public CellRangeAddress createSummaryCell(Sheet sheet, GPIReport report, 
            Map<String, GPIReportOutputColumn> columns, Row summaryRow, int pos, String columnName) {
        
        return createSummaryCell(sheet, report, columns, summaryRow, pos, pos, columnName);
    }

    public CellRangeAddress createSummaryCell(Sheet sheet, GPIReport report, 
            Map<String, GPIReportOutputColumn> columns, Row summaryRow, int init, int end, String columnName) {
        
        GPIReportOutputColumn ind9BColumn = columns.get(columnName);
        String cellValue = String.format("%s\n%s", report.getSummary().get(ind9BColumn), ind9BColumn.columnName);
        
        Cell cell = summaryRow.createCell(init);
        cell.setCellValue(cellValue);
        cell.setCellStyle(template.getSummaryCellStyle());
        setMaxColWidth(sheet, cell, init);
        CellRangeAddress summaryCell = new CellRangeAddress(summaryRow.getRowNum(), summaryRow.getRowNum(), init, end);
        
        if (summaryCell.getNumberOfCells() > 1) {
            summaryRow.setHeight(DEFAULT_HEIGHT_OF_SUMMARY_MERGED_REGION);
            sheet.addMergedRegion(summaryCell);
        }
        
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
            
            List<GPIReportOutputColumn> colums = getDataTableColumns(report);
            
            for (int j = 0; j < colums.size(); j++) {
                GPIReportOutputColumn column = colums.get(j);
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
    
    public Predicate<GPIReportOutputColumn> getColumnTableFilter() {
        return column -> !column.originalColumnName.equals(GPIReportConstants.COLUMN_USE_OF_COUNTRY_SYSTEMS);
    }
}
