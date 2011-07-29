package org.digijava.module.visualization.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.util.DbUtil;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

public class ExportToPDF extends Action {

    private static Logger logger = Logger.getLogger(ExportToPDF.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/download");
        response.setHeader("content-disposition", "attachment;filename=dashboard.pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        VisualizationForm vForm = (VisualizationForm) form;
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String financingInstOpt = request.getParameter("financingInstOpt");
        String donorOpt = request.getParameter("donorOpt");
        String sectorOpt = request.getParameter("sectorOpt");
        String regionOpt = request.getParameter("regionOpt");
        String summaryOpt = request.getParameter("summaryOpt");
        String ODAGrowthOpt = request.getParameter("ODAGrowthOpt");
        try {
        	String pageTrn = TranslatorWorker.translateText("Page", langCode, siteId);
        	String filtersTrn = TranslatorWorker.translateText("Filters", langCode, siteId);
        	String fundingTrn = TranslatorWorker.translateText("Funding", langCode, siteId);
            String odaGrowthTrn = TranslatorWorker.translateText("ODA Growth", langCode, siteId);
            String topPrjTrn = TranslatorWorker.translateText("Top 5 Projects", langCode, siteId);
            String ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth chart", langCode, siteId);
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
	            	dashboardTypeTrn = TranslatorWorker.translateText("Donor", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.SECTOR:
					dashboardTypeTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.REGION:
					dashboardTypeTrn = TranslatorWorker.translateText("Region", langCode, siteId);
					break;
			
			}
        
        	PdfWriter pdfWriter =PdfWriter.getInstance(doc, baos);
            HttpSession session = request.getSession();
            String footerText = pageTrn + " - ";
            doc.setPageCount(1);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), true);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.open();
            Paragraph pageTitle = new Paragraph(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn.toUpperCase(), TITLEFONT);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
     
            PdfPCell cell = null;
            List list = null;
            int colspan = 0;
            Image img = null;
            String[] singleRow = null;
            
            //Filters.
            PdfPTable filtersTbl = null;
            filtersTbl = new PdfPTable(1);
            filtersTbl.setWidthPercentage(100);
            PdfPCell filterTitleCell = new PdfPCell(new Paragraph(filtersTrn, HEADERFONT));
            filterTitleCell.setColspan(1);
            filtersTbl.addCell(filterTitleCell);
            
            cell = new PdfPCell(new Paragraph("Currency Type: " + vForm.getFilter().getCurrencyCode()));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph("Fiscal Start Year: " + vForm.getFilter().getYear()));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph("Years in Range: " + vForm.getFilter().getYearsInRange()));
            filtersTbl.addCell(cell);
            String itemList = "";
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = "All";
			}
            cell = new PdfPCell(new Paragraph("Organization Groups: " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] orgIds = vForm.getFilter().getOrgIds();
            if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
				for (int i = 0; i < orgIds.length; i++) {
					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
				}
			} else {
				itemList = "All";
			}
            cell = new PdfPCell(new Paragraph("Organizations: " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
				for (int i = 0; i < sectorIds.length; i++) {
					itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
				}
			} else {
				itemList = "All";
			}
            cell = new PdfPCell(new Paragraph("Sectors: " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] locationIds = vForm.getFilter().getSelLocationIds();
            if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
				for (int i = 0; i < locationIds.length; i++) {
					itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
				}
			} else {
				itemList = "All";
			}
            cell = new PdfPCell(new Paragraph("Locations: " + itemList));
            filtersTbl.addCell(cell);
            
		    doc.add(filtersTbl);
            doc.add(new Paragraph(" "));
            
          //Summary table.
            if (summaryOpt.equals("1")) {
				PdfPTable summaryTbl = null;
	            summaryTbl = new PdfPTable(6);
	            summaryTbl.setWidthPercentage(100);
	            PdfPCell sumamaryTitleCell = new PdfPCell(new Paragraph(summaryTrn + " (" + currName + ")", HEADERFONT));
	            sumamaryTitleCell.setColspan(6);
	            summaryTbl.addCell(sumamaryTitleCell);
	            cell = new PdfPCell(new Paragraph(totalCommsTrn, HEADERFONT));
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(totalDisbsTrn, HEADERFONT));
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(numberPrjTrn, HEADERFONT));
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(numberDonTrn, HEADERFONT));
	                summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new PdfPCell(new Paragraph(numberRegTrn, HEADERFONT));
		            summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new PdfPCell(new Paragraph(numberSecTrn, HEADERFONT));
		            summaryTbl.addCell(cell);
	            }
	            cell = new PdfPCell(new Paragraph(avgPrjZSizeTrn, HEADERFONT));
	            summaryTbl.addCell(cell);
	            
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getTotalCommitments().toString(), HEADERFONT));
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getTotalDisbursements().toString()));
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfDonors().toString()));
	                summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	                summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	                summaryTbl.addCell(cell);
	            }
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getAverageProjectSize().toString()));
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
	        
            Paragraph subTitle = new Paragraph(topPrjTrn + " (" + currName + ")", SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
            
          //Top projects table.
            PdfPTable topPrjTbl = null;
            topPrjTbl = new PdfPTable(2);
            topPrjTbl.setWidthPercentage(100);
            //PdfPCell topPrjTitleCell = new PdfPCell(new Paragraph(topPrjTrn + " (" + currName + ")", HEADERFONT));
            //topPrjTitleCell.setColspan(2);
            //topPrjTbl.addCell(topPrjTitleCell);
            cell = new PdfPCell(new Paragraph(projectTrn, HEADERFONT));
            topPrjTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(fundTypeTrn, HEADERFONT));
            topPrjTbl.addCell(cell);
            Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
            if (topProjects!=null){
	            list = new LinkedList(topProjects.entrySet());
			    for (Iterator it = list.iterator(); it.hasNext();) {
			        Map.Entry entry = (Map.Entry)it.next();
			        cell = new PdfPCell(new Paragraph(entry.getKey().toString()));
			        topPrjTbl.addCell(cell);
	            	cell = new PdfPCell(new Paragraph(entry.getValue().toString()));
	            	topPrjTbl.addCell(cell);
			    }
			    doc.add(topPrjTbl);
	            doc.add(new Paragraph(" "));
            }
            
            //ODA Growth 
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            if (ODAGrowthOpt.equals("1")) {
	            	doc.newPage();
	            	subTitle = new Paragraph(ODAGrowthTrn, SUBTITLEFONT);
	                subTitle.setAlignment(Element.ALIGN_LEFT);
	                doc.add(subTitle);
	                doc.add(new Paragraph(" "));
	                PdfPTable ODAGraph = new PdfPTable(1);
		            ODAGraph.setWidthPercentage(100);
		            ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(vForm.getExportData().getODAGrowthGraph(), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
		            //img = Image.getInstance(vForm.getExportData().getFundingGraph(),null);
		            ODAGraph.addCell(img);
		            //cell = new PdfPCell(new Paragraph(odaGrowthTrn, HEADERFONT));
		            //ODAGraph.addCell(cell);
		            doc.add(ODAGraph);
		            doc.add(new Paragraph(" "));
	            }
            }
          
          //Funding Table.
            if (!fundingOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(fundingTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	            PdfPTable fundingTbl = null;
	            String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	            colspan = (fundingRows[1].split(">").length + 1)/2; 
	            fundingTbl = new PdfPTable(colspan);
	            fundingTbl.setWidthPercentage(100);
	            //PdfPCell fundingTitleCell = new PdfPCell(new Paragraph(fundingTrn + " (" + currName + ")", HEADERFONT));
	            //fundingTitleCell.setColspan(colspan);
	            //fundingTbl.addCell(fundingTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            fundingTbl.addCell(cell);
	            singleRow = fundingRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	fundingTbl.addCell(cell);
				}
	            for (int i = 1; i < fundingRows.length; i++) {
	            	singleRow = fundingRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	fundingTbl.addCell(cell);
	    			}
				}
	            doc.add(fundingTbl);
	            doc.add(new Paragraph(" "));
            }
            if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	            PdfPTable fundingGraph = new PdfPTable(1);
	            fundingGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getFundingGraph(),null);
	            fundingGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(fundingTrn, HEADERFONT));
	            //fundingGraph.addCell(cell);
	            doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Predictability Table.
            if (!aidPredicOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidPredTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	            PdfPTable aidPredTbl = null;
	            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
	            colspan = (aidPredRows[1].split(">").length + 1)/2; 
	            aidPredTbl = new PdfPTable(colspan);
	            aidPredTbl.setWidthPercentage(100);
	            //PdfPCell aidPredTitleCell = new PdfPCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONT));
	            //aidPredTitleCell.setColspan(colspan);
	            //aidPredTbl.addCell(aidPredTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(plannedTrn, HEADERFONT));
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(actualTrn, HEADERFONT));
	            aidPredTbl.addCell(cell);
	            for (int i = 1; i < aidPredRows.length; i++) {
	            	singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	aidPredTbl.addCell(cell);
	    			}
				}
	            doc.add(aidPredTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
	            PdfPTable aidPredGraph = new PdfPTable(1);
	            aidPredGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getAidPredictabilityGraph(),null);
	            aidPredGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(aidPredTrn, HEADERFONT));
	            //aidPredGraph.addCell(cell);
	            doc.add(aidPredGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Type Table.
            if (!aidTypeOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidTypeTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	            PdfPTable aidTypeTbl = null;
	            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
	            colspan = (aidTypeRows[1].split(">").length + 1)/2; 
	            aidTypeTbl = new PdfPTable(colspan);
	            aidTypeTbl.setWidthPercentage(100);
	            //PdfPCell aidTypeTitleCell = new PdfPCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONT));
	            //aidTypeTitleCell.setColspan(colspan);
	            //aidTypeTbl.addCell(aidTypeTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            aidTypeTbl.addCell(cell);
	            singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	aidTypeTbl.addCell(cell);
				}
	            for (int i = 1; i < aidTypeRows.length; i++) {
	            	singleRow = aidTypeRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	aidTypeTbl.addCell(cell);
	    			}
				}
	            doc.add(aidTypeTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
	            PdfPTable aidTypeGraph = new PdfPTable(1);
	            aidTypeGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getAidTypeGraph(),null);
	            aidTypeGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(aidTypeTrn, HEADERFONT));
	            //aidTypeGraph.addCell(cell);
	            doc.add(aidTypeGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Financing Instrument Table.
            if (!financingInstOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(finInstTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
                PdfPTable finInstTbl = null;
	            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
	            colspan = (finInstRows[1].split(">").length + 1)/2; 
	            finInstTbl = new PdfPTable(colspan);
	            finInstTbl.setWidthPercentage(100);
	            //PdfPCell finInstTitleCell = new PdfPCell(new Paragraph(finInstTrn + " (" + currName + ")", HEADERFONT));
	            //finInstTitleCell.setColspan(colspan);
	            //finInstTbl.addCell(finInstTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            finInstTbl.addCell(cell);
	            singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	finInstTbl.addCell(cell);
				}
	            for (int i = 1; i < finInstRows.length; i++) {
	            	singleRow = finInstRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	finInstTbl.addCell(cell);
	    			}
				}
	            doc.add(finInstTbl);
	            doc.add(new Paragraph(" "));
            }
            if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
                PdfPTable finInstGraph = new PdfPTable(1);
	            finInstGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getFinancingInstGraph(),null);
	            finInstGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(finInstTrn, HEADERFONT));
	            //finInstGraph.addCell(cell);
	            doc.add(finInstGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Sector Profile Table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            if (!sectorOpt.equals("0")){
	            	doc.newPage();
	            	subTitle = new Paragraph(sectorProfTrn + " (" + currName + ")", SUBTITLEFONT);
	                subTitle.setAlignment(Element.ALIGN_LEFT);
	                doc.add(subTitle);
	                doc.add(new Paragraph(" "));
	            }
	            if (sectorOpt.equals("1") || sectorOpt.equals("3")){
	                PdfPTable sectorProfTbl = null;
		            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
		            colspan = sectorProfRows[1].split(">").length; 
		            sectorProfTbl = new PdfPTable(colspan);
		            sectorProfTbl.setWidthPercentage(100);
		            //PdfPCell sectorProfTitleCell = new PdfPCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONT));
		            //sectorProfTitleCell.setColspan(colspan);
		            //sectorProfTbl.addCell(sectorProfTitleCell);
		            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
		            sectorProfTbl.addCell(cell);
		            singleRow = sectorProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
		                sectorProfTbl.addCell(cell);
					}
		            for (int i = 2; i < sectorProfRows.length; i++) {
		            	singleRow = sectorProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new PdfPCell(new Paragraph(singleRow[j]));
		                	sectorProfTbl.addCell(cell);
		    			}
					}
		            doc.add(sectorProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (sectorOpt.equals("2") || sectorOpt.equals("3")){
	                PdfPTable sectorGraph = new PdfPTable(1);
		            sectorGraph.setWidthPercentage(100);
		            ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
		            //img = Image.getInstance(vForm.getExportData().getSectorGraph(),null);
		            sectorGraph.addCell(img);
		            //cell = new PdfPCell(new Paragraph(sectorProfTrn, HEADERFONT));
		            //sectorGraph.addCell(cell);
		            doc.add(sectorGraph);
		            doc.add(new Paragraph(" "));
	            }
            }
            
          //Region Profile Table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            if (!regionOpt.equals("0")){
	            	doc.newPage();
	            	subTitle = new Paragraph(regionProfTrn + " (" + currName + ")", SUBTITLEFONT);
	                subTitle.setAlignment(Element.ALIGN_LEFT);
	                doc.add(subTitle);
	                doc.add(new Paragraph(" "));
	            }
	            if (regionOpt.equals("1") || regionOpt.equals("3")){
	                PdfPTable regionProfTbl = null;
		            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
		            colspan = regionProfRows[1].split(">").length; 
		            regionProfTbl = new PdfPTable(colspan);
		            regionProfTbl.setWidthPercentage(100);
		            //PdfPCell regionProfTitleCell = new PdfPCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONT));
		            //regionProfTitleCell.setColspan(colspan);
		            //regionProfTbl.addCell(regionProfTitleCell);
		            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
		            regionProfTbl.addCell(cell);
		            singleRow = regionProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
		            	regionProfTbl.addCell(cell);
					}
		            for (int i = 2; i < regionProfRows.length; i++) {
		            	singleRow = regionProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new PdfPCell(new Paragraph(singleRow[j]));
		                	regionProfTbl.addCell(cell);
		    			}
					}
		            doc.add(regionProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (regionOpt.equals("2") || regionOpt.equals("3")){
		            PdfPTable regionGraph = new PdfPTable(1);
		            regionGraph.setWidthPercentage(100);
		            ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
		            //img = Image.getInstance(vForm.getExportData().getRegionGraph(),null);
		            regionGraph.addCell(img);
		            //cell = new PdfPCell(new Paragraph(regionProfTrn, HEADERFONT));
		            //regionGraph.addCell(cell);
		            doc.add(regionGraph);
		            doc.add(new Paragraph(" "));
	            }
            }
            
          //Donor Profile Table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            if (!donorOpt.equals("0")){
	            	doc.newPage();
	            	subTitle = new Paragraph(donorProfTrn + " (" + currName + ")", SUBTITLEFONT);
	                subTitle.setAlignment(Element.ALIGN_LEFT);
	                doc.add(subTitle);
	                doc.add(new Paragraph(" "));
	            }
	            if (donorOpt.equals("1") || donorOpt.equals("3")){
	                PdfPTable donorProfTbl = null;
		            String[] donorProfRows = vForm.getExportData().getDonorTableData().split("<");
		            colspan = donorProfRows[1].split(">").length; 
		            donorProfTbl = new PdfPTable(colspan);
		            donorProfTbl.setWidthPercentage(100);
		            //PdfPCell donorProfTitleCell = new PdfPCell(new Paragraph(donorProfTrn + " (" + currName + ")", HEADERFONT));
		            //donorProfTitleCell.setColspan(colspan);
		            //donorProfTbl.addCell(donorProfTitleCell);
		            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
		            donorProfTbl.addCell(cell);
		            singleRow = donorProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
		            	donorProfTbl.addCell(cell);
					}
		            for (int i = 2; i < donorProfRows.length; i++) {
		            	singleRow = donorProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new PdfPCell(new Paragraph(singleRow[j]));
		                	donorProfTbl.addCell(cell);
		    			}
					}
		            doc.add(donorProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (donorOpt.equals("2") || donorOpt.equals("3")){
		            PdfPTable donorGraph = new PdfPTable(1);
		            donorGraph.setWidthPercentage(100);
		            ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(vForm.getExportData().getDonorGraph(), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
		            //img = Image.getInstance(vForm.getExportData().getRegionGraph(),null);
		            donorGraph.addCell(img);
		            //cell = new PdfPCell(new Paragraph(regionProfTrn, HEADERFONT));
		            //donorGraph.addCell(cell);
		            doc.add(donorGraph);
		            doc.add(new Paragraph(" "));
	            }
            }
            //close document
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            logger.error("error", ex);
        }

        return null;
    }
}
