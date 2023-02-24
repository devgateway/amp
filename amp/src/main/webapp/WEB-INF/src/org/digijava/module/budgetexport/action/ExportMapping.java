package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.AdminXSLExportUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 12/10/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExportMapping extends Action {
    private static Logger logger    = Logger.getLogger(ExportMapping.class);
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
        {
            Long ruleId = Long.parseLong(request.getParameter("ruleId"));
            AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(ruleId);
            List<AmpBudgetExportMapItem> items = rule.getItems();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "inline; filename=Export.xls");
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("export");
            // title cells
            HSSFCellStyle titleCS = AdminXSLExportUtil.createTitleStyle(wb);
             //ordinary cell style
            HSSFCellStyle  cs = AdminXSLExportUtil.createOrdinaryStyle(wb);

            int rowIndex = 0;

            HSSFRow titleRow = sheet.createRow(rowIndex++);

            String[] headerTitles = new String[] {"AMP DB ID", "AMP Label", "AMP Acronyme", "Imported Label", "Imported Code", "Match Level", "Approved"};

            for (int headerIdx = 0; headerIdx < headerTitles.length; headerIdx ++) {
                HSSFCell nameCell = titleRow.createCell(headerIdx);
                nameCell.setCellValue(new HSSFRichTextString(headerTitles[headerIdx]));
                nameCell.setCellStyle(titleCS);
            }
            
            if (items != null && !items.isEmpty()) {
                for (AmpBudgetExportMapItem item : items) {
                    HSSFRow itemRow = sheet.createRow(rowIndex++);

                    HSSFCell ampIdCell = itemRow.createCell(0);
                    ampIdCell.setCellValue(new HSSFRichTextString(item.getAmpObjectID().toString()));
                    ampIdCell.setCellStyle(cs);

                    HSSFCell ampLabelCell = itemRow.createCell(1);
                    ampLabelCell.setCellValue(new HSSFRichTextString(item.getAmpLabel()));
                    ampLabelCell.setCellStyle(cs);

                    HSSFCell ampAcronymeCell = itemRow.createCell(2);
                    ampAcronymeCell.setCellValue(new HSSFRichTextString(item.getAdditionalLabel() != null ? item.getAdditionalLabel() : ""));
                    ampAcronymeCell.setCellStyle(cs);

                    HSSFCell importedLabelCell = itemRow.createCell(3);
                    importedLabelCell.setCellValue(new HSSFRichTextString(item.getImportedLabel()));
                    importedLabelCell.setCellStyle(cs);

                    HSSFCell importedCodeCell = itemRow.createCell(4);
                    importedCodeCell.setCellValue(new HSSFRichTextString(item.getImportedCode()));
                    importedCodeCell.setCellStyle(cs);
                    
                    HSSFCell matchLevelCell = itemRow.createCell(5);
                    matchLevelCell.setCellValue(new HSSFRichTextString(String.valueOf(item.getMatchLevel())));
                    matchLevelCell.setCellStyle(cs);

                    HSSFCell approvedCell = itemRow.createCell(6);
                    approvedCell.setCellValue(new HSSFRichTextString(String.valueOf(item.isApproved())));
                    approvedCell.setCellStyle(cs);
                }
            }
            

            for (int headerIdx = 0; headerIdx < headerTitles.length; headerIdx ++) {
                sheet.autoSizeColumn(headerIdx);
            }
            

            wb.write(response.getOutputStream());
            return null;
        }

}
