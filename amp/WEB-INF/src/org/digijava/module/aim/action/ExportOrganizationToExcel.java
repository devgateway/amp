package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpContactPhoneProperty;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrgStaffInformation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpOrganizationBudgetInformation;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ExportOrganizationToExcel extends DispatchAction {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(ExportOrganizationToExcel.class);
    private final static char BULLETCHAR = '\u2022';
    private final static char NEWLINECHAR = '\n';
    private final static int COLUMN_WIDTH = 5120;
    private final static int TITLE_WIDTH = 8960;



    public ActionForward exportStaffInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=OrganizationStaffExport.xls");

        //XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);
        HSSFSheet sheet = wb.createSheet(sheetName);

         // setting width of columns
        sheet.setColumnWidth( 0, TITLE_WIDTH);
        for(int i=1; i<3; i++){
             sheet.setColumnWidth(i, COLUMN_WIDTH);
        }

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);


        HSSFCellStyle subHeaderCS = wb.createCellStyle();
        subHeaderCS.setWrapText(true);
        subHeaderCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeaderCS.setFont(fontSubHeader);
        subHeaderCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFCellStyle regularCS = wb.createCellStyle();
        regularCS.setWrapText(true);
        regularCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        regularCS.setFont(font);
        regularCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        regularCS.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        
        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);
        short rowNum = 0;
        int cellNum = 0;
        
        HSSFRow row = sheet.createRow(rowNum++);
        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("Staff Information for");
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getStaff() != null && editForm.getStaff().size() > 0) {            
            exportStaffInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
        }
        wb.write(response.getOutputStream());
        wb.close();
        return null;


    }

    public ActionForward exportBudgetInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=OrganizationBudgetExport.xls");

       // XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);

        HSSFSheet sheet = wb.createSheet(sheetName);
        // setting width of columns
        sheet.setColumnWidth((short) 0, TITLE_WIDTH);
        for(short i=1;i<5;i++){
             sheet.setColumnWidth(i , COLUMN_WIDTH);
        }

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle subHeaderCS = wb.createCellStyle();
        subHeaderCS.setWrapText(true);
        subHeaderCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeaderCS.setFont(fontSubHeader);
        subHeaderCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFCellStyle regularCS = wb.createCellStyle();
        regularCS.setWrapText(true);
        regularCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        regularCS.setFont(font);
        regularCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        regularCS.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);


        int rowNum = 0;
        int cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);
        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("Budget Information for");
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getOrgInfos() != null && editForm.getOrgInfos().size() > 0) {
            exportBudgetInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
        }
        wb.write(response.getOutputStream());
        wb.close();
        return null;


    }

    public ActionForward exportContactInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
       AddOrgForm editForm = (AddOrgForm) form;
       
       response.setContentType("application/vnd.ms-excel");
       response.setHeader("Content-disposition", "inline; filename=OrganizationContactExport.xls");
 //    XLSExporter.resetStyles();
        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle subHeaderCS = wb.createCellStyle();
        subHeaderCS.setWrapText(true);
        subHeaderCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeaderCS.setFont(fontSubHeader);
        subHeaderCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFCellStyle regularCS = wb.createCellStyle();
        regularCS.setWrapText(true);
        regularCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        regularCS.setFont(font);
        regularCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        regularCS.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        // setting width of columns
        sheet.setColumnWidth((short) 0, TITLE_WIDTH);
        for(short i=1;i<7;i++){
             sheet.setColumnWidth(i , COLUMN_WIDTH);
        }
        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);

        short rowNum = 0;
        int cellNum = 0;
        
        HSSFRow row = sheet.createRow(rowNum++);
        HSSFCell cell = row.createCell(cellNum);
        String headerPrefix = TranslatorWorker.translateText("Contact Information for");
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getOrgContacts()!= null &&editForm.getOrgContacts().size() > 0) {
            exportContactInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
        }
        wb.write(response.getOutputStream());
        wb.close();
        return null;


    }

    public ActionForward exportGeneralInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=OrganizationGeneraInformationExport.xls");

        //XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);
        HSSFSheet sheet = wb.createSheet(sheetName);
        // setting width of columns
        sheet.setColumnWidth((short) 0, TITLE_WIDTH);
         for(short i=1;i<4;i++){
             sheet.setColumnWidth(i , COLUMN_WIDTH);
        }

        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);


        HSSFCellStyle subHeaderCS = wb.createCellStyle();
        subHeaderCS.setWrapText(true);
        subHeaderCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeaderCS.setFont(fontSubHeader);
        subHeaderCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFCellStyle regularCS = wb.createCellStyle();
        regularCS.setWrapText(true);
        regularCS.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        regularCS.setFont(font);
        regularCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        regularCS.setAlignment(HSSFCellStyle.ALIGN_LEFT);


        short rowNum = 0;
        int cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);
        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("General Information for");
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);

        exportGeneralInfo(request,editForm,sheet, subHeaderCS,regularCS, rowNum);

        wb.write(response.getOutputStream());
        wb.close();
        return null;

    }

    public ActionForward exportNGOForm(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
         AddOrgForm editForm = (AddOrgForm) form;
         
         response.setContentType("application/vnd.ms-excel");
         response.setHeader("Content-disposition", "inline; filename=OrganizationGeneraInformationExport.xls");      
         
         
         HSSFWorkbook wb = new HSSFWorkbook();
//       String name = editForm.getName();
         String sheetName = getSheetName("NGO Form");
         HSSFSheet sheet = wb.createSheet(sheetName);
         
         HSSFCellStyle headerCS = wb.createCellStyle();
         headerCS.setWrapText(true);
         //headerCS.setFillForegroundColor(HSSFColor.BLUE.index);
         HSSFFont fontHeader = wb.createFont();
         fontHeader.setFontName(HSSFFont.FONT_ARIAL);
         fontHeader.setFontHeightInPoints((short) 14);
         fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
         fontHeader.setColor( HSSFColor.BLUE.index );
         headerCS.setAlignment(HSSFCellStyle.ALIGN_LEFT);
         headerCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
         headerCS.setFont(fontHeader);

         HSSFCellStyle subHeaderCS = wb.createCellStyle();
         subHeaderCS.setWrapText(true);
         subHeaderCS.setFillForegroundColor(HSSFColor.BLACK.index);
         HSSFFont fontSubHeader = wb.createFont();
         fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
         fontSubHeader.setFontHeightInPoints((short) 10);
         fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
         subHeaderCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
         subHeaderCS.setFont(fontSubHeader);

         HSSFCellStyle regularCS = wb.createCellStyle();
         regularCS.setWrapText(true);
         regularCS.setFillForegroundColor(HSSFColor.BLACK.index);
         HSSFFont font = wb.createFont();
         font.setFontName(HSSFFont.FONT_ARIAL);
         font.setFontHeightInPoints((short) 10);
         font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
         regularCS.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
         regularCS.setFont(font);
         
         int rowNum = 0;
         int cellNum = 0;
         String text=null;
         
//       Region r=new Region(rowNum, new Integer(cellNum).shortValue(),rowNum, new Integer(cellNum+8).shortValue()); //colspan for header
         CellRangeAddress r = new CellRangeAddress(rowNum, cellNum,rowNum, cellNum + 8);
         HSSFRow row = sheet.createRow(rowNum++);
         row.setHeightInPoints(row.getHeightInPoints()*2);
         HSSFCell cell = row.createCell(cellNum);
         HSSFRichTextString value=new HSSFRichTextString(TranslatorWorker.translateText("Organization Data"));
         cell.setCellStyle(headerCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         //Organization Name
         text = TranslatorWorker.translateText("Organization Name");
         setColspan(sheet, cellNum, text);
         row = sheet.createRow(rowNum++);        
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = editForm.getName();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         
         //Acronym
         text = TranslatorWorker.translateText("Organization Acronym");
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = editForm.getAcronym();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         
         // next row 
         cellNum=0;
         //org. type
         row = sheet.createRow(rowNum++);
         text = TranslatorWorker.translateText("Organization Type");
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = (DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId())).getOrgType();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);       
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         
         //org. group        
         text = TranslatorWorker.translateText("Organization Group");
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);       
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         if (editForm.getAmpOrgGrpId()!=null && editForm.getAmpOrgGrpId().intValue()>0){
             text = (DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId()).getOrgGrpName());
             setColspan(sheet, cellNum, text);
             cell = row.createCell(cellNum++);
             value=new HSSFRichTextString(text);
             cell.setCellStyle(regularCS);
             cell.setCellValue(value);
         }
        
         
         // next row 
         cellNum=0;
         //fund. org.id
         row = sheet.createRow(rowNum++);
         text = TranslatorWorker.translateText("Funding Org Id");
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = editForm.getFundingorgid();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         
         // next row 
         cellNum=0;
         //Organization Primary Purpose
         r = new CellRangeAddress(rowNum, cellNum + 1, rowNum, cellNum + 3);
//       r=new Region(rowNum, new Integer(cellNum+1).shortValue(),rowNum, new Integer(cellNum+3).shortValue()); //colspan for primary purpose        
         row = sheet.createRow(rowNum++);
         text = TranslatorWorker.translateText("Organization Primary Purpose ");
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = editForm.getOrgPrimaryPurpose();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         //next row
         cellNum=0;
         r = new CellRangeAddress(rowNum, cellNum, rowNum, cellNum + 8);
//       r=new Region(rowNum, new Integer(cellNum).shortValue(),rowNum, new Integer(cellNum+8).shortValue()); //colspan for header
         row = sheet.createRow(rowNum++);
         row.setHeightInPoints(row.getHeightInPoints()*2);
         cell = row.createCell(cellNum);
         value=new HSSFRichTextString(TranslatorWorker.translateText("General Information"));
         cell.setCellStyle(headerCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         rowNum = exportGeneralInfo(request,editForm,sheet,subHeaderCS, regularCS, rowNum);
         
         //next row      
         cellNum=0;
         r = new CellRangeAddress(rowNum, cellNum, rowNum, cellNum + 8);
//       r=new Region(rowNum, new Integer(cellNum).shortValue(),rowNum, new Integer(cellNum+8).shortValue()); //colspan for header
         row = sheet.createRow(rowNum++);
         row.setHeightInPoints(row.getHeightInPoints()*2);
         cell = row.createCell(cellNum);
         value=new HSSFRichTextString(TranslatorWorker.translateText("Staff Information "));
         cell.setCellStyle(headerCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         if (editForm.getStaff() != null && editForm.getStaff().size() > 0) {           
             rowNum = exportStaffInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
         }
         
         //next row      
         cellNum=0;
         r = new CellRangeAddress(rowNum, cellNum, rowNum, cellNum + 8);
//       r=new Region(rowNum, new Integer(cellNum).shortValue(),rowNum, new Integer(cellNum+8).shortValue()); //colspan for header
         row = sheet.createRow(rowNum++);
         row.setHeightInPoints(row.getHeightInPoints()*2);
         cell = row.createCell(cellNum);
         value=new HSSFRichTextString(TranslatorWorker.translateText("Budget Information "));
         cell.setCellStyle(headerCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         if (editForm.getOrgInfos() != null && editForm.getOrgInfos().size() > 0) {
             rowNum = exportBudgetInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
         }
         
         //next row
         cellNum=0;
         r = new CellRangeAddress(rowNum, cellNum, rowNum, cellNum + 8);
//       r=new Region(rowNum, new Integer(cellNum).shortValue(),rowNum, new Integer(cellNum+8).shortValue()); //colspan for header
         row = sheet.createRow(rowNum++);
         row.setHeightInPoints(row.getHeightInPoints()*2);
         cell = row.createCell(cellNum);
         value=new HSSFRichTextString(TranslatorWorker.translateText("Contact Information  "));
         cell.setCellStyle(headerCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         if (editForm.getOrgContacts()!= null &&editForm.getOrgContacts().size() > 0) {
             rowNum = exportContactInfo(request, editForm, sheet, subHeaderCS, regularCS,rowNum);
         }
         
         //next row
         cellNum=0;
         //Other Information
         r = new CellRangeAddress(rowNum, cellNum + 1, rowNum, cellNum + 3);
//       r=new Region(rowNum, new Integer(cellNum+1).shortValue(),rowNum, new Integer(cellNum+3).shortValue()); //colspan for Other Information     
         row = sheet.createRow(rowNum++);        
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(TranslatorWorker.translateText("Other Information "));
         cell.setCellStyle(subHeaderCS);
         cell.setCellValue(value);
         
         text = editForm.getOtherInformation();
         setColspan(sheet, cellNum, text);
         cell = row.createCell(cellNum++);
         value=new HSSFRichTextString(text);
         cell.setCellStyle(regularCS);
         cell.setCellValue(value);
         sheet.addMergedRegion(r);
         
         wb.write(response.getOutputStream());
         wb.close();
         return null;
     }
    
    private String getSheetName(String name) {
        String sheetName = null;
        if (name.length() > 31) {
            sheetName = name.substring(0, 31);
        } else {
            if (name.length() == 0) {
                // should not be possible, but still...
                sheetName = "blank";
            } else {
                sheetName = name;
            }
        }
        // replacing odd symbols for sheet name...
        sheetName = sheetName.replace("/", "|");
        sheetName = sheetName.replace("*", "+");
        sheetName = sheetName.replace("?", " ");
        sheetName = sheetName.replace("\\", "|");
        sheetName = sheetName.replace("[", "(");
        sheetName = sheetName.replace("]", ")");
        sheetName = sheetName.replace(":", "-");
        return sheetName;
    }
    
    private void setColspan (HSSFSheet sheet , int cellNum, String textToWrite) {
        int defaultColumnWidth = 2560; //at least 10 chars
        int newWidth = 0;
        if (textToWrite.length() > 10) {
            if(textToWrite.length()<35){
                newWidth = textToWrite.length()*256;
            }else{
                newWidth = 35*256;
            }
            
        }else {
            newWidth = defaultColumnWidth;
        }
        if (sheet.getColumnWidth(cellNum) < newWidth) {
            sheet.setColumnWidth(cellNum, newWidth);
        }
    }
    
    private int exportGeneralInfo(HttpServletRequest request,AddOrgForm editForm, HSSFSheet sheet,HSSFCellStyle subHeaderCS,HSSFCellStyle regularCS, int rowNum)throws WorkerException {
        HSSFRow row;
        HSSFCell cell;
        HSSFRichTextString value;
        int cellNum =0;
        
        //registration number       
        row = sheet.createRow(rowNum++);
        String text = TranslatorWorker.translateText("Registration Number in MinPlan");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getRegNumbMinPlan();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //legal personality number
        text = TranslatorWorker.translateText("Legal Personality Number");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getLegalPersonNum();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //new row
        cellNum=0;
        //reg. date
        row = sheet.createRow(rowNum++);
        text = TranslatorWorker.translateText("Registration Date in MinPlan");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getMinPlanRegDate();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //Legal Personality Registration Date in the country of origin
        text = TranslatorWorker.translateText("Legal Personality Registration Date in the country of origin");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getLegalPersonRegDate();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);   
        
        //new row       
        cellNum=0;
        row = sheet.createRow(rowNum++);
        text = TranslatorWorker.translateText("Operation approval  date in the country of origin");
        setColspan(sheet, cellNum, text);
        // Operation approval  date in the country of origin
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getOperFuncApprDate();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
         
        // Registration date of Line Ministry
        text = TranslatorWorker.translateText("Registration date of Line Ministry");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getLineMinRegDate();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);       
                
        //new row
        cellNum=0;      
        row = sheet.createRow(rowNum++);
        text = TranslatorWorker.translateText("Receipt of legal personality act/form in DRC");
        setColspan(sheet, cellNum, text);
        // Receipt of legal personality act/form in DRC
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getReceiptLegPersonalityAct();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        // Recipients
        text = TranslatorWorker.translateText("Recipients");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        List<AmpOrgRecipient> orgs=editForm.getRecipients();
        String organizations="";
        String longestOrgRecord = "";
        String currentRecord = null;
        if(orgs!=null){
            Iterator<AmpOrgRecipient> orgIter=orgs.iterator();
            while(orgIter.hasNext()){
                AmpOrgRecipient organisation=orgIter.next();
                currentRecord= BULLETCHAR+organisation.getOrganization().getName();
                if(organisation.getDescription()!=null&&!organisation.getDescription().trim().equals("")){
                    currentRecord+=" ("+organisation.getDescription()+")";
                }
                if(currentRecord.length() > longestOrgRecord.length()){
                    longestOrgRecord = currentRecord;
                }
                organizations+= currentRecord+NEWLINECHAR;

            }
        }
        
        setColspan(sheet, cellNum, longestOrgRecord);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(organizations);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //new row       
        cellNum=0;
        text = TranslatorWorker.translateText("Fiscal Calendar");
        setColspan(sheet, cellNum, text);
        row = sheet.createRow(rowNum++);
        //Fiscal Calendar
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
            
        if (editForm.getFiscalCalId() != null&&editForm.getFiscalCalId()!=-1) {
            text = FiscalCalendarUtil.getAmpFiscalCalendar(editForm.getFiscalCalId()).getName();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(text);
            cell.setCellValue(value);
            cell.setCellStyle(regularCS);
        }
        
        //new row
        cellNum=0;
        row = sheet.createRow(rowNum++);
        //Sector Prefernces
        text = TranslatorWorker.translateText("Sector Prefernces");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        String longestSectRecord = "";
        currentRecord = null;       
        Collection<ActivitySector> activitySectors = editForm.getSectors();
        String sectors="";
        if(activitySectors!=null){
            Iterator<ActivitySector> activitySectorsIter=activitySectors.iterator();
            while(activitySectorsIter.hasNext()){
                ActivitySector activitySector=activitySectorsIter.next();
                currentRecord = BULLETCHAR+activitySector.getSectorName(); 
                sectors+= currentRecord + NEWLINECHAR;
                if(currentRecord.length() > longestSectRecord.length()){
                    longestSectRecord = currentRecord;
                }
            }
        }
        setColspan(sheet, cellNum, longestSectRecord);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(sectors);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //Country of Origin
        text = TranslatorWorker.translateText("Country of Origin");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        
        if (editForm.getCountryId() != null && editForm.getCountryId() != -1) {
            text = DynLocationManagerUtil.getLocation(editForm.getCountryId(), false).getName();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(text);
            cell.setCellValue(value);
            cell.setCellStyle(regularCS);
        }
         
        //new row
        cellNum=0;      
        row = sheet.createRow(rowNum++);
        text = TranslatorWorker.translateText("Organization website ");
        setColspan(sheet, cellNum, text);
        // Organization website 
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);     
        
        text =editForm.getOrgUrl();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        // Tax Number
        text = TranslatorWorker.translateText("Tax Number ");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text =editForm.getTaxNumber();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        //new row
        cellNum=0;
        row = sheet.createRow(rowNum++);
        text =TranslatorWorker.translateText("Organization Headquarters Address ");
        setColspan(sheet, cellNum, text);
        // Organization Headquarters Address
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(editForm.getAddress());
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        text =TranslatorWorker.translateText("Organization Intervention Level ");           
        //  Organization Intervention Level 
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        
        if (editForm.getImplemLocationLevel() != null && editForm.getImplemLocationLevel() != 0) {
            text =CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getImplemLocationLevel()).getValue();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(text);
            cell.setCellValue(value);
            cell.setCellStyle(regularCS);
        }
         
        //new row
        cellNum=0;
        row = sheet.createRow(rowNum++);
        // Organization Address Abroad(Internation NGO
        text = TranslatorWorker.translateText("Organization Address Abroad(Internation NGO) ");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = editForm.getAddressAbroad();
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        //Organization Intervention Location
        text = TranslatorWorker.translateText("Organization Intervention Location ");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value=new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        
        Collection<Location> selectedLocations=editForm.getSelectedLocs();
        String locations="";
        String longestLocRecord = "";
        currentRecord = null;   
        if(selectedLocations!=null){
            Iterator<Location> locationIter=selectedLocations.iterator();
            while(locationIter.hasNext()){
                Location location=locationIter.next();
                currentRecord = BULLETCHAR+location.getAmpCVLocation().getName() ; 
                locations+= currentRecord +" ("+location.getPercent()+"%) "+NEWLINECHAR;
                if(currentRecord.length() > longestLocRecord.length()){
                    longestLocRecord = currentRecord;
                }

            }
        }
        setColspan(sheet, cellNum, longestLocRecord);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(locations);
        cell.setCellValue(value);
        cell.setCellStyle(regularCS);
        
        return rowNum;
    }
    
    private int exportBudgetInfo(HttpServletRequest request, AddOrgForm editForm, HSSFSheet sheet, HSSFCellStyle subHeaderCS,
            HSSFCellStyle regularCS, int rowNum) throws WorkerException {
        HSSFRow row;
        HSSFCell cell;
        int cellNum=0;
        HSSFRichTextString value = null;
        // table header
        row = sheet.createRow(rowNum++);
        String text = TranslatorWorker.translateText("Year");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value= new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("Type of Organization");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value= new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("Organization(s)");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value= new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("Amount");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value= new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("Currency");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value= new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        

        Iterator<AmpOrganizationBudgetInformation> budgetInfoIter = editForm.getOrgInfos().iterator();
        while (budgetInfoIter.hasNext()) {
            AmpOrganizationBudgetInformation budgetInfo = budgetInfoIter.next();
            cellNum = 0;
            row = sheet.createRow(rowNum++);            
            cell = row.createCell(cellNum++);
            cell.setCellValue(budgetInfo.getYear().toString());
            cell.setCellStyle(regularCS);
            
            text = TranslatorWorker.translateText(budgetInfo.getType().getValue());
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(text);
            cell.setCellValue(value);
            cell.setCellStyle(regularCS);
           
            String organizations="";
            String longestOrgRecord = "";
            String currentRecord = null;
            if(budgetInfo.getOrganizations()!=null){
                for(AmpOrganisation organisation:budgetInfo.getOrganizations() ){
                    currentRecord = BULLETCHAR+organisation.getName();
                    organizations+= currentRecord + NEWLINECHAR;
                    if(currentRecord.length() > longestOrgRecord.length()){
                        longestOrgRecord = currentRecord;
                    }
                }
            }
            setColspan(sheet, cellNum, longestOrgRecord);
            cell = row.createCell(cellNum++);
            HSSFRichTextString organizationsValue = new HSSFRichTextString(organizations);
            cell.setCellValue(organizationsValue);
            cell.setCellStyle(regularCS);
            
            cell = row.createCell(cellNum++);
            cell.setCellValue(budgetInfo.getAmount().toString());
            cell.setCellStyle(regularCS);
            
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(budgetInfo.getCurrency().getCurrencyCode()));
            cell.setCellStyle(regularCS);

        }
        return rowNum;
    }
    
    private int exportStaffInfo(HttpServletRequest request,AddOrgForm editForm, HSSFSheet sheet, HSSFCellStyle subHeaderCS,HSSFCellStyle regularCS, int rowNum) throws WorkerException {
        HSSFRow row;
        HSSFCell cell;
        int cellNum =0;
        HSSFRichTextString value =null;     
    
        // table header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(TranslatorWorker.translateText("Year"));
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);     
        
        String text = TranslatorWorker.translateText("Type Of Staff");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("Number Of Staff");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        value = new HSSFRichTextString(text);
        cell.setCellValue(value);
        cell.setCellStyle(subHeaderCS);

        Iterator<AmpOrgStaffInformation> staffInfoIter = editForm.getStaff().iterator();
        while (staffInfoIter.hasNext()) {
            AmpOrgStaffInformation staffInfo = staffInfoIter.next();
            cellNum = 0;
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            cell.setCellValue(staffInfo.getYear().toString());
            cell.setCellStyle(regularCS);
            
            text = TranslatorWorker.translateText(staffInfo.getType().getValue());
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(text);
            cell.setCellValue(value);
            cell.setCellStyle(regularCS);
            
            text = staffInfo.getStaffNumber().toString();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            cell.setCellValue(text);
            cell.setCellStyle(regularCS);

        }
        return rowNum;
    }

    private int exportContactInfo(HttpServletRequest request,AddOrgForm editForm, HSSFSheet sheet, HSSFCellStyle subHeaderCS,HSSFCellStyle regularCS, int rowNum) throws WorkerException {
        HSSFRow row;
        HSSFCell cell;
        int cellNum =0;
        // table header
        row = sheet.createRow(rowNum++);
        
        String text = TranslatorWorker.translateText("LAST NAME");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("FIRST NAME");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("EMAIL");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("TELEPHONE");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("FAX");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("TITLE");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        text = TranslatorWorker.translateText("PRIMARY CONTACT");
        setColspan(sheet, cellNum, text);
        cell = row.createCell(cellNum++);
        cell.setCellValue(text);
        cell.setCellStyle(subHeaderCS);
        
        String primary= TranslatorWorker.translateText("Primary");
        String nonPrimary= TranslatorWorker.translateText("Non Primary Contact");

        Iterator<AmpOrganisationContact> contactInfoIter = editForm.getOrgContacts().iterator();
        while (contactInfoIter.hasNext()) {
            AmpOrganisationContact orgContact = contactInfoIter.next();
            cellNum = 0;
            row = sheet.createRow(rowNum++);
            text = orgContact.getContact().getLastname();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(text));
            cell.setCellStyle(regularCS);
            
            text = orgContact.getContact().getName();
            setColspan(sheet, cellNum, text);
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(text));
            cell.setCellStyle(regularCS);
                    
            
            String emails="";
            String longestEmailRecord = "";         
            String phones="";
            String longestPhoneRecord = "";
            String faxes="";
            String longestFaxRecord = "";
            String currentRecord = null;
            for (AmpContactProperty property : orgContact.getContact().getProperties()) {
                if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
                    currentRecord = property.getValue(); 
                    emails+= currentRecord + ";\n";
                    if(currentRecord.length() > longestEmailRecord.length()){
                        longestEmailRecord = currentRecord;
                    }
                }else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
                    String phoneCategory = ((AmpContactPhoneProperty) property).getPhoneCategory();
                    currentRecord = TranslatorWorker.translateText(phoneCategory) + " " + property.getValue();
                    phones+= currentRecord + ";\n";
                    if(currentRecord.length() > longestEmailRecord.length()){
                        longestPhoneRecord = currentRecord;
                    }
                }else{
                    currentRecord = property.getValue(); 
                    faxes+=currentRecord+";\n";
                    if(currentRecord.length() > longestEmailRecord.length()){
                        longestFaxRecord = currentRecord;
                    }
                }
            }
            
            setColspan(sheet, cellNum, longestEmailRecord);
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(emails));
            cell.setCellStyle(regularCS);
            
            setColspan(sheet, cellNum, longestPhoneRecord);
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(phones));
            cell.setCellStyle(regularCS);
            
            setColspan(sheet, cellNum, longestFaxRecord);
            cell = row.createCell(cellNum++);
            cell.setCellValue(new HSSFRichTextString(faxes));
            cell.setCellStyle(regularCS);
            cell = row.createCell(cellNum++);
            String title="";
            if(orgContact.getContact().getTitle()!=null){
                title=orgContact.getContact().getTitle().getValue();
            }
            cell.setCellValue(new HSSFRichTextString(title));
            cell.setCellStyle(regularCS);
            
            cell = row.createCell(cellNum++);
            String isprimaryContact=nonPrimary;
            if(orgContact.getPrimaryContact()!=null&&orgContact.getPrimaryContact()){
                isprimaryContact=primary;
            }
            setColspan(sheet, cellNum, isprimaryContact);
            cell.setCellValue(new HSSFRichTextString(isprimaryContact));
            cell.setCellStyle(regularCS);
        }
        return rowNum;
    }
}
