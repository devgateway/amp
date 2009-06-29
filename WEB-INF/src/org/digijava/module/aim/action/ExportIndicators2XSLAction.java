package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.IndicatorGridItem;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * Creates Excel file from Indicators list
 * 
 * @author Irakli Kobiashvili ikobiashvili@picktek.com
 * 
 */
public class ExportIndicators2XSLAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NpdForm npdForm = (NpdForm) form;
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		Site site = RequestUtils.getSite(request);
		 
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=AMPIndicatorsExport.xls");

		AmpTheme mainProg = ProgramUtil.getThemeObject(npdForm.getProgramId());
		Collection<IndicatorGridRow> rows = getGridRows(mainProg, npdForm.getRecursive(), npdForm
				.getSelYears());

		XLSExporter.resetStyles();

		HSSFWorkbook wb = new HSSFWorkbook();
		String sheetName = mainProg.getName();
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		HSSFSheet sheet = wb.createSheet( sheetName.replace(":", "-"));

		
		HSSFCellStyle csHeader = wb.createCellStyle();
		csHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short)12);			
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csHeader.setFont(font);				

		HSSFCellStyle csSubHeader = wb.createCellStyle();
		csSubHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
		HSSFFont fontSubHeader = wb.createFont();
		fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
		//fontSubHeader.setFontHeightInPoints((short)12);			
		fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csSubHeader.setFont(fontSubHeader);				

		
		short rowNum = 0;
		short cellNum = 0;

		HSSFRow row = sheet.createRow(rowNum++);

		HSSFCell cell = row.createCell(cellNum);

		cell.setCellValue(TranslatorWorker.translateText("Indicators for ", locale, site.getId()));
		cell.setCellStyle(csHeader);
		
		if (npdForm.getSelYears() != null && npdForm.getSelYears().length > 0) {

			// table header 1
			row = sheet.createRow(rowNum++);
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			for (int i = 0; i < npdForm.getSelYears().length; i++) {
				cell = row.createCell(cellNum++);
				cellNum++;
				cellNum++;
				cell.setCellValue(npdForm.getSelYears()[i]);
				cell.setCellStyle(csSubHeader);
			}

			// table header 2
			cellNum = 0;
			row = sheet.createRow(rowNum++);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("indicator Name", locale, site.getId()));			
			cell.setCellStyle(csSubHeader);			
			
			cell=row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("indicator Description", locale, site.getId()));			
			cell.setCellStyle(csSubHeader);

			for (int i = 0; i < npdForm.getSelYears().length; i++) {
                                cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Base", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
				cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Actual", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
				cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Target", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
			}

			// rows
			if (rows != null && rows.size() > 0) {
				for (IndicatorGridRow indic : rows) {
					cellNum = 0;

					row = sheet.createRow(rowNum++);
					
					cell = row.createCell(cellNum++);
					cell.setCellValue(indic.getName());
					
					
					cell = row.createCell(cellNum++);
					cell.setCellValue(indic.getDescription());
					
					
					List<IndicatorGridItem> values = indic.getValues();
					if (values!=null){
						for (IndicatorGridItem item: values) {
                                                        cell = row.createCell(cellNum++);
							cell.setCellValue(item.getBaseValue());
							cell = row.createCell(cellNum++);
							cell.setCellValue(item.getActualValue());
							cell = row.createCell(cellNum++);
							cell.setCellValue(item.getTargetValue());
						}
					}
				}
			}
		}

		wb.write(response.getOutputStream());

		return null;
	}

	private Collection<IndicatorGridRow> getGridRows(AmpTheme prog, boolean recursive,String[] years) throws DgException{
		List<IndicatorGridRow> result = null;
		if (prog != null && prog.getAmpThemeId() != null) {
			//get all indicators and if recursive=true then all sub indicators too 
			Set<IndicatorTheme> indicators = IndicatorUtil.getIndicators(prog, recursive);
			if (indicators != null && indicators.size() > 0) {
				//convert to list
				List<IndicatorTheme> indicatorsList = new ArrayList<IndicatorTheme>(indicators);
				//sort
				Collections.sort(indicatorsList,new IndicatorUtil.IndThemeIndciatorNameComparator());
				result = new ArrayList<IndicatorGridRow>(indicatorsList.size());
				//create row object for each indicator connection
				for (IndicatorTheme connection : indicatorsList) {
					IndicatorGridRow row = new IndicatorGridRow(connection,years);
					result.add(row);
				}
			}
		}
		return result;
	}


	public static class IndicatorsByyear2XLS extends XLSExporter {

		public IndicatorsByyear2XLS(Exporter parent, Viewable item) {
			super(parent, item);
		}

		public IndicatorsByyear2XLS(HSSFWorkbook wb, HSSFSheet sheet,
				HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
				Viewable item) {
			super(wb, sheet, row, rowId, colId, ownerId, item);
		}

		public void generate() {

			// TODO Auto-generated method stub

		}

	}

}
