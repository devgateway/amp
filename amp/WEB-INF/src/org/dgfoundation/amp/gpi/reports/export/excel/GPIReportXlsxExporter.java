package org.dgfoundation.amp.gpi.reports.export.excel;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.export.GPIReportExporter;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.reports.saiku.export.ExportFilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportXlsxExporter implements GPIReportExporter {

    protected GPIReportExcelTemplate template;

    public String reportSheetName = "Indicator";
    public String summarySheetName = "Summary Information";

    public final int currencyUnitsRowPosition = 1;
    public int initSummaryRowOffset = 3;
    public int initHeaderRowOffset = 6;

    protected static final int IN_MEMORY_ROWS = 10;

    /**
     * Map<String sheetName, Map<Integer header-column-number, Integer *
     * columnWidth>>
     */
    protected final Map<String, Map<Integer, Integer>> cachedWidths = new HashMap<String, Map<Integer, Integer>>();

    private static final Logger logger = Logger.getLogger(GPIReportXlsxExporter.class);

    protected AmountsUnits amountUnits = null;
    
    protected CalendarConverter calendarConverter;

    /**
     * generates a workbook containing data about GPI report
     * 
     * @param report
     */
    @Override
    public byte[] exportReport(GPIReport report) throws Exception {
        logger.info("Start generating Excel Report...");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SXSSFWorkbook wb = new SXSSFWorkbook(-1);

        template = new GPIReportExcelTemplate(wb);
        
        ReportSettings reportSettings = report.getSpec().getSettings();
        calendarConverter = (reportSettings != null && reportSettings.getCalendar() != null) 
                ? reportSettings.getCalendar() : AmpARFilter.getDefaultCalendar();

        addAllSheetsToWorkbook(report, wb);

        wb.write(os);
        os.flush();
        os.close();
        wb.dispose();
        logger.info("Stop generating GPI Excel Report.");

        return os.toByteArray();
    }

    /**
     * @param report
     * @param wb
     */
    protected void addAllSheetsToWorkbook(GPIReport report, SXSSFWorkbook wb) {
        addReportSheetToWorkbook(wb, report, getReportSheetName());
        addSummarySheetToWorkbook(wb, report, getSummarySheetName());
    }

    /**
     * @param wb
     * @param report
     * @param sheetName
     */
    protected void addReportSheetToWorkbook(SXSSFWorkbook wb, GPIReport report, String sheetName) {
        SXSSFSheet reportSheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
        cachedWidths.computeIfAbsent(reportSheet.getSheetName(), k -> new HashMap<Integer, Integer>());

        generateReportSheet(wb, reportSheet, report);
        postProcessGeneratedSheet(reportSheet, report);
    }

    protected void addSummarySheetToWorkbook(SXSSFWorkbook wb, GPIReport report, String sheetName) {
        SXSSFSheet summarySheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
        generateSummarySheet(wb, summarySheet, report);
    }

    protected void postProcessGeneratedSheet(Sheet sheet, GPIReport report) {
        calculateColumnsWidth(sheet, report);
    }

    protected void generateReportSheet(SXSSFWorkbook wb, SXSSFSheet sheet, GPIReport report) {
        renderReportTableUnits(wb, sheet, report);
        renderReportTableSummary(wb, sheet, report);
        renderReportTableHeader(wb, sheet, report);
    
        if (report.getPage().getContents() != null && !report.getPage().getContents().isEmpty()) {
            renderReportData(sheet, report);
        }
    }

    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableUnits(SXSSFWorkbook wb, SXSSFSheet sheet, GPIReport report) {
        Row row = sheet.createRow(currencyUnitsRowPosition);
        Cell cell = row.createCell(0);

        amountUnits = report.getSpec().getSettings().getUnitsOption();
        String unitsOption = amountUnits.userMessage;
        String currencyCode = report.getSettings().getCurrencyCode();
        currencyCode = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currencyCode);

        String translatedNotes = TranslatorWorker.translateText(unitsOption);
        String translatedCurrencyCode = TranslatorWorker.translateText(currencyCode);

        cell.setCellValue(translatedNotes + " - " + translatedCurrencyCode);
        cell.setCellStyle(template.getDefaultStyle());

        CellRangeAddress mergedUnitsCell = new CellRangeAddress(currencyUnitsRowPosition, currencyUnitsRowPosition, 0,
                report.getPage().getHeaders().size() + 1);

        if (mergedUnitsCell.getNumberOfCells() > 1)
            sheet.addMergedRegion(mergedUnitsCell);
    }

    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableHeader(Workbook wb, Sheet sheet, GPIReport report) {
    }
    
    /**
     * @param wb
     * @param sheet
     * @param report
     */
    protected void renderReportTableSummary(Workbook wb, Sheet sheet, GPIReport report) {
    }

    /**
     * @param sheet
     * @param report
     */
    protected void renderReportData(SXSSFSheet sheet, GPIReport report) {
    }

    /**
     * 
     * @param report
     * @param sheet
     * @param row
     * @param i
     * @param column
     * @param value
     * @return
     */
    public Cell createCell(GPIReport report, Sheet sheet, Row row, int i, String columnName, String value) {
        int cellType = getCellType(columnName);
        Cell cell = row.createCell(i, cellType);
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            DecimalFormat df = report.getSpec().getSettings().getCurrencyFormat();
            double val = 0;
            try {
                val = df.parse(value).doubleValue();
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            cell.setCellValue(val);
        } else if (cellType == Cell.CELL_TYPE_STRING) {
            cell.setCellValue(value);
        }
        
        if (hasSpecificStyle(columnName)) {
            cell.setCellStyle(getSpecificStyle(columnName));
        } else {
            cell.setCellStyle(template.getDefaultStyle());
        }
        
        setMaxColWidth(sheet, cell, i);

        return cell;
    }

    protected void setMaxColWidth(Sheet sheet, Cell cell, int i) {
        Map<Integer, Integer> widths = cachedWidths.get(sheet.getSheetName());
        IntWrapper width = new IntWrapper().inc(10);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                width.set(Double.toString(cell.getNumericCellValue()).length());
                break;
            default:
                int length = cell.getStringCellValue().length();
                width.set(length < width.value ? width.value : length);
        }

        widths.compute(i, (k, v) -> v == null ? width.value : v < width.value ? width.value : v);
    }

    public int getCellType(String columnName) {
        return Cell.CELL_TYPE_STRING;
    }
    
    protected boolean hasSpecificStyle(String columnName) {
        return false;
    }
    
    protected CellStyle getSpecificStyle(String columnName) {
        return template.getNumberStyle();
    }
    
    protected boolean isHiddenColumn(String columnName) {
        return false;
    }

    /**
     * Add extra info about filters applied, currency and settings.
     * 
     * @param wb
     * @param summarySheet
     * @param report
     */
    protected void generateSummarySheet(SXSSFWorkbook workbook, SXSSFSheet summarySheet, GPIReport report) {
        IntWrapper currLine = new IntWrapper();

        renderSummaryFilters(summarySheet, report, currLine);
        renderSummarySettings(summarySheet, report, currLine);
        summarySheet.trackAllColumnsForAutoSizing();

        for (int l = 0; l < 3; l++) {
            summarySheet.autoSizeColumn(l, true);
        }
    }

    /**
     * Add information about applied filters in the summary sheet
     * 
     * @param summarySheet
     * @param report
     * @param currLine
     */
    private void renderSummaryFilters(Sheet summarySheet, GPIReport report, IntWrapper currLine) {
        // the report specification contains only IDs in filter rules.
        // we need to export in the summary sheet the names instead of ids
        ReportFilters filters = report.getSpec().getFilters();
        Map<String, List<String>> extractedFilters = ExportFilterUtils.getFilterValuesForIds(filters);

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
                // Check if the row 'i' exists so we don't add an extra row for
                // the first filter result.
                Row row = summarySheet.getRow(currLine.intValue());
                
                if (summarySheet.getRow(currLine.intValue()) == null) {
                    row = summarySheet.createRow(currLine.intValue());
                }
                Cell filterCell = row.createCell(1);
                filterCell.setCellValue(filterValue);
                filterCell.setCellStyle(template.getDefaultStyle());
                currLine.inc();
                group++;
            }
            if (group > 0) {
                if (group > 1) {
                    summarySheet.addMergedRegion(
                            new CellRangeAddress(currLine.intValue() - group, currLine.intValue() - 1, 0, 0));
                }
                summarySheet.getRow(currLine.intValue() - group).getCell(0).setCellStyle(template.getHierarchyStyle());
            }
            currLine.dec();
        }
    }

    /**
     * 
     * Add report settings information (currency, calendar, units) in the
     * summary sheet
     *
     * @param summarySheet
     * @param reportSpec
     * @param currLine
     */
    protected void renderSummarySettings(Sheet summarySheet, GPIReport report, IntWrapper currLine) {
        String currency = report.getSettings().getCurrencyCode();
        if (currency == null) {
            currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        }
        String calendar = report.getSpec().getSettings().getCalendar().getName();
        String units = report.getSpec().getSettings().getUnitsOption().userMessage;
        currency = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currency);

        renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Currency"), currency);
        renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Calendar"), calendar);
        renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Units"), units);
    }

    public void renderSummaryLine(Sheet summarySheet, IntWrapper currLine, String title, String value) {
        currLine.inc(2);
        Row calendarRow = summarySheet.createRow(currLine.intValue());
        Cell calendarTitleCell = calendarRow.createCell(0);
        calendarTitleCell.setCellValue(TranslatorWorker.translateText(title));
        calendarTitleCell.setCellStyle(template.getOptionSettingsStyle());
        
        Cell calendarCell = calendarRow.createCell(1);
        calendarCell.setCellValue(value);
        calendarCell.setCellStyle(template.getDefaultStyle());
    }

    /**
     * We need an alternative way to calculate the column's width because
     * sheet.autoSizeColumn can add several minutes to the process.
     * 
     * @param sheet
     * @param totalColNumber
     * @param hierarchies
     * @param headers
     */
    protected void calculateColumnsWidth(Sheet sheet, GPIReport report) {
        Map<Integer, Integer> sheetWidths = cachedWidths.get(sheet.getSheetName());

        for (int i = 0; i < report.getPage().getHeaders().size(); i++) {
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
    
    protected String getReportSheetName() {
        return reportSheetName;
    }

    protected String getSummarySheetName() {
        return summarySheetName;
    }
}
