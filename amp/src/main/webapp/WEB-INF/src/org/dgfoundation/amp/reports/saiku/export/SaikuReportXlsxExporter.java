package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportXlsxExporter implements SaikuReportExporter {
    
    private SaikuReportExcelTemplate template;
    
    public final String reportSheetName = "Formatted";
    public final String summarySheetName = "Summary Information";
    
    public final int currencyUnitsRowPosition = 1;
    public final int initHeaderRowOffset = 3;

    private static final int IN_MEMORY_ROWS = 10;
    
    /**
     * Map<String sheetName, Map<Integer header-column-number, Integer columnWidth>> 
     */
    private final Map<String, Map<Integer, Integer>> cachedWidths = new HashMap<String, Map<Integer, Integer>>();
    
    private static final Logger logger = Logger.getLogger(SaikuReportXlsxExporter.class);

    /**
     * counter of rows written to output before being flushed (SXSSF behaviour)
     */

    protected int flushCounter = 0;
    /**
     * size of a batch of rows being accumulated until being flushed
     */
    protected int flushBatchSize = 100;
    
    /**
     * We need to know the current amount unit (units, millions, etc) at the moment of extracting the ReportCell's value to get the correct Double number.
     * Without this conversion ReportCell.displayedValue and ReportCell.value->double will be different.   
     */
    protected AmountsUnits amountUnits = null;

    /**
     * generates a workbook containing data about 1 or 2 reports. Normally you'd want both reports to actually be the
     * same one generated in different currencies, but the code does not care and you can put as different reports as
     * you want
     * 
     * @param report1
     *            - the first report
     * @param report2
     *            - the second report, might be null
     */
    @Override
    public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
        logger.info("Start generating Excel Report...");
//      this.requiresMemoryCare = requiresMemoryCare(report);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        template = new SaikuReportExcelTemplate(wb);
        
        addReportSheetToWorkbook(wb, report, getReportSheetName());
        addSummarySheetToWorkbook(wb, report, getSummarySheetName());
        
        addDualReportSheetToWorkbook(wb, dualReport);
        addDualSummarySheetToWorkbook(wb, dualReport);
        
        wb.write(os);
        os.flush();
        os.close();
        wb.dispose();
        logger.info("Stop generating Excel Report.");
        return os.toByteArray();
    }   
    

    /**
     * @param wb
     * @param report
     * @param sheetName
     */
    protected void addReportSheetToWorkbook(SXSSFWorkbook wb, GeneratedReport report, String sheetName) {
        SXSSFSheet reportSheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
        cachedWidths.computeIfAbsent(reportSheet.getSheetName(), k -> new HashMap<Integer, Integer>());
        
        generateReportSheet(wb, reportSheet, report);
        postProcessGeneratedSheet(reportSheet, report);
    }
    
    /**
     * @param wb
     * @param dualReport
     */
    protected void addDualReportSheetToWorkbook(SXSSFWorkbook wb, GeneratedReport dualReport) {
        if (dualReport != null) {
            String ampCurrencyCode = dualReport.spec.getSettings().getCurrencyCode();
            ampCurrencyCode = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(ampCurrencyCode);
            
            String sheetName = TranslatorWorker.translateText(getReportSheetName()) + String.format(" - %s", ampCurrencyCode);
            addReportSheetToWorkbook(wb, dualReport, sheetName);
        }
    }
    
    protected void addSummarySheetToWorkbook(SXSSFWorkbook wb, GeneratedReport report, String sheetName) {
        SXSSFSheet summarySheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
        generateSummarySheet(wb, summarySheet, report.spec);
    }
    
    protected void addDualSummarySheetToWorkbook(SXSSFWorkbook wb, GeneratedReport dualReport) {
        if (dualReport != null) {
            String ampCurrencyCode = dualReport.spec.getSettings().getCurrencyCode();
            ampCurrencyCode = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(ampCurrencyCode);
            
            String sheetName = TranslatorWorker.translateText(getSummarySheetName()) + String.format(" - %s", ampCurrencyCode);
            addSummarySheetToWorkbook(wb, dualReport, sheetName);
        }
    }
    
    protected void postProcessGeneratedSheet(Sheet sheet, GeneratedReport report) {
        calculateColumnsWidth(sheet, report);
    }

    protected void generateReportSheet(SXSSFWorkbook wb, SXSSFSheet sheet, GeneratedReport report) {
        renderReportTableUnits(wb, sheet, report);
        renderReportTableHeader(wb, sheet, report);
        renderReportData(sheet, report);
    }
    
    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableUnits(SXSSFWorkbook wb, SXSSFSheet sheet, GeneratedReport report) {
        Row row = sheet.createRow(currencyUnitsRowPosition);
        Cell cell = row.createCell(0);
        
        amountUnits = report.spec.getSettings().getUnitsOption();
        String unitsOption = amountUnits.userMessage;
        String currencyCode = report.spec.getSettings().getCurrencyCode();
        currencyCode = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currencyCode);
        
        String translatedNotes = TranslatorWorker.translateText(unitsOption);
        String translatedCurrencyCode = TranslatorWorker.translateText(currencyCode);
        
        String tableCurrencyUnitText = translatedNotes;
        
        if (!report.spec.isShowOriginalCurrency()) {
            tableCurrencyUnitText += " - " + translatedCurrencyCode;
        }

        cell.setCellValue(tableCurrencyUnitText);
        
        CellRangeAddress mergedUnitsCell = new CellRangeAddress(currencyUnitsRowPosition, currencyUnitsRowPosition, 
                0, report.rootHeaders.size() + 1);
        
        if (mergedUnitsCell.getNumberOfCells() > 1)
            sheet.addMergedRegion(mergedUnitsCell);
    }


    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableHeader(Workbook wb, Sheet sheet, GeneratedReport report) {
        List<Integer> hiddenColumnPositions = new ArrayList<>();
        Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();
        
        for(int i=0; i < report.generatedHeaders.size(); i++) {
            Row row = sheet.createRow(initHeaderRowOffset + i);
            for(HeaderCell headerCell : report.generatedHeaders.get(i)) {
                
                if (isHiddenColumn(headerCell.originalName)) {
                    hiddenColumnPositions.add(headerCell.getStartColumn());
                    continue;
                }
                
                // when a column (not measure) is splitted (like MTEFs columns), we need to calculate the position of hidden columns
                int offsetPosition = getOffsetPositionOfHiddenColumns(hiddenColumnPositions, headerCell.getStartColumn());
                int cellColumnPos = headerCell.getStartColumn() - offsetPosition;
                Cell cell = row.createCell(cellColumnPos);
                cell.setCellValue(headerCell.getName());
                setMaxColWidth(sheet, cell, cellColumnPos);
                
                CellRangeAddress mergedHeaderCell = new CellRangeAddress(initHeaderRowOffset + i, initHeaderRowOffset + i + headerCell.getRowSpan() - 1, 
                        cellColumnPos, cellColumnPos + headerCell.getColSpan() - 1);
                if (mergedHeaderCell.getNumberOfCells()  > 1)
                    sheet.addMergedRegion(mergedHeaderCell);
                mergedCells.add(mergedHeaderCell);
                cell.setCellStyle(template.getHeaderCellStyle());
            }
        }

        for (CellRangeAddress ca : mergedCells) {
            SaikuReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
        }
    }
    
    /**
     * @param sheet
     * @param report
     */
    protected void renderReportData(SXSSFSheet sheet, GeneratedReport report) {
        if (report.reportContents.getChildren() == null) {
            renderTableRow(sheet, report, report.reportContents, 0, new ArrayList<>());
        } else {
            for (ReportArea subReportArea : report.reportContents.getChildren()) {
                renderTableRow(sheet, report, subReportArea, 0, new ArrayList<>());
            }
        }
        if (report.hasTotals()) {
            renderTableTotals(sheet, report, report.reportContents);
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
    protected void renderTableRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level,
            List<HierarchyCell> hierarchyCells) {
        if (reportContents.getChildren() != null) {
            renderGroupRow(sheet, report, reportContents, level, hierarchyCells);
        } else {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            hierarchyCells.forEach(c -> {
                Cell cell = row.createCell(c.column);
                cell.setCellValue(c.value);
                cell.setCellStyle(template.getHierarchyStyle());
                setMaxColWidth(sheet, cell, c.column);
            });
            hierarchyCells.clear();

            IntWrapper intWrapper = new IntWrapper();
            columns(report).forEach(roc -> {
                if (!(report.spec.getHierarchies().size() > 0 && intWrapper.value < level)) {
                    ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
                    createCell(sheet, row, intWrapper.value, rc);
                } 
                intWrapper.inc();
            });

            flushRows(sheet);
        }
    }

    /**
     * Flushes the rows to disk.
     *
     * @param sheet the sheet to flush
     */
    private void flushRows(SXSSFSheet sheet) {
        try {
            sheet.flushRows(IN_MEMORY_ROWS);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    protected void renderGroupRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level,
            List<HierarchyCell> hierarchyCells) {
        int rowPosInit = sheet.getLastRowNum();

        hierarchyCells.add(new HierarchyCell(level, reportContents.getOwner().debugString));

        for (ReportArea subReportArea : reportContents.getChildren()) {
            renderTableRow(sheet, report, subReportArea, level + 1, hierarchyCells);
        }

        renderSubTotalRow(sheet, report, reportContents, level);

        createHierarchyCellMergeRegion(sheet, level, rowPosInit + 1);
    }

    private void createHierarchyCellMergeRegion(SXSSFSheet sheet, int level, int startRow) {
        if (startRow < sheet.getLastRowNum()) {
            CellRangeAddress hierarchyCell = new CellRangeAddress(startRow, sheet.getLastRowNum(), level, level);
            sheet.addMergedRegion(hierarchyCell);
        }
    }

    /**
     * @param sheet
     * @param report
     * @param reportContents
     * @param level
     * @param row
     */
    protected void renderSubTotalRow(Sheet sheet, GeneratedReport report, ReportArea reportContents, int level) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        IntWrapper intWrapper = new IntWrapper();
        columns(report).forEach(roc -> {
            if (intWrapper.value > level) {
                ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
                Cell cell = createCell(sheet, row, intWrapper.value, rc);
                cell.setCellStyle(template.getSubtotalStyle(level));
            }
            intWrapper.inc();
        });
    }
    
    /**
     * @param sheet
     * @param report
     * @param reportContents
     */
    protected void renderTableTotals(Sheet sheet, GeneratedReport report, ReportArea reportContents) {
        IntWrapper intWrapper = new IntWrapper();
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        columns(report).forEach(roc -> {
            ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
            Cell cell = createTotalCell(sheet, row, intWrapper.value, report, rc);
            cell.setCellStyle(template.getTotalNumberStyle());
            intWrapper.inc();
        });
    }

    private Stream<ReportOutputColumn> columns(GeneratedReport report) {
        return report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName));
    }
    
    /**
     * @param row
     * @param i
     * @param report
     * @param rc
     * @return
     */
    protected Cell createTotalCell(Sheet sheet, Row row, int i, GeneratedReport report, ReportCell rc) {
        Cell cell = null;
        if (i == 0 && report.spec.getColumns().size() > 0) {
            cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            String value = TranslatorWorker.translateText("Report Totals");
            cell.setCellValue(value + " (" + report.reportContents.getNrEntities() + ")");
            setMaxColWidth(sheet, cell, i);
        } else {
            return createCell(sheet, row, i, rc);
        }
        
        return cell;
    }
    
    /**
     * @param row
     * @param i
     * @param rc
     * @return
     */
    protected Cell createCell(Sheet sheet, Row row, int i, ReportCell rc) {
        int cellType = getCellType(rc);
        Cell cell = row.createCell(i, cellType);
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellValue(getDoubleValue(rc));
        } else if (cellType == Cell.CELL_TYPE_STRING) {
            cell.setCellValue(getStringValue(rc));
        }
        
        setMaxColWidth(sheet, cell, i);
        
        return cell;
    }
    
    protected void setMaxColWidth(Sheet sheet, Cell cell, int i) {
        Map<Integer, Integer> widths = cachedWidths.get(sheet.getSheetName());
        IntWrapper width = new IntWrapper().inc(10);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                width.set(cell.getStringCellValue().length());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                width.set(Double.toString(cell.getNumericCellValue()).length());
                break;
        }
        
        widths.compute(i, (k, v) -> v == null ? width.value : v < width.value ? width.value : v);
    }


    protected int getCellType(ReportCell reportCell) {
        if (reportCell instanceof AmountCell) {
            return Cell.CELL_TYPE_NUMERIC;
        }
        
        return Cell.CELL_TYPE_STRING;
    }
    
    protected double getDoubleValue(ReportCell reportCell) {
        return reportCell != null ? (new Double(reportCell.value.toString()) / amountUnits.divider) : 0d;
    }
    
    protected String getStringValue(ReportCell reportCell) {
        return reportCell != null ? reportCell.displayedValue : "";
    }
    
    
    
    /**
     * Add extra info about filters applied, currency and settings.
     * 
     * @param wb
     * @param summarySheet
     * @param reportSpec
     * @param queryObject
     */
    private void generateSummarySheet(SXSSFWorkbook workbook, SXSSFSheet summarySheet, ReportSpecification reportSpec) {
        IntWrapper currLine = new IntWrapper();
        
        renderSummaryFilters(summarySheet, reportSpec, currLine);
        renderSummarySettings(summarySheet, reportSpec, currLine);
            summarySheet.trackAllColumnsForAutoSizing();
        for (int l = 0; l < 3; l++) {
            summarySheet.autoSizeColumn(l, true);
        }
    }
    
    /**
     * Add information about applied filters in the summary sheet 
     * 
     * @param summarySheet
     * @param reportSpec
     * @param currLine
     */
    private void renderSummaryFilters(Sheet summarySheet, ReportSpecification reportSpec, IntWrapper currLine) {
        // the report specification contains only IDs in filter rules. 
        // we need to export in the summary sheet the names instead of ids
        Map<String, List<String>> extractedFilters = ExportFilterUtils.getFilterValuesForIds(reportSpec.getFilters());
        
        // Create header row for filters.
        int group = 0;
        Row filterRowTitle = summarySheet.createRow(currLine.intValue());
        Cell filterTitleCell = filterRowTitle.createCell(0);
        filterTitleCell.setCellValue(TranslatorWorker.translateText("Applied Filters"));
        filterTitleCell.setCellStyle(template.getOptionSettingsStyle());
        
        for (Map.Entry<String, List<String>> filter : extractedFilters.entrySet()) {
            group = 0;
            currLine.inc();
            Row filterCategoryRow = summarySheet.createRow(currLine.intValue());
            Cell filterCategoryCell = filterCategoryRow.createCell(0);
            filterCategoryCell.setCellValue(filter.getKey());
            filterCategoryCell.setCellStyle(template.getFilterSettingsStyle());
            
            for (String filterValue : filter.getValue()) {
                // Check if the row 'i' exists so we don't add an extra row for the first filter result.
                if (summarySheet.getRow(currLine.intValue()) != null) {
                    summarySheet.getRow(currLine.intValue()).createCell(1).setCellValue(filterValue);
                } else {
                    summarySheet.createRow(currLine.intValue()).createCell(1).setCellValue(filterValue);
                }
                currLine.inc();
                group++;
            }
            if (group > 0) {
                if (group > 1) {
                    summarySheet.addMergedRegion(new CellRangeAddress(currLine.intValue() - group, currLine.intValue() - 1, 0, 0));
                }
                summarySheet.getRow(currLine.intValue() - group).getCell(0).setCellStyle(template.getHierarchyStyle());
            }
            currLine.dec();
        }
    }
    
    /**
     * 
     * Add report settings information (currency, calendar, units) in the summary sheet 
     *
     * @param summarySheet
     * @param reportSpec
     * @param currLine
     */
    private void renderSummarySettings(Sheet summarySheet, ReportSpecification reportSpec, IntWrapper currLine) {
        String currency = reportSpec.getSettings().getCurrencyCode();
        if (currency == null) {
            currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        }
        String calendar = reportSpec.getSettings().getCalendar().getName();
        String units = reportSpec.getSettings().getUnitsOption().userMessage;
        currency = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currency);
        
        if (!reportSpec.isShowOriginalCurrency()) {
            renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Currency"), currency);
        }
        
        renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Calendar"), calendar);
        renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Units"),
                TranslatorWorker.translateText(units));
    }


    public void renderSummaryLine(Sheet summarySheet, IntWrapper currLine, String title, String value) {
        currLine.inc(2);
        Row calendarRow = summarySheet.createRow(currLine.intValue());
        Cell calendarTitleCell = calendarRow.createCell(0);
        calendarTitleCell.setCellValue(TranslatorWorker.translateText(title));
        calendarTitleCell.setCellStyle(template.getOptionSettingsStyle());
        calendarRow.createCell(1).setCellValue(value);
    }
    

    /**
     * We need an alternative way to calculate the column's width because sheet.autoSizeColumn can add several minutes
     * to the process.
     * 
     * @param sheet
     * @param totalColNumber
     * @param hierarchies
     * @param headers
     */
    protected void calculateColumnsWidth(Sheet sheet, GeneratedReport report) {
        Map<Integer, Integer> sheetWidths = cachedWidths.get(sheet.getSheetName());
        
        for (int i = 0; i < report.leafHeaders.size(); i++) {
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

    protected boolean isHiddenColumn(String columnName) {
        return columnName.equals("Draft") || columnName.equals("Approval Status");
    }
    
    protected boolean hasReportGeneratedDummyColumn(GeneratedReport report) {
         return report.spec.isSummaryReport() && report.spec.getHierarchies().isEmpty();
    }
    
    private int getOffsetPositionOfHiddenColumns(List<Integer> hiddenColumnPositions, int columnPosition) {
        int offsetPosition = hiddenColumnPositions.stream().filter(
                i -> i < columnPosition)
        .collect(Collectors.toList()).size();
        
        return offsetPosition;
    }
    
    protected String getReportSheetName() {
        return this.reportSheetName;
    }
    
    protected String getSummarySheetName() {
        return this.summarySheetName;
    }

    private static class HierarchyCell {

        private int column;
        private String value;

        HierarchyCell(int column, String value) {
            this.column = column;
            this.value = value;
        }
    }
}
