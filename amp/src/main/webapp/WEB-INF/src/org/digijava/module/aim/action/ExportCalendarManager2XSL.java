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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.form.FiscalCalendarForm;
import org.digijava.module.aim.util.AdminXSLExportUtil;

public class ExportCalendarManager2XSL extends Action {
     private static Logger logger = Logger.getLogger(ExportCalendarManager2XSL.class);
     
     private final String[] MONTHES = {"January", "February",
              "March", "April", "May", "June", "July",
              "August", "September", "October", "November",
              "December"
              };

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
        FiscalCalendarForm fcForm = (FiscalCalendarForm) form;
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
            
      HSSFCell nameCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Name",locale,siteId));
      nameCell.setCellValue(nameTitle);
      nameCell.setCellStyle(titleCS);
      
      HSSFCell baseCalCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString baseCal = new HSSFRichTextString(TranslatorWorker.translateText("Base Calendar",locale,siteId));
      baseCalCell.setCellValue(baseCal);
      baseCalCell.setCellStyle(titleCS);
      
      
      
      HSSFCell isFiscalCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString fiscalTitle = new HSSFRichTextString(TranslatorWorker.translateText("Is Fiscal",locale,siteId));
      isFiscalCell.setCellValue(fiscalTitle);
      isFiscalCell.setCellStyle(titleCS);
      
      HSSFCell monthCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString monthTitle = new HSSFRichTextString(TranslatorWorker.translateText("Start Month",locale,siteId));
      monthCell.setCellValue(monthTitle);
      monthCell.setCellStyle(titleCS);
      
      HSSFCell startDayCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString startDayTitle = new HSSFRichTextString(TranslatorWorker.translateText("Start Day",locale,siteId));
      startDayCell.setCellValue(startDayTitle);
      startDayCell.setCellStyle(titleCS);
      
      HSSFCell offsetCell = titleRow.createCell(cellIndex++);
      HSSFRichTextString offsetTitle = new HSSFRichTextString(TranslatorWorker.translateText("Offset (From current year)",locale,siteId));
      offsetCell.setCellValue(offsetTitle);
      offsetCell.setCellStyle(titleCS);
      
      String yes=TranslatorWorker.translateText("Yes",locale,siteId);
      String no=TranslatorWorker.translateText("No",locale,siteId);
      
     

      Collection<AmpFiscalCalendar> calendars = (Collection<AmpFiscalCalendar>)session.getAttribute("ampFisCal");

        if(calendars!=null){
            for(AmpFiscalCalendar calendar:calendars){
                cellIndex=0;
                  HSSFRow row = sheet.createRow(rowIndex++);
                  
                  HSSFCell cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(calendar.getName());
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(calendar.getBaseCal());
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue((calendar.getIsFiscal()) ? yes : no);
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(TranslatorWorker.translateText(MONTHES[calendar.getStartMonthNum()-1],locale,siteId));
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(calendar.getStartDayNum());
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(calendar.getYearOffset());
                  
                
            }
        }
        for(int i=0;i<6;i++){
            sheet.autoSizeColumn(i);
        }
      wb.write(response.getOutputStream());
        return null;

      }

}
