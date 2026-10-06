package org.digijava.module.esrigis.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.esrigis.form.DataDispatcherForm;

import java.util.Iterator;

public class ExcelExporter extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        DataDispatcherForm dataDispatcherForm = (DataDispatcherForm) form;

        JSONObject jsonobject = (JSONObject) new JSONTokener(dataDispatcherForm.getStructures()).nextValue();
        JSONArray starray = jsonobject.getJSONArray("Structures");
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            String titletrn = TranslatorWorker.translateText("Structures List");
            HSSFSheet sheet = wb.createSheet(titletrn);
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            HSSFRichTextString columtitle = new HSSFRichTextString(
                    TranslatorWorker.translateText("Name"));
            cell.setCellValue(columtitle);
            cell.setCellStyle(getRowHeadingSt(wb));

            cell = row.createCell(1);
            columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Type"));
            cell.setCellValue(columtitle);
            cell.setCellStyle(getRowHeadingSt(wb));

            cell = row.createCell(2);
            columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Activity Name"));
            cell.setCellValue(columtitle);
            cell.setCellStyle(getRowHeadingSt(wb));

            cell = row.createCell(3);
            columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Coordinates"));
            cell.setCellValue(columtitle);
            cell.setCellStyle(getRowHeadingSt(wb));

            Integer i = new Integer(1);
            for (Iterator iterator = starray.iterator(); iterator.hasNext();) {
                JSONObject json = (JSONObject) iterator.next();
                row = sheet.createRow(i);

                cell = row.createCell(0);
                columtitle = new HSSFRichTextString(json.get("Structure Name")
                        .toString());
                cell.setCellValue(columtitle);
                cell.setCellStyle(getCellSt(wb));

                cell = row.createCell(1);
                columtitle = new HSSFRichTextString(json.get("Structure Type")
                        .toString());
                cell.setCellValue(columtitle);
                cell.setCellStyle(getCellSt(wb));

                cell = row.createCell(2);
                columtitle = new HSSFRichTextString(getactivityname(json.get(
                        "Activity").toString()));
                cell.setCellValue(columtitle);
                cell.setCellStyle(getCellSt(wb));

                cell = row.createCell(3);
                columtitle = new HSSFRichTextString(json.get("Coordinates")
                        .toString());
                cell.setCellValue(columtitle);
                cell.setCellStyle(getCellSt(wb));
                i++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "inline; filename=Structures.xls");
            wb.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HSSFCellStyle getRowHeadingSt(HSSFWorkbook wb) {
        HSSFFont frowheading = wb.createFont();
        frowheading.setFontName("Arial Unicode MS");
        frowheading.setFontHeightInPoints((short) 8);
        frowheading.setBoldweight(frowheading.BOLDWEIGHT_BOLD);
        HSSFCellStyle rowheading = wb.createCellStyle();
        rowheading.setFont(frowheading);
        rowheading.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        rowheading.setBorderRight(HSSFCellStyle.BORDER_THIN);
        rowheading.setBorderTop(HSSFCellStyle.BORDER_THIN);
        rowheading.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        rowheading.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        return rowheading;
    }

    private HSSFCellStyle getCellSt(HSSFWorkbook wb) {
        HSSFFont fdataitem = wb.createFont();
        fdataitem.setFontName("Arial Unicode MS");
        fdataitem.setFontHeightInPoints((short) 8);
        fdataitem.setBoldweight(fdataitem.BOLDWEIGHT_NORMAL);
        HSSFCellStyle dataitem = wb.createCellStyle();
        dataitem.setFont(fdataitem);
        dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return dataitem;
    }

    private String getactivityname(String url) {
        String name = "";
        if (url != null && url != "") {
            name = url.substring(url.indexOf(">") + 1, url.lastIndexOf("<"));
        }
        return name;
    }
}
