package org.digijava.module.categorymanager.action;

import java.util.Collection;
import java.util.List;

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
import org.digijava.module.aim.util.AdminXSLExportUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.form.CategoryManagerForm;

public class ExportCategoryManager2XSL extends Action {
    private static Logger logger = Logger
            .getLogger(ExportCategoryManager2XSL.class);

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
        CategoryManagerForm myForm = (CategoryManagerForm) form;
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

        HSSFCell nameCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString(
                TranslatorWorker.translateText("Category Name", locale, siteId));
        nameCell.setCellValue(nameTitle);
        nameCell.setCellStyle(titleCS);

        HSSFCell descCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString description = new HSSFRichTextString(
                TranslatorWorker.translateText("Category Description", locale,
                        siteId));
        descCell.setCellValue(description);
        descCell.setCellStyle(titleCS);

        HSSFCell valCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString valTitle = new HSSFRichTextString(
                TranslatorWorker.translateText("Possible Values", locale,
                        siteId));
        valCell.setCellValue(valTitle);
        valCell.setCellStyle(titleCS);

        HSSFCell optCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString optionTitle = new HSSFRichTextString(
                TranslatorWorker.translateText("Category Options", locale,
                        siteId));
        optCell.setCellValue(optionTitle);
        optCell.setCellStyle(titleCS);

        String multiselect = TranslatorWorker.translateText("Multiselect",
                locale, siteId);
        String ordered = TranslatorWorker.translateText("Ordered", locale,
                siteId);

        Collection<AmpCategoryClass> categories = myForm.getCategories();
        if (categories != null) {
            for (AmpCategoryClass category : categories) {
                cellIndex = 0;
                HSSFRow row = sheet.createRow(rowIndex++);

                HSSFCell cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue(TranslatorWorker.translateText(category.getName(),
                        locale, siteId)+AdminXSLExportUtil.NEWLINECHAR+"("+TranslatorWorker.translateText(category.getKeyName(),
                                locale, siteId)+")");

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                cell.setCellValue((category.getDescription()==null||category.getDescription().trim().length()==0)?"":TranslatorWorker.translateText(category.getDescription(),
                        locale, siteId));

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                List<AmpCategoryValue> values = category.getPossibleValues();
                String currentRecord = "";
                if (values != null) {
                    for (AmpCategoryValue value : values) {
                        if (value!=null)
                            currentRecord += AdminXSLExportUtil.BULLETCHAR
                                + TranslatorWorker.translateText(value.getValue(),locale, siteId)
                                + AdminXSLExportUtil.NEWLINECHAR;
                    }
                }
                cell.setCellValue(currentRecord);
                cell.setCellStyle(cs);

                cell = row.createCell(cellIndex++);
                cell.setCellStyle(cs);
                String options="";
                if(category.isMultiselect()){
                    options+= AdminXSLExportUtil.BULLETCHAR+ multiselect+ AdminXSLExportUtil.NEWLINECHAR;
                }
                if(category.getIsOrdered()){
                    options+= AdminXSLExportUtil.BULLETCHAR+ ordered;
                }
                cell.setCellValue(options);
                cell.setCellStyle(cs);


            }
        }
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
        wb.write(response.getOutputStream());
        return null;

    }

}
