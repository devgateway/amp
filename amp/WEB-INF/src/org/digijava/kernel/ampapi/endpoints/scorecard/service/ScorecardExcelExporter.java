package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class ScorecardExcelExporter {

	public HSSFWorkbook generateExcel(List<AmpOrganisation> columns, List<Quarter> headers,
			Map<Long, Map<String, ColoredCell>> data) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet(TranslatorWorker.translateText("Donor Scorecard"));
		worksheet.setColumnWidth(0, 11000);
		// index from 0,0... cell A1 is cell(0,0)
		int headerIndex = 0;
		int rowIndex = 0;
		HSSFRow row = worksheet.createRow(rowIndex);
		HSSFCell donorTextCell = row.createCell(headerIndex);
		donorTextCell.setCellValue(TranslatorWorker.translateText("Donors"));

		HSSFCellStyle blueHeaderStyle = workbook.createCellStyle();
		blueHeaderStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		blueHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		donorTextCell.setCellStyle(blueHeaderStyle);
		for (Quarter headerName : headers) {
			HSSFCell quarter = row.createCell(++headerIndex);
			quarter.setCellValue(headerName.toString());
			quarter.setCellStyle(blueHeaderStyle);
		}
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		for (AmpOrganisation column : columns) {
			HSSFRow donorNameRow = worksheet.createRow(++rowIndex);
			HSSFCell donorNameCell = donorNameRow.createCell(0);
			donorNameCell.setCellValue(column.getName());
			donorNameCell.setCellStyle(style);
		}
		for (int i = 0; i < columns.size(); i++) {
			for (int j = 0; j < headers.size(); j++) {
				ColoredCell cell = data.get(columns.get(i).getIdentifier()).get(headers.get(j).toString());
				HSSFCell paintedCell = worksheet.getRow(i + 1).createCell(j + 1);
				CellStyle cellStyle = workbook.createCellStyle();
				short color = HSSFColor.RED.index;
				switch (cell.getColor()) {
				case GRAY:
					color = HSSFColor.GREY_40_PERCENT.index;
					break;
				case GREEN:
					color = HSSFColor.GREEN.index;
					break;
				case RED:
					color = HSSFColor.RED.index;
					break;

				case YELLOW:
					color = HSSFColor.YELLOW.index;
					break;

				}
				cellStyle.setFillForegroundColor(color);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				paintedCell.setCellStyle(cellStyle);

			}
		}
		return workbook;
	}

}
