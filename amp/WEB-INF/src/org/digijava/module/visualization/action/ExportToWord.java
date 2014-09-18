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
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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

    String orgInfoTrn = "";
    String orgGrpInfoTrn = "";
	String contactInfoTrn = "";
	String addNotesTrn = "";
	String nameTrn = "";
	String titleTrn = "";
	String emailsTrn = "";
	String phonesTrn = "";
	String faxesTrn = "";
	String backOrgTrn = "";
	String backOrgGrpTrn = "";
	String descriptionTrn = "";
	String keyAreasTrn = "";
	String pageTrn = "";
	String filtersTrn = "";
	String filtersAllTrn = "";
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
    String secProgramProfTrn = "";
    String organizationProfTrn = "";
    String beneficiaryAgencyProfTrn = "";
    String responsibleOrganizationProfTrn = "";
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
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=dashboard.doc");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        //String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        //String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        VisualizationForm vForm = (VisualizationForm) form;
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidPredicQuarterOpt = request.getParameter("aidPredicQuarterOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String budgetBreakdownOpt = request.getParameter("budgetBreakdownOpt");
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
        String ODAGrowthOpt = request.getParameter("ODAGrowthOpt");
        try {
        	String orgInfoTrn = TranslatorWorker.translateText("Organization Information");
            String orgGrpInfoTrn = TranslatorWorker.translateText("Organization Group Information");
        	String contactInfoTrn = TranslatorWorker.translateText("Contact Information");
        	String addNotesTrn = TranslatorWorker.translateText("Additional Notes");
        	String nameTrn = TranslatorWorker.translateText("Name");
        	String titleTrn = TranslatorWorker.translateText("Title");
        	String emailsTrn = TranslatorWorker.translateText("Emails");
        	String phonesTrn = TranslatorWorker.translateText("Phones");
        	String faxesTrn = TranslatorWorker.translateText("Faxes");
        	String backOrgTrn = TranslatorWorker.translateText("Background of organization");
            String backOrgGrpTrn = TranslatorWorker.translateText("Background of organization group");
        	String descriptionTrn = TranslatorWorker.translateText("Description");
            String keyAreasTrn = TranslatorWorker.translateText("Key Areas of Focus");
        	String pageTrn = TranslatorWorker.translateText("Page");
        	String filtersTrn = TranslatorWorker.translateText("Filters");
			String filtersAllTrn = TranslatorWorker.translateText("All");
			String filtersAmountsInTrn = "";
			String filtersMagnitudeTrn = "";
			if(vForm.getFilter().shouldShowAmountsInThousands()){
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in thousands");
				filtersMagnitudeTrn = TranslatorWorker.translateText("Thousands");
			}
			else{
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in millions");
				filtersMagnitudeTrn = TranslatorWorker.translateText("Millions");
			}
			String filtersCurrencyTypeTrn = TranslatorWorker.translateText("Currency Type");
			String filtersStartYearTrn = TranslatorWorker.translateText("Start Year");
			String filtersEndYearTrn = TranslatorWorker.translateText("End Year");
			String filtersOrgGroupTrn = TranslatorWorker.translateText("Organization Groups");
			String filtersOrganizationsTrn = TranslatorWorker.translateText("Organizations");
			String filtersSectorsTrn = TranslatorWorker.translateText("Sectors");
			String filtersLocationsTrn = TranslatorWorker.translateText("Locations");
			String filtersSubSectorsTrn = TranslatorWorker.translateText("Sub-Sectors");
			String filtersRegionsTrn = TranslatorWorker.translateText("Regions");
			String filtersZonesTrn = TranslatorWorker.translateText("Zones");
			String filtersStatusTrn = TranslatorWorker.translateText("Status");
			this.fundingTrn = TranslatorWorker.translateText("ODA Historical Trend");
			this.topPrjTrn = TranslatorWorker.translateText("Top Projects");
			this.ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth");
			this.topSectorTrn = TranslatorWorker.translateText("Top Sectors");
			this.topOrganizationTrn = TranslatorWorker.translateText("Top Organizations");
			this.topRegionTrn = TranslatorWorker.translateText("Top Regions");
            String projectTrn = TranslatorWorker.translateText("Project");
            this.sectorTrn = TranslatorWorker.translateText("Sector");
            this.organizationTrn = TranslatorWorker.translateText("Organization");
            this.regionTrn = TranslatorWorker.translateText("Region");
            this.NPOTrn = TranslatorWorker.translateText("NPO");
            this.programTrn = TranslatorWorker.translateText("Program");
            this.aidPredTrn = TranslatorWorker.translateText("Aid Predictability");
            this.aidPredQuarterTrn = TranslatorWorker.translateText("Aid Predictability Quarterly");
            this.aidTypeTrn = TranslatorWorker.translateText("Aid Type");
            this.budgetBreakdownTrn = TranslatorWorker.translateText("Budget Breakdown");
            this.finInstTrn = TranslatorWorker.translateText("Aid Modality");
            this.sectorProfTrn = TranslatorWorker.translateText("Sector Profile");
            this.regionProfTrn = TranslatorWorker.translateText("Region Profile");
            this.NPOProfTrn = TranslatorWorker.translateText("NPO Profile");
            this.programProfTrn = TranslatorWorker.translateText("Program Profile");
            this.secProgramProfTrn = TranslatorWorker.translateText("Secondary Program Profile");
            this.organizationProfTrn = TranslatorWorker.translateText("Organization Profile");
            this.beneficiaryAgencyProfTrn = TranslatorWorker.translateText("Beneficiary Agency Profile");
            this.responsibleOrganizationProfTrn = TranslatorWorker.translateText("Responsible Organization");
            this.plannedTrn = TranslatorWorker.translateText("Planned");
            this.actualTrn = TranslatorWorker.translateText("Actual");
            this.yearTrn = TranslatorWorker.translateText("Year");
            this.quarterTrn = TranslatorWorker.translateText("Year");
            String dashboardTrn = TranslatorWorker.translateText("Dashboard");
            String summaryTrn = TranslatorWorker.translateText("Summary");
            String totalCommsTrn = TranslatorWorker.translateText("Total Commitments");
            String totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements");
            String numberPrjTrn = TranslatorWorker.translateText("Number of Projects");
            String numberSecTrn = TranslatorWorker.translateText("Number of Sectors");
            String numberDonTrn = TranslatorWorker.translateText("Number of Organizations");
            String numberRegTrn = TranslatorWorker.translateText("Number of Regions");
            String avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size");
            AmpCurrency currency = (AmpCurrency)org.digijava.module.aim.util.DbUtil.getObject(AmpCurrency.class,vForm.getFilter().getCurrencyId());
            String currName = currency.getCurrencyName();
          
            String fundTypeTrn = "";
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
        
            RtfWriter2.getInstance(doc, baos);
            //HttpSession session = request.getSession();
            String footerText = pageTrn + " - ";
            doc.setPageCount(1);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), true);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.setMargins(20, 20, 40, 40);
            doc.open();
            Paragraph pageTitle;
            if(vForm.getDashboard()!=null){
            	pageTitle = new Paragraph(vForm.getDashboard().getName().toUpperCase(), TITLEFONT);
            } else {
            	pageTitle = new Paragraph(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn.toUpperCase(), TITLEFONT);
            }
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
     
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	String itemList = filtersOrgGroupTrn + ": ";
                Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
                if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
    				for (int i = 0; i < orgGroupIds.length; i++) {
    					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
            	Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                itemList = filtersOrganizationsTrn + ": ";
                Long[] orgIds = vForm.getFilter().getSelOrgIds();
                if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
    				for (int i = 0; i < orgIds.length; i++) {
    					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
            	String itemList = filtersSectorsTrn + ": ";
            	String itemList2 = filtersSubSectorsTrn + ": ";
            	Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            	boolean hasSub = false;
                if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
    				for (int i = 0; i < sectorIds.length; i++) {
    					if (SectorUtil.getAmpSector(sectorIds[i]).getParent()==null){
    						itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
    					} else {
    						hasSub = true;
    						itemList2 = itemList2 + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
    					}
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                if (!hasSub) {
                	itemList2 = itemList2 + filtersAllTrn;
				}
            	Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(itemList2, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
            	String itemList = filtersRegionsTrn + ": ";
            	String itemList2 = filtersZonesTrn + ": ";
            	Long[] locationIds = vForm.getFilter().getSelLocationIds();
            	boolean hasSub = false;
                if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
    				for (int i = 0; i < locationIds.length; i++) {
    					if (LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getParentLocation().getParentLocation()==null){
    						itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
    					} else {
    						hasSub = true;
    						itemList2 = itemList2 + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
    					}
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                if (!hasSub) {
                	itemList2 = itemList2 + filtersAllTrn;
				}
            	
                Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(itemList2, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
            
            RtfCell cell = null;
            List list = null;
            //int colspan = 0;
            //Image img = null;
            //String[] singleRow = null;
            int count = 0;
            
          //Org. Information
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	if (vForm.getFilter().getSelOrgIds().length==1 && vForm.getFilter().getSelOrgIds()[0] != -1){
            		long orgId = vForm.getFilter().getSelOrgIds()[0];
            		Table orgInfoTbl = null;
            		orgInfoTbl = new Table(2);
            		orgInfoTbl.setWidth(100);
            		RtfCell orgInfoTitleCell = new RtfCell(new Paragraph(orgInfoTrn, HEADERFONT));
                    orgInfoTitleCell.setColspan(2);
                    orgInfoTitleCell.setBackgroundColor(TITLECOLOR);
                    orgInfoTbl.addCell(orgInfoTitleCell);
            		AmpContact contact=DbUtil.getPrimaryContactForOrganization(orgId);
        			if(contact!=null){
        				RtfCell contInfoTitleCell = new RtfCell(new Paragraph(contactInfoTrn, HEADERFONT));
        				contInfoTitleCell.setColspan(2);
        				contInfoTitleCell.setBackgroundColor(TITLECOLOR);
                        orgInfoTbl.addCell(contInfoTitleCell);
                        cell = new RtfCell(new Paragraph(titleTrn));
                        cell.setBackgroundColor(CELLCOLOR);
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(contact.getTitle()!=null?contact.getTitle().getValue():""));
                        cell.setBackgroundColor(CELLCOLOR);
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(nameTrn));
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(contact.getName()+" "+contact.getLastname()));
                        orgInfoTbl.addCell(cell);
                        if(contact.getProperties()!=null){
            				for (AmpContactProperty property : contact.getProperties()) {
            					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
            						cell = new RtfCell(new Paragraph(emailsTrn));
            						cell.setBackgroundColor(CELLCOLOR);
            			            orgInfoTbl.addCell(cell);
                                    cell = new RtfCell(new Paragraph(property.getValue()));
                                    cell.setBackgroundColor(CELLCOLOR);
                                    orgInfoTbl.addCell(cell);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
            						cell = new RtfCell(new Paragraph(phonesTrn));
                                    orgInfoTbl.addCell(cell);
                                    cell = new RtfCell(new Paragraph(property.getValueAsFormatedPhoneNum()));
                                    orgInfoTbl.addCell(cell);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
            						cell = new RtfCell(new Paragraph(faxesTrn));
            						cell.setBackgroundColor(CELLCOLOR);
            			            orgInfoTbl.addCell(cell);
                                    cell = new RtfCell(new Paragraph(property.getValue()));
                                    cell.setBackgroundColor(CELLCOLOR);
                                    orgInfoTbl.addCell(cell);
            					}
            				}
            			}
        			}
        			AmpOrganisation organization=DbUtil.getOrganisation(orgId);
        			if(organization!=null){
        				RtfCell addNotesTitleCell = new RtfCell(new Paragraph(addNotesTrn, HEADERFONT));
        				addNotesTitleCell.setColspan(2);
        				addNotesTitleCell.setBackgroundColor(TITLECOLOR);
                        orgInfoTbl.addCell(addNotesTitleCell);
                        cell = new RtfCell(new Paragraph(backOrgTrn));
                        cell.setBackgroundColor(CELLCOLOR);
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(organization.getOrgBackground()));
                        cell.setBackgroundColor(CELLCOLOR);
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(descriptionTrn));
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(organization.getOrgDescription()));
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(keyAreasTrn));
                        orgInfoTbl.addCell(cell);
                        cell = new RtfCell(new Paragraph(organization.getOrgKeyAreas()));
                        orgInfoTbl.addCell(cell);
        			}
        			doc.add(orgInfoTbl);
                    doc.add(new Paragraph(" "));
            	}
            	else {
            		if(vForm.getFilter().getSelOrgGroupIds().length == 1 && vForm.getFilter().getSelOrgGroupIds()[0] != -1){
                		long orgGrpId = vForm.getFilter().getSelOrgGroupIds()[0];
                		Table orgGrpInfoTbl = null;
                		orgGrpInfoTbl = new Table(2);
                		orgGrpInfoTbl.setWidth(100);
                		RtfCell orgInfoTitleCell = new RtfCell(new Paragraph(orgGrpInfoTrn, HEADERFONT));
                        orgInfoTitleCell.setColspan(2);
                        orgInfoTitleCell.setBackgroundColor(TITLECOLOR);
                        orgGrpInfoTbl.addCell(orgInfoTitleCell);
            			AmpOrgGroup orgGrp=DbUtil.getOrgGroup(orgGrpId);
            			if(orgGrp!=null){
            				RtfCell addNotesTitleCell = new RtfCell(new Paragraph(addNotesTrn, HEADERFONT));
            				addNotesTitleCell.setColspan(2);
            				addNotesTitleCell.setBackgroundColor(TITLECOLOR);
            				orgGrpInfoTbl.addCell(addNotesTitleCell);
                            cell = new RtfCell(new Paragraph(backOrgGrpTrn));
                            cell.setBackgroundColor(CELLCOLOR);
                            orgGrpInfoTbl.addCell(cell);
                            cell = new RtfCell(new Paragraph(orgGrp.getOrgGrpBackground()));
                            cell.setBackgroundColor(CELLCOLOR);
                            orgGrpInfoTbl.addCell(cell);
                            cell = new RtfCell(new Paragraph(descriptionTrn));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new RtfCell(new Paragraph(orgGrp.getOrgGrpDescription()));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new RtfCell(new Paragraph(keyAreasTrn));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new RtfCell(new Paragraph(orgGrp.getOrgGrpKeyAreas()));
                            orgGrpInfoTbl.addCell(cell);
            			}
            			doc.add(orgGrpInfoTbl);
                        doc.add(new Paragraph(" "));
           			
            		}
            		
            	}
            }
            
          //Filters.
            Table filtersTbl = null;
            filtersTbl = new Table(1);
            filtersTbl.setWidth(100);
            RtfCell filterTitleCell = new RtfCell(new Paragraph(filtersTrn, HEADERFONTWHITE));
            filterTitleCell.setColspan(1);
            filterTitleCell.setBackgroundColor(TITLECOLOR);
            filtersTbl.addCell(filterTitleCell);
            
            cell = new RtfCell(new Paragraph(filtersAmountsInTrn));
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph(filtersCurrencyTypeTrn + ": " + currName));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph(filtersStartYearTrn + ": " + vForm.getFilter().getStartYear()));
            filtersTbl.addCell(cell);
            cell = new RtfCell(new Paragraph(filtersEndYearTrn + ": " + vForm.getFilter().getEndYear()));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            String itemList = "";
            
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new RtfCell(new Paragraph(filtersOrgGroupTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] orgIds = vForm.getFilter().getSelOrgIds();
            if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
				for (int i = 0; i < orgIds.length; i++) {
					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new RtfCell(new Paragraph(filtersOrganizationsTrn + ": " + itemList));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
				for (int i = 0; i < sectorIds.length; i++) {
					itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new RtfCell(new Paragraph(filtersSectorsTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] locationIds = vForm.getFilter().getSelLocationIds();
            if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
				for (int i = 0; i < locationIds.length; i++) {
					itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new RtfCell(new Paragraph(filtersLocationsTrn + ": " + itemList));
            cell.setBackgroundColor(CELLCOLOR);
            filtersTbl.addCell(cell);
            
            itemList = "";
            Long[] statusIds = vForm.getFilter().getSelStatusIds();
            if (statusIds != null && statusIds.length != 0 && statusIds[0]!=-1) {
				for (int i = 0; i < statusIds.length; i++) {
					itemList = itemList + CategoryManagerUtil.getAmpCategoryValueFromDb(statusIds[i]).getValue() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new RtfCell(new Paragraph(filtersStatusTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            
		    doc.add(filtersTbl);
            doc.add(new Paragraph(" "));
            
            String amountDescription = " (" + fundTypeTrn + " - " + filtersMagnitudeTrn + " " + currName + ")";
            
          //Summary table.
            if (summaryOpt.equals("1")) {
				Table summaryTbl = new Table(7);
				summaryTbl.setBorder(1);
				summaryTbl.setBorderWidth(5);
				summaryTbl.setBorderColor(Color.RED);
	            summaryTbl.setWidth(100);
	            RtfCell sumamaryTitleCell = new RtfCell(new Paragraph(summaryTrn + amountDescription, HEADERFONTWHITE));
	            sumamaryTitleCell.setColspan(7);
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
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(numberDonTrn, HEADERFONTWHITE));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	cell.setBackgroundColor(TITLECOLOR);
		            summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new RtfCell(new Paragraph(numberRegTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new RtfCell(new Paragraph(numberSecTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            cell = new RtfCell(new Paragraph(avgPrjZSizeTrn, HEADERFONTWHITE));
	            cell.setBackgroundColor(TITLECOLOR);
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            
	            cell = new RtfCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalCommitments()), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalDisbursements())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfOrganizations().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new RtfCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            cell = new RtfCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getAverageProjectSize())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
	        
            Paragraph subTitle = null;
          //Top projects table.
            if (vForm.getFilter().getShowProjectsRanking()==null || vForm.getFilter().getShowProjectsRanking()){
           	 	subTitle = new Paragraph(topPrjTrn + amountDescription, SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
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
					    cell = new RtfCell(new Paragraph(getFormattedNumber((BigDecimal)entry.getValue())));
					    if (count % 2 == 0)
				        	cell.setBackgroundColor(CELLCOLOR);
					    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					    topPrjTbl.addCell(cell);
					    count++;
				    }
				    doc.add(topPrjTbl);
		            doc.add(new Paragraph(" "));
	            }
            }
            List<AmpDashboardGraph> graphs = DbUtil.getDashboardGraphByDashboard(vForm.getDashboard().getId());
            for (Iterator iterator = graphs.iterator(); iterator.hasNext();) {
				AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
				AmpGraph ampGraph = ampDashboardGraph.getGraph();
				if (ampGraph.getContainerId().equals("Fundings"))
					getFundingTable(fundingOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("AidPredictability"))
					getAidPredictabilityTable(aidPredicOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("AidType"))
					getAidTypeTable(aidTypeOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("AidModality"))
					getAidModalityTable(financingInstOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("SectorProfile"))
					getSectorProfileTable(sectorOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("RegionProfile"))
					getRegionProfileTable(regionOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("OrganizationProfile"))
					getOrganizationProfileTable(organizationOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("ODAGrowth"))
					getODAGrowthTable(ODAGrowthOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("NPOProfile"))
					getNPOProfileTable(NPOOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("ProgramProfile"))
					getProgramProfileTable(programOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("SecondaryProgramProfile"))
					getSecondaryProgramProfileTable(secProgramOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("AidPredictabilityQuarter"))
					getAidPredictabilityQuarterTable(aidPredicQuarterOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("BudgetBreakdown"))
					getBudgetBreakdownTable(budgetBreakdownOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("BeneficiaryAgencyProfile"))
					getBeneficiaryAgencyProfileTable(beneficiaryAgencyOpt, doc, vForm, request, amountDescription);
				if (ampGraph.getContainerId().equals("ResponsibleOrganization"))
					getResponsibleOrganizationTable(responsibleOrganizationOpt, doc, vForm, request, amountDescription);
				
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
    public String getFormattedNumber(BigDecimal number){
   	 	return FormatHelper.formatNumberNotRounded(number.doubleValue());
    }
    
    private void getFundingTable(String fundingOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Funding Table.
        if (!fundingOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(fundingTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (fundingOpt.equals("1") || fundingOpt.equals("3")){
            Table fundingTbl = null;
            if(vForm.getExportData().getFundingTableData() != null) {
	            String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	            if (fundingRows.length>1){
		            int colspan = (fundingRows[1].split(">").length + 1)/2; 
		            fundingTbl = new Table(colspan);
		            fundingTbl.setWidth(100);
		            RtfCell cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setBackgroundColor(TITLECOLOR);
		            fundingTbl.addCell(cell);
		            String[] singleRow = fundingRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i=i+2) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setBackgroundColor(TITLECOLOR);
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            fundingTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 1; i < fundingRows.length; i++) {
		            	singleRow = fundingRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j=j+2) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		            		if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    fundingTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(fundingTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (fundingOpt.equals("2") || fundingOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable(); //col,row 
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getFundingGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidPredictabilityTable(String aidPredicOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Aid Predictability Table.
		if (!aidPredicOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(aidPredTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
            Table aidPredTbl = null;
            if(vForm.getExportData().getAidPredicTableData() != null) {
	            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
	            if (aidPredRows.length>1){
		            int colspan = (aidPredRows[1].split(">").length + 1)/2; 
		            aidPredTbl = new Table(colspan);
		            aidPredTbl.setWidth(100);
		            //RtfCell aidPredTitleCell = new RtfCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //aidPredTitleCell.setColspan(colspan);
		            //aidPredTitleCell.setBackgroundColor(TITLECOLOR);
		            //aidPredTbl.addCell(aidPredTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
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
		            int count = 0;
		            for (int i = 1; i < aidPredRows.length; i++) {
		            	String[] singleRow = aidPredRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j=j+2) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		            		if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    aidPredTbl.addCell(cell);
		    			}
		            	count++;
		            }
	            	doc.add(aidPredTbl);	            
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getAidPredictabilityGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidTypeTable(String aidTypeOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Aid Type Table.
		if (!aidTypeOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(aidTypeTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
            Table aidTypeTbl = null;
            if(vForm.getExportData().getAidTypeTableData() != null) {
	            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
	            if (aidTypeRows.length>1){
		            int colspan = (aidTypeRows[1].split(">").length + 1)/2; 
		            aidTypeTbl = new Table(colspan);
		            aidTypeTbl.setWidth(100);
		            //RtfCell aidTypeTitleCell = new RtfCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //aidTypeTitleCell.setColspan(colspan);
		            //aidTypeTitleCell.setBackgroundColor(TITLECOLOR);
		            //aidTypeTbl.addCell(aidTypeTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            aidTypeTbl.addCell(cell);
		            String[] singleRow = aidTypeRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i=i+2) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
			            aidTypeTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 1; i < aidTypeRows.length; i++) {
		            	singleRow = aidTypeRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j=j+2) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    aidTypeTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(aidTypeTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getAidTypeGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidModalityTable(String financingInstOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Financing Instrument Table.
	    if (!financingInstOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(finInstTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
            Table finInstTbl = null;
            if(vForm.getExportData().getFinancingInstTableData() != null) {
	            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
	            if (finInstRows.length>1){
		            int colspan = (finInstRows[1].split(">").length + 1)/2; 
		            finInstTbl = new Table(colspan);
		            finInstTbl.setWidth(100);
		            //RtfCell finInstTitleCell = new RtfCell(new Paragraph(finInstTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //finInstTitleCell.setColspan(colspan);
		            //finInstTitleCell.setBackgroundColor(TITLECOLOR);
		            //finInstTbl.addCell(finInstTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            finInstTbl.addCell(cell);
		            String[] singleRow = finInstRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i=i+2) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
			            finInstTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 1; i < finInstRows.length; i++) {
		            	singleRow = finInstRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j=j+2) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    finInstTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(finInstTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getFinancingInstGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getSectorProfileTable(String sectorOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Sector Profile Table.
        if (!sectorOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(sectorProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (sectorOpt.equals("1") || sectorOpt.equals("3")){
        	if(vForm.getExportData().getSectorTableData() != null) {
	            Table sectorProfTbl = null;
	            if(vForm.getExportData().getSectorTableData() != null) {
		            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
		            if (sectorProfRows.length>1){
			            int colspan = sectorProfRows[1].split(">").length; 
			            sectorProfTbl = new Table(colspan);
			            sectorProfTbl.setWidth(100);
			            //RtfCell sectorProfTitleCell = new RtfCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONTWHITE));
			            //sectorProfTitleCell.setColspan(colspan);
			            //sectorProfTitleCell.setBackgroundColor(TITLECOLOR);
			            //sectorProfTbl.addCell(sectorProfTitleCell);
			            RtfCell cell = new RtfCell(new Paragraph(sectorTrn, HEADERFONTWHITE));
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setBackgroundColor(TITLECOLOR);
			            sectorProfTbl.addCell(cell);
			            String[] singleRow = sectorProfRows[1].split(">");
			            for (int i = 1; i < singleRow.length; i++) {
			            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
			            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            	cell.setBackgroundColor(TITLECOLOR);
				            sectorProfTbl.addCell(cell);
						}
			            int count = 0;
			            for (int i = 2; i < sectorProfRows.length; i++) {
			            	singleRow = sectorProfRows[i].split(">");
			            	for (int j = 0; j < singleRow.length; j++) {
			                	if(j > 0) {
				                	BigDecimal bd = new BigDecimal(singleRow[j]);
			                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
			                	}
			                	else
			                		cell = new RtfCell(new Paragraph(singleRow[j]));
			                	if (count % 2 == 0)
			    		        	cell.setBackgroundColor(CELLCOLOR);
			                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			    			    sectorProfTbl.addCell(cell);
			    			}
			            	count++;
						}
			            doc.add(sectorProfTbl);
		            }
		            doc.add(new Paragraph(" "));
	            }
        	}
        }
        if (sectorOpt.equals("2") || sectorOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getSectorGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getRegionProfileTable(String regionOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Region Profile Table.
        if (!regionOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(regionProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (regionOpt.equals("1") || regionOpt.equals("3")){
            Table regionProfTbl = null;
            if(vForm.getExportData().getRegionTableData() != null) {
	            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
	            if (regionProfRows.length>1){
		            int colspan = regionProfRows[1].split(">").length; 
		            regionProfTbl = new Table(colspan);
		            regionProfTbl.setWidth(100);
		            //RtfCell regionProfTitleCell = new RtfCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //regionProfTitleCell.setColspan(colspan);
		            //regionProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //regionProfTbl.addCell(regionProfTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(regionTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            regionProfTbl.addCell(cell);
		            String[] singleRow = regionProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
			            regionProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < regionProfRows.length; i++) {
		            	singleRow = regionProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    			    regionProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(regionProfTbl);
	            }
	            doc.add(new Paragraph(" "));
        	}
        }
        if (regionOpt.equals("2") || regionOpt.equals("3")){
            SimpleTable fundingGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getRegionGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            fundingGraph.setWidthpercentage(100);
            fundingGraph.addElement(row);
            doc.add(fundingGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getOrganizationProfileTable(String organizationOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Organization Profile Table.
        if (!organizationOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(organizationProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (organizationOpt.equals("1") || organizationOpt.equals("3")){
            Table organizationProfTbl = null;
            if(vForm.getExportData().getOrganizationTableData() != null) {
	            String[] organizationProfRows = vForm.getExportData().getOrganizationTableData().split("<");
	            if (organizationProfRows.length>1){
		            int colspan = organizationProfRows[1].split(">").length; 
		            organizationProfTbl = new Table(colspan);
		            organizationProfTbl.setWidth(100);
		            //RtfCell organizationProfTitleCell = new RtfCell(new Paragraph(organizationProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //organizationProfTitleCell.setColspan(colspan);
		            //organizationProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //organizationProfTbl.addCell(organizationProfTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(organizationTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            organizationProfTbl.addCell(cell);
		            String[] singleRow = organizationProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	organizationProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < organizationProfRows.length; i++) {
		            	singleRow = organizationProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	organizationProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(organizationProfTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (organizationOpt.equals("2") || organizationOpt.equals("3")){
            SimpleTable organizationGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getOrganizationGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            organizationGraph.setWidthpercentage(100);
            organizationGraph.addElement(row);
            doc.add(organizationGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getODAGrowthTable(String ODAGrowthOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//ODA Growth 
    	RtfCell cell=null;
    	if (!ODAGrowthOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(ODAGrowthTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
    	if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
            Table ODAGrowthTbl = null;
            if(vForm.getExportData().getODAGrowthTableData() != null) {
	            String[] ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");
	            if (ODAGrowthRows.length>1){
		            int colspan = (ODAGrowthRows[1].split(">").length); 
		            ODAGrowthTbl = new Table(colspan);
		            ODAGrowthTbl.setWidth(100);
		            String[] singleRow = ODAGrowthRows[1].split(">");
		            for (int i = 0; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setBackgroundColor(TITLECOLOR);
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	ODAGrowthTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < ODAGrowthRows.length; i++) {
		            	singleRow = ODAGrowthRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	ODAGrowthTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(ODAGrowthTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
    	if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
        	SimpleTable ODAGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getODAGrowthGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            ODAGraph.setWidthpercentage(100);
            ODAGraph.addElement(row);
            doc.add(ODAGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getNPOProfileTable(String NPOOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//NPO Profile Table.
        if (!NPOOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(NPOProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (NPOOpt.equals("1") || NPOOpt.equals("3")){
            Table NPOProfTbl = null;
            if(vForm.getExportData().getNPOTableData() != null) {
	            String[] NPOProfRows = vForm.getExportData().getNPOTableData().split("<");
	            if (NPOProfRows.length>1){
		            int colspan = NPOProfRows[1].split(">").length; 
		            NPOProfTbl = new Table(colspan);
		            NPOProfTbl.setWidth(100);
		            RtfCell cell = new RtfCell(new Paragraph(NPOTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            NPOProfTbl.addCell(cell);
		            String[] singleRow = NPOProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	NPOProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < NPOProfRows.length; i++) {
		            	singleRow = NPOProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	NPOProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(NPOProfTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (NPOOpt.equals("2") || NPOOpt.equals("3")){
            SimpleTable NPOGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getNPOGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            NPOGraph.setWidthpercentage(100);
            NPOGraph.addElement(row);
            doc.add(NPOGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getProgramProfileTable(String programOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Program Profile Table.
        if (!programOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(programProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (programOpt.equals("1") || programOpt.equals("3")){
            Table programProfTbl = null;
            if(vForm.getExportData().getProgramTableData() != null) {
	            String[] programProfRows = vForm.getExportData().getProgramTableData().split("<");
	            if (programProfRows.length>1){
		            int colspan = programProfRows[1].split(">").length; 
		            programProfTbl = new Table(colspan);
		            programProfTbl.setWidth(100);
		            RtfCell cell = new RtfCell(new Paragraph(programTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            programProfTbl.addCell(cell);
		            String[] singleRow = programProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	programProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < programProfRows.length; i++) {
		            	singleRow = programProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	programProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(programProfTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (programOpt.equals("2") || programOpt.equals("3")){
            SimpleTable programGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getProgramGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            programGraph.setWidthpercentage(100);
            programGraph.addElement(row);
            doc.add(programGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getSecondaryProgramProfileTable(String programOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Program Profile Table.
        if (!programOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(secProgramProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (programOpt.equals("1") || programOpt.equals("3")){
            Table programProfTbl = null;
            if(vForm.getExportData().getSecondaryProgramTableData() != null) {
	            String[] programProfRows = vForm.getExportData().getSecondaryProgramTableData().split("<");
	            if (programProfRows.length>1){
		            int colspan = programProfRows[1].split(">").length; 
		            programProfTbl = new Table(colspan);
		            programProfTbl.setWidth(100);
		            RtfCell cell = new RtfCell(new Paragraph(programTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            programProfTbl.addCell(cell);
		            String[] singleRow = programProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	programProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < programProfRows.length; i++) {
		            	singleRow = programProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	programProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(programProfTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (programOpt.equals("2") || programOpt.equals("3")){
            SimpleTable programGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getSecondaryProgramGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            programGraph.setWidthpercentage(100);
            programGraph.addElement(row);
            doc.add(programGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidPredictabilityQuarterTable(String aidPredicQuarterOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Aid Predictability Quarterly Table.
	       if (!aidPredicQuarterOpt.equals("0")){
         	doc.newPage();
         	Paragraph subTitle = new Paragraph(aidPredQuarterTrn + amountDesc, SUBTITLEFONT);
             subTitle.setAlignment(Element.ALIGN_LEFT);
             doc.add(subTitle);
         }
         if (aidPredicQuarterOpt.equals("1") || aidPredicQuarterOpt.equals("3")){
	            Table aidPredQuarterTbl = null;
	            if(vForm.getExportData().getAidPredicQuarterTableData() != null) {
		            String[] aidPredQuarterRows = vForm.getExportData().getAidPredicQuarterTableData().split("<");
		            if (aidPredQuarterRows.length>1){
			            int colspan = (aidPredQuarterRows[1].split(">").length + 1)/2; 
			            aidPredQuarterTbl = new Table(colspan);
			            aidPredQuarterTbl.setWidth(100);
			            //RtfCell aidPredTitleCell = new RtfCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONTWHITE));
			            //aidPredTitleCell.setColspan(colspan);
			            //aidPredTitleCell.setBackgroundColor(TITLECOLOR);
			            //aidPredTbl.addCell(aidPredTitleCell);
			            RtfCell cell = new RtfCell(new Paragraph(quarterTrn, HEADERFONTWHITE));
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setBackgroundColor(TITLECOLOR);
			            aidPredQuarterTbl.addCell(cell);
			            cell = new RtfCell(new Paragraph(plannedTrn, HEADERFONTWHITE));
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setBackgroundColor(TITLECOLOR);
			            aidPredQuarterTbl.addCell(cell);
			            cell = new RtfCell(new Paragraph(actualTrn, HEADERFONTWHITE));
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setBackgroundColor(TITLECOLOR);
			            aidPredQuarterTbl.addCell(cell);
			            int count = 0;
			            for (int i = 1; i < aidPredQuarterRows.length; i++) {
			            	String[] singleRow = aidPredQuarterRows[i].split(">");
			            	for (int j = 0; j < singleRow.length; j=j+2) {
			                	if(j > 0) {
				                	BigDecimal bd = new BigDecimal(singleRow[j]);
			                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
			                	}
			                	else
			                		cell = new RtfCell(new Paragraph(singleRow[j]));
			            		if (count % 2 == 0)
			    		        	cell.setBackgroundColor(CELLCOLOR);
			            		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			            		aidPredQuarterTbl.addCell(cell);
			    			}
			            	count++;
						}
			            doc.add(aidPredQuarterTbl);
		            }
		            doc.add(new Paragraph(" "));
	            }
         }
         if (aidPredicQuarterOpt.equals("2") || aidPredicQuarterOpt.equals("3")){
	         SimpleTable fundingGraph = new SimpleTable();
             SimpleCell row = new SimpleCell(SimpleCell.ROW);
             SimpleCell cel = new SimpleCell(SimpleCell.CELL);
             //cel.setBorder(1);
             ByteArrayOutputStream ba = new ByteArrayOutputStream();
             ImageIO.write(scaleImage(vForm.getExportData().getAidPredictabilityQuarterGraph(),580,410), "png", ba);
             Image img = Image.getInstance(ba.toByteArray());
             cel.add(img);
             row.add(cel);
             fundingGraph.setWidthpercentage(100);
             fundingGraph.addElement(row);
             doc.add(fundingGraph);
	         doc.add(new Paragraph(" "));
         }
    }
    
    private void getBudgetBreakdownTable(String budgetBreakdownOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Budget Breakdown Table.
		if (!budgetBreakdownOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(budgetBreakdownTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (budgetBreakdownOpt.equals("1") || budgetBreakdownOpt.equals("3")){
            Table budgetBreakdownTbl = null;
            if(vForm.getExportData().getBudgetTableData() != null) {
	            String[] budgetBreakdownRows = vForm.getExportData().getBudgetTableData().split("<");
	            if (budgetBreakdownRows.length>1){
		            int colspan = (budgetBreakdownRows[1].split(">").length + 1)/2; 
		            budgetBreakdownTbl = new Table(colspan);
		            budgetBreakdownTbl.setWidth(100);
		            //RtfCell aidTypeTitleCell = new RtfCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //aidTypeTitleCell.setColspan(colspan);
		            //aidTypeTitleCell.setBackgroundColor(TITLECOLOR);
		            //aidTypeTbl.addCell(aidTypeTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(yearTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            budgetBreakdownTbl.addCell(cell);
		            String[] singleRow = budgetBreakdownRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i=i+2) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	budgetBreakdownTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 1; i < budgetBreakdownRows.length; i++) {
		            	singleRow = budgetBreakdownRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j=j+2) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	budgetBreakdownTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(budgetBreakdownTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (budgetBreakdownOpt.equals("2") || budgetBreakdownOpt.equals("3")){
            SimpleTable budgetBreakdownGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getBudgetGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            budgetBreakdownGraph.setWidthpercentage(100);
            budgetBreakdownGraph.addElement(row);
            doc.add(budgetBreakdownGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getBeneficiaryAgencyProfileTable(String beneficiaryAgencyOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Beneficiary Agency Profile Table.
        if (!beneficiaryAgencyOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(beneficiaryAgencyProfTrn + amountDesc, SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
        }
        if (beneficiaryAgencyOpt.equals("1") || beneficiaryAgencyOpt.equals("3")){
            Table organizationProfTbl = null;
            if(vForm.getExportData().getBeneficiaryAgencyTableData() != null) {
	            String[] organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
	            if (organizationProfRows.length>1){
		            int colspan = organizationProfRows[1].split(">").length; 
		            organizationProfTbl = new Table(colspan);
		            organizationProfTbl.setWidth(100);
		            //RtfCell organizationProfTitleCell = new RtfCell(new Paragraph(organizationProfTrn + " (" + currName + ")", HEADERFONTWHITE));
		            //organizationProfTitleCell.setColspan(colspan);
		            //organizationProfTitleCell.setBackgroundColor(TITLECOLOR);
		            //organizationProfTbl.addCell(organizationProfTitleCell);
		            RtfCell cell = new RtfCell(new Paragraph(organizationTrn, HEADERFONTWHITE));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setBackgroundColor(TITLECOLOR);
		            organizationProfTbl.addCell(cell);
		            String[] singleRow = organizationProfRows[1].split(">");
		            for (int i = 1; i < singleRow.length; i++) {
		            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	cell.setBackgroundColor(TITLECOLOR);
		            	organizationProfTbl.addCell(cell);
					}
		            int count = 0;
		            for (int i = 2; i < organizationProfRows.length; i++) {
		            	singleRow = organizationProfRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) {
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new RtfCell(new Paragraph(singleRow[j]));
		                	if (count % 2 == 0)
		    		        	cell.setBackgroundColor(CELLCOLOR);
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	organizationProfTbl.addCell(cell);
		    			}
		            	count++;
					}
		            doc.add(organizationProfTbl);
	            }
	            doc.add(new Paragraph(" "));
            }
        }
        if (beneficiaryAgencyOpt.equals("2") || beneficiaryAgencyOpt.equals("3")){
            SimpleTable organizationGraph = new SimpleTable();
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
            //cel.setBorder(1);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(scaleImage(vForm.getExportData().getBeneficiaryAgencyGraph(),580,410), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            cel.add(img);
            row.add(cel);
            organizationGraph.setWidthpercentage(100);
            organizationGraph.addElement(row);
            doc.add(organizationGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getResponsibleOrganizationTable(String responsibleOrganizationOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Beneficiary Agency Profile Table.
    	if(responsibleOrganizationOpt != null) {
	        if (!responsibleOrganizationOpt.equals("0")){
	        	doc.newPage();
	        	Paragraph subTitle = new Paragraph(responsibleOrganizationProfTrn + amountDesc, SUBTITLEFONT);
	            subTitle.setAlignment(Element.ALIGN_LEFT);
	            doc.add(subTitle);
	        }
	        if (responsibleOrganizationOpt.equals("1") || responsibleOrganizationOpt.equals("3")){
	            Table organizationProfTbl = null;
	            if(vForm.getExportData().getBeneficiaryAgencyTableData() != null) {
		            String[] organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
		            if (organizationProfRows.length>1){
			            int colspan = organizationProfRows[1].split(">").length; 
			            organizationProfTbl = new Table(colspan);
			            organizationProfTbl.setWidth(100);
			            //RtfCell organizationProfTitleCell = new RtfCell(new Paragraph(organizationProfTrn + " (" + currName + ")", HEADERFONTWHITE));
			            //organizationProfTitleCell.setColspan(colspan);
			            //organizationProfTitleCell.setBackgroundColor(TITLECOLOR);
			            //organizationProfTbl.addCell(organizationProfTitleCell);
			            RtfCell cell = new RtfCell(new Paragraph(organizationTrn, HEADERFONTWHITE));
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setBackgroundColor(TITLECOLOR);
			            organizationProfTbl.addCell(cell);
			            String[] singleRow = organizationProfRows[1].split(">");
			            for (int i = 1; i < singleRow.length; i++) {
			            	cell = new RtfCell(new Paragraph(singleRow[i], HEADERFONTWHITE));
			            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            	cell.setBackgroundColor(TITLECOLOR);
			            	organizationProfTbl.addCell(cell);
						}
			            int count = 0;
			            for (int i = 2; i < organizationProfRows.length; i++) {
			            	singleRow = organizationProfRows[i].split(">");
			            	for (int j = 0; j < singleRow.length; j++) {
			                	if(j > 0) {
				                	BigDecimal bd = new BigDecimal(singleRow[j]);
			                		cell = new RtfCell(new Paragraph(getFormattedNumber(bd)));
			                	}
			                	else
			                		cell = new RtfCell(new Paragraph(singleRow[j]));
			                	if (count % 2 == 0)
			    		        	cell.setBackgroundColor(CELLCOLOR);
			                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			                	organizationProfTbl.addCell(cell);
			    			}
			            	count++;
						}
			            doc.add(organizationProfTbl);
		            }
		            doc.add(new Paragraph(" "));
	            }
	        }
	        if (responsibleOrganizationOpt.equals("2") || responsibleOrganizationOpt.equals("3")){
	            SimpleTable organizationGraph = new SimpleTable();
	            SimpleCell row = new SimpleCell(SimpleCell.ROW);
	            SimpleCell cel = new SimpleCell(SimpleCell.CELL);
	            //cel.setBorder(1);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(scaleImage(vForm.getExportData().getBeneficiaryAgencyGraph(),580,410), "png", ba);
	            Image img = Image.getInstance(ba.toByteArray());
	            cel.add(img);
	            row.add(cel);
	            organizationGraph.setWidthpercentage(100);
	            organizationGraph.addElement(row);
	            doc.add(organizationGraph);
	            doc.add(new Paragraph(" "));
	        }
	    }
    }
}
