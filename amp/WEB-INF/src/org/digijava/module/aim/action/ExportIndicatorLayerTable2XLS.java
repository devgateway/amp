package org.digijava.module.aim.action;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class ExportIndicatorLayerTable2XLS extends Action {
	private static final String ADM_LEVEL_PARAM = "admLevel";
	private static final String GEO_ID_TITLE = "Code";
	private static Logger logger = Logger.getLogger(ExportIndicatorLayerTable2XLS.class);

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
		HttpSession session = request.getSession();
		String admLevel = request.getParameter(ADM_LEVEL_PARAM);
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=Export.xls");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("export");
		AmpCategoryValue categoryValue =(AmpCategoryValue) DbUtil.getObject(AmpCategoryValue.class, new Long (admLevel));
		logger.debug("Exporting indicator table layer for adm level: "+categoryValue.getValue());
		// title cells
		HSSFCellStyle titleCS = wb.createCellStyle();
		wb.createCellStyle();
		titleCS.setWrapText(true);
		titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setFontName(HSSFFont.FONT_ARIAL);
		fontHeader.setFontHeightInPoints((short) 10);
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCS.setFont(fontHeader);
		int rowIndex = 0;
		int cellIndex = 0;
		HSSFRow titleRow = sheet.createRow(rowIndex++);

		HSSFCell cell = titleRow.createCell(cellIndex++);
		HSSFRichTextString nameTitle = new HSSFRichTextString(categoryValue.getValue());
		cell.setCellValue(nameTitle);
		cell.setCellStyle(titleCS);

		cell = titleRow.createCell(cellIndex++);
		nameTitle = new HSSFRichTextString(GEO_ID_TITLE);
		cell.setCellValue(nameTitle);
		cell.setCellStyle(titleCS);

		List<AmpIndicatorLayer> indicatorLayers = DynLocationManagerUtil.getIndicatorByCategoryValueId(new Long (admLevel));
		for (AmpIndicatorLayer indicator : indicatorLayers) {
			cell = titleRow.createCell(cellIndex++);
			nameTitle = new HSSFRichTextString(indicator.getName());
			cell.setCellValue(nameTitle);
			cell.setCellStyle(titleCS);

		}
		populateIndicatorLayerTableValues(sheet,rowIndex,categoryValue);
		for (int i = 0; i < cellIndex; i++) {
			sheet.autoSizeColumn(i);
		}
		wb.write(response.getOutputStream());
		return null;

	}

	private void populateIndicatorLayerTableValues(HSSFSheet sheet,int rowIndex, AmpCategoryValue categoryValue) {
		Set<AmpCategoryValueLocations> locations = DynLocationManagerUtil
				.getLocationsByLayer(categoryValue);
		for (AmpCategoryValueLocations location : locations) {
			int cellIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);
			HSSFCell cell = row.createCell(cellIndex++);
			cell.setCellValue(location.getName());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(location.getGeoCode());
			List <AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocation(location);
			for (AmpLocationIndicatorValue value:values) {
				cell = row.createCell(cellIndex++);
				cell.setCellValue(value.getValue());
			}

		}

	}

	
}
