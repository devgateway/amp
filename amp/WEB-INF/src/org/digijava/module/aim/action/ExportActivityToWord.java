package org.digijava.module.aim.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Identification;
import org.digijava.module.aim.form.EditActivityForm.Planning;
import org.digijava.module.aim.form.EditActivityForm.Programs;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ExportActivityToPdfUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;

public class ExportActivityToWord extends Action {
	
	private static Logger logger = Logger.getLogger(ExportActivityToWord.class);
	public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(0, 255, 0);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Color CELLCOLORGRAY = new Color(242,242,242);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font BOLDFONT = new Font(Font.TIMES_ROMAN, 10,Font.BOLD);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);
    public static final int ROWS_IN_IDENTIFICATION_TABLE = 10;
    
    private static final Chunk BULLET_SYMBOL = new Chunk("\u2022");    
    private Identification identification = null;
    private Planning planning = null;
    private org.digijava.module.aim.form.EditActivityForm.Location location =null;
    private Programs programs =null;
    org.digijava.module.aim.form.EditActivityForm.Sector sectors =null;

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		EditActivityForm myForm=(EditActivityForm)form;
		identification = myForm.getIdentification();
		planning = myForm.getPlanning();
		location = myForm.getLocation();
		programs = myForm.getPrograms();
		sectors = myForm.getSectors();
		
		
		ServletContext ampContext = getServlet().getServletContext();
		//to know whether print happens from Public View or not
		HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
		Long actId=null;
		AmpActivityVersion activity=null;
		if(request.getParameter("activityid")!=null){
			actId=new Long(request.getParameter("activityid"));
		}
				
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=activity.doc");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        
        RtfWriter2.getInstance(doc, baos);
        doc.open();
        try {
        	activity=ActivityUtil.loadActivity(actId);        	
			if(activity != null){
				AmpCategoryValue catVal=null;
				String translatedValue="";
				String output="";
				String columnName="";
				String columnVal="";
				String currency = myForm.getCurrCode();
				
				Paragraph pageTitle = new Paragraph(activity.getName(), TITLEFONT);
	            pageTitle.setAlignment(Element.ALIGN_CENTER);
	            doc.add(pageTitle);
	            doc.add(new Paragraph(" "));
	            
	            RtfCell cell = null;
	            Paragraph p1 = null;
	            
	            
	            Table overAllTable = createOverallInformationTable(request,myForm, ampContext, currency);	            
	            doc.add(overAllTable);
	            doc.add(new Paragraph(" "));
	            
	            
	            /**
	             * Identification step
	             */
	            Table identificationTbl = null;
	            identificationTbl = new Table(1);
	            identificationTbl.setWidth(100);
	            RtfCell identificationTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Identification",request).toUpperCase(), HEADERFONT));
	            //identificationTitleCell.setColspan(2);
	            identificationTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            identificationTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            identificationTbl.addCell(identificationTitleCell);	            
	            //1st cell of the overAll Table	            
	            RtfCell identificationTblCell1 = new RtfCell();	            
	            Table identificationSubTable1 = new Table(2);
	            identificationSubTable1.setWidths(new float[]{1f,2f});
	            identificationSubTable1.setWidth(100);
	            identificationSubTable1.setBorder(0);
	            generateIdentificationPart(request, ampContext, p1,identificationSubTable1);
	            identificationTblCell1.add(identificationSubTable1);
	            //identificationTbl.addCell(identificationTblCell1);
	            doc.add(identificationTbl);
                doc.add(identificationSubTable1);
	            
	            /**
	             * Planning step
	             */
	            Table planningTbl = null;
	            planningTbl = new Table(1);
	            planningTbl.setWidth(100);
	            RtfCell planningTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Planning",request).toUpperCase(), HEADERFONT));
	            //identificationTitleCell.setColspan(2);
	            planningTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            planningTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            planningTbl.addCell(planningTitleCell);
	            
	            RtfCell planningTblCell1 = new RtfCell();	            
	            Table planningSubTable1 = new Table(2);
	            planningSubTable1.setWidths(new float[]{1f,2f});
	            planningSubTable1.setWidth(100);
	            
	            processPlanningPart(request, ampContext, planningSubTable1);
	            planningTblCell1.add(planningSubTable1);
	            //planningTbl.addCell(planningTblCell1);
	            doc.add(planningTbl);
                doc.add(planningSubTable1);
	            doc.add(new Paragraph(" "));
	            
	            /**
	             * Location Step
	             */	            
	            Table locationTbl = null;
	            locationTbl = new Table(1);
	            locationTbl.setWidth(100);
	            RtfCell locationTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Location",request).toUpperCase(), HEADERFONT));
	            locationTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            locationTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            locationTbl.addCell(locationTitleCell);

	            RtfCell locationTblCell1 = new RtfCell();	            
	            Table locationSubTable1 = new Table(2);
	            locationSubTable1.setWidths(new float[]{1f,2f});
	            locationSubTable1.setWidth(100);
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location", ampContext)){
	            	cell = new RtfCell();
	    			cell.setColspan(2);
	    			cell.setBorder(0);	            	
	            	columnVal="";
	            	if(location.getSelectedLocs()!=null){    	    			
    	    			for (Location loc  : location.getSelectedLocs()) {
    	    				for (String val : loc.getAncestorLocationNames()) {
    	    					columnVal+="["+val+"]";
							}
    	    				if(FeaturesUtil.isVisibleField("Regional Percentage", ampContext)){
    	    					columnVal+="\t\t"+loc.getPercent()+" %\n";
    	    				}    						
    					}    					
    	        	}
	            	p1 = new Paragraph(columnVal,BOLDFONT);
	            	cell.add(p1);
	            	locationSubTable1.addCell(cell);	            	
	            }
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Level", ampContext)){	            	
	            	columnName=TranslatorWorker.translateText("Implementation Level",request);
	    			columnVal="";
	    			catVal = null;
	    			if(location.getLevelId()!=null && location.getLevelId() !=0){
	    				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(location.getLevelId());
	    			}					
	    			if(catVal!=null){
	    				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
	    			}
	    			generateOverAllTableRows(locationSubTable1,columnName,columnVal,null);
	            }
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location", ampContext)){
	            	columnName=TranslatorWorker.translateText("Implementation Location",request);
	    			columnVal="";
	    			catVal = null;
	    			if(location.getImplemLocationLevel()!=null && location.getImplemLocationLevel() !=0){
	    				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(location.getImplemLocationLevel());
	    			}					
	    			if(catVal!=null){
	    				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
	    			}
	    			generateOverAllTableRows(locationSubTable1,columnName,columnVal,null);
	            }
	            
	            locationTblCell1.add(locationSubTable1);
            	//locationTbl.addCell(locationTblCell1);
	            doc.add(locationTbl);
                doc.add(locationSubTable1);
	            doc.add(new Paragraph(" "));
	            
	            /**
	             * PROGRAMS PART
	             */
	            Table programsTbl = null;
	            programsTbl = new Table(1);
	            programsTbl.setWidth(100);
	            RtfCell natPlanTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("programs",request).toUpperCase(), HEADERFONT));
	            natPlanTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            natPlanTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            programsTbl.addCell(natPlanTitleCell);
	            programsTbl.addCell(new RtfCell());
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Program/National Plan Objective", ampContext)){		           
		            if(programs.getNationalPlanObjectivePrograms()!=null){
						cell = new RtfCell();
						cell.setBorder(0);
						cell.add(new Paragraph(TranslatorWorker.translateText("National Plan Objective",request).toUpperCase(), BOLDFONT));						
						programsTbl.addCell(cell);
						 programsTbl.addCell(new RtfCell());
						columnVal= buildProgramsOutput(programs.getNationalPlanObjectivePrograms());
						cell = new RtfCell();
						cell.setBorder(0);
						cell.add(new Paragraph(columnVal,PLAINFONT));
						programsTbl.addCell(cell);
						applyEmptyCell(programsTbl,1);
					}
	            }
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Program", ampContext)){
	            	
	            	if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Primary Programs", ampContext)){		           
			            if(programs.getNationalPlanObjectivePrograms()!=null){
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(TranslatorWorker.translateText("Primary Programs",request).toUpperCase(), BOLDFONT));						
							programsTbl.addCell(cell);
							 programsTbl.addCell(new RtfCell());
							columnVal= buildProgramsOutput(programs.getPrimaryPrograms());
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(columnVal,PLAINFONT));
							programsTbl.addCell(cell);
							
							applyEmptyCell(programsTbl,1);
						}			           
		            }
	            	
	            	
		            if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Secondary Programs", ampContext)){		           
			            if(programs.getNationalPlanObjectivePrograms()!=null){
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(TranslatorWorker.translateText("Secondary Programs",request).toUpperCase(), BOLDFONT));						
							programsTbl.addCell(cell);
							programsTbl.addCell(new RtfCell());
							columnVal= buildProgramsOutput(programs.getSecondaryPrograms());
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(columnVal,PLAINFONT));
							programsTbl.addCell(cell);
						}
		            }
	            } 
	            
	            doc.add(programsTbl);
	            doc.add(new Paragraph(" "));
	            
	            /**
	             * Sectors Part
	             */	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors", ampContext)){
	            	 Table sectorsTbl = null;
	 	            sectorsTbl = new Table(1);
	 	            sectorsTbl.setWidth(100);
	 	            RtfCell sectTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Sector",request).toUpperCase(), HEADERFONT));
	 	            sectTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	            sectTitleCell.setBackgroundColor(CELLCOLORGRAY);
	 	            sectorsTbl.addCell(sectTitleCell);
	 	            
	 	            if(sectors.getClassificationConfigs() != null){
	 	            	for (AmpClassificationConfiguration config : (List<AmpClassificationConfiguration>)sectors.getClassificationConfigs()) {
	 	            		if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors/"+config.getName()+" Sectors", ampContext)){
	 	            			boolean hasSectors=false;
	 	            			for (ActivitySector actSect : sectors.getActivitySectors()) {
									if(actSect.getConfigId().equals(config.getId())){
										hasSectors=true;
									}									
								}
	 	            			if(hasSectors){
									cell = new RtfCell();
									cell.setBorder(0);
									cell.add(new Paragraph(config.getName()+TranslatorWorker.translateText(" Sector",request).toUpperCase(), PLAINFONT));						
									sectorsTbl.addCell(cell);
									sectorsTbl.addCell(new RtfCell());
								}
								if(sectors.getActivitySectors() !=null){
									for (ActivitySector actSect : sectors.getActivitySectors()) {
										if(actSect.getConfigId().equals(config.getId())){
											cell = new RtfCell();
											cell.setBorder(0);
											columnVal = actSect.getSectorScheme();
											if(actSect.getSectorName() !=null){
												columnVal += " - " +actSect.getSectorName(); 
											}
											if(actSect.getSubsectorLevel1Name() !=null){
												columnVal += " - "+actSect.getSubsectorLevel1Name();
											}
											if(actSect.getSubsectorLevel2Name() !=null){
												columnVal += " - "+actSect.getSubsectorLevel2Name();
											}
											columnVal += " "+actSect.getSectorPercentage() +" %";
											cell.add(new Paragraph(columnVal,PLAINFONT));
											sectorsTbl.addCell(cell);
										}
									}
								}
								applyEmptyCell(sectorsTbl, 1);
	 	            		}
						}
	 	            }
	 	           doc.add(sectorsTbl);
	 	           doc.add(new Paragraph(" "));
	            }	            

                for (Table tbl : getDonorFundingTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

                for (Table tbl : getRegioanlFundingTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

                List<Table> componentTables = getComponentTables(request, ampContext, activity);
				for (Table tbl : componentTables) {
                    doc.add(tbl);
                }

                for (Table tbl : getIssuesTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

                for (Table tbl : getRelatedOrgsTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

                for (Table tbl : getContactInfoTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

                for (Table tbl : getProposedProjectCostTables(request, ampContext, activity)) {
                    doc.add(tbl);
                }

			}
        	
            
            //close document
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
		} catch (Exception e) {
			logger.error(e);
		}
        
        
		return null;
	}

	private void processPlanningPart(HttpServletRequest request,
			ServletContext ampContext, Table planningSubTable1)
			throws WorkerException, Exception {
		String columnName;
		String columnVal;
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Line Ministry Rank", ampContext)){
			columnName=TranslatorWorker.translateText("Line Ministry Rank",request)+": ";
			columnVal = planning.getLineMinRank().equals("-1")?"":planning.getLineMinRank();
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Ministry of Planning Rank", ampContext)){
			columnName=TranslatorWorker.translateText("Ministry of Planning Rank",request)+": ";
			columnVal = planning.getPlanMinRank().equals("-1")?"":planning.getPlanMinRank();
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Start Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Start Date", request) + ":";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getOriginalStartDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Start Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Start Date",request) +": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getRevisedStartDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Approval Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Approval Date",request) +": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getOriginalAppDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Approval Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Approval Date",request)+": "; 
			generateOverAllTableRows(planningSubTable1,columnName,planning.getRevisedAppDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Contracting", ampContext)){
			columnName=TranslatorWorker.translateText("Final Date for Contracting",request)+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getContractingDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Disbursements", ampContext)){
			columnName=TranslatorWorker.translateText("Final Date for Disbursements",request)+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getDisbursementsDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Completion Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Completion Date",request)+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getProposedCompDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Completion Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Completion Date",request)+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getCurrentCompDate(),null);
		}
		
		if(FeaturesUtil.isVisibleField("Duration of Project", ampContext)){
			columnName=TranslatorWorker.translateText("Duration of Project",request)+": ";
			columnVal = planning.getProjectPeriod()!=null?planning.getProjectPeriod().toString():"";
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
	}


    /*
     * Proposed Project Cost
     */
    private List<Table> getProposedProjectCostTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Proposed Project Cost", true).setWidth(100f).setAlign("left");

        retVal.add(createSectionTable(eshTitle, request, ampContext));

//        Set<AmpFundingDetail> allComponents = new HashSet<AmpFundingDetail>();
//        
//        for (AmpFunding f : (Set<AmpFunding>) act.getFunding()) {
//            if (f.getFundingDetails() != null && !f.getFundingDetails().isEmpty()) {
//                allComponents.addAll(f.getFundingDetails());
//            }
//        }
        Site site = RequestUtils.getSite(request);
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        String siteId=site.getSiteId();
        String locale=navigationLanguage.getCode();
        String currencyCode = (String) request.getSession().getAttribute(org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY);
        if(currencyCode == null) {
            currencyCode = Constants.DEFAULT_CURRENCY;
        }
        
        String translatedCurrency = TranslatorWorker.translateText(currencyCode,locale,siteId);
        translatedCurrency=("".equalsIgnoreCase(currencyCode))?currencyCode:translatedCurrency;


//        FundingCalculationsHelper fch = new FundingCalculationsHelper();
//        fch.doCalculations(allComponents, currencyCode);




        ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
        eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Cost", null, null,  true).
                                                addRowData(FormatHelper.formatNumber(act.getFunAmount())).
                                                addRowData(translatedCurrency));

        eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Proposed Completion Date ", null, null,  true).
                                                        addRowData(DateConversion.ConvertDateToString(act.getFunDate())));


        retVal.add(createSectionTable(eshProjectCostTable, request, ampContext));
        return retVal;
    }



    /*
     * Contact info. section
     */
    private List<Table> getContactInfoTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Contact Information", true).setWidth(100f).setAlign("left");

        retVal.add(createSectionTable(eshTitle, request, ampContext));


        if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts", ampContext)){
            if (act.getActivityContacts() != null && !act.getActivityContacts().isEmpty()) {
                Set<AmpActivityContact> actContacts = act.getActivityContacts();



                Map <String, Set<AmpActivityContact>> contactGrouper = new HashMap<String, Set<AmpActivityContact>>();
                ExportSectionHelper eshContactInfoTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                for (AmpActivityContact actContact : actContacts) {
                    if (!contactGrouper.containsKey(actContact.getContactType())) {
                        contactGrouper.put(actContact.getContactType(), new HashSet<AmpActivityContact>());
                    }
                    contactGrouper.get(actContact.getContactType()).add(actContact);
                }

                for (String contactType : contactGrouper.keySet()) {
                    Set<AmpActivityContact> groupedContactTypeSet = contactGrouper.get(contactType);
                    eshContactInfoTable.addRowData(new ExportSectionHelperRowData(getContactTypeLable(contactType), null, null,  false));
                    for (AmpActivityContact contact : groupedContactTypeSet) {
                    	String contFunction = contact.getContact().getFunction() != null ?
                                contact.getContact().getFunction() : "-";

                        String contEmail = contact.getContact().getEmails() != null ?
                        		contact.getContact().getEmails().iterator().next() : "-";
                                
                        eshContactInfoTable.addRowData(new ExportSectionHelperRowData(contact.getContact().getNameAndLastName(), null, null,  false).
                                addRowData(contEmail).
                                addRowData(contFunction));



                    }

                    eshContactInfoTable.addRowData(new ExportSectionHelperRowData(null, null, null,  false).setSeparator(true));
                }

                retVal.add(createSectionTable(eshContactInfoTable, request, ampContext));
            }
        }

        return retVal;
    }



    /*
     * Related org.s section
     */

    private List<Table> getRelatedOrgsTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Related Organizations", true).setWidth(100f).setAlign("left");

                retVal.add(createSectionTable(eshTitle, request, ampContext));


                if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations", ampContext)){
                    if (act.getOrgrole() != null && !act.getOrgrole().isEmpty()) {
                        Set<AmpOrgRole> orgRoles = act.getOrgrole();

                        Map <String, Set<AmpOrgRole>> roleGrouper = new HashMap<String, Set<AmpOrgRole>>();
                        ExportSectionHelper eshRelatedOrgsTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                        for (AmpOrgRole orgRole : orgRoles) {
                            if (!roleGrouper.containsKey(orgRole.getRole().getRoleCode())) {
                                roleGrouper.put(orgRole.getRole().getRoleCode(), new HashSet<AmpOrgRole>());
                            }

                            roleGrouper.get(orgRole.getRole().getRoleCode()).add(orgRole);

                            //eshIssuesTable.addRowData(new ExportSectionHelperRowData(orgRile.getRole().getName(), false));
                            //retVal.add(createSectionTable(eshIssuesTable, request));
                        }
                        
                        for (String roleCode : roleGrouper.keySet()) {
                        	Set<AmpOrgRole> groupedRoleSet = roleGrouper.get(roleCode);
                        	
                        	if (roleCode.equals(Constants.RESPONSIBLE_ORGANISATION) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Responsible Organization", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Responsible Organization");
                        	}
                        	
                        	if (roleCode.equals(Constants.EXECUTING_AGENCY) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Executing Agency", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Executing Agency");
                        	}
                        	
                        	if (roleCode.equals(Constants.IMPLEMENTING_AGENCY) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Implementing Agency", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Implementing Agency");
                        	}
                        	
                        	if (roleCode.equals(Constants.BENEFICIARY_AGENCY) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Beneficiary Agency", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Beneficiary Agency");
                        	}
                            
                        	if (roleCode.equals(Constants.CONTRACTING_AGENCY) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Contracting Agency", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Contracting Agency");
                        	}
                        	
                        	if (roleCode.equals(Constants.SECTOR_GROUP) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Sector Group", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Sector Group");
                        	}

                        	if (roleCode.equals(Constants.REGIONAL_GROUP) &&
                        			FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Regional Group", ampContext)){
                        		buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Regional Group");
                        	}
                        	
                        }

                        retVal.add(createSectionTable(eshRelatedOrgsTable, request, ampContext));
                    }
                }

        return retVal;
    }
    
    private void buildRoleOrgInfo(ExportSectionHelper eshRelatedOrgsTable, Set<AmpOrgRole> groupedRoleSet, String roleName){
    	
        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(roleName, null, null,  true));
        for (AmpOrgRole role : groupedRoleSet) {
            Double orgPercentage = role.getPercentage() == null ? new Double(0) : role.getPercentage();
            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null,  false).
                    addRowData(role.getOrganisation().getName()).
                    addRowData(orgPercentage.toString() + "%"));
        }
        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(null,null,null, false).setSeparator(true));
    }

    /*
     * Issue section
     */
    private List<Table> getIssuesTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Issues", true).setWidth(100f).setAlign("left");

                retVal.add(createSectionTable(eshTitle, request, ampContext));


                if(FeaturesUtil.isVisibleModule("/Activity Form/Issues Section", ampContext)){
                    if (act.getIssues() != null && !act.getIssues().isEmpty()) {
                        Set<AmpIssues> issues = act.getIssues();

                        ExportSectionHelper eshIssuesTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                        for (AmpIssues issue : issues) {
                            eshIssuesTable.addRowData(new ExportSectionHelperRowData(issue.getName() + "  " + DateConversion.ConvertDateToString(issue.getIssueDate()), null, null, false));
                            if (issue.getMeasures() != null && !issue.getMeasures().isEmpty()) {
                                for (AmpMeasure measure : (Set<AmpMeasure>) issue.getMeasures()) {
                                    eshIssuesTable.addRowData((new ExportSectionHelperRowData(" �" + measure.getName(), null, null, false)));
                                    if(measure.getActors() != null && !measure.getActors().isEmpty()) {
                                        for (AmpActor actor : (Set<AmpActor>) measure.getActors()) {
                                            eshIssuesTable.addRowData((new ExportSectionHelperRowData("  �" + actor.getName(), null, null, false)));
                                        }
                                    }
                                }
                            }
                            retVal.add(createSectionTable(eshIssuesTable, request, ampContext));
                        }
                    }
                }

        return retVal;
    }

    /*
     * Component funding section
     */
    private List<Table> getComponentTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Components", true).setWidth(100f).setAlign("left");

        if(FeaturesUtil.isVisibleModule("/Activity Form/Components", ampContext)){
           retVal.add(createSectionTable(eshTitle, request, ampContext));

           for (AmpComponent comp : act.getComponents()){

        	   ExportSectionHelper eshCompFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
        	   eshCompFundingDetails.addRowData((new ExportSectionHelperRowData(comp.getTitle(), null, null, false)));
        	   String compDesc = comp.getDescription() != null ? comp.getDescription() : "";
        	   eshCompFundingDetails.addRowData((new ExportSectionHelperRowData("Description", null, null, true)).addRowData(compDesc));

        	   eshCompFundingDetails.addRowData((new ExportSectionHelperRowData("Component Funding", null, null, true)));
        	   
        	   if (act.getComponentFundings() != null && !act.getComponentFundings().isEmpty()) {
	                Set<AmpComponentFunding> compFnds = act.getComponentFundings();
	                
	                for (AmpComponentFunding compFnd : compFnds) {
	                	if(compFnd.getComponent().getTitle().equals(comp.getTitle())){
		                    eshCompFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(compFnd.getTransactionType()), null, null, true)).
		                            //addRowData(getTransactionTypeLable(compFnd.getTransactionType()), true).
		                            addRowData(compFnd.getAdjustmentType().getLabel(), true).
		                            addRowData(DateConversion.ConvertDateToString(compFnd.getTransactionDate())).
		                            addRowData(compFnd.getTransactionAmount().toString()).
		                            addRowData(compFnd.getCurrency().getCurrencyCode()));
	                	}
	                }
                	retVal.add(createSectionTable(eshCompFundingDetails, request, ampContext));
	            }        	   
           }
        }

        return retVal;
    }

    /*
     * Regional funding section
     */
    private List<Table> getRegioanlFundingTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Regional Fundings", true).setWidth(100f).setAlign("left");
        	if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding", ampContext)){
                retVal.add(createSectionTable(eshTitle, request, ampContext));

                if (act.getRegionalFundings() != null && !act.getRegionalFundings().isEmpty()) {
                    Set<AmpRegionalFunding> regFnds = act.getRegionalFundings();

                    ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                    for (AmpRegionalFunding regFnd : regFnds) {
                        eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(regFnd.getTransactionType()), null, null, true)).
                                                                        addRowData(regFnd.getRegionLocation().getName()).
                                                                        addRowData(regFnd.getAdjustmentType().getLabel(), true).
                                                                        addRowData(DateConversion.ConvertDateToString(regFnd.getTransactionDate())).
                                                                        addRowData(regFnd.getTransactionAmount().toString()).
                                                                        addRowData(regFnd.getCurrency().getCurrencyCode()));


                        retVal.add(createSectionTable(eshRegFundingDetails, request, ampContext));
                    }
                }
            }
        
        return retVal;
    }

    /*
     * Donor funding section
     */
    private List<Table> getDonorFundingTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Donor Fundings", true).setWidth(100f).setAlign("left");

        retVal.add(createSectionTable(eshTitle, request, ampContext));


        if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding", ampContext)){
            if (act.getFunding() != null && !act.getFunding().isEmpty()) {
                for (AmpFunding fnd : (Set<AmpFunding>)act.getFunding()) {
                    ExportSectionHelper eshDonorInfo = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Funding Classification/Funding Organization Id", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Id", null, null, true)).addRowData(fnd.getFinancingId()));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Donor Organisation", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Name", null, null, true)).addRowData(fnd.getAmpDonorOrgId().getName()));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Funding Classification/Type of Assistence", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Type of Assistance", null, null, true)).addRowData(fnd.getTypeOfAssistance().getLabel()));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Funding Classification/Financing Instrument", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Financial Instrument", null, null, true)).addRowData(fnd.getFinancingInstrument().getLabel()));
                    }
                    eshDonorInfo.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
                    retVal.add(createSectionTable(eshDonorInfo, request, ampContext));

                    Set <AmpFundingDetail> fndDets = fnd.getFundingDetails();

                    Map<String, Map<String, Set<AmpFundingDetail>>> structuredFundings = getStructuredFundings(fndDets);

                    ExportSectionHelper eshDonorFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                    for (String transTypeKey : structuredFundings.keySet()) {
                        Map<String, Set<AmpFundingDetail>> transTypeGroup = structuredFundings.get(transTypeKey);
                        for (String adjTypeKey : transTypeGroup.keySet()) {
                            eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(new StringBuilder(adjTypeKey).
                                    append(" ").append(transTypeKey).toString(),null, null, true));
                            Set<AmpFundingDetail> structuredFndDets = transTypeGroup.get(adjTypeKey);
                            for (AmpFundingDetail fndDet : structuredFndDets) {
                                eshDonorFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(fndDet.getTransactionType()), null, null, true)).
                                        addRowData(fndDet.getAdjustmentType().getLabel(), true).
                                        addRowData(DateConversion.ConvertDateToString(fndDet.getTransactionDate())).
                                        addRowData(FormatHelper.formatNumber(fndDet.getTransactionAmount())).
                                        addRowData(fndDet.getAmpCurrencyId().getCurrencyCode()));
                            }

                            eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
                        }
                    }
                    retVal.add(createSectionTable(eshDonorFundingDetails, request, ampContext));
                }
            }
        }
        return retVal;
    }
    
	private void generateIdentificationPart(HttpServletRequest request,	ServletContext ampContext, Paragraph p1,Table identificationSubTable1) throws WorkerException, Exception {
		AmpCategoryValue catVal;
		String columnName;
		String columnVal;
		RtfCell cell;
		//AMPID cells
		if(FeaturesUtil.isVisibleField("AMP ID", ampContext)){
			columnName=TranslatorWorker.translateText("AMP ID",request);
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getAmpId(),null);					
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Reason", ampContext)){
			columnName=TranslatorWorker.translateText("Status",request);
			columnVal="";
			catVal = null;
			if(identification.getStatusId()!=null && identification.getStatusId()!=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getStatusId());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			if(identification.getStatusReason() != null){
				columnVal += processHtml(request, identification.getStatusReason());
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective", ampContext)){
			columnName=TranslatorWorker.translateText("Objectives",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getObjectives()),null);
		}
		
		ArrayList<AmpComments> colAux	= null;
        Collection ampFields = DbUtil.getAmpFields();
        

        
		HashMap allComments = new HashMap();
		Long actId=new Long(request.getParameter("activityid"));
        if (ampFields!=null) {
        	for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                AmpField field = (AmpField) itAux.next();
                colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),actId);
                allComments.put(field.getFieldName(), colAux);
              }
        }

		if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments", ampContext)) {
			TeamMember teamMember = (TeamMember) request.getSession().getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
			//objective comments
			if(teamMember!=null ){ //Objective Comments shouldn't show up on Publc View
	            
	            Table objTable=new Table(2);
	            objTable.getDefaultCell().setBorder(0);
	            for (Object commentKey : allComments.keySet()) {            	
					String key=(String)commentKey;
					List<AmpComments> values=(List<AmpComments>)allComments.get(key);
					if(key.equalsIgnoreCase("Objective Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Assumption", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Assumption", request)+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(),request),BOLDFONT));
						}					
					}else if(key.equalsIgnoreCase("Objective Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Verification", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Verification",request)+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), request),BOLDFONT));
						}					
					}else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators", ampContext)) {
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Objectively Verifiable Indicators",request)+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(),request),BOLDFONT));						
						}
					}
				}			            
	            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Objective Comments",request), objTable,null);
			}
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Description", ampContext)){
			columnName=TranslatorWorker.translateText("Description",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getDescription()),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Comments", ampContext)){
			columnName=TranslatorWorker.translateText("Project Comments",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getProjectComments()),null);
		}
		
		if(FeaturesUtil.isVisibleField("NPD Clasification", ampContext)){
			columnName=TranslatorWorker.translateText("NPD Clasification",request);
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getClasiNPD(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Lessons Learned", ampContext)){
			columnName=TranslatorWorker.translateText("Lessons Learned",request);
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getLessonsLearned(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Impact", ampContext)){
			columnName=TranslatorWorker.translateText("Project Impact",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getProjectImpact()),null);
		}
		
		if(identification.getActivitySummary() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Summary", ampContext)){
			columnName=TranslatorWorker.translateText("Activity Summary",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getActivitySummary()),null);
		}
		
		if(identification.getContractingArrangements() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Contracting Arrangements", ampContext)){
			columnName=TranslatorWorker.translateText("Contracting Arrangements",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getContractingArrangements()),null);
		}
		
		if(identification.getCondSeq() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionality and Sequencing", ampContext)){
			columnName=TranslatorWorker.translateText("Conditionality and Sequencing",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getCondSeq()),null);
		}
		
		if(identification.getLinkedActivities() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Linked Activities", ampContext)){
			columnName=TranslatorWorker.translateText("Linked Activities",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getLinkedActivities()),null);
			applyEmptyCell(identificationSubTable1);
		}
		
		if(identification.getConditionality() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionalities", ampContext)){
			columnName=TranslatorWorker.translateText("Conditionalities",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getConditionality()),null);
		}
		
		if(identification.getProjectManagement() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Management", ampContext)){
			columnName=TranslatorWorker.translateText("Project Management",request);
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getProjectManagement()),null);
		}
		
		//Results
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();

		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results", ampContext)){
			
			columnName=TranslatorWorker.translateText("Results",locale,siteId);
			columnVal=processEditTagValue(request, identification.getResults());
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		/**
		 *  Results Comments
		 */
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments", ampContext)){

			Table objTable=new Table(2);
			objTable.getDefaultCell().setBorder(0);
	        for (Object commentKey : allComments.keySet()) {            	
        	   String key=(String)commentKey;
        	   List<AmpComments> values=(List<AmpComments>)allComments.get(key);
        	   if(key.equalsIgnoreCase("Results Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Assumption", ampContext)){
        		   for (AmpComments value : values) {
        			   objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Assumption", locale, siteId)+" :",PLAINFONT));
        			   objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),BOLDFONT));
        		   }					
				}else if(key.equalsIgnoreCase("Results Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Verification", ampContext)){
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Verification", locale, siteId)+" :",PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),BOLDFONT));
					}					
				}else if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")&& FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators", ampContext)) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Objectively Verifiable Indicators", locale, siteId)+" :",PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),BOLDFONT));						
					}
				}
			}
            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Results Comments",request), objTable,null);
		}
				
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Accession Instrument", ampContext)){
			columnName=TranslatorWorker.translateText("Accession Instrument",request);
			columnVal="";
			catVal = null;
			if(identification.getAccessionInstrument()!=null && identification.getAccessionInstrument() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAccessionInstrument());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}	
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Implementing Unit", ampContext)){
			columnName=TranslatorWorker.translateText("Project Implementing Unit",request);
			columnVal="";
			catVal = null;
			if(identification.getProjectImplUnitId()!=null && identification.getProjectImplUnitId() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectImplUnitId());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}					
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
			
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/A.C. Chapter", ampContext)){
			columnName=TranslatorWorker.translateText("A.C. Chapter",request);
			columnVal="";
			catVal = null;
			if(identification.getAcChapter()!=null && identification.getAcChapter() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAcChapter());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}	
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Cris Number", ampContext)){
			columnName=TranslatorWorker.translateText("Cris Number",request);
			columnVal="";
			catVal = null;
			if(identification.getCrisNumber()!=null){
				columnVal = identification.getCrisNumber();
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Procurement System", ampContext)){
			columnName=TranslatorWorker.translateText("Procurement System",request);
			columnVal="";
			catVal = null;
			if(identification.getProcurementSystem()!=null && identification.getProcurementSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProcurementSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Reporting System", ampContext)){
			columnName=TranslatorWorker.translateText("Reporting System",request);
			columnVal="";
			catVal = null;
			if(identification.getReportingSystem()!=null && identification.getReportingSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getReportingSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Audit System", ampContext)){
			columnName=TranslatorWorker.translateText("Audit System",request);
			columnVal="";
			catVal = null;
			if(identification.getAuditSystem()!=null && identification.getAuditSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAuditSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Institutions", ampContext)){
			columnName=TranslatorWorker.translateText("Institutions",request);
			columnVal="";
			catVal = null;
			if(identification.getInstitutions()!=null && identification.getInstitutions() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getInstitutions());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category", ampContext)){
			columnName=TranslatorWorker.translateText("Project Category",request);
			columnVal="";
			catVal = null;
			if(identification.getProjectCategory()!=null && identification.getProjectCategory() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectCategory());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Agreement Number", ampContext)){
			columnName=TranslatorWorker.translateText("Government Agreement Number",request);
			columnVal="";
			catVal = null;
			if(identification.getGovAgreementNumber()!=null){
				columnVal	= identification.getGovAgreementNumber();
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Budget", ampContext)){
			cell = new RtfCell();
			cell.setBorder(0);
			if (identification.getBudgetCV()!=null) {
				if(identification.getBudgetCV().equals(identification.getBudgetCVOn())){
					p1=new Paragraph(TranslatorWorker.translateText("Activity is on budget",request),PLAINFONT) ;
				}else if (identification.getBudgetCV().equals(identification.getBudgetCVOff())){
					p1=new Paragraph(TranslatorWorker.translateText("Activity is off budget",request),PLAINFONT) ;
				}else if (identification.getBudgetCV().equals(new Long(0))) {
					p1=new Paragraph(TranslatorWorker.translateText("Budget Unallocated",request),PLAINFONT) ;
				}else{
					p1=new Paragraph(TranslatorWorker.translateText("Activity is on ",request)+identification.getBudgetCV(),PLAINFONT) ;
				}
			}
			cell.add(p1);
			
			if (identification.getChapterForPreview() !=null){
				cell.add(new Paragraph(TranslatorWorker.translateText("Code Chapitre",request)+": ",PLAINFONT));
				cell.add(new Paragraph(identification.getChapterForPreview().getCode() + " - " 
						+ identification.getChapterForPreview().getDescription(),BOLDFONT));
				cell.add(new Paragraph(TranslatorWorker.translateText("Imputations",request)+": ",PLAINFONT));
				for (AmpImputation imputation : identification.getChapterForPreview().getImputations()) {
					cell.add(new Paragraph(identification.getChapterForPreview().getYear()+" - " +
							imputation.getCode() +" - "+ imputation.getDescription(),BOLDFONT));
				}
			}
			
			identificationSubTable1.addCell(cell);			
			cell=new RtfCell();
			cell.setBorder(0);
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras", ampContext)){
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/FY", ampContext)){
				columnName=TranslatorWorker.translateText("FY",request);
				generateOverAllTableRows(identificationSubTable1,columnName,identification.getFY(),null);
			}
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Vote", ampContext)){
				columnName=TranslatorWorker.translateText("Vote",request);
				generateOverAllTableRows(identificationSubTable1,columnName,identification.getVote(),null);
			}
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Vote", ampContext)){
				columnName=TranslatorWorker.translateText("Sub-Vote",request);
				generateOverAllTableRows(identificationSubTable1,columnName,identification.getSubVote(),null);
			}
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Program", ampContext)){
				columnName=TranslatorWorker.translateText("Sub-Program",request);
				generateOverAllTableRows(identificationSubTable1,columnName,identification.getSubProgram(),null);
			}
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Project Code", ampContext)){
				columnName=TranslatorWorker.translateText("Project Code",request);
				generateOverAllTableRows(identificationSubTable1,columnName,identification.getProjectCode(),null);
			}
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Classification", ampContext)){
			cell = new RtfCell();
			cell.setColspan(2);
			cell.setBorder(0);
			cell.add(new Paragraph(TranslatorWorker.translateText("Budget Classification",request)+": ",PLAINFONT));
			if(identification.getSelectedbudgedsector() !=null){
				for (AmpBudgetSector budgSect : identification.getBudgetsectors()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgSect.getCode()+" - "+ budgSect.getSectorname(),BOLDFONT));
				}
			}
			
			if(identification.getSelectedorg() !=null ){
				for (AmpOrganisation budgOrg : identification.getBudgetorgs()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgOrg.getBudgetOrgCode()+" - "+ budgOrg.getName(),BOLDFONT));
				}
			}
			
			if(identification.getSelecteddepartment() !=null ){
				for (AmpDepartments budgDept : identification.getBudgetdepartments()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgDept.getCode()+" - "+ budgDept.getName(),BOLDFONT));
				}
			}
			
			if(identification.getSelectedprogram() !=null ){
				for (AmpTheme budgprog : identification.getBudgetprograms()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgprog.getThemeCode()+" - "+ budgprog.getName(),BOLDFONT));
				}
			}
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleField("Organizations and Project ID", ampContext)){
			cell = new RtfCell();
			cell.setBorder(0);
			cell.add(new Paragraph(TranslatorWorker.translateText("Organizations and Project ID",request)+": ",PLAINFONT));
			if(identification.getSelectedOrganizations()!=null){
				for (OrgProjectId selectedOrgForPopup : identification.getSelectedOrganizations()) {
					if(selectedOrgForPopup!=null && selectedOrgForPopup.getOrganisation()!=null){
						cell.add(new Paragraph(BULLET_SYMBOL + "["+selectedOrgForPopup.getOrganisation().getName()+"]",PLAINFONT));
					}
				}
			}			
			
			identificationSubTable1.addCell(cell);
			cell=new RtfCell();
			cell.setBorder(0);
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Humanitarian Aid", ampContext)){
			cell = new RtfCell();
			cell.setColspan(2);
			cell.setBorder(0);
			cell.add(new Paragraph(TranslatorWorker.translateText("Humanitarian Aid",request)+": ",PLAINFONT));
			
			columnVal="";
			if(identification.getHumanitarianAid()!=null && identification.getHumanitarianAid()){
				columnVal="Yes";
			}else if(identification.getHumanitarianAid()!=null && ! identification.getHumanitarianAid()){
				columnVal="No";
			}
			cell.add(new Paragraph(columnVal,BOLDFONT));
			identificationSubTable1.addCell(cell);
		}
	}

	private void applyEmptyCell(Table identificationSubTable1) {
		applyEmptyCell(identificationSubTable1, 2);		
	}
	
	private void applyEmptyCell(Table identificationSubTable1,int colspan) {
		RtfCell lineCell = new RtfCell();
		lineCell.setColspan(colspan);		
		lineCell.add(new Paragraph("_____________________________________________________________________________________",new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.GRAY)));
		identificationSubTable1.addCell(lineCell);
	}

	private Table createOverallInformationTable(HttpServletRequest request,	EditActivityForm myForm, ServletContext ampContext, String currency)throws BadElementException, Exception, WorkerException {
		String columnVal;
		Table overAllTable = new Table(3); //overall table contains 3 subtables: funding informaiton, activity creation information , all amounts are in {curr] information
		overAllTable.setWidth(100);
		overAllTable.setBorder(0);	            
		overAllTable.setWidths(new float[]{2f,2f,1f});
		overAllTable.setBackgroundColor(CELLCOLORGRAY);
		overAllTable.setPadding(0);
		
		//1st cell of the overAll Table
		int rowAmountForCell1 = 0;
		RtfCell overallFundingCell=new RtfCell(); 
		overallFundingCell.setBackgroundColor(CELLCOLORGRAY);
		overallFundingCell.setBorder(0);	            
		overallFundingCell.setVerticalAlignment(Element.ALIGN_TOP);
		Table overAllFundingSubTable = new Table(2);
		overAllFundingSubTable.setWidth(100);
		overAllFundingSubTable.setBorder(0);
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Commitments", ampContext)){
			if(myForm.getFunding().getTotalCommitments()!=null && myForm.getFunding().getTotalCommitments().length()>0){
				columnVal =  myForm.getFunding().getTotalCommitments() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Commitment",request)+": ",columnVal,CELLCOLORGRAY );
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedCommitments()!=null && myForm.getFunding().getTotalPlannedCommitments().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedCommitments() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Commitment",request)+": ",columnVal,CELLCOLORGRAY );
			rowAmountForCell1++;	    		
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Disbursements", ampContext)){
			if(myForm.getFunding().getTotalDisbursements()!=null && myForm.getFunding().getTotalDisbursements().length()>0){
				columnVal =  myForm.getFunding().getTotalDisbursements() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Distributements",request)+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedDisbursements()!=null && myForm.getFunding().getTotalPlannedDisbursements().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedDisbursements()  +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Distributements",request)+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Item/Expenditures", ampContext)){
			if(myForm.getFunding().getTotalExpenditures()!=null && myForm.getFunding().getTotalExpenditures().length()>0){
				columnVal =  myForm.getFunding().getTotalExpenditures() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Expenditures",request)+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedExpenditures()!=null && myForm.getFunding().getTotalPlannedExpenditures().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedExpenditures() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Expenditures",request)+": ",columnVal ,CELLCOLORGRAY);	
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleField("Duration of Project", ampContext)){
			if(myForm.getPlanning().getProjectPeriod()!=null){
				columnVal =  myForm.getPlanning().getProjectPeriod().toString();
			}else{
				columnVal = "";
			}
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Duration of project",request)+": ",columnVal,CELLCOLORGRAY);
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleField("Delivery rate", ampContext)){
			if(myForm.getFunding().getDeliveryRate()!=null){
				columnVal =  myForm.getFunding().getDeliveryRate();
			}else{
				columnVal = "";
			}
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Delivery rate",request)+": ",columnVal,CELLCOLORGRAY);
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleField("Consumption rate", ampContext)){
			if(myForm.getFunding().getConsumptionRate()!=null){
				columnVal =  myForm.getFunding().getConsumptionRate();
			}else{
				columnVal = "";
			}
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Consumption rate",request)+": ",columnVal,CELLCOLORGRAY);	
			rowAmountForCell1++;
		}	            
		overallFundingCell.add(overAllFundingSubTable);	            
		overAllTable.addCell(overallFundingCell);
		
		//second cell of overall table is additional info
		int rowAmountForCell2 = 5; //there are 5 rows in additional information
		RtfCell additionalInfoCell=new RtfCell();	            
		additionalInfoCell.setBackgroundColor(CELLCOLORGRAY);
		additionalInfoCell.setBorder(0);
		additionalInfoCell.setVerticalAlignment(Element.ALIGN_TOP);
		Table additionalInfoSubTable = new Table(2);
		additionalInfoSubTable.setWidth(100);
		
		columnVal = "";
		if(identification.getActAthFirstName()!=null){
			columnVal +=  identification.getActAthFirstName();
		}
		if(identification.getActAthLastName()!=null){
			columnVal += " "+ identification.getActAthLastName();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Activity created by",request)+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedBy() != null)
		{
		    if(identification.getCreatedBy().getAmpTeam().getName()!=null){
				columnVal +=  identification.getCreatedBy().getAmpTeam().getName();
			}
			if(identification.getCreatedBy().getAmpTeam().getAccessType()!=null){
				columnVal += "-"+identification.getCreatedBy().getAmpTeam().getAccessType();
			}
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Workspace of creator",request)+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedBy() != null)
		{
			if(identification.getCreatedBy().getAmpTeam().getComputation()){
				columnVal += TranslatorWorker.translateText("yes",request);
			}else{
				columnVal += TranslatorWorker.translateText("no",request);
			}
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Computation",request)+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedDate()!=null){
			columnVal += identification.getCreatedDate();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Activity created on",request)+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getTeam().getTeamLead()!=null){
			columnVal += identification.getTeam().getTeamLead().getUser().getFirstNames();
			columnVal += identification.getTeam().getTeamLead().getUser().getLastName();
			columnVal += identification.getTeam().getTeamLead().getUser().getEmail();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Data Team Leader",request)+": ",columnVal,CELLCOLORGRAY);
		
		int emptyRowsAmount = rowAmountForCell1>=rowAmountForCell2?(rowAmountForCell1 - rowAmountForCell2):(rowAmountForCell2 - rowAmountForCell1);
		for (int i=0;i<emptyRowsAmount;i++){
			generateOverAllTableRows(additionalInfoSubTable,"","",CELLCOLORGRAY);
		}

		additionalInfoCell.add(additionalInfoSubTable);
		overAllTable.addCell(additionalInfoCell);
		
		//3rd cell is for currency
		RtfCell currencyInfoCell=new RtfCell();	            
		currencyInfoCell.setBorder(0);
		Table currencyInfoSubTable = new Table(1);
		currencyInfoSubTable.setWidth(100);
		RtfCell mycell = new RtfCell(new Paragraph(TranslatorWorker.translateText("All amounts are in ",request) + currency, PLAINFONT));
		mycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		mycell.setVerticalAlignment(Element.ALIGN_TOP);
		mycell.setBackgroundColor(CELLCOLORGRAY);
		mycell.setRowspan(rowAmountForCell1>rowAmountForCell2?rowAmountForCell1:rowAmountForCell2);
		currencyInfoSubTable.addCell(mycell);
		
		currencyInfoCell.add(currencyInfoSubTable);	            
		overAllTable.addCell(currencyInfoCell);
		return overAllTable;
	}
	
	private void generateOverAllTableRows (Table table,String fieldName,String fieldValue,Color bgColor) throws Exception {		
		RtfCell cell=new RtfCell();
		Paragraph p1=new Paragraph(fieldName,PLAINFONT);
		cell.addElement(p1);
		if(bgColor !=null){
			cell.setBackgroundColor(bgColor);
		}
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorder(0);
		table.addCell(cell);		    		
		cell=new RtfCell();
		p1=new Paragraph(fieldValue,BOLDFONT);
		cell.addElement(p1);
		if(bgColor !=null){
			cell.setBackgroundColor(bgColor);
		}
		cell.setBorder(0);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);
	}
	
	private void generateOverAllTableRows (Table table,String fieldName,Table fieldValue,Color bgColor) throws Exception {
		RtfCell cell=new RtfCell();
		Paragraph p1=new Paragraph(fieldName,PLAINFONT);
		cell.addElement(p1);	    		
		cell.setBackgroundColor(bgColor);
		cell.setBorder(0);
		table.addCell(cell);		    		
		cell=new RtfCell();		
		cell.addElement(fieldValue);
		cell.setBackgroundColor(bgColor);
		cell.setBorder(0);
		table.addCell(cell);
	}
	
	private String  getEditTagValue(HttpServletRequest request,String editKey) throws Exception{
		Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site.getSiteId(),editKey,RequestUtils.getNavigationLanguage(request).getCode());
        if (editorBody == null) {
        	editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site.getSiteId(),editKey,SiteUtils.getDefaultLanguages(site).getCode());
        }
        return editorBody;
	}
	
	//cuts <p> and </p> tags from editTag value
	private String processEditTagValue(HttpServletRequest request,String editTagKey) throws Exception {
		String result=getEditTagValue(request,editTagKey);
		return processHtml(request, result);
	}
	
	private String processHtml(HttpServletRequest request,String text) throws Exception {
		
		if(text!=null && text.indexOf("<![endif]-->") != -1){
			text = text.substring(text.lastIndexOf("<![endif]-->")+"<![endif]-->".length()); 
		}
		
		if(text!=null){
			String formatterPrefix = "<![endif]-->";  //some records contain wordpress tags in comments,which need to be filtered
			if(text.indexOf(formatterPrefix) != -1){
				text = text.substring(text.lastIndexOf(formatterPrefix)+formatterPrefix.length());				
			}
			while (text.contains("<!--")){ //remove possible comments 
				String text1 = text.substring(0,text.indexOf("<!--"));
				String text2 = text.substring(text.lastIndexOf("-->")+("-->").length()+1);
				text = text1 + text2;
			}
			
			String[] possibleTags = {"span","table","tr","td","tbody","p","style","ul","li","div"};
			
			for (int i = 0; i < possibleTags.length; i++) { //remove all possible tags
				String tag = possibleTags[i];
				
				String startTagStr = "<"+tag;
				String endTagStr = "</"+tag+">";
				
				int endTagLength = endTagStr.length();
				
				
				while(text.contains(startTagStr)){
					
					int firstIndexOfStartTag = text.indexOf(startTagStr);
					int beginIndex = text.indexOf(">", firstIndexOfStartTag)+1;
					int firstIndexOfEndTag = text.indexOf(endTagStr, beginIndex);
					
					String text1 = text.substring(0,firstIndexOfStartTag);
					String text2 = text.substring(beginIndex, firstIndexOfEndTag);
					String text3 = text.length()==firstIndexOfEndTag + endTagLength? "" : text.substring(firstIndexOfEndTag + endTagLength);
					text = text1 + text2 + text3;
				}
			}
						
			text = text.replaceAll("\\<.*?>","");
			text = text.replaceAll("&lt;", "<");
			text = text.replaceAll("&gt;",">");
			text = text.replaceAll("&amp;","&");
			text = text.replaceAll("&rsquo;","'");
			text = text.replaceAll("\\r","");
		}
		
		return ExportActivityToPdfUtil.unhtmlentities(text);
	}	

	private String buildProgramsOutput(List<AmpActivityProgram> programs) {
		String result="";
		for (AmpActivityProgram pr :programs) {			
			result+=pr.getHierarchyNames()+" \t\t"+pr.getProgramPercentage()+"% \n";
		}
		return result;
	}

    private class ExportSectionHelper {
        private String secTitle;
        private boolean translateTitle;
        private float width = 100f;
        private String align = "left";
        List <ExportSectionHelperRowData> rowData;

        public ExportSectionHelper(String secTitle, boolean translateTitle) {
            this.secTitle = secTitle;
            this.translateTitle = translateTitle;
            rowData = new ArrayList<ExportSectionHelperRowData>();
        }

        public ExportSectionHelper(String secTitle) {
            this(secTitle, false);
        }

        public String getSecTitle() {
            return secTitle;
        }

        public boolean isTranslateTitle() {
            return translateTitle;
        }

        public List<ExportSectionHelperRowData> getRowData() {
            return rowData;
        }

        public void addRowData (ExportSectionHelperRowData data) {
            this.rowData.add(data);
        }

        public float getWidth() {
            return width;
        }

        public ExportSectionHelper setWidth(float width) {
            this.width = width;
            return this;
        }

        public String getAlign() {
            return align;
        }

        public ExportSectionHelper setAlign(String align) {
            this.align = align;
            return this;
        }
    }
    
    private class ExportSectionHelperRowData {
        private String title;
        private boolean translateTitle;
        private List <String> fieldOrderedList;
        private Map<String, String> fldVisibilityKeyMap;
        private List<String> values;
        private boolean separator;
        private Map<String, Boolean> translateValues;




        public ExportSectionHelperRowData (String title, List<String> fields, List<String> visibilityKeys, boolean translateTitle, boolean separator) {
            this.title = title;
            this.translateTitle = translateTitle;
            this.separator = separator;
            values = new ArrayList<String>();
            translateValues = new HashMap<String, Boolean>();
            fldVisibilityKeyMap = new HashMap<String, String>();
            fieldOrderedList = fields;
            if (fields != null && !fields.isEmpty() && visibilityKeys != null && !visibilityKeys.isEmpty()) {
                int count = 0;
                for (String fld : fields) {
                    fldVisibilityKeyMap.put(fld, visibilityKeys.get(count));
                    count ++;
                }
            }
        }

        public ExportSectionHelperRowData (String title, List<String> fields, List<String> visibilityKeys, boolean translateTitle) {
            this (title, fields, visibilityKeys, translateTitle, false);
        }

        public ExportSectionHelperRowData (String title) {
            this(title, null, null, false);
        }

        public ExportSectionHelperRowData addRowData(String data, Boolean translate) {
            values.add(data);
            translateValues.put(data, translate);
            return this;
        }

        public ExportSectionHelperRowData addRowData(String data) {
            return addRowData(data, false);
        }

        public String getTitle() {
            return title;
        }

        public boolean isTranslateTitle() {
            return translateTitle;
        }

        public List<String> getValues() {
            return values;
        }

        public boolean isSeparator() {
            return separator;
        }

        public ExportSectionHelperRowData setSeparator(boolean separator) {
            this.separator = separator;
            return this;
        }

        public Map<String, Boolean> getTranslateValues() {
            return translateValues;
        }

        public Map<String, String> getFldVisibilityKeyMap() {
            return fldVisibilityKeyMap;
        }

        public List<String> getFieldOrderedList() {
            return fieldOrderedList;
        }

    }

    private Table createSectionTable(ExportSectionHelper esh, HttpServletRequest request, ServletContext ampContext) throws BadElementException, WorkerException {
        Table retVal = null;
        int maxCols = 0;
        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            //Calculate visible row count
            int visibleRowCount = 0;
            if (rd.getValues() != null && !rd.getValues().isEmpty()) {
                if (rd.getFieldOrderedList() != null) {
                    for (String fldTitle : rd.getFieldOrderedList()) { 
                        String visKey = rd.getFldVisibilityKeyMap().get(fldTitle);
                        if(FeaturesUtil.isVisibleField(visKey, ampContext)) {
                            visibleRowCount ++;
                        }
                    }
                } else {  //If no visibility keys
                    visibleRowCount = rd.getValues().size();
                }
            }

            if (visibleRowCount > maxCols) {
                maxCols = rd.getValues().size();
            }
        }

        maxCols++; //first col will be the row title by default

        retVal = new Table(maxCols);
        retVal.setWidth(esh.getWidth());
        retVal.setAlignment(esh.getAlign());

        if (esh.getSecTitle() != null && esh.getSecTitle().trim().length() > 0) {
            String secTitle = esh.translateTitle ?
                                    TranslatorWorker.translateText(esh.getSecTitle(),request) : esh.getSecTitle();
            RtfCell titleCell = new RtfCell(new Paragraph(secTitle, HEADERFONT));
            titleCell.setColspan(maxCols);
            titleCell.setBackgroundColor(CELLCOLORGRAY);
            retVal.addCell(titleCell);
        }



        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            RtfCell dataTitleCell = null;

            if (!rd.isSeparator()) {
                String title = (rd.translateTitle && rd.title != null) ?
                        TranslatorWorker.translateText(rd.title,request) : rd.title;


                
                dataTitleCell = new RtfCell(new Paragraph(title, BOLDFONT));
                dataTitleCell.setBackgroundColor(CELLCOLORGRAY);

                if ((rd.getValues() == null || rd.getValues().isEmpty()) && maxCols > 1) {
                    dataTitleCell.setColspan(maxCols);
                }
            } else {
                dataTitleCell = new RtfCell(new Paragraph("_____________________________________________________________________________________"));
                dataTitleCell.setColspan(maxCols);
            }


            retVal.addCell(dataTitleCell);

            int rowCounter = 1;
            int idx = 0;
            for(String rowData : rd.getValues()) {
                String visKey = null;
                if (rd.getFieldOrderedList() != null) {
                    String colTitle = rd.getFieldOrderedList().get(idx);
                    visKey = rd.getFldVisibilityKeyMap().get(colTitle);
                }

                idx ++;

                if(visKey == null || (visKey != null && FeaturesUtil.isVisibleField(visKey, ampContext))) {
                    String trnVal = (rowData !=null && rd.getTranslateValues().containsKey(rowData)) ?
                            TranslatorWorker.translateText(rowData,request) : rowData;
                    RtfCell dataValCell = new RtfCell(new Paragraph(trnVal != null ? trnVal : "-", PLAINFONT));
                    if (rd.getValues().size() < (maxCols - 1) && rowCounter == rd.getValues().size()) {
                        dataValCell.setColspan(maxCols - rowCounter);
                    }
                    retVal.addCell(dataValCell);
                    rowCounter ++;
                }
            }



        }


        return retVal;
    }
    
    private static String getTransactionTypeLable (int type) {
        String retVal = null;
        switch (type) {
            case Constants.COMMITMENT:
                retVal = "Commitment";
                break;

            case Constants.DISBURSEMENT:
                retVal = "Disbursement";
                break;

            case Constants.EXPENDITURE:
                retVal = "Expenditure";
                break;

            case Constants.DISBURSEMENT_ORDER:
                retVal = "Disbursement Order";
                break;

            case Constants.MTEFPROJECTION:
                retVal = "MTEF Projection";
                break;

        }
        return retVal;
    }

    private static String getContactTypeLable (String type) {
        String retVal = null;
        if (type.equals(Constants.DONOR_CONTACT)) {
            retVal = "Donor funding contact";
        } else if (type.equals(Constants.MOFED_CONTACT)) {
            retVal = "MOFED contact";
        } else if (type.equals(Constants.SECTOR_MINISTRY_CONTACT)) {
            retVal = "Project Coordinator Contact";
        } else if (type.equals(Constants.PROJECT_COORDINATOR_CONTACT)) {
            retVal = "Sector Ministry Contact";
        } else if (type.equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)) {
            retVal = "Implementing/Executing Agency Contact";
        }

        return retVal;
    }
    
    private Map<String, Map<String, Set<AmpFundingDetail>>> getStructuredFundings (Set<AmpFundingDetail> fndDets) {
        Map<String, Map<String, Set<AmpFundingDetail>>> retVal = new HashMap<String, Map<String, Set<AmpFundingDetail>>>();
        for (AmpFundingDetail fndDet : fndDets) {
            String transactionType = getTransactionTypeLable(fndDet.getTransactionType());
            if (!retVal.containsKey(transactionType)) {
                retVal.put(transactionType, new HashMap<String, Set<AmpFundingDetail>>());
            }

            if (!retVal.get(transactionType).containsKey(fndDet.getAdjustmentType().getLabel())) {
                retVal.get(transactionType).put(fndDet.getAdjustmentType().getLabel(), new HashSet<AmpFundingDetail>());
            }
            retVal.get(transactionType).get(fndDet.getAdjustmentType().getLabel()).add(fndDet);
        }
        return retVal;
    }


}
