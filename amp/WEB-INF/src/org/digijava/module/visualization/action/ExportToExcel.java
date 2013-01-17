package org.digijava.module.visualization.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.util.DbUtil;
import javax.servlet.ServletContext;
import org.digijava.module.aim.util.SectorUtil;

import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.rtf.table.RtfCell;

public class ExportToExcel extends Action {

	private static Logger logger = Logger.getLogger(ExportToExcel.class);
    private final static char BULLETCHAR = '\u2022';
    private final static char NEWLINECHAR = '\n';
    private final static short COLUMN_WIDTH=5120;
    private final static short TITLE_WIDTH=8960;

    String orgInfoTrn = "";
	String contactInfoTrn = "";
	String addNotesTrn = "";
	String nameTrn = "";
	String titleTrn = "";
	String emailsTrn = "";
	String phonesTrn = "";
	String faxesTrn = "";
	String backOrgTrn = "";
	String descriptionTrn = "";
	String pageTrn = "";
	String filtersTrn = "";
	String filtersAllTrn = "";
	String filtersAmountsInTrn = "";
	String filtersCurrencyTypeTrn = "";
	String filtersStartYearTrn = "";
	String filtersEndYearTrn = "";
	String filtersOrgGroupTrn = "";
	String filtersOrganizationsTrn = "";
	String filtersSectorsTrn = "";
	String filtersSubSectorsTrn = "";
	String filtersRegionsTrn = "";
	String filtersZonesTrn = "";
	String filtersLocationsTrn = "";
	String fundingTrn = "";
    String ODAGrowthTrn = "";
    String topPrjTrn = "";
    String topOrganizationTrn = "";
    String topRegionTrn = "";
    String projectTrn = "";
    String sectorTrn = "";
    String organizationTrn = "";
    String regionTrn = "";
    String NPOTrn = "";
    String programTrn = "";
    String aidPredTrn = "";
    String aidPredQuarterTrn = "";
    String aidTypeTrn = "";
    String budgetBreakdownTrn = "";
    String finInstTrn = "";
    String sectorProfTrn = "";
    String regionProfTrn = "";
    String NPOProfTrn = "";
    String programProfTrn = "";
    String organizationProfTrn = "";
    String beneficiaryAgencyProfTrn = "";
    String plannedTrn = "";
    String actualTrn = "";
    String yearTrn = "";
    String dashboardTrn = "";
    String summaryTrn = "";
    String totalCommsTrn = "";
    String totalDisbsTrn = "";
    String numberPrjTrn = "";
    String numberSecTrn = "";
    String numberDonTrn = "";
    String numberRegTrn = "";
    String avgPrjZSizeTrn = "";
    String currName = "";
    String fundTypeTrn = "";
    String dashboardTypeTrn = "";
    String topSectorTrn = "";
    String quarterTrn = "";
    HSSFSheet sheet1 = null;
    HSSFSheet sheet2 = null;
    HSSFSheet sheet3 = null;
    HSSFSheet sheet4 = null;
    HSSFSheet sheet5 = null;
    HSSFSheet sheet6 = null;
    HSSFSheet sheet7 = null;
    HSSFSheet sheet8 = null;
    HSSFSheet sheet9 = null;
    HSSFSheet sheet10 = null;
    HSSFSheet sheet11 = null;
    HSSFSheet sheet12 = null;
    HSSFSheet sheet13 = null;
    HSSFCellStyle headerCS = null;
    HSSFCellStyle subHeaderCS = null;
    HSSFCellStyle lastCellStyle = null;
    HSSFCellStyle lastCellStyleLeft = null;
    HSSFCellStyle cellStyle = null;
    HSSFCellStyle cellStyleLeft = null;
    HSSFRichTextString headerText = null;
    HSSFCellStyle titleCS = null;
    int rowNum=0;
    int cellNum=0;
    HSSFRow row = null;
    HSSFCell cell = null;
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=dashboardExport.xls");
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        VisualizationForm vForm = (VisualizationForm) form;
        String ODAGrowthOpt = request.getParameter("ODAGrowthOpt");
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidPredicQuarterOpt = request.getParameter("aidPredicQuarterOpt");
        String budgetBreakdownOpt = request.getParameter("budgetBreakdownOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String financingInstOpt = request.getParameter("financingInstOpt");
        String organizationOpt = request.getParameter("organizationOpt");
        String beneficiaryAgencyOpt = request.getParameter("beneficiaryAgencyOpt");
        String sectorOpt = request.getParameter("sectorOpt");
        String regionOpt = request.getParameter("regionOpt");
        String NPOOpt = request.getParameter("NPOOpt");
        String programOpt = request.getParameter("programOpt");
        String summaryOpt = request.getParameter("summaryOpt");
    
    	try {
    		orgInfoTrn = TranslatorWorker.translateText("Organization Information", langCode, siteId);
        	contactInfoTrn = TranslatorWorker.translateText("Contact Information", langCode, siteId);
        	addNotesTrn = TranslatorWorker.translateText("Additional Notes", langCode, siteId);
        	nameTrn = TranslatorWorker.translateText("Name", langCode, siteId);
        	titleTrn = TranslatorWorker.translateText("Title", langCode, siteId);
        	emailsTrn = TranslatorWorker.translateText("Emails", langCode, siteId);
        	phonesTrn = TranslatorWorker.translateText("Phones", langCode, siteId);
        	faxesTrn = TranslatorWorker.translateText("Faxes", langCode, siteId);
        	backOrgTrn = TranslatorWorker.translateText("Background of organization", langCode, siteId);
        	descriptionTrn = TranslatorWorker.translateText("Description", langCode, siteId);
			filtersTrn = TranslatorWorker.translateText("Filters", langCode, siteId);
			filtersAllTrn = TranslatorWorker.translateText("All", langCode, siteId);
			filtersAmountsInTrn = ""; 
			if(vForm.getFilter().getShowAmountsInThousands() != null && vForm.getFilter().getShowAmountsInThousands())
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in thousands", langCode, siteId);
			else
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in millions", langCode, siteId);
			filtersCurrencyTypeTrn = TranslatorWorker.translateText("Currency Type", langCode, siteId);
			filtersStartYearTrn = TranslatorWorker.translateText("Start Year", langCode, siteId);
			filtersEndYearTrn = TranslatorWorker.translateText("End Year", langCode, siteId);
			filtersOrgGroupTrn = TranslatorWorker.translateText("Organization Groups", langCode, siteId);
			filtersOrganizationsTrn = TranslatorWorker.translateText("Organizations", langCode, siteId);
			filtersSectorsTrn = TranslatorWorker.translateText("Sectors", langCode, siteId);
			filtersLocationsTrn = TranslatorWorker.translateText("Locations", langCode, siteId);
			filtersSubSectorsTrn = TranslatorWorker.translateText("Sub-Sectors", langCode, siteId);
			filtersRegionsTrn = TranslatorWorker.translateText("Regions", langCode, siteId);
			filtersZonesTrn = TranslatorWorker.translateText("Zones", langCode, siteId);
			ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth", langCode, siteId);
			fundingTrn = TranslatorWorker.translateText("Funding", langCode, siteId);
	        topPrjTrn = TranslatorWorker.translateText("Top Projects", langCode, siteId);
	        topSectorTrn = TranslatorWorker.translateText("Top Sectors", langCode, siteId);
	        topOrganizationTrn = TranslatorWorker.translateText("Top Organizations", langCode, siteId);
	        topRegionTrn = TranslatorWorker.translateText("Top Regions", langCode, siteId);
	        projectTrn = TranslatorWorker.translateText("Project", langCode, siteId);
	        sectorTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
	        organizationTrn = TranslatorWorker.translateText("Organization", langCode, siteId);
	        NPOTrn = TranslatorWorker.translateText("NPO", langCode, siteId);
	        budgetBreakdownTrn = TranslatorWorker.translateText("Budget Breakdown", langCode, siteId);
            programTrn = TranslatorWorker.translateText("Program", langCode, siteId);
	        regionTrn = TranslatorWorker.translateText("Region", langCode, siteId);
	        aidPredTrn = TranslatorWorker.translateText("Aid Predictability", langCode, siteId);
	        aidPredQuarterTrn = TranslatorWorker.translateText("Aid Predictability Quarterly", langCode, siteId);
	        aidTypeTrn = TranslatorWorker.translateText("Aid Type", langCode, siteId);
	        finInstTrn = TranslatorWorker.translateText("Financing Instrument", langCode, siteId);
	        sectorProfTrn = TranslatorWorker.translateText("Sector Profile", langCode, siteId);
	        regionProfTrn = TranslatorWorker.translateText("Region Profile", langCode, siteId);
	        organizationProfTrn = TranslatorWorker.translateText("Organization Profile", langCode, siteId);
	        beneficiaryAgencyProfTrn = TranslatorWorker.translateText("Beneficiary Agency Profile", langCode, siteId);
	        NPOProfTrn = TranslatorWorker.translateText("NPO Profile", langCode, siteId);
	        programProfTrn = TranslatorWorker.translateText("Program Profile", langCode, siteId);
	        plannedTrn = TranslatorWorker.translateText("Planned", langCode, siteId);
	        actualTrn = TranslatorWorker.translateText("Actual", langCode, siteId);
	        yearTrn = TranslatorWorker.translateText("Year", langCode, siteId);
	        quarterTrn = TranslatorWorker.translateText("Quarter", langCode, siteId);
	        dashboardTrn = TranslatorWorker.translateText("Dashboard", langCode, siteId);
	        summaryTrn = TranslatorWorker.translateText("Summary", langCode, siteId);
	        totalCommsTrn = TranslatorWorker.translateText("Total Commitments", langCode, siteId);
	        totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements", langCode, siteId);
	        numberPrjTrn = TranslatorWorker.translateText("Number of Projects", langCode, siteId);
	        numberSecTrn = TranslatorWorker.translateText("Number of Sectors", langCode, siteId);
	        numberDonTrn = TranslatorWorker.translateText("Number of Organizations", langCode, siteId);
	        numberRegTrn = TranslatorWorker.translateText("Number of Regions", langCode, siteId);
	        avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size", langCode, siteId);
	        currName = vForm.getFilter().getCurrencyCode();
	        fundTypeTrn = "";
	        switch (vForm.getFilter().getTransactionType()) {
				case Constants.COMMITMENT:
					fundTypeTrn = TranslatorWorker.translateText("Commitments", langCode, siteId);
					break;
				case Constants.DISBURSEMENT:
					fundTypeTrn = TranslatorWorker.translateText("Disbursements", langCode, siteId);
					break;
				case Constants.EXPENDITURE:
					fundTypeTrn = TranslatorWorker.translateText("Expenditures", langCode, siteId);
					break;
				default:
					fundTypeTrn = TranslatorWorker.translateText("Values", langCode, siteId);
				break;
			}
	        dashboardTypeTrn = "";
	        switch (vForm.getFilter().getDashboardType()) {
	            case org.digijava.module.visualization.util.Constants.DashboardType.DONOR:
	            	dashboardTypeTrn = TranslatorWorker.translateText("Organization", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.SECTOR:
					dashboardTypeTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.REGION:
					dashboardTypeTrn = TranslatorWorker.translateText("Region", langCode, siteId);
					break;
			
			}
    	
	        HSSFWorkbook wb = new HSSFWorkbook();
	       
	        // normal cells
	        cellStyle = wb.createCellStyle();
	        cellStyle.setWrapText(true);
	        HSSFFont fontCell = wb.createFont();
	        fontCell.setFontName(HSSFFont.FONT_ARIAL);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        HSSFDataFormat df = wb.createDataFormat();
	        cellStyle.setDataFormat(df.getFormat("General"));
	        cellStyle.setFont(fontCell);
	        cellStyle.setWrapText(true);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	        
	        cellStyleLeft = wb.createCellStyle();
	        cellStyleLeft.setWrapText(true);
	        cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        cellStyleLeft.setDataFormat(df.getFormat("General"));
	        cellStyleLeft.setFont(fontCell);
	        cellStyleLeft.setWrapText(true);
	        cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	        
	        lastCellStyle = wb.createCellStyle();
	        lastCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setFont(fontCell);
	        lastCellStyle.setWrapText(true);
	        lastCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        lastCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	        lastCellStyleLeft = wb.createCellStyle();
	        lastCellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        lastCellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        lastCellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        lastCellStyleLeft.setFont(fontCell);
	        lastCellStyleLeft.setWrapText(true);
	        lastCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        lastCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

	     // title cells
	        titleCS = wb.createCellStyle();
	        titleCS.setWrapText(true);
	        HSSFFont fontTitle = wb.createFont();
	        fontTitle.setFontName(HSSFFont.FONT_ARIAL);
	        fontTitle.setFontHeightInPoints((short) 14);
	        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        titleCS.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
	        titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        titleCS.setFont(fontTitle);

	     // header cells
	        headerCS = wb.createCellStyle();
	        headerCS.setWrapText(true);
	        HSSFFont fontHeader = wb.createFont();
	        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
	        fontHeader.setFontHeightInPoints((short) 12);
	        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        headerCS.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	        headerCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        headerCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        headerCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        headerCS.setFont(fontHeader);

	     // subHeader cells
	        subHeaderCS = wb.createCellStyle();
	        subHeaderCS.setWrapText(true);
	        HSSFFont fontSubHeader = wb.createFont();
	        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
	        fontSubHeader.setFontHeightInPoints((short) 10);
	        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        subHeaderCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        subHeaderCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        subHeaderCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        subHeaderCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        subHeaderCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        subHeaderCS.setFont(fontHeader);
	        
	     // subHeader cells
	        HSSFCellStyle subHeaderNumericCS = wb.createCellStyle();
	        subHeaderCS.setWrapText(true);
	        HSSFFont fontSubHeaderNumeric = wb.createFont();
	        fontSubHeaderNumeric.setFontName(HSSFFont.FONT_ARIAL);
	        fontSubHeaderNumeric.setFontHeightInPoints((short) 10);
	        fontSubHeaderNumeric.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        subHeaderNumericCS.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	        subHeaderNumericCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        subHeaderNumericCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        subHeaderNumericCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        subHeaderNumericCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        subHeaderNumericCS.setFont(fontHeader);
	        
	        String name =  dashboardTypeTrn + " " + dashboardTrn;
	        //String sheetName = "Dashboard";
	
	        HSSFSheet sheet = wb.createSheet(summaryTrn + " & " + projectTrn);
	        
	        String[] singleRow = null;
	        int rowNum = 1;
	        int cellNum = 0;
	        HSSFRow row = sheet.createRow(rowNum++);
	        rowNum++;
            HSSFCell cell = row.createCell(cellNum);
            //sheet.addMergedRegion(new CellRangeAddress(1,1,0,5));
            HSSFRichTextString header;
            if(vForm.getDashboard()!=null){
            	header = new HSSFRichTextString(vForm.getDashboard().getName());
            } else {
            	header = new HSSFRichTextString(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn.toUpperCase());
            }
	        cell.setCellValue(header);
	        cell.setCellStyle(titleCS);
	        
	      //Org. Information
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	if (vForm.getFilter().getSelOrgIds().length==1){
            		long orgId = vForm.getFilter().getSelOrgIds()[0];
                	row = sheet.createRow(rowNum++);
                	cell = row.createCell(0);
                    headerText = new HSSFRichTextString(orgInfoTrn);
                    cell.setCellValue(headerText);
                    cell.setCellStyle(subHeaderCS);
                    cellNum = 0;
            		AmpContact contact=DbUtil.getPrimaryContactForOrganization(orgId);
        			if(contact!=null){
        				HSSFRichTextString headerText2 = null;
                    	row = sheet.createRow(rowNum++);
                    	cell = row.createCell(0);
                        headerText2 = new HSSFRichTextString(contactInfoTrn);
                        cell.setCellValue(headerText2);
                        cell.setCellStyle(subHeaderCS);
                        cell = row.createCell(1);
                        cell.setCellStyle(subHeaderCS);

                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        headerText = new HSSFRichTextString(titleTrn);
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        cell = row.createCell(1);
                        headerText = new HSSFRichTextString(contact.getTitle()!=null?contact.getTitle().getValue():"");
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        headerText = new HSSFRichTextString(nameTrn);
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        cell = row.createCell(1);
                        headerText = new HSSFRichTextString(contact.getName()+" "+contact.getLastname());
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        
                        if(contact.getProperties()!=null){
            				for (AmpContactProperty property : contact.getProperties()) {
            					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    headerText = new HSSFRichTextString(emailsTrn);
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
                                    cell = row.createCell(1);
                                    headerText = new HSSFRichTextString(property.getValue());
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    headerText = new HSSFRichTextString(phonesTrn);
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
                                    cell = row.createCell(1);
                                    headerText = new HSSFRichTextString(property.getValueAsFormatedPhoneNum());
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    headerText = new HSSFRichTextString(faxesTrn);
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
                                    cell = row.createCell(1);
                                    headerText = new HSSFRichTextString(property.getValue());
                                    cell.setCellValue(headerText);
                                    cell.setCellStyle(cellStyleLeft);
            					}
            				}
            			}
        			}
        			AmpOrganisation organization=DbUtil.getOrganisation(orgId);
        			if(organization!=null){
        				HSSFRichTextString headerText2 = null;
                    	row = sheet.createRow(rowNum++);
                    	cell = row.createCell(0);
                        headerText2 = new HSSFRichTextString(addNotesTrn);
                        cell.setCellValue(headerText2);
                        cell.setCellStyle(subHeaderCS);
                        cell = row.createCell(1);
                        cell.setCellStyle(subHeaderCS);
                        
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        headerText = new HSSFRichTextString(backOrgTrn);
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        cell = row.createCell(1);
                        headerText = new HSSFRichTextString(organization.getOrgBackground());
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        headerText = new HSSFRichTextString(descriptionTrn);
                        cell.setCellValue(headerText);
                        cell.setCellStyle(cellStyleLeft);
                        cell = row.createCell(1);
                        headerText = new HSSFRichTextString(organization.getOrgDescription());
                        cell.setCellValue(headerText);
                        cell.setCellStyle(lastCellStyleLeft);
        			}
        			rowNum++;
            	}
            }
            
	        
	        //Filters
	        HSSFRichTextString headerText = null;
        	row = sheet.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(filtersTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            cellNum = 0;
          //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersAmountsInTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
          //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersCurrencyTypeTrn + ": " + vForm.getFilter().getCurrencyCode());
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersStartYearTrn + ": " + vForm.getFilter().getStartYear());
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersEndYearTrn + ": " + vForm.getFilter().getEndYear());
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            String itemList = "";
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersOrgGroupTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            itemList = "";
            Long[] orgIds = vForm.getFilter().getSelOrgIds();
            if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
				for (int i = 0; i < orgIds.length; i++) {
					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersOrganizationsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            itemList = "";
            Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
				for (int i = 0; i < sectorIds.length; i++) {
					itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersSectorsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(cellStyleLeft);
            itemList = "";
            Long[] locationIds = vForm.getFilter().getSelLocationIds();
            if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
				for (int i = 0; i < locationIds.length; i++) {
					itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(filtersLocationsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(lastCellStyleLeft);
            //cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_LEFT);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            
            rowNum++;
	        //Summary table.
	        if (summaryOpt.equals("1")) {
	        	headerText = null;
	        	row = sheet.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(summaryTrn + " (" + currName + ")");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(totalCommsTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(totalDisbsTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(numberPrjTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberDonTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberRegTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberSecTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            //}
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(numberPrjTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getTotalCommitments().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(lastCellStyle);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getTotalDisbursements().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(lastCellStyle);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfProjects().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(lastCellStyle);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfOrganizations().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfRegions().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfSectors().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            //}
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getAverageProjectSize().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(lastCellStyle);
	            
	        }
	        
	        rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        if (vForm.getFilter().getShowProjectsRanking()==null || vForm.getFilter().getShowProjectsRanking()){
           	 	headerText = null;
	        	row = sheet.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(topPrjTrn + " (" + currName + ")");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(projectTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(fundTypeTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	
	            //row = sheet.createRow(rowNum++);
		        //cellNum = 0;
		        Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
		        if(topProjects!=null){
		            List list = new LinkedList(topProjects.entrySet());
				    for (Iterator it = list.iterator(); it.hasNext();) {
				    	row = sheet.createRow(rowNum++);
				        cellNum = 0;
				        Map.Entry entry = (Map.Entry)it.next();
				        HSSFCellStyle st = null;
				        HSSFCellStyle stLf = null;
				    	if (it.hasNext()){
				    		st = cellStyle;
				    		stLf = cellStyleLeft;
				    	} else {
			            	st = lastCellStyle;
			            	stLf = lastCellStyleLeft;
				    	}
				        cell = row.createCell(cellNum++);
			            headerText = new HSSFRichTextString(entry.getKey().toString());
			            cell.setCellValue(headerText);
			            cell.setCellStyle(stLf);
			            cell = row.createCell(cellNum++);
			            headerText = new HSSFRichTextString(entry.getValue().toString());
			            cell.setCellValue(headerText);
			            cell.setCellStyle(st);
				    }
		        }	
	        }
	        
	        List<AmpDashboardGraph> graphs = DbUtil.getDashboardGraphByDashboard(vForm.getDashboard().getId());
            for (Iterator iterator = graphs.iterator(); iterator.hasNext();) {
				AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
				AmpGraph ampGraph = ampDashboardGraph.getGraph();
				if (ampGraph.getContainerId().equals("Fundings"))
					getFundingTable(fundingOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("AidPredictability"))
					getAidPredictabilityTable(aidPredicOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("AidType"))
					getAidTypeTable(aidTypeOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("AidModality"))
					getAidModalityTable(financingInstOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("SectorProfile"))
					getSectorProfileTable(sectorOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("RegionProfile"))
					getRegionProfileTable(regionOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("OrganizationProfile"))
					getOrganizationProfileTable(organizationOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("ODAGrowth"))
					getODAGrowthTable(ODAGrowthOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("NPOProfile"))
					getNPOProfileTable(NPOOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("ProgramProfile"))
					getProgramProfileTable(programOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("AidPredictabilityQuarter"))
					getAidPredictabilityQuarterTable(aidPredicQuarterOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("BudgetBreakdown"))
					getBudgetBreakdownTable(budgetBreakdownOpt, wb, vForm, request);
				if (ampGraph.getContainerId().equals("BeneficiaryAgencyProfile"))
					getBeneficiaryAgencyProfileTable(beneficiaryAgencyOpt, wb, vForm, request);
				
			}
		    
	        for(short i=0;i<10;i++){
	             sheet.setColumnWidth(i , COLUMN_WIDTH);
	        }
	        if (sheet1!=null){
	        	 for(short i=0;i<10;i++){
		             sheet1.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (sheet2!=null){
	        	 for(short i=0;i<10;i++){
		             sheet2.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (sheet3!=null){
	        	 for(short i=0;i<10;i++){
		             sheet3.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet4!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet4.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet5!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet5.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet6!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet6.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (sheet7!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet7.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet8!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet8.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet9!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet9.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet10!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet10.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet11!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet11.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet12!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet12.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (sheet13!=null){ 
	        	 for(short i=0;i<10;i++){
		             sheet13.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        wb.write(response.getOutputStream());
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;

    }
    
    private void getFundingTable(String fundingOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Funding Table.
        if (!fundingOpt.equals("0")){
	    	sheet2 = wb.createSheet(fundingTrn);
	    	rowNum=1;
	    }
	    if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        row = sheet2.createRow(rowNum++);
	        cell = row.createCell(cellNum++);
        	String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
            
        	headerText = new HSSFRichTextString(fundingTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet2.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(yearTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            
            String[] singleRow = fundingRows[1].split(">");
            for (int i = 1; i < singleRow.length; i=i+2) {
            	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(singleRow[i]);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
			}
	        for (int i = 1; i < fundingRows.length; i++) {
	        	cellNum = 0;
		        row = sheet2.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == fundingRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = fundingRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet2.createRow(rowNum++);
	        cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(fundingTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba0 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba0);
            int pictureIndex0 = wb.addPicture(ba0.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch0 = sheet2.createDrawingPatriarch();
            HSSFPicture pic0 =  patriarch0.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex0);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic0.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidPredictabilityTable(String aidPredicOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Aid Predictability Table.
		if (!aidPredicOpt.equals("0")){
	    	sheet3 = wb.createSheet(aidPredTrn);
	    	rowNum=1;
	    }
	    if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet3.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
            
        	headerText = new HSSFRichTextString(aidPredTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet3.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(yearTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(plannedTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(actualTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            
            for (int i = 1; i < aidPredRows.length; i++) {
	        	cellNum = 0;
		        row = sheet3.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == aidPredRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	String[] singleRow = aidPredRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet3.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(aidPredTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba1 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba1);
            int pictureIndex1 = wb.addPicture(ba1.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch1 = sheet3.createDrawingPatriarch();
            HSSFPicture pic1 =  patriarch1.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum+1), rowNum+25), pictureIndex1);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic1.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidTypeTable(String aidTypeOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Aid Type Table.
		if (!aidTypeOpt.equals("0")){
	    	sheet4 = wb.createSheet(aidTypeTrn);
	    	rowNum=1;
	    }
	    if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet4.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
            
        	headerText = new HSSFRichTextString(aidTypeTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet4.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(yearTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = aidTypeRows[1].split(">");
            for (int i = 1; i < singleRow.length; i=i+2) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 1; i < aidTypeRows.length; i++) {
	        	cellNum = 0;
		        row = sheet4.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == aidTypeRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = aidTypeRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet4.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(aidTypeTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba2);
            int pictureIndex2 = wb.addPicture(ba2.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch2 = sheet4.createDrawingPatriarch();
            HSSFPicture pic2 =  patriarch2.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex2);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic2.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidModalityTable(String financingInstOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	 //Financing Instrument Table.
        if (!financingInstOpt.equals("0")){
	    	sheet5 = wb.createSheet(finInstTrn);
	    	rowNum=1;
	    }
	    if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet5.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
            
        	headerText = new HSSFRichTextString(finInstTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet5.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(yearTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = finInstRows[1].split(">");
            for (int i = 1; i < singleRow.length; i=i+2) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 1; i < finInstRows.length; i++) {
	        	cellNum = 0;
		        row = sheet5.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == finInstRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = finInstRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet5.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(finInstTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba3 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba3);
            int pictureIndex3 = wb.addPicture(ba3.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch3 = sheet5.createDrawingPatriarch();
            HSSFPicture pic3 =  patriarch3.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex3);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic3.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getSectorProfileTable(String sectorOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Sector Profile Table.
        if (!sectorOpt.equals("0")){
	    	sheet6 = wb.createSheet(sectorProfTrn);
	    	rowNum=1;
	    }
	    if (sectorOpt.equals("1") || sectorOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet6.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
            
        	headerText = new HSSFRichTextString(sectorProfTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet6.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(sectorTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = sectorProfRows[1].split(">");
            for (int i = 1; i < singleRow.length; i++) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 2; i < sectorProfRows.length; i++) {
	        	cellNum = 0;
		        row = sheet6.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == sectorProfRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = sectorProfRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j++) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    if (sectorOpt.equals("2") || sectorOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet6.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(sectorProfTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba4 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba4);
            int pictureIndex4 = wb.addPicture(ba4.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch4 = sheet6.createDrawingPatriarch();
            HSSFPicture pic4 =  patriarch4.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex4);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic4.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getRegionProfileTable(String regionOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Region Profile Table.
        if (!regionOpt.equals("0")){
	    	sheet7 = wb.createSheet(regionProfTrn);
	    	rowNum=1;
	    }
	    if (regionOpt.equals("1") || regionOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet7.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
            
        	headerText = new HSSFRichTextString(regionProfTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet7.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(regionTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = regionProfRows[1].split(">");
            for (int i = 1; i < singleRow.length; i++) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 2; i < regionProfRows.length; i++) {
	        	cellNum = 0;
		        row = sheet7.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == regionProfRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = regionProfRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j++) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (regionOpt.equals("2") || regionOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet7.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(regionProfTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba5 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba5);
            int pictureIndex5 = wb.addPicture(ba5.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch5 = sheet7.createDrawingPatriarch();
			HSSFPicture pic5 =  patriarch5.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex5);
			HSSFClientAnchor anchor = (HSSFClientAnchor) pic5.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getOrganizationProfileTable(String organizationOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Organization Profile Table.
	    if (!organizationOpt.equals("0")){
	    	sheet8 = wb.createSheet(organizationProfTrn);
	    	rowNum=1;
	    }
	    if (organizationOpt.equals("1") || organizationOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet8.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] organizationProfRows = vForm.getExportData().getOrganizationTableData().split("<");
            
        	headerText = new HSSFRichTextString(organizationProfTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet8.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(organizationTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = organizationProfRows[1].split(">");
            for (int i = 1; i < singleRow.length; i++) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 2; i < organizationProfRows.length; i++) {
	        	cellNum = 0;
		        row = sheet8.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == organizationProfRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = organizationProfRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j++) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (organizationOpt.equals("2") || organizationOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet8.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(organizationProfTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getOrganizationGraph(), "png", ba6);
            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch6 = sheet8.createDrawingPatriarch();
            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex6);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getODAGrowthTable(String ODAGrowthOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//ODA Growth Table.
	    if (!ODAGrowthOpt.equals("0")){
	    	sheet1 = wb.createSheet(ODAGrowthTrn);
	    	rowNum=1;
	    }
	    if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
	    	cellNum = 0;
	    	headerText = null;
	    	row = sheet1.createRow(rowNum++);
	    	cell = row.createCell(cellNum++);
	    	String[] ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");

	    	headerText = new HSSFRichTextString(ODAGrowthTrn + " (" + currName + ")");
	    	cell.setCellValue(headerText);
	    	cell.setCellStyle(subHeaderCS);
	            
	    	cellNum = 0;
            row = sheet1.createRow(rowNum++);
            String[] singleRow = ODAGrowthRows[1].split(">");
            for (int i = 0; i < singleRow.length; i++) {
            	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(singleRow[i]);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 2; i < ODAGrowthRows.length; i++) {
            	singleRow = ODAGrowthRows[i].split(">");
            	cellNum = 0;
		        row = sheet1.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == ODAGrowthRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	for (int j = 0; j < singleRow.length; j++) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
        }
	    if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet1.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(ODAGrowthTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba0 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getODAGrowthGraph(), "png", ba0);
            int pictureIndex0 = wb.addPicture(ba0.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
            HSSFPicture pic =  patriarch.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex0);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
        }
    }
    
    private void getNPOProfileTable(String NPOOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//NPO Profile Table.
		    if (!NPOOpt.equals("0")){
		    	sheet9 = wb.createSheet(NPOProfTrn);
		    	rowNum=1;
		    }
		    if (NPOOpt.equals("1") || NPOOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        cellNum = 0;
		        
		        headerText = null;
	        	row = sheet9.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	        	String[] NPOProfRows = vForm.getExportData().getNPOTableData().split("<");
	            
	        	headerText = new HSSFRichTextString(NPOProfTrn + " (" + currName + ")");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet9.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(NPOTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            String[] singleRow = NPOProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = row.createCell(cellNum++);
	 	            headerText = new HSSFRichTextString(singleRow[i]);
	 	            cell.setCellValue(headerText);
	 	            cell.setCellStyle(subHeaderCS);
				}
	            for (int i = 2; i < NPOProfRows.length; i++) {
		        	cellNum = 0;
			        row = sheet9.createRow(rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == NPOProfRows.length-1)
			    		st = lastCellStyle;
		            else
		            	st = cellStyle;
	            	singleRow = NPOProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		cell = row.createCell(cellNum++);
	 		            headerText = new HSSFRichTextString(singleRow[j]);
	 		            cell.setCellValue(headerText);
	 		            cell.setCellStyle(st);
	    			}
				}
		    }
		    
		    if (NPOOpt.equals("2") || NPOOpt.equals("3")){
		    	rowNum++;
		    	rowNum++;
		        cellNum = 0;
		        row = sheet9.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(NPOProfTrn + " Chart");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getNPOGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = sheet9.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
    }
    
    private void getProgramProfileTable(String programOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Program Profile Table.
		    if (!programOpt.equals("0")){
		    	sheet10 = wb.createSheet(programProfTrn);
		    	rowNum=1;
		    }
		    if (programOpt.equals("1") || programOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        cellNum = 0;
		        
		        headerText = null;
	        	row = sheet10.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	        	String[] programProfRows = vForm.getExportData().getProgramTableData().split("<");
	            
	        	headerText = new HSSFRichTextString(programProfTrn + " (" + currName + ")");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet10.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(programTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            String[] singleRow = programProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = row.createCell(cellNum++);
	 	            headerText = new HSSFRichTextString(singleRow[i]);
	 	            cell.setCellValue(headerText);
	 	            cell.setCellStyle(subHeaderCS);
				}
	            for (int i = 2; i < programProfRows.length; i++) {
		        	cellNum = 0;
			        row = sheet10.createRow(rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == programProfRows.length-1)
			    		st = lastCellStyle;
		            else
		            	st = cellStyle;
	            	singleRow = programProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		cell = row.createCell(cellNum++);
	 		            headerText = new HSSFRichTextString(singleRow[j]);
	 		            cell.setCellValue(headerText);
	 		            cell.setCellStyle(st);
	    			}
				}
		    }
		    
		    if (programOpt.equals("2") || programOpt.equals("3")){
		    	rowNum++;
		    	rowNum++;
		        cellNum = 0;
		        row = sheet10.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(programProfTrn + " Chart");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getProgramGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = sheet10.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
    }
    
    private void getAidPredictabilityQuarterTable(String aidPredicQuarterOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Aid Predictability Quarter Table.
		if (!aidPredicQuarterOpt.equals("0")){
	    	sheet11 = wb.createSheet(aidPredQuarterTrn);
	    	rowNum=1;
	    }
	    if (aidPredicQuarterOpt.equals("1") || aidPredicQuarterOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet11.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] aidPredQuarterRows = vForm.getExportData().getAidPredicQuarterTableData().split("<");
            
        	headerText = new HSSFRichTextString(aidPredQuarterTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet11.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(quarterTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(plannedTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(actualTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            
            for (int i = 1; i < aidPredQuarterRows.length; i++) {
	        	cellNum = 0;
		        row = sheet11.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == aidPredQuarterRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	String[] singleRow = aidPredQuarterRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (aidPredicQuarterOpt.equals("2") || aidPredicQuarterOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet11.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(aidPredQuarterTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba1 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityQuarterGraph(), "png", ba1);
            int pictureIndex1 = wb.addPicture(ba1.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch1 = sheet11.createDrawingPatriarch();
            HSSFPicture pic1 =  patriarch1.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum+1), rowNum+25), pictureIndex1);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic1.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getBudgetBreakdownTable(String budgetBreakdownOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Budget Breakdown Table.
		if (!budgetBreakdownOpt.equals("0")){
	    	sheet12 = wb.createSheet(budgetBreakdownTrn);
	    	rowNum=1;
	    }
	    if (budgetBreakdownOpt.equals("1") || budgetBreakdownOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet12.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] budgetBreakdownRows = vForm.getExportData().getBudgetTableData().split("<");
            
        	headerText = new HSSFRichTextString(budgetBreakdownTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet12.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(yearTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = budgetBreakdownRows[1].split(">");
            for (int i = 1; i < singleRow.length; i=i+2) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 1; i < budgetBreakdownRows.length; i++) {
	        	cellNum = 0;
		        row = sheet12.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == budgetBreakdownRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = budgetBreakdownRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j=j+2) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (budgetBreakdownOpt.equals("2") || budgetBreakdownOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet12.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(budgetBreakdownTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBudgetGraph(), "png", ba2);
            int pictureIndex2 = wb.addPicture(ba2.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch2 = sheet12.createDrawingPatriarch();
            HSSFPicture pic2 =  patriarch2.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex2);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic2.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getBeneficiaryAgencyProfileTable(String beneficiaryAgencyOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request) throws Exception{
    	//Beneficiary Agency Profile Table.
	    if (!beneficiaryAgencyOpt.equals("0")){
	    	sheet13 = wb.createSheet(beneficiaryAgencyProfTrn);
	    	rowNum=1;
	    }
	    if (beneficiaryAgencyOpt.equals("1") || beneficiaryAgencyOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        headerText = null;
        	row = sheet13.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
        	String[] organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
            
        	headerText = new HSSFRichTextString(beneficiaryAgencyProfTrn + " (" + currName + ")");
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            cellNum = 0;
            row = sheet13.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(organizationTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(subHeaderCS);
            String[] singleRow = organizationProfRows[1].split(">");
            for (int i = 1; i < singleRow.length; i++) {
            	cell = row.createCell(cellNum++);
 	            headerText = new HSSFRichTextString(singleRow[i]);
 	            cell.setCellValue(headerText);
 	            cell.setCellStyle(subHeaderCS);
			}
            for (int i = 2; i < organizationProfRows.length; i++) {
	        	cellNum = 0;
		        row = sheet13.createRow(rowNum++);
		        HSSFCellStyle st = null;
		    	if (i == organizationProfRows.length-1)
		    		st = lastCellStyle;
	            else
	            	st = cellStyle;
            	singleRow = organizationProfRows[i].split(">");
            	for (int j = 0; j < singleRow.length; j++) {
            		cell = row.createCell(cellNum++);
 		            headerText = new HSSFRichTextString(singleRow[j]);
 		            cell.setCellValue(headerText);
 		            cell.setCellStyle(st);
    			}
			}
	    }
	    
	    if (beneficiaryAgencyOpt.equals("2") || beneficiaryAgencyOpt.equals("3")){
	    	rowNum++;
	    	rowNum++;
	        cellNum = 0;
	        row = sheet13.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(beneficiaryAgencyProfTrn + " Chart");
            cell.setCellValue(headerText);
            cell.setCellStyle(headerCS);
            
	        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBeneficiaryAgencyGraph(), "png", ba6);
            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch6 = sheet13.createDrawingPatriarch();
            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)5, rowNum+25), pictureIndex6);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
}
	    