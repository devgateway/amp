package org.digijava.module.aim.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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
import org.apache.xerces.parsers.DOMParser;
import org.digijava.module.aim.form.ExportTableForm;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ExportAdminTable extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=Export.xls");
        ExportTableForm exportForm = (ExportTableForm) form;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");

        // title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        // titleCS.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 10);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleCS.setFont(fontHeader);

        // HSSFFont fontHeader = wb.createFont();
        // fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        // fontHeader.setFontHeightInPoints((short) 12);
        // fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // ordinary cells
        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setColor(HSSFColor.BLACK.index);
        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat df = wb.createDataFormat();
        cs.setDataFormat(df.getFormat("General"));

        cs.setFont(font);
        cs.setWrapText(true);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        String data = exportForm.getData();
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new java.io.StringReader(data)));
        Document doc = parser.getDocument();
        int cellSize=0;
        NodeList rows = doc.getElementsByTagName("row");
        int rowIndex = 0;
        float rowHeight = 12.75f;
        for (int i = 0; i < rows.getLength(); i++) {
            int maxMerge = 1;
            Node nodeRow = rows.item(i);
            NodeList cells = nodeRow.getChildNodes();
            Map<Integer, String[]> columnsPerRow = new HashMap<Integer, String[]>();
            cellSize = cells.getLength();
            for (int j = 0; j < cells.getLength(); j++) {
                String textContent;
                if (cells.item(j).getNodeName().equalsIgnoreCase("cell")) {
                    textContent = cells.item(j).getTextContent().trim();
                    if (textContent.contains("\u2022")) {
                      String[] splitedText = textContent.split("\u2022");
                        if (maxMerge < splitedText.length) {
                            maxMerge = splitedText.length;
                        }
                       columnsPerRow.put(j, splitedText);
                    } else {
                         columnsPerRow.put(j, new String[]{textContent});
                    }
                }
            }
            for (int j = 0; j < maxMerge; j++) {
                HSSFRow row = sheet.createRow(rowIndex++);
                row.setHeightInPoints(rowHeight);
                int cellIndex = 0;
                for (int key = 0; key < cellSize; key++) {
                    String[] splitedText = columnsPerRow.get(key);
                    if (splitedText.length >j) {
                        HSSFCell bulletCell = row.createCell(cellIndex++);
                        String cellValue = null;
                        if (splitedText.length > 1) {
                            cellValue = "\u2022" + splitedText[j];
                        } else {
                            cellValue = splitedText[0];
                        }
                        if (i == 0) { //title cells
                            bulletCell.setCellStyle(titleCS);
                        } else {
                            bulletCell.setCellStyle(cs);
                        }
                        HSSFRichTextString text = new HSSFRichTextString(cellValue);
                        bulletCell.setCellValue(text);
                        
                    } else {
                       row.createCell(cellIndex++);

                    }

                }
            }
        }
        for(int i=0;i<cellSize;i++){
            sheet.autoSizeColumn(i); //adjust width of the first column
        }


        wb.write(response.getOutputStream());
        return null;
    }
}
