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
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.OrgManagerForm;
import org.digijava.module.aim.util.AdminXSLExportUtil;

import javax.servlet.http.HttpSession;
import java.util.Collection;

public class ExportOrganizationManager2XSL  extends Action {
     private static Logger logger = Logger.getLogger(ExportOrganizationManager2XSL.class);

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
        OrgManagerForm eaForm = (OrgManagerForm) form;
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
        
        HSSFCell cellName = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Organization Name",locale,siteId));
        cellName.setCellValue(nameTitle);
        cellName.setCellStyle(titleCS);
        
        HSSFCell acroTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString acroTitle = new HSSFRichTextString(TranslatorWorker.translateText("Organization Acronym",locale,siteId));
        acroTitleCell.setCellValue(acroTitle);
        acroTitleCell.setCellStyle(titleCS);
        
        HSSFCell typeTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString typeTitle = new HSSFRichTextString(TranslatorWorker.translateText("Type",locale,siteId));
        typeTitleCell.setCellValue(typeTitle);
        typeTitleCell.setCellStyle(titleCS);
        
        HSSFCell orgGroupTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString orgGroupTitle = new HSSFRichTextString(TranslatorWorker.translateText("Organization Group",locale,siteId));
        orgGroupTitleCell.setCellValue(orgGroupTitle);
        orgGroupTitleCell.setCellStyle(titleCS);
        String alpha = eaForm.getAlpha();
        Collection<AmpOrganisation> organizations= (alpha!=null && !alpha.equals("viewAll"))? eaForm.getColsAlpha(): eaForm.getCols();

    
        if(organizations!=null){
            for(AmpOrganisation organization:organizations){
                cellIndex=0;
                  HSSFRow row = sheet.createRow(rowIndex++);
                  
                HSSFCell cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue(organization.getName());

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue(organization.getAcronym());

                AmpOrgGroup group = organization.getOrgGrpId();

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue(group.getOrgType().getOrgType());

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue(group.getOrgGrpName());
                    
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
