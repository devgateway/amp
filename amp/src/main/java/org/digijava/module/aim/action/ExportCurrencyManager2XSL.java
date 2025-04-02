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
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.util.AdminXSLExportUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class ExportCurrencyManager2XSL extends Action {
     private static Logger logger = Logger.getLogger(ExportCurrencyManager2XSL.class);

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
        CurrencyForm crForm = (CurrencyForm) form;
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
       
       titleRow.createCell(cellIndex++);

       
       HSSFCell codeTitleCell = titleRow.createCell(cellIndex++);
       HSSFRichTextString codeTitle = new HSSFRichTextString(TranslatorWorker.translateText("Code",locale,siteId));
       codeTitleCell.setCellValue(codeTitle);
       codeTitleCell.setCellStyle(titleCS);
       
       HSSFCell cellName = titleRow.createCell(cellIndex++);
       HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Currency Name",locale,siteId));
       cellName.setCellValue(nameTitle);
       cellName.setCellStyle(titleCS);
       
       
       
       HSSFCell countryTitleCell = titleRow.createCell(cellIndex++);
       HSSFRichTextString countryTitle = new HSSFRichTextString(TranslatorWorker.translateText(
               CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getValueKey(), locale, siteId));
       countryTitleCell.setCellValue(countryTitle);
       countryTitleCell.setCellStyle(titleCS);
       String active=TranslatorWorker.translateText("Active",locale,siteId);
       String inactive=TranslatorWorker.translateText("Inactive",locale,siteId);
       
      

        Collection<AmpCurrency> currencies = (crForm.getFilterByCurrency() != null && crForm
                .getFilterByCurrency().trim().length() > 0) ? crForm
                .getCurrency() : crForm.getAllCurrencies();

        if(currencies!=null){
            for(AmpCurrency currency:currencies){
                cellIndex=0;
                  HSSFRow row = sheet.createRow(rowIndex++);
                  
                  HSSFCell cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue((currency.getActiveFlag()==0)?inactive:active);
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(currency.getCurrencyCode());
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue(currency.getCurrencyName());
                  
                  cell=row.createCell(cellIndex++);
                  cell.setCellStyle(cs);
                  cell.setCellValue((currency.getCountryLocation()!=null)?currency.getCountryLocation().getName():"");
                  
                
            }
        }
        sheet.autoSizeColumn(0); //adjust width of the first column
       sheet.autoSizeColumn(1);
       sheet.autoSizeColumn(2);
       sheet.autoSizeColumn(3);
       wb.write(response.getOutputStream());
        return null;

      }

}
