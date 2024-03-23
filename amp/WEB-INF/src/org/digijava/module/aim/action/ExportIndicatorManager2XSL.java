package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.ViewIndicatorsForm;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.util.AdminXSLExportUtil;

import javax.servlet.http.HttpSession;
import java.util.Collection;

public class ExportIndicatorManager2XSL extends Action {
    private static Logger logger = Logger
    .getLogger(ExportIndicatorManager2XSL.class);


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
ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
Site site = RequestUtils.getSite(request);
Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
Long siteId = site.getId();
String locale = navigationLanguage.getCode();

HSSFWorkbook wb = new HSSFWorkbook();
HSSFSheet sheet = wb.createSheet("export");
// title cells
HSSFCellStyle titleCS =AdminXSLExportUtil.createTitleStyle(wb);
 //ordinary cell style
HSSFCellStyle  cs = AdminXSLExportUtil.createOrdinaryStyle(wb);



int rowIndex = 0;
int cellIndex = 0;

HSSFRow titleRow = sheet.createRow(rowIndex++);

HSSFCell titleCell = titleRow.createCell(cellIndex++);
HSSFRichTextString title = new HSSFRichTextString(
        TranslatorWorker.translateText("Indicator Name", locale, siteId));
titleCell.setCellValue(title);
titleCell.setCellStyle(titleCS);

HSSFCell cellSector = titleRow.createCell(cellIndex++);
HSSFRichTextString sector= new HSSFRichTextString(
        TranslatorWorker.translateText("Sector", locale, siteId));
cellSector.setCellValue(sector);
cellSector.setCellStyle(titleCS);

Collection<IndicatorsBean> indicators = allIndForm.getAllIndicators();

if (indicators != null) {
    for (IndicatorsBean indicator : indicators) {
        cellIndex = 0;
        HSSFRow row = sheet.createRow(rowIndex++);
        HSSFCell cell=row.createCell(cellIndex++);
        cell.setCellStyle(cs);
        cell.setCellValue(indicator.getName());
        cell = row.createCell(cellIndex++);
        String currentRecord = "";
        if (indicator.getSectorNames() != null) {
            Collection<String> names=indicator.getSectorNames();
            for (String name : names) {
                currentRecord += AdminXSLExportUtil.BULLETCHAR
                        + name + AdminXSLExportUtil.NEWLINECHAR;
            }
            cell.setCellValue( currentRecord);
            cell.setCellStyle(cs);
        } 
    }
}
sheet.autoSizeColumn(0); 
sheet.autoSizeColumn(1);
wb.write(response.getOutputStream());
return null;

}

}
