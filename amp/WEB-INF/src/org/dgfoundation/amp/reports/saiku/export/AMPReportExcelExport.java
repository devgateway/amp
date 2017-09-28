package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AMPReportExcelExport {

    private static CellStyle styleHeader = null;
    private static CellStyle styleSubTotalLvl1 = null;
    private static CellStyle styleSubTotalLvl2 = null;
    private static CellStyle styleSubTotalLvl3 = null;
    private static CellStyle styleTotal = null;
    private static CellStyle styleHierarchy = null;
    private static CellStyle styleHeaderClean = null;
    private static CellStyle styleTotalClean = null;
    private static CellStyle styleNumber = null;
    private static CellStyle styleSettingOption = null;
    private static CellStyle styleSettingFilter = null;

    private static final int TYPE_STYLED = 0;
    private static final int TYPE_PLAIN = 1;

    private static final short cellHeight = 300;
    private static final float charWidth = 260;
    private static final int defaultColWidth = 25;

    private static final String xls_type_styled = "styled";
    private static final String xls_type_plain = "plain";

    private static final Logger logger = Logger.getLogger(AMPReportExcelExport.class);

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
    public static byte[] generateExcel(ReportGenerationInfo report1, ReportGenerationInfo report2) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Workbook wb = new XSSFWorkbook();
        createStyles(wb);
        addSheetsToWorkbook(wb, report1);
        if (report2 != null)
            addSheetsToWorkbook(wb, report2);
        wb.write(os);
        os.flush();
        os.close();
        logger.info("Return excel");
        return os.toByteArray();
    }

    protected static void addSheetsToWorkbook(Workbook wb, ReportGenerationInfo reportInfo) throws IOException {
        // Generate html table.
        String content = AMPJSConverter.convertToHtml(reportInfo.jb, reportInfo.type);
        // Parse the string.
        logger.info("Start Parse document.");
        Document doc = Jsoup.parse(content);
        logger.info("End Parse document.");

        Sheet styledSheet = null;
        Sheet plainSheet = null;

        String styleType = (String) reportInfo.queryModel.get(AMPReportExportConstants.EXCEL_TYPE_PARAM);
        if (styleType != null && styleType.equals(xls_type_plain)) {
            plainSheet = wb.createSheet(TranslatorWorker.translateText("Plain") + reportInfo.suffix);
        } else if (styleType != null && styleType.equals(xls_type_styled)) {
            styledSheet = wb.createSheet(TranslatorWorker.translateText("Formatted") + reportInfo.suffix);
        }
        Sheet summarySheet = wb.createSheet(TranslatorWorker.translateText("Summary Information") + reportInfo.suffix);

        generateSummarySheet(summarySheet, reportInfo.report, reportInfo.queryModel);
        if (styledSheet != null) {
            generateSheet(wb, styledSheet, doc, TYPE_STYLED, reportInfo.report);
        }
        if (plainSheet != null) {
            generateSheet(wb, plainSheet, doc, TYPE_PLAIN, reportInfo.report);
        }

        logger.info("Write excel");
    }

    /**
     * Add extra info about filters applied, currency and settings.
     * 
     * @param wb
     * @param sheet
     * @param report
     * @param queryObject
     */
    private static void generateSummarySheet(Sheet sheet, ReportSpecification report, Map<String, Object> queryModel) {
        logger.info("Start generateSummarySheet.");
        int i = 0;
        int j = 0;
        Map<String, List<String>> extractedFilters = new<String, List<String>> HashMap();
        if (queryModel.get("filtersWithModels") != null) {
            LinkedHashMap<String, Object> filtersWithModels = (LinkedHashMap<String, Object>) queryModel
                    .get("filtersWithModels");
            if (filtersWithModels.get("columnFilters") != null) {
                LinkedHashMap<String, Object> columnFilters = (LinkedHashMap<String, Object>) filtersWithModels
                        .get("columnFilters");
                for (Map.Entry<String, Object> columnFilter : columnFilters.entrySet()) {
                    String extractedFilter = TranslatorWorker.translateText(columnFilter.getKey().toString());
                    List<String> extractedValues = new ArrayList<String>();
                    for (LinkedHashMap<String, Object> columnFilterValues : (List<LinkedHashMap<String, Object>>) columnFilter
                            .getValue()) {
                        // AMP-21066
                        if (columnFilterValues != null && columnFilterValues.get("name") != null) {
                            extractedValues.add(columnFilterValues.get("name").toString());
                        } else {
                            extractedValues.add("");
                        }
                    }
                    extractedFilters.put(extractedFilter, extractedValues);
                }
            }
            if (filtersWithModels.get("otherFilters") != null) {
                LinkedHashMap<String, Object> otherFilters = (LinkedHashMap<String, Object>) filtersWithModels
                        .get("otherFilters");
                for (Map.Entry<String, Object> otherFilter : otherFilters.entrySet()) {
                    String extractedFilter = TranslatorWorker.translateText(otherFilter.getKey().toString());
                    List<String> extractedValues = new ArrayList<String>();
                    LinkedHashMap<String, Object> columnFilterValues = (LinkedHashMap<String, Object>) otherFilter
                            .getValue();
                    for (Map.Entry<String, Object> columnFilterValue : columnFilterValues.entrySet()) {
                        // AMP-21066
                        if (columnFilterValue != null && columnFilterValue.getValue() != null) {
                            extractedValues.add(columnFilterValue.getValue().toString());
                        } else {
                            extractedValues.add("");
                        }
                    }
                    extractedFilters.put(extractedFilter, extractedValues);
                }
            }
        } else if (report.getFilters() != null) {
            ReportFilters filters = report.getFilters();
            Map<ReportElement, FilterRule> filterRules = filters.getFilterRules();
            for (Map.Entry<ReportElement, FilterRule> filter : filterRules.entrySet()) {
                FilterRule filterRule = filter.getValue();
                String entityName = filter.getKey().type.name();
                if (filter.getKey().entity != null) {
                    entityName = filter.getKey().entity.getEntityName();
                }
                String extractedFilter = TranslatorWorker.translateText(entityName);
                List<String> extractedValues = new ArrayList<String>();
//              for (Map.Entry<String, String> filterValue : filterRule.valueToName.entrySet()) {
//                  // AMP-21066
//                  if (filterValue != null) {
//                      extractedValues.add(filterValue.getValue());
//                  } else {
//                      extractedValues.add("");
//                  }
//              }
                extractedFilters.put(extractedFilter, extractedValues);
            }
        }

        // Create header row for filters.
        int group = 0;
        Row filterRowTitle = sheet.createRow(i);
        Cell filterTitleCell = filterRowTitle.createCell(0);
        filterTitleCell.setCellValue(TranslatorWorker.translateText("Applied Filters"));
        filterTitleCell.setCellStyle(styleSettingOption);
        for (Map.Entry<String, List<String>> filter : extractedFilters.entrySet()) {
            group = 0;
            i++;
            Row filterCategoryRow = sheet.createRow(i);
            Cell filterCategoryCell = filterCategoryRow.createCell(j);
            filterCategoryCell.setCellValue(filter.getKey());
            filterCategoryCell.setCellStyle(styleSettingFilter);
            for (String filterValue : filter.getValue()) {
                // Check if the row 'i' exists so we dont add an extra row for the first filter result.
                if (sheet.getRow(i) != null) {
                    sheet.getRow(i).createCell(j + 1).setCellValue(filterValue);
                } else {
                    sheet.createRow(i).createCell(j + 1).setCellValue(filterValue);
                }
                i++;
                group++;
            }
            if (group > 0) {
                sheet.addMergedRegion(new CellRangeAddress(i - group, i - 1, 0, 0));
                sheet.getRow(i - group).getCell(j).setCellStyle(styleHierarchy);
            }
            i--;
        }

        i += 2;
        j = 0;
        String currency = report.getSettings().getCurrencyCode();
        if (currency == null) {
            // we get the default currency
            currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        }       
        String calendar = report.getSettings().getCalendar().getName();
        if (queryModel.containsKey("settings")) {
            LinkedHashMap<String, Object> settings = (LinkedHashMap<String, Object>) queryModel.get("settings");
            currency = settings.get(SettingsConstants.CURRENCY_ID).toString();
            if (settings.containsKey(SettingsConstants.CALENDAR_TYPE_ID)) {
                calendar = FiscalCalendarUtil.getAmpFiscalCalendar(
                        new Long(settings.get(SettingsConstants.CALENDAR_TYPE_ID).toString())).getName();
            }
        }
        // AMP-21951: workaround to hide calendar id
        currency = ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(currency);
        Row currencyRow = sheet.createRow(i);
        Cell currencyTitleCell = currencyRow.createCell(j);
        currencyTitleCell.setCellValue(TranslatorWorker.translateText("Currency"));
        currencyTitleCell.setCellStyle(styleSettingOption);
        currencyRow.createCell(j + 1).setCellValue(currency);

        i += 2;
        j = 0;
        Row calendarRow = sheet.createRow(i);
        Cell calendarTitleCell = calendarRow.createCell(j);
        calendarTitleCell.setCellValue(TranslatorWorker.translateText("Calendar"));
        calendarTitleCell.setCellStyle(styleSettingOption);
        calendarRow.createCell(j + 1).setCellValue(calendar);

        i += 2;
        j = 0;
        Row unitsRow = sheet.createRow(i);
        Cell unitsTitleCell = unitsRow.createCell(j);
        unitsTitleCell.setCellValue(TranslatorWorker.translateText("Units"));
        unitsTitleCell.setCellStyle(styleSettingOption);
        String units = report.getSettings().getUnitsOption().userMessage;
        unitsRow.createCell(j + 1).setCellValue(TranslatorWorker.translateText(units));

        for (int l = 0; l < 3; l++) {
            sheet.autoSizeColumn(l, true);
        }
        logger.info("End generateSummarySheet.");
    }

    private static void generateSheet(Workbook wb, Sheet sheet, Document doc, int type, ReportSpecification report) {
        int columns = report.getColumns().size();
        int hierarchies = report.getHierarchies().size();
        boolean isSummaryReport = hierarchies == columns;
        logger.info("Start generateSheet " + sheet.getSheetName());
        Map<Integer, Integer> widths = new TreeMap<Integer, Integer>();     
        // Process header.
        Element headerRows = doc.getElementsByTag("thead").first();
        int i = 0;
        int headers;
        int totalColNumber = 0;
        for (Element headerRowElement : headerRows.getElementsByTag("tr")) {
            Row row = sheet.createRow(i);
            int j = 0;
            int colSpan;
            for (Element headerColElement : headerRowElement.getElementsByTag("th")) {
                Cell cell = row.createCell(j);
                String cellContent = "";
                if (headerColElement.hasClass("all_null")) {
                    // Empty header cell, do nothing.
                } else if (headerColElement.hasClass("col")) {
                    // This is a header cell with a <div> inside.
                    cellContent = ((Element) headerColElement.getElementsByTag("div").toArray()[0]).text();
                    if (headerColElement.hasAttr("colspan")) {
                        colSpan = Integer.valueOf(headerColElement.attr("colspan").toString()).intValue();
                        if (type == TYPE_PLAIN) {
                            // "Unmerge" these header columns.
                            if (colSpan > 1) {
                                for (int k = 1; k < colSpan; k++) {
                                    Cell auxCell = row.createCell(j + k);
                                    auxCell.setCellValue(cellContent);
                                    auxCell.setCellStyle(styleHeaderClean);
                                }
                                j += colSpan - 1;
                            }
                        } else {
                            // If this cell is being grouped with other cells on its right we merge them.
                            if (colSpan > 1) {
                                for (int k = 0; k < colSpan; k++) {
                                    // Create the merged cells to avoid problems.
                                    Cell mergedCell = row.createCell(j + k);
                                    mergedCell.setCellStyle(styleHeader);
                                }
                                sheet.addMergedRegion(new CellRangeAddress(i, i, j, j + colSpan - 1));
                                j += colSpan - 1;
                            }
                        }
                    }
                    cell.setCellValue(cellContent);
                    if (type == TYPE_PLAIN) {
                        cell.setCellStyle(styleHeaderClean);
                    } else {
                        cell.setCellStyle(styleHeader);
                    }
                }
                setMaxColWidth(widths, cell, j);
                j++;
                totalColNumber++;
            }
            i++;
        }
        headers = i;

        // Check special case when summarized report has only 1 column and 1 row for "report totals".
        String reportTotalsString = TranslatorWorker.translateText("Report Totals");
        // Create a map with the styles we will be cloning so we can add or change them (without breaking the WorkBook
        // with too many styles).
        Map<String, CellStyle> clonedStyles = new HashMap<String, CellStyle>();
        String clonedSufixAlignRight = "_align_right";
        // Process data.
        Element contentRows = doc.getElementsByTag("tbody").first();
        int totalRows = contentRows.getElementsByTag("tr").size() + i - 1;
        for (Element contentRowElement : contentRows.getElementsByTag("tr")) {
            Row row = sheet.createRow(i);
            CellStyle styleForCurrentRow = null;
            int j = 0;
            Elements elements = contentRowElement.getElementsByTag("th");
            elements.addAll(contentRowElement.getElementsByTag("td"));
            for (Element contentColElement : elements) {
                boolean isNumber = false;
                Cell cell = row.createCell(j);
                String cellContent = ((Element) contentColElement).text();
                if (contentColElement.getElementsByClass("data").size() != 0 
                        && !reportTotalsString.equals(cellContent)) {
                    // Measure columns (in theory).
                    isNumber = true;
                    
                    // Workaround since we cant trust the columns variable (sometimes we have less columns in the report) we need an extra check for the last row (Totals).
                    // This problem exist because in excel sometimes we have to show "" for numbers and sometimes 0.
                    if (i == totalRows) {
                        if(detectNumericColumn(contentRows, j, headers, columns)) {
                            isNumber = true;
                            setNumericValueToCell(cellContent, cell);
                        } else {
                            isNumber = false;
                            cell.setCellValue(cellContent);
                        }
                    } else {
                        setNumericValueToCell(cellContent, cell);
                    }
                } else {
                    // Regular columns.
                    isNumber = false;
                    cell.setCellValue(cellContent);
                }

                boolean thisIsTotalsRow = i == totalRows && contentColElement.hasClass("total");
                if (thisIsTotalsRow) {
                    // Style last total row.
                    if (type == TYPE_STYLED) {
                        styleForCurrentRow = styleTotal;
                    } else {
                        styleForCurrentRow = styleTotalClean;
                    }
                } else if (contentColElement.hasClass("total") || contentColElement.hasClass("row_total")) {
                    if (type == TYPE_STYLED) {
                        // Start applying the subtotal style in the right column (not the first).
                        if (styleForCurrentRow == null && !cellContent.equals("")) {
                            switch (j) {
                            case 0:
                                styleForCurrentRow = styleSubTotalLvl1;
                                break;
                            case 1:
                                styleForCurrentRow = styleSubTotalLvl2;
                                break;
                            case 2:
                                styleForCurrentRow = styleSubTotalLvl3;
                                break;
                            }
                        }
                    } else {
                        // This is a "clean" style we use to mark total rows in plain export.
                        styleForCurrentRow = styleTotalClean;
                    }
                }
                if (styleForCurrentRow != null) {
                    cell.setCellStyle(styleForCurrentRow);
                }
                if (isNumber) {
                    // If this is a number then we need to keep the current format style (if any) and apply some extra
                    // properties like 'align', if we create a new style for each cell that will break POI so we will
                    // use a map to cache the few new styles we will create and use.
                    if (styleForCurrentRow != null) {
                        String key = styleForCurrentRow.toString() + clonedSufixAlignRight;
                        if (clonedStyles.containsKey(key)) {
                            CellStyle auxStyle = clonedStyles.get(key);
                            cell.setCellStyle(auxStyle);
                        } else {
                            CellStyle auxStyle = wb.createCellStyle();
                            auxStyle.cloneStyleFrom(cell.getCellStyle());
                            auxStyle.setAlignment(CellStyle.ALIGN_RIGHT);
                            cell.setCellStyle(auxStyle);
                            clonedStyles.put(key, auxStyle);
                        }
                    } else {
                        // Use a predefined basic number style with the align we need (and more properties if we need in
                        // the future).
                        cell.setCellStyle(styleNumber);
                    }
                }
                setMaxColWidth(widths, cell, j);
                j++;
            }
            row.setHeight(cellHeight);
            i++;
        }

        // If this is a "summary report" we need to "ignore" the last hierarchy in the post-processing.
        if (isSummaryReport) {
            hierarchies--;
        }

        // Postprocess according to sheet type.
        switch (type) {
        case TYPE_STYLED:
            if (!report.getGroupingCriteria().equals(GroupingCriteria.GROUPING_TOTALS_ONLY)) {
                mergeHierarchyRows(sheet, hierarchies, headers);
            }
            break;
        case TYPE_PLAIN:
            if (!report.getGroupingCriteria().equals(GroupingCriteria.GROUPING_TOTALS_ONLY)) {
                refillHierarchyRows(sheet, hierarchies, headers);
                deleteHierarchyTotalRows(sheet, hierarchies, headers, columns);
            }
            break;
        }

        calculateColumnsWidth(sheet, sheet.getRow(0).getPhysicalNumberOfCells(), widths);
        logger.info("End generateSheet " + sheet.getSheetName());
    }
    
    /**
     * Try to determine if a column is numeric (measure column or computed measure column) by the data on it.
     * If this fails another way to test it is agains the previously generated Excel cells (its data type) instead of the html.
     * @param contentRows
     * @param colIndex
     * @param headerRows
     * @param numberOfColumns
     * @return
     */
    private static boolean detectNumericColumn(Element contentRows, int colIndex, int headerRows, int numberOfColumns) {
        boolean isNumeric = false;
        boolean failedBefore = false;
        Elements rows = contentRows.getElementsByTag("tr");
        for (int i = 0; i < rows.size(); i++) {
            if (i >= headerRows) {
                Element row = rows.get(i);
                Elements columns = row.getElementsByTag("th");
                columns.addAll(row.getElementsByTag("td"));
                for (int j = 0; j < columns.size(); j++) {
                    if (j == colIndex) {
                        Element col = columns.get(j);
                        String text = col.text();
                        // If text is "" or "null" we still cant decide if is a number column or not, but new Double("") will fail.
                        if (!text.equals("") && !text.equals("null")) {
                            try {
                                //new Double(text); //Doesnt work for some number formats.
                                if (NumberUtils.isNumber(text)) {
                                    if (!failedBefore) {
                                        isNumeric = true;
                                    }
                                }
                            } catch (Exception e) {
                                isNumeric = false;
                                failedBefore = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        // Still at this point could be a false negative due to a report with few rows. 
        // So we will assume that after numberOfColumns is a numeric column (measure).
        if (!failedBefore && colIndex >= numberOfColumns) {
            isNumeric = true;
        }
        return isNumeric;
    }
    
    private static void setNumericValueToCell(String cellContent, Cell cell) {
        boolean emptyAsZero = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.REPORTS_EMPTY_VALUES_AS_ZERO_XLS);
        try {
            Double doubleNumber = new Double(cellContent);
            if (doubleNumber.equals(0d)) {
                if (emptyAsZero) {
                    cell.setCellValue(doubleNumber);
                } else {
                    cell.setCellValue("");
                }
            } else {
                cell.setCellValue(doubleNumber);
            }
        } catch (NumberFormatException e) {
            if (emptyAsZero) {
                cell.setCellValue(new Double(0));
            } else {
                cell.setCellValue("");
            }
        }
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
    private static void calculateColumnsWidth(Sheet sheet, int totalColNumber, Map<Integer, Integer> widths) {
        for (int i = 0; i < totalColNumber; i++) {
            try {
                if (widths.containsKey(i)) {
                    sheet.setColumnWidth(i, (int) (widths.get(i) * charWidth));
                } else {
                    sheet.setColumnWidth(i, (int) (defaultColWidth * charWidth));
                }
            } catch (Exception e) {
                // Alternative slow method.
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * Delete hierarchy total rows (useful for plain excel export).
     * 
     * @param sheet
     * @param hierarchies
     * @param headers
     */
    private static void deleteHierarchyTotalRows(Sheet sheet, int hierarchies, int headers, int columns) {
        logger.info("Start deleteHierarchyTotalRows");
        if (sheet.getRow(0) != null) {
            boolean deleted = false;
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (columns > hierarchies) {
                // Iterate columns from right to left, then rows from bottom to top.
                // Notice 'j = hierarchies' instead of 'j = hierarchies - 1' because we start on the first non
                // hierarchical column to get the last hierarchy empty total cells.
                for (int j = hierarchies; j > 0; j--) {
                    for (int i = sheet.getPhysicalNumberOfRows() - 2; i >= headers - 1; i--) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            if (row.getCell(j).getStringCellValue().isEmpty()) {
                                CellStyle style = row.getCell(j).getCellStyle();
                                // We need this extra check for reports where the first non hierarchical column has
                                // empty cells (it happens on Niger).
                                if (style.getIndex() == styleTotalClean.getIndex()) {
                                    sheet.removeRow(row);
                                    deleted = true;
                                }
                            }
                        }
                    }
                }
                // Now shift rows to fill the gaps created by removing rows with POI.
                if (deleted) {
                    int shift = 0;
                    for (int i = headers; i < totalRows; i++) {
                        Row row = sheet.getRow(i);
                        // row is null when was deleted by POI, but still exists in the sheet.
                        if (row == null || row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) {
                            shift++;
                        } else {
                            if (shift > 0) {
                                // Move up this row 'shift' positions and restart the loop.
                                sheet.shiftRows(i, i, -1 * shift);
                                i = headers;
                                shift = 0;
                            }
                        }
                    }
                }
            }
        }
        logger.info("End deleteHierarchyTotalRows");
    }

    /**
     * Postprocess the sheet by filling empty hierarchy cells with the right value.
     * 
     * @param sheet
     * @param hierarchies
     * @param headers
     */
    private static void refillHierarchyRows(Sheet sheet, int hierarchies, int headers) {
        logger.info("Start refillHierarchyRows");
        if (sheet.getRow(0) != null) {
            String prevCellValue = null;
            // Iterate columns then rows.
            for (int j = 0; j < hierarchies; j++) {
                for (int i = headers; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                    if (prevCellValue == null) {
                        // Beginning a hierarchy.
                        prevCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                    } else {
                        String currentCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                        if (currentCellValue.isEmpty()) {
                            // Empty cell means we are in the same hierarchy.
                            sheet.getRow(i).getCell(j).setCellValue(prevCellValue);
                        } else {
                            // We reached the end of the hierarchy (its the total row).
                            prevCellValue = null;
                            if (j > 0) {
                                // If this is not the first column then we need to check if below this 'total' row there
                                // are 1 or more rows that are also the 'total' for a higher hierarchy (ie: sector ->
                                // sub sector -> sub sub sector, etc).
                                int mustSkipRows = 0;
                                for (int k = 1; k < hierarchies; k++) {
                                    if (sheet.getRow(i + k).getCell(j).getStringCellValue().isEmpty()) {
                                        mustSkipRows++;
                                    } else {
                                        break;
                                    }
                                }
                                i += mustSkipRows;
                            }
                        }
                    }
                }
            }
        }
        logger.info("End refillHierarchyRows");
    }

    /**
     * Postprocess the sheet by merging vertically all empty hierarchy cells.
     * 
     * @param sheet
     * @param hierarchies
     * @param headers
     */
    private static void mergeHierarchyRows(Sheet sheet, int hierarchies, int headers) {
        logger.info("Start mergeHierarchyRows");
        if (sheet.getRow(0) != null) {
            String prevCellValue = null;
            int group = 0;
            int groupStart = 0;
            // Iterate columns then rows.
            for (int j = 0; j < hierarchies; j++) {
                for (int i = headers; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                    if (prevCellValue == null) {
                        // Beginning a hierarchy.
                        prevCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                        group = 0;
                        groupStart = i;
                    } else {
                        String currentCellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                        if (currentCellValue.isEmpty()) {
                            // Empty cell means we are in the same hierarchy.
                            group++;
                        } else {
                            // We reached the end of the hierarchy (its the total row).
                            if (group > 0) {
                                sheet.addMergedRegion(new CellRangeAddress(groupStart, groupStart + group, j, j));
                                sheet.getRow(groupStart).getCell(j).setCellStyle(styleHierarchy);
                            }
                            prevCellValue = null;
                            if (j > 0) {
                                // If this is not the first column then we need to check if below this 'total' row there
                                // are 1 or more rows that are also the 'total' for a higher hierarchy (ie: sector ->
                                // sub sector -> sub sub sector, etc).
                                int mustSkipRows = 0;
                                for (int k = 1; k < hierarchies; k++) {
                                    if (sheet.getRow(i + k).getCell(j).getStringCellValue().isEmpty()) {
                                        mustSkipRows++;
                                    } else {
                                        break;
                                    }
                                }
                                i += mustSkipRows;
                            }
                        }
                    }
                }
            }
        }
        logger.info("End mergeHierarchyRows");
    }

    /**
     * Define all styles we will use in the Workbook.
     * 
     * @param wb
     */
    private static void createStyles(Workbook wb) {
        logger.info("Create Excel styles");
        Font fontHeaderAndTotal = wb.createFont();
        fontHeaderAndTotal.setColor(IndexedColors.BLACK.getIndex());
        Font fontBold = wb.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // fontHeaderAndTotal.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleHeader = wb.createCellStyle();
        styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleHeader.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
        styleHeader.setWrapText(true);
        styleHeader.setBorderTop(CellStyle.BORDER_THIN);
        styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
        styleHeader.setBorderRight(CellStyle.BORDER_THIN);
        styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
        styleHeader.setFont(fontHeaderAndTotal);

        styleHeaderClean = wb.createCellStyle();
        styleHeaderClean.setAlignment(CellStyle.ALIGN_CENTER);
        styleHeaderClean.setWrapText(true);
        styleHeaderClean.setFont(fontHeaderAndTotal);

        styleTotal = wb.createCellStyle();
        styleTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleTotal.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleTotal.setAlignment(CellStyle.ALIGN_CENTER);
        styleTotal.setFont(fontHeaderAndTotal);
        styleTotal.setWrapText(true);

        // Important: DO NOT change this style (is used as a marker for total rows in plain export).
        styleTotalClean = wb.createCellStyle();
        styleTotalClean.setAlignment(CellStyle.ALIGN_LEFT);
        styleTotalClean.setFont(fontHeaderAndTotal);
        styleTotalClean.setWrapText(true);

        styleSubTotalLvl1 = wb.createCellStyle();
        styleSubTotalLvl1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleSubTotalLvl1.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        styleSubTotalLvl1.setAlignment(CellStyle.ALIGN_LEFT);
        styleSubTotalLvl1.setWrapText(true);
        styleSubTotalLvl1.setFont(fontHeaderAndTotal);

        styleSubTotalLvl2 = wb.createCellStyle();
        styleSubTotalLvl2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleSubTotalLvl2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        styleSubTotalLvl2.setAlignment(CellStyle.ALIGN_LEFT);
        styleSubTotalLvl2.setWrapText(true);
        styleSubTotalLvl2.setFont(fontHeaderAndTotal);

        styleSubTotalLvl3 = wb.createCellStyle();
        styleSubTotalLvl3.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleSubTotalLvl3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleSubTotalLvl3.setAlignment(CellStyle.ALIGN_LEFT);
        styleSubTotalLvl3.setWrapText(true);
        styleSubTotalLvl3.setFont(fontHeaderAndTotal);

        styleHierarchy = wb.createCellStyle();
        styleHierarchy.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        styleNumber = wb.createCellStyle();
        styleNumber.setAlignment(CellStyle.ALIGN_RIGHT);

        styleSettingOption = wb.createCellStyle();
        styleSettingOption.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleSettingOption.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleSettingOption.setFont(fontBold);

        styleSettingFilter = wb.createCellStyle();
    }

    private static void setMaxColWidth(Map<Integer, Integer> widths, Cell cell, int i) {
        int currentWidth = 10;
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            currentWidth = cell.getStringCellValue().length();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            currentWidth = Double.toString(cell.getNumericCellValue()).length();
            break;
        }
        if (widths.containsKey(i)) {
            if (currentWidth > widths.get(i)) {
                widths.put(i, currentWidth);
            }
        } else {
            widths.put(i, currentWidth);
        }
    }
}
