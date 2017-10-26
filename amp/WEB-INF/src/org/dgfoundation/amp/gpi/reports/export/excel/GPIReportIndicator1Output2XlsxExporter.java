package org.dgfoundation.amp.gpi.reports.export.excel;

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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.gpi.reports.GPIReportOutputColumn;
import org.dgfoundation.amp.gpi.reports.export.GPIReportMessages;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportIndicator1Output2XlsxExporter extends GPIReportXlsxExporter {
    
    public int initHeaderRowOffset = 3;

    public GPIReportIndicator1Output2XlsxExporter() {
        reportSheetName = "Indicator 1 Output 2";
    }
    
    protected void generateReportSheet(SXSSFWorkbook wb, SXSSFSheet sheet, GPIReport report) {
        renderReportTableHeader(wb, sheet, report);
        renderReportData(sheet, report);
    }

    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableHeader(Workbook wb, Sheet sheet, GPIReport report) {
        Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();

        Row row = sheet.createRow(initHeaderRowOffset);
        
        Cell cell = row.createCell(0);
        cell.setCellValue(GPIReportConstants.COLUMN_YEAR);
        cell.setCellStyle(template.getHeaderCellStyle());
        setMaxColWidth(sheet, cell, 0);

        CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, 0, 0);
        mergedCells.add(mergedHeaderCell);
        
        cell = row.createCell(1);
        cell.setCellValue(COLUMN_QUESTION);
        cell.setCellStyle(template.getHeaderCellStyle());
        setMaxColWidth(sheet, cell, 1);

        mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, 1, 1);
        mergedCells.add(mergedHeaderCell);
        
        cell = row.createCell(2);
        cell.setCellValue(COLUMN_VALUE);
        cell.setCellStyle(template.getHeaderCellStyle());
        setMaxColWidth(sheet, cell, 2);

        mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset, initHeaderRowOffset, 2, 2);
        mergedCells.add(mergedHeaderCell);

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
            Row row = sheet.createRow(initHeaderRowOffset + (i * 4 + 1));
            Map<GPIReportOutputColumn, String> rowData = report.getPage().getContents().get(i);
            
            for (int j = 0; j < report.getPage().getHeaders().size(); j++) {
                GPIReportOutputColumn column = report.getPage().getHeaders().get(j);

                int rowPos = initHeaderRowOffset + (i * 4 + 1);
                if (!column.originalColumnName.equals(GPIReportConstants.COLUMN_YEAR)) {
                    switch (column.originalColumnName) {
                    case GPIReportConstants.GPI_1_Q1:
                        break;
                    case GPIReportConstants.GPI_1_Q2:
                        rowPos += 1;
                        row = sheet.createRow(rowPos);
                        break;
                    case GPIReportConstants.GPI_1_Q3:
                        rowPos += 2;
                        row = sheet.createRow(rowPos);
                        break;
                    case GPIReportConstants.GPI_1_Q4:
                        rowPos += 3;
                        row = sheet.createRow(rowPos);
                        break;
                    }

                    createCell(report, sheet, row, 1, COLUMN_QUESTION, GPIReportMessages.getString(column.originalColumnName));
                    createCell(report, sheet, row, 2, COLUMN_VALUE, rowData.get(column));
                } else {
                    createCell(report, sheet, row, 0, column.columnName, rowData.get(column));
                }
            }

            CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset + (i * 4 + 1), 
                    initHeaderRowOffset + ((i+1) * 4), 0, 0);
            
            if (mergedHeaderCell.getNumberOfCells() > 1)
                sheet.addMergedRegion(mergedHeaderCell);
        }
    }
    
    @Override
    protected boolean hasSpecificStyle(String columnName) {
        return true;
    }
    
    @Override
    protected CellStyle getSpecificStyle(String columnName) {
        switch(columnName) {
            case COLUMN_QUESTION:
                return template.getWrappedStyle();  
            case COLUMN_VALUE:
                return template.getNumberStyle();
            case GPIReportConstants.COLUMN_YEAR:
                return template.getHierarchyStyle();
            default:
                return template.getNumberStyle();
        }
    }
}
