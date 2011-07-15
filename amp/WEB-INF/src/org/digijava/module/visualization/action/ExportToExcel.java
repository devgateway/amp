package org.digijava.module.visualization.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.ExportOrganizationToExcel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;
import javax.servlet.ServletContext;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class ExportToExcel extends Action {

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
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        VisualizationForm vForm = (VisualizationForm) form;
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String financingInstOpt = request.getParameter("financingInstOpt");
        String donorOpt = request.getParameter("donorOpt");
        String sectorOpt = request.getParameter("sectorOpt");
        String regionOpt = request.getParameter("regionOpt");
        String summaryOpt = request.getParameter("summaryOpt");
    
    	try {
			String notAvailable = TranslatorWorker.translateText("Not Available", langCode, siteId);
			String fundingTrn = TranslatorWorker.translateText("Funding", langCode, siteId);
	        String topPrjTrn = TranslatorWorker.translateText("Top 5 Projects", langCode, siteId);
	        String topSectorTrn = TranslatorWorker.translateText("Top 5 Sectors", langCode, siteId);
	        String topDonorTrn = TranslatorWorker.translateText("Top 5 Donors", langCode, siteId);
	        String topRegionTrn = TranslatorWorker.translateText("Top 5 Regions", langCode, siteId);
	        String projectTrn = TranslatorWorker.translateText("Project", langCode, siteId);
	        String sectorTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
	        String donorTrn = TranslatorWorker.translateText("Donor", langCode, siteId);
	        String regionTrn = TranslatorWorker.translateText("Region", langCode, siteId);
	        String aidPredTrn = TranslatorWorker.translateText("Aid Predictability", langCode, siteId);
	        String aidTypeTrn = TranslatorWorker.translateText("Aid Type", langCode, siteId);
	        String finInstTrn = TranslatorWorker.translateText("Financing Instrument", langCode, siteId);
	        String sectorProfTrn = TranslatorWorker.translateText("Sector Profile", langCode, siteId);
	        String regionProfTrn = TranslatorWorker.translateText("Region Profile", langCode, siteId);
	        String donorProfTrn = TranslatorWorker.translateText("Donor Profile", langCode, siteId);
	        String plannedTrn = TranslatorWorker.translateText("Planned", langCode, siteId);
	        String actualTrn = TranslatorWorker.translateText("Actual", langCode, siteId);
	        String yearTrn = TranslatorWorker.translateText("Year", langCode, siteId);
	        String dashboardTrn = TranslatorWorker.translateText("Dashboard", langCode, siteId);
	        String summaryTrn = TranslatorWorker.translateText("Summary", langCode, siteId);
	        String totalCommsTrn = TranslatorWorker.translateText("Total Commitments", langCode, siteId);
	        String totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements", langCode, siteId);
	        String numberPrjTrn = TranslatorWorker.translateText("Number of Projects", langCode, siteId);
	        String numberSecTrn = TranslatorWorker.translateText("Number of Sectors", langCode, siteId);
	        String numberDonTrn = TranslatorWorker.translateText("Number of Donors", langCode, siteId);
	        String numberRegTrn = TranslatorWorker.translateText("Number of Regions", langCode, siteId);
	        String avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size", langCode, siteId);
	        String currName = vForm.getFilter().getCurrencyCode();
	        String fundTypeTrn = "";
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
	        String dashboardTypeTrn = "";
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
	        HSSFCellStyle cellStyle = wb.createCellStyle();
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
	        
	        HSSFCellStyle lastCellStyle = wb.createCellStyle();
	        lastCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        lastCellStyle.setFont(fontCell);
	        lastCellStyle.setWrapText(true);
	        lastCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);


	     // title cells
	        HSSFCellStyle titleCS = wb.createCellStyle();
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
	        HSSFCellStyle headerCS = wb.createCellStyle();
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
	        HSSFCellStyle subHeaderCS = wb.createCellStyle();
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
	        HSSFRichTextString header = new HSSFRichTextString(name);
	        cell.setCellValue(header);
	        cell.setCellStyle(titleCS);
	        
	        //Summary table.
	        if (summaryOpt.equals("1")) {
	        	HSSFRichTextString headerText = null;
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
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberDonTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberRegTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = row.createCell(cellNum++);
	            	headerText = new HSSFRichTextString(numberSecTrn);
	                cell.setCellValue(headerText);
	                cell.setCellStyle(subHeaderCS);
	            }
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
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfDonors().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfRegions().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = row.createCell(cellNum++);
	                headerText = new HSSFRichTextString(vForm.getSummaryInformation().getNumberOfSectors().toString());
	                cell.setCellValue(headerText);
	                cell.setCellStyle(lastCellStyle);
	            }
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(vForm.getSummaryInformation().getAverageProjectSize().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(lastCellStyle);
	            
	        }
	        
	        rowNum = rowNum + 2;
	        cellNum = 0;
	        
	        HSSFRichTextString headerText = null;
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
            List list = new LinkedList(topProjects.entrySet());
		    for (Iterator it = list.iterator(); it.hasNext();) {
		    	row = sheet.createRow(rowNum++);
		        cellNum = 0;
		        Map.Entry entry = (Map.Entry)it.next();
		    	HSSFCellStyle st = null;
		    	if (it.hasNext())
		    		st = cellStyle;
	            else
	            	st = lastCellStyle;
		        cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(entry.getKey().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(st);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(entry.getValue().toString());
	            cell.setCellValue(headerText);
	            cell.setCellStyle(st);
		    }
		   
		  //Funding Table.
		    HSSFSheet sheet2 = null;
		    if (!fundingOpt.equals("0")){
		    	sheet2 = wb.createSheet(fundingTrn);
		    	rowNum=1;
		    }
		    if (fundingOpt.equals("1") || fundingOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        cellNum = 0;
		        
		        headerText = null;
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
	            
	            singleRow = fundingRows[1].split(">");
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
	            HSSFPicture pic0 =  patriarch0.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex0);
	            rowNum = rowNum+27;
	            pic0.resize();
		    }
            
		  //Aid Predictability Table.
		    HSSFSheet sheet3 = null;
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
	            	singleRow = aidPredRows[i].split(">");
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
	            rowNum = rowNum+27;
	            pic1.resize();
		    }
		    
		  //Aid Type Table.
		    HSSFSheet sheet4 = null;
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
	            singleRow = aidTypeRows[1].split(">");
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
	            HSSFPicture pic2 =  patriarch2.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex2);
	            rowNum = rowNum+27;
	            pic2.resize();
		    }
            
		  //Financing Instrument Table.
		    HSSFSheet sheet5 = null;
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
	            singleRow = finInstRows[1].split(">");
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
	            HSSFPicture pic3 =  patriarch3.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex3);
	            rowNum = rowNum+27;
	            pic3.resize();
		    }
            
		  //Sector Profile Table.
		    HSSFSheet sheet6 = null;
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
	            headerText = new HSSFRichTextString(yearTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            singleRow = sectorProfRows[1].split(">");
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
	            HSSFPicture pic4 =  patriarch4.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex4);
	            rowNum = rowNum+27;
	            pic4.resize();
		    }
		    
		  //Region Profile Table.
		    HSSFSheet sheet7 = null;
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
	            headerText = new HSSFRichTextString(yearTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            singleRow = regionProfRows[1].split(">");
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
				HSSFPicture pic5 =  patriarch5.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex5);
	            rowNum = rowNum+27;
	            pic5.resize();
		    }
            
		  //Donor Profile Table.
		    HSSFSheet sheet8 = null;
		    if (!donorOpt.equals("0")){
		    	sheet8 = wb.createSheet(donorProfTrn);
		    	rowNum=1;
		    }
		    if (donorOpt.equals("1") || donorOpt.equals("3")){
		    	//rowNum = rowNum + 2;
		        cellNum = 0;
		        
		        headerText = null;
	        	row = sheet8.createRow(rowNum++);
	        	cell = row.createCell(cellNum++);
	        	String[] donorProfRows = vForm.getExportData().getDonorTableData().split("<");
	            
	        	headerText = new HSSFRichTextString(donorProfTrn + " (" + currName + ")");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            //sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,0,5));
	            
	            cellNum = 0;
	            row = sheet8.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(yearTrn);
	            cell.setCellValue(headerText);
	            cell.setCellStyle(subHeaderCS);
	            singleRow = donorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = row.createCell(cellNum++);
	 	            headerText = new HSSFRichTextString(singleRow[i]);
	 	            cell.setCellValue(headerText);
	 	            cell.setCellStyle(subHeaderCS);
				}
	            for (int i = 2; i < donorProfRows.length; i++) {
		        	cellNum = 0;
			        row = sheet8.createRow(rowNum++);
			        HSSFCellStyle st = null;
			    	if (i == donorProfRows.length-1)
			    		st = lastCellStyle;
		            else
		            	st = cellStyle;
	            	singleRow = donorProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	            		cell = row.createCell(cellNum++);
	 		            headerText = new HSSFRichTextString(singleRow[j]);
	 		            cell.setCellValue(headerText);
	 		            cell.setCellStyle(st);
	    			}
				}
		    }
		    
		    if (donorOpt.equals("2") || donorOpt.equals("3")){
		    	rowNum++;
		    	rowNum++;
		        cellNum = 0;
		        row = sheet8.createRow(rowNum++);
	            cell = row.createCell(cellNum++);
	            headerText = new HSSFRichTextString(donorProfTrn + " Chart");
	            cell.setCellValue(headerText);
	            cell.setCellStyle(headerCS);
	            
		        ByteArrayOutputStream ba6 = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getDonorGraph(), "png", ba6);
	            int pictureIndex6 = wb.addPicture(ba6.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
	            HSSFPatriarch patriarch6 = sheet8.createDrawingPatriarch();
	            HSSFPicture pic6 =  patriarch6.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum, (short)(cellNum-1), rowNum+25), pictureIndex6);
	            rowNum = rowNum+27;
	            pic6.resize();
		    }
            
		    sheet.autoSizeColumn(0); 
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        sheet.autoSizeColumn(3); 
	        sheet.autoSizeColumn(4);
	        sheet.autoSizeColumn(5);
	        sheet.autoSizeColumn(6); 
	        if (sheet2!=null){
		        sheet2.autoSizeColumn(0); 
		        sheet2.autoSizeColumn(1);
		        sheet2.autoSizeColumn(2);
		        sheet2.autoSizeColumn(3); 
		        sheet2.autoSizeColumn(4);
		        sheet2.autoSizeColumn(5);
		        sheet2.autoSizeColumn(6); 
	        }
	        if (sheet3!=null){
		        sheet3.autoSizeColumn(0); 
		        sheet3.autoSizeColumn(1);
		        sheet3.autoSizeColumn(2);
		        sheet3.autoSizeColumn(3); 
		        sheet3.autoSizeColumn(4);
		        sheet3.autoSizeColumn(5);
		        sheet3.autoSizeColumn(6); 
	        }
	        if (sheet4!=null){ 
		        sheet4.autoSizeColumn(0); 
		        sheet4.autoSizeColumn(1);
		        sheet4.autoSizeColumn(2);
		        sheet4.autoSizeColumn(3); 
		        sheet4.autoSizeColumn(4);
		        sheet4.autoSizeColumn(5);
		        sheet4.autoSizeColumn(6); 
	        }
	        if (sheet5!=null){ 
		        sheet5.autoSizeColumn(0); 
		        sheet5.autoSizeColumn(1);
		        sheet5.autoSizeColumn(2);
		        sheet5.autoSizeColumn(3); 
		        sheet5.autoSizeColumn(4);
		        sheet5.autoSizeColumn(5);
		        sheet5.autoSizeColumn(6); 
	        }
	        if (sheet6!=null){ 
		        sheet6.autoSizeColumn(0); 
		        sheet6.autoSizeColumn(1);
		        sheet6.autoSizeColumn(2);
		        sheet6.autoSizeColumn(3); 
		        sheet6.autoSizeColumn(4);
		        sheet6.autoSizeColumn(5);
		        sheet6.autoSizeColumn(6); 
	        }
	        if (sheet7!=null){ 
		        sheet7.autoSizeColumn(0); 
		        sheet7.autoSizeColumn(1);
		        sheet7.autoSizeColumn(2);
		        sheet7.autoSizeColumn(3); 
		        sheet7.autoSizeColumn(4);
		        sheet7.autoSizeColumn(5);
		        sheet7.autoSizeColumn(6); 
	        }
	        if (sheet8!=null){ 
		        sheet8.autoSizeColumn(0); 
		        sheet8.autoSizeColumn(1);
		        sheet8.autoSizeColumn(2);
		        sheet8.autoSizeColumn(3); 
		        sheet8.autoSizeColumn(4);
		        sheet8.autoSizeColumn(5);
		        sheet8.autoSizeColumn(6); 
	        }
	        wb.write(response.getOutputStream());
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;

    }
}
	    