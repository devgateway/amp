/**
 * 
 */
package org.digijava.module.aim.action;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.GregorianBasedWorker;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.editor.exception.EditorException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfFont;

/**
 * @author mihai
 * 
 * 
 */
public class ProjectFicheExport extends Action {
	protected static Logger logger = Logger.getLogger(ProjectFicheExport.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		try {
		response.setContentType("application/rtf");
		response.setHeader("Content-Disposition",
				"attachment; filename=ProjectFiche.rtf");
		
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		Locale defaultLanguages = SiteUtils.getDefaultLanguages(site);
		
		Long id=new Long(request.getParameter("ampActivityId"));
		TeamMember tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		
		AmpActivityVersion act = ActivityUtil.loadActivity(id);
		String currencyName = CurrencyUtil.getCurrencyName(tm.getAppSettings().getCurrencyId());
		
		HashMap allComments=new HashMap();		
		Collection ampFields=DbUtil.getAmpFields();
		for(Iterator itAux=ampFields.iterator(); itAux.hasNext();)
		{
			AmpField field = (AmpField) itAux.next();
			ArrayList colAux= DbUtil.getAllCommentsByField(field.getAmpFieldId(),act.getAmpActivityId());
			allComments.put(field.getFieldName(),colAux);
		}
		
		
		Document document = new Document();
		RtfWriter2.getInstance(document,
			response.getOutputStream());
		
		document.open();
		
		//title of doc:
		RtfFont titleFont = new RtfFont("Arial", 13);
		titleFont.setStyle(RtfFont.BOLD); 
		Paragraph p = new Paragraph("Standard Summary Project Fiche - IPA Centralised National and CDB Programmes",titleFont);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
				

		RtfFont rootSectionFont = getSectionFont(false);
		RtfFont subSectionFont = getSectionFont(true);
		RtfFont regularFont=getRegularFont();
		
		//title of activity
		RtfFont title2Font = new RtfFont("Arial", 12);
		title2Font.setStyle(RtfFont.BOLD | RtfFont.UNDERLINE);
		p = new Paragraph(act.getName(),title2Font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		
		document.add(new Paragraph());

		//1. BASIC INFORMATION
		document.add(newParagraph("1. Basic information",rootSectionFont,1));
		
		//document.add(newParagraph("Background:",subSectionFont,1));
		document.add(newParagraph("1.1 CRIS Number: "+act.getCrisNumber(),regularFont,1));
		document.add(newParagraph("1.2 Title: "+act.getName(),regularFont,1));
		document.add(newParagraph("1.3 Sector: "+Util.toCSStringForIN(act.getSectors()),regularFont,1));
		document.add(newParagraph("1.4 Location: ",regularFont,1));
		Iterator i=act.getLocations().iterator();
		while (i.hasNext()) {
			AmpActivityLocation element = (AmpActivityLocation) i.next();
			document.add(newParagraph(element.getLocation().toString(),regularFont,2));	
		}
		
		document.add(newParagraph("Implementing Arrangements:",subSectionFont,1));
		document.add(newParagraph("1.5 Contracting Authority (EC)",regularFont,1));
		document.add(newParagraph("1.6 Implementing Agency: "+Util.toCSString(Util.getOrgsByRole(act.getOrgrole(),"IA")),regularFont,1));
		document.add(newParagraph("1.7 Beneficiary: "+Util.toCSString(Util.getOrgsByRole(act.getOrgrole(),"BA")),regularFont,1));
		//	
		
		document.add(newParagraph("1.8 Overall Cost: "+CurrencyUtil.df.format(convertToThousands(EUActivityUtil.getTotalCostConverted(act.getCosts(), tm.getAppSettings().getCurrencyId()).doubleValue()))+" "+currencyName,regularFont,1));
		document.add(newParagraph("1.9 EU Contribution: "+CurrencyUtil.df.format(convertToThousands(EUActivityUtil.getTotalContributionsConverted(act.getCosts(), tm.getAppSettings().getCurrencyId()).doubleValue()))+" "+currencyName,regularFont,1));
		document.add(newParagraph("1.10 Final date for contracting: "+(act.getContractingDate()==null?"":act.getContractingDate().toString()),regularFont,1));
		//TODO: check here, this is same as 1.12!!
		document.add(newParagraph("1.11 Final date for execution of contracts: "+(act.getDisbursmentsDate()==null?"":act.getDisbursmentsDate().toString()),regularFont,1));
		document.add(newParagraph("1.12 Final date for disbursements: "+(act.getDisbursmentsDate()==null?"":act.getDisbursmentsDate().toString()),regularFont,1));

		//2. OVERALL OBJECTIVE
		document.add(newParagraph("2. Overall Objective and Project Purpose",rootSectionFont,1));

		document.add(newParagraph("2.1 Overall Objective: "+getEditorBody(site,act.getObjective(),navigationLanguage),regularFont,1));
		document.add(newParagraph("2.2 Project Purpose: "+getEditorBody(site,act.getPurpose(),navigationLanguage),regularFont,1));
		document.add(newParagraph("2.3 Link with AP/NPAA/EP/SAA: ",regularFont,1));
		document.add(newParagraph("2.4 Link with MIPD: ",regularFont,1));
		document.add(newParagraph("2.5 Link with National Development Plan (where applicable): "+Util.toCSString(ActivityUtil.getActivityPrograms(act.getAmpActivityId())),regularFont,1));
		document.add(newParagraph("2.6 Link with national/ sectoral investment plans (where applicable): ",regularFont,1));
		

		//2. DESCRIPTION OF PROJECT
		document.add(newParagraph("3. Description of Project",rootSectionFont,1));
		
		document.add(newParagraph("3.1 Background and justification: "+getEditorBody(site,act.getDescription(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.2 Assessment of Project Impact, Catalytic Effect, Sustainability and Cross Border Impact (where applicable) : "+getEditorBody(site,act.getProjectImpact(),navigationLanguage),regularFont,1));		
		document.add(newParagraph("3.3 Results and measurable indicators: "+getEditorBody(site,act.getResults(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.4 Activities: "+getEditorBody(site,act.getActivitySummary(),navigationLanguage),regularFont,1));
		
		/*
		i=act.getCosts().iterator();
		int k=0;
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			document.add(newParagraph("3.4."+(++k)+" "+element.getName(),regularFont,2));
			document.add(newParagraph("- ID: "+element.getTextId(),regularFont,3));
			document.add(newParagraph("- Total cost: "+CurrencyUtil.df.format(element.getTotalCost())+" "+element.getTotalCostCurrency().getCurrencyCode(),regularFont,3));			
			document.add(newParagraph("- Inputs: "+element.getInputs(),regularFont,3));
			document.add(newParagraph("- Assumptions: "+element.getAssumptions(),regularFont,3));
			document.add(newParagraph("- Progress: "+element.getProgress(),regularFont,3));

			Table tbl=new Table(5);
			Cell c = new Cell("Contribution amount");
			c.setHeader(true);
			tbl.addCell(c);
			c=new Cell("Currency");
			c.setHeader(true);
			tbl.addCell(c);
			c=new Cell("Donor");
			c.setHeader(true);
			tbl.addCell(c);
			c=new Cell("Financing Type");
			c.setHeader(true);
			tbl.addCell(c);
			c=new Cell("Financing Instrument");
			c.setHeader(true);
			tbl.addCell(c);		
			tbl.endHeaders();
			Iterator ii=element.getContributions().iterator();
			while (ii.hasNext()) {
				EUActivityContribution contr = (EUActivityContribution) ii.next();
				tbl.addCell(new Cell(CurrencyUtil.df.format(contr.getAmount())));
				tbl.addCell(new Cell(contr.getAmountCurrency().getCurrencyCode()));
				tbl.addCell(new Cell(contr.getDonor().getName()));
				tbl.addCell(new Cell(contr.getFinancingTypeCategVal().getValue()));
				tbl.addCell(new Cell(contr.getFinancingInstr().getValue()));
			}
			document.add(tbl);
			
		}
		*/

		document.add(newParagraph("3.6 Conditionality and sequencing: "+getEditorBody(site,act.getCondSeq(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.7 Linked activities: "+getEditorBody(site,act.getLinkedActivities(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.8 Lessons learned: "+getEditorBody(site,act.getLessonsLearned(),navigationLanguage),regularFont,1));
		
		document.add(newParagraph("4. Indicative Budget (amounts in ME)",rootSectionFont,1));
		
		Table tbl = new Table(15);
		tbl.setTableFitsPage(true);
		
		final int tableFontSize = 7; //constant
		RtfFont tableFont = getRegularFont();
		tableFont.setStyle(RtfFont.UNDERLINE);
		tableFont.setSize(tableFontSize);

		Cell c = new Cell(newParagraph("Activities", tableFont, 0));
		c.setRowspan(3);
		c.setColspan(2);
		tbl.addCell(c);
		c = new Cell(" ");
		c.setColspan(2);
		tbl.addCell(c);
		c = new Cell(newParagraph("SOURCES OF FUNDING", tableFont, 0));
		c.setColspan(11);
		tbl.addCell(c);
		c = new Cell(newParagraph("TOTAL COST  (Million Euro)", tableFont, 0));
		c.setColspan(2);
		c.setRowspan(2);
		tbl.addCell(c);
		c = new Cell(newParagraph("EU CONTRIBUTION", tableFont, 0));
		c.setColspan(4);
		tbl.addCell(c);
		c = new Cell(newParagraph("NATIONAL PUBLIC CONTRIBUTION", tableFont, 0));
		c.setColspan(5);
		tbl.addCell(c);
		c = new Cell(newParagraph("PRIVATE", tableFont, 0));
		c.setColspan(2);
		tbl.addCell(c);
		
		c = new Cell(newParagraph("Total", tableFont, 0));
		c = new Cell(newParagraph("%*", tableFont, 0));
		tbl.addCell(c);
		tbl.addCell(c);
		c = new Cell(newParagraph("IB", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("INV", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Total", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("%*", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Central", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Regional", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("IFIs", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Total", tableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("%*", tableFont, 0));
		tbl.addCell(c);
		
		AmpCategoryClass actType = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_ACTIVITY_TYPE_KEY);
		AmpCategoryClass ctrType = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_TYPE_KEY);
		
		List contracts = ActivityUtil.getIPAContracts(act.getAmpActivityId());
		final double grayLevel1 = 0.7;
		final double grayLevel2 = 0.8;
		
		final class TableHelper {
			Double totalCost = new Double(0);

			Double euTotal = new Double(0);
			Double euPercent = new Double(0);
			Double euIB = new Double(0);
			Double euINV = new Double(0);
			
			Double nationalTotal = new Double(0);
			Double nationalPercent = new Double(0);
			Double nationalCentral = new Double(0);
			Double nationalRegional = new Double(0);
			Double nationalIFIs = new Double(0);

			Double privateTotal = new Double(0);
			Double privatePercent = new Double(0);
			
			public void magic(){
				euTotal = euIB + euINV;
				nationalTotal = nationalCentral + nationalRegional + nationalIFIs;
				
				totalCost = euTotal + nationalTotal + privateTotal;
				
				euPercent = euTotal*100.00/totalCost;
				nationalPercent = nationalTotal*100.00/totalCost;
				privatePercent = privateTotal*100.00/totalCost;
			}
			
			public void add(TableHelper x){
				euIB += x.euIB;
				euINV += x.euINV;
				
				nationalCentral += x.nationalCentral;
				nationalIFIs += x.nationalIFIs;
				nationalRegional += x.nationalRegional;
				
				privateTotal += x.privateTotal;
				
				this.magic();
			}
			
			
			public void printMe(String name, RtfFont tableFont, Table tbl, boolean grayOut) throws BadElementException{
				Cell c = new Cell(newParagraph(name, tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				c.setColspan(2);
				tbl.addCell(c);
			    
			    RtfFont boldTableFont = getRegularFont();
				boldTableFont.setStyle(RtfFont.BOLD);
				boldTableFont.setSize(tableFontSize);
				c = new Cell(newParagraph(formatNumber(totalCost), boldTableFont, 0));
				c.setColspan(2);
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(euTotal), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(euPercent), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(euIB), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(euINV), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				
				c = new Cell(newParagraph(formatNumber(nationalTotal), boldTableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(nationalPercent), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(nationalCentral), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(nationalRegional), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(nationalIFIs), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);

				c = new Cell(newParagraph(formatNumber(privateTotal), boldTableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
				c = new Cell(newParagraph(formatNumber(privatePercent), tableFont, 0));
				if (grayOut)
					c.setGrayFill((float)grayLevel1);
				tbl.addCell(c);
			}
		};
		
		RtfFont tableFont2 = getRegularFont();
		tableFont2.setSize(tableFontSize);
		Iterator ait = actType.getPossibleValues().iterator();
		while (ait.hasNext()) {
			AmpCategoryValue aval = (AmpCategoryValue) ait.next();
			
			TableHelper actLine = new TableHelper();
			HashMap contractLines = new HashMap(); 

			Iterator ctypeIt = ctrType.getPossibleValues().iterator();
			while (ctypeIt.hasNext()) {
				AmpCategoryValue cval = (AmpCategoryValue) ctypeIt.next();
				
				Iterator contractIt = contracts.iterator();
				boolean found = false;
				TableHelper th = new TableHelper();
				while (contractIt.hasNext()) {
					IPAContract ipaContr = (IPAContract) contractIt.next();
					
					if (ipaContr.getContractType().equals(cval) && ipaContr.getType().equals(aval)){
						if (!found){ 
							contractLines.put(cval.getId(), th);
							found = true;
						}
						if (ipaContr.getTotalECContribIBAmount() != null)
							th.euIB += ipaContr.getTotalECContribIBAmount();
						if (ipaContr.getTotalECContribINVAmount() != null)
							th.euINV += ipaContr.getTotalECContribINVAmount();
						
						if (ipaContr.getTotalNationalContribCentralAmount() != null)
							th.nationalCentral += ipaContr.getTotalNationalContribCentralAmount();
						if (ipaContr.getTotalNationalContribIFIAmount() != null)
							th.nationalIFIs += ipaContr.getTotalNationalContribIFIAmount();
						if (ipaContr.getTotalNationalContribRegionalAmount() != null)
							th.nationalRegional += ipaContr.getTotalNationalContribRegionalAmount();
						
						if (ipaContr.getTotalPrivateContribAmount() != null)
							th.privateTotal += ipaContr.getTotalPrivateContribAmount();
					}
					
				}
				th.magic();
				actLine.add(th);
			}
			if (contractLines.size() != 0){
				actLine.printMe(aval.getValue(), tableFont2, tbl, true);
				ctypeIt = ctrType.getPossibleValues().iterator();
				while (ctypeIt.hasNext()) {
					AmpCategoryValue cval = (AmpCategoryValue) ctypeIt.next();
					if (contractLines.containsKey(cval.getId())){
						TableHelper tth = (TableHelper) contractLines.get(cval.getId());
						tth.printMe("     " + cval.getValue(), tableFont2, tbl, false);
					}
				}
			}
			
		}
		
		
		document.add(tbl);

		RtfFont boldTableFont = getRegularFont();
		boldTableFont.setStyle(RtfFont.BOLD);
		boldTableFont.setSize(tableFontSize);
		
		document.add(newParagraph("Note: Depending on the type of supplies required, contracts may be split into more than 1 contract.",tableFont,1));
		document.add(newParagraph("* - expressed in % of Total Cost for each Activity", boldTableFont,1));

		
		document.add(newParagraph("5. Indicative Implementation Schedule (periods broken down per quarter)",rootSectionFont,1));
		

		tbl = new Table(5);
		tbl.setTableFitsPage(true);
		
		c = new Cell(newParagraph("Contracts", boldTableFont, 0));
		c.setColspan(2);
		tbl.addCell(c);
		c = new Cell(newParagraph("Start of Tendering", boldTableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Signature of Contract", boldTableFont, 0));
		tbl.addCell(c);
		c = new Cell(newParagraph("Project Completion", boldTableFont, 0));
		tbl.addCell(c);
		GregorianBasedWorker worker=new GregorianBasedWorker();
		Iterator it = contracts.iterator();
		while (it.hasNext()) {
			IPAContract contract = (IPAContract) it.next();
			
			c = new Cell(newParagraph(contract.getContractName(), tableFont2, 0));
			c.setColspan(2);
			tbl.addCell(c);
			
			Date date = contract.getStartOfTendering();
			worker.setTime(date);
			if (date != null){
				int quarter = worker.getQuarter();
				c = new Cell(newParagraph("Q"+String.valueOf(quarter)+" "+worker.getYear(), tableFont2, 0));
			}
			else
				c = new Cell(newParagraph("", tableFont2, 0));
			tbl.addCell(c);
			
			date = contract.getSignatureOfContract();
			worker.setTime(date);
			if (date != null){
				
				int quarter = worker.getYear();
				c = new Cell(newParagraph("Q"+String.valueOf(quarter)+" "+worker.getYear(), tableFont2, 0));
			}
			else
				c = new Cell(newParagraph("", tableFont2, 0));
			tbl.addCell(c);
			
			date = contract.getContractCompletion();
			
			 worker.setTime(date);
			
			if (date != null){
				int quarter = worker.getQuarter();
				c = new Cell(newParagraph("Q"+String.valueOf(quarter)+" "+String.valueOf(worker.getYear()), tableFont2, 0));
			}
			else
				c = new Cell(newParagraph("", tableFont2, 0));
			tbl.addCell(c);
		}

		document.add(tbl);
		
		document.add(newParagraph("6. Cross Cutting Issues (where applicable)",rootSectionFont,1));
		document.add(newParagraph("6.1 Equal Opportunity"+getEditorBody(site,act.getEqualOpportunity(),navigationLanguage),regularFont,1));
		document.add(newParagraph("6.2 Environment"+getEditorBody(site,act.getEnvironment(),navigationLanguage),regularFont,1));
		document.add(newParagraph("6.3 Minorities"+getEditorBody(site,act.getMinorities(),navigationLanguage),regularFont,1));
		
		
		//LOGFRAME
		RtfFont annexFont = getRegularFont();
		annexFont.setStyle(RtfFont.BOLD);
		document.add(newParagraph("ANNEXES",rootSectionFont,1));
		document.add(newParagraph("1 - \t Log Frame in Standard Format",annexFont,1));
		document.add(newParagraph("2 - \t Amounts Contracted and Disbursed per Quarter over the full duration of Programme",annexFont,1));
		document.add(newParagraph("3 - \t References to laws, regulations and Strategic Documents:",annexFont,1));
		document.add(newParagraph("    \t\t Reference list of relevant laws and regulations",annexFont,1));
		document.add(newParagraph("    \t\t Reference to AP /NPAA / EP / SAA",annexFont,1));
		document.add(newParagraph("    \t\t Reference to MIPD",annexFont,1));
		document.add(newParagraph("    \t\t Reference to National Development Plan",annexFont,1));
		document.add(newParagraph("    \t\t Reference to National/Sectoral Investment Plans",annexFont,1));
		document.add(newParagraph("4 - \t Details per EU Funded Contract",annexFont,1));
		
		//document.add(newParagraph("ANNEX 1: Logical framework matrix in standard format",rootSectionFont,0));
		
		document.add(newParagraph("ANNEX 1: Logical Framework Matrix",annexFont,1));
		
		
		tbl=new Table(4);
		tbl.setTableFitsPage(true);
		
		
		c=(getLogframeHeadingCell("Logframe Planning Matrix for Project Fiche"));
		c.setVerticalAlignment(Cell.ALIGN_CENTER);
		
		c.setColspan(4);
		tbl.addCell(c);
		c=new Cell("Program ID:");
		tbl.addCell(c);
		c=new Cell(act.getAmpId());
		c.setColspan(3);
		tbl.addCell(c);
		c=new Cell("Program Name:");
		tbl.addCell(c);
		c=new Cell(act.getName());
		c.setColspan(3);
		tbl.addCell(c);
		c=new Cell("Contract Expiration:");
		tbl.addCell(c);
		if(act.getActualCompletionDate()!=null)
		c=new Cell(act.getActualCompletionDate().toString()); else c=new Cell("");
		c.setColspan(3);
		tbl.addCell(c);
		
		
		
		
		List<ActivityIndicator> indicatorsMe=IndicatorUtil.getActivityIndicatorHelperBeans(act.getAmpActivityId());

		
		//fische objectives:
		addIndicatorsLine(allComments,tbl,getEditorBody(site,act.getObjective(),navigationLanguage), CategoryConstants.LOGFRAME_OBJECTIVE ,indicatorsMe);
		//fische purpose:
		addIndicatorsLine(allComments,tbl,getEditorBody(site,act.getPurpose(),navigationLanguage), CategoryConstants.LOGFRAME_PURPOSE,indicatorsMe);	
		//fische results:
		addIndicatorsLine(allComments,tbl,getEditorBody(site,act.getResults(),navigationLanguage),CategoryConstants.LOGFRAME_RESULTS,indicatorsMe);
		
		tbl.addCell(getLogframeHeadingCell("Activities"));
		tbl.addCell(getLogframeHeadingCell("Contributions"));
		tbl.addCell(getLogframeHeadingCell("Costs"));
		tbl.addCell(getLogframeHeadingCell("Assumptions"));
		
		i=act.getCosts().iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(tm.getAppSettings().getCurrencyId());
			tbl.addCell(new Cell(element.getName()));
			tbl.addCell(new Cell(CurrencyUtil.df.format(convertToThousands(element.getTotalContributionsConverted()))+" "+currencyName));
			tbl.addCell(new Cell(CurrencyUtil.df.format(convertToThousands(element.getTotalCostConverted()))+" "+currencyName));
			tbl.addCell(new Cell(element.getAssumptions()));
		}
		
		document.add(tbl);
		
		
		document.add(newParagraph("ANNEX 2: Amounts Contracted and Disbursed per Quarter over the full duration of Programme",annexFont,1));
		
		Iterator cit = contracts.iterator();
		Date lowDate = null, highDate = null;
		while (cit.hasNext()) {
			IPAContract contract = (IPAContract) cit.next();
			if (lowDate == null && highDate == null) {
				lowDate = highDate = contract.getTotalECContribIBAmountDate();
			}
			Date date = contract.getTotalECContribIBAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			date = contract.getTotalECContribINVAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			
			date = contract.getTotalNationalContribCentralAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			date = contract.getTotalNationalContribIFIAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			date = contract.getTotalNationalContribRegionalAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			
			date = contract.getTotalPrivateContribAmountDate();
			if (date != null){
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
			
			Iterator dit = contract.getDisbursements().iterator();
			while (dit.hasNext()) {
				IPAContractDisbursement disb = (IPAContractDisbursement) dit.next();
				
				date = disb.getDate();
				lowDate = (lowDate.compareTo(date) < 0 )?lowDate:date;
				highDate = (highDate.compareTo(date) > 0 )?highDate:date;
			}
		}
		if (lowDate!=null && highDate!=null ){
		worker.setTime(lowDate);
		//calendarHelper.setTime(new java.sql.Date(lowDate.getYear(), lowDate.getMonth(), lowDate.getDay()));
		int lowDateQuarter = worker.getQuarter();
		//calendarHelper.setTime(new java.sql.Date(highDate.getYear(), highDate.getMonth(), highDate.getDay()));
		worker.setTime(highDate);
		int highDateQuarter = worker.getQuarter();
		
		int noOfQuarters;
		
		if (highDate.getYear() != lowDate.getYear())
			noOfQuarters = (highDate.getYear() - lowDate.getYear())*4 + (5 - lowDateQuarter) + (highDateQuarter);
		else
			noOfQuarters = (highDateQuarter + 1 - lowDateQuarter);
		
		
		tbl=new Table(noOfQuarters + 2);
		
		tbl.setTableFitsPage(true);
		
		
		tableFont = getRegularFont();
		tableFont.setSize(tableFontSize);
		tableFont.setStyle(RtfFont.STYLE_BOLD);

		tableFont2 = getRegularFont();
		tableFont2.setSize(tableFontSize);
		
		c=new Cell(newParagraph("Contracted", tableFont, 0));
		c.setColspan(2);
		c.setGrayFill((float)grayLevel1);
		tbl.addCell(c);
		
		int currentQuarter = lowDateQuarter;
		int currentYear = lowDate.getYear() + 1900; 
		
		for (int k = 0; k < noOfQuarters; k++){
			c=new Cell(newParagraph(String.valueOf(currentYear) + " Q" + currentQuarter, tableFont, 0));
			tbl.addCell(c);
			currentQuarter++;
			if (currentQuarter == 5){
				currentYear++;
				currentQuarter = 1;
			}
		}
		
		final int noOfStaticAmounts = 6;
		
		Double[] quarterAmounts = new Double[noOfQuarters];
		for (int h = 0; h < noOfQuarters; h++)
			quarterAmounts[h] = new Double(0);
		Double[] totalQuarterAmounts = new Double[noOfQuarters];
		for (int h = 0; h < noOfQuarters; h++)
			totalQuarterAmounts[h] = new Double(0);
		

		cit = contracts.iterator();
		while (cit.hasNext()) {
			IPAContract contract = (IPAContract) cit.next();
			for (int h = 0; h < noOfQuarters; h++)
				quarterAmounts[h] = new Double(0);
			 
			
			Double[] staticAmounts = new Double[noOfStaticAmounts];
			Date[] staticDates = new Date[noOfStaticAmounts];
			staticAmounts[0] = contract.getTotalECContribIBAmount();
			staticDates[0] = contract.getTotalECContribIBAmountDate();
			staticAmounts[1] = contract.getTotalECContribINVAmount();
			staticDates[1] = contract.getTotalECContribINVAmountDate();
			staticAmounts[2] = contract.getTotalNationalContribCentralAmount();
			staticDates[2] = contract.getTotalNationalContribCentralAmountDate();
			staticAmounts[3] = contract.getTotalNationalContribIFIAmount();
			staticDates[3] = contract.getTotalNationalContribIFIAmountDate();
			staticAmounts[4] = contract.getTotalNationalContribRegionalAmount();
			staticDates[4] = contract.getTotalNationalContribRegionalAmountDate();
			staticAmounts[5] = contract.getTotalPrivateContribAmount();
			staticDates[5] = contract.getTotalPrivateContribAmountDate();
			
			for (int j = 0; j < noOfStaticAmounts; j++){
				if (staticAmounts[j] != null){
					if (staticDates[j] != null){
						worker.setTime(staticDates[j]);
						//calendarHelper.setTime(new java.sql.Date(staticDates[j].getYear(), staticDates[j].getMonth(), staticDates[j].getDay()));
						int quarter = worker.getQuarter();
						int position = 4*(staticDates[j].getYear() - lowDate.getYear()) + quarter - lowDateQuarter;
						quarterAmounts[position] += staticAmounts[j];
					}
				}
				
			}
			
			c = new Cell(newParagraph(contract.getContractName(), tableFont2, 0));
			c.setColspan(2);
			tbl.addCell(c);
			
			for (int j = 0; j < noOfQuarters; j++){
				c = new Cell(newParagraph(formatNumber(quarterAmounts[j]), tableFont2, 0));
				tbl.addCell(c);
				totalQuarterAmounts[j] += quarterAmounts[j];
			}
		}
		
		c = new Cell(newParagraph("Cumulative", tableFont, 0));
		c.setColspan(2);
		c.setGrayFill((float)grayLevel2);
		tbl.addCell(c);
		
		for (int j = 0; j < noOfQuarters; j++){
			c = new Cell(newParagraph(formatNumber(totalQuarterAmounts[j]), tableFont, 0));
			c.setGrayFill((float)grayLevel2);
			tbl.addCell(c);
		}
		//part2

		c=new Cell(newParagraph("Disbursed", tableFont, 0));
		c.setColspan(2);
		c.setGrayFill((float)grayLevel1);
		tbl.addCell(c);
		for (int k = 0; k < noOfQuarters; k++){
			c=new Cell(newParagraph(String.valueOf(""), tableFont, 0));
			tbl.addCell(c);
		}

		
		for (int h = 0; h < noOfQuarters; h++)
			totalQuarterAmounts[h] = new Double(0);

		cit = contracts.iterator();
		while (cit.hasNext()) {
			IPAContract contract = (IPAContract) cit.next();
			for (int h = 0; h < noOfQuarters; h++)
				quarterAmounts[h] = new Double(0);
			
			Iterator dit = contract.getDisbursements().iterator();
			while (dit.hasNext()) {
				IPAContractDisbursement disb = (IPAContractDisbursement) dit.next();
				
				Double amount = disb.getAmount();
				Date date = disb.getDate();
				worker.setTime(date);
				if (amount != null){
					if (date != null){
						worker.setTime(new java.sql.Date(date.getYear(), date.getMonth(), date.getDay()));
						int quarter = worker.getQuarter();
						int position = 4*(date.getYear() - lowDate.getYear()) + quarter - lowDateQuarter;
						quarterAmounts[position] += amount;
					}
				}
			}
			c = new Cell(newParagraph(contract.getContractName(), tableFont2, 0));
			c.setColspan(2);
			tbl.addCell(c);
			
			for (int j = 0; j < noOfQuarters; j++){
				c = new Cell(newParagraph(formatNumber(quarterAmounts[j]), tableFont2, 0));
				tbl.addCell(c);
				totalQuarterAmounts[j] += quarterAmounts[j];
			}
		}
		c = new Cell(newParagraph("Cumulative", tableFont, 0));
		c.setColspan(2);
		c.setGrayFill((float)grayLevel2);
		tbl.addCell(c);
		
		for (int j = 0; j < noOfQuarters; j++){
			c = new Cell(newParagraph(formatNumber(totalQuarterAmounts[j]), tableFont, 0));
			c.setGrayFill((float)grayLevel2);
			tbl.addCell(c);
		}
		document.add(tbl);
		}
		
		
		document.add(newParagraph("ANNEX 3: References to laws, regulations and Strategic Documents",annexFont,1));
		Collection<AmpActivityReferenceDoc> refDocs = ActivityUtil.getReferenceDocumentsFor(act.getAmpActivityId());
		if(refDocs !=null) {
			i = refDocs.iterator();
			while (i.hasNext()) {
				AmpActivityReferenceDoc element = (AmpActivityReferenceDoc) i.next();
				document.add(newParagraph(" - Reference to "+element.getCategoryValue()+" :"+element.getComment(),regularFont,1));
			}
		}

		document.add(newParagraph("ANNEX 4: Details per EU Funded Contract",annexFont,1));
		
		document.add(newParagraph(getEditorBody(site,act.getContractDetails(),navigationLanguage),regularFont,1));
		
		document.close();
		
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
				
		return null;

	}
	

	public void addIndicatorsLine(Map allComments,Table tbl,String text, HardCodedCategoryValue hcValue, Collection indicatorsMe) throws BadElementException {
		Cell c=null;

		c=getLogframeHeadingCell( hcValue.getValueKey() );
		tbl.addCell(c);
		c=getLogframeHeadingCell("Indicators");
		tbl.addCell(c);
		c=getLogframeHeadingCell("Verification");
		tbl.addCell(c);
		c=hcValue.equals(CategoryConstants.LOGFRAME_OBJECTIVE)?new Cell(""):getLogframeHeadingCell("Assumptions");
		tbl.addCell(c);

		//fische objectives:
		c=new Cell(text);
		tbl.addCell(c);

		c=new Cell("");
		Iterator i=indicatorsMe.iterator();
		while (i.hasNext()) {
			ActivityIndicator element = (ActivityIndicator) i.next();
			if(element.getIndicatorsCategory()==null || element.getIndicatorsCategory().getValue()==null ||
					CategoryManagerUtil.equalsCategoryValue(element.getIndicatorsCategory(), hcValue) )
						continue;
			c.addElement(new Paragraph(element.getIndicatorName()));
		}
		tbl.addCell(c);
		
	
		//verifcation space
		c=new Cell("");
		Collection verifications=(Collection) allComments.get(hcValue.getValueKey()+" Verification");
		i=verifications.iterator();
		while (i.hasNext()) {
			AmpComments element = (AmpComments) i.next();
			c.add(new Paragraph(element.getComment()));
		}		
		tbl.addCell(c);
		
		//assumptions space
		c=new Cell("");
		if(!hcValue.equals(CategoryConstants.LOGFRAME_OBJECTIVE)) {
			Collection assumptions=(Collection) allComments.get(hcValue.getValueKey()+" Assumption");
			i=assumptions.iterator();
			while (i.hasNext()) {
				AmpComments element = (AmpComments) i.next();
				c.add(new Paragraph(element.getComment()));
			}		
		}
		tbl.addCell(c);
		
		
	}
	

	private String formatNumber(double nr) {
		Double number;
		String result;
		if (nr == 0) {
			number = new Double(0);
		}
		else 
			number = new Double(nr);
	
		
		String format = "#0.0";
		DecimalFormat formater = new DecimalFormat(format);
		result = formater.format(number);
		return result;
	}

	public Cell getLogframeHeadingCell(String text) {
		Cell c=new Cell(text);
		c.setHeader(true);
		c.setBackgroundColor(Color.LIGHT_GRAY);
		return c;
	}
	
	public Paragraph newParagraph(String txt,RtfFont font,int indentTabs) {
		Paragraph p=new Paragraph(txt,font);
		p.setIndentationLeft(indentTabs*30);
		p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		p.setSpacingAfter(10);
		return p;
	}
	
	public RtfFont getSectionFont(boolean subSection) {
		RtfFont font = getRegularFont();
		font.setStyle(subSection?RtfFont.BOLD|RtfFont.UNDERLINE:RtfFont.BOLD);
		return font;
	}
	
	public RtfFont getRegularFont() {
		return new RtfFont("Times New Roman",12);
	}
	
	public static double convertToThousands(double amount) {
		return amount*1000;
	}
	
	/**
	 * This is just wrapper to hide 1) long package name 2) correctly converting siteID to string.
	 * About site ID: there are two ID's, 1) numeric PK of the record, and 2) string unique name of 
	 * the site also called siteId. People mess them frequently and this causes too much bad problems.  
	 * @param site
	 * @param key
	 * @param navLang
	 * @return
	 * @throws EditorException 
	 */
	private static String getEditorBody(Site site,String key,  Locale navLang) throws EditorException{
		String result = org.digijava.module.editor.util.DbUtil.getEditorBodyFiltered(site, key, navLang.getCode());
		return result;
	}
}
