package org.digijava.module.um.action;

import java.util.Collection;

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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.UserBean;
import org.digijava.module.um.form.ViewAllUsersForm;

public class ExportUserManager2XSL extends Action {
	private static Logger logger = Logger
			.getLogger(ExportUserManager2XSL.class);
	private final static char BULLETCHAR = '\u2022';
	private final static char NEWLINECHAR = '\n';

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
		ViewAllUsersForm userForm = (ViewAllUsersForm) form;
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		Long siteId = site.getId();
		String locale = navigationLanguage.getCode();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("export");
		// title cells
		HSSFCellStyle titleCS = wb.createCellStyle();
		titleCS.setWrapText(true);
		titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setFontName(HSSFFont.FONT_ARIAL);
		fontHeader.setFontHeightInPoints((short) 10);
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCS.setFont(fontHeader);
		 //ordinary cell style
		HSSFCellStyle  cs = wb.createCellStyle();
	    cs.setWrapText(true);
	    cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		for (int i = 0; i < 3; i++) {
			sheet.setDefaultColumnStyle(i, cs);
		}


		int rowIndex = 0;
		int cellIndex = 0;

		HSSFRow titleRow = sheet.createRow(rowIndex++);

		HSSFCell titleCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString title = new HSSFRichTextString(
				TranslatorWorker.translateText("Name", locale, siteId));
		titleCell.setCellValue(title);
		titleCell.setCellStyle(titleCS);

		HSSFCell cellName = titleRow.createCell(cellIndex++);
		HSSFRichTextString nameTitle = new HSSFRichTextString(
				TranslatorWorker.translateText("Email", locale, siteId));
		cellName.setCellValue(nameTitle);
		cellName.setCellStyle(titleCS);

		HSSFCell countryTitleCell = titleRow.createCell(cellIndex++);
		HSSFRichTextString countryTitle = new HSSFRichTextString(
				TranslatorWorker.translateText("Workspace", locale, siteId));
		countryTitleCell.setCellValue(countryTitle);
		countryTitleCell.setCellStyle(titleCS);
		Collection<UserBean> users = userForm.getUsers();

		if (users != null) {
			for (UserBean user : users) {
				cellIndex = 0;
				HSSFRow row = sheet.createRow(rowIndex++);
				row.createCell(cellIndex++).setCellValue(
						user.getFirstNames() + " " + user.getLastName());
				row.createCell(cellIndex++).setCellValue(user.getEmail());
				HSSFCell cell = row.createCell(cellIndex++);
				String currentRecord = "";
				if (user.getTeamMembers() != null&&user.getTeamMembers().size()>0) {
					for (AmpTeamMember member : user.getTeamMembers()) {
						currentRecord += BULLETCHAR
								+ member.getAmpTeam().getName() + NEWLINECHAR;
					}
					cell.setCellValue( currentRecord);
				} else {
					cell.setCellValue(TranslatorWorker.translateText(
							"Unassigned", locale, siteId));
				}

			}
		}
		sheet.autoSizeColumn(0); // adjust width of the first column
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		wb.write(response.getOutputStream());
		return null;

	}

}
