package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.contrib.RegionUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.util.AdminXSLExportUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.um.action.ExportUserManager2XSL;


public class ExportSectorManager2XSL extends Action {
	private static Logger logger = Logger
			.getLogger(ExportUserManager2XSL.class);

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
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		Long siteId = site.getId();
		String locale = navigationLanguage.getCode();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("export");
		// title cells
		HSSFCellStyle titleCS = AdminXSLExportUtil.createTitleStyle(wb);
		// ordinary cell style
		HSSFCellStyle cs = AdminXSLExportUtil.createOrdinaryStyle(wb);

		int rowIndex = 0;
		int cellIndex = 0;

		HSSFRow titleRow = sheet.createRow(rowIndex++);

		HSSFCell schemeCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString scheme = new HSSFRichTextString(
				TranslatorWorker.translateText("Schemes", locale, siteId));
		schemeCell.setCellValue(scheme);
		schemeCell.setCellStyle(titleCS);

		HSSFCell levelOneSecCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString levelOneSec = new HSSFRichTextString(
				TranslatorWorker.translateText("Level One Sectors", locale,
						siteId));
		levelOneSecCell.setCellValue(levelOneSec);
		levelOneSecCell.setCellStyle(titleCS);

		HSSFCell levelTwoSecCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString levelTwoSec = new HSSFRichTextString(
				TranslatorWorker.translateText("Level Two Sectors", locale,
						siteId));
		levelTwoSecCell.setCellValue(levelTwoSec);
		levelTwoSecCell.setCellStyle(titleCS);

		HSSFCell levelThreeSecCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString levelThreeSec = new HSSFRichTextString(
				TranslatorWorker.translateText("Level Three Sectors", locale,
						siteId));
		levelThreeSecCell.setCellValue(levelThreeSec);
		levelThreeSecCell.setCellStyle(titleCS);

		Collection<AmpSectorScheme> schemes = SectorUtil.getAllSectorSchemes();
		if (schemes != null) {
			for (AmpSectorScheme currScheme : schemes) {
				int totalMerge=0;
				cellIndex = 0;
				HSSFRow row = sheet.createRow(rowIndex++);
				HSSFCell cell = row.createCell(cellIndex++);
				cell.setCellStyle(cs);
				cell.setCellValue(currScheme.getSecSchemeName());
				Collection<AmpSector> parentSectors = SectorUtil
						.getAllParentSectors(currScheme.getAmpSecSchemeId());
				if (parentSectors != null&&parentSectors.size()>0) {
					for (AmpSector oneLevelSec : parentSectors) {
						cellIndex=1;
						int mergeRowsNum=0;
						cell = row.createCell(cellIndex++);
						cell.setCellStyle(cs);
						cell.setCellValue(oneLevelSec.getName());
						Collection<AmpSector> twoLevelSectors = SectorUtil
								.getAllChildSectors(oneLevelSec
										.getAmpSectorId());
						if (twoLevelSectors != null&&twoLevelSectors.size()>0) {
							for (AmpSector twoLevelSec : twoLevelSectors) {
								cellIndex=2;
								cell = row.createCell(cellIndex++);
								cell.setCellStyle(cs);
								cell.setCellValue(twoLevelSec.getName());
								Collection<AmpSector> threeLevelSectors = SectorUtil
										.getAllChildSectors(twoLevelSec
												.getAmpSectorId());
								if (threeLevelSectors != null&&threeLevelSectors.size()>0) {
									for (AmpSector threeLevelSec : threeLevelSectors) {
										cellIndex=3;
										cell = row.createCell(cellIndex++);
										cell.setCellStyle(cs);
										cell.setCellValue(threeLevelSec
												.getName());
										row = sheet.createRow(rowIndex++);
									}
									mergeRowsNum+=threeLevelSectors.size();
									CellRangeAddress region=new CellRangeAddress((row.getRowNum()-threeLevelSectors.size()),row.getRowNum()-1,2,2);
									AdminXSLExportUtil.applyingStylesToRegion(region, sheet, wb);
									sheet.addMergedRegion(region);
								}
								else{
									cellIndex=3;
									cell = row.createCell(cellIndex++);
									cell.setCellStyle(cs);
									row = sheet.createRow(rowIndex++);
									mergeRowsNum++;
								}
							}
							CellRangeAddress region=new CellRangeAddress((row.getRowNum()-mergeRowsNum),row.getRowNum()-1,1,1);
							AdminXSLExportUtil.applyingStylesToRegion(region, sheet, wb);
							sheet.addMergedRegion(region);
						}
						else{
							cellIndex=2;
							for(int i=0;i<2;i++){
								cell = row.createCell(cellIndex++);
								cell.setCellStyle(cs);
							}
							row = sheet.createRow(rowIndex++);
							mergeRowsNum++;
						}
						totalMerge+=mergeRowsNum;
					}
				    // Set the border and border colors.
					CellRangeAddress region=new CellRangeAddress((row.getRowNum()-totalMerge),row.getRowNum()-1,0,0);
					AdminXSLExportUtil.applyingStylesToRegion(region, sheet, wb);
					sheet.addMergedRegion(region);
				}
				else{
					cellIndex=1;
					for (int i = 0; i < 3; i++) {
						cell = row.createCell(cellIndex++);
						cell.setCellStyle(cs);
					}
					row = sheet.createRow(rowIndex++);
					
				}

			}
		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}
		
		wb.write(response.getOutputStream());
		return null;

	}

}
