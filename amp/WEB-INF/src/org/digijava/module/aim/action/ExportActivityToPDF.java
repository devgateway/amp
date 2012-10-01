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
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
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
import org.digijava.module.aim.util.ExportActivityToPdfUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
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
/**
 * Export Activity to PDF
 * @author Dare
 */
public class ExportActivityToPDF extends Action {
	
	private static Logger logger = Logger.getLogger(ExportActivityToPDF.class);
	private static final com.lowagie.text.Font plainFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11,Font.NORMAL);
	private static final com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11,Font.BOLD);

	private static final String [] fundingCommitmentsFMfields={"Adjustment Type Commitment","Date Commitment","Amount Commitment","Currency Commitment","Exchange Rate"};
	private static final String [] fundingDisbursementsFMfields={"Adjustment Type Disbursement","Date Disbursement","Amount Disbursement","Currency Disbursement"};
	private static final String [] fundingExpendituresFMfields={"Adjustment Type Expenditure","Date Expenditure","Amount Expenditure","Currency Expenditure"};
	private static final String [] fundingDisbOrdersFMfields={"Adjustment Type of Disbursement Order","Date of Disbursement Order","Amount of Disbursement Order","Currency of Disbursement Order"};
	
	private static final String [] componentCommitmentsFMfields={"/Activity Form/Components/Component/Components Commitments","/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount","/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency","/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date"};
	private static final String [] componentDisbursementsFMfields={"/Activity Form/Components/Component/Components Disbursements","/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount","/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency","/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date"};
	private static final String [] componentExpendituresFMfields={"/Activity Form/Components/Component/Components Expeditures","/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Amount","/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Currency","/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Transaction Date"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		EditActivityForm myForm=(EditActivityForm)form;
		Long siteId=null;
		String locale=null;
		ServletContext ampContext = null;
		
		ampContext = getServlet().getServletContext();
		//to know whether print happens from Public View or not
		HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
		Long actId=null;
		AmpActivityVersion activity=null;
		if(request.getParameter("activityid")!=null){
			actId=new Long(request.getParameter("activityid"));
		}
		
        response.setContentType("application/pdf; charset=UTF-8");
        //response.setHeader("content-disposition", "attachment;filename=activity.pdf");
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
			AmpCategoryValue catVal=null;
			String translatedValue="";
			String output="";
			String columnName="";
			String columnVal="";
			
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
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Title", ampContext)){
				columnName=TranslatorWorker.translateText("Activity Name",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,activity.getName());
			}
			
			//AMPID cells
			if(FeaturesUtil.isVisibleField("AMP ID", ampContext)){
				columnName=TranslatorWorker.translateText("AMP ID",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,activity.getAmpId());
			}

			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Reason", ampContext)){
				columnName=TranslatorWorker.translateText("Status",request);
				columnVal="";
				catVal = null;
				Long statusId = myForm.getIdentification().getStatusId();
				if(statusId!=null && statusId!=0){
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(statusId);
				}					
				if(catVal!=null){
					columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
				}
				if(myForm.getIdentification().getStatusReason() != null){
					columnVal += processHtml(request, myForm.getIdentification().getStatusReason()); 
				}
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			
			//contract Number
			if(FeaturesUtil.isVisibleField("Contract Number", ampContext)){
				columnName=TranslatorWorker.translateText("Contract Number",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,activity.getConvenioNumcont());
			}
			//project comments
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Comments", ampContext)){
				columnName=TranslatorWorker.translateText("Project Comments",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getProjectComments()));
			}			
			
			//objective
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective", ampContext)){
				columnName=TranslatorWorker.translateText("Objectives",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getObjective()));
			}
			
			//objective comments
			HashMap allComments = new HashMap();
			if(teamMember!=null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments", ampContext)){ //Objective Comments shouldn't show up on Publc View
				ArrayList<AmpComments> colAux	= null;
	            Collection ampFields = DbUtil.getAmpFields();
	            
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
					if(key.equalsIgnoreCase("Objective Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Assumption", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Assumption", locale, siteId)+" :",titleFont));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
						}					
					}else if(key.equalsIgnoreCase("Objective Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Verification", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Verification", locale, siteId)+" :",titleFont));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
						}					
					}else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators", ampContext)) {
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
			}
			//Description cell
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Description", ampContext)){
				columnName=TranslatorWorker.translateText("Description",locale,siteId);				
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getDescription()));
			}
			//Lessons learned
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Lessons Learned", ampContext)){
				columnName=TranslatorWorker.translateText("Lessons Learned",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getLessonsLearned()));
			}
			//Project Impact
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Impact", ampContext)){
				columnName=TranslatorWorker.translateText("Project Impact",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getProjectImpact()));
			}
			//Activity Summary
			if(activity.getActivitySummary()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Summary", ampContext)){
				columnName=TranslatorWorker.translateText("Activity Summary",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getActivitySummary()));
			}
			//Contracting Arrangements
			if(activity.getContractingArrangements()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Contracting Arrangements", ampContext)){
				columnName=TranslatorWorker.translateText("Contracting Arrangements",locale,siteId);
				columnVal=processEditTagValue(request, activity.getContractingArrangements());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			//Conditionality and Sequencing
			if(activity.getCondSeq()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionality and Sequencing", ampContext)){			
				columnName=TranslatorWorker.translateText("Conditionality and Sequencing",locale,siteId);
				columnVal=processEditTagValue(request, activity.getCondSeq());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}			
			//Linked Activities
			if(activity.getLinkedActivities()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Linked Activities", ampContext)){			
				columnName=TranslatorWorker.translateText("Linked Activities",locale,siteId);
				columnVal=processEditTagValue(request, activity.getLinkedActivities());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			//Conditionalities
			if(activity.getConditionality()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionalities", ampContext)){
				columnName=TranslatorWorker.translateText("Conditionalities",locale,siteId);
				columnVal=processEditTagValue(request, activity.getConditionality());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			//Project Management
			if(activity.getProjectManagement()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Management", ampContext)){
				columnName=TranslatorWorker.translateText("Project Management",locale,siteId);
				columnVal=processEditTagValue(request, activity.getProjectManagement());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			//Purpose cell
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose", ampContext)){
				columnName=TranslatorWorker.translateText("Purpose",locale,siteId);
				columnVal=processEditTagValue(request, activity.getPurpose());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
				
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
			}
			
			
			// results cell
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results", ampContext)){
				columnName=TranslatorWorker.translateText("Results",locale,siteId);
				columnVal=processEditTagValue(request, activity.getResults());
				createGeneralInfoRow(mainLayout,columnName,columnVal);
			}
			/**
			 *  Results Comments
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments", ampContext)){
				PdfPTable resultsCommentsTable=new PdfPTable(2);
		           resultsCommentsTable.getDefaultCell().setBorder(0);
		           for (Object commentKey : allComments.keySet()) {            	
					String key=(String)commentKey;
					List<AmpComments> values=(List<AmpComments>)allComments.get(key);
					if(key.equalsIgnoreCase("Results Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Assumption", ampContext)){
						for (AmpComments value : values) {
							resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Assumption", locale, siteId)+" :",titleFont));
							resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
						}					
					}else if(key.equalsIgnoreCase("Results Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Verification", ampContext)){
						for (AmpComments value : values) {
							resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Verification", locale, siteId)+" :",titleFont));
							resultsCommentsTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment(), locale, siteId),plainFont));
						}					
					}else if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")&& FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators", ampContext)) {
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
			}
			
			/**
			 * Accession Instrument cell
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Accession Instrument", ampContext)){				
				if(myForm.getIdentification().getAccessionInstrument()!=null && myForm.getIdentification().getAccessionInstrument().longValue()>0){
					columnName=TranslatorWorker.translateText("Accession Instrument",locale,siteId);
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getAccessionInstrument());
					if(catVal!=null){
						columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					}
					createGeneralInfoRow(mainLayout,columnName,columnVal);					
				}
			}			
			
			// A.C. Chapter cell
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/A.C. Chapter", ampContext)){
				if(myForm.getIdentification().getAcChapter()!=null && myForm.getIdentification().getAcChapter().longValue()>0){
					columnName=TranslatorWorker.translateText("A.C. Chapter",locale,siteId);
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getAcChapter());
					if(catVal!=null){
						columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					}
					createGeneralInfoRow(mainLayout,columnName,columnVal);
				}
			}			
			
			//Cris Number
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Cris Number", ampContext)){
				columnName=TranslatorWorker.translateText("Cris Number",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,activity.getCrisNumber());
			}			
			
			//Project Category	
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category", ampContext)){
				if(myForm.getIdentification().getProjectCategory()!=null && myForm.getIdentification().getProjectCategory().longValue()>0){
					columnName=TranslatorWorker.translateText("Project Category",locale,siteId);
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getIdentification().getProjectCategory());
					if(catVal!=null){
						columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					}
					createGeneralInfoRow(mainLayout,columnName,columnVal);
				}
			}
			
			
			//Budget
			if(FeaturesUtil.isVisibleField("Budget", ampContext)){
				String budget="";
				if(activity.getBudget()!=null && activity.getBudget() == null){
					budget="Activity is Off Budget";
				}else if(activity.getBudget()!=null && activity.getBudget() != null){
					budget="Activity is On Budget";
				}
				
				if(budget.length()>0){
					columnName=TranslatorWorker.translateText("Budget",locale,siteId);
					createGeneralInfoRow(mainLayout,columnName,budget);
				}
			}					
			
			/**
			 * Humanitarian Aid
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Humanitarian Aid", ampContext)){
				String value="";
				if(activity.getHumanitarianAid()!=null && activity.getHumanitarianAid()){
					value="Yes";
				}else if(activity.getHumanitarianAid()!=null && ! activity.getHumanitarianAid()){
					value="No";
				}
				
				if(value.length()>0){
					columnName=TranslatorWorker.translateText("Humanitarian Aid",locale,siteId);
					createGeneralInfoRow(mainLayout,columnName,value);
				}
			}			
			
			//Organizations and Project IDs
			if(FeaturesUtil.isVisibleField("Organizations and Project ID", ampContext)){
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
			}
			 
			
            //Planning
			if(FeaturesUtil.isVisibleModule("/Activity Form/Planning", ampContext)){			
				String outputValue="";
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Line Ministry Rank", ampContext)){
					outputValue=TranslatorWorker.translateText("Line Ministry Rank", locale, siteId)+ "\t: ";
					if(activity.getLineMinRank()!=null && activity.getLineMinRank().intValue()>0){
                                               outputValue+=(activity.getLineMinRank())+"\n";
                                             
                                                
					}else{
						outputValue+="\n";
					}
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Ministry of Planning Rank", ampContext)){
					outputValue+=TranslatorWorker.translateText("Ministry of Planning Rank", locale, siteId)+ "\t: ";
					if(activity.getPlanMinRank()!=null && activity.getPlanMinRank().intValue()>0){
                                            outputValue+=(activity.getPlanMinRank())+"\n";	    
					}else{
						outputValue+="\n";
					}
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Start Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Proposed Start Date", locale, siteId)+ "\t: " + myForm.getPlanning().getOriginalStartDate()+"\n";
				}

				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Start Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Actual Start Date ", locale, siteId)+ "\t: " +myForm.getPlanning().getRevisedStartDate() +"\n";
				}

				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Approval Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Proposed Approval Date ", locale, siteId)+ "\t: " + myForm.getPlanning().getOriginalAppDate()+"\n";
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Approval Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Actual Approval Date ", locale, siteId)+ "\t: " + myForm.getPlanning().getRevisedAppDate()+"\n";
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Contracting", ampContext)){
					outputValue+=TranslatorWorker.translateText("Final Date for Contracting ", locale, siteId)+ "\t: " + myForm.getPlanning().getContractingDate()+"\n";
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Disbursements", ampContext)){
					outputValue+=TranslatorWorker.translateText("Final Date for Disbursements ", locale, siteId)+ "\t: " + myForm.getPlanning().getDisbursementsDate()+"\n";
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Completion Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Proposed Completion Date ", locale, siteId)+ "\t: " +myForm.getPlanning().getProposedCompDate() +"\n";
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Completion Date", ampContext)){
					outputValue+=TranslatorWorker.translateText("Actual Completion Date", locale, siteId)+ "\t: " +myForm.getPlanning().getCurrentCompDate() +"\n";
				}		
				if(FeaturesUtil.isVisibleField("Duration of Project", ampContext)){
					String durationStr = myForm.getPlanning().getProjectPeriod() != null ? myForm.getPlanning().getProjectPeriod().toString(): "";
					outputValue+=TranslatorWorker.translateText("Duration of Project", locale, siteId)+ "\t: " + durationStr +"\n";
				}
				
				columnName=TranslatorWorker.translateText("Planning",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,outputValue);
				
				//status
                /* No Fields. Disabling temporary
				if(FeaturesUtil.isVisibleField("Status", ampContext)){
					columnName=TranslatorWorker.translateText("Status",locale,siteId);
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getPlanning().getStatusId());
					if(catVal!=null){
						columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					}
					createGeneralInfoRow(mainLayout,columnName,columnVal+"\n"+myForm.getPlanning().getStatusReason());				
				}
				*/
			}			
			
			//References
			if(FeaturesUtil.isVisibleModule("References", ampContext)){
				Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false, request);

	        	if (catValues!=null){	        		
	            	ReferenceDoc[] refDocs=myForm.getDocuments().getReferenceDocs();
	            	output="";
	        		if(refDocs!=null){
	        			for (ReferenceDoc referenceDoc : refDocs) {
	        				if(referenceDoc.getComment()!=null){
	        					output+=referenceDoc.getCategoryValue()+"\n";
	        				}        				
						}
	        		}	        		
	        		columnName=TranslatorWorker.translateText("References",locale,siteId);
					createGeneralInfoRow(mainLayout,columnName,output);
	        	}
			}
			
			//LOCATIONS
        	if(FeaturesUtil.isVisibleModule("/Activity Form/Location", ampContext)){
        		//locations
    			if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location", ampContext)){
    				if(myForm.getLocation().getSelectedLocs()!=null){
    	    			output="";
    	    			for (Location loc  : myForm.getLocation().getSelectedLocs()) {
    	    				for(String locName : loc.getAncestorLocationNames()){
    	    					output+="["+locName+"] ";
    	    				}
    	    				
    						output+="\t "+loc.getPercent()+"% \n";
    					}    					
    	        	}
    				columnName=TranslatorWorker.translateText("Locations",locale,siteId);
					createGeneralInfoRow(mainLayout,columnName,output);
    			}
            	
            	//level
    			if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Level", ampContext)){
    				translatedValue="";    				
    				if(myForm.getLocation()!=null && myForm.getLocation().getLevelId()!=null && myForm.getLocation().getLevelId()>0){    	        		
    	    			catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getLevelId());
    					if(catVal!=null){
    						translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
    					}
    	        	}
    				
    				columnName=TranslatorWorker.translateText("Level",locale,siteId);
					catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getLevelId());
					if(catVal!=null){
						columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
					}
					createGeneralInfoRow(mainLayout,columnName,translatedValue);
    			}
        	}
        	
        	//Sector
        	if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors", ampContext)){
        		output="";
        		PdfPCell sectorCell1=new PdfPCell();
            	p1=new Paragraph(TranslatorWorker.translateText("Sectors",locale,siteId),titleFont);
            	p1.setAlignment(Element.ALIGN_RIGHT);
    			sectorCell1.addElement(p1);
    			sectorCell1.setBackgroundColor(new Color(244,244,242));
    			sectorCell1.setBorder(0);
    			mainLayout.addCell(sectorCell1);
    			
    			
    			String primary="Primary Sectors: ";
    			String secondary= "Secondary Sectors: "; 
    			
    			List<AmpClassificationConfiguration> classificationConfigs=SectorUtil.getAllClassificationConfigs();
    			for (AmpClassificationConfiguration configuration : classificationConfigs) {
    				if(myForm.getSectors().getActivitySectors()!=null){
    					for (ActivitySector actSect : myForm.getSectors().getActivitySectors()) {
    						String val="";
    						if(FeaturesUtil.isVisibleField(configuration.getName()+" Sector", ampContext)){
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
    						}
    						
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
    			}
    			if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors/Primary Sectors", ampContext)){
    				output+=primary+"\n";
    			}
    			if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors/Secondary Sectors", ampContext)){
    				output+=secondary;
    			}
    			PdfPCell sectorCell2=new PdfPCell();
    			p1=new Paragraph(output,plainFont);
    			sectorCell2.addElement(p1);
    			sectorCell2.setBorder(0);
    			mainLayout.addCell(sectorCell2);
        	}
        	
			
			//Components
			Collection<ActivitySector> components=myForm.getComponents().getActivityComponentes();
			if(components!=null){
				String result="";
				columnName=TranslatorWorker.translateText("Components",locale,siteId);
				for (ActivitySector component : components) {
					result+=component.getSectorName()+" " + component.getSectorPercentage()+"% \n";
				}
				createGeneralInfoRow(mainLayout,columnName,result);
			}
			
			//NPD Programs
			if(FeaturesUtil.isVisibleFeature("NPD Programs", ampContext)){
				if(FeaturesUtil.isVisibleModule("/Activity Form/Program/National Plan Objective", ampContext)){
					//National Plan Objective
					if(myForm.getPrograms().getNationalPlanObjectivePrograms()!=null){
						columnName=TranslatorWorker.translateText("National Plan Objective",locale,siteId);
						String result= buildProgramsOutput(myForm.getPrograms().getNationalPlanObjectivePrograms());
						createGeneralInfoRow(mainLayout,columnName,result);
					}
				}
				
				if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Primary Programs", ampContext)){
					//Primary Programs
					if(myForm.getPrograms().getPrimaryPrograms()!=null){
						columnName=TranslatorWorker.translateText("Primary Programs",locale,siteId);
						String result= buildProgramsOutput(myForm.getPrograms().getPrimaryPrograms());
						createGeneralInfoRow(mainLayout,columnName,result);						
					}
				}

				if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Secondary Programs", ampContext)){
					//secondary Programs
					if(myForm.getPrograms().getSecondaryPrograms()!=null){
						columnName=TranslatorWorker.translateText("Secondary Programs",locale,siteId);
						String result= buildProgramsOutput(myForm.getPrograms().getSecondaryPrograms());
						createGeneralInfoRow(mainLayout,columnName,result);
					}
				}
			}
			
			/**
			 * funding
			 */
			if(teamMember!=null && FeaturesUtil.isVisibleModule("Funding", ampContext)){ //funding Information shouldn't be visible on Public View
				//PdfPTable fundingTable = buildFundingInformationPart(myForm,mainLayout);
				buildFundingInformationPart(myForm,mainLayout,locale,siteId,ampContext);
			}			
			
			/**
			 * Regional Funding
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding", ampContext)){
				PdfPCell regFundingCell1=new PdfPCell();
				p1=new Paragraph(TranslatorWorker.translateText("Regional Funding",locale,siteId),titleFont);
				p1.setAlignment(Element.ALIGN_RIGHT);
				regFundingCell1.addElement(p1);
				regFundingCell1.setBackgroundColor(new Color(244,244,242));
				regFundingCell1.setBorder(0);
				mainLayout.addCell(regFundingCell1);
				
				//now we should create nested table and add it as second cell in mainLayout
				PdfPTable regFundingNested = new PdfPTable(1);
				regFundingNested.setWidthPercentage(80);
				regFundingNested.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(myForm.getFunding().getRegionalFundings()!=null){
					for (RegionalFunding regFunf : (Collection<RegionalFunding>)myForm.getFunding().getRegionalFundings()) {
						//create first row (Region Name)
						PdfPCell nestedCell1=new PdfPCell();
						nestedCell1.setBorder(0);
						p1=new Paragraph(TranslatorWorker.translateText(regFunf.getRegionName(),locale,siteId),titleFont);
						p1.setAlignment(Element.ALIGN_LEFT);
						nestedCell1.addElement(p1);
						regFundingCell1.setBackgroundColor(new Color(255,255,255));
						regFundingCell1.setBorder(0);
						regFundingNested.addCell(nestedCell1);
						
						if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Commitments", ampContext) && regFunf.getCommitments()!=null){ //create commitment row
							PdfPTable commitmentsTable=buildRegionalFundingInfoOutput(TranslatorWorker.translateText("Commitment",locale,siteId),(List<FundingDetail>)regFunf.getCommitments(),locale,siteId,ampContext);
							regFundingNested.addCell(commitmentsTable);						
						}
						
						if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Disbursements", ampContext) && regFunf.getDisbursements()!=null){ //create disbursments row 
							PdfPTable disbTable=buildRegionalFundingInfoOutput( TranslatorWorker.translateText("Disbursment",locale,siteId),(List<FundingDetail>)regFunf.getDisbursements(),locale,siteId,ampContext);
							regFundingNested.addCell(disbTable);
						}
						
						if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Expenditures", ampContext) && regFunf.getExpenditures()!=null){ //create expenditure row
							PdfPTable expTable=buildRegionalFundingInfoOutput( TranslatorWorker.translateText("Expenditures",locale,siteId), (List<FundingDetail>)regFunf.getExpenditures(),locale,siteId,ampContext);
							regFundingNested.addCell(expTable);
						}
					}				
				}
				PdfPCell regFundCell=new PdfPCell(regFundingNested);
				regFundCell.setBorder(0);
				mainLayout.addCell(regFundCell);				
			}			

			/**
			 * components
			 */			
			buildComponentsPart(myForm, mainLayout,locale,siteId,ampContext);						
			
			/**
			 * Issues
			 */
			if(FeaturesUtil.isVisibleModule("Issues", ampContext)){
				buildIssuesPart(myForm, mainLayout,locale,siteId,ampContext);
			}			
			
			/**
			 * related documents
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Related Documents", ampContext)){
				buildRelatedDocsPart(myForm, mainLayout, event,locale,siteId,ampContext);
			}			
			
			/**
			 * Related organizations
			 */
			if(FeaturesUtil.isVisibleModule("Organizations", ampContext)){
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
				//Responsible Organizations
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Responsible Organization", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Responsible Organization",myForm.getAgencies().getRespOrganisations(),locale,siteId,ampContext);
				}				
				//Executing Agency
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Executing Agency", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Executing Agency",myForm.getAgencies().getExecutingAgencies(),locale,siteId,ampContext);
				}				
				//Implementing Agency
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Implementing Agency", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Implementing Agency",myForm.getAgencies().getImpAgencies(),locale,siteId,ampContext);
				}				
				//Beneficiary Agency
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Beneficiary Agency", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Beneficiary Agency",myForm.getAgencies().getBenAgencies(),locale,siteId,ampContext);
				}				
				//Contracting Agency
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Contracting Agency", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Contracting Agency",myForm.getAgencies().getConAgencies(),locale,siteId,ampContext);
				}
				
				//Sector Group
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Sector Group", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Sector Group",myForm.getAgencies().getSectGroups(),locale,siteId,ampContext);
				}
				//Regional Group
				if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Regional Group", ampContext)){
					buildRelatedOrganisationsOutput(relatedOrgnested,"Regional Group",myForm.getAgencies().getRegGroups(),locale,siteId,ampContext);
				}			
				
				relOrgCell2.addElement(relatedOrgnested);
				mainLayout.addCell(relOrgCell2);
			}			
			
			/**
			 *	Contact Informations 
			 */
			if(FeaturesUtil.isVisibleModule("Contact Information", ampContext)){
				//Donor funding contact information
				if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Donor Contact Information", ampContext)){
					buildContactInfoOutput(mainLayout,"Donor funding contact information",myForm.getContactInformation().getDonorContacts(),locale,siteId,ampContext);
				}						
				//MOFED contact information
				if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Mofed Contact Information", ampContext)){
					buildContactInfoOutput(mainLayout,"MOFED contact information",myForm.getContactInformation().getMofedContacts(),locale,siteId,ampContext);	
				}				
				//Sec Min funding contact information
				if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Project Coordinator Contact Information", ampContext)){
					buildContactInfoOutput(mainLayout,"Sector Ministry contact information",myForm.getContactInformation().getSectorMinistryContacts(),locale,siteId,ampContext);	
				}				
				//Project Coordinator contact information
				if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Sector Ministry Contact Information", ampContext)){
					buildContactInfoOutput(mainLayout,"Proj. Coordinator contact information",myForm.getContactInformation().getProjCoordinatorContacts(),locale,siteId,ampContext);	
				}				
				//Implementing/executing agency contact information
				if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Implementing Executing Agency Contact Information", ampContext)){
					buildContactInfoOutput(mainLayout,"Implementing/Executing Agency contact information",myForm.getContactInformation().getImplExecutingAgencyContacts(),locale,siteId,ampContext);	
				}				
			}			
			
			/**
			 * Proposed Project Cost
			 */
			if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Proposed Project Cost", ampContext)){
				String costOutput="";
				columnName=TranslatorWorker.translateText("Proposed Project Cost",locale,siteId);
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
				
				createGeneralInfoRow(mainLayout,columnName,costOutput);
			}			
			
			/**
			 * Costing
			 */
			if(FeaturesUtil.isVisibleModule("Activity Costing", ampContext)){
				buildCostingPart(request, actId, mainLayout,locale,siteId,ampContext);
			}			
			
			/**
			 * Build IPA contracting
			 */
			if(FeaturesUtil.isVisibleModule("Contracting", ampContext)){
				PdfPCell ipaContract1=new PdfPCell();
				ipaContract1.setBorder(0);
				ipaContract1.setBackgroundColor(new Color(244,244,242));
				Paragraph ipaContractP=new Paragraph(TranslatorWorker.translateText("IPA Contracting", locale, siteId),titleFont);
				ipaContractP.setAlignment(Element.ALIGN_RIGHT);
				ipaContract1.addElement(ipaContractP);
				mainLayout.addCell(ipaContract1);
				
				PdfPCell ipaContracting2=new PdfPCell();
				ipaContracting2.setBorder(1);
				//inner table with two cells 
				PdfPTable ipaInnerTable=new PdfPTable(2);			
				if(myForm.getContracts().getContracts()!=null){
					for (IPAContract contract : (List<IPAContract>)myForm.getContracts().getContracts()) {
						//name
						if(FeaturesUtil.isVisibleField("Contract Name", ampContext)){
							columnName=TranslatorWorker.translateText("Contract Name",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable,columnName,contract.getContractName());
						}
						
						//description
						if(FeaturesUtil.isVisibleField("Contract Description", ampContext)){
							columnName=TranslatorWorker.translateText("Contract Description",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getDescription());
						}						
						
						//activity category
						if(FeaturesUtil.isVisibleField("Contracting Activity Category", ampContext)){
							columnName=TranslatorWorker.translateText("Activity Category",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getActivityCategory()!=null?contract.getActivityCategory().getValue():"");
						}
						
						//type
						if(FeaturesUtil.isVisibleField("Contract type", ampContext)){
							columnName=TranslatorWorker.translateText("Type",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getType()!=null?contract.getType().getValue():"");
						}
						
						//start of tendering
						if(FeaturesUtil.isVisibleField("Contracting Start of Tendering", ampContext)){
							columnName=TranslatorWorker.translateText("Type",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedStartOfTendering());
						}						
						
						//Signature of Contract
						if(FeaturesUtil.isVisibleField("Signature of Contract", ampContext)){
							columnName=TranslatorWorker.translateText("Signature of Contract",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedSignatureOfContract());
						}						
												
						columnName=TranslatorWorker.translateText("Contract Organization",locale,siteId)+":";
						// Contract Organization
						if(FeaturesUtil.isVisibleField("Contract Organization", ampContext)){							
							createContractingTblRows(ipaInnerTable, columnName,contract.getOrganization()!=null?contract.getOrganization().getName():"");
						}
						
						// Contract Organization
						if(FeaturesUtil.isVisibleField("Contracting Contractor Name", ampContext)){
							createContractingTblRows(ipaInnerTable, columnName,contract.getContractingOrganizationText());
						}						
						
						//Contract Completion
						if(FeaturesUtil.isVisibleField("Contract Completion", ampContext)){
							columnName=TranslatorWorker.translateText("Contract Completion",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedContractCompletion());
						}
						
						//Status
						if(FeaturesUtil.isVisibleField("Contracting Status", ampContext)){
							columnName=TranslatorWorker.translateText("Status",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,contract.getStatus()!=null?contract.getStatus().getValue():"");
						}					
						
						//Total Amount
						if(FeaturesUtil.isVisibleField("Total Amount", ampContext)){
							columnName=TranslatorWorker.translateText("Total Amount",locale,siteId)+":";
							output=contract.getTotalAmount()!=null? contract.getTotalAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode()  : " ";
							createContractingTblRows(ipaInnerTable, columnName,output);							
						}						
						
						//Total EC Contribution
						if(FeaturesUtil.isVisibleField("Total EC Contribution", ampContext)){
							PdfPCell totalECCont=new PdfPCell();
							totalECCont.setBorder(0);
							totalECCont.setColspan(2);
							Paragraph totalECContP=new Paragraph(TranslatorWorker.translateText("Total EC Contribution", locale, siteId)+":",titleFont);
							totalECCont.addElement(totalECContP);
							ipaInnerTable.addCell(totalECCont);
						}						
						
						//IB
						if(FeaturesUtil.isVisibleField("Contracting IB", ampContext)){
							columnName=TranslatorWorker.translateText("IB",locale,siteId)+":";
							if(contract.getTotalECContribIBAmount()!=null){
								output=contract.getTotalECContribIBAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}
						
						//INV
						if(FeaturesUtil.isVisibleField("Contracting INV", ampContext)){
							columnName=TranslatorWorker.translateText("Contracting INV",locale,siteId)+":";
							if(contract.getTotalECContribINVAmount()!=null){
								output=contract.getTotalECContribINVAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}
						
						//Total National Contribution
						if(FeaturesUtil.isVisibleField("Contracting Total National Contribution", ampContext)){
							PdfPCell totalNationalCont=new PdfPCell();
							totalNationalCont.setBorder(0);
							totalNationalCont.setColspan(2);
							Paragraph totalNationalContP=new Paragraph(TranslatorWorker.translateText("Total National Contribution", locale, siteId)+":",titleFont);
							totalNationalCont.addElement(totalNationalContP);
							ipaInnerTable.addCell(totalNationalCont);
						}						
						
						//Central
						if(FeaturesUtil.isVisibleField("Contracting Central Amount", ampContext)){
							columnName=TranslatorWorker.translateText("Contracting Central Amount",locale,siteId)+":";							
							if(contract.getTotalNationalContribCentralAmount()!=null){
								output=contract.getTotalNationalContribCentralAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//Regional
						if(FeaturesUtil.isVisibleField("Contracting Regional Amount", ampContext)){
							columnName=TranslatorWorker.translateText("Regional",locale,siteId)+":";
							if(contract.getTotalNationalContribRegionalAmount()!=null){
								output=contract.getTotalNationalContribRegionalAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//IFIs
						if(FeaturesUtil.isVisibleField("Contracting IFIs", ampContext)){
							columnName=TranslatorWorker.translateText("IFIs",locale,siteId)+":";
							if(contract.getTotalNationalContribIFIAmount()!=null){
								output=contract.getTotalNationalContribIFIAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//Total Private Contribution
						if(FeaturesUtil.isVisibleField("Total Private Contribution", ampContext)){
							PdfPCell totalPrivateCont=new PdfPCell();
							totalPrivateCont.setBorder(0);
							totalPrivateCont.setColspan(2);
							Paragraph totalPrivateContP=new Paragraph(TranslatorWorker.translateText("Total Private Contribution", locale, siteId)+":",titleFont);
							totalPrivateCont.addElement(totalPrivateContP);
							ipaInnerTable.addCell(totalPrivateCont);
						}						
						
						//IB
						if(FeaturesUtil.isVisibleField("Contracting IB", ampContext)){
							columnName=TranslatorWorker.translateText("IB",locale,siteId)+":";
							if(contract.getTotalPrivateContribAmount()!=null){
								output=contract.getTotalPrivateContribAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else if(contract.getTotalAmountCurrency()!=null){
								output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
							}else{
								output="";
							}
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//Total disbursements
						if(FeaturesUtil.isVisibleField("Total Disbursements of Contract", ampContext)){
							columnName=TranslatorWorker.translateText("Total disbursements",locale,siteId)+":";
							output=(contract.getTotalDisbursements()!=null ? contract.getTotalDisbursements().floatValue() : " ")+" ";
							output+=contract.getDibusrsementsGlobalCurrency()!=null?contract.getDibusrsementsGlobalCurrency().getCurrencyCode():myForm.getCurrCode();
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//Total Funding Disbursements
						if(FeaturesUtil.isVisibleField("Total Funding Disbursements of Contract", ampContext)){
							columnName=TranslatorWorker.translateText("Total Funding Disbursements",locale,siteId)+":";
							output=(contract.getFundingTotalDisbursements()!=null? contract.getFundingTotalDisbursements().floatValue() : " ")+" ";
							output+=contract.getDibusrsementsGlobalCurrency()!=null?contract.getDibusrsementsGlobalCurrency().getCurrencyCode():myForm.getCurrCode();
							createContractingTblRows(ipaInnerTable, columnName,output);
						}						
						
						//Contract Execution Rate
						if(FeaturesUtil.isVisibleField("Contract Execution Rate", ampContext)){
							columnName=TranslatorWorker.translateText("Contract Execution Rate",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,(contract.getExecutionRate()!=null ? contract.getExecutionRate().floatValue() : " ")+"");
						}						
						
						//Contract Execution Rate
						if(FeaturesUtil.isVisibleField("Contract Funding Execution Rate", ampContext)){
							columnName=TranslatorWorker.translateText("Contract Execution Rate",locale,siteId)+":";
							createContractingTblRows(ipaInnerTable, columnName,(contract.getFundingExecutionRate()!=null ? contract.getFundingExecutionRate().floatValue() : " ")+"");
						}						
						
						//Disbursements
						if(FeaturesUtil.isVisibleField("Disbursements", ampContext)){
							PdfPCell disbs1=new PdfPCell();
							disbs1.setBorder(0);
							Paragraph disbsP=new Paragraph(TranslatorWorker.translateText("Disbursements", locale, siteId)+":",titleFont);
							disbs1.addElement(disbsP);
							ipaInnerTable.addCell(disbs1);
							
							PdfPCell disbs2=new PdfPCell();
							disbs2.setBorder(0);
							PdfPTable disbursmentsInnerTable= new PdfPTable(3);
							if(contract.getDisbursements()!=null){
								for (IPAContractDisbursement ipaDisb : (Set<IPAContractDisbursement>)contract.getDisbursements()) {
									PdfPCell adjType=new PdfPCell();
									adjType.setBorder(0);
									adjType.addElement(new Paragraph(ipaDisb.getAdjustmentType().getValue(),plainFont));
									disbursmentsInnerTable.addCell(adjType);
									
									PdfPCell amount=new PdfPCell();
									amount.setBorder(0);
									amount.addElement(new Paragraph(ipaDisb.getAmount().floatValue() + " "+ ipaDisb.getCurrency().getCurrencyName(),plainFont));
									disbursmentsInnerTable.addCell(amount);
									
									PdfPCell disbDate=new PdfPCell();
									disbDate.setBorder(0);
									disbDate.addElement(new Paragraph(ipaDisb.getDisbDate(),plainFont));
									disbursmentsInnerTable.addCell(disbDate);
								}
							}
							
							disbs2.addElement(disbursmentsInnerTable);
							ipaInnerTable.addCell(disbs2);
						}							
						
						//Funding Disbursements
						if(FeaturesUtil.isVisibleField("Contracting Funding Disbursements", ampContext)){
							PdfPCell fundingDisbs1=new PdfPCell();
							fundingDisbs1.setBorder(0);
							Paragraph fundDisbsP=new Paragraph(TranslatorWorker.translateText("Funding Disbursements", locale, siteId)+":",titleFont);
							fundingDisbs1.addElement(fundDisbsP);
							ipaInnerTable.addCell(fundingDisbs1);
							
							PdfPCell fundingDisbs2=new PdfPCell();
							fundingDisbs2.setBorder(0);
							PdfPTable fundDisbursmentsInnerTable= new PdfPTable(4);
							
							if(myForm.getFunding()!=null){
								PdfPCell adjType=new PdfPCell();
								adjType.setBorder(0);
								adjType.addElement(new Paragraph(TranslatorWorker.translateText("Adj. Type Disb.", locale, siteId),plainFont));
								fundDisbursmentsInnerTable.addCell(adjType);
								
								PdfPCell ampuntDisb=new PdfPCell();
								ampuntDisb.setBorder(0);
								ampuntDisb.addElement(new Paragraph(TranslatorWorker.translateText("Amount Disb.", locale, siteId),plainFont));
								fundDisbursmentsInnerTable.addCell(ampuntDisb);
								
								PdfPCell currencyDisb=new PdfPCell();
								currencyDisb.setBorder(0);
								currencyDisb.addElement(new Paragraph(TranslatorWorker.translateText("Currency Disb.", locale, siteId),plainFont));
								fundDisbursmentsInnerTable.addCell(currencyDisb);
								
								PdfPCell dateDisb=new PdfPCell();
								dateDisb.setBorder(0);
								dateDisb.addElement(new Paragraph(TranslatorWorker.translateText("Date Disb.", locale, siteId),plainFont));
								fundDisbursmentsInnerTable.addCell(dateDisb);
								
								for (FundingDetail fundingDetail : myForm.getFunding().getFundingDetails()) {
									if(fundingDetail.getContract()!=null && contract.getContractName().equals(fundingDetail.getContract().getContractName()) && fundingDetail.getTransactionType()==1){
										adjType=new PdfPCell();
										adjType.setBorder(0);
										adjType.addElement(new Paragraph(fundingDetail.getAdjustmentTypeName().getValue(),plainFont));
										fundDisbursmentsInnerTable.addCell(adjType);
										
										PdfPCell amount=new PdfPCell();
										amount.setBorder(0);
										amount.addElement(new Paragraph(fundingDetail.getTransactionAmount(),plainFont));
										fundDisbursmentsInnerTable.addCell(amount);
										
										PdfPCell currency=new PdfPCell();
										currency.setBorder(0);
										currency.addElement(new Paragraph(fundingDetail.getCurrencyName(),plainFont));
										fundDisbursmentsInnerTable.addCell(currency);
										
										PdfPCell disbDate=new PdfPCell();
										disbDate.setBorder(0);
										disbDate.addElement(new Paragraph(fundingDetail.getTransactionDate(),plainFont));
										fundDisbursmentsInnerTable.addCell(disbDate);
									}
									
								}
							}
							fundingDisbs2.addElement(fundDisbursmentsInnerTable);
							ipaInnerTable.addCell(fundingDisbs2);
						}						
					}					
				}
				
				ipaContracting2.addElement(ipaInnerTable);
				mainLayout.addCell(ipaContracting2);
			}
			
			
			/**
			 * Activity created by
			 */
			if(FeaturesUtil.isVisibleField("Activity Created By", ampContext)){
				columnName=TranslatorWorker.translateText("Activity created by",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,myForm.getIdentification().getActAthFirstName()+" "+myForm.getIdentification().getActAthLastName()+"-"+myForm.getIdentification().getActAthEmail());				
			}			
			
			/**
			 * Data Source
			 */
			if(FeaturesUtil.isVisibleField("Data Source", ampContext)){
				columnName=TranslatorWorker.translateText("Data Source",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,myForm.getIdentification().getActAthAgencySource());
			}			
			
			/**
			 * Activity updated on
			 */
			if(FeaturesUtil.isVisibleField("Activity Updated On", ampContext)){
				columnName=TranslatorWorker.translateText("Updated On",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,myForm.getIdentification().getUpdatedDate());
			}			
			
			/**
			 * Activity updated by
			 */
			if(FeaturesUtil.isVisibleField("Activity Updated By", ampContext)){
				columnName=TranslatorWorker.translateText("Activity Updated By",locale,siteId);
				output="";
				if(myForm.getIdentification().getModifiedBy()!=null){
					User user=myForm.getIdentification().getModifiedBy().getUser();
					output=user.getFirstNames()+" "+user.getLastName()+"-"+user.getEmail();
				}
				createGeneralInfoRow(mainLayout,columnName,output);
			}			
			
			/**
			 *  Activity created on
			 */
			if(FeaturesUtil.isVisibleField("Activity Created On", ampContext)){
				columnName=TranslatorWorker.translateText("Created On",locale,siteId);
				createGeneralInfoRow(mainLayout,columnName,myForm.getIdentification().getCreatedDate());
			}			

// AMP-13881: commented since custom fields are not in use anymore
//			/**
//			 * Custom Fields
//			 */
//			if(myForm.getCustomFields()!=null && myForm.getCustomFields().size()>0){
//				for (CustomField<?> customField : myForm.getCustomFields()) {
//					if(FeaturesUtil.isVisibleField(customField.getFM_field(), ampContext)){
//						PdfPCell customFields1=new PdfPCell();
//						customFields1.setBorder(0);
//						customFields1.setBackgroundColor(new Color(244,244,242));
//						Paragraph customFields1P=new Paragraph(TranslatorWorker.translateText(customField.getName(), locale, siteId),titleFont);
//						customFields1P.setAlignment(Element.ALIGN_RIGHT);
//						customFields1.addElement(customFields1P);
//						mainLayout.addCell(customFields1);
//						
//						PdfPCell customFields2=new PdfPCell();
//						customFields2.setBorder(0);
//						Paragraph customFields2P=new Paragraph(" ");					
//						if(customField instanceof ComboBoxCustomField){
//							customFields2P=new Paragraph(((ComboBoxCustomField) customField).getOptions().get(customField.getValue()),plainFont);
//						}else if(customField instanceof CategoryCustomField){
//							if(((CategoryCustomField)customField).getValue()!=null && ((CategoryCustomField)customField).getValue()>0){
//								catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(((CategoryCustomField)customField).getValue());
//								if(catVal!=null){
//									translatedValue	= CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
//									customFields2P=new Paragraph(translatedValue,plainFont);
//								}
//							}					
//						}else if(customField instanceof DateCustomField){
//							customFields2P=new Paragraph(((DateCustomField) customField).getStrDate(),plainFont);
//						}else if(customField instanceof RadioOptionCustomField){
//							String outputVal=" ";
//							for (String option : ((RadioOptionCustomField) customField).getOptions().keySet()) {
//								if(option.equals(customField.getValue())){
//									outputVal+=((RadioOptionCustomField) customField).getOptions().get(option)+" ";
//								}
//							}
//							customFields2P=new Paragraph(outputVal,plainFont);
//						}else if(customField instanceof CheckCustomField){
//							if(((CheckCustomField)customField).getValue()!=null && ((CheckCustomField)customField).getValue()){
//								customFields2P=new Paragraph(((CheckCustomField)customField).getLabelTrue(),plainFont);
//							}else if(((CheckCustomField)customField).getValue()!=null && !((CheckCustomField)customField).getValue()){
//								customFields2P=new Paragraph(((CheckCustomField)customField).getLabelFalse(),plainFont);
//							}
//						}else{
//							if(customField.getValue()!=null){
//								customFields2P=new Paragraph(customField.getValue().toString(),plainFont);
//							}
//						}
//						customFields2.addElement(customFields2P);
//						mainLayout.addCell(customFields2);
//					}					
//					
//				}
//			}
			
			/**
			 * Activity - Performance
			 */
			if(FeaturesUtil.isVisibleField("Activity Performance", ampContext)){
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
			}
			
			
			/**
			 * Activity - Risk
			 */
			if(FeaturesUtil.isVisibleField("Project Risk", ampContext)){
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
				Collection<AmpIndicatorRiskRatings> risks=IndicatorUtil.getRisks(actId);
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
	
	/**
	 * create Contacting Inner table rows 
	 * @param ipaInnerTable
	 * @param columnName
	 * @param columnValue
	 * @throws WorkerException
	 */
	private void createContractingTblRows(PdfPTable ipaInnerTable, String columnName,String columnValue) throws WorkerException {
		PdfPCell cell1=new PdfPCell();
		cell1.setBorder(0);
		Paragraph descriptionP=new Paragraph(columnName,titleFont);
		cell1.addElement(descriptionP);
		ipaInnerTable.addCell(cell1);
		
		PdfPCell cell2=new PdfPCell();
		cell2.setBorder(0);
		cell2.addElement(new Paragraph(columnValue,plainFont));
		ipaInnerTable.addCell(cell2);
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
		
	
	private void buildIssuesPart(EditActivityForm myForm, PdfPTable mainLayout,String locale,Long siteId,ServletContext ampContext)	throws WorkerException {
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
				if(FeaturesUtil.isVisibleField("Issue Date", ampContext)){
					ListItem issueItem=new ListItem(new Phrase(issue.getName()+" \t"+issue.getIssueDate(),plainFont));
					issuesList.add(issueItem);
				}				
				if(FeaturesUtil.isVisibleField("Measures Taken", ampContext) && issue.getMeasures()!=null){
					com.lowagie.text.List measuresSubList=new com.lowagie.text.List(false,20);  //is not numbered list
					measuresSubList.setListSymbol("-");
					for (Measures measure : issue.getMeasures()) {
						ListItem measureItem=new ListItem(new Phrase(measure.getName(),plainFont));
						measuresSubList.add(measureItem);
						if(FeaturesUtil.isVisibleField("Actors", ampContext) && measure.getActors()!=null && measure.getActors().size()>0){
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

	private void buildCostingPart(HttpServletRequest request, Long actId,PdfPTable mainLayout,String locale,Long siteId,ServletContext ampContext) throws WorkerException, AimException {
		Paragraph p1;
		PdfPCell costingCell1=new PdfPCell();
		costingCell1.setBorder(0);
		costingCell1.setBackgroundColor(new Color(244,244,242));
		p1=new Paragraph(TranslatorWorker.translateText("Costing", locale, siteId),titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		costingCell1.addElement(p1);			
		mainLayout.addCell(costingCell1);
		
		int fmVisibleFieldsCounter=0;
		String [] costingFmfields={"Costing Activity Name","Costing Total Cost","Costing Total Contribution"};
		for(int i=0;i<costingFmfields.length;i++){
			if(FeaturesUtil.isVisibleField(costingFmfields[i], ampContext)){
				fmVisibleFieldsCounter++;
			}
		}
		PdfPCell costingCell2=new PdfPCell();		
		costingCell2.setBorder(1);
		costingCell2.setBorderColor(new Color(201,201,199));
		PdfPTable costingInnerTable=new PdfPTable(fmVisibleFieldsCounter); //table with 3 cells
		BigDecimal grandCost = new BigDecimal(0);
		BigDecimal grandContribution = new BigDecimal(0);
			if(FeaturesUtil.isVisibleField("Costing Activity Name", ampContext)){
				PdfPCell nameCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("name", locale, siteId),titleFont));
				nameCell.setBorder(0);
				nameCell.setBackgroundColor(new Color(244,244,242));
				costingInnerTable.addCell(nameCell);
			}			
			
			if(FeaturesUtil.isVisibleField("Costing Total Cost", ampContext)){
				PdfPCell totalCostCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Total Cost", locale, siteId),titleFont));
				totalCostCell.setBorder(0);
				totalCostCell.setBackgroundColor(new Color(244,244,242));
				costingInnerTable.addCell(totalCostCell);
			}			
			
			if(FeaturesUtil.isVisibleField("Costing Total Contribution", ampContext)){
				PdfPCell totalContrCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Total Contribution", locale, siteId),titleFont));
				totalContrCell.setBorder(0);
				totalContrCell.setBackgroundColor(new Color(244,244,242));
				costingInnerTable.addCell(totalContrCell);
			}
			
			
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
					if(euActivity.getTotalCostConverted() != 0d){
						grandCost=grandCost.add(new BigDecimal(euActivity.getTotalCostConverted()));
					}
					if(euActivity.getTotalContributionsConverted()!= 0d){
						grandContribution=grandContribution.add(new BigDecimal(euActivity.getTotalContributionsConverted()));
					}
					//name
					if(FeaturesUtil.isVisibleField("Costing Activity Name", ampContext)){
						PdfPCell name=new PdfPCell(new Paragraph(euActivity.getName(),titleFont));
						name.setBorder(0);
						name.setBackgroundColor(new Color(255,255,255));
						costingInnerTable.addCell(name);
					}
					
					//euAct totalsConverted
					if(FeaturesUtil.isVisibleField("Costing Total Cost", ampContext)){
						NumberFormat formatter = FormatHelper.getDecimalFormat();
						Double totalsConverted=new Double(formatter.format(euActivity.getTotalCostConverted())) ;						
						p1=new Paragraph(totalsConverted.toString(),plainFont);
						p1.setAlignment(Element.ALIGN_RIGHT);
						PdfPCell totalsConvertedCell=new PdfPCell(p1);
						totalsConvertedCell.setBorder(0);
						totalsConvertedCell.setBackgroundColor(new Color(255,255,255));
						costingInnerTable.addCell(totalsConvertedCell);
					}
					
					//totalContributionsConverted
					if(FeaturesUtil.isVisibleField("Costing Total Contribution", ampContext)){
						Double totalContributionsConverted=new Double(new DecimalFormat("### ### ###.##").format(euActivity.getTotalContributionsConverted()));
						p1=new Paragraph(totalContributionsConverted.toString(),plainFont);
						p1.setAlignment(Element.ALIGN_RIGHT);
						PdfPCell totalContConvertedCell=new PdfPCell(p1);
						totalContConvertedCell.setBorder(0);
						totalContConvertedCell.setBackgroundColor(new Color(255,255,255));
						costingInnerTable.addCell(totalContConvertedCell);
					}
					
					PdfPCell anotherInfo=new PdfPCell();
					anotherInfo.setBorder(0);
					anotherInfo.setColspan(3);
					String euInfo="";
					if(FeaturesUtil.isVisibleField("Costing Inputs", ampContext) && euActivity.getInputs()!=null){
						euInfo+= TranslatorWorker.translateText("Inputs", locale, siteId)+":"+ euActivity.getInputs() + "\n";
					}
					if(FeaturesUtil.isVisibleField("Costing Assumptions", ampContext) && euActivity.getAssumptions()!=null){
						euInfo+= TranslatorWorker.translateText("Assumptions", locale, siteId)+":"+ euActivity.getAssumptions() + "\n";
					}
					if(FeaturesUtil.isVisibleField("Costing Progress", ampContext) && euActivity.getProgress()!=null){
						euInfo+= TranslatorWorker.translateText("Progress", locale, siteId)+":"+ euActivity.getProgress() + "\n";
					}
					if(FeaturesUtil.isVisibleField("Costing Due Date", ampContext) && euActivity.getDueDate()!=null){
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
				
				NumberFormat formatter = FormatHelper.getDecimalFormat();
				if(FeaturesUtil.isVisibleField("Grand Total Cost", ampContext)){
					PdfPCell grandCostCell=new PdfPCell();
					grandCostCell.setBorder(0);
					
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
				}				
				
				if(FeaturesUtil.isVisibleField("Costing Contribution Gap", ampContext)){
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
				
			}
		costingCell2.addElement(costingInnerTable);

		mainLayout.addCell(costingCell2);
	}

	private void buildRelatedDocsPart(EditActivityForm myForm,PdfPTable mainLayout, PdfPTableEvents event,String locale,Long siteId,ServletContext ampContext) throws WorkerException {
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

	private void buildComponentsPart(EditActivityForm myForm,PdfPTable mainLayout,String locale,Long siteId,ServletContext ampContext)	throws WorkerException, DocumentException {
		Paragraph p1;
		if(GlobalSettings.getInstance().getShowComponentFundingByYear()!=null && FeaturesUtil.isVisibleModule("/Activity Form/Components", ampContext)){
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
						p1=new Paragraph(TranslatorWorker.translateText("Component Funding",locale,siteId),titleFont);
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
							//String[] fmFields=new String[] {"Components Actual/Planned Commitments","Components Amount Commitments","Components Currency Commitments","Components Date Commitments"};
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Commitment",locale,siteId),(List) comp.getCommitments(),componentCommitmentsFMfields,locale,siteId,ampContext);
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
							//String[] fmFields=new String[] {"Components Actual/Planned Disbursements","Components Amount Disbursements","Components Currency Disbursements","Components Date Disbursements"};
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Disbursment",locale,siteId),(List) comp.getDisbursements(),componentDisbursementsFMfields,locale,siteId,ampContext);
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
							//final String[] fmFields=new String[] {"Components Actual/Planned Expenditures","Components Amount Expenditures","Components Currency Expenditures","Components Date Expenditures"};
							buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Expenditures",locale,siteId),(List) comp.getExpenditures(),componentExpendituresFMfields,locale,siteId,ampContext);
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
							//fundingTable.addCell(amountsInThousandsCell1);
							componentsNestedTable.addCell(amountsInThousandsCell1);
						}
						//physical progress
						if(FeaturesUtil.isVisibleField("Components Physical Progress", ampContext)){
							PdfPCell phProgressNestedCell=new PdfPCell();
							p1=new Paragraph(TranslatorWorker.translateText("Physical progress of the component",locale,siteId),titleFont);
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
						}
						
					}else if(GlobalSettings.getInstance().getShowComponentFundingByYear() && FeaturesUtil.isVisibleModule("Components Resume", ampContext)){ //true case
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
							Map<String,Double> myMap=comp.getFinanceByYearInfo().get(key); //value of the  comp.getFinanceByYearInfo() Map
							PdfPCell pcs1=new PdfPCell();
							p1=new Paragraph(TranslatorWorker.translateText("Planned Commitments Sum", locale,siteId),plainFont);
							p1.setAlignment(Element.ALIGN_LEFT);
							pcs1.addElement(p1);
							pcs1.setBackgroundColor(new Color(255,255,255));
							pcs1.setBorder(0);
							financeNestedTable.addCell(pcs1);								
							PdfPCell pcs2=new PdfPCell();
							Double a=myMap.get("MontoProgramado");
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

	private void buildFundingInformationPart(EditActivityForm myForm,PdfPTable mainLayout,String locale,Long siteId,ServletContext ampContext) throws WorkerException, DocumentException {
		Paragraph p1;
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
			String currencyCode=myForm.getCurrCode()!=null?myForm.getCurrCode():"";
			for (FundingOrganization fundingOrganisation : myForm.getFunding().getFundingOrganizations()) {
				if(fundingOrganisation.getFundings()!=null){
					drawTotals=true;
					for (Funding funding : (Collection<Funding>)fundingOrganisation.getFundings()) {
						String output="";				
						//general info rows
						if(FeaturesUtil.isVisibleFeature("Funding Information", ampContext)){
							//funding org id
							if(FeaturesUtil.isVisibleField("Funding Organization Id", ampContext)){								 
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
							}
							
							//funding org. name
							if(FeaturesUtil.isVisibleField("Funding Organization Name", ampContext)){								
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
							}
							
							//funding org Assistance
							if(FeaturesUtil.isVisibleField("Type Of Assistance", ampContext)){								
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
							}
							
							//Financial Instrument
							if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Financial Instrument", ampContext)){								
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
							}
							
							//Funding status
							if(FeaturesUtil.isVisibleField("Funding Status", ampContext)){
								PdfPCell foInstrumentCell1=new PdfPCell();
								foInstrumentCell1.setBackgroundColor(new Color(221,221,221));
								foInstrumentCell1.setBorder(0);
								p1=new Paragraph(TranslatorWorker.translateText("Funding Status",locale,siteId)+":",plainFont);
								foInstrumentCell1.addElement(p1);
								fundingTable.addCell(foInstrumentCell1);							
								//meaning
								PdfPCell foInstrumentCell2=new PdfPCell(new Paragraph(funding.getFundingStatus()!=null?funding.getFundingStatus().getValue():" ",plainFont));
								foInstrumentCell2.setBorder(0);
								foInstrumentCell2.setColspan(2);
								foInstrumentCell2.setBackgroundColor(new Color(221,221,221));
								fundingTable.addCell(foInstrumentCell2);
							}
							
							//Donor objective
							if(FeaturesUtil.isVisibleModule("/Activity Form/Donor Funding/Funding Group/Funding Item/Donor Objective", ampContext)){								
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
							}
						}
											
						//PLANNED COMITMENTS
						if(FeaturesUtil.isVisibleFeature("Planned Commitments", ampContext)){
							output=TranslatorWorker.translateText("PLANNED COMMITMENTS",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell plCommCell1=new PdfPCell(new Paragraph(output,titleFont));
							plCommCell1.setBorder(0);
							plCommCell1.setBackgroundColor(new Color(255,255,204));
							plCommCell1.setColspan(3);
							fundingTable.addCell(plCommCell1);
						}
						
						
						if(funding.getFundingDetails()!=null){
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==0){
									if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())){
										buildFundingInfoInnerTable(fundingTable, fd,fundingCommitmentsFMfields,locale,siteId,ampContext);
									}
								}
							}
							
							
							if(FeaturesUtil.isVisibleFeature("Planned Commitments", ampContext)){
								createSubtotalRow(fundingTable, "SUBTOTAL PLANNED COMMITMENTS:", funding.getSubtotalPlannedCommitments(), locale, siteId, currencyCode);
							}
							
							
							//actual commitments							
							output=TranslatorWorker.translateText("ACTUAL COMMITMENTS",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actCommCell1=new PdfPCell(new Paragraph(output,titleFont));
							actCommCell1.setBorder(0);
							actCommCell1.setBackgroundColor(new Color(255,255,204));
							actCommCell1.setColspan(3);
							fundingTable.addCell(actCommCell1);
							
							for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
								if(fd.getTransactionType()==0){
									if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
										buildFundingInfoInnerTable(fundingTable, fd,fundingCommitmentsFMfields,locale,siteId,ampContext);
									}
								}
							}
							
							createSubtotalRow(fundingTable, "SUBTOTAL ACTUAL COMMITMENTS:", funding.getSubtotalActualCommitments(), locale, siteId, currencyCode);
						}
						
						//DISBURSEMENTS
						if(FeaturesUtil.isVisibleFeature("Disbursement", ampContext)){
							output="";
							//planned disbursement
							if(FeaturesUtil.isVisibleField("Planned Disbursement Preview", ampContext)){
								output=TranslatorWorker.translateText("PLANNED DISBURSEMENT",locale,siteId);
								if(myForm.getFunding().isFixerate()){
									output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
								}
								PdfPCell plDisbCell1=new PdfPCell(new Paragraph(output,titleFont));
								plDisbCell1.setBorder(0);
								plDisbCell1.setBackgroundColor(new Color(255,255,204));
								plDisbCell1.setColspan(3);
								fundingTable.addCell(plDisbCell1);								
								}
								
								if(funding.getFundingDetails()!=null){
									if(FeaturesUtil.isVisibleField("Planned Disbursement Preview", ampContext)){									
										for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
											if(fd.getTransactionType()==1){
												if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())){
													buildFundingInfoInnerTable(fundingTable, fd,fundingDisbursementsFMfields,locale,siteId,ampContext);
												}
											}
										}									
										createSubtotalRow(fundingTable, "SUBTOTAL PLANNED DISBURSEMENT:", funding.getSubtotalPlannedDisbursements(), locale, siteId, currencyCode);
									}
																
									//actual disbursement
									output=TranslatorWorker.translateText("ACTUAL DISBURSEMENT:",locale,siteId);
									if(myForm.getFunding().isFixerate()){
										output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
									}
									PdfPCell actDisbCell1=new PdfPCell(new Paragraph(output,titleFont));
									actDisbCell1.setBorder(0);
									actDisbCell1.setBackgroundColor(new Color(255,255,204));
									actDisbCell1.setColspan(3);
									fundingTable.addCell(actDisbCell1);
									
									if(FeaturesUtil.isVisibleField("Adjustment Type Disbursement", ampContext)){
										for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
											if(fd.getTransactionType()==1){
												if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
													buildFundingInfoInnerTable(fundingTable, fd,fundingDisbursementsFMfields,locale,siteId,ampContext);													
												}
											}
										}
									}
									createSubtotalRow(fundingTable, "SUBTOTAL ACTUAL DISBURSEMENT:", funding.getSubtotalDisbursements(), locale, siteId, currencyCode);
								}
							}
							
						//EXPENDITURES
						if(FeaturesUtil.isVisibleFeature("Expenditures", ampContext)){
							//planned expenditures
							output=TranslatorWorker.translateText("PLANNED EXPENDITURES:",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell plExpCell1=new PdfPCell(new Paragraph(output,titleFont));
							plExpCell1.setBorder(0);
							plExpCell1.setBackgroundColor(new Color(255,255,204));
							plExpCell1.setColspan(3);
							fundingTable.addCell(plExpCell1);
							
							
							if(funding.getFundingDetails()!=null){
								for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
									if(fd.getTransactionType()==2){
										if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())){
											buildFundingInfoInnerTable(fundingTable, fd,fundingExpendituresFMfields,locale,siteId,ampContext);
										}
									}
								}
								createSubtotalRow(fundingTable, "SUBTOTAL PLANNED EXPENDITURES:", funding.getSubtotalPlannedExpenditures(), locale, siteId, currencyCode);
								
								//actual expenditures
								output=TranslatorWorker.translateText("ACTUAL EXPENDITURES:",locale,siteId);
								if(myForm.getFunding().isFixerate()){
									output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
								}
								PdfPCell actExpCell1=new PdfPCell(new Paragraph(output,titleFont));
								actExpCell1.setBorder(0);
								actExpCell1.setBackgroundColor(new Color(255,255,204));
								actExpCell1.setColspan(3);
								fundingTable.addCell(actExpCell1);
								
								for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
									if(fd.getTransactionType()==2){
										if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
											buildFundingInfoInnerTable(fundingTable, fd,fundingExpendituresFMfields,locale,siteId,ampContext);
										}
									}
								}								
								createSubtotalRow(fundingTable, "SUBTOTAL ACTUAL EXPENDITURES:", funding.getSubtotalExpenditures(), locale, siteId, currencyCode);
							}
							
						}
						
						//ACTUAL DISBURSMENT ORDERS
						if(FeaturesUtil.isVisibleFeature("Disbursement Orders", ampContext)){
							output=TranslatorWorker.translateText("ACTUAL DISBURSMENT ORDERS:",locale,siteId);
							if(myForm.getFunding().isFixerate()){
								output+=" \t"+ TranslatorWorker.translateText("Exchange Rate",locale,siteId);
							}
							PdfPCell actDisbOrdCell1=new PdfPCell(new Paragraph(output,titleFont));
							actDisbOrdCell1.setBorder(0);
							actDisbOrdCell1.setBackgroundColor(new Color(255,255,204));
							actDisbOrdCell1.setColspan(3);
							fundingTable.addCell(actDisbOrdCell1);
							
							if(funding.getFundingDetails()!=null){
								for (FundingDetail fd : (Collection<FundingDetail>)funding.getFundingDetails()) {
									if(fd.getTransactionType()==2){
										if(fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
											buildFundingInfoInnerTable(fundingTable, fd,fundingDisbOrdersFMfields,locale,siteId,ampContext);											
										}
									}
								}
							}
							createSubtotalRow(fundingTable, "SUBTOTAL DISBURSMENT ORDERS:", funding.getSubtotalActualDisbursementsOrders(), locale, siteId, currencyCode);
						}
						
						//UNDISBURSED BALANCE
						if(FeaturesUtil.isVisibleFeature("Undisbursed Balance", ampContext)){
							output=(funding.getUnDisbursementBalance()!=null && funding.getUnDisbursementBalance().length()>0)?	funding.getUnDisbursementBalance()+currencyCode : "";
							PdfPCell undisbursedBalanceCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("UNDISBURSED BALANCE:",locale,siteId)+" \t\t         "+ output+"\n\n",plainFont));
							undisbursedBalanceCell1.setBorder(0);
							undisbursedBalanceCell1.setBackgroundColor(new Color(255,255,204));
							undisbursedBalanceCell1.setColspan(3);
							fundingTable.addCell(undisbursedBalanceCell1);
						}							
						
						if(org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS.equals("true")){
							PdfPCell amountsInThousandsCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("The amount entered are in thousands (000)",locale,siteId),plainFont));
							amountsInThousandsCell1.setBorder(0);
							amountsInThousandsCell1.setBackgroundColor(new Color(255,255,204));
							amountsInThousandsCell1.setColspan(3);
							fundingTable.addCell(amountsInThousandsCell1);
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
				String totalsOutput="";
				String totalAmountType=null;
				PdfPCell empty=new PdfPCell(new Paragraph("\n\n"));
				empty.setBorder(0);
				empty.setBackgroundColor(new Color(255,255,255));
				empty.setColspan(3);
				fundingTable.addCell(empty);							
				//TOTAL PLANNED COMMITMENTS
				if(FeaturesUtil.isVisibleFeature("Planned Commitments", ampContext)){
					totalAmountType=TranslatorWorker.translateText("TOTAL PLANNED COMMITMENTS",locale,siteId)+":";
					if(myForm.getFunding().getTotalPlannedCommitments()!=null && myForm.getFunding().getTotalPlannedCommitments().length()>0){
						totalsOutput=myForm.getFunding().getTotalPlannedCommitments()+currencyCode;
					}					
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);
				}				
				
		    	//TOTAL ACTUAL COMMITMENTS
				totalAmountType=TranslatorWorker.translateText("TOTAL ACTUAL COMMITMENTS",locale,siteId)+":";
				totalsOutput="";
				if(myForm.getFunding().getTotalCommitments()!=null && myForm.getFunding().getTotalCommitments().length()>0){
					totalsOutput=myForm.getFunding().getTotalCommitments()+currencyCode;
				} 
				addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);
				
				//TOTAL PLANNED DISBURSEMENT
				if(FeaturesUtil.isVisibleFeature("Disbursement", ampContext)){
					totalAmountType=TranslatorWorker.translateText("TOTAL PLANNED DISBURSEMENT",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getTotalPlannedDisbursements()!=null && myForm.getFunding().getTotalPlannedDisbursements().length()>0){
						totalsOutput=myForm.getFunding().getTotalPlannedDisbursements()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);
					
					//TOTAL ACTUAL DISBURSEMENT
					totalAmountType=TranslatorWorker.translateText("TOTAL ACTUAL DISBURSEMENT",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getTotalDisbursements()!=null && myForm.getFunding().getTotalDisbursements().length()>0){
						totalsOutput=myForm.getFunding().getTotalDisbursements()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);				
					
				}				
				
				//TOTAL PLANNED EXPENDITURES
				if(FeaturesUtil.isVisibleFeature("Expenditures", ampContext)){
					totalAmountType=TranslatorWorker.translateText("TOTAL PLANNED EXPENDITURES",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getTotalPlannedExpenditures()!=null && myForm.getFunding().getTotalPlannedExpenditures().length()>0){
						totalsOutput=myForm.getFunding().getTotalPlannedExpenditures()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);					
					
					//TOTAL ACTUAL EXPENDITURES
					totalAmountType=TranslatorWorker.translateText("TOTAL ACTUAL EXPENDITURES",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getTotalExpenditures()!=null && myForm.getFunding().getTotalExpenditures().length()>0){
						totalsOutput=myForm.getFunding().getTotalExpenditures()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);
				}
				
				//TOTAL ACTUAL DISBURSeMENT ORDERS:
				if(FeaturesUtil.isVisibleFeature("Disbursement Orders", ampContext)){
					totalAmountType=TranslatorWorker.translateText("TOTAL ACTUAL DISBURSeMENT ORDERS",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getTotalActualDisbursementsOrders()!=null && myForm.getFunding().getTotalActualDisbursementsOrders().length()>0){
						totalsOutput=myForm.getFunding().getTotalActualDisbursementsOrders()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);					
				}
				
				//UNDISBURSED BALANCE
				if(FeaturesUtil.isVisibleFeature("Undisbursed Balance", ampContext)){
					totalAmountType=TranslatorWorker.translateText("UNDISBURSED BALANCE",locale,siteId)+":";
					totalsOutput="";
					if(myForm.getFunding().getUnDisbursementsBalance()!=null && myForm.getFunding().getUnDisbursementsBalance().length()>0){
						totalsOutput=myForm.getFunding().getUnDisbursementsBalance()+currencyCode;
					} 
					addTotalAmountsCellsToFundingTable(fundingTable,totalAmountType,totalsOutput);					
				}
				
			}				
		}
		PdfPCell fundingCell=new PdfPCell(fundingTable);
		fundingCell.setBorder(0);
		mainLayout.addCell(fundingCell);
		//return fundingTable;
	}

	private void createSubtotalRow(PdfPTable fundingTable, String title, String value, String locale, Long siteId, String currencyCode) throws WorkerException{
		
		String output= (value != null && value.trim().length() > 0 ) ? value + " " + currencyCode : "";								
		PdfPCell subTotal=new PdfPCell(new Paragraph(TranslatorWorker.translateText(title, locale, siteId),plainFont));
		subTotal.setBackgroundColor(new Color(221,221,221));
		subTotal.setColspan(2);
		subTotal.setBorderWidthBottom(0);
		subTotal.setBorderWidthTop(0.5f);
		subTotal.setBorderWidthLeft(0);
		subTotal.setBorderWidthRight(0);
		fundingTable.addCell(subTotal);
		
		subTotal=new PdfPCell(new Paragraph(output,plainFont));
		subTotal.setBackgroundColor(new Color(221,221,221));
		subTotal.setBorderWidthBottom(0);
		subTotal.setBorderWidthTop(0.5f);
		subTotal.setBorderWidthLeft(0);
		subTotal.setBorderWidthRight(0);
		fundingTable.addCell(subTotal);
	}
	private void addTotalAmountsCellsToFundingTable(PdfPTable fundingTable,String totalAmountType, String totalsOutput) throws WorkerException {
		Paragraph p1;
		PdfPCell totalPC=new PdfPCell(new Paragraph(totalAmountType,plainFont));
		totalPC.setColspan(2);
		totalPC.setBorder(0);
		totalPC.setBackgroundColor(new Color(221,221,221));
		fundingTable.addCell(totalPC);
		PdfPCell totalPCAmount=new PdfPCell();
		totalPCAmount.setBorder(0);
		totalPCAmount.setBackgroundColor(new Color(221,221,221));					
		p1=new Paragraph(totalsOutput,plainFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		totalPCAmount.addElement(p1);
		fundingTable.addCell(totalPCAmount);
		//empty cell
		buildEmptyCell(fundingTable);
	}

	private void buildEmptyCell(PdfPTable fundingTable) {
		PdfPCell lineCell;
		lineCell=new PdfPCell(new Paragraph(0));
		lineCell.setBorder(0);
		lineCell.setColspan(3);
		fundingTable.addCell(lineCell);
	}
	
	/**
	 * Used to create simple two columned row
	 * @param mainLayout
	 * @param columnName
	 * @param value
	 */
	private void createGeneralInfoRow(PdfPTable mainLayout,String columnName,String value){
		PdfPCell cell1=new PdfPCell();
		Paragraph p1=new Paragraph(columnName,titleFont);
		p1.setAlignment(Element.ALIGN_RIGHT);
		cell1.addElement(p1);
		cell1.setBackgroundColor(new Color(244,244,242));
		cell1.setBorder(0);
		mainLayout.addCell(cell1);
		
		PdfPCell cell2=new PdfPCell();
		p1=new Paragraph(value,plainFont);
		cell2.addElement(p1);
		cell2.setBorder(0);
		mainLayout.addCell(cell2);
	}
	
	private void  buildFundingInfoInnerTable(PdfPTable infoTable, FundingDetail fd,String []fmFields,String locale,Long siteId,ServletContext ampContext) throws WorkerException {
		PdfPCell innerCell=new PdfPCell();

		if(FeaturesUtil.isVisibleField(fmFields[0], ampContext)){
			innerCell.setBorder(0);
			innerCell=new PdfPCell(new Paragraph(TranslatorWorker.translateText(fd.getAdjustmentTypeName().getValue(),locale,siteId),plainFont));
			innerCell.setBorder(0);
			infoTable.addCell(innerCell);
		}else{
			addEmptyCell(infoTable);
		}
		if(FeaturesUtil.isVisibleField(fmFields[1], ampContext)){
			innerCell=new PdfPCell(new Paragraph(fd.getTransactionDate(),plainFont));
			innerCell.setBorder(0);
			infoTable.addCell(innerCell);
		}else{
			addEmptyCell(infoTable);
		}
		if(FeaturesUtil.isVisibleField(fmFields[2], ampContext)){
			String output="";
			if(fd.getTransactionAmount()!=null && fd.getTransactionAmount().length()>0){
				output=fd.getTransactionAmount()+" " + fd.getCurrencyCode();
			}
			innerCell=new PdfPCell(new Paragraph(output,plainFont));
			innerCell.setBorder(0);
			infoTable.addCell(innerCell);
		}else{
			addEmptyCell(infoTable);
		}		
		String formattedRate="";
		if(fd.getFormattedRate()!=null){
			formattedRate=fd.getFormattedRate();
		}
		if(fd.getFormattedRate()!=null && FeaturesUtil.isVisibleField(fmFields[fmFields.length-1], ampContext)){
			innerCell=new PdfPCell(new Paragraph(formattedRate,plainFont));
			innerCell.setBorder(0);
			infoTable.addCell(innerCell);
		}		
	}	
	
	private void addEmptyCell(PdfPTable infoTable){
		PdfPCell innerCell = new PdfPCell();
		innerCell.setBorder(0);
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
	private void buildContactInfoOutput(PdfPTable mainLayout,String contactType, Collection<AmpActivityContact> contacts,String locale,Long siteId,ServletContext ampContext) throws WorkerException{
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
				Set<AmpContactProperty> contactProperties=cont.getContact().getProperties();
				String emails="";
				if(contactProperties!=null){
					for (AmpContactProperty email : contactProperties) {
						if(email.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
							emails+=email.getValue()+"; ";
						}
					}
				}				
				output+=cont.getContact().getName()+" "+cont.getContact().getLastname()+"- "+emails+ "\n";
			}
			paragraph=new Paragraph(output,plainFont);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			cell2.addElement(paragraph);
		}
		mainLayout.addCell(cell2);
	}
	
	/**
	 * builds all related organizations Info that should be exported to PDF
	 */
	private void buildRelatedOrganisationsOutput(PdfPTable relatedOrgsTable, String orgType , Collection<AmpOrganisation> orgs,String locale,Long siteId,ServletContext ampContext) throws WorkerException{
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
	private PdfPTable buildFinanceInfoOutput(PdfPTable nestedTable,String elemntName, List<FundingDetail> listToIterate,String[] fmFields,String locale,Long siteId,ServletContext ampContext) throws WorkerException,DocumentException{			
		
		PdfPCell cell=new PdfPCell();
		cell.setBorder(0);
		Paragraph paragraph=new Paragraph(elemntName,plainFont);
		paragraph.setAlignment(Element.ALIGN_LEFT);
		cell.addElement(paragraph);
		cell.setBackgroundColor(new Color(255,255,255));
		cell.setBorder(0);
		nestedTable.addCell(cell);
		
		int visibleFmFieldsAmount=0;
		for(int i=0;i<fmFields.length;i++){
			if(FeaturesUtil.isVisibleModule(fmFields[i], ampContext)){
				visibleFmFieldsAmount++;
			}
		}
//		float[] widthArray=new float[visibleFmFieldsAmount];
//		for(int i=0;i<widthArray.length;i++){
//			if(i!=widthArray.length-1){
//				widthArray[i]=2f;
//			}else{
//				widthArray[i]=1f;
//			}			
//		}
		if (visibleFmFieldsAmount > 0){
			PdfPTable fdTable=new PdfPTable(visibleFmFieldsAmount);
	//		fdTable.setWidths(widthArray);
			for (FundingDetail fd : listToIterate) {
				if(FeaturesUtil.isVisibleModule(fmFields[0], ampContext)){
					cell=new PdfPCell();
					paragraph=new Paragraph(TranslatorWorker.translateText(fd.getAdjustmentTypeName().getValue(),locale,siteId),plainFont);
					cell.addElement(paragraph);
					cell.setBorder(0);
					fdTable.addCell(cell);
				}			
				
				if(FeaturesUtil.isVisibleModule(fmFields[1], ampContext)){
					cell=new PdfPCell();
					paragraph=new Paragraph(fd.getTransactionDate(),plainFont);
					cell.addElement(paragraph);
					cell.setBorder(0);
					fdTable.addCell(cell);
				}
				
				String output="";
				if(FeaturesUtil.isVisibleModule(fmFields[2], ampContext)){
					output+=fd.getTransactionAmount();
				}
				if(FeaturesUtil.isVisibleModule(fmFields[3], ampContext)){
					output+= " " + fd.getCurrencyCode();
				}
				cell=new PdfPCell();
				paragraph=new Paragraph(output,plainFont);
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
		return nestedTable;
	}
	
	private PdfPTable buildRegionalFundingInfoOutput(String elementName,List<FundingDetail> listToIterate,String locale,Long siteId,ServletContext ampContext) throws Exception{
		PdfPTable regFundTable=new PdfPTable(2);
		regFundTable.getDefaultCell().setBorder(0);
		regFundTable.setWidths(new float[] {1f,3f});
		
		//commitment,disbursement,expenditure
		PdfPCell cell=new PdfPCell();
		cell.setBorder(0);
		Paragraph paragraph=new Paragraph(elementName,plainFont);
		paragraph.setAlignment(Element.ALIGN_LEFT);
		cell.addElement(paragraph);		
		cell.setBorder(0);
		regFundTable.addCell(cell);
		
		//second cell for actual/planned
		PdfPTable fdTable=new PdfPTable(3);
		fdTable.getDefaultCell().setBorder(0);
		for (FundingDetail fd : listToIterate) {			
			cell=new PdfPCell();
			paragraph=new Paragraph(TranslatorWorker.translateText(fd.getAdjustmentTypeName().getValue(),locale,siteId),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
			
			cell=new PdfPCell();
			paragraph=new Paragraph(fd.getTransactionAmount()+" "+fd.getCurrencyCode(),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
			
			cell=new PdfPCell();
			paragraph=new Paragraph(fd.getTransactionDate(),plainFont);
			cell.addElement(paragraph);
			cell.setBorder(0);
			fdTable.addCell(cell);
		}
		regFundTable.addCell(fdTable);
		return regFundTable;
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