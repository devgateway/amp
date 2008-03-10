/**
 * 
 */
package org.digijava.module.aim.action;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

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
		
		AmpActivity act = ActivityUtil.getAmpActivity(id);
		
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
		RtfFont titleFont = new RtfFont("Arial", 14);
		titleFont.setStyle(RtfFont.BOLD | RtfFont.UNDERLINE); 
		Paragraph p = new Paragraph("Standard Summary Project Fiche",titleFont);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
				

		RtfFont rootSectionFont = getSectionFont(false);
		RtfFont subSectionFont = getSectionFont(true);
		RtfFont regularFont=getRegularFont();
		
		//title of activity
		RtfFont title2Font = getRegularFont();
		title2Font.setStyle(RtfFont.BOLD);
		p = new Paragraph(act.getName(),title2Font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		
		document.add(new Paragraph());

		//1. BASIC INFORMATION
		document.add(newParagraph("1. Basic information",rootSectionFont,0));
		
		document.add(newParagraph("Background:",subSectionFont,1));
		document.add(newParagraph("1.1 CRIS Number: "+act.getAmpId(),regularFont,1));
		document.add(newParagraph("1.2 Title: "+act.getName(),regularFont,1));
		document.add(newParagraph("1.3 Sector: "+Util.toCSString(act.getSectors(),false),regularFont,1));
		document.add(newParagraph("1.4 Location: ",regularFont,1));
		Iterator i=act.getLocations().iterator();
		while (i.hasNext()) {
			AmpLocation element = (AmpLocation) i.next();
			document.add(newParagraph(element.toString(),regularFont,2));	
		}
		
		document.add(newParagraph("Implementing arrangements:",subSectionFont,1));
		document.add(newParagraph("1.5 Contracting Authority (EC)",regularFont,1));
		document.add(newParagraph("1.6 Implementing Agency: "+Util.toCSString(Util.getOrgsByRole(act.getOrgrole(),"IA"),false),regularFont,1));
		document.add(newParagraph("1.7 Beneficiary: "+Util.toCSString(Util.getOrgsByRole(act.getOrgrole(),"BA"),false),regularFont,1));
		
		document.add(newParagraph("Costs:",subSectionFont,1));
		document.add(newParagraph("1.8 Overall Cost: "+CurrencyUtil.df.format(EUActivityUtil.getTotalCostConverted(act.getCosts(), tm.getAppSettings().getCurrencyId()).doubleValue()),regularFont,1));
		document.add(newParagraph("1.9 EU Contribution: "+CurrencyUtil.df.format(EUActivityUtil.getTotalContributionsConverted(act.getCosts(), tm.getAppSettings().getCurrencyId()).doubleValue()),regularFont,1));
		document.add(newParagraph("1.10 Final date for contracting: "+(act.getContractingDate()==null?"":act.getContractingDate().toString()),regularFont,1));
		//TODO: check here, this is same as 1.12!!
		//document.add(newParagraph("1.11 Final date for execution of contracts: "+(act.getDisbursmentsDate()==null?"":act.getDisbursmentsDate().toString()),regularFont,1));
		document.add(newParagraph("1.11 Final date for disbursements: "+(act.getDisbursmentsDate()==null?"":act.getDisbursmentsDate().toString()),regularFont,1));

		//2. OVERALL OBJECTIVE
		document.add(newParagraph("2. Overall Objective and Project Purpose",rootSectionFont,0));

		document.add(newParagraph("2.1 Overall Objective: "+Util.getEditorBody(site,act.getObjective(),navigationLanguage),regularFont,1));
		document.add(newParagraph("2.2 Project Purpose: "+Util.getEditorBody(site,act.getPurpose(),navigationLanguage),regularFont,1));
		document.add(newParagraph("2.3 Link with AP/NPAA/EP/SAA: ",regularFont,1));
		document.add(newParagraph("2.4 Link with MIPD: ",regularFont,1));
		document.add(newParagraph("2.5 Link with National Development Plan (where applicable): "+Util.toCSString(act.getActivityPrograms(),false),regularFont,1));
		document.add(newParagraph("2.6 Link with national/ sectoral investment plans (where applicable): ",regularFont,1));
		

		//2. DESCRIPTION OF PROJECT
		document.add(newParagraph("3. Description of Project",rootSectionFont,0));
		
		document.add(newParagraph("3.1 Background and justification: "+Util.getEditorBody(site,act.getDescription(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.2 Assessment of project impact, catalytic effect, sustainability and cross border impact (where applicable) : ",regularFont,1));		
		document.add(newParagraph("3.3 Results and measurable indicators: "+Util.getEditorBody(site,act.getResults(),navigationLanguage),regularFont,1));
		document.add(newParagraph("3.4 Activities: ",regularFont,1));
		
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

		document.add(newParagraph("3.5 Conditionality and sequencing: ",regularFont,1));
		document.add(newParagraph("3.6 Linked activities: ",regularFont,1));
		document.add(newParagraph("3.7 Lessons learned: ",regularFont,1));
		
		//LOGFRAME
		
		document.add(newParagraph("ANNEX 1: Logical framework matrix in standard format",rootSectionFont,0));
		
		Table tbl=new Table(4);
		tbl.setTableFitsPage(true);
		
		
		Cell c=(getLogframeHeadingCell("Logframe Planning Matrix for Project Fiche"));
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
		
		
		
		Collection indicatorsMe=MEIndicatorsUtil.getActivityIndicators(act.getAmpActivityId());

		
		//fische objectives:
		addIndicatorsLine(allComments,tbl,Util.getEditorBody(site,act.getObjective(),navigationLanguage),"Objective",indicatorsMe);
		//fische purpose:
		addIndicatorsLine(allComments,tbl,Util.getEditorBody(site,act.getPurpose(),navigationLanguage),"Purpose",indicatorsMe);	
		//fische results:
		addIndicatorsLine(allComments,tbl,Util.getEditorBody(site,act.getResults(),navigationLanguage),"Results",indicatorsMe);
		
		tbl.addCell(getLogframeHeadingCell("Activities"));
		tbl.addCell(getLogframeHeadingCell("Contributions"));
		tbl.addCell(getLogframeHeadingCell("Costs"));
		tbl.addCell(getLogframeHeadingCell("Assumptions"));
		
		i=act.getCosts().iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(tm.getAppSettings().getCurrencyId());
			tbl.addCell(new Cell(element.getName()));
			tbl.addCell(new Cell(CurrencyUtil.df.format(element.getTotalContributionsConverted())));
			tbl.addCell(new Cell(CurrencyUtil.df.format(element.getTotalCostConverted())));
			tbl.addCell(new Cell(element.getAssumptions()));
		}
		
		
		
		
		document.add(tbl);
			
		document.close();
		
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
				
		return null;

	}
	

	public void addIndicatorsLine(Map allComments,Table tbl,String text, String category, Collection indicatorsMe) throws BadElementException {
		Cell c=null;

		c=getLogframeHeadingCell(category);
		tbl.addCell(c);
		c=getLogframeHeadingCell("Indicators");
		tbl.addCell(c);
		c=getLogframeHeadingCell("Verification");
		tbl.addCell(c);
		c=category.equals("Objective")?new Cell("Amounts are in 000s"):getLogframeHeadingCell("Assumptions");
		tbl.addCell(c);

		//fische objectives:
		c=new Cell(text);
		tbl.addCell(c);

		c=new Cell("");
		Iterator i=indicatorsMe.iterator();
		while (i.hasNext()) {
			ActivityIndicator element = (ActivityIndicator) i.next();
			if(element.getIndicatorsCategory()==null || element.getIndicatorsCategory().getValue()==null || element.getIndicatorsCategory().getValue().equals(category)) continue;
			c.addElement(new Paragraph(element.getIndicatorName()));
		}
		tbl.addCell(c);
		
	
		//verifcation space
		c=new Cell("");
		Collection verifications=(Collection) allComments.get(category+" Verification");
		i=verifications.iterator();
		while (i.hasNext()) {
			AmpComments element = (AmpComments) i.next();
			c.add(new Paragraph(element.getComment()));
		}		
		tbl.addCell(c);
		
		//assumptions space
		c=new Cell("");
		if(!category.equals("Objective")) {
			Collection assumptions=(Collection) allComments.get(category+" Assumption");
			i=assumptions.iterator();
			while (i.hasNext()) {
				AmpComments element = (AmpComments) i.next();
				c.add(new Paragraph(element.getComment()));
			}		
		}
		tbl.addCell(c);
		
		
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
	
	
}
