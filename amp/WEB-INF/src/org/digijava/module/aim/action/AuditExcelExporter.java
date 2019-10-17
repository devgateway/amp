package org.digijava.module.aim.action;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.versioning.ActivityComparisonResult;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;

public  class AuditExcelExporter {
    private static final Integer CELL_LIMIT = 32767;
    private static int rowIndex = 1;
    private static int cellIndex = 0;
    private static final Integer FIRST_COLUMN = 0;
    private static final Integer LAST_COLUMN = 2;
    private static final Integer VALUE_NAME = 0;
    AuditXLSExportService auditXLSExportService;
    public AuditExcelExporter() {
        auditXLSExportService = new AuditXLSExportService();
    }

    public HSSFWorkbook generateExcel(List<ActivityComparisonResult> outputCollection) {

        HSSFSheet sheet = createWorkbook();
        for (ActivityComparisonResult result : outputCollection) {
            cellIndex = 0;
            String name = result.getName();
            HSSFRow nameRow = sheet.createRow(rowIndex);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, FIRST_COLUMN, LAST_COLUMN));
            rowIndex++;
            HSSFCell nameCell = nameRow.createCell(cellIndex);
            nameCell.setCellValue(name);
            nameCell.setCellStyle(auditXLSExportService.getOrCreateTitleStyle(sheet.getWorkbook()));
            Map<String, List<CompareOutput>> outputCollectionGrouped = result.getCompareOutput();
            rowIndex = getCellValues(outputCollectionGrouped, sheet, rowIndex);
        }
        auditXLSExportService.setColumnWidth(sheet);
        return sheet.getWorkbook();
    }

    public HSSFRow createHeader(HSSFWorkbook wb, HSSFSheet sheet) {
        HSSFCellStyle titleCS = auditXLSExportService.getOrCreateTitleStyle(wb);

        rowIndex = 0;
        cellIndex = 0;

        HSSFRow titleRow = sheet.createRow(rowIndex++);

        HSSFCell titleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString title = new HSSFRichTextString(
                TranslatorWorker.translateText("Value Name", TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()));
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleCS);

        HSSFCell cellName = titleRow.createCell(cellIndex++);
        HSSFRichTextString previous = new HSSFRichTextString(
                TranslatorWorker.translateText("Previous Version", TLSUtils.getEffectiveLangCode(),
                        TLSUtils.getSiteId()));
        cellName.setCellValue(previous);
        cellName.setCellStyle(titleCS);

        HSSFCell cellNew = titleRow.createCell(cellIndex++);
        HSSFRichTextString newVersion = new HSSFRichTextString(
                TranslatorWorker.translateText("New Version", TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()));
        cellNew.setCellValue(newVersion);
        cellNew.setCellStyle(titleCS);

        return titleRow;
    }

    public int checkMaxCellLimit(String cellValue, HSSFSheet sheet, int rowIndex, int cellIndex, HSSFCell cell) {
        int length = cellValue.length();
        int mergeIndex = rowIndex;
        if (length > CELL_LIMIT) {
            String valueOne = cellValue.substring(0, CELL_LIMIT);
            cell.setCellValue(valueOne);
            int remain = length - CELL_LIMIT;
            for (int i = CELL_LIMIT; i < length; i += CELL_LIMIT) {
                String valueNext = null;
                HSSFRow newvalueRow = sheet.createRow(++rowIndex);
                HSSFCell valuescell = newvalueRow.createCell(cellIndex);
                if (remain < CELL_LIMIT) {
                    valueNext = cellValue.substring(i);
                } else {
                    valueNext = cellValue.substring(i, i + CELL_LIMIT);
                }
                valuescell.setCellValue(valueNext);
                HSSFWorkbook wb = sheet.getWorkbook();
                valuescell.setCellStyle(auditXLSExportService.getOrCreateOrdinaryStyle(wb));
                remain = length - (i + CELL_LIMIT);
            }
            sheet.addMergedRegion(new CellRangeAddress(mergeIndex, rowIndex, FIRST_COLUMN, FIRST_COLUMN));
        } else {
            cell.setCellValue(cellValue);
        }
        return rowIndex;
    }

    public HSSFSheet createWorkbook() {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(TranslatorWorker.translateText("Audit Logger"));
        createHeader(wb, sheet);
        return sheet;
    }

    public int getCellValues(Map<String, List<CompareOutput>> outputCollectionGrouped, HSSFSheet sheet,
                             int rowIndex) {

        HSSFWorkbook wb = sheet.getWorkbook();
        HSSFCellStyle cs = auditXLSExportService.getOrCreateOrdinaryStyle(wb);
        Set<String> keyset = outputCollectionGrouped.keySet();
        for (String key : keyset) {
            cellIndex = 0;
            HSSFRow valueRow = sheet.createRow(rowIndex);
            HSSFCell colcell = valueRow.createCell(cellIndex++);
            colcell.setCellValue(key);
            colcell.setCellStyle(cs);

            List<CompareOutput> nameList = outputCollectionGrouped.get(key);
            CompareOutput comp = nameList.get(VALUE_NAME);
            HSSFCell groupcell = valueRow.createCell(cellIndex++);
            String[] value = comp.getStringOutput();
            String oldValue = value[1];
            String old = ActivityVersionUtil.sanitizeHtmlForExport(oldValue);
            rowIndex = checkMaxCellLimit(old, sheet, rowIndex, cellIndex, groupcell);
            groupcell.setCellStyle(cs);

            HSSFCell newcell = valueRow.createCell(cellIndex);
            String newValue = value[0];
            String newVal = ActivityVersionUtil.sanitizeHtmlForExport(newValue);
            rowIndex = checkMaxCellLimit(newVal, sheet, rowIndex, cellIndex, newcell);
            rowIndex++;
            newcell.setCellStyle(cs);
        }
        return rowIndex;
    }

}