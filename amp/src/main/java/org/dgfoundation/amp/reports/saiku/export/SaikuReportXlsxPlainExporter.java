package org.dgfoundation.amp.reports.saiku.export;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.newreports.*;

import java.util.ArrayList;
import java.util.List;

/** renders the result of report to a plain Excel. See {@link SaikuReportXlsxExporter}
 * @author Viorel Chihai
 *
 */
public class SaikuReportXlsxPlainExporter extends SaikuReportXlsxExporter {
    
    private final String reportSheetName = "Plain";
    
    private static final Logger logger = Logger.getLogger(SaikuReportXlsxPlainExporter.class);

    /**
     * @param sheet
     * @param report
     */
    @Override
    protected void renderReportData(SXSSFSheet sheet, GeneratedReport report) {
        Row row = sheet.createRow(initHeaderRowOffset + report.generatedHeaders.size());
        renderTableRow(sheet, report, report.reportContents, 0, row, new ArrayList<>());
        if (report.hasTotals()) {
            renderTableTotals(sheet, report, report.reportContents);
        }
    }
    
    
    @Override
    protected void renderReportTableHeader(Workbook wb, Sheet sheet, GeneratedReport report) {
        int hiddenColumnsCnt = 0;
        for(int i=0; i < report.generatedHeaders.size(); i++) {
            Row row = sheet.createRow(initHeaderRowOffset + i);
            for(HeaderCell headerCell : report.generatedHeaders.get(i)) {
                
                if (isHiddenColumn(headerCell.originalName)) {
                    hiddenColumnsCnt++;
                    continue;
                }
                
                int cellColumnPos = headerCell.getStartColumn() - hiddenColumnsCnt;
                Cell cell = row.createCell(cellColumnPos);
                cell.setCellValue(headerCell.getName());
                setMaxColWidth(sheet, cell, cellColumnPos);
                CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset + i, initHeaderRowOffset + i + headerCell.getRowSpan() - 1, 
                        cellColumnPos, cellColumnPos + headerCell.getColSpan() - 1);
                if (mergedHeaderCell.getNumberOfCells()  > 1)
                    sheet.addMergedRegion(mergedHeaderCell);
            }
        }
    }
    

    
    /**
     * @param sheet
     * @param report
     * @param reportContents
     * @param level
     * @param row
     * @return
     */
    protected int renderTableRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row, List<ReportCell> _hierarchies) {
        this.flushCounter++;
        List<ReportCell> hierarchies = new ArrayList<>(_hierarchies);
        if (reportContents.getChildren() != null ) {
            if (reportContents.getOwner() != null) {
                reportContents.getContents().entrySet().stream().filter(e -> e.getKey().originalColumnName.equals(reportContents.getOwner().columnName)).
                    findFirst().ifPresent(p -> hierarchies.add(p.getValue()));
            }
            return renderGroupRow(sheet, report, reportContents, level, row, hierarchies);
        } else {
            // Totals are rendered in renderTableTotals method(). If it is summaryReport with one dummy column, we need to show the row with data
            if (level == 0 && !hasReportGeneratedDummyColumn(report)) {
                return row.getRowNum();
            }
            
            IntWrapper intWrapper = new IntWrapper();
            
            for (ReportOutputColumn roc : report.leafHeaders) {
                if (isHiddenColumn(roc.originalColumnName))
                    continue;
                ReportCell rc = null;
                if ((report.spec.getHierarchies().size() > 0) && (intWrapper.value < level - 1)) {
                    rc = hierarchies.get(intWrapper.intValue()) != null ? hierarchies.get(intWrapper.intValue()) : roc.emptyCell;
                } else {
                    rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
                }
                createCell(sheet, row, intWrapper.value, rc);
                intWrapper.inc();
            }
            if (flushCounter % flushBatchSize == 0) {
                flushCounter = 0;
                try {
                    ((SXSSFSheet)sheet).flushRows(2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return row.getRowNum();
    }
    
    @SuppressWarnings("unused")
    protected int renderGroupRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row, List<ReportCell> _hierarchies) {
        List<ReportCell> hierarchies = new ArrayList<>();
        //hierarchies are kind of immutable in this implementation
        hierarchies.addAll(_hierarchies);
        for (int i = 0; i < reportContents.getChildren().size(); i++) {
            ReportArea reportArea = reportContents.getChildren().get(i);
            List<ReportCell> roclist = new ArrayList<ReportCell>(reportArea.getContents().values());
            int rowPosInit = row.getRowNum();
            //the first row will contain the actual values, which will be pushed to the list of already existing hierarchies
            int rowPos = renderTableRow(sheet, report, reportArea, level+1, row, hierarchies);
            // Do not generate the row for sub-totals
            if (reportArea == reportContents.getChildren().get(reportContents.getChildren().size() - 1)) {
                return rowPos;
            }
            row = sheet.createRow(rowPos + 1);
        }
        return row.getRowNum();
    }
    
    protected void renderTableTotals(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents) {
        IntWrapper intWrapper = new IntWrapper();
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> { 
            createTotalCell(sheet, row, intWrapper.value, report, reportContents.getContents().get(roc));
            intWrapper.inc();
            
        });
        try {
            sheet.flushRows(10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    protected String getReportSheetName() {
        return this.reportSheetName;
    }
}
