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

import static org.digijava.module.aim.action.ExportActivityToPDF.postprocessText;
import static org.digijava.module.aim.action.ExportActivityToPDF.basefont;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
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

import com.lowagie.text.DocumentException;
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
    public static final Font PLAINFONT = new Font(basefont, 10);
    public static final Font HEADERFONT = new Font(basefont, 12, Font.BOLD);
    public static final Font TITLEFONT = new Font(basefont, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(basefont, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(basefont, 12, Font.BOLD, Color.WHITE);

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
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/download");
        response.setHeader("content-disposition", "attachment;filename=dashboard.pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        VisualizationForm vForm = (VisualizationForm) form;
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
			String filtersSubSectorsTrn = TranslatorWorker.translateText("Sub-Sectors");
			String filtersRegionsTrn = TranslatorWorker.translateText("Regions");
			String filtersZonesTrn = TranslatorWorker.translateText("Zones");
			String filtersStatusTrn = TranslatorWorker.translateText("Status");
			String filtersLocationsTrn = TranslatorWorker.translateText("Locations");
			this.fundingTrn = TranslatorWorker.translateText("ODA Historical Trend");
			this.ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth");
			this.topPrjTrn = TranslatorWorker.translateText("Top Projects");
			this.topOrganizationTrn = TranslatorWorker.translateText("Top Organizations");
			this.topRegionTrn = TranslatorWorker.translateText("Top Regions");
			this.projectTrn = TranslatorWorker.translateText("Project");
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
	        this.responsibleOrganizationProfTrn = TranslatorWorker.translateText("Responsible Organization Profile");
	        this.plannedTrn = TranslatorWorker.translateText("Planned");
            this.actualTrn = TranslatorWorker.translateText("Actual");
            this.yearTrn = TranslatorWorker.translateText("Year");
            String dashboardTrn = TranslatorWorker.translateText("Dashboard");
            String summaryTrn = TranslatorWorker.translateText("Summary");
            String totalCommsTrn = TranslatorWorker.translateText("Total Commitments");
            String totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements");
            String numberPrjTrn = TranslatorWorker.translateText("Number of Projects");
            String numberSecTrn = TranslatorWorker.translateText("Number of Sectors");
            String numberDonTrn = TranslatorWorker.translateText("Number of Organizations");
            String numberRegTrn = TranslatorWorker.translateText("Number of Regions");
            String avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size");
            String currName = vForm.getFilter().getCurrencyCode();
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
        
        	PdfWriter pdfWriter =PdfWriter.getInstance(doc, baos);
            HttpSession session = request.getSession();
            String footerText = pageTrn + " - ";
            doc.setPageCount(0);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), true);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.open();
            Paragraph pageTitle;
            if(vForm.getDashboard()!=null){
            	pageTitle = new Paragraph(postprocessText(vForm.getDashboard().getName()).toUpperCase(), TITLEFONT);
                	
            } else {
            	pageTitle = new Paragraph(postprocessText(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn).toUpperCase(), TITLEFONT);
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
            	Paragraph pageSubTitle = new Paragraph(postprocessText(itemList), SUBTITLEFONT);
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
                pageSubTitle = new Paragraph(postprocessText(itemList), SUBTITLEFONT);
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
            	Paragraph pageSubTitle = new Paragraph(postprocessText(itemList), SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(postprocessText(itemList2), SUBTITLEFONT);
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
            	
                Paragraph pageSubTitle = new Paragraph(postprocessText(itemList), SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(postprocessText(itemList2), SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            PdfPCell cell = null;
            List list = null;
            int colspan = 0;
            Image img = null;
            String[] singleRow = null;
            
            //Org. Information
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	if (vForm.getFilter().getSelOrgIds().length==1 && vForm.getFilter().getSelOrgIds()[0] != -1){
            		long orgId = vForm.getFilter().getSelOrgIds()[0];
            		PdfPTable orgInfoTbl = null;
            		orgInfoTbl = new PdfPTable(2);
            		orgInfoTbl.setWidthPercentage(100);
                    PdfPCell orgInfoTitleCell = new PdfPCell(new Paragraph(postprocessText(orgInfoTrn), HEADERFONT));
                    orgInfoTitleCell.setColspan(2);
                    orgInfoTbl.addCell(orgInfoTitleCell);
            		AmpContact contact=DbUtil.getPrimaryContactForOrganization(orgId);
        			if(contact!=null){
        				PdfPCell contInfoTitleCell = new PdfPCell(new Paragraph(postprocessText(contactInfoTrn), HEADERFONT));
        				contInfoTitleCell.setColspan(2);
                        orgInfoTbl.addCell(contInfoTitleCell);
                        cell = new PdfPCell(new Paragraph(postprocessText(titleTrn)));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(contact.getTitle()!=null?contact.getTitle().getValue():"")));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(nameTrn)));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(contact.getName()+" "+contact.getLastname())));
                        orgInfoTbl.addCell(cell);
                        if(contact.getProperties()!=null){
            				for (AmpContactProperty property : contact.getProperties()) {
            					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
            						cell = new PdfPCell(new Paragraph(postprocessText(emailsTrn)));
                                    orgInfoTbl.addCell(cell);
                                    cell = new PdfPCell(new Paragraph(postprocessText(property.getValue())));
                                    orgInfoTbl.addCell(cell);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
            						cell = new PdfPCell(new Paragraph(postprocessText(phonesTrn)));
                                    orgInfoTbl.addCell(cell);
                                    cell = new PdfPCell(new Paragraph(postprocessText(property.getValueAsFormatedPhoneNum())));
                                    orgInfoTbl.addCell(cell);
            					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
            						cell = new PdfPCell(new Paragraph(postprocessText(faxesTrn)));
                                    orgInfoTbl.addCell(cell);
                                    cell = new PdfPCell(new Paragraph(postprocessText(property.getValue())));
                                    orgInfoTbl.addCell(cell);
            					}
            				}
            			}
        			}
        			AmpOrganisation organization=DbUtil.getOrganisation(orgId);
        			if(organization!=null){
        				PdfPCell addNotesTitleCell = new PdfPCell(new Paragraph(addNotesTrn, HEADERFONT));
        				addNotesTitleCell.setColspan(2);
                        orgInfoTbl.addCell(addNotesTitleCell);
                        cell = new PdfPCell(new Paragraph(postprocessText(backOrgTrn)));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(organization.getOrgBackground())));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(descriptionTrn)));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(organization.getOrgDescription())));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(keyAreasTrn)));
                        orgInfoTbl.addCell(cell);
                        cell = new PdfPCell(new Paragraph(postprocessText(organization.getOrgKeyAreas())));
                        orgInfoTbl.addCell(cell);
        			}
        		    doc.add(orgInfoTbl);
                    doc.add(new Paragraph(" "));
            	}
            	else {
            		if(vForm.getFilter().getSelOrgGroupIds().length == 1 && vForm.getFilter().getSelOrgGroupIds()[0] != -1){
                		long orgGrpId = vForm.getFilter().getSelOrgGroupIds()[0];
                		PdfPTable orgGrpInfoTbl = null;
                		orgGrpInfoTbl = new PdfPTable(2);
                		orgGrpInfoTbl.setWidthPercentage(100);
                		PdfPCell orgInfoTitleCell = new PdfPCell(new Paragraph(postprocessText(orgGrpInfoTrn), HEADERFONT));
                        orgInfoTitleCell.setColspan(2);
                        orgGrpInfoTbl.addCell(orgInfoTitleCell);
            			AmpOrgGroup orgGrp=DbUtil.getOrgGroup(orgGrpId);
            			if(orgGrp!=null){
            				PdfPCell addNotesTitleCell = new PdfPCell(new Paragraph(postprocessText(addNotesTrn), HEADERFONT));
            				addNotesTitleCell.setColspan(2);
            				orgGrpInfoTbl.addCell(addNotesTitleCell);
                            cell = new PdfPCell(new Paragraph(postprocessText(backOrgGrpTrn)));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new PdfPCell(new Paragraph(postprocessText(orgGrp.getOrgGrpBackground())));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new PdfPCell(new Paragraph(postprocessText(descriptionTrn)));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new PdfPCell(new Paragraph(postprocessText(orgGrp.getOrgGrpDescription())));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new PdfPCell(new Paragraph(postprocessText(keyAreasTrn)));
                            orgGrpInfoTbl.addCell(cell);
                            cell = new PdfPCell(new Paragraph(postprocessText(orgGrp.getOrgGrpKeyAreas())));
                            orgGrpInfoTbl.addCell(cell);
            			}
            			doc.add(orgGrpInfoTbl);
                        doc.add(new Paragraph(" "));
           			
            		}
            		
            	}
            		
            }
            
            //Filters.
            PdfPTable filtersTbl = null;
            filtersTbl = new PdfPTable(1);
            filtersTbl.setWidthPercentage(100);
            PdfPCell filterTitleCell = new PdfPCell(new Paragraph(postprocessText(filtersTrn), HEADERFONT));
            filterTitleCell.setColspan(1);
            filtersTbl.addCell(filterTitleCell);
            
            
            cell = new PdfPCell(new Paragraph(postprocessText(filtersAmountsInTrn)));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(postprocessText(filtersCurrencyTypeTrn + ": " + vForm.getFilter().getCurrencyCode())));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(postprocessText(filtersStartYearTrn + ": " + vForm.getFilter().getStartYear())));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(postprocessText(filtersEndYearTrn + ": " + vForm.getFilter().getEndYear())));
            filtersTbl.addCell(cell);
            String itemList = "";
            Long[] statusIds = vForm.getFilter().getSelStatusIds();
            if (statusIds != null && statusIds.length != 0 && statusIds[0]!=-1) {
				for (int i = 0; i < statusIds.length; i++) {
					itemList = itemList + CategoryManagerUtil.getAmpCategoryValueFromDb(statusIds[i]).getValue() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(postprocessText(filtersStatusTrn + ": " + itemList)));
            filtersTbl.addCell(cell);
            itemList = "";
            
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(postprocessText(filtersOrgGroupTrn + ": " + itemList)));
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
            cell = new PdfPCell(new Paragraph(postprocessText(filtersOrganizationsTrn + ": " + itemList)));
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
            cell = new PdfPCell(new Paragraph(postprocessText(filtersSectorsTrn + ": " + itemList)));
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
            cell = new PdfPCell(new Paragraph(postprocessText(filtersLocationsTrn + ": " + itemList)));
            filtersTbl.addCell(cell);
            
		    doc.add(filtersTbl);
            doc.add(new Paragraph(" "));
            
            String amountsDescription =  " (" + fundTypeTrn + " - " + filtersMagnitudeTrn + " " + currName + ")";
          //Summary table.
            if (summaryOpt.equals("1")) {
				PdfPTable summaryTbl = null;
	            summaryTbl = new PdfPTable(7);
	            summaryTbl.setWidthPercentage(100);
	            PdfPCell sumamaryTitleCell = new PdfPCell(new Paragraph(postprocessText(summaryTrn + amountsDescription), HEADERFONT));
	            sumamaryTitleCell.setColspan(7);
	            summaryTbl.addCell(sumamaryTitleCell);
	            cell = new PdfPCell(new Paragraph(postprocessText(totalCommsTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(totalDisbsTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(numberPrjTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(numberDonTrn), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new PdfPCell(new Paragraph(postprocessText(numberRegTrn), HEADERFONT));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new PdfPCell(new Paragraph(postprocessText(numberSecTrn), HEADERFONT));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            cell = new PdfPCell(new Paragraph(postprocessText(avgPrjZSizeTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalCommitments()), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalDisbursements())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfOrganizations().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getAverageProjectSize())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
           
            Paragraph subTitle = null;
          //Top projects table.
            if (vForm.getFilter().getShowProjectsRanking()==null || vForm.getFilter().getShowProjectsRanking()){
            	 subTitle = new Paragraph(postprocessText(topPrjTrn +  amountsDescription), SUBTITLEFONT);
                 subTitle.setAlignment(Element.ALIGN_LEFT);
                 doc.add(subTitle);
                 doc.add(new Paragraph(" "));
                 PdfPTable topPrjTbl = null;
	            topPrjTbl = new PdfPTable(2);
	            topPrjTbl.setWidthPercentage(100);
	            //PdfPCell topPrjTitleCell = new PdfPCell(new Paragraph(topPrjTrn + " (" + currName + ")", HEADERFONT));
	            //topPrjTitleCell.setColspan(2);
	            //topPrjTbl.addCell(topPrjTitleCell);
	            cell = new PdfPCell(new Paragraph(postprocessText(projectTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            topPrjTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(fundTypeTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            topPrjTbl.addCell(cell);
	            Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
	            if (topProjects!=null){
		            list = new LinkedList(topProjects.entrySet());
				    for (Iterator it = list.iterator(); it.hasNext();) {
				        Map.Entry entry = (Map.Entry)it.next();
				        cell = new PdfPCell(new Paragraph(postprocessText(entry.getKey().toString())));
				        topPrjTbl.addCell(cell);
		            	cell = new PdfPCell(new Paragraph(getFormattedNumber((BigDecimal)entry.getValue())));
		            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		            	topPrjTbl.addCell(cell);
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
					getFundingTable(fundingOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("AidPredictability"))
					getAidPredictabilityTable(aidPredicOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("AidType"))
					getAidTypeTable(aidTypeOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("AidModality"))
					getAidModalityTable(financingInstOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("SectorProfile"))
					getSectorProfileTable(sectorOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("RegionProfile"))
					getRegionProfileTable(regionOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("OrganizationProfile"))
					getOrganizationProfileTable(organizationOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("ODAGrowth"))
					getODAGrowthTable(ODAGrowthOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("NPOProfile"))
					getNPOProfileTable(NPOOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("ProgramProfile"))
					getProgramProfileTable(programOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("SecondaryProgramProfile"))
					getSecondaryProgramProfileTable(secProgramOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("AidPredictabilityQuarter"))
					getAidPredictabilityQuarterTable(aidPredicQuarterOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("BudgetBreakdown"))
					getBudgetBreakdownTable(budgetBreakdownOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("BeneficiaryAgencyProfile"))
					getBeneficiaryAgencyProfileTable(beneficiaryAgencyOpt, doc, vForm, request, amountsDescription);
				if (ampGraph.getContainerId().equals("ResponsibleOrganization"))
					getResponsibleOrganizationTable(responsibleOrganizationOpt, doc, vForm, request, amountsDescription);
				
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
    
    public String getFormattedNumber(BigDecimal number){
    	 return FormatHelper.formatNumberNotRounded(number.doubleValue());
    }
    
  
    private void getFundingTable(String fundingOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Funding Table.
    	if (!fundingOpt.equals("0")){
	    	doc.newPage();
	    	Paragraph subTitle = new Paragraph(postprocessText(fundingTrn + amountDesc), SUBTITLEFONT);
	        subTitle.setAlignment(Element.ALIGN_LEFT);
	        doc.add(subTitle);
	        doc.add(new Paragraph(" "));
	    }
	    if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	        PdfPTable fundingTbl = null;
	        String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	        if (fundingRows.length>1){
		        int colspan = (fundingRows[1].split(">").length + 1)/2; 
		        fundingTbl = new PdfPTable(colspan);
		        fundingTbl.setWidthPercentage(100);
		        PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        fundingTbl.addCell(cell);
		        String[] singleRow = fundingRows[1].split(">");
		        for (int i = 1; i < singleRow.length; i=i+2) {
		        	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
		        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        	fundingTbl.addCell(cell);
				}
		        for (int i = 1; i < fundingRows.length; i++) {
		        	singleRow = fundingRows[i].split(">");
		        	for (int j = 0; j < singleRow.length; j=j+2) {
		            	if(j > 0) {
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
		            		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
		            	}
		            	else
		            		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
		            	
		            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		            	fundingTbl.addCell(cell);
					}
				}
		        doc.add(fundingTbl);
		    }
	        doc.add(new Paragraph(" "));
	    }
	    if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	        PdfPTable fundingGraph = new PdfPTable(1);
	        fundingGraph.setWidthPercentage(100);
	        ByteArrayOutputStream ba = new ByteArrayOutputStream();
	        ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba);
	        Image img = Image.getInstance(ba.toByteArray());
	        fundingGraph.addCell(img);
	        doc.add(fundingGraph);
	        doc.add(new Paragraph(" "));
	    }
    }
    
    private void getAidPredictabilityTable(String aidPredicOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Aid Predictability Table.
		if (!aidPredicOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(aidPredTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
            PdfPTable aidPredTbl = null;
            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
            if (aidPredRows.length>1){
	            int colspan = (aidPredRows[1].split(">").length + 1)/2; 
	            aidPredTbl = new PdfPTable(colspan);
	            aidPredTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(plannedTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(actualTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            for (int i = 1; i < aidPredRows.length; i++) {
	            	String[] singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidPredTbl.addCell(cell);
	    			}
				}
	            doc.add(aidPredTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
            PdfPTable aidPredGraph = new PdfPTable(1);
            aidPredGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            aidPredGraph.addCell(img);
            doc.add(aidPredGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidTypeTable(String aidTypeOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Aid Type Table.
		if (!aidTypeOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(aidTypeTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
            PdfPTable aidTypeTbl = null;
            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
            if (aidTypeRows.length>1){
	            int colspan = (aidTypeRows[1].split(">").length + 1)/2; 
	            aidTypeTbl = new PdfPTable(colspan);
	            aidTypeTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidTypeTbl.addCell(cell);
	            String[] singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	aidTypeTbl.addCell(cell);
				}
	            for (int i = 1; i < aidTypeRows.length; i++) {
	            	singleRow = aidTypeRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidTypeTbl.addCell(cell);
	    			}
				}
	            doc.add(aidTypeTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
            PdfPTable aidTypeGraph = new PdfPTable(1);
            aidTypeGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            aidTypeGraph.addCell(img);
            doc.add(aidTypeGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getAidModalityTable(String financingInstOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Financing Instrument Table.
	    if (!financingInstOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(finInstTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
            PdfPTable finInstTbl = null;
            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
            if (finInstRows.length>1){
	            int colspan = (finInstRows[1].split(">").length + 1)/2; 
	            finInstTbl = new PdfPTable(colspan);
	            finInstTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            finInstTbl.addCell(cell);
	            String[] singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	finInstTbl.addCell(cell);
				}
	            for (int i = 1; i < finInstRows.length; i++) {
	            	singleRow = finInstRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	finInstTbl.addCell(cell);
	    			}
				}
	            doc.add(finInstTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
            PdfPTable finInstGraph = new PdfPTable(1);
            finInstGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            finInstGraph.addCell(img);
            doc.add(finInstGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getSectorProfileTable(String sectorOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Sector Profile Table.
        if (!sectorOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(sectorProfTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (sectorOpt.equals("1") || sectorOpt.equals("3")){
            PdfPTable sectorProfTbl = null;
            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
            if (sectorProfRows.length>1){
	            int colspan = sectorProfRows[1].split(">").length; 
	            sectorProfTbl = new PdfPTable(colspan);
	            sectorProfTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(sectorTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            sectorProfTbl.addCell(cell);
	            String[] singleRow = sectorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                sectorProfTbl.addCell(cell);
				}
	            for (int i = 2; i < sectorProfRows.length; i++) {
	            	singleRow = sectorProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	sectorProfTbl.addCell(cell);
	    			}
				}
	            doc.add(sectorProfTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (sectorOpt.equals("2") || sectorOpt.equals("3")){
            PdfPTable sectorGraph = new PdfPTable(1);
            sectorGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            sectorGraph.addCell(img);
            doc.add(sectorGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getRegionProfileTable(String regionOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Region Profile Table.
        if (!regionOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(regionProfTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (regionOpt.equals("1") || regionOpt.equals("3")){
            PdfPTable regionProfTbl = null;
            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
            if (regionProfRows.length>1){
	            int colspan = regionProfRows[1].split(">").length; 
	            regionProfTbl = new PdfPTable(colspan);
	            regionProfTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(regionTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            regionProfTbl.addCell(cell);
	            String[] singleRow = regionProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	regionProfTbl.addCell(cell);
				}
	            
	            for (int i = 2; i < regionProfRows.length; i++) {
	            	singleRow = regionProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	regionProfTbl.addCell(cell);
	    			}
				}
	            doc.add(regionProfTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (regionOpt.equals("2") || regionOpt.equals("3")){
            PdfPTable regionGraph = new PdfPTable(1);
            regionGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            regionGraph.addCell(img);
            doc.add(regionGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getOrganizationProfileTable(String organizationOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Organization Profile Table.
        if (!organizationOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(organizationProfTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (organizationOpt.equals("1") || organizationOpt.equals("3")){
            PdfPTable organizationProfTbl = null;
            String[] organizationProfRows = vForm.getExportData().getOrganizationTableData().split("<");
            if (organizationProfRows.length>1){
	            int colspan = organizationProfRows[1].split(">").length; 
	            organizationProfTbl = new PdfPTable(colspan);
	            organizationProfTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(organizationTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            organizationProfTbl.addCell(cell);
	            String[] singleRow = organizationProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	organizationProfTbl.addCell(cell);
				}
	            for (int i = 2; i < organizationProfRows.length; i++) {
	            	singleRow = organizationProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	organizationProfTbl.addCell(cell);
	    			}
				}
	            doc.add(organizationProfTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (organizationOpt.equals("2") || organizationOpt.equals("3")){
            PdfPTable organizationGraph = new PdfPTable(1);
            organizationGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getOrganizationGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            organizationGraph.addCell(img);
            doc.add(organizationGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getODAGrowthTable(String ODAGrowthOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{

	if (!ODAGrowthOpt.equals("0")){
	  		doc.newPage();
	  		Paragraph subTitle = new Paragraph(postprocessText(ODAGrowthTrn + amountDesc), SUBTITLEFONT);
	  		subTitle.setAlignment(Element.ALIGN_LEFT);
	  		doc.add(subTitle);
	  		doc.add(new Paragraph(" "));
	 }
      	if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
	            PdfPTable ODAGrowthTbl = null;
	            PdfPCell cell = null;
	            String[] ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");
	            if (ODAGrowthRows.length>1){
		            int colspan = (ODAGrowthRows[1].split(">").length); 
		            ODAGrowthTbl = new PdfPTable(colspan);
		            ODAGrowthTbl.setWidthPercentage(100);
		            String[] singleRow = ODAGrowthRows[1].split(">");
		            for (int i = 0; i < singleRow.length; i++) {
		            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
		            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	ODAGrowthTbl.addCell(cell);
					}
		            for (int i = 2; i < ODAGrowthRows.length; i++) {
		            	singleRow = ODAGrowthRows[i].split(">");
		            	for (int j = 0; j < singleRow.length; j++) {
		                	if(j > 0) { //Skip first and last column
			                	BigDecimal bd = new BigDecimal(singleRow[j]);
		                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
		                	}
		                	else
		                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	//  	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
		                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		                	ODAGrowthTbl.addCell(cell);
		    			}
					}
		            doc.add(ODAGrowthTbl);
	            }
	            doc.add(new Paragraph(" "));
          }
      	if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
          	PdfPTable ODAGraph = new PdfPTable(1);
	            ODAGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getODAGrowthGraph(), "png", ba);
	            Image img = Image.getInstance(ba.toByteArray());
	            ODAGraph.addCell(img);
	            doc.add(ODAGraph);
	            doc.add(new Paragraph(" "));
          }
         
    }
    
    private void getNPOProfileTable(String NPOOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//NPO Profile Table.
		if (!NPOOpt.equals("0")){
			doc.newPage();
			Paragraph subTitle = new Paragraph(postprocessText(NPOProfTrn + amountDesc), SUBTITLEFONT);
		    subTitle.setAlignment(Element.ALIGN_LEFT);
		    doc.add(subTitle);
		    doc.add(new Paragraph(" "));
		}
		if (NPOOpt.equals("1") || NPOOpt.equals("3")){
		    PdfPTable NPOProfTbl = null;
		    String[] NPOProfRows = vForm.getExportData().getNPOTableData().split("<");
		    if (NPOProfRows.length>1){
			    int colspan = NPOProfRows[1].split(">").length; 
			    NPOProfTbl = new PdfPTable(colspan);
			    NPOProfTbl.setWidthPercentage(100);
			    PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(NPOTrn), HEADERFONT));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    NPOProfTbl.addCell(cell);
			    String[] singleRow = NPOProfRows[1].split(">");
			    for (int i = 1; i < singleRow.length; i++) {
			    	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
			    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    	NPOProfTbl.addCell(cell);
				}
			    for (int i = 2; i < NPOProfRows.length; i++) {
			    	singleRow = NPOProfRows[i].split(">");
			    	for (int j = 0; j < singleRow.length; j++) {
			        	if(j > 0) { //Skip first and last column
			            	BigDecimal bd = new BigDecimal(singleRow[j]);
			        		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
			        	}
			        	else
			        		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
			        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        	NPOProfTbl.addCell(cell);
					}
				}
			    doc.add(NPOProfTbl);
		    }
		    doc.add(new Paragraph(" "));
		}
		if (NPOOpt.equals("2") || NPOOpt.equals("3")){
		    PdfPTable NPOGraph = new PdfPTable(1);
		    NPOGraph.setWidthPercentage(100);
		    ByteArrayOutputStream ba = new ByteArrayOutputStream();
		    ImageIO.write(vForm.getExportData().getNPOGraph(), "png", ba);
		    Image img = Image.getInstance(ba.toByteArray());
		    NPOGraph.addCell(img);
		    doc.add(NPOGraph);
		    doc.add(new Paragraph(" "));
		}
    }
    
    private void getProgramProfileTable(String programOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Program Profile Table.
		if (!programOpt.equals("0")){
			doc.newPage();
			Paragraph subTitle = new Paragraph(postprocessText(programProfTrn + amountDesc), SUBTITLEFONT);
		    subTitle.setAlignment(Element.ALIGN_LEFT);
		    doc.add(subTitle);
		    doc.add(new Paragraph(" "));
		}
		if (programOpt.equals("1") || programOpt.equals("3")){
		    PdfPTable programProfTbl = null;
		    String[] programProfRows = vForm.getExportData().getProgramTableData().split("<");
		    if (programProfRows.length>1){
			    int colspan = programProfRows[1].split(">").length; 
			    programProfTbl = new PdfPTable(colspan);
			    programProfTbl.setWidthPercentage(100);
			    PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(programTrn), HEADERFONT));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    programProfTbl.addCell(cell);
			    String[] singleRow = programProfRows[1].split(">");
			    for (int i = 1; i < singleRow.length; i++) {
			    	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
			    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    	programProfTbl.addCell(cell);
				}
			    for (int i = 2; i < programProfRows.length; i++) {
			    	singleRow = programProfRows[i].split(">");
			    	for (int j = 0; j < singleRow.length; j++) {
			        	if(j > 0) { //Skip first and last column
			            	BigDecimal bd = new BigDecimal(singleRow[j]);
			        		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
			        	}
			        	else
			        		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
			        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        	programProfTbl.addCell(cell);
					}
				}
			    doc.add(programProfTbl);
		    }
		    doc.add(new Paragraph(" "));
		}
		if (programOpt.equals("2") || programOpt.equals("3")){
		    PdfPTable programGraph = new PdfPTable(1);
		    programGraph.setWidthPercentage(100);
		    ByteArrayOutputStream ba = new ByteArrayOutputStream();
		    ImageIO.write(vForm.getExportData().getProgramGraph(), "png", ba);
		    Image img = Image.getInstance(ba.toByteArray());
		    programGraph.addCell(img);
		    doc.add(programGraph);
		    doc.add(new Paragraph(" "));
		}
    }
    
    private void getSecondaryProgramProfileTable(String programOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Secondary Program Profile Table.
		if (!programOpt.equals("0")){
			doc.newPage();
			Paragraph subTitle = new Paragraph(postprocessText(secProgramProfTrn + amountDesc), SUBTITLEFONT);
		    subTitle.setAlignment(Element.ALIGN_LEFT);
		    doc.add(subTitle);
		    doc.add(new Paragraph(" "));
		}
		if (programOpt.equals("1") || programOpt.equals("3")){
		    PdfPTable programProfTbl = null;
		    String[] programProfRows = vForm.getExportData().getSecondaryProgramTableData().split("<");
		    if (programProfRows.length>1){
			    int colspan = programProfRows[1].split(">").length; 
			    programProfTbl = new PdfPTable(colspan);
			    programProfTbl.setWidthPercentage(100);
			    PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(programTrn), HEADERFONT));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    programProfTbl.addCell(cell);
			    String[] singleRow = programProfRows[1].split(">");
			    for (int i = 1; i < singleRow.length; i++) {
			    	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
			    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    	programProfTbl.addCell(cell);
				}
			    for (int i = 2; i < programProfRows.length; i++) {
			    	singleRow = programProfRows[i].split(">");
			    	for (int j = 0; j < singleRow.length; j++) {
			        	if(j > 0) { //Skip first and last column
			            	BigDecimal bd = new BigDecimal(singleRow[j]);
			        		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
			        	}
			        	else
			        		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
			        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        	programProfTbl.addCell(cell);
					}
				}
			    doc.add(programProfTbl);
		    }
		    doc.add(new Paragraph(" "));
		}
		if (programOpt.equals("2") || programOpt.equals("3")){
		    PdfPTable programGraph = new PdfPTable(1);
		    programGraph.setWidthPercentage(100);
		    ByteArrayOutputStream ba = new ByteArrayOutputStream();
		    ImageIO.write(vForm.getExportData().getSecondaryProgramGraph(), "png", ba);
		    Image img = Image.getInstance(ba.toByteArray());
		    programGraph.addCell(img);
		    doc.add(programGraph);
		    doc.add(new Paragraph(" "));
		}
    }
    
    private void getAidPredictabilityQuarterTable(String aidPredicQuarterOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
		if (!aidPredicQuarterOpt.equals("0")){
			doc.newPage();
			Paragraph subTitle = new Paragraph(postprocessText(aidPredQuarterTrn + amountDesc), SUBTITLEFONT);
			subTitle.setAlignment(Element.ALIGN_LEFT);
			doc.add(subTitle);
			doc.add(new Paragraph(" "));
	     }
     	if (aidPredicQuarterOpt.equals("1") || aidPredicQuarterOpt.equals("3")){
            PdfPTable aidPredQuarterTbl = null;
            String[] aidPredQuarterRows = vForm.getExportData().getAidPredicQuarterTableData().split("<");
            if (aidPredQuarterRows.length>1){
	            int colspan = (aidPredQuarterRows[1].split(">").length + 1)/2; 
	            aidPredQuarterTbl = new PdfPTable(colspan);
	            aidPredQuarterTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(plannedTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(postprocessText(actualTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            for (int i = 1; i < aidPredQuarterRows.length; i++) {
	            	String[] singleRow = aidPredQuarterRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(postprocessText(singleRow[j]));
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidPredQuarterTbl.addCell(cell);
	    			}
				}
	            doc.add(aidPredQuarterTbl);
            }
            doc.add(new Paragraph(" "));
      	}
      	if (aidPredicQuarterOpt.equals("2") || aidPredicQuarterOpt.equals("3")){
            PdfPTable aidPredQuarterGraph = new PdfPTable(1);
            aidPredQuarterGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getAidPredictabilityQuarterGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            aidPredQuarterGraph.addCell(img);
            doc.add(aidPredQuarterGraph);
            doc.add(new Paragraph(" "));
      	}
    }
    
    private void getBudgetBreakdownTable(String budgetBreakdownOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Budget breakdown Table.
		if (!budgetBreakdownOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(budgetBreakdownTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (budgetBreakdownOpt.equals("1") || budgetBreakdownOpt.equals("3")){
            PdfPTable budgetBreakdownTbl = null;
            String[] budgetBreakdownRows = vForm.getExportData().getBudgetTableData().split("<");
            if (budgetBreakdownRows.length>1){
	            int colspan = (budgetBreakdownRows[1].split(">").length + 1)/2; 
	            budgetBreakdownTbl = new PdfPTable(colspan);
	            budgetBreakdownTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(yearTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            budgetBreakdownTbl.addCell(cell);
	            String[] singleRow = budgetBreakdownRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	budgetBreakdownTbl.addCell(cell);
				}
	            for (int i = 1; i < budgetBreakdownRows.length; i++) {
	            	singleRow = budgetBreakdownRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	budgetBreakdownTbl.addCell(cell);
	    			}
				}
	            doc.add(budgetBreakdownTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (budgetBreakdownOpt.equals("2") || budgetBreakdownOpt.equals("3")){
            PdfPTable budgetBreakdownGraph = new PdfPTable(1);
            budgetBreakdownGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBudgetGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            budgetBreakdownGraph.addCell(img);
            doc.add(budgetBreakdownGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getBeneficiaryAgencyProfileTable(String beneficiaryAgencyOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Beneficiary Agency Profile Table.
        if (!beneficiaryAgencyOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(beneficiaryAgencyProfTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (beneficiaryAgencyOpt.equals("1") || beneficiaryAgencyOpt.equals("3")){
            PdfPTable organizationProfTbl = null;
            String[] organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
            if (organizationProfRows.length>1){
	            int colspan = organizationProfRows[1].split(">").length; 
	            organizationProfTbl = new PdfPTable(colspan);
	            organizationProfTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(organizationTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            organizationProfTbl.addCell(cell);
	            String[] singleRow = organizationProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	organizationProfTbl.addCell(cell);
				}
	            for (int i = 2; i < organizationProfRows.length; i++) {
	            	singleRow = organizationProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	organizationProfTbl.addCell(cell);
	    			}
				}
	            doc.add(organizationProfTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (beneficiaryAgencyOpt.equals("2") || beneficiaryAgencyOpt.equals("3")){
            PdfPTable organizationGraph = new PdfPTable(1);
            organizationGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBeneficiaryAgencyGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            organizationGraph.addCell(img);
            doc.add(organizationGraph);
            doc.add(new Paragraph(" "));
        }
    }
    
    private void getResponsibleOrganizationTable(String responsibleOrganizationOpt, com.lowagie.text.Document doc, VisualizationForm vForm, HttpServletRequest request, String amountDesc) throws Exception{
    	//Beneficiary Agency Profile Table.
        if (!responsibleOrganizationOpt.equals("0")){
        	doc.newPage();
        	Paragraph subTitle = new Paragraph(postprocessText(beneficiaryAgencyProfTrn + amountDesc), SUBTITLEFONT);
            subTitle.setAlignment(Element.ALIGN_LEFT);
            doc.add(subTitle);
            doc.add(new Paragraph(" "));
        }
        if (responsibleOrganizationOpt.equals("1") || responsibleOrganizationOpt.equals("3")){
            PdfPTable organizationProfTbl = null;
            String[] organizationProfRows = vForm.getExportData().getBeneficiaryAgencyTableData().split("<");
            if (organizationProfRows.length>1){
	            int colspan = organizationProfRows[1].split(">").length; 
	            organizationProfTbl = new PdfPTable(colspan);
	            organizationProfTbl.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(organizationTrn), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            organizationProfTbl.addCell(cell);
	            String[] singleRow = organizationProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(postprocessText(singleRow[i]), HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	organizationProfTbl.addCell(cell);
				}
	            for (int i = 2; i < organizationProfRows.length; i++) {
	            	singleRow = organizationProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(postprocessText(singleRow[j])));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	organizationProfTbl.addCell(cell);
	    			}
				}
	            doc.add(organizationProfTbl);
            }
            doc.add(new Paragraph(" "));
        }
        if (responsibleOrganizationOpt.equals("2") || responsibleOrganizationOpt.equals("3")){
            PdfPTable organizationGraph = new PdfPTable(1);
            organizationGraph.setWidthPercentage(100);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(vForm.getExportData().getBeneficiaryAgencyGraph(), "png", ba);
            Image img = Image.getInstance(ba.toByteArray());
            organizationGraph.addCell(img);
            doc.add(organizationGraph);
            doc.add(new Paragraph(" "));
        }
    }
}
