package org.digijava.module.visualization.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;
import com.sun.media.jai.codec.PNGEncodeParam;
import com.sun.media.jai.codecimpl.PNGCodec;
import com.sun.media.jai.codecimpl.PNGImageEncoder;


public class ExportToWord extends Action {

	private static Logger logger = Logger.getLogger(ExportToPDF.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(0, 255, 0);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=dashboard.doc");
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
            String topPrjTrn = TranslatorWorker.translateText("Top 5 Projects", langCode, siteId);
            String ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth", langCode, siteId);
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
	            	dashboardTypeTrn = TranslatorWorker.translateText("Donor", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.SECTOR:
					dashboardTypeTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.REGION:
					dashboardTypeTrn = TranslatorWorker.translateText("Region", langCode, siteId);
					break;
			
			}
        
            RtfWriter2.getInstance(doc, baos);
            HttpSession session = request.getSession();
            String footerText = pageTrn + " - ";
            doc.setPageCount(1);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), true);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.setMargins(20, 20, 40, 40);
            doc.open();
            Paragraph pageTitle = new Paragraph(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn.toUpperCase(), TITLEFONT);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
     
            RtfCell cell = null;
            List list = null;
            int colspan = 0;
            Image img = null;
            String[] singleRow = null;
            int count = 0;
            
          //Filters.
            Table filtersTbl = null;
            filtersTbl = new Table(1);
            filtersTbl.setWidth(100);
            RtfCell filterTitleCell = new RtfCell(new Paragraph(filtersTrn, HEADERFONTWHITE));
            filterTitleCell.setColspan(1);
            filterTitleCell.setBackgroundColor(TITLECOLOR);
            filtersTbl.addCell(filterTitleCell);
            
            cell = new RtfCell(new Paragraph("All amounts in millions"));
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph("Currency Type: " + vForm.getFilter().getCurrencyCode()));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph("Fiscal Start Year: " + vForm.getFilter().getYear()));
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph("Years in Range: " + vForm.getFilter().getYearsInRange()));
            cell.setBackgroundColor(CELLCOLOR);
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
            cell = new RtfCell(new Paragraph("Organization Groups: " + itemList));
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
            cell = new RtfCell(new Paragraph("Organizations: " + itemList));
            cell.setBackgroundColor(CELLCOLOR);
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
            cell = new RtfCell(new Paragraph("Sectors: " + itemList));
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
            cell = new RtfCell(new Paragraph("Locations: " + itemList));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            
		    doc.add(filtersTbl);
            doc.add(new Paragraph(" "));
            
          //Summary table.
            if (summaryOpt.equals("1")) {
				Table summaryTbl = new Table(6);
				summaryTbl.setBorder(1);
				summaryTbl.setBorderWidth(5);
				summaryTbl.setBorderColor(Color.RED);
	            summaryTbl.setWidth(100);
	            RtfCell sumamaryTitleCell = new RtfCell(new Paragraph(summaryTrn + " (" + currName + ")", HEADERFONTWHITE));
	            sumamaryTitleCell.setColspan(6);
	            summaryTbl.addCell(sumamaryTitleCell);
	            cell = new RtfCell(new Paragraph(totalCommsTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(totalDisbsTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(numberPrjTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(numberDonTrn, HEADERFONTWHITE));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	cell.setBackgroundColor(TITLECOLOR);
		            summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new RtfCell(new Paragraph(numberRegTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new RtfCell(new Paragraph(numberSecTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            }
	            cell = new RtfCell(new Paragraph(avgPrjZSizeTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getTotalCommitments().toString(), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getTotalDisbursements().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfDonors().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            }
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getAverageProjectSize().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
	            
            Paragraph subTitle = new Paragraph(topPrjTrn + " (" + currName + ")", SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            
          //Top projects table.
            Table topPrjTbl = null;
            topPrjTbl = new Table(2);
            topPrjTbl.setWidth(100);
            //RtfCell topPrjTitleCell = new RtfCell(new Paragraph(topPrjTrn + " (" + currName + ")", HEADERFONTWHITE));
            //topPrjTitleCell.setColspan(2);
            //topPrjTitleCell.setBackgroundColor(TITLECOLOR);
            //topPrjTbl.addCell(topPrjTitleCell);
            cell = new RtfCell(new Paragraph(projectTrn, HEADERFONTWHITE));
            cell.setBackgroundColor(TITLECOLOR);
            topPrjTbl.addCell(cell);
            cell = new RtfCell(new Paragraph(fundTypeTrn, HEADERFONTWHITE));
            cell.setBackgroundColor(TITLECOLOR);
            topPrjTbl.addCell(cell);
            Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
            if (topProjects != null){
	            list = new LinkedList(topProjects.entrySet());
	            count = 0;
			    for (Iterator it = list.iterator(); it.hasNext();) {
			        Map.Entry entry = (Map.Entry)it.next();
			        cell = new RtfCell(new Paragraph(entry.getKey().toString()));
			        if (count % 2 == 0)
			        	cell.setBackgroundColor(CELLCOLOR);
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				    topPrjTbl.addCell(cell);
				    cell = new RtfCell(new Paragraph(entry.getValue().toString()));
				    if (count % 2 == 0)
			        	cell.setBackgroundColor(CELLCOLOR);
				    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				    topPrjTbl.addCell(cell);
				    count++;
			    }
			    doc.add(topPrjTbl);
	            doc.add(new Paragraph(" "));
            }
            
            //ODA Growth 
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	if (!ODAGrowthOpt.equals("0")){
                	doc.newPage();
                	subTitle = new Paragraph(ODAGrowthTrn + " (" + currName + ")", SUBTITLEFONT);
                    subTitle.setAlignment(Element.ALIGN_LEFT);
                    doc.add(subTitle);
                    doc.add(new Paragraph(" "));
                }
            	if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
    	            Table ODAGrowthTbl = null;
    	            String[] ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");
    	            colspan = (ODAGrowthRows[1].split(">").length); 
    	            ODAGrowthTbl = new Table(colspan);
    	            ODAGrowthTbl.setWidth(100);
    	            singleRow = ODAGrowthRows[1].split(">");
    	            for (int i = 0; i < singleRow.length; i++) {
    	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
    	            	cell.setBackgroundColor(TITLECOLOR);
    	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	            	ODAGrowthTbl.addCell(cell);
    				}
    	            count = 0;
    	            for (int i = 2; i < ODAGrowthRows.length; i++) {
    	            	singleRow = ODAGrowthRows[i].split(">");
    	            	for (int j = 0; j < singleRow.length; j++) {
    	                	cell = new RtfCell(new Paragraph(singleRow[j]));
    	                	if (count % 2 == 0)
    	    		        	cell.setBackgroundColor(CELLCOLOR);
    	            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	                	ODAGrowthTbl.addCell(cell);
    	    			}
    	            	count++;
    				}
    	            doc.add(ODAGrowthTbl);
    	            doc.add(new Paragraph(" "));
                }
            	if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
	            	SimpleTable ODAGraph = new SimpleTable();
	                SimpleCell row = new SimpleCell(SimpleCell.ROW);
	                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
	                //cel.setBorder(1);
	                ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(scaleImage(vForm.getExportData().getODAGrowthGraph(),580,410), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
	                cel.add(img);
	                row.add(cel);
	                ODAGraph.setWidthpercentage(100);
	                ODAGraph.addElement(row);
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
            }
            if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	            Table fundingTbl = null;
	            String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	            colspan = (fundingRows[1].split(">").length + 1)/2; 
	            fundingTbl = new Table(colspan);
	            fundingTbl.setWidth(100);
	            //RtfCell fundingTitleCell = new RtfCell(new Paragraph(fundingTrn + " (" + currName + ")", HEADERFONTWHITE));
	            //fundingTitleCell.setColspan(colspan);
	            //fundingTitleCell.setBackgroundColor(TITLECOLOR);
	            //fundingTbl.addCell(fundingTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            fundingTbl.addCell(cell);
	            singleRow = fundingRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
	            	cell.setBackgroundColor(TITLECOLOR);
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            fundingTbl.addCell(cell);
				}
	            count = 0;
	            for (int i = 1; i < fundingRows.length; i++) {
	            	singleRow = fundingRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		cell = new RtfCell(new Paragraph(singleRow[j]));
	            		if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
	            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    			    fundingTbl.addCell(cell);
	    			}
	            	count++;
				}
	            doc.add(fundingTbl);
	            doc.add(new Paragraph(" "));
            }
            if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	            SimpleTable fundingGraph = new SimpleTable(); //col,row 
                SimpleCell row = new SimpleCell(SimpleCell.ROW);
                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
                //cel.setBorder(1);
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(scaleImage(vForm.getExportData().getFundingGraph(),580,410), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Predictability Table.
            if (!aidPredicOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidPredTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
            }
            if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	            Table aidPredTbl = null;
	            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
	            colspan = (aidPredRows[1].split(">").length + 1)/2; 
	            aidPredTbl = new Table(colspan);
	            aidPredTbl.setWidth(100);
	            //RtfCell aidPredTitleCell = new RtfCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONTWHITE));
	            //aidPredTitleCell.setColspan(colspan);
	            //aidPredTitleCell.setBackgroundColor(TITLECOLOR);
	            //aidPredTbl.addCell(aidPredTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(plannedTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(actualTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            count = 0;
	            for (int i = 1; i < aidPredRows.length; i++) {
	            	singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		cell = new RtfCell(new Paragraph(singleRow[j]));
	            		if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
	            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    			    aidPredTbl.addCell(cell);
	    			}
	            	count++;
				}
	            doc.add(aidPredTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
	            SimpleTable fundingGraph = new SimpleTable();
                SimpleCell row = new SimpleCell(SimpleCell.ROW);
                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
                //cel.setBorder(1);
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(scaleImage(vForm.getExportData().getAidPredictabilityGraph(),580,410), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Type Table.
            if (!aidTypeOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidTypeTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
            }
            if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	            Table aidTypeTbl = null;
	            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
	            colspan = (aidTypeRows[1].split(">").length + 1)/2; 
	            aidTypeTbl = new Table(colspan);
	            aidTypeTbl.setWidth(100);
	            //RtfCell aidTypeTitleCell = new RtfCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONTWHITE));
	            //aidTypeTitleCell.setColspan(colspan);
	            //aidTypeTitleCell.setBackgroundColor(TITLECOLOR);
	            //aidTypeTbl.addCell(aidTypeTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            aidTypeTbl.addCell(cell);
	            singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	cell.setBackgroundColor(TITLECOLOR);
		            aidTypeTbl.addCell(cell);
				}
	            count = 0;
	            for (int i = 1; i < aidTypeRows.length; i++) {
	            	singleRow = aidTypeRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new RtfCell(new Paragraph(singleRow[j]));
	                	if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    			    aidTypeTbl.addCell(cell);
	    			}
	            	count++;
				}
	            doc.add(aidTypeTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
	            SimpleTable fundingGraph = new SimpleTable();
                SimpleCell row = new SimpleCell(SimpleCell.ROW);
                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
                //cel.setBorder(1);
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(scaleImage(vForm.getExportData().getAidTypeGraph(),580,410), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Financing Instrument Table.
            if (!financingInstOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(finInstTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
            }
            if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
                Table finInstTbl = null;
	            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
	            colspan = (finInstRows[1].split(">").length + 1)/2; 
	            finInstTbl = new Table(colspan);
	            finInstTbl.setWidth(100);
	            //RtfCell finInstTitleCell = new RtfCell(new Paragraph(finInstTrn + " (" + currName + ")", HEADERFONTWHITE));
	            //finInstTitleCell.setColspan(colspan);
	            //finInstTitleCell.setBackgroundColor(TITLECOLOR);
	            //finInstTbl.addCell(finInstTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(TITLECOLOR);
	            finInstTbl.addCell(cell);
	            singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	cell.setBackgroundColor(TITLECOLOR);
		            finInstTbl.addCell(cell);
				}
	            count = 0;
	            for (int i = 1; i < finInstRows.length; i++) {
	            	singleRow = finInstRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	cell = new RtfCell(new Paragraph(singleRow[j]));
	                	if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    			    finInstTbl.addCell(cell);
	    			}
	            	count++;
				}
	            doc.add(finInstTbl);
	            doc.add(new Paragraph(" "));
            }
            if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
                SimpleTable fundingGraph = new SimpleTable();
                SimpleCell row = new SimpleCell(SimpleCell.ROW);
                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
                //cel.setBorder(1);
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(scaleImage(vForm.getExportData().getFinancingInstGraph(),580,410), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Sector Profile Table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            if (!sectorOpt.equals("0")){
	            	doc.newPage();
	            	subTitle = new Paragraph(sectorProfTrn + " (" + currName + ")", SUBTITLEFONT);
	                subTitle.setAlignment(Element.ALIGN_LEFT);
	                doc.add(subTitle);
	            }
	            if (sectorOpt.equals("1") || sectorOpt.equals("3")){
	                Table sectorProfTbl = null;
		            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
		            colspan = sectorProfRows[1].split(">").length; 
		            sectorProfTbl = new Table(colspan);
		            sectorProfTbl.setWidth(100);
		            //RtfCell sectorProfTitleCell = new RtfCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //sectorProfTitleCell.setColspan(colspan);
		            //sectorProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //sectorProfTbl.addCell(sectorProfTitleCell);
		            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            sectorProfTbl.addCell(cell);
		            singleRow = sectorProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
			            sectorProfTbl.addCell(cell);
					}
		            count = 0;
		            for (int i = 2; i < sectorProfRows.length; i++) {
		            	singleRow = sectorProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    sectorProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(sectorProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (sectorOpt.equals("2") || sectorOpt.equals("3")){
	                SimpleTable fundingGraph = new SimpleTable();
	                SimpleCell row = new SimpleCell(SimpleCell.ROW);
	                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
	                //cel.setBorder(1);
	                ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(scaleImage(vForm.getExportData().getSectorGraph(),580,410), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
	                cel.add(img);
	                row.add(cel);
	                fundingGraph.setWidthpercentage(100);
	                fundingGraph.addElement(row);
	                doc.add(fundingGraph);
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
	            }
	            if (regionOpt.equals("1") || regionOpt.equals("3")){
	                Table regionProfTbl = null;
		            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
		            colspan = regionProfRows[1].split(">").length; 
		            regionProfTbl = new Table(colspan);
		            regionProfTbl.setWidth(100);
		            //RtfCell regionProfTitleCell = new RtfCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //regionProfTitleCell.setColspan(colspan);
		            //regionProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //regionProfTbl.addCell(regionProfTitleCell);
		            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            regionProfTbl.addCell(cell);
		            singleRow = regionProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
			            regionProfTbl.addCell(cell);
					}
		            count = 0;
		            for (int i = 2; i < regionProfRows.length; i++) {
		            	singleRow = regionProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    regionProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(regionProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (regionOpt.equals("2") || regionOpt.equals("3")){
		            SimpleTable fundingGraph = new SimpleTable();
	                SimpleCell row = new SimpleCell(SimpleCell.ROW);
	                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
	                //cel.setBorder(1);
	                ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(scaleImage(vForm.getExportData().getRegionGraph(),580,410), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
	                cel.add(img);
	                row.add(cel);
	                fundingGraph.setWidthpercentage(100);
	                fundingGraph.addElement(row);
	                doc.add(fundingGraph);
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
	            }
	            if (donorOpt.equals("1") || donorOpt.equals("3")){
	                Table donorProfTbl = null;
		            String[] donorProfRows = vForm.getExportData().getDonorTableData().split("<");
		            colspan = donorProfRows[1].split(">").length; 
		            donorProfTbl = new Table(colspan);
		            donorProfTbl.setWidth(100);
		            //RtfCell donorProfTitleCell = new RtfCell(new Paragraph(donorProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //donorProfTitleCell.setColspan(colspan);
		            //donorProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //donorProfTbl.addCell(donorProfTitleCell);
		            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            donorProfTbl.addCell(cell);
		            singleRow = donorProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	donorProfTbl.addCell(cell);
					}
		            count = 0;
		            for (int i = 2; i < donorProfRows.length; i++) {
		            	singleRow = donorProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	donorProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(donorProfTbl);
		            doc.add(new Paragraph(" "));
	            }
	            if (donorOpt.equals("2") || donorOpt.equals("3")){
		            SimpleTable donorGraph = new SimpleTable();
	                SimpleCell row = new SimpleCell(SimpleCell.ROW);
	                SimpleCell cel = new SimpleCell(SimpleCell.CELL);
	                //cel.setBorder(1);
	                ByteArrayOutputStream ba = new ByteArrayOutputStream();
		            ImageIO.write(scaleImage(vForm.getExportData().getDonorGraph(),580,410), "png", ba);
		            img = Image.getInstance(ba.toByteArray());
	                cel.add(img);
	                row.add(cel);
	                donorGraph.setWidthpercentage(100);
	                donorGraph.addElement(row);
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
    
    public static BufferedImage scaleImage(BufferedImage image, int p_width, int p_height) throws Exception {

    	int thumbWidth = p_width;
        int thumbHeight = p_height;        
    
           // Make sure the aspect ratio is maintained, so the image is not skewed
           double thumbRatio = (double)thumbWidth / (double)thumbHeight;
           float imageWidth = image.getWidth();
           float imageHeight = image.getHeight();
           double imageRatio = (double)imageWidth / (double)imageHeight;
           if (thumbRatio < imageRatio) {
             thumbHeight = (int)(thumbWidth / imageRatio);
           } else {
             thumbWidth = (int)(thumbHeight * imageRatio);
           }
    
           // Draw the scaled image
           BufferedImage thumbImage = new BufferedImage(thumbWidth, 
             thumbHeight, BufferedImage.TYPE_INT_RGB);
           Graphics2D graphics2D = thumbImage.createGraphics();
           graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
             RenderingHints.VALUE_INTERPOLATION_BILINEAR);
           graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
    
           // Write the scaled image to the outputstream
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam(thumbImage);
           PNGImageEncoder encoder = (PNGImageEncoder) PNGCodec.createImageEncoder("png",out,param);
           encoder.setParam(param);
           //int quality = 100; // Use between 1 and 100, with 100 being highest quality
           //quality = Math.max(0, Math.min(quality, 100));
           //param.setQuality((float)quality / 100.0f, false);
           encoder.encode(thumbImage);        
           ImageIO.write(thumbImage, "jpg" , out); 
    
           return thumbImage;        
       }
}
