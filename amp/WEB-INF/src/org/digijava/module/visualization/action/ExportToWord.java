package org.digijava.module.visualization.action;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
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


public class ExportToWord extends Action {

	private static Logger logger = Logger.getLogger(ExportToPDF.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
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
            String footerText = dashboardTypeTrn + " " + dashboardTrn;
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), false);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.setMargins(20, 20, 40, 40);
            doc.open();
            com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
            Paragraph pageTitle = new Paragraph(dashboardTypeTrn + " " + dashboardTrn, pageTitleFont);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
     
            RtfCell cell = null;
            List list = null;
            int colspan = 0;
            Image img = null;
            String[] singleRow = null;
            int count = 0;
            
          //Summary table.
            if (summaryOpt.equals("1")) {
				Table summaryTbl = null;
	            summaryTbl = new Table(6);
	            summaryTbl.setWidth(100);
	            RtfCell sumamaryTitleCell = new RtfCell(new Paragraph(summaryTrn + " (" + currName + ")", HEADERFONTWHITE));
	            sumamaryTitleCell.setColspan(6);
	            summaryTbl.addCell(sumamaryTitleCell);
	            cell = new RtfCell(new Paragraph(totalCommsTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(totalDisbsTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(numberPrjTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(numberDonTrn, HEADERFONTWHITE));
	            	cell.setBackgroundColor(TITLECOLOR);
		            summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new RtfCell(new Paragraph(numberRegTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new RtfCell(new Paragraph(numberSecTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            summaryTbl.addCell(cell);
	            }
	            cell = new RtfCell(new Paragraph(avgPrjZSizeTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            summaryTbl.addCell(cell);
	            
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getTotalCommitments().toString(), HEADERFONT));
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getTotalDisbursements().toString()));
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            summaryTbl.addCell(cell);
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfDonors().toString()));
	                summaryTbl.addCell(cell);
				}
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	                summaryTbl.addCell(cell);
	            }
	            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	                summaryTbl.addCell(cell);
	            }
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getAverageProjectSize().toString()));
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
	            
          //Top projects table.
            Table topPrjTbl = null;
            topPrjTbl = new Table(2);
            topPrjTbl.setWidth(100);
            RtfCell topPrjTitleCell = new RtfCell(new Paragraph(topPrjTrn + " (" + currName + ")", HEADERFONTWHITE));
            topPrjTitleCell.setColspan(2);
            topPrjTitleCell.setBackgroundColor(TITLECOLOR);
            topPrjTbl.addCell(topPrjTitleCell);
            cell = new RtfCell(new Paragraph(projectTrn, HEADERFONTWHITE));
            cell.setBackgroundColor(TITLECOLOR);
            topPrjTbl.addCell(cell);
            cell = new RtfCell(new Paragraph(fundTypeTrn, HEADERFONTWHITE));
            cell.setBackgroundColor(TITLECOLOR);
            topPrjTbl.addCell(cell);
            Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
            list = new LinkedList(topProjects.entrySet());
            count = 0;
		    for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        cell = new RtfCell(new Paragraph(entry.getKey().toString()));
		        if (count % 2 == 0)
		        	cell.setBackgroundColor(CELLCOLOR);
			    topPrjTbl.addCell(cell);
			    cell = new RtfCell(new Paragraph(entry.getValue().toString()));
			    if (count % 2 == 0)
		        	cell.setBackgroundColor(CELLCOLOR);
			    topPrjTbl.addCell(cell);
            	count++;
		    }
		    doc.add(topPrjTbl);
            doc.add(new Paragraph(" "));
            /*
          //Top sectors table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR){
	            Table topSectorTbl = null;
	            topSectorTbl = new Table(2);
	            topSectorTbl.setWidth(100);
	            RtfCell topSectorTitleCell = new RtfCell(new Paragraph(topSectorTrn + " (" + currName + ")", HEADERFONTWHITE));
	            topSectorTitleCell.setColspan(2);
	            topSectorTbl.addCell(topSectorTitleCell);
	            cell = new RtfCell(new Paragraph(sectorTrn, HEADERFONTWHITE));
	            topSectorTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(fundTypeTrn, HEADERFONTWHITE));
	            topSectorTbl.addCell(cell);
	            Map<AmpSector, BigDecimal> topSectors = vForm.getRanksInformation().getTopSectors();
	            list = new LinkedList(topSectors.entrySet());
			    for (Iterator it = list.iterator(); it.hasNext();) {
			        Map.Entry entry = (Map.Entry)it.next();
			        cell = new RtfCell(new Paragraph(entry.getKey().toString()));
			        topSectorTbl.addCell(cell);
	            	cell = new RtfCell(new Paragraph(entry.getValue().toString()));
	            	topSectorTbl.addCell(cell);
			    }
			    doc.add(topSectorTbl);
	            doc.add(new Paragraph(" "));
            }
            
          //Top donors table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR){
	            Table topDonorTbl = null;
	            topDonorTbl = new Table(2);
	            topDonorTbl.setWidth(100);
	            RtfCell topDonorTitleCell = new RtfCell(new Paragraph(topDonorTrn + " (" + currName + ")", HEADERFONTWHITE));
	            topDonorTitleCell.setColspan(2);
	            topDonorTbl.addCell(topDonorTitleCell);
	            cell = new RtfCell(new Paragraph(donorTrn, HEADERFONTWHITE));
	            topDonorTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(fundTypeTrn, HEADERFONTWHITE));
	            topDonorTbl.addCell(cell);
	            Map<AmpOrganisation, BigDecimal> topDonors = vForm.getRanksInformation().getTopDonors();
	            list = new LinkedList(topDonors.entrySet());
			    for (Iterator it = list.iterator(); it.hasNext();) {
			        Map.Entry entry = (Map.Entry)it.next();
			        cell = new RtfCell(new Paragraph(entry.getKey().toString()));
			        topDonorTbl.addCell(cell);
	            	cell = new RtfCell(new Paragraph(entry.getValue().toString()));
	            	topDonorTbl.addCell(cell);
			    }
			    doc.add(topDonorTbl);
	            doc.add(new Paragraph(" "));
            }
            
          //Top regions table.
            if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION){
	            Table topRegionTbl = null;
	            topRegionTbl = new Table(2);
	            topRegionTbl.setWidth(100);
	            RtfCell topRegionTitleCell = new RtfCell(new Paragraph(topRegionTrn + " (" + currName + ")", HEADERFONTWHITE));
	            topRegionTitleCell.setColspan(2);
	            topRegionTbl.addCell(topRegionTitleCell);
	            cell = new RtfCell(new Paragraph(regionTrn, HEADERFONTWHITE));
	            topRegionTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(fundTypeTrn, HEADERFONTWHITE));
	            topRegionTbl.addCell(cell);
	            Map<AmpCategoryValueLocations, BigDecimal> topRegions = vForm.getRanksInformation().getTopRegions();
	            list = new LinkedList(topRegions.entrySet());
			    for (Iterator it = list.iterator(); it.hasNext();) {
			        Map.Entry entry = (Map.Entry)it.next();
			        cell = new RtfCell(new Paragraph(entry.getKey().toString()));
			        topRegionTbl.addCell(cell);
	            	cell = new RtfCell(new Paragraph(entry.getValue().toString()));
	            	topRegionTbl.addCell(cell);
			    }
			    doc.add(topRegionTbl);
	            doc.add(new Paragraph(" "));
            }
            */
          
          //Funding Table.
            if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	            Table fundingTbl = null;
	            String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	            colspan = (fundingRows[1].split(">").length + 1)/2; 
	            fundingTbl = new Table(colspan);
	            fundingTbl.setWidth(100);
	            RtfCell fundingTitleCell = new RtfCell(new Paragraph(fundingTrn + " (" + currName + ")", HEADERFONTWHITE));
	            fundingTitleCell.setColspan(colspan);
	            fundingTitleCell.setBackgroundColor(TITLECOLOR);
	            fundingTbl.addCell(fundingTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            fundingTbl.addCell(cell);
	            singleRow = fundingRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
	            	cell.setBackgroundColor(TITLECOLOR);
		            fundingTbl.addCell(cell);
				}
	            count = 0;
	            for (int i = 1; i < fundingRows.length; i++) {
	            	singleRow = fundingRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		cell = new RtfCell(new Paragraph(singleRow[j]));
	            		if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Predictability Table.
            if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	            Table aidPredTbl = null;
	            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
	            colspan = (aidPredRows[1].split(">").length + 1)/2; 
	            aidPredTbl = new Table(colspan);
	            aidPredTbl.setWidth(100);
	            RtfCell aidPredTitleCell = new RtfCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONTWHITE));
	            aidPredTitleCell.setColspan(colspan);
	            aidPredTitleCell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(aidPredTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(plannedTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(actualTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            aidPredTbl.addCell(cell);
	            count = 0;
	            for (int i = 1; i < aidPredRows.length; i++) {
	            	singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	            		cell = new RtfCell(new Paragraph(singleRow[j]));
	            		if (count % 2 == 0)
	    		        	cell.setBackgroundColor(CELLCOLOR);
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Aid Type Table.
            if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	            Table aidTypeTbl = null;
	            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
	            colspan = (aidTypeRows[1].split(">").length + 1)/2; 
	            aidTypeTbl = new Table(colspan);
	            aidTypeTbl.setWidth(100);
	            RtfCell aidTypeTitleCell = new RtfCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONTWHITE));
	            aidTypeTitleCell.setColspan(colspan);
	            aidTypeTitleCell.setBackgroundColor(TITLECOLOR);
	            aidTypeTbl.addCell(aidTypeTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            aidTypeTbl.addCell(cell);
	            singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Financing Instrument Table.
            if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
                Table finInstTbl = null;
	            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
	            colspan = (finInstRows[1].split(">").length + 1)/2; 
	            finInstTbl = new Table(colspan);
	            finInstTbl.setWidth(100);
	            RtfCell finInstTitleCell = new RtfCell(new Paragraph(finInstTrn + " (" + currName + ")", HEADERFONTWHITE));
	            finInstTitleCell.setColspan(colspan);
	            finInstTitleCell.setBackgroundColor(TITLECOLOR);
	            finInstTbl.addCell(finInstTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            finInstTbl.addCell(cell);
	            singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Sector Profile Table.
            if (sectorOpt.equals("1") || sectorOpt.equals("3")){
                Table sectorProfTbl = null;
	            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
	            colspan = sectorProfRows[1].split(">").length; 
	            sectorProfTbl = new Table(colspan);
	            sectorProfTbl.setWidth(100);
	            RtfCell sectorProfTitleCell = new RtfCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONTWHITE));
	            sectorProfTitleCell.setColspan(colspan);
	            sectorProfTitleCell.setBackgroundColor(TITLECOLOR);
	            sectorProfTbl.addCell(sectorProfTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            sectorProfTbl.addCell(cell);
	            singleRow = sectorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
            
            
          //Region Profile Table.
            if (regionOpt.equals("1") || regionOpt.equals("3")){
                Table regionProfTbl = null;
	            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
	            colspan = regionProfRows[1].split(">").length; 
	            regionProfTbl = new Table(colspan);
	            regionProfTbl.setWidth(100);
	            RtfCell regionProfTitleCell = new RtfCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONTWHITE));
	            regionProfTitleCell.setColspan(colspan);
	            regionProfTitleCell.setBackgroundColor(TITLECOLOR);
	            regionProfTbl.addCell(regionProfTitleCell);
	            cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            regionProfTbl.addCell(cell);
	            singleRow = regionProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
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
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
                cel.add(img);
                row.add(cel);
                fundingGraph.setWidthpercentage(100);
                fundingGraph.addElement(row);
                doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
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
