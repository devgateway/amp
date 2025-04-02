package org.digijava.module.aim.action;

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
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.form.ActivityForm;

public class ExportActivityManager2XSL extends Action {
private static Logger logger = Logger.getLogger(ExportActivityManager2XSL.class);

public ActionForward execute(ActionMapping mapping, ActionForm form,
                             javax.servlet.http.HttpServletRequest request,
                             javax.servlet.http.HttpServletResponse response) throws
    java.lang.Exception {
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
    ActivityForm actForm = (ActivityForm) form;
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


 int rowIndex = 0;
 int cellIndex = 0;
 
 HSSFRow titleRow = sheet.createRow(rowIndex++);
  
 HSSFCell titleCell = titleRow.createCell(cellIndex++);
 HSSFRichTextString title = new HSSFRichTextString(TranslatorWorker.translateText("Activity Name",locale,siteId));
 titleCell.setCellValue(title);
 titleCell.setCellStyle(titleCS);
 
 HSSFCell cellName = titleRow.createCell(cellIndex++);
 HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Team Name",locale,siteId));
 cellName.setCellValue(nameTitle);
 cellName.setCellStyle(titleCS);
 
 
 
 HSSFCell countryTitleCell = titleRow.createCell(cellIndex++);
 HSSFRichTextString countryTitle = new HSSFRichTextString(TranslatorWorker.translateText("Activity Id ",locale,siteId));
 countryTitleCell.setCellValue(countryTitle);
 countryTitleCell.setCellStyle(titleCS);
    Collection<AmpActivityFake> activities =actForm.getAllActivityList();

    if(activities!=null){
        for(AmpActivityFake activity:activities){
            cellIndex=0;
              HSSFRow row = sheet.createRow(rowIndex++);
              row.createCell(cellIndex++).setCellValue(activity.getName());
              row.createCell(cellIndex++).setCellValue((activity.getTeam()==null)?"":activity.getTeam().getName());
              row.createCell(cellIndex++).setCellValue(activity.getAmpId());
            
        }
    }
        sheet.autoSizeColumn(0); // adjust width of the first column
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        wb.write(response.getOutputStream());
    return null;

}

}
