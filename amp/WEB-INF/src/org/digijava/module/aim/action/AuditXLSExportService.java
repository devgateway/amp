package org.digijava.module.aim.action;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public  class AuditXLSExportService {

    private static final Integer first_COLUMN_WIDTH = 3840;
    private static final Integer COLUMN_WIDTH = 12800;
    private HSSFCellStyle cs;
    private HSSFCellStyle titleCS;

    public AuditXLSExportService() {

    }

    public HSSFCellStyle getOrCreateTitleStyle(HSSFWorkbook wb) {
        if (titleCS == null) {
            titleCS = wb.createCellStyle();
            wb.createCellStyle();
            titleCS.setWrapText(true);
            HSSFFont fontHeader = wb.createFont();
            fontHeader.setFontName(HSSFFont.FONT_ARIAL);
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titleCS.setFont(fontHeader);
            addBorderToCell(titleCS);
        }
        return titleCS;
    }

    private void addBorderToCell(HSSFCellStyle cell) {
        cell.setBorderLeft(BORDER_THIN);
        cell.setBorderRight(BORDER_THIN);
        cell.setBorderTop(BORDER_THIN);
        cell.setBorderBottom(BORDER_THIN);
    }

    public HSSFCellStyle getOrCreateOrdinaryStyle(HSSFWorkbook wb) {
        if (cs == null) {
            cs = wb.createCellStyle();
            cs.setWrapText(true);
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            addBorderToCell(cs);
        }
        return cs;
    }
    public void setColumnWidth(HSSFSheet sheet) {
        sheet.setColumnWidth(0, first_COLUMN_WIDTH);
        sheet.setColumnWidth(1, COLUMN_WIDTH);
        sheet.setColumnWidth(2, COLUMN_WIDTH);
    }
}