package org.dgfoundation.amp.gpi.reports.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.GPIReportUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator5aXlsxExporter extends GPIReportXlsxExporter {

    public GPIReportIndicator5aXlsxExporter() {
        reportSheetName = "Indicator 5a";
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
            Cell cell = row.createCell(i);
            cell.setCellValue(getColumnHeaderLabel(gpiReportOutputColumn.columnName));
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
        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
            GPIReportOutputColumn gpiReportOutputColumn = report.getPage().getHeaders().get(i);
            if (columns.containsKey(gpiReportOutputColumn.getOriginalColumnName())) {
                GPIReportOutputColumn summaryColumn = columns.get(gpiReportOutputColumn.getOriginalColumnName());
                Cell cell = summaryRow.createCell(i);
                cell.setCellValue(String.format("%s\n%s", report.getSummary().get(summaryColumn),
                        gpiReportOutputColumn.columnName));
                setMaxColWidth(sheet, cell, i);

                CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, i,
                        i);
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
            Row row = sheet.createRow(initHeaderRowOffset + (i + 1));
            Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
            for (int j = 0; j < report.getPage().getHeaders().size(); j++) {
                GPIReportOutputColumn column = report.getPage().getHeaders().get(j);
                if (isRemarkColumn(column.originalColumnName)) {
                    createCell(report, sheet, row, j, column.originalColumnName, 
                            GPIReportUtils.getRemarksForIndicator5a(rowData));
                } else {
                    createCell(report, sheet, row, j, column.originalColumnName, rowData.get(column));
                }
            }
        }
    }

    @Override
    public int getCellType(String columnName) {
        switch (columnName) {
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
        switch (columnName) {
        case MeasureConstants.DISBURSED_AS_SCHEDULED:
        case MeasureConstants.OVER_DISBURSED:
        case GPIReportConstants.COLUMN_CONCESSIONAL:
        case GPIReportConstants.COLUMN_REMARK:
            return true;
        default:
            return false;
        }
    }

    @Override
    protected CellStyle getSpecificStyle(String columnName) {
        switch (columnName) {
        case MeasureConstants.DISBURSED_AS_SCHEDULED:
        case MeasureConstants.OVER_DISBURSED:
            return template.getNumberStyle();
        case GPIReportConstants.COLUMN_CONCESSIONAL:
            return template.getCenterStyle();
        case GPIReportConstants.COLUMN_REMARK:
            return template.getWrappedStyle();
        default:
            return template.getNumberStyle();
        }
    }

    protected boolean isRemarkColumn(String columnName) {
        return columnName.equals(GPIReportConstants.COLUMN_REMARK);
    }

    private String getColumnHeaderLabel(String columnName) {
        return getColumnHeaderLabel(GPIReportConstants.INDICATOR_5A_COLUMN_LABELS, columnName);
    }
    
}
