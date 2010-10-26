package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ExportTableForm;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class ExportAdminTable extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=Export.xls");
        ExportTableForm exportForm=(ExportTableForm)form;
 
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 12);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);


        String data=exportForm.getData();
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new java.io.StringReader(data)));
        Document doc = parser.getDocument();
        NodeList rows=doc.getElementsByTagName("row");
        for(int i=0;i<rows.getLength();i++){
           Node nodeRow=rows.item(i);
           HSSFRow row = sheet.createRow(i);
           NodeList cells=nodeRow.getChildNodes();
           for(short j=0;j<cells.getLength();j++){
               HSSFCell cell =row.createCell(j);
               if(cells.item(j).getNodeName().equalsIgnoreCase("cell")){
                   HSSFRichTextString value=new HSSFRichTextString(cells.item(j).getTextContent());
                   if(i==0){ //title cells
                        value.applyFont(fontHeader);
                   }
                   else{
                       value.applyFont(font);
                   }
                  cell.setCellValue(value);
               }
           }

        }
        wb.write(response.getOutputStream());
        return null;
    }
}
