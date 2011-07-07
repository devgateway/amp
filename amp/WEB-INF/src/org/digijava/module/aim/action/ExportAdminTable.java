package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=Export.xls");
        ExportTableForm exportForm=(ExportTableForm)form;
 
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        
        //title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        //titleCS.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short)10);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);       
        titleCS.setFont(fontHeader);
        
               
        
//        HSSFFont fontHeader = wb.createFont();
//        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
//        fontHeader.setFontHeightInPoints((short) 12);
//        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //ordinary cells
        HSSFCellStyle cs = wb.createCellStyle();
		HSSFFont font= wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor( HSSFColor.BLACK.index );
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		HSSFDataFormat df = wb.createDataFormat();
		cs.setDataFormat(df.getFormat("General"));
	
		cs.setFont(font);
		cs.setWrapText(true);
		cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);       
        

        String data=exportForm.getData();
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new java.io.StringReader(data)));
        Document doc = parser.getDocument();
        NodeList rows=doc.getElementsByTagName("row");
        int cellAmountForEachRow = rows.item(0).getChildNodes().getLength();
        int [] cellSizes=new int[cellAmountForEachRow];
        int [] count=new int[cellAmountForEachRow];
        for(int i=0;i<rows.getLength();i++){
           Node nodeRow=rows.item(i);
           HSSFRow row = sheet.createRow(i);
           float rowHeight = 12.75f;
           
           NodeList cells=nodeRow.getChildNodes();
           for(short j=0;j<cells.getLength();j++){
               HSSFCell cell =row.createCell(j);
               if(cells.item(j).getNodeName().equalsIgnoreCase("cell")){
            	   String textContent =cells.item(j).getTextContent();
            	   int largestTextItemLength=0;
            	   if(textContent.trim().contains("\u2022")){
            		   String[] splitedText = textContent.trim().split("\u2022");
            		   textContent="";
            		   if(splitedText.length >2 && splitedText.length*11 > rowHeight){
            			   rowHeight=splitedText.length*11;
            		   }
            		   for(int k=0;k<splitedText.length;k++){
            			   if(splitedText[k].length()>0){
            				   textContent+="\u2022"+splitedText[k]+"\n";
            			   }
            			   largestTextItemLength =splitedText[k].length()>largestTextItemLength?splitedText[k].length():largestTextItemLength; 
            		   }
            	   }else{
            		   if(textContent.length()>10){
            			   largestTextItemLength = textContent.length()>largestTextItemLength?textContent.length():largestTextItemLength;
            		   }
            	   }
            	             	   
                   HSSFRichTextString value=new HSSFRichTextString(textContent);
                   if(largestTextItemLength>10){
                	   cellSizes[j] +=largestTextItemLength;
                	   count[j]++;
                   }
                   if(i==0){ //title cells
                	   cell.setCellStyle(titleCS);
                   }
                   else{
                	   cell.setCellStyle(cs);
                   }                   
                  cell.setCellValue(value);
                  
               }
           } 
           
           row.setHeightInPoints(rowHeight);

        }        

        
        for(int k=0;k<count.length;k++){
        	int countVal = count[k];
        	if(countVal > 0){
        		int cellSize = cellSizes[k]/countVal;
        		cellSize+=5;
        		
        		int val;
				if ((cellSize*256) < 2560)
					val = 2560; //at least 10 chars
				else
					val = cellSize*256;
								
				sheet.setColumnWidth(k, val);
        	}else if(countVal==0){
        		sheet.setColumnWidth(k, 2560);
        	}
        }
        	
        wb.write(response.getOutputStream());
        return null;
    }
}
