package org.digijava.module.aim.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.CategoryCustomField;
import org.digijava.module.aim.helper.ChartGenerator;
import org.digijava.module.aim.helper.ChartParams;
import org.digijava.module.aim.helper.CheckCustomField;
import org.digijava.module.aim.helper.ComboBoxCustomField;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DateCustomField;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettings;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RadioOptionCustomField;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

public class ExportActivityToPDF extends Action {
	
	private static Logger logger = Logger.getLogger(ExportActivityToPDF.class);	
	private Long siteId;
	private String locale;
	private com.lowagie.text.Font plainFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11,Font.NORMAL);
	private com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11,Font.BOLD);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		EditActivityForm myForm=(EditActivityForm)form;
		Long actId=null;
		AmpActivity activity=null;
		if(request.getParameter("activityid")!=null){
			actId=new Long(request.getParameter("activityid"));
		}
		
		response.setContentType("application/pdf");
		Document document = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();
		PdfPTable mainLayout = new PdfPTable(2);
        mainLayout.setWidthPercentage(100);
		mainLayout.setWidths(new float[]{1f,2f});
		PdfPTableEvents event = new PdfPTableEvents();
		mainLayout.setTableEvent(event);
		mainLayout.getDefaultCell().setBorder(0);
        try {
			activity=ActivityUtil.loadActivity(actId);
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
					
			siteId = site.getId();
			locale = navigationLanguage.getCode();
		} catch (Exception e) {
			logger.error(e);
		}
		//building  table
		if(activity!=null){
			Paragraph p1=null;
			//heading cell
			PdfPCell titleCell=new PdfPCell();
			com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11, Font.BOLD, new Color(255, 255, 255));
			p1=new Paragraph(TranslatorWorker.translateText("Activity Details",locale,siteId),headerFont);
			p1.setAlignment(Element.ALIGN_CENTER);
			titleCell.addElement(p1);
			titleCell.setColspan(2);
			titleCell.setBackgroundColor(new Color(0,102,153));
			mainLayout.addCell(titleCell);			
			//activity name cells
			PdfPCell nameCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Activity Name",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			nameCell1.addElement(p1);			
			nameCell1.setBackgroundColor(new Color(244,244,242));
			nameCell1.setBorder(0);
			mainLayout.addCell(nameCell1);
			
			PdfPCell nameCell2=new PdfPCell();
			p1=new Paragraph(activity.getName(),plainFont);
			nameCell2.addElement(p1);
			nameCell2.setBorder(0);			
			mainLayout.addCell(nameCell2);
			
			//project comments
			PdfPCell projCommentsCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Project Comments",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			projCommentsCell1.addElement(p1);
			projCommentsCell1.setBackgroundColor(new Color(244,244,242));
			projCommentsCell1.setBorder(0);
			mainLayout.addCell(projCommentsCell1);
			
			PdfPCell projCommentsCell2=new PdfPCell();
			String projectComments = processEditTagValue(request, activity.getProjectComments());
			p1=new Paragraph(projectComments,plainFont);
			projCommentsCell2.addElement(p1);
			projCommentsCell2.setBorder(0);
			mainLayout.addCell(projCommentsCell2);
			
			//objective
			PdfPCell objectiveCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Objectives",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			objectiveCell1.addElement(p1);
			objectiveCell1.setBackgroundColor(new Color(244,244,242));
			objectiveCell1.setBorder(0);			
			mainLayout.addCell(objectiveCell1);
			
			PdfPCell objectiveCell2=new PdfPCell();
			String ongComments = processEditTagValue(request, activity.getObjective());
			p1=new Paragraph(ongComments,plainFont);
			objectiveCell2.addElement(p1);
			objectiveCell2.setBorder(0);
			mainLayout.addCell(objectiveCell2);
			
			
			//objective comments
			ArrayList<AmpComments> colAux	= null;
            Collection ampFields = DbUtil.getAmpFields();
            HashMap allComments = new HashMap();
            String objectiveComments="";
            
            if (ampFields!=null) {
            	for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                    AmpField field = (AmpField) itAux.next();
                    colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),actId);
                    allComments.put(field.getFieldName(), colAux);
                  }
            }
            
            PdfPTable objTable=new PdfPTable(2);
            objTable.getDefaultCell().setBorder(0);
            for (Object commentKey : allComments.keySet()) {            	
				String key=(String)commentKey;
				List<AmpComments> values=(List<AmpComments>)allComments.get(key);
				if(key.equalsIgnoreCase("Objective Assumption")){
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Assumption", locale, siteId)+" :",titleFont));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if(key.equalsIgnoreCase("Objective Verification")){
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Verification", locale, siteId)+" :",titleFont));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators")) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Objectively Verifiable Indicators", locale, siteId)+" :",titleFont));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));						
					}
				}
			}
            
            PdfPCell objectiveCommentsCell1=new PdfPCell();
            p1=new Paragraph(TranslatorWorker.translateText("Objective Comments",locale,siteId),titleFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
			objectiveCommentsCell1.addElement(p1);
			objectiveCommentsCell1.setBackgroundColor(new Color(244,244,242));
			objectiveCommentsCell1.setBorder(0);		
			mainLayout.addCell(objectiveCommentsCell1);
			
			PdfPCell objectiveCommentsCell2=new PdfPCell(objTable);
			objectiveCommentsCell2.setBorder(0);
			mainLayout.addCell(objectiveCommentsCell2);
			
			//AMPID cells
			PdfPCell ampIdCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("AMP ID",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			ampIdCell1.addElement(p1);
			ampIdCell1.setBackgroundColor(new Color(244,244,242));
			ampIdCell1.setBorder(0);
			mainLayout.addCell(ampIdCell1);
			
			PdfPCell ampIdCell2=new PdfPCell();
			p1=new Paragraph(activity.getAmpId(),plainFont);
			ampIdCell2.addElement(p1);
			ampIdCell2.setBorder(0);
			mainLayout.addCell(ampIdCell2);
			
			//contract Number
			PdfPCell contractNumCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Contract Number",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			contractNumCell1.addElement(p1);
			contractNumCell1.setBackgroundColor(new Color(244,244,242));
			contractNumCell1.setBorder(0);
			mainLayout.addCell(contractNumCell1);
			
			PdfPCell contractNumCell2=new PdfPCell();
			p1=new Paragraph(activity.getConvenioNumcont(),plainFont);
			contractNumCell2.addElement(p1);
			contractNumCell2.setBorder(0);
			mainLayout.addCell(contractNumCell2);
			
			//Description cell
			PdfPCell descCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Description",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			descCell1.addElement(p1);
			descCell1.setBackgroundColor(new Color(244,244,242));
			descCell1.setBorder(0);
			mainLayout.addCell(descCell1);
			
			PdfPCell descCell2=new PdfPCell();
			String actDesc = processEditTagValue(request, activity.getDescription());
			p1=new Paragraph(actDesc,plainFont);
			descCell2.addElement(p1);
			descCell2.setBorder(0);
			mainLayout.addCell(descCell2);
			
			//Purpose cell
			PdfPCell purposeCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Purpose",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			purposeCell1.addElement(p1);
			purposeCell1.setBackgroundColor(new Color(244,244,242));
			purposeCell1.setBorder(0);
			mainLayout.addCell(purposeCell1);
			
			PdfPCell purposeCell2=new PdfPCell();
			String actPurpose = processEditTagValue(request, activity.getPurpose());
			p1=new Paragraph(actPurpose,plainFont);
			purposeCell2.addElement(p1);
			purposeCell2.setBorder(0);
			mainLayout.addCell(purposeCell2);
			
			//purpose comments
			PdfPCell purposeCommentsCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Purpose Comments",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			purposeCommentsCell1.addElement(p1);
			purposeCommentsCell1.setBackgroundColor(new Color(244,244,242));
			purposeCommentsCell1.setBorder(0);
			mainLayout.addCell(purposeCommentsCell1);
			
			PdfPTable purposeTable=new PdfPTable(2);
            purposeTable.getDefaultCell().setBorder(0);
            for (Object commentKey : allComments.keySet()) {            	
				String key=(String)commentKey;
				List<AmpComments> values=(List<AmpComments>)allComments.get(key);
				if(key.equalsIgnoreCase("Purpose Assumption")){
					for (AmpComments value : values) {
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText("Purpose Assumption", locale, siteId)+" :",titleFont));
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if(key.equalsIgnoreCase("Purpose Verification")){
					for (AmpComments value : values) {
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText("Purpose Verification", locale, siteId)+" :",titleFont));
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if (key.equalsIgnoreCase("Purpose Objectively Verifiable Indicators")) {
					for (AmpComments value : values) {
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText("Purpose Objectively Verifiable Indicators", locale, siteId)+" :",titleFont));
						purposeTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));						
					}
				}
			}
			
			PdfPCell purposeCommentsCell2=new PdfPCell(purposeTable);
			purposeCommentsCell2.setBorder(0);
			mainLayout.addCell(purposeCommentsCell2);
			
			// results cell
			PdfPCell resultsCell1=new PdfPCell();			
			p1=new Paragraph(TranslatorWorker.translateText("Results",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			resultsCell1.addElement(p1);
			resultsCell1.setBackgroundColor(new Color(244,244,242));
			resultsCell1.setBorder(0);
			mainLayout.addCell(resultsCell1);
			
			PdfPCell resultsCell2=new PdfPCell();
			String actResults = processEditTagValue(request, activity.getResults());
			p1=new Paragraph(actResults,plainFont);
			resultsCell2.addElement(p1);
			resultsCell2.setBorder(0);
			mainLayout.addCell(resultsCell2);
			
			/**
			 *  Results Comments
			 */
			PdfPTable resultsCommentsTable=new PdfPTable(2);
            resultsCommentsTable.getDefaultCell().setBorder(0);
            for (Object commentKey : allComments.keySet()) {            	
				String key=(String)commentKey;
				List<AmpComments> values=(List<AmpComments>)allComments.get(key);
				if(key.equalsIgnoreCase("Results Assumption")){
					for (AmpComments value : values) {
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Assumption", locale, siteId)+" :",titleFont));
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if(key.equalsIgnoreCase("Results Verification")){
					for (AmpComments value : values) {
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Verification", locale, siteId)+" :",titleFont));
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
					}					
				}else if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")) {
					for (AmpComments value : values) {
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Objectively Verifiable Indicators", locale, siteId)+" :",titleFont));
						resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));						
					}
				}
			}
			PdfPCell resultsCommentsCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Results Comments",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			resultsCommentsCell1.addElement(p1);
			resultsCommentsCell1.setBackgroundColor(new Color(244,244,242));
			resultsCommentsCell1.setBorder(0);
			mainLayout.addCell(resultsCommentsCell1);
			
			PdfPCell resultsCommentsCell2=new PdfPCell(resultsCommentsTable);
			resultsCommentsCell2.setBorder(0);
			mainLayout.addCell(resultsCommentsCell2);
			
			/**
			 * Accession Instrument cell
			 */
			AmpCategoryValue catVal=null;
			if(myForm.getIdentification().getAccessionInstrument()!=null && myForm.getIdentification().getAccessionInstrument().longValue()>0){
				PdfPCell accessionInstrumentCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Accession Instrument",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				accessionInstrumentCell1.addElement(p1);
				accessionInstrumentCell1.setBackgroundColor(new Color(244,244,242));
				accessionInstrumentCell1.setBorder(0);
				mainLayout.addCell(accessionInstrumentCell1);
				
				PdfPCell accessionInstrumentCell2=new PdfPCell();
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getAccessionInstrument());
				if(catVal!=null){
					String translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					p1=new Paragraph(translatedValue,plainFont);
				}				
				accessionInstrumentCell2.addElement(p1);
				accessionInstrumentCell2.setBorder(0);
				mainLayout.addCell(accessionInstrumentCell2);
			}
			
			// A.C. Chapter cell			
			if(myForm.getIdentification().getAcChapter()!=null && myForm.getIdentification().getAcChapter().longValue()>0){
				PdfPCell chapterCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("A.C. Chapter",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				chapterCell1.addElement(p1);
				chapterCell1.setBackgroundColor(new Color(244,244,242));
				chapterCell1.setBorder(0);
				mainLayout.addCell(chapterCell1);
				
				PdfPCell chapterCell2=new PdfPCell();
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getAcChapter());
				if(catVal!=null){
					String translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					p1=new Paragraph(translatedValue,plainFont);
				}				
				chapterCell2.addElement(p1);
				chapterCell2.setBorder(0);
				mainLayout.addCell(chapterCell2);
			}
			
			//Cris Number
			PdfPCell crisNumberCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Cris Number",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			crisNumberCell1.addElement(p1);
			crisNumberCell1.setBackgroundColor(new Color(244,244,242));
			crisNumberCell1.setBorder(0);
			mainLayout.addCell(crisNumberCell1);
			
			PdfPCell crisNumberCell2=new PdfPCell();
			p1=new Paragraph(activity.getCrisNumber(),plainFont);
			crisNumberCell2.addElement(p1);
			crisNumberCell2.setBorder(0);
			mainLayout.addCell(crisNumberCell2);
			
			//Project Category			
			if(myForm.getIdentification().getProjectCategory()!=null && myForm.getIdentification().getProjectCategory().longValue()>0){
				PdfPCell projCatCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Project Category",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				projCatCell1.addElement(p1);
				projCatCell1.setBackgroundColor(new Color(244,244,242));
				projCatCell1.setBorder(0);
				mainLayout.addCell(projCatCell1);
				
				PdfPCell projCatCell2=new PdfPCell();
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getProjectCategory());
				if(catVal!=null){
					String translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					p1=new Paragraph(translatedValue,plainFont);
				}
				projCatCell2.addElement(p1);
				projCatCell2.setBorder(0);
				mainLayout.addCell(projCatCell2);
			}
			
			//Budget
			String budget="";
			if(activity.getBudget()!=null && ! activity.getBudget()){
				budget="Activity is Off Budget";
			}else if(activity.getBudget()!=null && ! activity.getBudget()){
				budget="Activity is On Budget";
			}
			
			if(budget.length()>0){
				PdfPCell budgetCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Budget",locale,siteId),titleFont);
				budgetCell1.addElement(p1);
				p1.setAlignment(Element.ALIGN_RIGHT);
				budgetCell1.setBackgroundColor(new Color(244,244,242));
				budgetCell1.setBorder(0);
				mainLayout.addCell(budgetCell1);
				
				PdfPCell budgetCell2=new PdfPCell();
				p1=new Paragraph(budget,plainFont);
				budgetCell2.addElement(p1);
				budgetCell2.setBorder(0);
				mainLayout.addCell(budgetCell2);
			}		
			
			/**
			 * Humanitarian Aid
			 */
			String value="";
			if(activity.getHumanitarianAid()!=null && activity.getHumanitarianAid()){
				value="Yes";
			}else if(activity.getHumanitarianAid()!=null && ! activity.getHumanitarianAid()){
				value="No";
			}
			
			if(value.length()>0){
				PdfPCell humAinCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Humanitarian Aid",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				humAinCell1.addElement(p1);
				humAinCell1.setBackgroundColor(new Color(244,244,242));
				humAinCell1.setBorder(0);
				mainLayout.addCell(humAinCell1);
				
				PdfPCell humAinCell2=new PdfPCell();
				p1=new Paragraph(value,plainFont);
				humAinCell2.addElement(p1);
				humAinCell2.setBorder(0);
				mainLayout.addCell(humAinCell2);
			}
			
			//Organizations and Project IDs			
			PdfPCell orgProjCell1=new PdfPCell();
        	p1=new Paragraph(TranslatorWorker.translateText("Organizations and Project IDs",locale,siteId),titleFont);
        	p1.setAlignment(Element.ALIGN_RIGHT);
			orgProjCell1.addElement(p1);
			orgProjCell1.setBackgroundColor(new Color(244,244,242));
			orgProjCell1.setBorder(0);
			mainLayout.addCell(orgProjCell1);
			
			com.lowagie.text.List orgsList=new com.lowagie.text.List(false,20);  //is not numbered list
			orgsList.setListSymbol(new Chunk("\u2022"));
			if(myForm.getIdentification().getSelectedOrganizations()!=null){
				for (OrgProjectId selectedOrgForPopup : myForm.getIdentification().getSelectedOrganizations()) {
					if(selectedOrgForPopup!=null && selectedOrgForPopup.getOrganisation()!=null){
						ListItem orgItem=new ListItem(new Phrase("["+selectedOrgForPopup.getOrganisation().getName()+"]",plainFont));
						orgsList.add(orgItem);
					}
				}
			}
			 
			PdfPCell orgProjCell2=new PdfPCell();			
			orgProjCell2.addElement(orgsList);
			orgProjCell2.setBorder(0);
			mainLayout.addCell(orgProjCell2);
            //Planning
        	PdfPCell planningCell1=new PdfPCell();
        	p1=new Paragraph(TranslatorWorker.translateText("Planning",locale,siteId),titleFont);
        	p1.setAlignment(Element.ALIGN_RIGHT);
			planningCell1.addElement(p1);
			planningCell1.setBackgroundColor(new Color(244,244,242));
			planningCell1.setBorder(0);
			mainLayout.addCell(planningCell1);
			
			String outputValue=TranslatorWorker.translateText("Line Ministry Rank", locale, siteId)+ "\t: ";
			if(activity.getLineMinRank()!=null){
				outputValue+=activity.getLineMinRank().intValue()+"\n";
			}else{
				outputValue+="\n";
			}
			outputValue+=TranslatorWorker.translateText("Ministry of Planning Rank", locale, siteId)+ "\t: ";
			if(activity.getPlanMinRank()!=null){
				outputValue+=activity.getPlanMinRank().intValue()+"\n";
			}else{
				outputValue+="\n";
			}
			outputValue+=TranslatorWorker.translateText("Proposed Approval Date ", locale, siteId)+ "\t: " + myForm.getPlanning().getOriginalAppDate()+"\n";
			outputValue+=TranslatorWorker.translateText("Actual Approval Date ", locale, siteId)+ "\t: " + myForm.getPlanning().getRevisedAppDate()+"\n";
			outputValue+=TranslatorWorker.translateText("Original Start Date ", locale, siteId)+ "\t: " + myForm.getPlanning().getOriginalStartDate()+"\n";
			outputValue+=TranslatorWorker.translateText("Final Date for Contracting ", locale, siteId)+ "\t: " + myForm.getPlanning().getContractingDate()+"\n";
			outputValue+=TranslatorWorker.translateText("Final Date for Disbursements ", locale, siteId)+ "\t: " + myForm.getPlanning().getDisbursementsDate()+"\n";
			outputValue+=TranslatorWorker.translateText("Actual Start Date ", locale, siteId)+ "\t: " +myForm.getPlanning().getRevisedStartDate() +"\n";
			outputValue+=TranslatorWorker.translateText("Proposed Completion Date ", locale, siteId)+ "\t: " +myForm.getPlanning().getProposedCompDate() +"\n";
			outputValue+=TranslatorWorker.translateText("Current Completion Date ", locale, siteId)+ "\t: " +myForm.getPlanning().getCurrentCompDate() +"\n";
			
			
			PdfPCell planningCell2=new PdfPCell();
			p1=new Paragraph(outputValue,plainFont);
			planningCell2.addElement(p1);
			planningCell2.setBorder(0);
			mainLayout.addCell(planningCell2);
			
			//status
			PdfPCell statusCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Status",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			statusCell1.addElement(p1);
			statusCell1.setBackgroundColor(new Color(244,244,242));
			statusCell1.setBorder(0);
			mainLayout.addCell(statusCell1);
			
			PdfPCell statusCell2=new PdfPCell();
			String translatedValue="";
			catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getPlanning().getStatusId());
			if(catVal!=null){
				translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
			}
			p1=new Paragraph(translatedValue+"\n"+myForm.getPlanning().getStatusReason(),plainFont);
			statusCell2.addElement(p1);
			statusCell2.setBorder(0);
			mainLayout.addCell(statusCell2);
			
			//References
			Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false, request);

        	if (catValues!=null){
        		PdfPCell referenceCell1=new PdfPCell();
        		p1=new Paragraph(TranslatorWorker.translateText("References",locale,siteId),titleFont);
        		p1.setAlignment(Element.ALIGN_RIGHT);
        		referenceCell1.addElement(p1);
        		referenceCell1.setBackgroundColor(new Color(244,244,242));
        		referenceCell1.setBorder(0);
    			mainLayout.addCell(referenceCell1);
    			
            	List<ReferenceDoc> refDocs=myForm.getDocuments().getReferenceDocs();
            	String output="";
        		if(refDocs!=null){
        			for (ReferenceDoc referenceDoc : refDocs) {
        				if(referenceDoc.getComment()!=null){
        					output+=referenceDoc.getCategoryValue()+"\n";
        				}        				
					}
        		}
        		
        		PdfPCell referenceCell2=new PdfPCell();
        		p1=new Paragraph(output,plainFont);
    			referenceCell2.addElement(p1);
    			referenceCell2.setBorder(0);
    			mainLayout.addCell(referenceCell2);
        	}
        	
        	//locations
        	if(myForm.getLocation().getSelectedLocs()!=null){
        		PdfPCell locationsCell1=new PdfPCell();
        		p1=new Paragraph(TranslatorWorker.translateText("Locations",locale,siteId),titleFont);
        		p1.setAlignment(Element.ALIGN_RIGHT);
    			locationsCell1.addElement(p1);
    			locationsCell1.setBackgroundColor(new Color(244,244,242));
    			locationsCell1.setBorder(0);
    			mainLayout.addCell(locationsCell1);
    			
    			String output="";
    			for (Location loc  : myForm.getLocation().getSelectedLocs()) {
					if(loc.getCountry()!=null && loc.getCountry().length()>0){
						output+="["+loc.getCountry()+"]";
					}
					if(loc.getRegion()!=null && loc.getRegion().length()>0){
						output+="["+loc.getRegion()+"]";
					}
					if(loc.getZone()!=null && loc.getZone().length()>0){
						output+="["+loc.getZone()+"]";
					}
					if(loc.getWoreda()!=null && loc.getWoreda().length()>0){
						output+="["+loc.getWoreda()+"]";
					}
					output+="\t["+loc.getPercent()+"]\n";
				}
    			
    			PdfPCell locationsCell2=new PdfPCell();
    			p1=new Paragraph(output,plainFont);
    			locationsCell2.addElement(p1);
    			locationsCell2.setBorder(0);
    			mainLayout.addCell(locationsCell2);
        	}
        	
        	//level
        	if(myForm.getLocation()!=null && myForm.getLocation().getLevelId()!=null && myForm.getLocation().getLevelId()>0){
        		PdfPCell levelCell1=new PdfPCell();
        		p1=new Paragraph(TranslatorWorker.translateText("Level",locale,siteId),titleFont);
        		p1.setAlignment(Element.ALIGN_RIGHT);
    			levelCell1.addElement(p1);
    			levelCell1.setBackgroundColor(new Color(244,244,242));
    			levelCell1.setBorder(0);
    			mainLayout.addCell(levelCell1);
    			
    			PdfPCell levelCell2=new PdfPCell();
    			translatedValue="";
    			catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getLevelId());
				if(catVal!=null){
					translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
				}
    			p1=new Paragraph(translatedValue,plainFont);
    			levelCell2.addElement(p1);
    			levelCell2.setBorder(0);
    			mainLayout.addCell(levelCell2);
        	}
        	
        	//Sector
        	PdfPCell sectorCell1=new PdfPCell();
        	p1=new Paragraph(TranslatorWorker.translateText("Sectors",locale,siteId),titleFont);
        	p1.setAlignment(Element.ALIGN_RIGHT);
			sectorCell1.addElement(p1);
			sectorCell1.setBackgroundColor(new Color(244,244,242));
			sectorCell1.setBorder(0);
			mainLayout.addCell(sectorCell1);
			
			String output="";
			String primary="Primary Sectors: ";
			String secondary= "Secondary Sectors: "; 
			
			List<AmpClassificationConfiguration> classificationConfigs=SectorUtil.getAllClassificationConfigs();
			for (AmpClassificationConfiguration configuration : classificationConfigs) {
				for (ActivitySector actSect : myForm.getSectors().getActivitySectors()) {
					String val="";
					if(actSect.getSectorName()!=null && actSect.getSectorName().length()>0){
						val+=actSect.getSectorName();
					}
					if(actSect.getSubsectorLevel1Name()!=null && actSect.getSubsectorLevel1Name().length()>0){
						val+="["+actSect.getSubsectorLevel1Name()+"]";
					}
					if(actSect.getSubsectorLevel2Name()!=null && actSect.getSubsectorLevel2Name().length()>0){
						val+="["+actSect.getSubsectorLevel2Name()+"]";
					}
					val+="  ("+actSect.getSectorPercentage()+")% \n";
					//is primary or secondary sector
					if(actSect.getConfigId().equals(configuration.getId())){
						if(configuration.isPrimary()){							
							primary+=val;
						}else{
							secondary+=val;
						}
					}
				}
			}
			output=primary+"\n"+secondary;
			PdfPCell sectorCell2=new PdfPCell();
			p1=new Paragraph(output,plainFont);
			sectorCell2.addElement(p1);
			sectorCell2.setBorder(0);
			mainLayout.addCell(sectorCell2);
			
			//Components
			Collection<ActivitySector> components=myForm.getComponents().getActivityComponentes();
			if(components!=null){
				PdfPCell componentsCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Components",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				componentsCell1.addElement(p1);
				componentsCell1.setBackgroundColor(new Color(244,244,242));
				componentsCell1.setBorder(0);
				mainLayout.addCell(componentsCell1);
				
				String result="";
				for (ActivitySector component : components) {
					result+=component.getSectorName()+" " + component.getSectorPercentage()+"% \n";
				}
				
				PdfPCell componentCell2=new PdfPCell();
				p1=new Paragraph(result,plainFont);
				componentCell2.addElement(p1);
				componentCell2.setBorder(0);
				mainLayout.addCell(componentCell2);
			}
			
			//National Plan Objective
			if(myForm.getPrograms().getNationalPlanObjectivePrograms()!=null){
				PdfPCell natPlanObjCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("National Plan Objective",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				natPlanObjCell1.addElement(p1);
				natPlanObjCell1.setBackgroundColor(new Color(244,244,242));
				natPlanObjCell1.setBorder(0);
				mainLayout.addCell(natPlanObjCell1);
				
				String result= buildProgramsOutput(myForm.getPrograms().getNationalPlanObjectivePrograms());
				
				PdfPCell natPlanObjCell2=new PdfPCell();
				p1=new Paragraph(result,plainFont);
				natPlanObjCell2.addElement(p1);
				natPlanObjCell2.setBorder(0);
				mainLayout.addCell(natPlanObjCell2);
			}
			
			//Primary Programs
			if(myForm.getPrograms().getPrimaryPrograms()!=null){
				PdfPCell primaryProgCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Primary Programs",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				primaryProgCell1.addElement(p1);
				primaryProgCell1.setBackgroundColor(new Color(244,244,242));
				primaryProgCell1.setBorder(0);
				mainLayout.addCell(primaryProgCell1);
				
				String result= buildProgramsOutput(myForm.getPrograms().getPrimaryPrograms());
				
				PdfPCell primaryProgCell2=new PdfPCell();
				p1=new Paragraph(result,plainFont);
				primaryProgCell2.addElement(p1);
				primaryProgCell2.setBorder(0);
				mainLayout.addCell(primaryProgCell2);
			}
			
			//secondary Programs
			if(myForm.getPrograms().getSecondaryPrograms()!=null){
				PdfPCell secondaryProgCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Secondary Programs",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				secondaryProgCell1.addElement(p1);
				secondaryProgCell1.setBackgroundColor(new Color(244,244,242));
				secondaryProgCell1.setBorder(0);
				mainLayout.addCell(secondaryProgCell1);
				
				String result= buildProgramsOutput(myForm.getPrograms().getSecondaryPrograms());
				
				PdfPCell secondaryProgCell2=new PdfPCell();
				p1=new Paragraph(result,plainFont);
				secondaryProgCell2.addElement(p1);
				secondaryProgCell2.setBorder(0);
				mainLayout.addCell(secondaryProgCell2);
			}
			
			/**
			 * funding
			 */
			PdfPTable fundingTable = buildFundingInformationPart(myForm,mainLayout);
			
			/**
			 * Regional Funding
			 */
			PdfPCell regFundingCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Regional Funding",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			regFundingCell1.addElement(p1);
			regFundingCell1.setBackgroundColor(new Color(244,244,242));
			regFundingCell1.setBorder(0);
			mainLayout.addCell(regFundingCell1);
			
			//now we should create nested table and add it as second cell in mainLayout
			PdfPTable nested1 = new PdfPTable(2);
			if(myForm.getFunding().getRegionalFundings()!=null){
				for (RegionalFunding regFunf : (Collection<RegionalFunding>)myForm.getFunding().getRegionalFundings()) {
					//create first row (Region Name)
					PdfPCell nestedCell1=new PdfPCell();
					nestedCell1.setBorder(0);
					p1=new Paragraph(TranslatorWorker.translateText(regFunf.getRegionName(),locale,siteId),plainFont);
					p1.setAlignment(Element.ALIGN_LEFT);
					nestedCell1.addElement(p1);
					regFundingCell1.setBackgroundColor(new Color(255,255,255));
					regFundingCell1.setBorder(0);
					nested1.addCell(nestedCell1);
					
					if(regFunf.getCommitments()!=null){ //create commitment row
						buildFinanceInfoOutput(nested1, TranslatorWorker.translateText("Commitment",locale,siteId),(List<FundingDetail>)regFunf.getCommitments());
					}
					
					if(regFunf.getDisbursements()!=null){ //create disbursments row 
						buildFinanceInfoOutput(nested1, TranslatorWorker.translateText("Disbursment",locale,siteId),(List<FundingDetail>)regFunf.getDisbursements());
					}
					
					if(regFunf.getExpenditures()!=null){ //create expenditure row
						buildFinanceInfoOutput(nested1, TranslatorWorker.translateText("Expenditures",locale,siteId), (List<FundingDetail>)regFunf.getExpenditures());
					}
				}				
			}
			PdfPCell regFundCell=new PdfPCell(nested1);
			regFundCell.setBorder(0);
			mainLayout.addCell(regFundCell);

			/**
			 * components
			 */
			buildComponentsPart(myForm, mainLayout, fundingTable);		
			
			/**
			 * Issues
			 */
			buildIssuesPart(myForm, mainLayout);
			
			/**
			 * related documents
			 */
			buildRelatedDocsPart(myForm, mainLayout, event);
			
			/**
			 * Related organizations
			 */
			PdfPCell relOrgCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Related Organizations", locale, siteId)+":",titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			relOrgCell1.setBorder(0);
			relOrgCell1.addElement(p1);
			relOrgCell1.setBackgroundColor(new Color(244,244,242));
			mainLayout.addCell(relOrgCell1);
			
			PdfPCell relOrgCell2=new PdfPCell();
			relOrgCell2.setBorder(0);
			PdfPTable relatedOrgnested=new PdfPTable(1); //table that holds all related organisations			
			//Responsible Organisations
			buildRelatedOrganisationsOutput(relatedOrgnested,"Responsible Organization",myForm.getAgencies().getRespOrganisations());
			//Executing Agency
			buildRelatedOrganisationsOutput(relatedOrgnested,"Executing Agency",myForm.getAgencies().getExecutingAgencies());
			//Implementing Agency
			buildRelatedOrganisationsOutput(relatedOrgnested,"Implementing Agency",myForm.getAgencies().getImpAgencies());
			//Beneficiary Agency
			buildRelatedOrganisationsOutput(relatedOrgnested,"Beneficiary Agency",myForm.getAgencies().getBenAgencies());
			//Contracting Agency
			buildRelatedOrganisationsOutput(relatedOrgnested,"Contracting Agency",myForm.getAgencies().getConAgencies());
			//Sector Group
			buildRelatedOrganisationsOutput(relatedOrgnested,"Sector Group",myForm.getAgencies().getSectGroups());
			//Regional Group
			buildRelatedOrganisationsOutput(relatedOrgnested,"Regional Group",myForm.getAgencies().getRegGroups());
			
			relOrgCell2.addElement(relatedOrgnested);
			mainLayout.addCell(relOrgCell2);			
			/**
			 *	Contact Informations 
			 */			
			//Donor funding contact information
			buildContactInfoOutput(mainLayout,"Donor funding contact information",myForm.getContactInformation().getDonorContacts());		
			//MOFED contact information
			buildContactInfoOutput(mainLayout,"MOFED contact information",myForm.getContactInformation().getMofedContacts());
			//Sec Min funding contact information
			buildContactInfoOutput(mainLayout,"Sector Ministry contact information",myForm.getContactInformation().getSectorMinistryContacts());
			//Project Coordinator contact information
			buildContactInfoOutput(mainLayout,"Proj. Coordinator contact information",myForm.getContactInformation().getProjCoordinatorContacts());
			
			/**
			 * Proposed Project Cost
			 */
			PdfPCell costCell1=new PdfPCell();
			costCell1.setBorder(0);
			p1=new Paragraph(TranslatorWorker.translateText("Proposed Project Cost", locale, siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			costCell1.addElement(p1);
			costCell1.setBackgroundColor(new Color(244,244,242));			
			mainLayout.addCell(costCell1);
			
			PdfPCell costCell2=new PdfPCell();
			costCell2.setBorder(0);
			String costOutput="";
			if(myForm.getFunding().getProProjCost()!=null){
				costOutput="Cost \t";
				if(myForm.getFunding().getProProjCost().getFunAmount()!=null){
					costOutput+="*"+myForm.getFunding().getProProjCost().getFunAmount();
				}
				if(myForm.getFunding().getProProjCost().getCurrencyCode()!=null){
					costOutput+=" "+myForm.getFunding().getProProjCost().getCurrencyCode();
				}
				costOutput+="\n Proposed Completion Date \t";
				if(myForm.getFunding().getProProjCost().getFunDate()!=null){
					costOutput+=myForm.getFunding().getProProjCost().getFunDate();
				}
			}		
			costCell2.addElement(new Paragraph(costOutput,plainFont));
			mainLayout.addCell(costCell2);
			
			/**
			 * Costing
			 */
			buildCostingPart(request, actId, mainLayout);
			/**
			 * Activity created by
			 */
			PdfPCell createdBy1=new PdfPCell();
			createdBy1.setBorder(0);
			createdBy1.setBackgroundColor(new Color(244,244,242));
			Paragraph createdBy1P=new Paragraph(TranslatorWorker.translateText("Activity created by", locale, siteId),titleFont);
			createdBy1P.setAlignment(Element.ALIGN_RIGHT);
			createdBy1.addElement(createdBy1P);
			mainLayout.addCell(createdBy1);
			
			PdfPCell createdBy2=new PdfPCell();
			createdBy2.setBorder(0);
			Paragraph createdBy2P=new Paragraph(myForm.getIdentification().getActAthFirstName()+" "+myForm.getIdentification().getActAthLastName()+"-"+myForm.getIdentification().getActAthEmail(),plainFont);
			createdBy2.addElement(createdBy2P);
			mainLayout.addCell(createdBy2);
			
			/**
			 * Data Source
			 */
			PdfPCell dataSource1=new PdfPCell();
			dataSource1.setBorder(0);
			dataSource1.setBackgroundColor(new Color(244,244,242));
			Paragraph dataSource1P=new Paragraph(TranslatorWorker.translateText("Data Source", locale, siteId),titleFont);
			dataSource1P.setAlignment(Element.ALIGN_RIGHT);
			dataSource1.addElement(dataSource1P);
			mainLayout.addCell(dataSource1);
			
			PdfPCell dataSource2=new PdfPCell();
			dataSource2.setBorder(0);
			Paragraph dataSource2P=new Paragraph(myForm.getIdentification().getActAthAgencySource(),plainFont);
			dataSource2.addElement(dataSource2P);
			mainLayout.addCell(dataSource2);
			
			/**
			 * Activity updated on
			 */
			PdfPCell updatedOn1=new PdfPCell();
			updatedOn1.setBorder(0);
			updatedOn1.setBackgroundColor(new Color(244,244,242));
			Paragraph updatedOn1P=new Paragraph(TranslatorWorker.translateText("Updated On", locale, siteId),titleFont);
			updatedOn1P.setAlignment(Element.ALIGN_RIGHT);
			updatedOn1.addElement(updatedOn1P);
			mainLayout.addCell(updatedOn1);
			
			PdfPCell updatedOn2=new PdfPCell();
			updatedOn2.setBorder(0);
			Paragraph updatedOn2P=new Paragraph(myForm.getIdentification().getUpdatedDate(),plainFont);
			updatedOn2.addElement(updatedOn2P);
			mainLayout.addCell(updatedOn2);
			
			/**
			 * Activity updated by
			 */
			PdfPCell updatedBy1=new PdfPCell();
			updatedBy1.setBorder(0);
			updatedBy1.setBackgroundColor(new Color(244,244,242));
			Paragraph updatedBy1P=new Paragraph(TranslatorWorker.translateText("Activity updated by", locale, siteId),titleFont);
			updatedBy1P.setAlignment(Element.ALIGN_RIGHT);
			updatedBy1.addElement(updatedBy1P);
			mainLayout.addCell(updatedBy1);
			
			PdfPCell updatedBy2=new PdfPCell();
			updatedBy2.setBorder(0);
			if(myForm.getIdentification().getUpdatedBy()!=null){
				Paragraph updatedBy2P=new Paragraph(myForm.getIdentification().getUpdatedBy().getUser().getFirstNames()+" "+myForm.getIdentification().getUpdatedBy().getUser().getLastName()+"-"+myForm.getIdentification().getUpdatedBy().getUser().getEmail(),plainFont);
				updatedBy2.addElement(updatedBy2P);
			}			
			mainLayout.addCell(updatedBy2);
			
			/**
			 *  Activity created on
			 */
			PdfPCell createdOn1=new PdfPCell();
			createdOn1.setBorder(0);
			createdOn1.setBackgroundColor(new Color(244,244,242));
			Paragraph createdOn1P=new Paragraph(TranslatorWorker.translateText("Created On", locale, siteId),titleFont);
			createdOn1P.setAlignment(Element.ALIGN_RIGHT);
			createdOn1.addElement(createdOn1P);
			mainLayout.addCell(createdOn1);
			
			PdfPCell createdOn2=new PdfPCell();
			createdOn2.setBorder(0);
			Paragraph createdOn2P=new Paragraph(myForm.getIdentification().getCreatedDate(),plainFont);
			createdOn2.addElement(createdOn2P);
			mainLayout.addCell(createdOn2);
			
			/**
			 * Custom Fields
			 */
			if(myForm.getCustomFields()!=null && myForm.getCustomFields().size()>0){
				for (CustomField<?> customField : myForm.getCustomFields()) {
					PdfPCell customFields1=new PdfPCell();
					customFields1.setBorder(0);
					customFields1.setBackgroundColor(new Color(244,244,242));
					Paragraph customFields1P=new Paragraph(TranslatorWorker.translateText(customField.getName(), locale, siteId),titleFont);
					customFields1P.setAlignment(Element.ALIGN_RIGHT);
					customFields1.addElement(customFields1P);
					mainLayout.addCell(customFields1);
					
					PdfPCell customFields2=new PdfPCell();
					customFields2.setBorder(0);
					Paragraph customFields2P=new Paragraph(" ");					
					if(customField instanceof ComboBoxCustomField){
						customFields2P=new Paragraph(((ComboBoxCustomField) customField).getOptions().get(customField.getValue()),plainFont);
					}else if(customField instanceof CategoryCustomField){
						if(((CategoryCustomField)customField).getValue()>0){
							catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(((CategoryCustomField)customField).getValue());
							if(catVal!=null){
								translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
								customFields2P=new Paragraph(translatedValue,plainFont);
							}
						}					
					}else if(customField instanceof DateCustomField){
						customFields2P=new Paragraph(((DateCustomField) customField).getStrDate(),plainFont);
					}else if(customField instanceof RadioOptionCustomField){
						String outputVal=" ";
						for (String option : ((RadioOptionCustomField) customField).getOptions().keySet()) {
							if(option.equals(customField.getValue())){
								outputVal+=((RadioOptionCustomField) customField).getOptions().get(option)+" ";
							}
						}
						customFields2P=new Paragraph(outputVal,plainFont);
					}else if(customField instanceof CheckCustomField){
						if(((CheckCustomField)customField).getValue()){
							customFields2P=new Paragraph(((CheckCustomField)customField).getLabelTrue(),plainFont);
						}else if(!((CheckCustomField)customField).getValue()){
							customFields2P=new Paragraph(((CheckCustomField)customField).getLabelFalse(),plainFont);
						}
					}else{
						if(customField.getValue()!=null){
							customFields2P=new Paragraph(customField.getValue().toString(),plainFont);
						}
					}
					customFields2.addElement(customFields2P);
					mainLayout.addCell(customFields2);
					
				}
			}
			
			/**
			 * Activity - Performance
			 */
			PdfPCell actPerformanceCell1=new PdfPCell();
			actPerformanceCell1.setBorder(0);
			actPerformanceCell1.setBackgroundColor(new Color(244,244,242));
			p1=new Paragraph(TranslatorWorker.translateText("Activity Performance", locale, siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			actPerformanceCell1.addElement(p1);			
			mainLayout.addCell(actPerformanceCell1);
			
			//chart
			PdfPCell chartCell = new PdfPCell();
			chartCell.setBorder(0);
			Set<IndicatorActivity> values=activity.getIndicators();
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			org.digijava.module.aim.helper.ChartParams cp = new ChartParams();
			cp.setData(values);
			cp.setTitle("");
			cp.setSession(request.getSession());
			JFreeChart chart=ChartGenerator.generatePerformanceChart(cp,siteId,locale);
			CategoryPlot pl = (CategoryPlot) chart.getPlot();
			CategoryItemRenderer r1 = pl.getRenderer();   //new StackedBarRenderer();		
			r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
			r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);
			pl.setRenderer(r1);
			ChartRenderingInfo info = new ChartRenderingInfo();
			if (chart != null) {
	            Plot plot = chart.getPlot();
	            plot.setNoDataMessage("No Data Available");
	            java.awt.Font font = new java.awt.Font(null, 0, 24);
	            plot.setNoDataMessageFont(font);
	            
	            // write image in response
	            ChartUtilities.writeChartAsPNG(outByteStream,chart,350,420,info);
	            Image img = Image.getInstance(outByteStream.toByteArray());
	            img.setAlignment(Image.ALIGN_MIDDLE);
	            img.setWidthPercentage(60);
	            chartCell.addElement(img);
	            chartCell.setPadding(4);
	            chartCell.setBorder(PdfPCell.NO_BORDER);
	            
	        }
			mainLayout.addCell(chartCell);
			
			/**
			 * Activity - Risk
			 */
			PdfPCell riskCell1=new PdfPCell();
			riskCell1.setBorder(0);
			riskCell1.setBackgroundColor(new Color(244,244,242));
			p1=new Paragraph(TranslatorWorker.translateText("Activity Risk", locale, siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			riskCell1.addElement(p1);
			mainLayout.addCell(riskCell1);
			
			//chart
			ByteArrayOutputStream outByteStream1 = new ByteArrayOutputStream();
			PdfPCell riskCell2 = new PdfPCell();
			riskCell2.setBorder(0);
			ArrayList<AmpCategoryValue> risks=ChartGenerator.getActivityRisks(actId);
			ChartParams rcp = new ChartParams();		
			rcp.setData(risks);
			rcp.setTitle("");
			JFreeChart riskChart=ChartGenerator.generateRiskChart(rcp, siteId, locale);
			ChartRenderingInfo riskInfo = new ChartRenderingInfo();
			if (riskChart != null) {
	            Plot plot = riskChart.getPlot();
	            plot.setNoDataMessage("No Data Available");
	            java.awt.Font font = new java.awt.Font(null, 0, 24);
	            plot.setNoDataMessageFont(font);	            
	            // write image in response
	            ChartUtilities.writeChartAsPNG(outByteStream1,riskChart,350,420,riskInfo);
	            Image img = Image.getInstance(outByteStream1.toByteArray());
	            img.setWidthPercentage(60);
	            img.setAlignment(Image.ALIGN_MIDDLE);
	            riskCell2.addElement(img);
	            riskCell2.setPadding(4);
	            riskCell2.setBorder(PdfPCell.NO_BORDER);
	            
	        }
			mainLayout.addCell(riskCell2);
		}
		
		document.add(mainLayout); //put pdfTable in document
		document.close();		
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		// TODO Auto-generated method stub
		return null;
	}
	
	//cuts <p> and </p> tags from editTag value
	private String processEditTagValue(HttpServletRequest request,String editTagKey) throws Exception {
		int startInex;
		int endIndex;
		String projectComments=getEditTagValue(request,editTagKey);
		startInex=projectComments.indexOf("<p>");
		endIndex=projectComments.indexOf("</p>");
		if(startInex!=-1 && endIndex!=-1 && startInex<endIndex){
			projectComments=projectComments.substring(startInex+3, endIndex);
		}
		return projectComments;
	}

	private void buildIssuesPart(EditActivityForm myForm, PdfPTable mainLayout)	throws WorkerException {
		Paragraph p1;
		PdfPCell issuesCell1=new PdfPCell();
		issuesCell1.setBackgroundColor(new Color(244,244,242));
		issuesCell1.setBorder(0);
		p1=new Paragraph(TranslatorWorker.translateText("Issues", locale, siteId),titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		issuesCell1.addElement(p1);			
		mainLayout.addCell(issuesCell1);
		
		PdfPCell issuesCell2=new PdfPCell();
		issuesCell2.setBackgroundColor(new Color(255,255,255));
		issuesCell2.setBorder(0);
		if(myForm.getIssues().getIssues()!=null && myForm.getIssues().getIssues().size()>0){
			com.lowagie.text.List issuesList=new com.lowagie.text.List(false,20);  //is not numbered list
			issuesList.setListSymbol(new Chunk("\u2022"));
			for (org.digijava.module.aim.helper.Issues issue : myForm.getIssues().getIssues()) {
				ListItem issueItem=new ListItem(new Phrase(issue.getName()+" \t"+issue.getIssueDate(),plainFont));
				issuesList.add(issueItem);
				if(issue.getMeasures()!=null){
					com.lowagie.text.List measuresSubList=new com.lowagie.text.List(false,20);  //is not numbered list
					measuresSubList.setListSymbol("-");
					for (Measures measure : issue.getMeasures()) {
						ListItem measureItem=new ListItem(new Phrase(measure.getName(),plainFont));
						measuresSubList.add(measureItem);
						if(measure.getActors()!=null && measure.getActors().size()>0){
							com.lowagie.text.List actorsSubList=new com.lowagie.text.List(false,20); //is not numbered list
							actorsSubList.setListSymbol(new Chunk("\u2022"));
							for (AmpActor actor : measure.getActors()) {
								ListItem actorItem=new ListItem(new Phrase(actor.getName(),plainFont));
								actorsSubList.add(actorItem);
							}
							measuresSubList.add(actorsSubList);
						}
					}
					issuesList.add(measuresSubList);
				}					
			}
			issuesCell2.addElement(issuesList);
		}
		mainLayout.addCell(issuesCell2);
	}

	private void buildCostingPart(HttpServletRequest request, Long actId,PdfPTable mainLayout) throws WorkerException, AimException {
		Paragraph p1;
		PdfPCell costingCell1=new PdfPCell();
		costingCell1.setBorder(0);
		costingCell1.setBackgroundColor(new Color(244,244,242));
		p1=new Paragraph(TranslatorWorker.translateText("Costing", locale, siteId),titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		costingCell1.addElement(p1);			
		mainLayout.addCell(costingCell1);
		
		PdfPCell costingCell2=new PdfPCell();		
		costingCell2.setBorder(1);
		costingCell2.setBorderColor(new Color(201,201,199));
		PdfPTable costingInnerTable=new PdfPTable(3); //table with 3 cells
		BigDecimal grandCost = new BigDecimal(0);
		BigDecimal grandContribution = new BigDecimal(0);
			PdfPCell nameCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("name", locale, siteId),titleFont));
			nameCell.setBorder(0);
			nameCell.setBackgroundColor(new Color(244,244,242));
			costingInnerTable.addCell(nameCell);
			
			PdfPCell totalCostCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Total Cost", locale, siteId),titleFont));
			totalCostCell.setBorder(0);
			totalCostCell.setBackgroundColor(new Color(244,244,242));
			costingInnerTable.addCell(totalCostCell);
			
			PdfPCell totalContrCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Total Contribution", locale, siteId),titleFont));
			totalContrCell.setBorder(0);
			totalContrCell.setBackgroundColor(new Color(244,244,242));
			costingInnerTable.addCell(totalContrCell);
			
			Collection euActs = EUActivityUtil.getEUActivities(actId); //costs
			if(euActs!=null && euActs.size()>0){
				HttpSession session = request.getSession();
				TeamMember tm = (TeamMember) session.getAttribute("currentMember");
				Long defaultCurrency=null;
				if(tm.getAppSettings().getCurrencyId()!=null){
					defaultCurrency=tm.getAppSettings().getCurrencyId();
				}else{
					defaultCurrency=CurrencyUtil.getAmpcurrency("USD").getAmpCurrencyId();
				}
			
				for (EUActivity euActivity : (Collection<EUActivity>)euActs) {
					euActivity.setDesktopCurrencyId(defaultCurrency);
					//euAct name
					if(euActivity.getTotalCostConverted()!=null){
						grandCost=grandCost.add(euActivity.getTotalCostConverted());
					}
					if(euActivity.getTotalContributionsConverted()!=null){
						grandContribution=grandContribution.add(euActivity.getTotalContributionsConverted());
					}						
					PdfPCell name=new PdfPCell(new Paragraph(euActivity.getName(),titleFont));
					name.setBorder(0);
					name.setBackgroundColor(new Color(255,255,255));
					costingInnerTable.addCell(name);
					//euAct totalsConverted   
					NumberFormat formatter = FormatHelper.getDecimalFormat();
					Double totalsConverted=new Double(formatter.format(euActivity.getTotalCostConverted())) ;						
					p1=new Paragraph(totalsConverted.toString(),plainFont);
					p1.setAlignment(Element.ALIGN_RIGHT);
					PdfPCell totalsConvertedCell=new PdfPCell(p1);
					totalsConvertedCell.setBorder(0);
					totalsConvertedCell.setBackgroundColor(new Color(255,255,255));
					costingInnerTable.addCell(totalsConvertedCell);
					//totalContributionsConverted
					Double totalContributionsConverted=new Double(new DecimalFormat("###,###,###.##").format(euActivity.getTotalContributionsConverted()));
					p1=new Paragraph(totalContributionsConverted.toString(),plainFont);
					p1.setAlignment(Element.ALIGN_RIGHT);
					PdfPCell totalContConvertedCell=new PdfPCell(p1);
					totalContConvertedCell.setBorder(0);
					totalContConvertedCell.setBackgroundColor(new Color(255,255,255));
					costingInnerTable.addCell(totalContConvertedCell);
					
					PdfPCell anotherInfo=new PdfPCell();
					anotherInfo.setBorder(0);
					anotherInfo.setColspan(3);
					String euInfo="";
					if(euActivity.getInputs()!=null){
						euInfo+= TranslatorWorker.translateText("Inputs", locale, siteId)+":"+ euActivity.getInputs() + "\n";
					}
					if(euActivity.getAssumptions()!=null){
						euInfo+= TranslatorWorker.translateText("Assumptions", locale, siteId)+":"+ euActivity.getAssumptions() + "\n";
					}
					if(euActivity.getProgress()!=null){
						euInfo+= TranslatorWorker.translateText("Progress", locale, siteId)+":"+ euActivity.getProgress() + "\n";
					}
					if(euActivity.getDueDate()!=null){
						euInfo+= TranslatorWorker.translateText("Due Date", locale, siteId)+":"+ DateConversion.ConvertDateToString(euActivity.getDueDate()) + "\n";
					}
					anotherInfo.addElement(new Paragraph(euInfo,plainFont));
					costingInnerTable.addCell(anotherInfo);
				}
				
			}
			PdfPCell emptyCell=new PdfPCell();
			emptyCell.setBorder(0);
			//emptyCell.setColspan(2);
			costingInnerTable.addCell(emptyCell);
			PdfPCell lineCell=new PdfPCell();
			lineCell.setBorder(0);
			//HORIZONTAL LINE
			Paragraph separator = new Paragraph(0);
			separator.add(new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_LEFT, 0)));
			lineCell.addElement(separator);
			costingInnerTable.addCell(lineCell);        	
			
			PdfPCell emptyCell1=new PdfPCell();
			emptyCell1.setBorder(0);
			costingInnerTable.addCell(emptyCell1);
			
			if(euActs!=null && euActs.size()>0){
				PdfPCell totals=new PdfPCell();
				totals.setBorder(0);
				Paragraph total1=new Paragraph(TranslatorWorker.translateText("Totals", locale, siteId)+": ",titleFont);
				total1.setAlignment(Element.ALIGN_RIGHT);
				totals.addElement(total1);
				costingInnerTable.addCell(totals);
				
				PdfPCell grandCostCell=new PdfPCell();
				grandCostCell.setBorder(0);
				NumberFormat formatter = FormatHelper.getDecimalFormat();
				String grTotal=formatter.format(grandCost);
				Paragraph gc1=new Paragraph(grTotal,plainFont);
				grandCostCell.addElement(gc1);
				costingInnerTable.addCell(grandCostCell);
				
				PdfPCell grandContributionCell=new PdfPCell();
				grandContributionCell.setBorder(0);
				String grContTotal=formatter.format(grandContribution) ;
				Paragraph gtc1=new Paragraph(grContTotal,plainFont);
				grandContributionCell.addElement(gtc1);
				costingInnerTable.addCell(grandContributionCell);
				
				
				PdfPCell contGap=new PdfPCell();
				contGap.setBorder(0);
				Paragraph contGap1=new Paragraph(TranslatorWorker.translateText("Contribution Gap", locale, siteId)+": ",titleFont);
				contGap1.setAlignment(Element.ALIGN_RIGHT);
				contGap.addElement(contGap1);
				costingInnerTable.addCell(contGap);
				
				PdfPCell contGapCell=new PdfPCell(); 
				contGapCell.setBorder(0);
				String contributionGap=formatter.format(grandCost.subtract(grandContribution));
				Paragraph cg=new Paragraph(contributionGap,plainFont);
				contGapCell.addElement(cg);
				costingInnerTable.addCell(contGapCell);
				
				PdfPCell anotherEmptyCell=new PdfPCell();
				anotherEmptyCell.setBorder(0);
				Paragraph emptyCellVal=new Paragraph("  ");
				emptyCellVal.setAlignment(Element.ALIGN_RIGHT);
				anotherEmptyCell.addElement(emptyCellVal);
				costingInnerTable.addCell(anotherEmptyCell);
				
			}
		costingCell2.addElement(costingInnerTable);

		mainLayout.addCell(costingCell2);
	}

	private void buildRelatedDocsPart(EditActivityForm myForm,PdfPTable mainLayout, PdfPTableEvents event) throws WorkerException {
		Paragraph p1;
		PdfPCell relDocCell1=new PdfPCell();
		relDocCell1.setBackgroundColor(new Color(244,244,242));
		relDocCell1.setBorder(0);
		p1=new Paragraph(TranslatorWorker.translateText("Related Documents", locale, siteId),titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		relDocCell1.addElement(p1);
		mainLayout.addCell(relDocCell1);
		
		PdfPCell relDocCell2=new PdfPCell();
		relDocCell2.setBackgroundColor(new Color(255,255,255));
		relDocCell2.setBorder(0);
		PdfPTable relatedDocnested=new PdfPTable(2);
		relatedDocnested.setTableEvent(event);
		//documents
		if(myForm.getDocuments().getCrDocuments()!=null && myForm.getDocuments().getCrDocuments().size()>0 || myForm.getDocuments().getDocumentList()!=null && myForm.getDocuments().getDocumentList().size()>0){				
			for (Documents doc : (Collection<Documents>)myForm.getDocuments().getDocuments()) {
				if(doc.getIsFile()){						
					//document fields						
					PdfPCell docTableNameCell1=new PdfPCell(new Paragraph(new Phrase(doc.getTitle())+"- \t",plainFont));
					docTableNameCell1.setBackgroundColor(new Color(255,255,255));
					docTableNameCell1.setBorder(0);
					relatedDocnested.addCell(docTableNameCell1);
					PdfPCell docTableNameCell2=new PdfPCell(new Paragraph(new Phrase(doc.getFileName(),titleFont)));
					docTableNameCell2.setBackgroundColor(new Color(255,255,255));
					docTableNameCell2.setBorder(0);
					relatedDocnested.addCell(docTableNameCell2);
					
					PdfPCell docTableDescCell1=new PdfPCell(new Paragraph(new Phrase(TranslatorWorker.translateText("Description", locale, siteId)+":",titleFont)));
					docTableDescCell1.setBackgroundColor(new Color(255,255,255));
					docTableDescCell1.setBorder(0);
					relatedDocnested.addCell(docTableDescCell1);
					PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(doc.getDocDescription(),plainFont));
					docTableDescCell2.setBackgroundColor(new Color(255,255,255));
					docTableDescCell2.setBorder(0);
					relatedDocnested.addCell(docTableDescCell2);
					
					PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date", locale, siteId)+":",titleFont));
					docTableDateCell1.setBackgroundColor(new Color(255,255,255));
					docTableDateCell1.setBorder(0);
					relatedDocnested.addCell(docTableDateCell1);
					PdfPCell docTableDateCell2=new PdfPCell(new Paragraph(new Phrase(doc.getDate(), plainFont)));
					docTableDateCell2.setBackgroundColor(new Color(255,255,255));
					docTableDateCell2.setBorder(0);
					relatedDocnested.addCell(docTableDateCell2);
					
					if(doc.getDocType()!=null){
						PdfPCell docTabletypeCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Document Type", locale, siteId)+":",titleFont));
						docTabletypeCell1.setBackgroundColor(new Color(255,255,255));
						docTabletypeCell1.setBorder(0);
						relatedDocnested.addCell(docTabletypeCell1);
						PdfPCell docTabletypeCell2=new PdfPCell(new Paragraph(new Phrase(doc.getDocType(), plainFont)));
						docTabletypeCell2.setBackgroundColor(new Color(255,255,255));
						docTabletypeCell2.setBorder(0);
						relatedDocnested.addCell(docTabletypeCell2);
					}
				}
			}				
			
			if(myForm.getDocuments().getCrDocuments()!=null && myForm.getDocuments().getCrDocuments().size()>0){
				for (DocumentData crDoc : myForm.getDocuments().getCrDocuments()) {
					//title
					PdfPCell docTableNameCell1=new PdfPCell(new Paragraph(new Phrase(crDoc.getTitle())+"- \t",titleFont));
					docTableNameCell1.setBackgroundColor(new Color(255,255,255));
					docTableNameCell1.setBorder(1);
					docTableNameCell1.setBorderColorLeft(Color.BLACK);
					relatedDocnested.addCell(docTableNameCell1);
					PdfPCell docTableNameCell2=new PdfPCell(new Paragraph(new Phrase(crDoc.getName(),plainFont)));
					docTableNameCell2.setBackgroundColor(new Color(255,255,255));
					docTableNameCell2.setBorder(1);
					docTableNameCell2.setBorderColorRight(Color.BLACK);
					relatedDocnested.addCell(docTableNameCell2);
					//description
					PdfPCell docTableDescCell1=new PdfPCell(new Paragraph(new Phrase(TranslatorWorker.translateText("Description", locale, siteId)+":",titleFont)));
					docTableDescCell1.setBackgroundColor(new Color(255,255,255));
					docTableDescCell1.setBorder(0);
					relatedDocnested.addCell(docTableDescCell1);
					PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(crDoc.getDescription(), plainFont));
					docTableDescCell2.setBackgroundColor(new Color(255,255,255));
					docTableDescCell2.setBorder(0);
					relatedDocnested.addCell(docTableDescCell2);
					//date
					if(crDoc.getCalendar()!=null){
						PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date", locale, siteId)+":",titleFont));
						docTableDateCell1.setBackgroundColor(new Color(255,255,255));
						docTableDateCell1.setBorder(0);
						relatedDocnested.addCell(docTableDateCell1);
						PdfPCell docTableDateCell2=new PdfPCell(new Paragraph(new Phrase(crDoc.getCalendar(),plainFont)));
						docTableDateCell2.setBackgroundColor(new Color(255,255,255));
						docTableDateCell2.setBorder(0);
						relatedDocnested.addCell(docTableDateCell2);
					}
				}
			}
		}
		
		//links
		if(myForm.getDocuments().getLinksList()!=null && myForm.getDocuments().getLinksList().size()>0){				
			for (RelatedLinks doc : (Collection<RelatedLinks>)myForm.getDocuments().getLinksList()) {	
					//document fields						
					PdfPCell docTableNameCell1=new PdfPCell(new Paragraph(new Phrase(doc.getRelLink().getTitle())+"- \t",titleFont));
					docTableNameCell1.setBackgroundColor(new Color(255,255,255));
					docTableNameCell1.setBorder(0);
					relatedDocnested.addCell(docTableNameCell1);
					PdfPCell docTableNameCell2=new PdfPCell(new Paragraph(new Phrase(doc.getRelLink().getUrl(),plainFont)));
					docTableNameCell2.setBackgroundColor(new Color(255,255,255));
					docTableNameCell2.setBorder(0);
					relatedDocnested.addCell(docTableNameCell2);
					
					PdfPCell docTableDescCell1=new PdfPCell(new Phrase(TranslatorWorker.translateText("Description", locale, siteId)+":",titleFont));
					docTableDescCell1.setBackgroundColor(new Color(255,255,255));
					docTableDescCell1.setBorder(0);
					relatedDocnested.addCell(docTableDescCell1);
					PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(doc.getRelLink().getDescription(),plainFont));
					docTableDescCell2.setBackgroundColor(new Color(255,255,255));
					docTableDescCell2.setBorder(0);
					relatedDocnested.addCell(docTableDescCell2);
					
					PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date", locale, siteId)+":",titleFont));
					docTableDateCell1.setBackgroundColor(new Color(255,255,255));
					docTableDateCell1.setBorder(0);
					relatedDocnested.addCell(docTableDateCell1);
					PdfPCell docTableDateCell2=new PdfPCell(new Paragraph(new Phrase(doc.getRelLink().getDate(), plainFont)));
					docTableDateCell2.setBackgroundColor(new Color(255,255,255));
					docTableDateCell2.setBorder(0);
					relatedDocnested.addCell(docTableDateCell2);				
			}				
		}
		relDocCell2.addElement(relatedDocnested);
		mainLayout.addCell(relDocCell2);
	}

	private void buildComponentsPart(EditActivityForm myForm,PdfPTable mainLayout, PdfPTable fundingTable)	throws WorkerException, DocumentException {
		Paragraph p1;
		if(GlobalSettings.getInstance().getShowComponentFundingByYear()!=null){
			PdfPCell compCell1=new PdfPCell();
			p1=new Paragraph(TranslatorWorker.translateText("Components",locale,siteId),titleFont);
			p1.setAlignment(Element.ALIGN_RIGHT);
			compCell1.addElement(p1);
			compCell1.setBackgroundColor(new Color(244,244,242));
			compCell1.setBorder(0);
			mainLayout.addCell(compCell1);
			//now we should create nested table and add it as second cell in mainLayout
			PdfPTable componentsNestedTable = new PdfPTable(2);
			componentsNestedTable.getDefaultCell().setBorder(1);
			if(myForm.getComponents().getSelectedComponents()!=null){					
				for (Components<FundingDetail> comp : myForm.getComponents().getSelectedComponents()) {
					//first row- title
					PdfPCell nestedCell1=new PdfPCell();						
					nestedCell1.setBackgroundColor(new Color(255,255,255));
					nestedCell1.setBorder(0);
					nestedCell1.setColspan(2);
					p1=new Paragraph(TranslatorWorker.translateText(comp.getTitle(),locale,siteId),titleFont);
					nestedCell1.addElement(p1);						
					componentsNestedTable.addCell(nestedCell1);
					
					if(! GlobalSettings.getInstance().getShowComponentFundingByYear()){ //false case
						//Description
						PdfPCell descNestedCell=new PdfPCell();
						p1=new Paragraph(TranslatorWorker.translateText("Description",locale,siteId)+":",plainFont);
						descNestedCell.addElement(p1);
						descNestedCell.setBackgroundColor(new Color(255,255,255));
						descNestedCell.setBorder(0);
						componentsNestedTable.addCell(descNestedCell);							
						
						descNestedCell=new PdfPCell();
						p1=new Paragraph(new Phrase(TranslatorWorker.translateText(comp.getDescription(),locale,siteId) ,plainFont));
						p1.setAlignment(Element.ALIGN_LEFT);
						descNestedCell.addElement(p1);
						descNestedCell.setBackgroundColor(new Color(255,255,255));
						descNestedCell.setBorder(0);
						componentsNestedTable.addCell(descNestedCell);
						//third row - finanse of comp.
						PdfPCell financeCompNestedCell=new PdfPCell();
						financeCompNestedCell.setBackgroundColor(new Color(244,244,242));
						financeCompNestedCell.setBorder(0);
						financeCompNestedCell.setColspan(2);
						p1=new Paragraph(TranslatorWorker.translateText("Finance of the component",locale,siteId),titleFont);
						p1.setAlignment(Element.ALIGN_LEFT);
						financeCompNestedCell.addElement(p1);						
						componentsNestedTable.addCell(financeCompNestedCell);
						//commitments row
						if(comp.getCommitments()!=null && comp.getCommitments().size()>0){ //commitments row
							PdfPCell financeCell=new PdfPCell();
							financeCell.setBorder(0);
							financeCell.setColspan(2);
							PdfPTable financeTable=new PdfPTable(2);
							financeTable.setWidths(new float[]{1f,4f});
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Commitment",locale,siteId),(List) comp.getCommitments());
							financeCell.addElement(financeTable);
							componentsNestedTable.addCell(financeCell);
						}
						//disbursments row 
						if(comp.getDisbursements()!=null && comp.getDisbursements().size()>0){
							PdfPCell financeCell=new PdfPCell();
							financeCell.setBorder(0);
							financeCell.setColspan(2);
							PdfPTable financeTable=new PdfPTable(2);
							financeTable.setWidths(new float[]{1f,4f});
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Disbursment",locale,siteId),(List) comp.getDisbursements());
							financeCell.addElement(financeTable);
							componentsNestedTable.addCell(financeCell);
						}
						 //expenditures row
						if(comp.getExpenditures()!=null && comp.getExpenditures().size()>0){
							PdfPCell financeCell=new PdfPCell();
							financeCell.setBorder(0);
							financeCell.setColspan(2);
							PdfPTable financeTable=new PdfPTable(2);
							financeTable.setWidths(new float[]{1f,4f});
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Expenditures",locale,siteId),(List) comp.getExpenditures());
							financeCell.addElement(financeTable);
							componentsNestedTable.addCell(financeCell);														
						}
						//empty line
						PdfPCell emptyCell=new PdfPCell();
						emptyCell.addElement(new Paragraph("\n"));
						emptyCell.setBackgroundColor(new Color(255,255,255));
						emptyCell.setBorder(0);
						emptyCell.setColspan(2);
						componentsNestedTable.addCell(emptyCell);
						//amounts in thousands
						if(org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS.equals("true")){
							PdfPCell amountsInThousandsCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("The amount entered are in thousands (000)",locale,siteId),plainFont));
							amountsInThousandsCell1.setBorder(0);
							amountsInThousandsCell1.setBackgroundColor(new Color(255,255,204));
							amountsInThousandsCell1.setColspan(2);
							fundingTable.addCell(amountsInThousandsCell1);
						}
						//physical progress
						PdfPCell phProgressNestedCell=new PdfPCell();
						p1=new Paragraph(TranslatorWorker.translateText("Physical progress of the component",locale,siteId),plainFont);
						p1.setAlignment(Element.ALIGN_LEFT);
						phProgressNestedCell.addElement(p1);
						phProgressNestedCell.setBackgroundColor(new Color(244,244,242));
						phProgressNestedCell.setBorder(0);
						phProgressNestedCell.setColspan(2);
						componentsNestedTable.addCell(phProgressNestedCell);
						
						if(comp.getPhyProgress()!=null && comp.getPhyProgress().size()>0){
							for (PhysicalProgress phy : comp.getPhyProgress()) {
								//ph progress title
								PdfPCell phProgressCell=new PdfPCell();
								phProgressCell.setBackgroundColor(new Color(255,255,255));
								phProgressCell.setBorder(0);
								p1=new Paragraph(TranslatorWorker.translateText(phy.getTitle(),locale,siteId)+"-",titleFont);									
								phProgressCell.addElement(p1);									
								componentsNestedTable.addCell(phProgressCell);
								
								phProgressCell=new PdfPCell();
								phProgressCell.setBackgroundColor(new Color(255,255,255));
								phProgressCell.setBorder(0);
								p1=new Paragraph(phy.getReportingDate()+"\n",plainFont);
								phProgressCell.addElement(p1);
								componentsNestedTable.addCell(phProgressCell);
								
								phProgressCell=new PdfPCell();
								phProgressCell.setBackgroundColor(new Color(255,255,255));
								phProgressCell.setBorder(0);
								p1=new Paragraph(new Phrase(TranslatorWorker.translateText("Description",locale,siteId)+":" ,plainFont));
								phProgressCell.addElement(p1);
								componentsNestedTable.addCell(phProgressCell);
								
								phProgressCell=new PdfPCell();
								p1=new Paragraph(TranslatorWorker.translateText(phy.getDescription(),locale,siteId)+"\n",plainFont);
								p1.setAlignment(Element.ALIGN_LEFT);
								phProgressCell.addElement(p1);
								phProgressCell.setBackgroundColor(new Color(255,255,255));
								phProgressCell.setBorder(0);
								componentsNestedTable.addCell(phProgressCell);
								
							}
							
						}
					}else if(GlobalSettings.getInstance().getShowComponentFundingByYear() ){ //true case
						//comp code
						PdfPCell compNestedCell=new PdfPCell();
						p1=new Paragraph(TranslatorWorker.translateText("Component Code",locale,siteId)+":",titleFont);
						compNestedCell.addElement(p1);
						compNestedCell.setBackgroundColor(new Color(255,255,255));
						compNestedCell.setBorder(0);
						componentsNestedTable.addCell(compNestedCell);							
						
						compNestedCell=new PdfPCell();
						p1=new Paragraph(comp.getCode() ,plainFont);
						compNestedCell.addElement(p1);
						compNestedCell.setBackgroundColor(new Color(255,255,255));
						compNestedCell.setBorder(0);
						componentsNestedTable.addCell(compNestedCell);
						//finance of the comp
						PdfPCell financeCompNestedCell=new PdfPCell();
						financeCompNestedCell.setBackgroundColor(new Color(244,244,242));
						financeCompNestedCell.setBorder(0);
						financeCompNestedCell.setColspan(2);
						p1=new Paragraph(TranslatorWorker.translateText("Finance of the component",locale,siteId));
						p1.setAlignment(Element.ALIGN_LEFT);
						financeCompNestedCell.addElement(p1);						
						componentsNestedTable.addCell(financeCompNestedCell);							
						//nested							
						for (Integer key : comp.getFinanceByYearInfo().keySet()) {
							//first cell in nested2 table
							PdfPCell nestedCell5=new PdfPCell();
							p1=new Paragraph(key.toString(),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							nestedCell5.addElement(p1);
							nestedCell5.setBackgroundColor(new Color(255,255,255));
							nestedCell5.setBorder(0);
							componentsNestedTable.addCell(nestedCell5);
							//second cell in nested2 table
							PdfPTable financeNestedTable=new PdfPTable(2);
							Map<String,BigDecimal> myMap=comp.getFinanceByYearInfo().get(key); //value of the  comp.getFinanceByYearInfo() Map
							PdfPCell pcs1=new PdfPCell();
							p1=new Paragraph(TranslatorWorker.translateText("Planned Commitments Sum", locale,siteId),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							pcs1.addElement(p1);
							pcs1.setBackgroundColor(new Color(255,255,255));
							pcs1.setBorder(0);
							financeNestedTable.addCell(pcs1);								
							PdfPCell pcs2=new PdfPCell();
							BigDecimal a=myMap.get("MontoProgramado");
							Double plannedCommSum=new Double(new DecimalFormat("0.00").format(a));
							p1=new Paragraph(plannedCommSum.toString(),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							pcs2.addElement(p1);
							pcs2.setBackgroundColor(new Color(255,255,255));
							pcs2.setBorder(0);
							financeNestedTable.addCell(pcs2);								
							
							
							PdfPCell acs1=new PdfPCell();
							p1=new Paragraph(TranslatorWorker.translateText("Actual Commitments Sum", locale,siteId),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							acs1.addElement(p1);
							financeNestedTable.addCell(acs1);
							PdfPCell acs2=new PdfPCell();
							acs2.setBackgroundColor(new Color(255,255,255));
							acs2.setBorder(0);
							Double actCommSum=new Double(new DecimalFormat("0.00").format( myMap.get("MontoReprogramado") ));
							acs2.addElement(new Paragraph(actCommSum.toString(),plainFont));
							financeNestedTable.addCell(acs2);								
							
							
							PdfPCell aes1=new PdfPCell();
							p1=new Paragraph(TranslatorWorker.translateText("Actual Expenditures Sum", locale,siteId),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							aes1.addElement(p1);
							aes1.setBackgroundColor(new Color(255,255,255));
							aes1.setBorder(0);
							financeNestedTable.addCell(aes1);
							
							PdfPCell aes2=new PdfPCell();
							aes2.setBackgroundColor(new Color(255,255,255));
							aes2.setBorder(0);
							Double actExpSum=new Double(new DecimalFormat("0.00").format( myMap.get("MontoEjecutado") ));
							p1=new Paragraph(actExpSum.toString(),plainFont);
							aes2.addElement(p1);
							p1.setAlignment(Element.ALIGN_LEFT);
							financeNestedTable.addCell(aes2);
							
							componentsNestedTable.addCell(financeNestedTable);
						}
					}
				}
			}
			mainLayout.addCell(componentsNestedTable);
		}
	}

	private PdfPTable buildFundingInformationPart(EditActivityForm myForm,PdfPTable mainLayout) throws WorkerException, DocumentException {
		Paragraph p1;
		String output;
		PdfPCell fundingCell1=new PdfPCell();
		p1=new Paragraph(TranslatorWorker.translateText("Funding",locale,siteId),titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		fundingCell1.addElement(p1);
		fundingCell1.setBackgroundColor(new Color(244,244,242));
		fundingCell1.setBorder(0);
		mainLayout.addCell(fundingCell1);
		
		
		PdfPTable fundingTable=new PdfPTable(3);
		fundingTable.setWidths(new float[]{2f,2f,1f});
		boolean drawTotals=false; //draw total planned commitment,total actual commitments e.t.c.
		if(myForm.getFunding().getFundingOrganizations()!=null){				
			for (FundingOrganization fundingOrganisation : myForm.getFunding().getFundingOrganizations()) {
				if(fundingOrganisation.getFundings()!=null){
					drawTotals=true;
					for (Funding funding : (Collection<Funding>)fundingOrganisation.getFundings()) {
						//general info rows
						//funding org id 
						PdfPCell foIdCell1=new PdfPCell();
						foIdCell1.setBackgroundColor(new Color(221,221,221));
						foIdCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText("Funding Organization Id",locale,siteId)+":",plainFont);
						foIdCell1.addElement(p1);
						fundingTable.addCell(foIdCell1);							
						//meaning
						PdfPCell foIdCell3=new PdfPCell(new Paragraph(funding.getOrgFundingId(),plainFont));
						foIdCell3.setBorder(0);
						foIdCell3.setColspan(2);
						foIdCell3.setBackgroundColor(new Color(221,221,221));
						fundingTable.addCell(foIdCell3);
						
						//funding org. name
						PdfPCell foNameCell1=new PdfPCell();
						foNameCell1.setBackgroundColor(new Color(221,221,221));
						foNameCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText("Funding Organization Name",locale,siteId)+":",plainFont);
						foNameCell1.addElement(p1);
						fundingTable.addCell(foNameCell1);							
						//meaning
						PdfPCell foNameCell3=new PdfPCell(new Paragraph(fundingOrganisation.getOrgName(),plainFont));
						foNameCell3.setBorder(0);
						foNameCell3.setColspan(2);
						foNameCell3.setBackgroundColor(new Color(221,221,221));
						fundingTable.addCell(foNameCell3);	
						
						//funding org Assistance
						PdfPCell foAssitanceCell1=new PdfPCell();
						foAssitanceCell1.setBackgroundColor(new Color(221,221,221));
						foAssitanceCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText("Type of Assistance",locale,siteId)+":",plainFont);
						foAssitanceCell1.addElement(p1);
						fundingTable.addCell(foAssitanceCell1);							
						//meaning
						PdfPCell foAssistanceCell2=new PdfPCell(new Paragraph(funding.getTypeOfAssistance()!=null?funding.getTypeOfAssistance().getValue():" ",plainFont));
						foAssistanceCell2.setBorder(0);
						foAssistanceCell2.setColspan(2);
						foAssistanceCell2.setBackgroundColor(new Color(221,221,221));
						fundingTable.addCell(foAssistanceCell2);
						
						//Financial Instrument
						PdfPCell foInstrumentCell1=new PdfPCell();
						foInstrumentCell1.setBackgroundColor(new Color(221,221,221));
						foInstrumentCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText("Financial Instrument",locale,siteId)+":",plainFont);
						foInstrumentCell1.addElement(p1);
						fundingTable.addCell(foInstrumentCell1);							
						//meaning
						PdfPCell foInstrumentCell2=new PdfPCell(new Paragraph(funding.getFinancingInstrument()!=null?funding.getFinancingInstrument().getValue():" ",plainFont));
						foInstrumentCell2.setBorder(0);
						foInstrumentCell2.setColspan(2);
						foInstrumentCell2.setBackgroundColor(new Color(221,221,221));
						fundingTable.addCell(foInstrumentCell2);
						
						//Donor objective
						PdfPCell donorObjCell1=new PdfPCell();
						donorObjCell1.setBackgroundColor(new Color(221,221,221));
						donorObjCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText("Donor Objective",locale,siteId)+":",plainFont);
						donorObjCell1.addElement(p1);
						fundingTable.addCell(donorObjCell1);
						//meaning
						PdfPCell donorObjCell2=new PdfPCell();
						p1=new Paragraph(funding.getDonorObjective(),plainFont);						
						donorObjCell2.addElement(p1);
						donorObjCell2.setBorder(0);
						donorObjCell2.setColspan(2);
						donorObjCell2.setBackgroundColor(new Color(221,221,221));
						fundingTable.addCell(donorObjCell2);
						
						//PLANNED COMITMENTS
						output=TranslatorWorker.translateText("PLANNED COMMITMENTS",locale,siteId);
						if(myForm.getFunding().isFixerate()){
							output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
						}
						PdfPCell plCommCell1=new PdfPCell(new Paragraph(output,plainFont));
						plCommCell1.setBorder(0);
						plCommCell1.setBackgroundColor(new Color(255,255,204));
						plCommCell1.setColspan(3);
						fundingTable.addCell(plCommCell1);
						
						if(funding.getFundingDetails()!=null){
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==0){
									if(fd.getAdjustmentType()==0){
										PdfPCell plCommCell2=new PdfPCell();
										plCommCell2.setBorder(0);
										plCommCell2.setBackgroundColor(new Color(221,221,221));
										plCommCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plCommCell2.addElement(infoTable);
										fundingTable.addCell(plCommCell2);
									}
								}
							}
							PdfPCell subTotalPlComm=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL PLANNED COMMITMENTS:",locale,siteId)+" \t\t         "+funding.getSubtotalPlannedCommitments()+myForm.getCurrCode(),plainFont));
							subTotalPlComm.setBackgroundColor(new Color(221,221,221));
							subTotalPlComm.setColspan(3);
							fundingTable.addCell(subTotalPlComm);
							
							//actual commitments
							output=TranslatorWorker.translateText("ACTUAL COMMITMENTS",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actCommCell1=new PdfPCell(new Paragraph(output,plainFont));
							actCommCell1.setBorder(0);
							actCommCell1.setBackgroundColor(new Color(255,255,204));
							actCommCell1.setColspan(3);
							fundingTable.addCell(actCommCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==0){
									if(fd.getAdjustmentType()==1){
										PdfPCell actCommCell2=new PdfPCell();
										actCommCell2.setBorder(0);
										actCommCell2.setBackgroundColor(new Color(221,221,221));
										actCommCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										actCommCell2.addElement(infoTable);
										fundingTable.addCell(actCommCell2);
									}
								}
							}
							PdfPCell subTotalActComm=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL ACTUAL COMMITMENTS:",locale,siteId)+" \t\t          "+funding.getSubtotalActualCommitments()+myForm.getCurrCode(),plainFont));
							subTotalActComm.setBackgroundColor(new Color(221,221,221));
							subTotalActComm.setColspan(3);
							fundingTable.addCell(subTotalActComm);
							
							//planned disbursment								
							output=TranslatorWorker.translateText("PLANNED DISBURSEMENT",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell plDisbCell1=new PdfPCell(new Paragraph(output,plainFont));
							plDisbCell1.setBorder(0);
							plDisbCell1.setBackgroundColor(new Color(255,255,204));
							plDisbCell1.setColspan(3);
							fundingTable.addCell(plDisbCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==1){
									if(fd.getAdjustmentType()==0){
										PdfPCell plDisbCell2=new PdfPCell();
										plDisbCell2.setBorder(0);
										plDisbCell2.setBackgroundColor(new Color(221,221,221));
										plDisbCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plDisbCell2.addElement(infoTable);
										fundingTable.addCell(plDisbCell2);
									}
								}
							}
							PdfPCell subTotalPlDisb=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL PLANNED DISBURSEMENT:",locale,siteId)+" \t\t         "+funding.getSubtotalPlannedDisbursements()+myForm.getCurrCode(), plainFont));
							subTotalPlDisb.setBackgroundColor(new Color(221,221,221));
							subTotalPlDisb.setColspan(3);
							subTotalPlDisb.setBorder(0);
							fundingTable.addCell(subTotalPlDisb);							
							//actual disbursement
							output=TranslatorWorker.translateText("ACTUAL DISBURSEMENT:",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actDisbCell1=new PdfPCell(new Paragraph(output,plainFont));
							actDisbCell1.setBorder(0);
							actDisbCell1.setBackgroundColor(new Color(255,255,204));
							actDisbCell1.setColspan(3);
							fundingTable.addCell(actDisbCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==1){
									if(fd.getAdjustmentType()==1){
										PdfPCell plDisbCell2=new PdfPCell();
										plDisbCell2.setBorder(0);
										plDisbCell2.setBackgroundColor(new Color(221,221,221));
										plDisbCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plDisbCell2.addElement(infoTable);
										fundingTable.addCell(plDisbCell2);
									}
								}
							}
							PdfPCell subTotalActDisb=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL ACTUAL DISBURSEMENT:",locale,siteId)+" \t\t         "+funding.getSubtotalDisbursements()+myForm.getCurrCode(), plainFont));
							subTotalActDisb.setBackgroundColor(new Color(221,221,221));
							subTotalActDisb.setColspan(3);
							fundingTable.addCell(subTotalActDisb);
							
							//planned expenditures
							output=TranslatorWorker.translateText("PLANNED EXPENDITURES:",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell plExpCell1=new PdfPCell(new Paragraph(output,plainFont));
							plExpCell1.setBorder(0);
							plExpCell1.setBackgroundColor(new Color(255,255,204));
							plExpCell1.setColspan(3);
							fundingTable.addCell(plExpCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==2){
									if(fd.getAdjustmentType()==0){
										PdfPCell plDisbCell2=new PdfPCell();
										plDisbCell2.setBorder(0);
										plDisbCell2.setBackgroundColor(new Color(221,221,221));
										plDisbCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plDisbCell2.addElement(infoTable);
										fundingTable.addCell(plDisbCell2);
									}
								}
							}
							PdfPCell subTotalPlExp=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL PLANNED EXPENDITURES:",locale,siteId)+" \t\t         "+funding.getSubtotalPlannedExpenditures()+myForm.getCurrCode(), plainFont));
							subTotalPlExp.setBackgroundColor(new Color(221,221,221));
							subTotalPlExp.setColspan(3);
							fundingTable.addCell(subTotalPlExp);
							
							//actual expenditures
							output=TranslatorWorker.translateText("ACTUAL EXPENDITURES::",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actExpCell1=new PdfPCell(new Paragraph(output,plainFont));
							actExpCell1.setBorder(0);
							actExpCell1.setBackgroundColor(new Color(255,255,204));
							actExpCell1.setColspan(3);
							fundingTable.addCell(actExpCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==2){
									if(fd.getAdjustmentType()==1){
										PdfPCell plDisbCell2=new PdfPCell();
										plDisbCell2.setBorder(0);
										plDisbCell2.setBackgroundColor(new Color(221,221,221));
										plDisbCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plDisbCell2.addElement(infoTable);
										fundingTable.addCell(plDisbCell2);
									}
								}
							}
							PdfPCell subTotalActExp=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL ACTUAL EXPENDITURES:",locale,siteId)+" \t\t         "+funding.getSubtotalExpenditures()+myForm.getCurrCode(),plainFont));
							subTotalActExp.setBackgroundColor(new Color(221,221,221));
							subTotalActExp.setColspan(3);
							fundingTable.addCell(subTotalActExp);
							
							//ACTUAL DISBURSMENT ORDERS
							output=TranslatorWorker.translateText("ACTUAL DISBURSMENT ORDERS:",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actDisbOrdCell1=new PdfPCell(new Paragraph(output,plainFont));
							actDisbOrdCell1.setBorder(0);
							actDisbOrdCell1.setBackgroundColor(new Color(255,255,204));
							actDisbOrdCell1.setColspan(3);
							fundingTable.addCell(actDisbOrdCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==2){
									if(fd.getAdjustmentType()==1){
										PdfPCell plDisbCell2=new PdfPCell();
										plDisbCell2.setBorder(0);
										plDisbCell2.setBackgroundColor(new Color(221,221,221));
										plDisbCell2.setColspan(3);
										//inner table with funding information
										PdfPTable infoTable=new PdfPTable(4);
										buildFundingInfoInnerTable(fd,infoTable);
										plDisbCell2.addElement(infoTable);
										fundingTable.addCell(plDisbCell2);
									}
								}
							}
							PdfPCell subTotalDisbOrd=new PdfPCell(new Paragraph(TranslatorWorker.translateText("SUBTOTAL DISBURSMENT ORDERS:",locale,siteId)+" \t\t          "+funding.getSubtotalActualDisbursementsOrders()+myForm.getCurrCode(),plainFont));
							subTotalDisbOrd.setBackgroundColor(new Color(221,221,221));
							subTotalDisbOrd.setColspan(3);
							fundingTable.addCell(subTotalDisbOrd);
							
							//UNDISBURSED BALANCE
							PdfPCell undisbursedBalanceCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("UNDISBURSED BALANCE:",locale,siteId)+" \t\t         "+ funding.getUnDisbursementBalance()+myForm.getCurrCode()+"\n\n",plainFont));
							undisbursedBalanceCell1.setBorder(0);
							undisbursedBalanceCell1.setBackgroundColor(new Color(255,255,204));
							undisbursedBalanceCell1.setColspan(3);
							fundingTable.addCell(undisbursedBalanceCell1);
							
							if(org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS.equals("true")){
								PdfPCell amountsInThousandsCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("The amount entered are in thousands (000)",locale,siteId),plainFont));
								amountsInThousandsCell1.setBorder(0);
								amountsInThousandsCell1.setBackgroundColor(new Color(255,255,204));
								amountsInThousandsCell1.setColspan(3);
								fundingTable.addCell(amountsInThousandsCell1);
							}
						}
						//empty cell
						PdfPCell empty=new PdfPCell(new Paragraph("\n\n"));
						empty.setBorder(0);
						empty.setBackgroundColor(new Color(255,255,255));
						empty.setColspan(3);
						fundingTable.addCell(empty);							
					}
				}
				
			}
			//totals
			if(myForm.getFunding().getFundingOrganizations()!=null && drawTotals){
				PdfPCell empty=new PdfPCell(new Paragraph("\n\n"));
				empty.setBorder(0);
				empty.setBackgroundColor(new Color(255,255,255));
				empty.setColspan(3);
				fundingTable.addCell(empty);							
				//TOTAL PLANNED COMMITMENTS
				PdfPCell totalPC=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL PLANNED COMMITMENTS:",locale,siteId),plainFont));
				totalPC.setColspan(2);
				totalPC.setBorder(0);
				totalPC.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalPC);
				PdfPCell totalPCAmount=new PdfPCell();
				totalPCAmount.setBorder(0);
				totalPCAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalPlannedCommitments()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalPCAmount.addElement(p1);
				fundingTable.addCell(totalPCAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				
		    	//TOTAL ACTUAL COMMITMENTS
				PdfPCell totalPAC=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL ACTUAL COMMITMENTS:",locale,siteId),plainFont));
				totalPAC.setColspan(2);
				totalPAC.setBackgroundColor(new Color(221,221,221));
				totalPAC.setBorder(0);
				fundingTable.addCell(totalPAC);						
				PdfPCell totalACAmount=new PdfPCell();
				totalACAmount.setBorder(0);
				totalACAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalCommitments()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalACAmount.addElement(p1);
				fundingTable.addCell(totalACAmount);
				//empty cell
				buildEmptyCell(fundingTable); 
				
				//TOTAL PLANNED DISBURSEMENT
				PdfPCell totalPD=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL PLANNED DISBURSEMENT:",locale,siteId),plainFont));
				totalPD.setColspan(2);
				totalPD.setBorder(0);
				totalPD.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalPD);						
				PdfPCell totalPDAmount=new PdfPCell();
				totalPDAmount.setBorder(0);
				totalPDAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalPlannedDisbursements()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalPDAmount.addElement(p1);
				fundingTable.addCell(totalPDAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				//TOTAL ACTUAL DISBURSEMENT
				PdfPCell totalAD=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL ACTUAL DISBURSEMENT:",locale,siteId),plainFont));
				totalAD.setColspan(2);
				totalAD.setBorder(0);
				totalAD.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalAD);						
				PdfPCell totalADAmount=new PdfPCell();
				totalADAmount.setBorder(0);
				totalADAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalDisbursements()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalADAmount.addElement(p1);
				fundingTable.addCell(totalADAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				//TOTAL PLANNED EXPENDITURES
				PdfPCell totalPE=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL PLANNED EXPENDITURES:",locale,siteId),plainFont));
				totalPE.setColspan(2);
				totalPE.setBorder(0);
				totalPE.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalPE);						
				PdfPCell totalPEAmount=new PdfPCell();
				totalPEAmount.setBorder(0);
				totalPEAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalPlannedExpenditures()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalPEAmount.addElement(p1);
				fundingTable.addCell(totalPEAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				//TOTAL ACTUAL EXPENDITURES
				PdfPCell totalAE=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL ACTUAL EXPENDITURES:",locale,siteId),plainFont));
				totalAE.setColspan(2);
				totalAE.setBorder(0);
				totalAE.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalAE);						
				PdfPCell totalAEAmount=new PdfPCell();
				totalAEAmount.setBorder(0);
				totalAEAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalExpenditures()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalAEAmount.addElement(p1);
				fundingTable.addCell(totalAEAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				//TOTAL ACTUAL DISBURSMENT ORDERS:
				PdfPCell totalADO=new PdfPCell(new Paragraph(TranslatorWorker.translateText("TOTAL ACTUAL DISBURSMENT ORDERS:",locale,siteId),plainFont));
				totalADO.setColspan(2);
				totalADO.setBorder(0);
				totalADO.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(totalADO);						
				PdfPCell totalADOAmount=new PdfPCell();
				totalADOAmount.setBorder(0);
				totalADOAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getTotalActualDisbursementsOrders()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				totalADOAmount.addElement(p1);
				fundingTable.addCell(totalADOAmount);
				//empty cell
				buildEmptyCell(fundingTable);
				//UNDISBURSED BALANCE:
				PdfPCell undBal=new PdfPCell(new Paragraph(TranslatorWorker.translateText("UNDISBURSED BALANCE:",locale,siteId),plainFont));
				undBal.setColspan(2);
				undBal.setBorder(0);
				undBal.setBackgroundColor(new Color(221,221,221));
				fundingTable.addCell(undBal);						
				PdfPCell undBalAmount=new PdfPCell();
				undBalAmount.setBorder(0);
				undBalAmount.setBackgroundColor(new Color(221,221,221));
				p1=new Paragraph(myForm.getFunding().getUnDisbursementsBalance()+myForm.getCurrCode(),plainFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				undBalAmount.addElement(p1);
				fundingTable.addCell(undBalAmount);
			}				
		}
		PdfPCell fundingCell=new PdfPCell(fundingTable);
		fundingCell.setBorder(0);
		mainLayout.addCell(fundingCell);
		return fundingTable;
	}

	private void buildEmptyCell(PdfPTable fundingTable) {
		PdfPCell lineCell;
		lineCell=new PdfPCell(new Paragraph(0));
		lineCell.setBorder(0);
		lineCell.setColspan(3);
		fundingTable.addCell(lineCell);
	}

	private void buildFundingInfoInnerTable(FundingDetail fd,PdfPTable infoTable) throws WorkerException {
		infoTable.getDefaultCell().setBorder(0);
		PdfPCell innerCell=new PdfPCell();
		innerCell.setBorder(0);
		innerCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText(fd.getAdjustmentTypeName(),locale,siteId),plainFont));
		innerCell.setBorder(0);
		infoTable.addCell(innerCell);
		innerCell=new PdfPCell(new Paragraph(fd.getTransactionDate(),plainFont));
		innerCell.setBorder(0);
		infoTable.addCell(innerCell);
		innerCell=new PdfPCell(new Paragraph(fd.getTransactionAmount()+fd.getCurrencyCode(),plainFont));
		innerCell.setBorder(0);
		infoTable.addCell(innerCell);											
		innerCell=new PdfPCell(new Paragraph(fd.getFormattedRate(),plainFont));
		innerCell.setBorder(0);
		infoTable.addCell(innerCell);
	}
	
	private String  getEditTagValue(HttpServletRequest request,String editKey) throws Exception{
		Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site.getSiteId(),editKey,RequestUtils.getNavigationLanguage(request).getCode());
        if (editorBody == null) {
        	editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site.getSiteId(),editKey,SiteUtils.getDefaultLanguages(site).getCode());
        }
        return editorBody;
	}
	
	/**
	 * builds donor,MOFED,Sec.Ministry and Proj.Coord. Contacts info output	
	 */
	private void buildContactInfoOutput(PdfPTable mainLayout,String contactType, Collection<AmpActivityContact> contacts) throws WorkerException{
		PdfPCell cell1=new PdfPCell();
		cell1.setBorder(0);
		cell1.setBackgroundColor(new Color(244,244,242));
		Paragraph paragraph=new Paragraph(TranslatorWorker.translateText(contactType, locale, siteId),titleFont);
		paragraph.setAlignment(Element.ALIGN_RIGHT);
		cell1.addElement(paragraph);
		mainLayout.addCell(cell1);
		
		PdfPCell cell2=new PdfPCell();
		cell2.setBorder(0);
		cell2.setBackgroundColor(new Color(255,255,255));
		if(contacts!=null && contacts.size()>0){
			String output="";
			for (AmpActivityContact cont : contacts) {
				output+=cont.getContact().getName()+" "+cont.getContact().getLastname()+"- "+cont.getContact().getEmail()+ "\n";
			}
			paragraph=new Paragraph(output,plainFont);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			cell2.addElement(paragraph);
		}
		mainLayout.addCell(cell2);
	}
	
	/**
	 * builds all related organisations Info that should be exported to PDF
	 */
	private void buildRelatedOrganisationsOutput(PdfPTable relatedOrgsTable, String orgType , Collection<AmpOrganisation> orgs) throws WorkerException{
		Paragraph paragraph=new Paragraph(new Paragraph(new Phrase(TranslatorWorker.translateText(orgType, locale, siteId)+":",titleFont)));
		PdfPCell orgTypeCell=new PdfPCell(paragraph);
		orgTypeCell.setBorder(0);
		orgTypeCell.setBackgroundColor(new Color(255,255,255));
		relatedOrgsTable.addCell(orgTypeCell);
		
		if(orgs!=null && orgs.size()>0){
			PdfPCell respOrgCell=new PdfPCell();			
			respOrgCell.setBorder(1);
			respOrgCell.setBorderColor(new Color(201,201,199));
			com.lowagie.text.List orgList=new com.lowagie.text.List(false); //not numbered list
			orgList.setListSymbol(new Chunk("\u2022"));
			for (AmpOrganisation org : orgs) {
				ListItem item=new ListItem(org.getName(),plainFont);
				orgList.add(item);
			}
			respOrgCell.addElement(orgList);
			relatedOrgsTable.addCell(respOrgCell);
		}
		PdfPCell emptyCell=new PdfPCell(new Paragraph(" "));
		emptyCell.setBorder(0);
		relatedOrgsTable.addCell(emptyCell);
	}
	
	/**
	 * builds commitments, expenditures, disbursement data output
	 */
	private void buildFinanceInfoOutput(PdfPTable nestedTable,String elemntName, List<FundingDetail> listToIterate) throws WorkerException,DocumentException{
		PdfPCell cell=new PdfPCell();
		cell.setBorder(0);
		Paragraph paragraph=new Paragraph(elemntName,plainFont);
		paragraph.setAlignment(Element.ALIGN_LEFT);
		cell.addElement(paragraph);
		cell.setBackgroundColor(new Color(255,255,255));
		cell.setBorder(0);
		nestedTable.addCell(cell);
		
		PdfPTable fdTable=new PdfPTable(4);
		fdTable.setWidths(new float[]{2f,2f,2f,1f});
		for (FundingDetail fd : listToIterate) {			
			cell=new PdfPCell();
			paragraph=new Paragraph(TranslatorWorker.translateText(fd.getAdjustmentTypeName(),locale,siteId),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
			
			cell=new PdfPCell();
			paragraph=new Paragraph(fd.getTransactionDate(),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
			
			cell=new PdfPCell();
			paragraph=new Paragraph(fd.getTransactionAmount()+fd.getCurrencyCode(),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
			
			cell=new PdfPCell();
			paragraph=new Paragraph(fd.getFormattedRate()!=null?fd.getFormattedRate():" ",plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
		}
		nestedTable.addCell(fdTable);
	}

	private String buildProgramsOutput(List<AmpActivityProgram> programs) {
		String result="";
		for (AmpActivityProgram pr :programs) {			
			result+=pr.getHierarchyNames()+" "+pr.getProgramPercentage()+"% \n";
		}
		return result;
	}
	
	public class PdfPTableEvents implements PdfPTableEvent {
		/**
		 * @see com.lowagie.text.pdf.PdfPTableEvent#tableLayout(com.lowagie.text.pdf.PdfPTable,
		 *      float[][], float[], int, int, com.lowagie.text.pdf.PdfContentByte[])
		 */
		public void tableLayout(PdfPTable table, float[][] width, float[] height,int headerRows, int rowStart, PdfContentByte[] canvas) {
			// widths of the different cells of the first row
			float widths[] = width[0];	 
			PdfContentByte cb = canvas[PdfPTable.TEXTCANVAS];
			cb.saveState();
			// border for the complete table
			cb.setLineWidth(1);
			cb.setRGBColorStroke(0, 0, 0);
			cb.rectangle(widths[0], height[height.length - 1],widths[widths.length - 1] - widths[0], height[0]- height[height.length - 1]);
			cb.stroke();			
			cb.restoreState();
		}
	}
	
}