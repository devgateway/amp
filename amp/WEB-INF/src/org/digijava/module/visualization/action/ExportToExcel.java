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
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
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
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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

	public static class ExportToExcelData {
		
		public static ExportToExcelData factory(VisualizationForm vForm) {
		  
			ExportToExcelData data = new ExportToExcelData(vForm);

			return data;
			
		}
		
		public String orgInfoTrn;
		public String orgGrpInfoTrn;
		public String contactInfoTrn;
		public String addNotesTrn;
		public String nameTrn;
		public String titleTrn;
		public String emailsTrn;
		public String phonesTrn;
		public String faxesTrn;
		public String backOrgTrn;
		public String backOrgGrpTrn;
		public String descriptionTrn;
		public String keyAreasTrn;
		public String pageTrn;
		public String filtersTrn;
		public String filtersAllTrn;
		public String filtersCurrencyTypeTrn;
		public String filtersStartYearTrn;
		public String filtersEndYearTrn;
		public String filtersOrgGroupTrn;
		public String filtersOrganizationsTrn;
		public String filtersSectorsTrn;
		public String filtersSubSectorsTrn;
		public String filtersRegionsTrn;
		public String filtersZonesTrn;
		public String filtersStatusTrn;
		public String filtersLocationsTrn;
		public String fundingTrn;
		public String ODAGrowthTrn;
		public String topPrjTrn;
		public String topOrganizationTrn;
		public String topRegionTrn;
		public String projectTrn;
		public String sectorTrn;
		public String organizationTrn;
		public String regionTrn;
		public String NPOTrn;
		public String programTrn;
		public String aidPredTrn;
		public String aidPredQuarterTrn;
		public String aidTypeTrn;
		public String budgetBreakdownTrn;
		public String finInstTrn;
		public String sectorProfTrn;
		public String regionProfTrn;
		public String NPOProfTrn;
		public String programProfTrn;
		public String secProgramProfTrn;
		public String organizationProfTrn;
		public String beneficiaryAgencyProfTrn;
		public String responsibleOrganizationProfTrn;
		public String plannedTrn;
		public String actualTrn;
		public String yearTrn;
		public String dashboardTrn;
		public String summaryTrn;
		public String totalCommsTrn;
		public String totalDisbsTrn;
		public String numberPrjTrn;
		public String numberSecTrn;
		public String numberDonTrn;
		public String numberRegTrn;
		public String avgPrjZSizeTrn;
		public String currName;
		public String fundTypeTrn;
		public String dashboardTypeTrn;
		public String topSectorTrn;
		public String quarterTrn;
		public String filtersAmountsInTrn;
		public String filtersMagnitudeTrn;
		public HSSFSheet sheet1;
		public HSSFSheet sheet2;
		public HSSFSheet sheet3;
		public HSSFSheet sheet4;
		public HSSFSheet sheet5;
		public HSSFSheet sheet6;
		public HSSFSheet sheet7;
		public HSSFSheet sheet8;
		public HSSFSheet sheet9;
		public HSSFSheet sheet10;
		public HSSFSheet sheet11;
		public HSSFSheet sheet12;
		public HSSFSheet sheet13;
		public HSSFCellStyle headerCS;
		public HSSFCellStyle subHeaderCS;
		public HSSFCellStyle lastCellStyle;
		public HSSFCellStyle lastCellStyleLeft;
		public HSSFCellStyle cellStyle;
		public HSSFCellStyle cellStyleLeft;
		public HSSFRichTextString headerText;
		public HSSFCellStyle titleCS;
		public int rowNum=0;
		public int cellNum=0;
		public HSSFRow row;
		public HSSFCell cell;

		public ExportToExcelData(VisualizationForm vForm) {

			
			orgInfoTrn = TranslatorWorker.translateText("Organization Information");
	    	orgGrpInfoTrn = TranslatorWorker.translateText("Organization Group Information");
	    	contactInfoTrn = TranslatorWorker.translateText("Contact Information");
	    	addNotesTrn = TranslatorWorker.translateText("Additional Notes");
	    	nameTrn = TranslatorWorker.translateText("Name");
	    	titleTrn = TranslatorWorker.translateText("Title");
	    	emailsTrn = TranslatorWorker.translateText("Emails");
	    	phonesTrn = TranslatorWorker.translateText("Phones");
	    	faxesTrn = TranslatorWorker.translateText("Faxes");
	    	backOrgTrn = TranslatorWorker.translateText("Background of organization");
	    	backOrgGrpTrn = TranslatorWorker.translateText("Background of organization group");
	        descriptionTrn = TranslatorWorker.translateText("Description");
	        keyAreasTrn = TranslatorWorker.translateText("Key Areas of Focus");
			filtersTrn = TranslatorWorker.translateText("Filters");
			filtersAllTrn = TranslatorWorker.translateText("All");
			filtersAmountsInTrn = ""; 
			filtersMagnitudeTrn = "";
			if(vForm.getFilter().shouldShowAmountsInThousands()){
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in thousands");
				filtersMagnitudeTrn = TranslatorWorker.translateText("Thousands");
			}
			else{
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in millions");
				filtersMagnitudeTrn = TranslatorWorker.translateText("Millions");
			}
			filtersCurrencyTypeTrn = TranslatorWorker.translateText("Currency Type");
			filtersStartYearTrn = TranslatorWorker.translateText("Start Year");
			filtersEndYearTrn = TranslatorWorker.translateText("End Year");
			filtersOrgGroupTrn = TranslatorWorker.translateText("Organization Groups");
			filtersOrganizationsTrn = TranslatorWorker.translateText("Organizations");
			filtersSectorsTrn = TranslatorWorker.translateText("Sectors");
			filtersLocationsTrn = TranslatorWorker.translateText("Locations");
			filtersSubSectorsTrn = TranslatorWorker.translateText("Sub-Sectors");
			filtersRegionsTrn = TranslatorWorker.translateText("Regions");
			filtersZonesTrn = TranslatorWorker.translateText("Zones");
			filtersStatusTrn = TranslatorWorker.translateText("Status");
			ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth");
			fundingTrn = TranslatorWorker.translateText("ODA Historical Trend");
	        topPrjTrn = TranslatorWorker.translateText("Top Projects");
	        topSectorTrn = TranslatorWorker.translateText("Top Sectors");
	        topOrganizationTrn = TranslatorWorker.translateText("Top Organizations");
	        topRegionTrn = TranslatorWorker.translateText("Top Regions");
	        projectTrn = TranslatorWorker.translateText("Project");
	        sectorTrn = TranslatorWorker.translateText("Sector");
	        organizationTrn = TranslatorWorker.translateText("Organization");
	        NPOTrn = TranslatorWorker.translateText("NPO");
	        budgetBreakdownTrn = TranslatorWorker.translateText("Budget Breakdown");
	        programTrn = TranslatorWorker.translateText("Program");
	        regionTrn = TranslatorWorker.translateText("Region");
	        aidPredTrn = TranslatorWorker.translateText("Aid Predictability");
	        aidPredQuarterTrn = TranslatorWorker.translateText("Aid Predictability Quarterly");
	        aidTypeTrn = TranslatorWorker.translateText("Aid Type");
	        finInstTrn = TranslatorWorker.translateText("Aid Modality");
	        sectorProfTrn = TranslatorWorker.translateText("Sector Profile");
	        regionProfTrn = TranslatorWorker.translateText("Region Profile");
	        organizationProfTrn = TranslatorWorker.translateText("Organization Profile");
	        beneficiaryAgencyProfTrn = TranslatorWorker.translateText("Beneficiary Agency Profile");
	        responsibleOrganizationProfTrn = TranslatorWorker.translateText("Responsible Organization");
	        NPOProfTrn = TranslatorWorker.translateText("NPO Profile");
	        programProfTrn = TranslatorWorker.translateText("Program Profile");
	        secProgramProfTrn = TranslatorWorker.translateText("Secondary Program Profile");
	        plannedTrn = TranslatorWorker.translateText("Planned");
	        actualTrn = TranslatorWorker.translateText("Actual");
	        yearTrn = TranslatorWorker.translateText("Year");
	        quarterTrn = TranslatorWorker.translateText("Quarter");
	        dashboardTrn = TranslatorWorker.translateText("Dashboard");
	        summaryTrn = TranslatorWorker.translateText("Summary");
	        totalCommsTrn = TranslatorWorker.translateText("Total Commitments");
	        totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements");
	        numberPrjTrn = TranslatorWorker.translateText("Number of Projects");
	        numberSecTrn = TranslatorWorker.translateText("Number of Sectors");
	        numberDonTrn = TranslatorWorker.translateText("Number of Organizations");
	        numberRegTrn = TranslatorWorker.translateText("Number of Regions");
	        avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size");
	        AmpCurrency currency = (AmpCurrency)org.digijava.module.aim.util.DbUtil.getObject(AmpCurrency.class,vForm.getFilter().getCurrencyId());
            currName = currency.getCurrencyName();
          
	        fundTypeTrn = "";
	        if (vForm.getFilter().getAdjustmentType().equals("Actual"))
            	fundTypeTrn = TranslatorWorker.translateText("Actual");
            if (vForm.getFilter().getAdjustmentType().equals("Planned"))
            	fundTypeTrn = TranslatorWorker.translateText("Planned");
            
            switch (vForm.getFilter().getTransactionType()) {
				case Constants.COMMITMENT:
					fundTypeTrn = fundTypeTrn+" "+TranslatorWorker.translateText("Commitments");
					break;
				case Constants.DISBURSEMENT:
					fundTypeTrn = fundTypeTrn+" "+TranslatorWorker.translateText("Disbursements");
					break;
				case Constants.EXPENDITURE:
					fundTypeTrn = fundTypeTrn+" "+TranslatorWorker.translateText("Expenditures");
					break;
				default:
					fundTypeTrn = fundTypeTrn+" "+TranslatorWorker.translateText("Values");
				break;
			}
	        dashboardTypeTrn = "";
	        switch (vForm.getFilter().getDashboardType()) {
	            case org.digijava.module.visualization.util.Constants.DashboardType.DONOR:
	            	dashboardTypeTrn = TranslatorWorker.translateText("Organization");
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.SECTOR:
					dashboardTypeTrn = TranslatorWorker.translateText("Sector");
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.REGION:
					dashboardTypeTrn = TranslatorWorker.translateText("Region");
					break;
			
			}
			
		}
	}

	private static Logger logger = Logger.getLogger(ExportToExcel.class);
    private final static char BULLETCHAR = '\u2022';
    private final static char NEWLINECHAR = '\n';
    private final static short COLUMN_WIDTH=5120;
    private final static short TITLE_WIDTH=8960;

   	   
	
    
   
   
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=dashboardExport.xls");
        VisualizationForm vForm = (VisualizationForm) form;
        
        ExportToExcelData data = ExportToExcelData.factory(vForm);
    	
        String ODAGrowthOpt = request.getParameter("ODAGrowthOpt");
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidPredicQuarterOpt = request.getParameter("aidPredicQuarterOpt");
        String budgetBreakdownOpt = request.getParameter("budgetBreakdownOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String financingInstOpt = request.getParameter("financingInstOpt");
        String organizationOpt = request.getParameter("organizationOpt");
        String beneficiaryAgencyOpt = request.getParameter("beneficiaryAgencyOpt");
        String responsibleOrganizationOpt = request.getParameter("responsibleOrganizationOpt");
        String sectorOpt = request.getParameter("sectorOpt");
        String regionOpt = request.getParameter("regionOpt");
        String NPOOpt = request.getParameter("NPOOpt");
        String programOpt = request.getParameter("programOpt");
        String secProgramOpt = request.getParameter("secondaryProgramOpt");
        String summaryOpt = request.getParameter("summaryOpt");
    
    	try {
    		
    	
	        HSSFWorkbook wb = new HSSFWorkbook();
	       
	        // normal cells
	        data.cellStyle = wb.createCellStyle();
	        data.cellStyle.setWrapText(true);
	        HSSFFont fontCell = wb.createFont();
	        fontCell.setFontName(HSSFFont.FONT_ARIAL);
	        data.cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        HSSFDataFormat df = wb.createDataFormat();
	        data.cellStyle.setDataFormat(df.getFormat("General"));
	        data.cellStyle.setFont(fontCell);
	        data.cellStyle.setWrapText(true);
	        data.cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        data.cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	        
	        data.cellStyleLeft = wb.createCellStyle();
	        data.cellStyleLeft.setWrapText(true);
	        data.cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.cellStyleLeft.setDataFormat(df.getFormat("General"));
	        data.cellStyleLeft.setFont(fontCell);
	        data.cellStyleLeft.setWrapText(true);
	        data.cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        data.cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	        
	        data.lastCellStyle = wb.createCellStyle();
	        data.lastCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyle.setFont(fontCell);
	        data.lastCellStyle.setWrapText(true);
	        data.lastCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        data.lastCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	        data.lastCellStyleLeft = wb.createCellStyle();
	        data.lastCellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.lastCellStyleLeft.setFont(fontCell);
	        data.lastCellStyleLeft.setWrapText(true);
	        data.lastCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        data.lastCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

	     // title cells
	        data.titleCS = wb.createCellStyle();
	        data.titleCS.setWrapText(true);
	        HSSFFont fontTitle = wb.createFont();
	        fontTitle.setFontName(HSSFFont.FONT_ARIAL);
	        fontTitle.setFontHeightInPoints((short) 14);
	        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        data.titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        data.titleCS.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
	        data.titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        data.titleCS.setFont(fontTitle);

	     // header cells
	        data.headerCS = wb.createCellStyle();
	        data.headerCS.setWrapText(true);
	        HSSFFont fontHeader = wb.createFont();
	        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
	        fontHeader.setFontHeightInPoints((short) 12);
	        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        data.headerCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        data.headerCS.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	        data.headerCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.headerCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.headerCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        data.headerCS.setFont(fontHeader);

	     // subHeader cells
	        data.subHeaderCS = wb.createCellStyle();
	        data.subHeaderCS.setWrapText(true);
	        HSSFFont fontSubHeader = wb.createFont();
	        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
	        fontSubHeader.setFontHeightInPoints((short) 10);
	        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        data.subHeaderCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        data.subHeaderCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        data.subHeaderCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        data.subHeaderCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        data.subHeaderCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        data.subHeaderCS.setFont(fontHeader);
	        
	     // subHeader cells
	        HSSFCellStyle subHeaderNumericCS = wb.createCellStyle();
	        data.subHeaderCS.setWrapText(true);
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
	        
	        String name =  data.dashboardTypeTrn + " " + data.dashboardTrn;
	        //String sheetName = "Dashboard";
	
	        HSSFSheet sheet = wb.createSheet(data.summaryTrn + " & " + data.projectTrn);
	        
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
            	header = new HSSFRichTextString(data.dashboardTypeTrn.toUpperCase() + " " + data.dashboardTrn.toUpperCase());
            	
            }
	        cell.setCellValue(header);
	        cell.setCellStyle(data.titleCS);
	        
	      //Org. Information
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	if (vForm.getFilter().getSelOrgIds()!=null && vForm.getFilter().getSelOrgIds().length==1 && vForm.getFilter().getSelOrgIds()[0] != -1){
            		long orgId = vForm.getFilter().getSelOrgIds()[0];
                	row = sheet.createRow(rowNum++);
                	cell = row.createCell(0);
                    data.headerText = new HSSFRichTextString(data.orgInfoTrn);
                    cell.setCellValue(data.headerText);
                    cell.setCellStyle(data.subHeaderCS);
                    cellNum = 0;
            		AmpContact contact=DbUtil.getPrimaryContactForOrganization(orgId);
        			if(contact!=null){
        				HSSFRichTextString headerText2 = null;
                    	row = sheet.createRow(rowNum++);
                    	cell = row.createCell(0);
                        headerText2 = new HSSFRichTextString(data.contactInfoTrn);
                        cell.setCellValue(headerText2);
                        cell.setCellStyle(data.subHeaderCS);
                        cell = row.createCell(1);
                        cell.setCellStyle(data.subHeaderCS);

                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.titleTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        cell = row.createCell(1);
                        data.headerText = new HSSFRichTextString(contact.getTitle()!=null?contact.getTitle().getValue():"");
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.nameTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        cell = row.createCell(1);
                        data.headerText = new HSSFRichTextString(contact.getName()+" "+contact.getLastname());
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        
                        if(contact.getProperties()!=null){
            				for (AmpContactProperty property : contact.getProperties()) {
            					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    data.headerText = new HSSFRichTextString(data.emailsTrn);
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
                                    cell = row.createCell(1);
                                    data.headerText = new HSSFRichTextString(property.getValue());
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    data.headerText = new HSSFRichTextString(data.phonesTrn);
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
                                    cell = row.createCell(1);
                                    data.headerText = new HSSFRichTextString(property.getValueAsFormatedPhoneNum());
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
            						row = sheet.createRow(rowNum++);
                                    cell = row.createCell(0);
                                    data.headerText = new HSSFRichTextString(data.faxesTrn);
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
                                    cell = row.createCell(1);
                                    data.headerText = new HSSFRichTextString(property.getValue());
                                    cell.setCellValue(data.headerText);
                                    cell.setCellStyle(data.cellStyleLeft);
            					}
            				}
            			}
        			}
        			AmpOrganisation organization=DbUtil.getOrganisation(orgId);
        			if(organization!=null){
        				HSSFRichTextString headerText2 = null;
                    	row = sheet.createRow(rowNum++);
                    	cell = row.createCell(0);
                        headerText2 = new HSSFRichTextString(data.addNotesTrn);
                        cell.setCellValue(headerText2);
                        cell.setCellStyle(data.subHeaderCS);
                        cell = row.createCell(1);
                        cell.setCellStyle(data.subHeaderCS);
                        
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.backOrgTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        cell = row.createCell(1);
                        data.headerText = new HSSFRichTextString(organization.getOrgBackground());
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.descriptionTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        cell = row.createCell(1);
                        data.headerText = new HSSFRichTextString(organization.getOrgDescription());
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.keyAreasTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.cellStyleLeft);
                        cell = row.createCell(1);
                        data.headerText = new HSSFRichTextString(organization.getOrgKeyAreas());
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.lastCellStyleLeft);
        			}
        			rowNum++;
            	}
            	else {
                	if (vForm.getFilter().getSelOrgGroupIds()!=null && vForm.getFilter().getSelOrgGroupIds().length==1 && vForm.getFilter().getSelOrgGroupIds()[0] != -1){
                		long orgGrpId = vForm.getFilter().getSelOrgGroupIds()[0];
                    	row = sheet.createRow(rowNum++);
                    	cell = row.createCell(0);
                        data.headerText = new HSSFRichTextString(data.orgGrpInfoTrn);
                        cell.setCellValue(data.headerText);
                        cell.setCellStyle(data.subHeaderCS);
                        cellNum = 0;
            			AmpOrgGroup orgGroup=DbUtil.getOrgGroup(orgGrpId);
            			if(orgGroup!=null){
            				HSSFRichTextString headerText2 = null;
                        	row = sheet.createRow(rowNum++);
                        	cell = row.createCell(0);
                            headerText2 = new HSSFRichTextString(data.addNotesTrn);
                            cell.setCellValue(headerText2);
                            cell.setCellStyle(data.subHeaderCS);
                            cell = row.createCell(1);
                            cell.setCellStyle(data.subHeaderCS);
                            
                            row = sheet.createRow(rowNum++);
                            cell = row.createCell(0);
                            data.headerText = new HSSFRichTextString(data.backOrgGrpTrn);
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.cellStyleLeft);
                            cell = row.createCell(1);
                            data.headerText = new HSSFRichTextString(orgGroup.getOrgGrpBackground());
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.cellStyleLeft);
                            row = sheet.createRow(rowNum++);
                            cell = row.createCell(0);
                            data.headerText = new HSSFRichTextString(data.descriptionTrn);
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.cellStyleLeft);
                            cell = row.createCell(1);
                            data.headerText = new HSSFRichTextString(orgGroup.getOrgGrpDescription());
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.cellStyleLeft);
                            row = sheet.createRow(rowNum++);
                            cell = row.createCell(0);
                            data.headerText = new HSSFRichTextString(data.keyAreasTrn);
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.cellStyleLeft);
                            cell = row.createCell(1);
                            data.headerText = new HSSFRichTextString(orgGroup.getOrgGrpKeyAreas());
                            cell.setCellValue(data.headerText);
                            cell.setCellStyle(data.lastCellStyleLeft);
            			}
            			rowNum++;
                	}
            	}
            }
            
	        
	        //Filters
	        HSSFRichTextString headerText = null;
        	row = sheet.createRow(rowNum++);
        	cell = row.createCell(cellNum++);
            headerText = new HSSFRichTextString(data.filtersTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.subHeaderCS);
            cellNum = 0;
          //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersAmountsInTrn);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
          //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersCurrencyTypeTrn + ": " + data.currName);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersStartYearTrn + ": " + vForm.getFilter().getStartYear());
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersEndYearTrn + ": " + vForm.getFilter().getEndYear());
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            String itemList = "";
            Long[] statusIds = vForm.getFilter().getSelStatusIds();
            if (statusIds != null && statusIds.length != 0 && statusIds[0]!=-1) {
				for (int i = 0; i < statusIds.length; i++) {
					itemList = itemList + CategoryManagerUtil.getAmpCategoryValueFromDb(statusIds[i]).getValue() + "; ";
				}
			} else {
				itemList = data.filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersStatusTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            itemList = "";
            
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = data.filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersOrgGroupTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            itemList = "";
            Long[] orgIds = vForm.getFilter().getSelOrgIds();
            if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
				for (int i = 0; i < orgIds.length; i++) {
					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
				}
			} else {
				itemList = data.filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersOrganizationsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            itemList = "";
            Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
				for (int i = 0; i < sectorIds.length; i++) {
					itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
				}
			} else {
				itemList = data.filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersSectorsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.cellStyleLeft);
            itemList = "";
            Long[] locationIds = vForm.getFilter().getSelLocationIds();
            if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
				for (int i = 0; i < locationIds.length; i++) {
					itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
				}
			} else {
				itemList = data.filtersAllTrn;
			}
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum);
            headerText = new HSSFRichTextString(data.filtersLocationsTrn + ": " + itemList);
            cell.setCellValue(headerText);
            cell.setCellStyle(data.lastCellStyleLeft);
            //cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_LEFT);
            //sheet.addMergedRegion(new Region(rowNum-1,(short)0,rowNum-1,(short)5));
            
            rowNum++;
            
            String amountDescription = " (" + data.fundTypeTrn + " - " + data.filtersMagnitudeTrn + " " + data.currName + ")";  
	        //Summary table.
	        if (summaryOpt.equals("1")) {
	        	headerText = null;
	        	row = sheet.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.summaryTrn + amountDescription);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	            
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.totalCommsTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.totalDisbsTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.numberPrjTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(data.numberDonTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(data.subHeaderCS);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(data.numberRegTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(data.subHeaderCS);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(data.numberSecTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(data.subHeaderCS);
	            //}
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.avgPrjZSizeTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            
	            if(vForm.getSummaryInformation().getTotalCommitments() != null) {
	            	cell = row.createCell(cellNum++);
		            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getTotalCommitments().toString());
		            cell.setCellValue(headerText);
		            cell.setCellStyle(data.lastCellStyle);
	            }
	            if(vForm.getSummaryInformation().getTotalDisbursements()!=null){
	            	cell = row.createCell(cellNum++);
		            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getTotalDisbursements().toString());
		            cell.setCellValue(headerText);
		            cell.setCellStyle(data.lastCellStyle);
	            }
	            if(vForm.getSummaryInformation().getNumberOfProjects()!=null){
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfProjects().toString());
		            cell.setCellValue(headerText);
		            cell.setCellStyle(data.lastCellStyle);
	            }
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {	            	
	            	if(vForm.getSummaryInformation().getNumberOfOrganizations()!=null){
	            		cell = row.createCell(cellNum++);
		                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfOrganizations().toString());
		                cell.setCellValue(headerText);
		                cell.setCellStyle(data.lastCellStyle);
	            	}
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	if(vForm.getSummaryInformation().getNumberOfRegions()!=null){
		            	cell = row.createCell(cellNum++);
		                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfRegions().toString());
		                cell.setCellValue(headerText);
		                cell.setCellStyle(data.lastCellStyle);
	            	}
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	if(vForm.getSummaryInformation().getNumberOfSectors()!=null){
		            	cell = row.createCell(cellNum++);
		                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfSectors().toString());
		                cell.setCellValue(headerText);
		                cell.setCellStyle(data.lastCellStyle);		                
	            	}
	            //}	            
	            if(vForm.getSummaryInformation().getAverageProjectSize()!=null){
	            	cell = row.createCell(cellNum++);
		            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getAverageProjectSize().toString());
		            cell.setCellValue(headerText);
		            cell.setCellStyle(data.lastCellStyle);
	            }
	            
	        }
	        
	        rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        if (vForm.getFilter().getShowProjectsRanking()==null || vForm.getFilter().getShowProjectsRanking()){
           	 	headerText = null;
	        	row = sheet.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.topPrjTrn + amountDescription);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.projectTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(data.fundTypeTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(data.subHeaderCS);
	
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
				    		st = data.cellStyle;
				    		stLf = data.cellStyleLeft;
				    	} else {
			            	st = data.lastCellStyle;
			            	stLf = data.lastCellStyleLeft;
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
					getFundingTable(fundingOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("AidPredictability"))
					getAidPredictabilityTable(aidPredicOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("AidType"))
					getAidTypeTable(aidTypeOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("AidModality"))
					getAidModalityTable(financingInstOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("SectorProfile"))
					getSectorProfileTable(sectorOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("RegionProfile"))
					getRegionProfileTable(regionOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("OrganizationProfile"))
					getOrganizationProfileTable(organizationOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("ODAGrowth"))
					getODAGrowthTable(ODAGrowthOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("NPOProfile"))
					getNPOProfileTable(NPOOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("ProgramProfile"))
					getProgramProfileTable(programOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("SecondaryProgramProfile"))
					getSecondaryProgramProfileTable(secProgramOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("AidPredictabilityQuarter"))
					getAidPredictabilityQuarterTable(aidPredicQuarterOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("BudgetBreakdown"))
					getBudgetBreakdownTable(budgetBreakdownOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("BeneficiaryAgencyProfile"))
					getBeneficiaryAgencyProfileTable(beneficiaryAgencyOpt, wb, vForm, request, amountDescription, data);
				if (ampGraph.getContainerId().equals("ResponsibleOrganization"))
					getResponsibleOrganizationTable(responsibleOrganizationOpt, wb, vForm, request, amountDescription, data);
				
			}
		    
	        for(short i=0;i<10;i++){
	             sheet.setColumnWidth(i , COLUMN_WIDTH);
	        }
	        if (data.sheet1!=null){
	        	 for(short i=0;i<10;i++){
		             data.sheet1.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (data.sheet2!=null){
	        	 for(short i=0;i<10;i++){
		             data.sheet2.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (data.sheet3!=null){
	        	 for(short i=0;i<10;i++){
		             data.sheet3.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet4!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet4.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet5!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet5.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet6!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet6.setColumnWidth(i , COLUMN_WIDTH);
		        } 
	        }
	        if (data.sheet7!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet7.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet8!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet8.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet9!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet9.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet10!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet10.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet11!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet11.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet12!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet12.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        if (data.sheet13!=null){ 
	        	 for(short i=0;i<10;i++){
		             data.sheet13.setColumnWidth(i , COLUMN_WIDTH);
		        }
	        }
	        wb.write(response.getOutputStream());
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;

    }
    
    private void getFundingTable(String fundingOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc, ExportToExcelData data) throws Exception{
    	//Funding Table.
    	
    	
        if (!fundingOpt.equals("0")){
	    	data.sheet2 = wb.createSheet(data.fundingTrn);
	    	data.rowNum=1;
	    }
	    if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.row = data.sheet2.createRow(data.rowNum++);
	        data.cell = data.row.createCell(data.cellNum++);
	        String[] fundingRows = null;
	        if(vForm.getExportData().getFundingTableData()!=null){
	        	fundingRows = vForm.getExportData().getFundingTableData().split("<");
	        }
            
        	data.headerText = new HSSFRichTextString(data.fundingTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet2.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.yearTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (fundingRows != null && fundingRows.length>1){
	            String[] singleRow = fundingRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	data.cell = data.row.createCell(data.cellNum++);
		            data.headerText = new HSSFRichTextString(singleRow[i]);
		            data.cell.setCellValue(data.headerText);
		            data.cell.setCellStyle(data.subHeaderCS);
				}
		        for (int i = 1; i < fundingRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet2.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == fundingRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = fundingRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet2.createRow(data.rowNum++);
	        data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.fundingTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba0 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba0);
            int pictureIndex0 = wb.addPicture(ba0.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch0 = data.sheet2.createDrawingPatriarch();
            HSSFPicture pic0 =  patriarch0.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex0);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic0.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidPredictabilityTable(String aidPredicOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Aid Predictability Table.
    
    	
		if (!aidPredicOpt.equals("0")){
	    	data.sheet3 = wb.createSheet(data.aidPredTrn);
	    	data.rowNum=1;
	    }
	    if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet3.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] aidPredRows = null;
        	if(vForm.getExportData().getAidPredicTableData()!=null){
        		aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.aidPredTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet3.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.yearTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.plannedTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.actualTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (aidPredRows!=null && aidPredRows.length>1){
	            for (int i = 1; i < aidPredRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet3.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == aidPredRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	String[] singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet3.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.aidPredTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba1 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba1);
            int pictureIndex1 = wb.addPicture(ba1.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch1 = data.sheet3.createDrawingPatriarch();
            HSSFPicture pic1 =  patriarch1.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)(data.cellNum+1), data.rowNum+25), pictureIndex1);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic1.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidTypeTable(String aidTypeOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    
    	//Aid Type Table.
		if (!aidTypeOpt.equals("0")){
	    	data.sheet4 = wb.createSheet(data.aidTypeTrn);
	    	data.rowNum=1;
	    }
	    if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet4.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] aidTypeRows = null;
        	if(vForm.getExportData().getAidTypeTableData()!= null) {
        		aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.aidTypeTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet4.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.yearTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (aidTypeRows!=null && aidTypeRows.length>1){
	            String[] singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 1; i < aidTypeRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet4.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == aidTypeRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = aidTypeRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet4.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.aidTypeTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba2);
            int pictureIndex2 = wb.addPicture(ba2.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch2 = data.sheet4.createDrawingPatriarch();
            HSSFPicture pic2 =  patriarch2.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex2);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic2.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getAidModalityTable(String financingInstOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    
    	 //Financing Instrument Table.
        if (!financingInstOpt.equals("0")){
	    	data.sheet5 = wb.createSheet(data.finInstTrn);
	    	data.rowNum=1;
	    }
	    if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet5.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] finInstRows = null;
        	if(vForm.getExportData().getFinancingInstTableData()!=null){
        		finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.finInstTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet5.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.yearTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (finInstRows!=null && finInstRows.length>1){
	            String[] singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 1; i < finInstRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet5.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == finInstRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = finInstRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet5.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.finInstTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba3 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba3);
            int pictureIndex3 = wb.addPicture(ba3.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch3 = data.sheet5.createDrawingPatriarch();
            HSSFPicture pic3 =  patriarch3.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex3);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic3.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getSectorProfileTable(String sectorOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	
    	//Sector Profile Table.
        if (!sectorOpt.equals("0")){
	    	data.sheet6 = wb.createSheet(data.sectorProfTrn);
	    	data.rowNum=1;
	    }
	    if (sectorOpt.equals("1") || sectorOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet6.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] sectorProfRows = null;
        	if(vForm.getExportData().getSectorTableData() != null) {
        		sectorProfRows= vForm.getExportData().getSectorTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.sectorProfTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet6.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.sectorTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (sectorProfRows != null && sectorProfRows.length>1){
	            String[] singleRow = sectorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 2; i < sectorProfRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet6.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == sectorProfRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = sectorProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    if (sectorOpt.equals("2") || sectorOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet6.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.sectorProfTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba4 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba4);
            int pictureIndex4 = wb.addPicture(ba4.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch4 = data.sheet6.createDrawingPatriarch();
            HSSFPicture pic4 =  patriarch4.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex4);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic4.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getRegionProfileTable(String regionOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Region Profile Table.
        if (!regionOpt.equals("0")){
	    	data.sheet7 = wb.createSheet(data.regionProfTrn);
	    	data.rowNum=1;
	    }
	    if (regionOpt.equals("1") || regionOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet7.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] regionProfRows = null;
        	if(vForm.getExportData().getRegionTableData() != null) {
        		regionProfRows = vForm.getExportData().getRegionTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.regionProfTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet7.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.regionTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (regionProfRows != null && regionProfRows.length>1){
	            String[] singleRow = regionProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 2; i < regionProfRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet7.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == regionProfRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = regionProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (regionOpt.equals("2") || regionOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet7.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.regionProfTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba5 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba5);
            int pictureIndex5 = wb.addPicture(ba5.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch5 = data.sheet7.createDrawingPatriarch();
			HSSFPicture pic5 =  patriarch5.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex5);
			HSSFClientAnchor anchor = (HSSFClientAnchor) pic5.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getOrganizationProfileTable(String organizationOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Organization Profile Table.
	    if (!organizationOpt.equals("0")){
	    	data.sheet8 = wb.createSheet(data.organizationProfTrn);
	    	data.rowNum=1;
	    }
	    if (organizationOpt.equals("1") || organizationOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet8.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] organizationProfRows = null;
        	if(vForm.getExportData().getOrganizationTableData() != null) {
        		organizationProfRows = vForm.getExportData().getOrganizationTableData().split("<");
        	}
            if(organizationProfRows != null) {
	        	data.headerText = new HSSFRichTextString(data.organizationProfTrn + amountDesc);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            data.cellNum = 0;
	            data.row = data.sheet8.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.organizationTrn);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            if (organizationProfRows.length>1){
		            String[] singleRow = organizationProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	data.cell = data.row.createCell(data.cellNum++);
		 	            data.headerText = new HSSFRichTextString(singleRow[i]);
		 	            data.cell.setCellValue(data.headerText);
		 	            data.cell.setCellStyle(data.subHeaderCS);
					}
		            for (int i = 2; i < organizationProfRows.length; i++) {
			        	data.cellNum = 0;
				        data.row = data.sheet8.createRow(data.rowNum++);
				        HSSFCellStyle st = null;
				    	if (i == organizationProfRows.length-1)
				    		st = data.lastCellStyle;
			            else
			            	st = data.cellStyle;
		            	singleRow = organizationProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		            		data.cell = data.row.createCell(data.cellNum++);
		 		            data.headerText = new HSSFRichTextString(singleRow[j]);
		 		            data.cell.setCellValue(data.headerText);
		 		            data.cell.setCellStyle(st);
		    			}
					}
	            }
            }
	    }
	    
	    if (organizationOpt.equals("2") || organizationOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet8.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.organizationProfTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getOrganizationGraph(), "png", ba6);
            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch6 = data.sheet8.createDrawingPatriarch();
            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getODAGrowthTable(String ODAGrowthOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//ODA Growth Table.
	    if (!ODAGrowthOpt.equals("0")){
	    	data.sheet1 = wb.createSheet(data.ODAGrowthTrn);
	    	data.rowNum=1;
	    }
	    if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
	    	data.cellNum = 0;
	    	data.headerText = null;
	    	data.row = data.sheet1.createRow(data.rowNum++);
	    	data.cell = data.row.createCell(data.cellNum++);
	    	String[] ODAGrowthRows = null;
	    	if(vForm.getExportData().getODAGrowthTableData()!=null){
	    		ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");
	    	}

	    	data.headerText = new HSSFRichTextString(data.ODAGrowthTrn + amountDesc);
	    	data.cell.setCellValue(data.headerText);
	    	data.cell.setCellStyle(data.subHeaderCS);
	            
	    	data.cellNum = 0;
            data.row = data.sheet1.createRow(data.rowNum++);
            if (ODAGrowthRows!=null && ODAGrowthRows.length>1){
	            String[] singleRow = ODAGrowthRows[1].split(">");
	            for (int i = 0; i < singleRow.length; i++) {
	            	data.cell = data.row.createCell(data.cellNum++);
		            data.headerText = new HSSFRichTextString(singleRow[i]);
		            data.cell.setCellValue(data.headerText);
		            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 2; i < ODAGrowthRows.length; i++) {
	            	singleRow = ODAGrowthRows[i].split(">");
	            	data.cellNum = 0;
			        data.row = data.sheet1.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == ODAGrowthRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	for (int j = 0; j < singleRow.length; j++) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
        }
	    if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet1.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.ODAGrowthTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba0 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getODAGrowthGraph(), "png", ba0);
            int pictureIndex0 = wb.addPicture(ba0.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch = data.sheet1.createDrawingPatriarch();
            HSSFPicture pic =  patriarch.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex0);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
        }
    }
    
    private void getNPOProfileTable(String NPOOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//NPO Profile Table.
		    if (!NPOOpt.equals("0")){
		    	data.sheet9 = wb.createSheet(data.NPOProfTrn);
		    	data.rowNum=1;
		    }
		    if (NPOOpt.equals("1") || NPOOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        data.cellNum = 0;
		        
		        data.headerText = null;
	        	data.row = data.sheet9.createRow(data.rowNum++);
	        	data.cell = data.row.createCell(data.cellNum++);
	        	String[] NPOProfRows = null;
	        	if(vForm.getExportData().getNPOTableData()!=null){
	        		NPOProfRows = vForm.getExportData().getNPOTableData().split("<");
	        	}
	            
	        	data.headerText = new HSSFRichTextString(data.NPOProfTrn + amountDesc);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            data.cellNum = 0;
	            data.row = data.sheet9.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.NPOTrn);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            if (NPOProfRows!=null && NPOProfRows.length>1){
		            String[] singleRow = NPOProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	data.cell = data.row.createCell(data.cellNum++);
		 	            data.headerText = new HSSFRichTextString(singleRow[i]);
		 	            data.cell.setCellValue(data.headerText);
		 	            data.cell.setCellStyle(data.subHeaderCS);
					}
		            for (int i = 2; i < NPOProfRows.length; i++) {
			        	data.cellNum = 0;
				        data.row = data.sheet9.createRow(data.rowNum++);
				        HSSFCellStyle st = null;
				    	if (i == NPOProfRows.length-1)
				    		st = data.lastCellStyle;
			            else
			            	st = data.cellStyle;
		            	singleRow = NPOProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		            		data.cell = data.row.createCell(data.cellNum++);
		 		            data.headerText = new HSSFRichTextString(singleRow[j]);
		 		            data.cell.setCellValue(data.headerText);
		 		            data.cell.setCellStyle(st);
		    			}
					}
	            }
		    }
		    
		    if (NPOOpt.equals("2") || NPOOpt.equals("3")){
		    	data.rowNum++;
		    	data.rowNum++;
		        data.cellNum = 0;
		        data.row = data.sheet9.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.NPOProfTrn + " Chart");
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getNPOGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = data.sheet9.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(data.rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
    }
    
    private void getProgramProfileTable(String programOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Program Profile Table.
		    if (!programOpt.equals("0")){
		    	data.sheet10 = wb.createSheet(data.programProfTrn);
		    	data.rowNum=1;
		    }
		    if (programOpt.equals("1") || programOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        data.cellNum = 0;
		        
		        data.headerText = null;
	        	data.row = data.sheet10.createRow(data.rowNum++);
	        	data.cell = data.row.createCell(data.cellNum++);
	        	String[] programProfRows = null;
	        	if(vForm.getExportData().getProgramTableData()!=null){
	        		programProfRows = vForm.getExportData().getProgramTableData().split("<");
	        	}
	            
	        	data.headerText = new HSSFRichTextString(data.programProfTrn + amountDesc);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            data.cellNum = 0;
	            data.row = data.sheet10.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.programTrn);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            if (programProfRows!=null && programProfRows.length>1){
		            String[] singleRow = programProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	data.cell = data.row.createCell(data.cellNum++);
		 	            data.headerText = new HSSFRichTextString(singleRow[i]);
		 	            data.cell.setCellValue(data.headerText);
		 	            data.cell.setCellStyle(data.subHeaderCS);
					}
		            for (int i = 2; i < programProfRows.length; i++) {
			        	data.cellNum = 0;
				        data.row = data.sheet10.createRow(data.rowNum++);
				        HSSFCellStyle st = null;
				    	if (i == programProfRows.length-1)
				    		st = data.lastCellStyle;
			            else
			            	st = data.cellStyle;
		            	singleRow = programProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		            		data.cell = data.row.createCell(data.cellNum++);
		 		            data.headerText = new HSSFRichTextString(singleRow[j]);
		 		            data.cell.setCellValue(data.headerText);
		 		            data.cell.setCellStyle(st);
		    			}
					}
	            }
		    }
		    
		    if (programOpt.equals("2") || programOpt.equals("3")){
		    	data.rowNum++;
		    	data.rowNum++;
		        data.cellNum = 0;
		        data.row = data.sheet10.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.programProfTrn + " Chart");
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getProgramGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = data.sheet10.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(data.rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
    }
    
    private void getSecondaryProgramProfileTable(String programOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Program Profile Table.
		    if (!programOpt.equals("0")){
		    	data.sheet10 = wb.createSheet(data.secProgramProfTrn);
		    	data.rowNum=1;
		    }
		    if (programOpt.equals("1") || programOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        data.cellNum = 0;
		        
		        data.headerText = null;
	        	data.row = data.sheet10.createRow(data.rowNum++);
	        	data.cell = data.row.createCell(data.cellNum++);
	        	String[] programProfRows = null;
	        	if(vForm.getExportData().getSecondaryProgramTableData()!=null){
	        		programProfRows = vForm.getExportData().getSecondaryProgramTableData().split("<");
	        	}
	            
	        	data.headerText = new HSSFRichTextString(data.secProgramProfTrn + amountDesc);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            data.cellNum = 0;
	            data.row = data.sheet10.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.programTrn);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            if (programProfRows!=null && programProfRows.length>1){
		            String[] singleRow = programProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	data.cell = data.row.createCell(data.cellNum++);
		 	            data.headerText = new HSSFRichTextString(singleRow[i]);
		 	            data.cell.setCellValue(data.headerText);
		 	            data.cell.setCellStyle(data.subHeaderCS);
					}
		            for (int i = 2; i < programProfRows.length; i++) {
			        	data.cellNum = 0;
				        data.row = data.sheet10.createRow(data.rowNum++);
				        HSSFCellStyle st = null;
				    	if (i == programProfRows.length-1)
				    		st = data.lastCellStyle;
			            else
			            	st = data.cellStyle;
		            	singleRow = programProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		            		data.cell = data.row.createCell(data.cellNum++);
		 		            data.headerText = new HSSFRichTextString(singleRow[j]);
		 		            data.cell.setCellValue(data.headerText);
		 		            data.cell.setCellStyle(st);
		    			}
					}
	            }
		    }
		    
		    if (programOpt.equals("2") || programOpt.equals("3")){
		    	data.rowNum++;
		    	data.rowNum++;
		        data.cellNum = 0;
		        data.row = data.sheet10.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.programProfTrn + " Chart");
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getSecondaryProgramGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = data.sheet10.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(data.rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
    }
    
    private void getAidPredictabilityQuarterTable(String aidPredicQuarterOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Aid Predictability Quarter Table.
		if (!aidPredicQuarterOpt.equals("0")){
	    	data.sheet11 = wb.createSheet(data.aidPredQuarterTrn);
	    	data.rowNum=1;
	    }
	    if (aidPredicQuarterOpt.equals("1") || aidPredicQuarterOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet11.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] aidPredQuarterRows = null;
        	if(vForm.getExportData().getAidPredicQuarterTableData()!=null){
        		aidPredQuarterRows = vForm.getExportData().getAidPredicQuarterTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.aidPredQuarterTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet11.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.quarterTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.plannedTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.actualTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (aidPredQuarterRows != null && aidPredQuarterRows.length>1){
	            for (int i = 1; i < aidPredQuarterRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet11.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == aidPredQuarterRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	String[] singleRow = aidPredQuarterRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (aidPredicQuarterOpt.equals("2") || aidPredicQuarterOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet11.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.aidPredQuarterTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba1 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityQuarterGraph(), "png", ba1);
            int pictureIndex1 = wb.addPicture(ba1.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch1 = data.sheet11.createDrawingPatriarch();
            HSSFPicture pic1 =  patriarch1.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)(data.cellNum+1), data.rowNum+25), pictureIndex1);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic1.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getBudgetBreakdownTable(String budgetBreakdownOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Budget Breakdown Table.
		if (!budgetBreakdownOpt.equals("0")){
	    	data.sheet12 = wb.createSheet(data.budgetBreakdownTrn);
	    	data.rowNum=1;
	    }
	    if (budgetBreakdownOpt.equals("1") || budgetBreakdownOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet12.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] budgetBreakdownRows = null;
        	if(vForm.getExportData().getBudgetTableData()!=null){
        		budgetBreakdownRows = vForm.getExportData().getBudgetTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.budgetBreakdownTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet12.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.yearTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (budgetBreakdownRows!=null && budgetBreakdownRows.length>1){
	            String[] singleRow = budgetBreakdownRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 1; i < budgetBreakdownRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet12.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == budgetBreakdownRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = budgetBreakdownRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (budgetBreakdownOpt.equals("2") || budgetBreakdownOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet12.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.budgetBreakdownTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBudgetGraph(), "png", ba2);
            int pictureIndex2 = wb.addPicture(ba2.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch2 = data.sheet12.createDrawingPatriarch();
            HSSFPicture pic2 =  patriarch2.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex2);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic2.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getBeneficiaryAgencyProfileTable(String beneficiaryAgencyOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Beneficiary Agency Profile Table.
	    if (!beneficiaryAgencyOpt.equals("0")){
	    	data.sheet13 = wb.createSheet(data.beneficiaryAgencyProfTrn);
	    	data.rowNum=1;
	    }
	    if (beneficiaryAgencyOpt.equals("1") || beneficiaryAgencyOpt.equals("3")){
	    	//rowNum = rowNum + 2;
	        data.cellNum = 0;
	        
	        data.headerText = null;
        	data.row = data.sheet13.createRow(data.rowNum++);
        	data.cell = data.row.createCell(data.cellNum++);
        	String[] organizationProfRows = null;
        	if(vForm.getExportData().getBeneficiaryAgencyTableData()!=null){
        		organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
        	}
            
        	data.headerText = new HSSFRichTextString(data.beneficiaryAgencyProfTrn + amountDesc);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
            
            data.cellNum = 0;
            data.row = data.sheet13.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.organizationTrn);
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.subHeaderCS);
            if (organizationProfRows!=null && organizationProfRows.length>1){
	            String[] singleRow = organizationProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	data.cell = data.row.createCell(data.cellNum++);
	 	            data.headerText = new HSSFRichTextString(singleRow[i]);
	 	            data.cell.setCellValue(data.headerText);
	 	            data.cell.setCellStyle(data.subHeaderCS);
				}
	            for (int i = 2; i < organizationProfRows.length; i++) {
		        	data.cellNum = 0;
			        data.row = data.sheet13.createRow(data.rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == organizationProfRows.length-1)
			    		st = data.lastCellStyle;
		            else
		            	st = data.cellStyle;
	            	singleRow = organizationProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		data.cell = data.row.createCell(data.cellNum++);
	 		            data.headerText = new HSSFRichTextString(singleRow[j]);
	 		            data.cell.setCellValue(data.headerText);
	 		            data.cell.setCellStyle(st);
	    			}
				}
            }
	    }
	    
	    if (beneficiaryAgencyOpt.equals("2") || beneficiaryAgencyOpt.equals("3")){
	    	data.rowNum++;
	    	data.rowNum++;
	        data.cellNum = 0;
	        data.row = data.sheet13.createRow(data.rowNum++);
            data.cell = data.row.createCell(data.cellNum++);
            data.headerText = new HSSFRichTextString(data.beneficiaryAgencyProfTrn + " Chart");
            data.cell.setCellValue(data.headerText);
            data.cell.setCellStyle(data.headerCS);
            
	        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBeneficiaryAgencyGraph(), "png", ba6);
            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch patriarch6 = data.sheet13.createDrawingPatriarch();
            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
            anchor.setCol2((short)5);
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setRow2(data.rowNum+25);
            anchor.setDy1(0);
            anchor.setDy2(0);
	    }
    }
    
    private void getResponsibleOrganizationTable(String responsibleOrganizationOpt, HSSFWorkbook wb, VisualizationForm vForm, HttpServletRequest request, String amountDesc,ExportToExcelData data) throws Exception{
    	//Beneficiary Agency Profile Table.
    	if(responsibleOrganizationOpt != null) {
		    if (!responsibleOrganizationOpt.equals("0")){
		    	data.sheet13 = wb.createSheet(data.responsibleOrganizationProfTrn);
		    	data.rowNum=1;
		    }
		    if (responsibleOrganizationOpt.equals("1") || responsibleOrganizationOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        data.cellNum = 0;
		        
		        data.headerText = null;
	        	data.row = data.sheet13.createRow(data.rowNum++);
	        	data.cell = data.row.createCell(data.cellNum++);
	        	String[] organizationProfRows = null;
	        	if(vForm.getExportData().getResponsibleOrganizationTableData()!=null){
	        		organizationProfRows = vForm.getExportData().getResponsibleOrganizationTableData().split("<");
	        	}
	            
	        	data.headerText = new HSSFRichTextString(data.responsibleOrganizationProfTrn + amountDesc);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            data.cellNum = 0;
	            data.row = data.sheet13.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.organizationTrn);
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.subHeaderCS);
	            if (organizationProfRows!=null && organizationProfRows.length>1){
		            String[] singleRow = organizationProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	data.cell = data.row.createCell(data.cellNum++);
		 	            data.headerText = new HSSFRichTextString(singleRow[i]);
		 	            data.cell.setCellValue(data.headerText);
		 	            data.cell.setCellStyle(data.subHeaderCS);
					}
		            for (int i = 2; i < organizationProfRows.length; i++) {
			        	data.cellNum = 0;
				        data.row = data.sheet13.createRow(data.rowNum++);
				        HSSFCellStyle st = null;
				    	if (i == organizationProfRows.length-1)
				    		st = data.lastCellStyle;
			            else
			            	st = data.cellStyle;
		            	singleRow = organizationProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		            		data.cell = data.row.createCell(data.cellNum++);
		 		            data.headerText = new HSSFRichTextString(singleRow[j]);
		 		            data.cell.setCellValue(data.headerText);
		 		            data.cell.setCellStyle(st);
		    			}
					}
	            }
		    }
		    
		    if (responsibleOrganizationOpt.equals("2") || responsibleOrganizationOpt.equals("3")){
		    	data.rowNum++;
		    	data.rowNum++;
		        data.cellNum = 0;
		        data.row = data.sheet13.createRow(data.rowNum++);
	            data.cell = data.row.createCell(data.cellNum++);
	            data.headerText = new HSSFRichTextString(data.responsibleOrganizationProfTrn + " Chart");
	            data.cell.setCellValue(data.headerText);
	            data.cell.setCellStyle(data.headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getResponsibleOrganizationGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = data.sheet13.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, data.rowNum, (short)5, data.rowNum+25), pictureIndex6);
	            HSSFClientAnchor anchor = (HSSFClientAnchor) pic6.getAnchor();
	            anchor.setCol2((short)5);
	            anchor.setDx1(0);
	            anchor.setDx2(0);
	            anchor.setRow2(data.rowNum+25);
	            anchor.setDy1(0);
	            anchor.setDy2(0);
		    }
	    }
    }
}
	    