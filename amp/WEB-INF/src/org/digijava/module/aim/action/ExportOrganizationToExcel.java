package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.form.AddOrgForm;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrgStaffInformation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpOrganizationBudgetInformation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ExportOrganizationToExcel extends DispatchAction {

    private static Logger logger = Logger.getLogger(ExportOrganizationToExcel.class);
    private final static char BULLETCHAR = '\u2022';
    private final static char NEWLINECHAR = '\n';
    private final static short COLUMN_WIDTH=5120;
    private final static short TITLE_WIDTH=8960;



    public ActionForward exportStaffInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;
         String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=OrganizationStaffExport.xls");

        //XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);

        HSSFSheet sheet = wb.createSheet(sheetName);

         // setting width of columns
        sheet.setColumnWidth((short) 0, TITLE_WIDTH);
        for(short i=1;i<3;i++){
             sheet.setColumnWidth(i , COLUMN_WIDTH);
        }



        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);


        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 12);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);



        short rowNum = 0;
        short cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);

        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("Staff Information for", locale, siteId);
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getStaff() != null && editForm.getStaff().size() > 0) {

            // table header
            row = sheet.createRow(rowNum++);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Year", locale, siteId);
            HSSFRichTextString headerYear = new HSSFRichTextString(headerPrefix);
            headerYear.applyFont(fontSubHeader);
            cell.setCellValue(headerYear);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Type Of Staff", locale, siteId);
            HSSFRichTextString headerType = new HSSFRichTextString(headerPrefix);
            headerType.applyFont(fontSubHeader);
            cell.setCellValue(headerType);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Number Of Staff", locale, siteId);
            HSSFRichTextString headerNumber = new HSSFRichTextString(headerPrefix);
            headerNumber.applyFont(fontSubHeader);
            cell.setCellValue(headerNumber);
            cell.setCellStyle(style);

            Iterator<AmpOrgStaffInformation> staffInfoIter = editForm.getStaff().iterator();
            while (staffInfoIter.hasNext()) {
                AmpOrgStaffInformation staffInfo = staffInfoIter.next();
                cellNum = 0;
                row = sheet.createRow(rowNum++);
                cell = row.createCell(cellNum++);
                cell.setCellValue(staffInfo.getYear());
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                String value = TranslatorWorker.translateText(staffInfo.getType().getValue(), locale, siteId);
                HSSFRichTextString typeValue = new HSSFRichTextString(value);
                cell.setCellValue(typeValue);
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(staffInfo.getStaffNumber());
                cell.setCellStyle(style);

            }
        }
        wb.write(response.getOutputStream());
        return null;


    }

    public ActionForward exportBudgetInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();

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


        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 12);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);



        short rowNum = 0;
        short cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);

        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("Budget Information for", locale, siteId);
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getOrgInfos() != null && editForm.getOrgInfos().size() > 0) {

            // table header
            row = sheet.createRow(rowNum++);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Year", locale, siteId);
            HSSFRichTextString headerYear = new HSSFRichTextString(headerPrefix);
            headerYear.applyFont(fontSubHeader);
            cell.setCellValue(headerYear);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Type of Organization", locale, siteId);
            HSSFRichTextString headerType = new HSSFRichTextString(headerPrefix);
            headerType.applyFont(fontSubHeader);
            cell.setCellValue(headerType);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization(s)", locale, siteId);
            HSSFRichTextString headerPercent = new HSSFRichTextString(headerPrefix);
            headerPercent.applyFont(fontSubHeader);
            cell.setCellValue(headerPercent);
            cell.setCellStyle(style);


            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Amount", locale, siteId);
            HSSFRichTextString headerNumber = new HSSFRichTextString(headerPrefix);
            headerNumber.applyFont(fontSubHeader);
            cell.setCellValue(headerNumber);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Currency", locale, siteId);
            HSSFRichTextString headerCurrency = new HSSFRichTextString(headerPrefix);
            headerCurrency.applyFont(fontSubHeader);
            cell.setCellValue(headerCurrency);
            cell.setCellStyle(style);

            Iterator<AmpOrganizationBudgetInformation> budgetInfoIter = editForm.getOrgInfos().iterator();
            while (budgetInfoIter.hasNext()) {
                AmpOrganizationBudgetInformation budgetInfo = budgetInfoIter.next();
                cellNum = 0;
                row = sheet.createRow(rowNum++);
                cell = row.createCell(cellNum++);
                cell.setCellValue(budgetInfo.getYear());
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                String value = TranslatorWorker.translateText(budgetInfo.getType().getValue(), locale, siteId);
                HSSFRichTextString typeValue = new HSSFRichTextString(value);
                cell.setCellValue(typeValue);
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                String organizations="";
                if(budgetInfo.getOrganizations()!=null){
                    for(AmpOrganisation organisation:budgetInfo.getOrganizations() ){
                         organizations+=BULLETCHAR+organisation.getName()+NEWLINECHAR;

                    }
                }
                HSSFRichTextString organizationsValue = new HSSFRichTextString(organizations);
                cell.setCellValue(organizationsValue);
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(budgetInfo.getAmount());
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(budgetInfo.getCurrency().getCurrencyCode()));
                cell.setCellStyle(style);

            }
        }
        wb.write(response.getOutputStream());
        return null;


    }

    public ActionForward exportContactInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
       AddOrgForm editForm = (AddOrgForm) form;
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=OrganizationContactExport.xls");

 //       XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();
        String name = editForm.getName();
        String sheetName = getSheetName(name);
        HSSFSheet sheet = wb.createSheet(sheetName);



        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);


        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 12);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        // setting width of columns
        sheet.setColumnWidth((short) 0, TITLE_WIDTH);
        for(short i=1;i<7;i++){
             sheet.setColumnWidth(i , COLUMN_WIDTH);
        }
        HSSFCellStyle style= wb.createCellStyle();
        style.setWrapText(true);

        short rowNum = 0;
        short cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);

        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("Contact Information for", locale, siteId);
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);


        if (editForm.getOrgContacts()!= null &&editForm.getOrgContacts().size() > 0) {

            // table header
            row = sheet.createRow(rowNum++);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("LAST NAME", locale, siteId);
            HSSFRichTextString headerlLastname = new HSSFRichTextString(headerPrefix);
            headerlLastname.applyFont(fontSubHeader);
            cell.setCellValue(headerlLastname);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("FIRST NAME", locale, siteId);
            HSSFRichTextString headerFirstName = new HSSFRichTextString(headerPrefix);
            headerFirstName.applyFont(fontSubHeader);
            cell.setCellValue(headerFirstName);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("EMAIL", locale, siteId);
            HSSFRichTextString headerEmail = new HSSFRichTextString(headerPrefix);
            headerEmail.applyFont(fontSubHeader);
            cell.setCellValue(headerEmail);
            cell.setCellStyle(style);


            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("TELEPHONE", locale, siteId);
            HSSFRichTextString headerTele = new HSSFRichTextString(headerPrefix);
            headerTele.applyFont(fontSubHeader);
            cell.setCellValue(headerTele);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("FAX", locale, siteId);
            HSSFRichTextString headerFax = new HSSFRichTextString(headerPrefix);
            headerFax.applyFont(fontSubHeader);
            cell.setCellValue(headerFax);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("TITLE", locale, siteId);
            HSSFRichTextString headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);

            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("PRIMARY CONTACT", locale, siteId);
            HSSFRichTextString headerPrimaryContact = new HSSFRichTextString(headerPrefix);
            headerPrimaryContact.applyFont(fontSubHeader);
            cell.setCellValue(headerPrimaryContact);
            cell.setCellStyle(style);
            String primary= TranslatorWorker.translateText("Primary", locale, siteId);
            String nonPrimary= TranslatorWorker.translateText("Non Primary Contact", locale, siteId);

            Iterator<AmpOrganisationContact> contactInfoIter = editForm.getOrgContacts().iterator();
            while (contactInfoIter.hasNext()) {
            	AmpOrganisationContact orgContact = contactInfoIter.next();
                cellNum = 0;
                row = sheet.createRow(rowNum++);
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(orgContact.getContact().getLastname()));
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(orgContact.getContact().getName()));
                cell.setCellStyle(style);
                String emails="";
                String phones="";
                String faxes="";
                for (AmpContactProperty property : orgContact.getContact().getProperties()) {
					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
						emails+=property.getValue()+";\n";
					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
						phones+= TranslatorWorker.translateText(property.getPhoneCategory(), locale, siteId)+property.getActualPhoneNumber()+";\n";
					}else{
						faxes+=property.getValue()+";\n";
					}
				}
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(emails));
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(phones));
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                cell.setCellValue(new HSSFRichTextString(faxes));
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                String title="";
                if(orgContact.getContact().getTitle()!=null){
                    title=orgContact.getContact().getTitle().getValue();
                }
                cell.setCellValue(new HSSFRichTextString(title));
                cell.setCellStyle(style);
                cell = row.createCell(cellNum++);
                String isprimaryContact=nonPrimary;
                if(orgContact.getPrimaryContact()!=null&&orgContact.getPrimaryContact()){
                    isprimaryContact=primary;
                }
                cell.setCellValue(new HSSFRichTextString(isprimaryContact));

            }
        }
        wb.write(response.getOutputStream());
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

     public ActionForward exportGeneralInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddOrgForm editForm = (AddOrgForm) form;
         String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();

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


        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);


        short rowNum = 0;
        short cellNum = 0;
        HSSFRow row = sheet.createRow(rowNum++);

        HSSFCell cell = row.createCell(cellNum);

        String headerPrefix = TranslatorWorker.translateText("General Information for", locale, siteId);
        HSSFRichTextString header = new HSSFRichTextString(headerPrefix + "  " + name);
        header.applyFont(fontHeader);
        cell.setCellValue(header);
        cell.setCellStyle(style);

            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Registration Number in MinPlan", locale, siteId);
            HSSFRichTextString  headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            HSSFRichTextString  value = new HSSFRichTextString(editForm.getRegNumbMinPlan());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Legal Personality Number", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getLegalPersonNum());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            
            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Registration Date in MinPlan", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getMinPlanRegDate());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Legal Personality Registration Date in the country of origin", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getLegalPersonRegDate());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);

           
            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Operation approval  date in the country of origin ", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getOperFuncApprDate());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Registration date of Line Ministry ", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getLineMinRegDate());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);

            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Receipt of legal personality act/form in DRC", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getReceiptLegPersonalityAct());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Recipients", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            List<AmpOrgRecipient> orgs=editForm.getRecipients();
            String organizations="";
            if(orgs!=null){
                Iterator<AmpOrgRecipient> orgIter=orgs.iterator();
                while(orgIter.hasNext()){
                    AmpOrgRecipient organisation=orgIter.next();
                    organizations+=BULLETCHAR+organisation.getOrganization().getName()+" ("+organisation.getDescription()+")"+NEWLINECHAR;

                }
            }
          
            value = new HSSFRichTextString(organizations);
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);

            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Fiscal Calendar", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            if (editForm.getFiscalCalId() != null&&editForm.getFiscalCalId()!=-1) {
             value = new HSSFRichTextString(FiscalCalendarUtil.getAmpFiscalCalendar(editForm.getFiscalCalId()).getName());
             value.applyFont(font);
             cell.setCellValue(value);
             cell.setCellStyle(style);
            }

            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Sector Prefernces", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            Collection<ActivitySector> activitySectors=editForm.getSectors();
            String sectors="";
            if(activitySectors!=null){
                Iterator<ActivitySector> activitySectorsIter=activitySectors.iterator();
                while(activitySectorsIter.hasNext()){
                    ActivitySector activitySector=activitySectorsIter.next();
                    sectors+=BULLETCHAR+activitySector.getSectorName()+NEWLINECHAR;

                }
            }
            value = new HSSFRichTextString(sectors);
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Country of Origin", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell = row.createCell(cellNum++);
            if (editForm.getCountryId() != null && editForm.getCountryId() != -1) {
             value = new HSSFRichTextString(DynLocationManagerUtil.getLocation(editForm.getCountryId(), false).getName());
             value.applyFont(font);
             cell.setCellValue(value);
             cell.setCellStyle(style);
         }
           


            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization website", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getOrgUrl());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Tax Number", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getTaxNumber());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);

            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization Headquarters Address", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getAddress());
            value.applyFont(font);
            cell.setCellValue(value);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization Intervention Level", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            if (editForm.getImplemLocationLevel() != null && editForm.getImplemLocationLevel() != 0) {
             value = new HSSFRichTextString(CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getImplemLocationLevel()).getValue());
             value.applyFont(font);
             cell.setCellValue(value);
             cell.setCellStyle(style);

            }
           

            row = sheet.createRow(rowNum++);
            cellNum=0;
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization Address Abroad(Internation NGO)", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellStyle(style);
            cell.setCellValue(headerTitle);
            cell = row.createCell(cellNum++);
            value = new HSSFRichTextString(editForm.getAddressAbroad());
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            headerPrefix = TranslatorWorker.translateText("Organization Intervention Location", locale, siteId);
            headerTitle = new HSSFRichTextString(headerPrefix);
            headerTitle.applyFont(fontSubHeader);
            cell.setCellValue(headerTitle);
            cell.setCellStyle(style);
            cell = row.createCell(cellNum++);
            Collection<Location> selectedLocations=editForm.getSelectedLocs();
            String locations="";
            if(selectedLocations!=null){
                Iterator<Location> locationIter=selectedLocations.iterator();
                while(locationIter.hasNext()){
                    Location location=locationIter.next();
                    locations+=BULLETCHAR+location.getAmpCVLocation().getName()+NEWLINECHAR;

                }
            }
            value = new HSSFRichTextString(locations);
            value.applyFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(style);
            

        wb.write(response.getOutputStream());
        return null;


    }
}
