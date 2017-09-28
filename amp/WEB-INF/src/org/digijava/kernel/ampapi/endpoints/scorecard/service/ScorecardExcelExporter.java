package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * Class in charge of generating the scorecard excel file.
 * 
 * @author Emanuel Perez
 *
 */
public class ScorecardExcelExporter {
    
    ScorecardExcelTemplate scorecardExcelTemplate;

    /**
     * Creates an excel workbook (HSSFWorkbook) having the headers with all the Quarters spanning the desired period,
     * the donors as columns and each cell (for a given donor and quarter) painted with a color depending on the number
     * of updated projects for a given donor on a quarter.
     * 
     * @param columns the List <AmpOrganisation> with the donors for which the scorecard is generated 
     * @param headers, List with the quarters that represent the header of the scorecard Excel file.
     * @param data contains the ColoredCells for every quarter and donor. Each cell is filled with gray, 
     * yellow, red or green depending how many projects were updated by the donor on a given quarter
     * @return the HSSFWorkbook with the generated excel file
     */
    public HSSFWorkbook generateExcel(List<AmpOrganisation> columns, List<Quarter> headers,
            Map<Long, Map<String, ColoredCell>> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet(TranslatorWorker.translateText("Donor Scorecard"));
        worksheet.setColumnWidth(0, 11000);
        
        // create the template styles
        scorecardExcelTemplate = new ScorecardExcelTemplate(workbook);
        
        // index from 0,0... cell A1 is cell(0,0)
        createHeader(headers, workbook, worksheet);
        createColumns(columns, workbook, worksheet);
        for (int i = 0; i < columns.size(); i++) {
            for (int j = 0; j < headers.size(); j++) {
                ColoredCell cell = data.get(columns.get(i).getIdentifier()).get(headers.get(j).toString());
                HSSFCell paintedCell = worksheet.getRow(i + 1).createCell(j + 1);
                CellStyle cellStyle = scorecardExcelTemplate.getColoredStyle(cell.getColor());
                paintedCell.setCellStyle(cellStyle);
            }
        }
        return workbook;
    }

    /**
     * Creates the columns with all the donors for whom we determine the level of project updates on a given quarter
     * 
     * @param columns List of AmpOrganisations with the donors that will represent the columns
     * @param workbook the HSSFWorkbook
     * @param worksheet the HSSFSheet
     */
    private void createColumns(List<AmpOrganisation> columns, HSSFWorkbook workbook, HSSFSheet worksheet) {
        int rowIndex = 0;
        for (AmpOrganisation column : columns) {
            HSSFRow donorNameRow = worksheet.createRow(++rowIndex);
            HSSFCell donorNameCell = donorNameRow.createCell(0);
            donorNameCell.setCellValue(column.getName());
            donorNameCell.setCellStyle(scorecardExcelTemplate.getWrapTextCellStyle());
        }
    }

    /**
     * Creates the header for the HSSFWorkbook with the quarters that span the desired period
     * @param headers List of Quarters that represent the header
     * @param workbook the HSSFWorkbook
     * @param worksheet the HSSFSheet
     */
    private void createHeader(List<Quarter> headers, HSSFWorkbook workbook, HSSFSheet worksheet) {
        int headerIndex = 0;
        HSSFRow row = worksheet.createRow(0);
        HSSFCell donorTextCell = row.createCell(headerIndex);
        donorTextCell.setCellValue(TranslatorWorker.translateText("Donors"));
        donorTextCell.setCellStyle(scorecardExcelTemplate.getBlueHeaderCellStyle());
        for (Quarter headerName : headers) {
            HSSFCell quarter = row.createCell(++headerIndex);
            quarter.setCellValue(headerName.toString());
            quarter.setCellStyle(scorecardExcelTemplate.getBlueHeaderCellStyle());
            worksheet.autoSizeColumn(headerIndex);
        }
    }

}
