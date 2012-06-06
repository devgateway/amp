package org.digijava.module.aim.action;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;

import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.AdminXSLExportUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ExportRegionManager2XSL extends Action {
	private static Logger logger = Logger
			.getLogger(ExportRegionManager2XSL.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		HttpSession session = request.getSession();
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
		DynLocationManagerForm myForm = (DynLocationManagerForm) form;
		String hideEmptyCountriesStr				= request.getParameter("hideEmptyCountriesAction");
		if ( "false".equals(hideEmptyCountriesStr) ) {
			myForm.setHideEmptyCountries(false);
		}
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		Long siteId = site.getId();
		String locale = navigationLanguage.getCode();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("export");
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

		AmpCategoryClass implLocClass = CategoryManagerUtil
				.loadAmpCategoryClassByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
		Collection<AmpCategoryValue> values = CategoryManagerUtil
				.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
		AmpCategoryValue countryLayer									= 
			CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
		int countryLayerIndex	= countryLayer.getIndex();
		int size = values.size();
		HSSFRow titleRow = sheet.createRow(rowIndex++);
		for (AmpCategoryValue value : values) {
			HSSFCell cell = titleRow.createCell(cellIndex++);
			HSSFRichTextString nameTitle = new HSSFRichTextString(
					TranslatorWorker.translateText(value.getValue(), locale,
							siteId));
			cell.setCellValue(nameTitle);
			cell.setCellStyle(titleCS);

		}
		ActionMessages errors = new ActionMessages();
		Collection<AmpCategoryValueLocations> rootLocations = DynLocationManagerUtil
				.getHighestLayerLocations(implLocClass, myForm, errors);
		Integer[] rowIndexArr={rowIndex};
		cellIndex=-1;
		generateLocationHierarchy(rootLocations, rowIndexArr,sheet,++cellIndex,myForm.getHideEmptyCountries(), countryLayerIndex);
		for (int i = 0; i < size; i++) {
			sheet.autoSizeColumn(i);
		}
		wb.write(response.getOutputStream());
		return null;

	}

	private void generateLocationHierarchy(
			Collection<AmpCategoryValueLocations> locations, Integer[] rowIndex, HSSFSheet sheet,int cellIndex,boolean hideEmptyCountries,int countryLayerIndex ) {
		if (locations != null&&locations.size()>0) {
			for (AmpCategoryValueLocations location : locations) {
				Set<AmpCategoryValueLocations> childrenLocs = location
				.getChildLocations();
				int currentLayer				= location.getParentCategoryValue().getIndex();
				if(hideEmptyCountries&&currentLayer==countryLayerIndex&&childrenLocs.size()==0){
					continue;
				}
				HSSFRow row = sheet.createRow(rowIndex[0]++);
				HSSFCell cell = row.createCell(cellIndex);
				cell.setCellValue(location.getName());
				int newVal=cellIndex+1;
				generateLocationHierarchy(childrenLocs, rowIndex,sheet,newVal,hideEmptyCountries,countryLayerIndex);
			}
		}
		else{
			return;
		}

	}
}
