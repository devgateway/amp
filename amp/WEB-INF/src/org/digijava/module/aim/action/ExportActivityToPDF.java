package org.digijava.module.aim.action;

import static org.digijava.module.aim.helper.Constants.CURRENT_MEMBER;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
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
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureCoordinate;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Identification;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.BudgetStructure;
import org.digijava.module.aim.helper.ChartGenerator;
import org.digijava.module.aim.helper.ChartParams;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.ExportActivityToPdfUtil;
import org.digijava.module.aim.util.ExportUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
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
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
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

    
    public static final int COLUMNS_3 = 3;
    public static final int COLUMNS_4 = 4;
    public static final int INDENTATION_LEFT = 5;
    public static final Color BACKGROUND_COLOR = new Color(244, 244, 242);
    public static final Color BACKGROUND_COLOR_WHITE = new Color(255, 255, 255);
    public static final Color BORDER_COLOR = new Color(201, 201, 199);
    public static final Color MTEF_BACKGROUND_COLOR = new Color(255, 255, 204);
    public static final Color SUBTOTAL_BACKGROUND_COLOR = new Color(221, 221, 221);
    private static final int CURRENCY_COLUMN_WIDTH = 13;
    private static final int AMOUNT_COLUMN_WIDTH = 87;
    private static final float SUBTOTAL_BORDER_TOP_WIDTH = 0.5f;
    private static final int ARRAY_IDX_3 = 3;
    private static final int SYMBOL_INDENT = 20;
    
    private static Logger logger = Logger.getLogger(ExportActivityToPDF.class);

    /**
     * font which supports Diacritics - for Romanian language support. The standard PDF fonts do not have diacritics, so we have to embed a TTF font into AMP
     */
    public final static BaseFont basefont = getBaseFont();

    private static final com.lowagie.text.Font plainFont = new com.lowagie.text.Font(basefont, 11,Font.NORMAL);
    private static final com.lowagie.text.Font smallerFont = new com.lowagie.text.Font(basefont, 9,Font.NORMAL);
    private static final com.lowagie.text.Font titleFont = new com.lowagie.text.Font(basefont, 11,Font.BOLD);

    private static final String [] fundingCommitmentsFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Currency","/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Exchange Rate"};
    private static final String [] fundingDisbursementsFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Currency"};
    private static final String [] fundingArrearsFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Arrears/Arrears Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Arrears/Arrears Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Arrears/Arrears Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Arrears/Arrears Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Arrears/Arrears Table/Currency"};
    private static final String [] fundingExpendituresFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Currency"};
    private static final String [] fundingRoFFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Release of Funds/Release of Funds Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Release of Funds/Release of Funds Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Release of Funds/Release of Funds Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Release of Funds/Release of Funds Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Release of Funds/Release of Funds Table/Currency"};
    private static final String [] fundingEDDFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements/Estimated Disbursements Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements/Estimated Disbursements Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements/Estimated Disbursements Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements/Estimated Disbursements Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements/Estimated Disbursements Table/Currency"};
    private static final String [] fundingDisbOrdersFMfields={"/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders/Disbursement Orders Table/Adjustment Type","/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders/Disbursement Orders Table/Disaster Response","/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders/Disbursement Orders Table/Transaction Date","/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders/Disbursement Orders Table/Amount","/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders/Disbursement Orders Table/Currency"};

    private static final String[] componentCommitmentsFMfields = {
            "/Activity Form/Components/Component/Components Commitments",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Second Reporting "
                    + "Organisation",
            "/Activity Form/Components/Component/Components Commitments/Commitment Table/Description"
    };
    private static final String[] componentDisbursementsFMfields = {
            "/Activity Form/Components/Component/Components Disbursements",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Organization",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Second Reporting "
                    + "Organisation",
            "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Description"
    };
    private static final String[] componentExpendituresFMfields = {
            "/Activity Form/Components/Component/Components Expenditures",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Organization",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Second Reporting "
                    + "Organisation",
            "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Description"
    };
    private static final Chunk BULLET_SYMBOL = new Chunk("\u2022");

    private static final String [] mtefProjectionFields = {
            "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/MTEF Projection",
            "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Projection Date",
            "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Amount",
            "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Currency",
            "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Funding Flows OrgRole Selector"

    };

    private static BaseFont getBaseFont()
    {
        try
        {
            InputStream inputStream = ExportActivityToPDF.class.getResourceAsStream("Arial.ttf");
            byte[] fontFile = IOUtils.toByteArray(inputStream);

            BaseFont result = BaseFont.createFont("Arial.ttf", BaseFont.IDENTITY_H, true, true, fontFile, null);
            IOUtils.closeQuietly(inputStream);

            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
            //return null;
        }
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception {
        EditActivityForm myForm=(EditActivityForm)form;
        Long siteId=null;
        String locale=null;
        ServletContext ampContext = null;

        ampContext = getServlet().getServletContext();
        //to know whether print happens from Public View or not
        HttpSession session = request.getSession();
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
        PdfPTable mainLayout = buildPdfTable(2);
        if (SiteUtils.isEffectiveLangRTL()) {
            mainLayout.setWidths(new float[]{2f, 1f});
        } else {
            mainLayout.setWidths(new float[]{1f, 2f});
        }
        mainLayout.setWidthPercentage(100);
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
            logger.error(e.getMessage(), e);
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

            com.lowagie.text.Font headerFont = new com.lowagie.text.Font(basefont, 11, Font.BOLD, new Color(255, 255, 255));
            p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Activity Details",locale,siteId)), headerFont);
            p1.setAlignment(Element.ALIGN_CENTER);
            titleCell.addElement(p1);
            titleCell.setColspan(2);
            titleCell.setBackgroundColor(new Color(0,102,153));
            mainLayout.addCell(titleCell);
            //activity name cells
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Title")){
                columnName=TranslatorWorker.translateText("Activity Name");
                createGeneralInfoRow(mainLayout,columnName,activity.getName());
            }

            //AMPID cells
            if(FeaturesUtil.isVisibleField("AMP ID")){
                columnName=TranslatorWorker.translateText("AMP ID");
                createGeneralInfoRow(mainLayout,columnName,activity.getAmpId());
            }

            Identification identification = myForm.getIdentification();

            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Status")) {
                columnName=TranslatorWorker.translateText("Status");
                columnVal = "";
                catVal = null;
                Long statusId = identification.getStatusId();

                if (statusId != null && statusId != 0) {
                    catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(statusId);
                }
                if (catVal != null) {
                    columnVal = processHtml(request, CategoryManagerUtil.translateAmpCategoryValue(catVal));
                    createGeneralInfoRow(mainLayout, columnName, columnVal);
                }
            }
            //Status Other Info
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Other Info")) {
                columnName = TranslatorWorker.translateText("Status Other Info");
                createGeneralInfoRow(mainLayout, columnName, activity.getStatusOtherInfo());
            }

            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Reason")) {
                columnName = TranslatorWorker.translateText("Status Reason");
                columnVal = "";
                if (identification.getStatusReason() != null) {
                    columnVal += processEditTagValue(request, activity.getStatusReason());
                    createGeneralInfoRow(mainLayout,columnName,columnVal);
                }
            }

            if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Type of Cooperation")) {
                columnName = TranslatorWorker.translateText("Type of Cooperation");
                columnVal = identification.getSsc_typeOfCooperation();

                if (columnVal != null) {
                    columnVal = TranslatorWorker.translateText(columnVal);
                }
                createGeneralInfoRow(mainLayout, columnName, columnVal);
            }

            if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Type of Implementation")) {
                columnName = TranslatorWorker.translateText("Type of Implementation");
                columnVal = identification.getSsc_typeOfImplementation();

                if (columnVal != null) {
                    columnVal = TranslatorWorker.translateText(columnVal);
                }
                createGeneralInfoRow(mainLayout, columnName, columnVal);
            }

            String sscPrefix = "";
            if (identification.getTeam() != null && identification.getTeam().isSSCWorkspace()) {
                sscPrefix = "SSC ";
            }
            
            if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/" + sscPrefix + "Modalities")) {
                columnName = TranslatorWorker.translateText("Modalities");
                columnVal = identification.getSscModalitiesAsString("\n");
                createGeneralInfoRow(mainLayout, columnName, columnVal);
            }
            //Modalities Other Info
            if (FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Overview Section/" + sscPrefix + "Modalities Other Info")) {
                columnName = TranslatorWorker.translateText("Modalities Other Info");
                createGeneralInfoRow(mainLayout, columnName, activity.getModalitiesOtherInfo());
            }

            //objective
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective")){
                columnName=TranslatorWorker.translateText("Objectives");
                createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getObjective()));
            }

            //objective comments
            Map<String, List<AmpComments>> allComments = new HashMap<String, List<AmpComments>>();
            List<AmpComments> colAux    = null;
            Collection<AmpField> ampFields = DbUtil.getAmpFields();

            if (ampFields!=null) {
                for (Iterator<AmpField> itAux = ampFields.iterator(); itAux.hasNext(); ) {
                    AmpField field = (AmpField) itAux.next();
                    colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),actId);
                    allComments.put(field.getFieldName(), colAux);
                }
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments")) {

                PdfPTable objTable = buildPdfTable(2);
                objTable.getDefaultCell().setBorder(0);
                for (Object commentKey : allComments.keySet()) {
                    String key=(String)commentKey;
                    List<AmpComments> values=(List<AmpComments>)allComments.get(key);
                    if(key.equalsIgnoreCase("Objective Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Assumption")){
                        for (AmpComments value : values) {
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Objective Assumption", locale, siteId))+" :",titleFont));
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }else if(key.equalsIgnoreCase("Objective Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Verification")){
                        for (AmpComments value : values) {
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Objective Verification", locale, siteId))+" :",titleFont));
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators")) {
                        for (AmpComments value : values) {
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Objective Objectively Verifiable Indicators", locale, siteId))+" :",titleFont));
                            objTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }
                }

                PdfPCell objectiveCommentsCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Objective Comments",locale,siteId)),titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                objectiveCommentsCell1.addElement(p1);
                objectiveCommentsCell1.setBackgroundColor(new Color(244,244,242));
                objectiveCommentsCell1.setBorder(0);
                mainLayout.addCell(objectiveCommentsCell1);

                PdfPCell objectiveCommentsCell2=new PdfPCell(objTable);
                objectiveCommentsCell2.setBorder(0);
                mainLayout.addCell(objectiveCommentsCell2);
            }

            //
            //Description cell
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Description")){
                columnName=TranslatorWorker.translateText("Description");
                createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getDescription()));
            }

            //project comments
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Comments")) {
                columnName = TranslatorWorker.translateText("Project Comments");
                createGeneralInfoRow(mainLayout, columnName, processEditTagValue(request, activity.
                        getProjectComments()));
            }
            //Lessons learned
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Lessons Learned")){
                columnName=TranslatorWorker.translateText("Lessons Learned");
                createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getLessonsLearned()));
            }
            //Project Impact
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Impact")){
                columnName=TranslatorWorker.translateText("Project Impact");
                createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getProjectImpact()));
            }
            //Activity Summary
            if(activity.getActivitySummary()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Summary")){
                columnName=TranslatorWorker.translateText("Activity Summary");
                createGeneralInfoRow(mainLayout,columnName,processEditTagValue(request, activity.getActivitySummary()));
            }
            //Conditionalities
            if(activity.getConditionality()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionalities")){
                columnName=TranslatorWorker.translateText("Conditionalities");
                columnVal=processEditTagValue(request, activity.getConditionality());
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            //Project Management
            if(activity.getProjectManagement()!= null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Management")){
                columnName=TranslatorWorker.translateText("Project Management");
                columnVal=processEditTagValue(request, activity.getProjectManagement());
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            //Purpose cell
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose")){
                columnName=TranslatorWorker.translateText("Purpose");
                columnVal=processEditTagValue(request, activity.getPurpose());
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose Comments")){
                //purpose comments
                PdfPCell purposeCommentsCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Purpose Comments",locale,siteId)), titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                purposeCommentsCell1.addElement(p1);
                purposeCommentsCell1.setBackgroundColor(new Color(244,244,242));
                purposeCommentsCell1.setBorder(0);
                mainLayout.addCell(purposeCommentsCell1);

                PdfPTable purposeTable = buildPdfTable(2);
                purposeTable.getDefaultCell().setBorder(0);

                boolean visiblePurposeAssumtion = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose Comments/Purpose Assumption");
                boolean visiblePurposeVerification = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose Comments/Purpose Verification");
                boolean visiblePurposeIndicators = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators");

                for (Object commentKey : allComments.keySet()) {
                    String key=(String)commentKey;
                    List<AmpComments> values=(List<AmpComments>)allComments.get(key);
                    if(key.equalsIgnoreCase("Purpose Assumption") && visiblePurposeAssumtion){
                        for (AmpComments value : values) {
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Purpose Assumption", locale, siteId))+" :",titleFont));
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }else if(key.equalsIgnoreCase("Purpose Verification") && visiblePurposeVerification){
                        for (AmpComments value : values) {
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Purpose Verification", locale, siteId))+" :",titleFont));
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }else if (key.equalsIgnoreCase("Purpose Objectively Verifiable Indicators") && visiblePurposeIndicators) {
                        for (AmpComments value : values) {
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Purpose Objectively Verifiable Indicators", locale, siteId))+" :",titleFont));
                            purposeTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    }
                }

                PdfPCell purposeCommentsCell2=new PdfPCell(purposeTable);
                purposeCommentsCell2.setBorder(0);
                mainLayout.addCell(purposeCommentsCell2);
            }


            // results cell
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results")){
                columnName=TranslatorWorker.translateText("Results");
                columnVal=processEditTagValue(request, activity.getResults());
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            /**
             *  Results Comments
             */

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments")){
                PdfPTable resultsCommentsTable = buildPdfTable(2);
                resultsCommentsTable.getDefaultCell().setBorder(0);

                boolean visibleResultsAssumption = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Assumption");
                boolean visibleResultsVerification = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Verification");
                boolean visibleResultsIndicators = FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators");

                for (Object commentKey : allComments.keySet()) {
                    String key=(String)commentKey;
                    List<AmpComments> values=(List<AmpComments>)allComments.get(key);

                    if(key.equalsIgnoreCase("Results Assumption") && visibleResultsAssumption){
                        for (AmpComments value : values) {
                            resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Results Assumption", locale, siteId))+" :",titleFont));
                            resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                        }
                    } else {
                        if(key.equalsIgnoreCase("Results Verification") && visibleResultsVerification){
                            for (AmpComments value : values) {
                                resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Results Verification", locale, siteId))+" :",titleFont));
                                resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                            }
                        } else {
                            if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")&& visibleResultsIndicators) {
                                for (AmpComments value : values) {
                                    resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Results Objectively Verifiable Indicators", locale, siteId))+" :",titleFont));
                                    resultsCommentsTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)),plainFont));
                                }
                            }
                        }
                    }
                }
                PdfPCell resultsCommentsCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Results Comments",locale,siteId)),titleFont);
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
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Accession Instrument")){
                if(identification.getAccessionInstrument()!=null && identification.getAccessionInstrument().longValue()>0){
                    columnName=TranslatorWorker.translateText("Accession Instrument");
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAccessionInstrument());
                    if(catVal!=null){
                        columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                    }
                    createGeneralInfoRow(mainLayout,columnName,columnVal);
                }
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Implementing Unit")){
                columnName=TranslatorWorker.translateText("Project Implementing Unit");
                columnVal="";
                catVal = null;
                if(identification.getProjectImplUnitId()!=null && identification.getProjectImplUnitId() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectImplUnitId());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }

            // A.C. Chapter cell
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/A.C. Chapter")){
                if(identification.getAcChapter()!=null && identification.getAcChapter().longValue()>0){
                    columnName=TranslatorWorker.translateText("A.C. Chapter");
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAcChapter());
                    if(catVal!=null){
                        columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                    }
                    createGeneralInfoRow(mainLayout,columnName,columnVal);
                }
            }

            //Cris Number
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Cris Number")){
                columnName=TranslatorWorker.translateText("Cris Number");
                createGeneralInfoRow(mainLayout,columnName,activity.getCrisNumber());
            }
    
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/IATI Identifier")) {
                columnName = TranslatorWorker.translateText("IATI Identifier");
                createGeneralInfoRow(mainLayout, columnName, activity.getIatiIdentifier());
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Procurement System")){
                columnName=TranslatorWorker.translateText("Procurement System");
                columnVal="";
                catVal = null;
                if(identification.getProcurementSystem()!=null && identification.getProcurementSystem() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProcurementSystem());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);

            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Reporting System")){
                columnName=TranslatorWorker.translateText("Reporting System");
                columnVal="";
                catVal = null;
                if(identification.getReportingSystem()!=null && identification.getReportingSystem() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getReportingSystem());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Audit System")){
                columnName=TranslatorWorker.translateText("Audit System");
                columnVal="";
                catVal = null;
                if(identification.getAuditSystem()!=null && identification.getAuditSystem() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAuditSystem());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Institutions")){
                columnName=TranslatorWorker.translateText("Institutions");
                columnVal="";
                catVal = null;
                if(identification.getInstitutions()!=null && identification.getInstitutions() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getInstitutions());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }

            //Project Category
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category")){
                if(identification.getProjectCategory()!=null && identification.getProjectCategory().longValue()>0){
                    columnName=TranslatorWorker.translateText("Project Category");
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectCategory());
                    if(catVal!=null){
                        columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                    }
                    createGeneralInfoRow(mainLayout,columnName,columnVal);
                }
            }
            //Project Category Other Info
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category Other Info")) {
                columnName = TranslatorWorker.translateText("Project Category Other Info");
                createGeneralInfoRow(mainLayout, columnName, activity.getProjectCategoryOtherInfo());
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Agreement Number")){
                columnName=TranslatorWorker.translateText("Government Agreement Number");
                columnVal="";
                catVal = null;
                if(identification.getGovAgreementNumber()!=null){
                    columnVal   = identification.getGovAgreementNumber();
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            //end identification
            //Budget
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Budget")){
                String budget="";

                if (identification.getBudgetCV()!=null) {
                    if(identification.getBudgetCV().equals(identification.getBudgetCVOn())){
                        budget = TranslatorWorker.translateText("Activity is on budget");
                    }else if (identification.getBudgetCV().equals(identification.getBudgetCVOff())){
                        budget = TranslatorWorker.translateText("Activity is off budget");
                    }else if (identification.getBudgetCV().equals(new Long(0))) {
                        budget = TranslatorWorker.translateText("Budget Unallocated");
                    }else{
                        AmpCategoryValue value = (AmpCategoryValue) DbUtil.getObject(AmpCategoryValue.class, identification.getBudgetCV());
                        budget = TranslatorWorker.translateText("Activity is on")+" "+value.getLabel() ;
                    }
                }

                if (identification.getChapterForPreview() ==null){
                    columnName=TranslatorWorker.translateText("Budget");
                    createGeneralInfoRow(mainLayout,columnName,budget);
                }
                else{
                    PdfPTable budgetTable = buildPdfTable(2);
                    budgetTable.getDefaultCell().setBorder(0);
                    budgetTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Code Chapitre"))+": ",plainFont));
                    budgetTable.addCell(new Paragraph(postprocessText(identification.getChapterForPreview().getCode()) + " - "
                            + identification.getChapterForPreview().getDescription(),titleFont));
                    budgetTable.addCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Imputations"))+": ",plainFont));

                    PdfPTable imputationsTable = buildPdfTable(1);
                    for (AmpImputation imputation : identification.getChapterForPreview().getImputations()) {
                        imputationsTable.addCell(new Paragraph(postprocessText(identification.getChapterForPreview().getYear()+" - " +
                                imputation.getCode() +" - "+ imputation.getDescription()),titleFont));
                    }
                    budgetTable.addCell(imputationsTable);


                    PdfPCell cell = new PdfPCell();
                    p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Budget",locale,siteId)),titleFont);
                    p1.setAlignment(Element.ALIGN_RIGHT);
                    cell.addElement(p1);
                    cell.setBackgroundColor(new Color(244,244,242));
                    cell.setBorder(0);
                    mainLayout.addCell(cell);

                    cell = new PdfPCell(budgetTable);
                    cell.setBorder(0);
                    mainLayout.addCell(cell);
                }
            }

            /**
             * Budget Extras
             */

                //AMP-16421
                if(identification.getBudgetCV() != null && identification.getBudgetCV().equals(identification.getBudgetCVOn())) {
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/FY")){
                        columnName = TranslatorWorker.translateText("FY");
                        createGeneralInfoRow(mainLayout, columnName, identification.getFY());
                    }
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Ministry Code")) {
                        columnName = TranslatorWorker.translateText("Ministry Code");
                        createGeneralInfoRow(mainLayout, columnName, identification.getMinistryCode());
                    }
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Project Code")) {
                        columnName = TranslatorWorker.translateText("Project Code");
                        createGeneralInfoRow(mainLayout, columnName, identification.getProjectCode());
                    }
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Vote")) {
                        columnName = TranslatorWorker.translateText("Vote");
                        createGeneralInfoRow(mainLayout, columnName, identification.getVote());
                    }
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Vote")) {
                        columnName = TranslatorWorker.translateText("Sub-Vote");
                        createGeneralInfoRow(mainLayout, columnName, identification.getSubVote());
                    }
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Program")) {
                        columnName = TranslatorWorker.translateText("Sub-Program");
                        createGeneralInfoRow(mainLayout, columnName, identification.getSubProgram());
                    }
                }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Classification")) {

                PdfPCell cell = new PdfPCell();
                cell.setColspan(2);
                cell.setBorder(0);

                if(identification.getSelectedbudgedsector() != null){
                    for (AmpBudgetSector budgSect : identification.getBudgetsectors()) {
                        if (identification.getSelectedbudgedsector().equals(budgSect.getIdsector())) {

                            String classificationColumnName = postprocessText(TranslatorWorker.translateText("Budget Classification"))+": ";
                            String classificationColumnValue = BULLET_SYMBOL + budgSect.getCode()+ " - " + postprocessText(budgSect.getSectorname());

                            createGeneralInfoRow(mainLayout, classificationColumnName, classificationColumnValue);
                        }
                    }
                }

                if(identification.getSelectedorg() !=null ){
                    for (AmpOrganisation budgOrg : identification.getBudgetorgs()) {
                        cell.addElement(new Paragraph(BULLET_SYMBOL + budgOrg.getBudgetOrgCode()+" - "+ postprocessText(budgOrg.getName()),titleFont));
                    }
                }

                if(identification.getSelecteddepartment() !=null ){
                    for (AmpDepartments budgDept : identification.getBudgetdepartments()) {
                        cell.addElement(new Paragraph(BULLET_SYMBOL + budgDept.getCode()+" - "+ postprocessText(budgDept.getName()),titleFont));
                    }
                }

                if(identification.getSelectedprogram() !=null ){
                    for (AmpTheme budgprog : identification.getBudgetprograms()) {
                        cell.addElement(new Paragraph(BULLET_SYMBOL + budgprog.getThemeCode()+" - "+ postprocessText(budgprog.getName()),titleFont));
                    }
                }
                mainLayout.addCell(cell);
            }

            //Organizations and Project IDs
            if(FeaturesUtil.isVisibleField("Organizations and Project ID")){
                PdfPCell orgProjCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Organizations and Project IDs",locale,siteId)),titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                orgProjCell1.addElement(p1);
                orgProjCell1.setBackgroundColor(new Color(244,244,242));
                orgProjCell1.setBorder(0);
                mainLayout.addCell(orgProjCell1);

                com.lowagie.text.List orgsList=new com.lowagie.text.List(false,20);  //is not numbered list
                orgsList.setListSymbol(new Chunk("\u2022"));
                if(identification.getSelectedOrganizations()!=null){
                    for (OrgProjectId selectedOrgForPopup : identification.getSelectedOrganizations()) {
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

            /**
             * Government Approval Procedures
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Approval Procedures")){
                if (activity.isGovernmentApprovalProcedures() != null) {
                    String value = TranslatorWorker.translateText(activity.isGovernmentApprovalProcedures() ? "Yes": "No");
                    columnName = TranslatorWorker.translateText("Government Approval Procedures");
                    createGeneralInfoRow(mainLayout, columnName, value);
                }
            }
            /**
             * Joint Criteria
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Joint Criteria")){
                if (activity.isJointCriteria() != null) {
                    String value = TranslatorWorker.translateText(activity.isJointCriteria() ? "Yes": "No");
                    columnName = TranslatorWorker.translateText("Joint Criteria");
                    createGeneralInfoRow(mainLayout, columnName, value);
                }
            }
            /**
             * Humanitarian Aid
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Humanitarian Aid")){
                if (activity.isHumanitarianAid() != null) {
                    String value = TranslatorWorker.translateText(activity.isHumanitarianAid() ? "Yes": "No");
                    columnName = TranslatorWorker.translateText("Humanitarian Aid");
                    createGeneralInfoRow(mainLayout, columnName, value);
                }
            }

            //AGENCY INTERNAL IDS
            if (FeaturesUtil.isVisibleModule("/Activity Form/Activity Internal IDs")) {
                if (myForm.getInternalIds() != null) {
                    output = ExportUtil.buildInternalId(myForm.getInternalIds());
                }
                columnName = TranslatorWorker.translateText("Agency Internal IDs");
                createGeneralInfoRow(mainLayout, columnName, output);
            }

            //Planning
            if(FeaturesUtil.isVisibleModule("/Activity Form/Planning")){
                List<PdfPTable> valuesTable = new ArrayList<>();
                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Line Ministry Rank")) {
                    String value = "";
                    if (activity.getLineMinRank() != null && activity.getLineMinRank().intValue() > 0) {
                        value += (activity.getLineMinRank());
                    }
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Line Ministry Rank") + ":", 
                            value, false));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Approval Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Proposed Approval Date") + ":", 
                            myForm.getPlanning().getOriginalAppDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Approval Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Actual Approval Date") + ":", 
                            myForm.getPlanning().getRevisedAppDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Start Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Proposed Start Date") + ":", 
                            myForm.getPlanning().getOriginalStartDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Start Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Actual Start Date") + ":", 
                            myForm.getPlanning().getRevisedStartDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Original Completion Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Original Completion Date") + ":", 
                            myForm.getPlanning().getOriginalCompDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Completion Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Proposed Completion Date") + ":", 
                            myForm.getPlanning().getProposedCompDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Completion Date")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Actual Completion Date") + ":", 
                            myForm.getPlanning().getCurrentCompDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Project Implementation Delay")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Project Implementation Delay") + ":", 
                            myForm.getPlanning().getProjectImplementationDelay(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Contracting")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Final Date for Contracting") + ":", 
                            myForm.getPlanning().getContractingDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Disbursements")) {
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Final Date for Disbursements") + ":", 
                            myForm.getPlanning().getDisbursementsDate(), true));
                }

                if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Project Life")) {
                    Integer life = myForm.getPlanning().getProposedProjectLife();
                    valuesTable.add(createGeneralInfoTable(
                            TranslatorWorker.translateText("Proposed Project Life") + ":", 
                            life == null ? "" : life.toString(), true));
                }

                if(FeaturesUtil.isVisibleField("Duration of Project")){
                    BigDecimal duration = myForm.getPlanning().getProjectPeriod();
                    if (duration != null) {
                        valuesTable.add(createGeneralInfoTable(
                                TranslatorWorker.translateText("Duration of Project") + ":", 
                                duration.toString(), true));
                    }
                }
             
                columnName = TranslatorWorker.translateText("Planning");
                createGeneralInfoRow(mainLayout, columnName, valuesTable);

                //status
                /* No Fields. Disabling temporary
                if(FeaturesUtil.isVisibleField("Status", ampContext,session)){
                    columnName=TranslatorWorker.translateText("Status");
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getPlanning().getStatusId());
                    if(catVal!=null){
                        columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal, request);
                    }
                    createGeneralInfoRow(mainLayout,columnName,columnVal+"\n"+myForm.getPlanning().getStatusReason());              
                }
                */
            }

            String commColumnName = "Final Date for Disbursements Comments";
            if(FeaturesUtil.isVisibleField(commColumnName)){
                this.buildCommentsPart("Final Date for Disbursements", commColumnName, allComments, locale, siteId, mainLayout);
            }
            commColumnName = "Current Completion Date Comments";
            if(FeaturesUtil.isVisibleField(commColumnName)){
                this.buildCommentsPart("current completion date", commColumnName, allComments, locale, siteId, mainLayout);
            }
            //References
            if(FeaturesUtil.isVisibleModule("References")){
                Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);

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
                    columnName=TranslatorWorker.translateText("References");
                    createGeneralInfoRow(mainLayout,columnName,output);
                }
            }

            //LOCATIONS
            if(FeaturesUtil.isVisibleModule("/Activity Form/Location")){
                //locations

                if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location")){
                    if(myForm.getLocation().getSelectedLocs()!=null){
                        output="";
                        for (Location loc  : myForm.getLocation().getSelectedLocs()) {
                            for(String locName : loc.getAncestorLocationNames()){
                                output+="["+locName+"] ";
                            }

                            output+="\t "+loc.getPercent()+"% \n";
                        }
                    }
                    columnName=TranslatorWorker.translateText("Locations");
                    createGeneralInfoRow(mainLayout,columnName,output);
                }

                //level
                if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Level")){
                    translatedValue="";
                    if(myForm.getLocation()!=null && myForm.getLocation().getLevelId()!=null && myForm.getLocation().getLevelId()>0){
                        catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getLevelId());
                        if(catVal!=null){
                            translatedValue = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                        }
                    }

                    columnName=TranslatorWorker.translateText("Level");
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getLevelId());
                    if(catVal!=null){
                        columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                    }
                    createGeneralInfoRow(mainLayout,columnName,translatedValue);
                }
            }

            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location")){
                columnName=TranslatorWorker.translateText("Implementation Location");
                columnVal="";
                catVal = null;
                if(myForm.getLocation().getImplemLocationLevel()!=null && myForm.getLocation().getImplemLocationLevel() !=0){
                    catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getLocation().getImplemLocationLevel());
                }
                if(catVal!=null){
                    columnVal   = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                }
                createGeneralInfoRow(mainLayout,columnName,columnVal);
            }
            //Sector
            if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors")){
                output="";
                PdfPCell sectorCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Sectors",locale,siteId)),titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                sectorCell1.addElement(p1);
                sectorCell1.setBackgroundColor(new Color(244,244,242));
                sectorCell1.setBorder(0);
                mainLayout.addCell(sectorCell1);

                String sectorsToAdd="";
                String sectors = "";
                List<AmpClassificationConfiguration> classificationConfigs = SectorUtil
                        .getAllClassificationConfigsOrdered();
                for (AmpClassificationConfiguration configuration : classificationConfigs) {

                    boolean hasSectors = false;


                    if (myForm.getSectors().getActivitySectors() != null) {
                        for (ActivitySector actSect : myForm.getSectors().getActivitySectors()) {
                            if (actSect.getConfigId().equals(configuration.getId())) {
                                hasSectors = true;
                            }
                        }
                    }
                    if (hasSectors) {

                        sectorsToAdd+=TranslatorWorker.translateText(configuration.getName()  +" Sector")
                                .toUpperCase()+":\n ";

                    }
                    
                    if (myForm.getSectors().getActivitySectors() != null) {
                        for (ActivitySector actSect : myForm.getSectors().getActivitySectors()) {
                            if (actSect.getConfigId().equals(configuration.getId())) {

                                sectors += actSect.getSectorScheme();
                                if (actSect.getSectorName() != null) {
                                    sectors += " - "
                                            + actSect.getSectorName();
                                }
                                if (actSect.getSubsectorLevel1Name() != null) {
                                    sectors += " - "
                                            + actSect.getSubsectorLevel1Name();
                                }
                                if (actSect.getSubsectorLevel2Name() != null) {
                                    sectors += " - "
                                            + actSect.getSubsectorLevel2Name();
                                }
                                sectors += " " + actSect.getSectorPercentage() + " %";
                                sectors += "\n";
                            }
                        }
                    }

                }

                PdfPTable sectorTable = new PdfPTable(2);
                if (SiteUtils.isEffectiveLangRTL()) {
                    sectorTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                }
                
                PdfPCell sectorsCell1 = new PdfPCell();
                p1 = new Paragraph(postprocessText(sectorsToAdd), plainFont);
                p1.setAlignment(Element.ALIGN_LEFT);
                sectorsCell1.addElement(p1);
                sectorsCell1.setBorder(0);
                sectorTable.addCell(sectorsCell1);
                
                PdfPCell sectorsCell2 = new PdfPCell();
                p1 = new Paragraph(postprocessText(sectors), plainFont);
                p1.setAlignment(Element.ALIGN_LEFT);
                sectorsCell2.addElement(p1);
                sectorsCell2.setBorder(0);
                sectorTable.addCell(sectorsCell2);
                
                PdfPCell cell2 = new PdfPCell(sectorTable);
                cell2.setBorder(0);
                mainLayout.addCell(cell2);
            }

            //Components
            Collection<ActivitySector> components=myForm.getComponents().getActivityComponentes();
            if(components!=null){
                String result="";
                columnName=TranslatorWorker.translateText("Components");
                for (ActivitySector component : components) {
                    result+=component.getSectorName()+" " + component.getSectorPercentage()+"% \n";
                }
                createGeneralInfoRow(mainLayout,columnName,result);
            }

            // Programs
            if (FeaturesUtil.isVisibleModule("/Activity Form/Program")) {
                PdfPCell programCell1 = new PdfPCell();
                p1 = new Paragraph(postprocessText(
                        TranslatorWorker.translateText("Program", locale, siteId)), titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                programCell1.addElement(p1);
                programCell1.setBackgroundColor(BACKGROUND_COLOR);
                programCell1.setBorder(0);
                mainLayout.addCell(programCell1);

                PdfPTable programsTable = new PdfPTable(2);
                if (SiteUtils.isEffectiveLangRTL()) {
                    programsTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                }
                
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/National Plan Objective")) {
                    if (hasContents(myForm.getPrograms().getNationalPlanObjectivePrograms())) {
                        String programs = buildProgramsOutput(myForm.getPrograms().getNationalPlanObjectivePrograms());
                        programsTable.addCell(buildProgramCell("National Plan Objective", true));
                        programsTable.addCell(buildProgramCell(programs, false));
                    }
                }
                
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Primary Programs")) {
                    if (hasContents(myForm.getPrograms().getPrimaryPrograms())) {
                        String programs = buildProgramsOutput(myForm.getPrograms().getPrimaryPrograms());
                        programsTable.addCell(buildProgramCell("Primary Programs", true));
                        programsTable.addCell(buildProgramCell(programs, false));
                    }
                }
                
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Secondary Programs")) {
                    if (hasContents(myForm.getPrograms().getSecondaryPrograms())) {
                        String programs = buildProgramsOutput(myForm.getPrograms().getSecondaryPrograms());
                        programsTable.addCell(buildProgramCell("Secondary Programs", true));
                        programsTable.addCell(buildProgramCell(programs, false));
                    }
                }
                
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Tertiary Programs")) {
                    if (hasContents(myForm.getPrograms().getTertiaryPrograms())) {
                        String programs = buildProgramsOutput(myForm.getPrograms().getTertiaryPrograms());
                        programsTable.addCell(buildProgramCell("Tertiary Programs", true));
                        programsTable.addCell(buildProgramCell(programs, false));
                    }
                }
                
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Program Description")) {
                    if (myForm.getPrograms().getProgramDescription() != null) {
                        String programs = processEditTagValue(request, myForm.getPrograms().getProgramDescription());
                        programsTable.addCell(buildProgramCell("Program Description", true));
                        programsTable.addCell(buildProgramCell(programs, false));
                    }
                }
                
                PdfPCell cell2 = new PdfPCell(programsTable);
                cell2.setBorder(0);
                mainLayout.addCell(cell2);
            }
            
            /**
             * funding
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding")){
                //PdfPTable fundingTable = buildFundingInformationPart(myForm,mainLayout);
                buildFundingInformationPart(myForm,mainLayout,ampContext,session);
            }
            /*
             * AidEffectiveness
             */
            if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes")) {
                java.util.List<String[]> aidEffectivenesToAdd = ActivityUtil.getAidEffectivenesForExport(activity);
                if (aidEffectivenesToAdd != null && aidEffectivenesToAdd.size() > 0) {
                    buildAidEffectivenessInformationPart(mainLayout,aidEffectivenesToAdd);
                }
            }


            /**
             * Regional Funding
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding")){
                PdfPCell regFundingCell1=new PdfPCell();
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Regional Funding",locale,siteId)),titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                regFundingCell1.addElement(p1);
                regFundingCell1.setBackgroundColor(new Color(244,244,242));
                regFundingCell1.setBorder(0);
                mainLayout.addCell(regFundingCell1);

                //now we should create nested table and add it as second cell in mainLayout
                PdfPTable regFundingNested = buildPdfTable(1);
                regFundingNested.setWidthPercentage(80);
                regFundingNested.setHorizontalAlignment(Element.ALIGN_CENTER);
                if(myForm.getFunding().getRegionalFundings()!=null){
                    boolean visibleModuleRegCommitments = FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Commitments");
                    boolean visibleModuleRegDisbursements = FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Disbursements");
                    boolean visibleModuleRegExpenditures = FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding/Region Item/Expenditures");

                    for (RegionalFunding regFunf : (Collection<RegionalFunding>)myForm.getFunding().getRegionalFundings()) {
                        //create first row (Region Name)
                        PdfPCell nestedCell1=new PdfPCell();
                        nestedCell1.setBorder(0);
                        p1=new Paragraph(postprocessText(TranslatorWorker.translateText(regFunf.getRegionName(),locale,siteId)),titleFont);
                        p1.setAlignment(Element.ALIGN_LEFT);
                        nestedCell1.addElement(p1);
                        regFundingCell1.setBackgroundColor(new Color(255,255,255));
                        regFundingCell1.setBorder(0);
                        regFundingNested.addCell(nestedCell1);

                        if(visibleModuleRegCommitments && regFunf.getCommitments()!=null){ //create commitment row
                            PdfPTable commitmentsTable=buildRegionalFundingInfoOutput(TranslatorWorker.translateText("Commitment"), regFunf.getRegionName(),(List<FundingDetail>)regFunf.getCommitments(),ampContext);
                            regFundingNested.addCell(commitmentsTable);
                        }

                        if(visibleModuleRegDisbursements && regFunf.getDisbursements()!=null){ //create disbursments row
                            PdfPTable disbTable=buildRegionalFundingInfoOutput( TranslatorWorker.translateText("Disbursment"), regFunf.getRegionName(), (List<FundingDetail>)regFunf.getDisbursements(),ampContext);
                            regFundingNested.addCell(disbTable);
                        }

                        if(visibleModuleRegExpenditures && regFunf.getExpenditures()!=null){ //create expenditure row
                            PdfPTable expTable=buildRegionalFundingInfoOutput( TranslatorWorker.translateText("Expenditures"), regFunf.getRegionName(), (List<FundingDetail>)regFunf.getExpenditures(),ampContext);
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
            buildComponentsPart(myForm, mainLayout,ampContext,session);

            /**
             * Issues
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Issues Section")){
                buildIssuesPart(myForm, mainLayout,ampContext,session);
            }
    
            /**
             * related documents
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Related Documents")){
                buildRelatedDocsPart(myForm, mainLayout, event,ampContext);
            }
    
            if (FeaturesUtil.isVisibleModule("/Activity Form/Regional Observations")) {
                buildRegionalObservationsPart(myForm, mainLayout);
            }
    
            if (FeaturesUtil.isVisibleModule("/Activity Form/Line Ministry Observations")) {
                buildLineMinistryObservationsPart(myForm, mainLayout);
            }

            /**
             * Related organizations
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations")){
                PdfPCell relOrgCell1=new PdfPCell();
                p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("Related Organizations", locale,
                        siteId)), titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                relOrgCell1.setBorder(0);
                relOrgCell1.addElement(p1);
                relOrgCell1.setBackgroundColor(new Color(244,244,242));
                mainLayout.addCell(relOrgCell1);

                PdfPCell relOrgCell2=new PdfPCell();
                relOrgCell2.setBorder(0);
                PdfPTable relatedOrgnested = buildPdfTable(1); //table that holds all related organisations
                //Donor Organizations
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Donor Organization")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Donor Agency",null,myForm.getFunding().getFundingOrganizations(), myForm.getAgencies().getRespOrgPercentage(),ampContext);
                }
                //Responsible Organizations
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Responsible Organization")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Responsible Organization",myForm.getAgencies().getRespOrganisations(),null, myForm.getAgencies().getRespOrgPercentage(),ampContext);
                }
                //Executing Agency
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Executing Agency")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Executing Agency",myForm.getAgencies().getExecutingAgencies(),null, myForm.getAgencies().getExecutingOrgPercentage(), ampContext);
                }
                //Implementing Agency
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Implementing Agency")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Implementing Agency",myForm.getAgencies().getImpAgencies(),null,myForm.getAgencies().getImpOrgPercentage(), ampContext);
                }
                //Beneficiary Agency
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Beneficiary Agency")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Beneficiary Agency",myForm.getAgencies().getBenAgencies(),null, myForm.getAgencies().getBenOrgPercentage(),ampContext);
                }
                //Contracting Agency
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Contracting Agency")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Contracting Agency",myForm.getAgencies().getConAgencies(),null,myForm.getAgencies().getConOrgPercentage(),ampContext);
                }

                //Sector Group
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Sector Group")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Sector Group",myForm.getAgencies().getSectGroups(),null,myForm.getAgencies().getSectOrgPercentage(), ampContext);
                }
                //Regional Group
                if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Regional Group")){
                    buildRelatedOrganisationsOutput(relatedOrgnested,"Regional Group",myForm.getAgencies().getRegGroups(),null,myForm.getAgencies().getRegOrgPercentage(), ampContext);
                }

                relOrgCell2.addElement(relatedOrgnested);
                mainLayout.addCell(relOrgCell2);
            }

            /**
             *  Contact Informations
             */
            
            boolean isContactInformationVisible = FeaturesUtil.isVisibleModule("/Activity Form/Contacts") &&
                    ((TeamMember) session.getAttribute(CURRENT_MEMBER) != null || FeaturesUtil.isVisibleFeature("Contacts"));
            
            if(isContactInformationVisible){
                //Funding contact information
                if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Donor Contact Information")){
                    buildContactInfoOutput(mainLayout,"Donor funding contact information",myForm.getContactInformation().getDonorContacts(),ampContext);
                }
                //MOFED contact information
                if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Mofed Contact Information")){
                    buildContactInfoOutput(mainLayout,"MOFED contact information",myForm.getContactInformation().getMofedContacts(),ampContext);
                }
                //Sec Min funding contact information
                if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Sector Ministry Contact Information")){
                    buildContactInfoOutput(mainLayout,"Sector Ministry contact information",myForm.getContactInformation().getSectorMinistryContacts(),ampContext);
                }
                //Project Coordinator contact information
                if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Project Coordinator Contact Information")){
                    buildContactInfoOutput(mainLayout,"Proj. Coordinator contact information",myForm.getContactInformation().getProjCoordinatorContacts(),ampContext);
                }
                //Implementing/executing agency contact information
                if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Implementing Executing Agency Contact Information")){
                    buildContactInfoOutput(mainLayout,"Implementing/Executing Agency contact information",myForm.getContactInformation().getImplExecutingAgencyContacts(),ampContext);
                }
            }

            /**
             * Proposed Project Cost
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Proposed Project Cost")){
                buildProjectCost(mainLayout, myForm.getFunding().getProProjCost(), "Proposed Project Cost");
            }
            /**
             * Revised Project Cost
             */
            if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Revised Project Cost")) {
                buildProjectCost(mainLayout, myForm.getFunding().getRevProjCost(), "Revised Project Cost");
            }

            /**
             * Budget Structure
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Budget Structure")){
                String costOutput="";
                columnName=TranslatorWorker.translateText("Budget Structure");
                if(myForm.getBudgetStructure().size()>0){
                    for(BudgetStructure abs: myForm.getBudgetStructure()){
                        costOutput += " "+abs.getBudgetStructureName()+": "+abs.getBudgetStructurePercentage()+"%\n";
                    }
                }
                createGeneralInfoRow(mainLayout,columnName,costOutput);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost")) {
                buildAnnualProjectBudgetTable(myForm,request,mainLayout);
            }
            /**
             * Costing
             */
            if(FeaturesUtil.isVisibleModule("Activity Costing")){
                buildCostingPart(request, actId, mainLayout,ampContext);
            }

            /**
             * Build IPA contracting
             */
            buildContractsPart(myForm, request, mainLayout);

            //GPI
            if (FeaturesUtil.isVisibleModule("/Activity Form/GPI")) {
                PdfPCell gpiCell1 = new PdfPCell();
                p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("GPI", locale, siteId)) + ":", titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                gpiCell1.setBorder(0);
                gpiCell1.addElement(p1);
                gpiCell1.setBackgroundColor(new Color(244, 244, 242));
                mainLayout.addCell(gpiCell1);

                PdfPCell gpiCell2 = new PdfPCell();
                gpiCell2.setBorder(0);
                PdfPTable gpiTable = buildPdfTable(1);

                buildGpiSurveyOutput(gpiTable, myForm.getGpiSurvey());

                gpiCell2.addElement(gpiTable);
                mainLayout.addCell(gpiCell2);
            }

            /**
             * Activity created by
             */
            if (FeaturesUtil.isVisibleField("Activity Created By")) {
                columnName = TranslatorWorker.translateText("Activity created by");
                String firstName = identification.getActAthFirstName() == null ? "" : identification.getActAthFirstName();
                String lastName = identification.getActAthLastName() == null ? "" : identification.getActAthLastName();
                createGeneralInfoRow(mainLayout, columnName, firstName + " " + lastName);
            }

            /**
             *  Activity created on
             */
            if(FeaturesUtil.isVisibleField("Activity Created On")){
                columnName=TranslatorWorker.translateText("Activity created on");
                createGeneralInfoRow(mainLayout,columnName,identification.getCreatedDate());
            }

            /**
             * Activity Last Updated by
             */
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Last Updated by")) {
                columnName = TranslatorWorker.translateText("Activity last updated by");
                createGeneralInfoRow(mainLayout, columnName, identification.getModifiedBy().getUser().getFirstNames() + " " + identification.getModifiedBy().getUser().getLastName());
            }

            /**
             * Activity updated on
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Updated On")){
                columnName=TranslatorWorker.translateText("Activity updated on");
                createGeneralInfoRow(mainLayout,columnName,identification.getUpdatedDate());
            }

            if (identification.getTeam()!= null){
                /**
                 * Activity created in workspace
                 */
                columnName=TranslatorWorker.translateText("Created in workspace");
                createGeneralInfoRow(mainLayout, columnName, identification.getTeam().getName()
                        + " - "
                        + TranslatorWorker.translateText(identification.getTeam().getAccessType()));

                /**
                 * Workspace manager
                 */
                if (FeaturesUtil.isVisibleField("Data Team Leader")) {
                    columnName = TranslatorWorker.translateText("Workspace manager");
                    createGeneralInfoRow(mainLayout, columnName, identification.getTeam().getTeamLead().getUser().getFirstNames()
                            + " " + identification.getTeam().getTeamLead().getUser().getLastName() + " - "
                            + identification.getTeam().getTeamLead().getUser().getEmail());
                }

                columnName=TranslatorWorker.translateText("Computation");
                createGeneralInfoRow(mainLayout,columnName, Boolean.TRUE.equals(identification.getTeam().getComputation()) ?
                        TranslatorWorker.translateText("Yes") :
                        TranslatorWorker.translateText("No"));

            }

            if (FeaturesUtil.isVisibleModule("/Activity Form/M&E")) {

                PdfPCell meCell = new PdfPCell();
                p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("M & E", locale, siteId)), titleFont);
                p1.setAlignment(Element.ALIGN_RIGHT);
                meCell.addElement(p1);
                meCell.setBackgroundColor(new Color(244, 244, 242));
                meCell.setBorder(0);
                mainLayout.addCell(meCell);

                PdfPTable meTable = buildPdfTable(1);
                meTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                if (myForm.getIndicators() != null) {
                    String valueLabel = TranslatorWorker.translateText("Value");
                    String commentLabel = TranslatorWorker.translateText("Comment");
                    String dateLabel = TranslatorWorker.translateText("Date");
                    String nameLabel = TranslatorWorker.translateText("Name");
                    String codeLabel = TranslatorWorker.translateText("Code");
                    String logFrameLabel = TranslatorWorker.translateText("LogFrame");
                    String sectorsLabel = TranslatorWorker.translateText("Sectors");

                    for (IndicatorActivity indicator : myForm.getIndicators()) {
                        PdfPTable headerTable = buildPdfTable(COLUMNS_4);
                        headerTable.setWidths(new int[]{ 3, 1, 1, 2 });
                        headerTable.setTotalWidth(100);
                        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                        headerTable.getDefaultCell().setBackgroundColor(new Color(244, 244, 242));
                        headerTable.addCell(new Paragraph(postprocessText(nameLabel), plainFont));
                        headerTable.addCell(new Paragraph(postprocessText(codeLabel), plainFont));
                        headerTable.addCell(new Paragraph(postprocessText(logFrameLabel), plainFont));
                        headerTable.addCell(new Paragraph(postprocessText(sectorsLabel), plainFont));
                        headerTable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));

                        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/Name")) {
                            headerTable.addCell(new Paragraph(postprocessText(indicator.getIndicator().getName()), titleFont));
                        }
                        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/Code")) {
                            headerTable.addCell(indicator.getIndicator().getCode());
                        }
                        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/Logframe Category")) {
                            if (indicator.getValues() != null && indicator.getValues().size() > 0) {
                                headerTable.addCell(indicator.getLogFrame());
                            }
                        }

                        if (indicator.getIndicator().getSectors() != null) {
                            headerTable.addCell(new Paragraph(postprocessText(ExportUtil.getIndicatorSectors(indicator) + "\n"), titleFont));
                        }

                        meTable.addCell(headerTable);

                        for (AmpIndicatorValue value : indicator.getValuesSorted()) {
                            columnVal = "";
                            String fieldName = ExportUtil.getIndicatorValueType(value);
                            PdfPCell indicatorTypeCell = new PdfPCell();
                            indicatorTypeCell.addElement(new Paragraph(postprocessText(TranslatorWorker.translateText(ExportUtil.INDICATOR_VALUE_NAME.get(value.getValueType()))), titleFont));
                            indicatorTypeCell.setBorder(0);
                            meTable.addCell(indicatorTypeCell);

                            if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/" + fieldName + " Value/" + fieldName + " Value")) {
                                columnVal += valueLabel + ": " + Strings.nullToEmpty(FormatHelper.formatNumber(value.getValue())) + "\n";
                            }
                            if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/" + fieldName + " Value/" + fieldName + " Comments")) {
                                columnVal += commentLabel + ": " + Strings.nullToEmpty(value.getComment()) + "\n";
                            }
                            if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/" + fieldName + " Value/" + fieldName + " Date")) {
                                columnVal += dateLabel + ": " + DateConversion.convertDateToLocalizedString(value.getValueDate()) + "\n";
                            }
                            PdfPCell valuesCell = new PdfPCell();
                            valuesCell.addElement(new Paragraph(postprocessText(columnVal), plainFont));
                            valuesCell.setBorder(0);
                            meTable.addCell(valuesCell);

                        }
                    }

                    mainLayout.addCell(meTable);
                    mainLayout.addCell(new Paragraph("\n"));
                }
            }
            /**
             * Activity - Performance
             */
            if(FeaturesUtil.isVisibleField("Activity Performance")){
                PdfPCell actPerformanceCell1=new PdfPCell();
                actPerformanceCell1.setBorder(0);
                actPerformanceCell1.setBackgroundColor(new Color(244,244,242));
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Activity Performance", locale, siteId)),titleFont);
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
            if(FeaturesUtil.isVisibleField("Project Risk")){
                PdfPCell riskCell1=new PdfPCell();
                riskCell1.setBorder(0);
                riskCell1.setBackgroundColor(new Color(244,244,242));
                p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Activity Risk", locale, siteId)),titleFont);
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

            /**
             * Structure
             */
            if(FeaturesUtil.isVisibleModule("/Activity Form/Structures")){
                String costOutput="";
                columnName=TranslatorWorker.translateText("Structures");
                Set<AmpStructure> structures = activity.getStructures();

                String sep = "\t:\t ";
                for (AmpStructure struc : structures) {
                    if (struc.getTitle() != null) {
                        costOutput += "\n" + TranslatorWorker.translateText("Title") + sep + struc.getTitle();
                    }
                    if (struc.getType() != null) {
                        costOutput += "\n" + TranslatorWorker.translateText("Type") + sep + struc.getType().getName();
                    }
                    if (struc.getDescription() != null) {
                        costOutput += "\n" + TranslatorWorker.translateText("Description") + sep 
                                + struc.getDescription();
                    }
                    if (struc.getLatitude() != null) {
                        costOutput += "\n" + TranslatorWorker.translateText("Latitude") + sep + struc.getLatitude();
                    }
                    if (struc.getLongitude() != null) {
                        costOutput += "\n" + TranslatorWorker.translateText("Longitude") + sep + struc.getLongitude();
                    }

                    if (struc.getCoordinates() != null && struc.getCoordinates().size() > 0) {
                        StringJoiner coordinatesOutput = new StringJoiner("\n");
                        coordinatesOutput.add(TranslatorWorker.translateText("Coordinates") + sep);
                        for (AmpStructureCoordinate coordinate : struc.getCoordinates()) {
                            coordinatesOutput.add(coordinate.getLatitude() + " " + coordinate.getLongitude());
                        }
                        costOutput += "\n" + coordinatesOutput.toString();
                    }

                    costOutput+="\n";
                }

                createGeneralInfoRow(mainLayout,columnName,costOutput);
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

    private PdfPCell buildProgramCell(String programText, boolean toTranslate) {
        String text = toTranslate ? TranslatorWorker.translateText(programText) : programText;
        PdfPCell programsCell1 = new PdfPCell();
        Paragraph p1 = new Paragraph(postprocessText(text), plainFont);
        p1.setAlignment(Element.ALIGN_LEFT);
        programsCell1.addElement(p1);
        programsCell1.setBorder(0);
        
        return programsCell1;
    }

    private PdfPTable buildPdfTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        if (SiteUtils.isEffectiveLangRTL()) {
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        }
        return table;
    }

    private void buildProjectCost(PdfPTable mainLayout, ProposedProjCost proProjCost, String columnName) {
        String costOutput = "";
        columnName = TranslatorWorker.translateText(columnName);
        if (proProjCost != null) {
            if (proProjCost.getFunAmount() != null) {
                costOutput += " " + proProjCost.getFunAmount();
            }
            if (proProjCost.getCurrencyCode() != null) {
                costOutput += " " + proProjCost.getCurrencyCode();
            }
            if (proProjCost.getFunDate() != null){
                costOutput += "\n" + TranslatorWorker.translateText("Date") + "\t: " + proProjCost.getFunDate();
            }
        }
        createGeneralInfoRow(mainLayout, columnName, costOutput);
    }

    private void buildCommentsPart(String fieldName, String columnName, Map<String, List<AmpComments>> allComments,
                                   String locale, Long siteId, PdfPTable mainLayout) throws WorkerException {
        List<String> outList = new ArrayList<String>();
        for (Object commentKey : allComments.keySet()) {
            String key=(String)commentKey;
            List<AmpComments> values=(List<AmpComments>)allComments.get(key);
            if(key.equalsIgnoreCase(fieldName)){
                for (AmpComments value : values) {
                    outList.add(postprocessText(TranslatorWorker.translateText(value.getComment(), locale, siteId)));
                }
            }
        }
        for (int i = 0; i < outList.size(); i++) {
            createGeneralInfoRow(mainLayout, postprocessText(TranslatorWorker.translateText(columnName,locale,siteId) + " " + (i+1)), outList.get(i));
        }
    }


    private void buildAidEffectivenessInformationPart(PdfPTable mainLayout, List<String[]> aidEffectivenesToAdd) throws WorkerException {
        String columnName = "";
        columnName = TranslatorWorker.translateText("Aid Effectivenes");
        createGeneralInfoRowAid(mainLayout, columnName, aidEffectivenesToAdd);
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
        Paragraph descriptionP=new Paragraph(postprocessText(columnName),titleFont);
        cell1.addElement(descriptionP);
        ipaInnerTable.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        cell2.setBorder(0);
        cell2.addElement(new Paragraph(postprocessText(columnValue),plainFont));
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

                String startTagStrClosed = "<"+tag+">";
                String startTagStrOpened = "<"+tag+" ";

                int endTagLength = endTagStr.length();
              //AMP-20746 fixed the problem related with editor from form
                if(text.contains("/"+tag+">") && !text.contains("</"+tag+">"))
                    text = text.replace("/"+tag+">", "</"+tag+">");

                while(text.contains(startTagStrOpened) || text.contains(startTagStrClosed)){
                    int firstIndexOfStartTag=0;
                    int firstIndexO = text.indexOf(startTagStrOpened);
                    int firstIndexC = text.indexOf(startTagStrClosed);
                    if((firstIndexO > -1 && firstIndexC < 0)||(firstIndexO >-1 && firstIndexO < firstIndexC))
                        firstIndexOfStartTag = firstIndexO;
                    else
                        firstIndexOfStartTag = firstIndexC;

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


    private void buildIssuesPart(EditActivityForm myForm, PdfPTable mainLayout,ServletContext ampContext,HttpSession session)   throws WorkerException {
        if (myForm.getIssues().getIssues()==null || myForm.getIssues().getIssues().isEmpty())
            return;
        Paragraph p1;
        PdfPCell issuesCell1=new PdfPCell();
        issuesCell1.setBackgroundColor(new Color(244,244,242));
        issuesCell1.setBorder(0);
        p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Issues")),titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        issuesCell1.addElement(p1);
        mainLayout.addCell(issuesCell1);

        PdfPCell issuesCell2=new PdfPCell();
        issuesCell2.setBackgroundColor(new Color(255,255,255));
        issuesCell2.setBorder(0);
        if(myForm.getIssues().getIssues()!=null && myForm.getIssues().getIssues().size()>0){
            for (org.digijava.module.aim.helper.Issues issue : myForm.getIssues().getIssues()) {
                com.lowagie.text.List issuesList=new com.lowagie.text.List(false,20);  //is not numbered list
                issuesList.setListSymbol(new Chunk("\u2022"));
                String issueName = issue.getName();
                if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Date")){
                    issueName += " \t"+issue.getIssueDate();
                }
                ListItem issueItem=new ListItem(new Phrase(issueName,plainFont));
                issuesList.add(issueItem);
                if(FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure") &&
                        issue.getMeasures()!=null){
                    com.lowagie.text.List measuresSubList=new com.lowagie.text.List(false,20);  //is not numbered list
                    measuresSubList.setListSymbol("-");
                    for (Measures measure : issue.getMeasures()) {
                        String measureName = measure.getName();
                        if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure/Date")) {
                            measureName += " \t"+measure.getMeasureDate();
                        }
                        ListItem measureItem=new ListItem(new Phrase(measureName,plainFont));
                        measuresSubList.add(measureItem);
                        if(FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure/Actor") && measure.getActors()!=null && measure.getActors().size()>0){
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
                issuesCell2.addElement(issuesList);
            }
        }
        mainLayout.addCell(issuesCell2);
    }
    
    private void buildRegionalObservationsPart(EditActivityForm myForm, PdfPTable mainLayout)
            throws WorkerException {
        ArrayList<Issues> regObs = myForm.getRegionalObservations().getIssues();
        if (regObs == null || regObs.isEmpty()) {
            return;
        }
        
        PdfPCell regObsTitleCell = new PdfPCell();
        regObsTitleCell.setBackgroundColor(BACKGROUND_COLOR);
        regObsTitleCell.setBorder(0);
        
        Paragraph p1;
        p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("Regional Observations")), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        
        regObsTitleCell.addElement(p1);
        mainLayout.addCell(regObsTitleCell);
        
        PdfPCell regObsValuesCell = new PdfPCell();
        regObsValuesCell.setBackgroundColor(BACKGROUND_COLOR_WHITE);
        regObsValuesCell.setBorder(0);
        
        String regObsModulePath = "/Activity Form/Regional Observations/Observation";
        String regObsDatePath = regObsModulePath + "/Date";
        String regObsMeasurePath = regObsModulePath + "/Measure";
        String regObsActorPath = regObsMeasurePath + "/Actor";
        
        for (Issues issue : regObs) {
            com.lowagie.text.List issuesList = new com.lowagie.text.List(false, SYMBOL_INDENT);
            issuesList.setListSymbol(new Chunk("\u2022"));
            String issueName = issue.getName();
            if (FeaturesUtil.isVisibleModule(regObsDatePath)) {
                issueName += " \t" + issue.getIssueDate();
            }
            
            ListItem issueItem = new ListItem(new Phrase(issueName, plainFont));
            issuesList.add(issueItem);
            if (issue.getMeasures() != null && issue.getMeasures().size() > 0
                    && FeaturesUtil.isVisibleModule(regObsMeasurePath)) {
                com.lowagie.text.List measuresSubList = new com.lowagie.text.List(false, SYMBOL_INDENT);
                measuresSubList.setListSymbol("-");
                
                for (Measures measure : issue.getMeasures()) {
                    ListItem measureItem = new ListItem(new Phrase(measure.getName(), plainFont));
                    measuresSubList.add(measureItem);
                    
                    if (measure.getActors() != null && measure.getActors().size() > 0
                            && FeaturesUtil.isVisibleModule(regObsActorPath)) {
                        com.lowagie.text.List actorsSubList = new com.lowagie.text.List(false, SYMBOL_INDENT);
                        actorsSubList.setListSymbol(new Chunk("\u2022"));
                        
                        for (AmpActor actor : measure.getActors()) {
                            ListItem actorItem = new ListItem(new Phrase(actor.getName(), plainFont));
                            actorsSubList.add(actorItem);
                        }
                        measuresSubList.add(actorsSubList);
                    }
                }
                issuesList.add(measuresSubList);
            }
            regObsValuesCell.addElement(issuesList);
        }
        mainLayout.addCell(regObsValuesCell);
    }
    
    private void buildLineMinistryObservationsPart(EditActivityForm myForm, PdfPTable mainLayout)
            throws WorkerException {
        ArrayList<Issues> lmo = myForm.getLineMinistryObservations().getIssues();
        if (lmo == null || lmo.isEmpty()) {
            return;
        }
    
        PdfPCell lmoTitleCell = new PdfPCell();
        lmoTitleCell.setBackgroundColor(BACKGROUND_COLOR);
        lmoTitleCell.setBorder(0);
    
        Paragraph p1;
        p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("Line Ministry Observations")), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
    
        lmoTitleCell.addElement(p1);
        mainLayout.addCell(lmoTitleCell);
    
        PdfPCell lmoValuesCell = new PdfPCell();
        lmoValuesCell.setBackgroundColor(BACKGROUND_COLOR_WHITE);
        lmoValuesCell.setBorder(0);
    
        String lmoModulePath = "/Activity Form/Line Ministry Observations/Observation";
        String lmoDatePath = lmoModulePath + "/Date";
        String lmoMeasurePath = lmoModulePath + "/Measure";
        String lmoActorPath = lmoMeasurePath + "/Actor";
    
        for (Issues issue : lmo) {
            com.lowagie.text.List issuesList = new com.lowagie.text.List(false, SYMBOL_INDENT);
            issuesList.setListSymbol(new Chunk("\u2022"));
            String issueName = issue.getName();
            if (FeaturesUtil.isVisibleModule(lmoDatePath)) {
                issueName += " \t" + issue.getIssueDate();
            }
            
            ListItem issueItem = new ListItem(new Phrase(issueName, plainFont));
            issuesList.add(issueItem);
            if (issue.getMeasures() != null && issue.getMeasures().size() > 0
                    && FeaturesUtil.isVisibleModule(lmoMeasurePath)) {
                com.lowagie.text.List measuresSubList = new com.lowagie.text.List(false, SYMBOL_INDENT);
                measuresSubList.setListSymbol("-");
                
                for (Measures measure : issue.getMeasures()) {
                    ListItem measureItem = new ListItem(new Phrase(measure.getName(), plainFont));
                    measuresSubList.add(measureItem);
                    
                    if (measure.getActors() != null && measure.getActors().size() > 0
                            && FeaturesUtil.isVisibleModule(lmoActorPath)) {
                        com.lowagie.text.List actorsSubList = new com.lowagie.text.List(false, SYMBOL_INDENT);
                        actorsSubList.setListSymbol(new Chunk("\u2022"));
                        
                        for (AmpActor actor : measure.getActors()) {
                            ListItem actorItem = new ListItem(new Phrase(actor.getName(), plainFont));
                            actorsSubList.add(actorItem);
                        }
                        measuresSubList.add(actorsSubList);
                    }
                }
                issuesList.add(measuresSubList);
            }
            lmoValuesCell.addElement(issuesList);
        }
        mainLayout.addCell(lmoValuesCell);
    }
    
    private void buildCostingPart(HttpServletRequest request, Long actId,PdfPTable mainLayout,ServletContext ampContext) throws WorkerException, AimException {
        int fmVisibleFieldsCounter=0;
        String [] costingFmfields={"Costing Activity Name","Costing Total Cost","Costing Total Contribution"};
        for(int i=0;i<costingFmfields.length;i++){
            if(FeaturesUtil.isVisibleField(costingFmfields[i])){
                fmVisibleFieldsCounter++;
            }
        }
        if(fmVisibleFieldsCounter>0){
            Paragraph p1;
            HttpSession session=request.getSession();
            PdfPCell costingCell1=new PdfPCell();
            costingCell1.setBorder(0);
            costingCell1.setBackgroundColor(new Color(244,244,242));
            p1=new Paragraph(postprocessText(TranslatorWorker.translateText("Costing")),titleFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
            costingCell1.addElement(p1);
            mainLayout.addCell(costingCell1);


            PdfPCell costingCell2=new PdfPCell();
            costingCell2.setBorder(1);
            costingCell2.setBorderColor(new Color(201,201,199));

            PdfPTable costingInnerTable = buildPdfTable(fmVisibleFieldsCounter); //table with 3 cells
            BigDecimal grandCost = new BigDecimal(0);
            BigDecimal grandContribution = new BigDecimal(0);
            if(FeaturesUtil.isVisibleField("Costing Activity Name")){
                PdfPCell nameCell=new PdfPCell(new Paragraph(postprocessText(TranslatorWorker.translateText("name")),titleFont));
                nameCell.setBorder(0);
                nameCell.setBackgroundColor(new Color(244,244,242));
                costingInnerTable.addCell(nameCell);
            }

            if(FeaturesUtil.isVisibleField("Costing Total Cost")){
                PdfPCell totalCostCell=new PdfPCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Total Cost")),titleFont));
                totalCostCell.setBorder(0);
                totalCostCell.setBackgroundColor(new Color(244,244,242));
                costingInnerTable.addCell(totalCostCell);
            }

            if(FeaturesUtil.isVisibleField("Costing Total Contribution")){
                PdfPCell totalContrCell=new PdfPCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Total Contribution")),titleFont));
                totalContrCell.setBorder(0);
                totalContrCell.setBackgroundColor(new Color(244,244,242));
                costingInnerTable.addCell(totalContrCell);
            }


            Collection euActs = EUActivityUtil.getEUActivities(actId); //costs
            if(euActs!=null && euActs.size()>0){

                TeamMember tm = (TeamMember) session.getAttribute(CURRENT_MEMBER);
                Long defaultCurrency=null;
                if (tm != null && tm.getAppSettings() != null && tm.getAppSettings().getCurrencyId() != null) {
                    defaultCurrency = tm.getAppSettings().getCurrencyId();
                } else{
                    defaultCurrency = CurrencyUtil.getDefaultCurrency().getAmpCurrencyId();
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
                    if(FeaturesUtil.isVisibleField("Costing Activity Name")){
                        PdfPCell name=new PdfPCell(new Paragraph(postprocessText(euActivity.getName()),titleFont));
                        name.setBorder(0);
                        name.setBackgroundColor(new Color(255,255,255));
                        costingInnerTable.addCell(name);
                    }

                    //euAct totalsConverted
                    if(FeaturesUtil.isVisibleField("Costing Total Cost")){
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
                    if(FeaturesUtil.isVisibleField("Costing Total Contribution")){
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
                    if(FeaturesUtil.isVisibleField("Costing Inputs") && euActivity.getInputs()!=null){
                        euInfo+= TranslatorWorker.translateText("Inputs")+":"+ euActivity.getInputs() + "\n";
                    }
                    if(FeaturesUtil.isVisibleField("Costing Assumptions") && euActivity.getAssumptions()!=null){
                        euInfo+= TranslatorWorker.translateText("Assumptions")+":"+ euActivity.getAssumptions() + "\n";
                    }
                    if(FeaturesUtil.isVisibleField("Costing Progress") && euActivity.getProgress()!=null){
                        euInfo+= TranslatorWorker.translateText("Progress")+":"+ euActivity.getProgress() + "\n";
                    }
                    if(FeaturesUtil.isVisibleField("Costing Due Date") && euActivity.getDueDate()!=null){
                        euInfo+= TranslatorWorker.translateText("Due Date")+":"+ DateConversion.convertDateToLocalizedString(euActivity.getDueDate()) + "\n";
                    }
                    anotherInfo.addElement(new Paragraph(postprocessText(euInfo),plainFont));
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
                Paragraph total1=new Paragraph(postprocessText(TranslatorWorker.translateText("Totals"))+": ",titleFont);
                total1.setAlignment(Element.ALIGN_RIGHT);
                totals.addElement(total1);
                costingInnerTable.addCell(totals);

                NumberFormat formatter = FormatHelper.getDecimalFormat();
                if(FeaturesUtil.isVisibleField("Grand Total Cost")){
                    PdfPCell grandCostCell=new PdfPCell();
                    grandCostCell.setBorder(0);

                    String grTotal=formatter.format(grandCost);
                    Paragraph gc1=new Paragraph(postprocessText(grTotal),plainFont);
                    grandCostCell.addElement(gc1);
                    costingInnerTable.addCell(grandCostCell);

                    PdfPCell grandContributionCell=new PdfPCell();
                    grandContributionCell.setBorder(0);
                    String grContTotal=formatter.format(grandContribution) ;
                    Paragraph gtc1=new Paragraph(postprocessText(grContTotal),plainFont);
                    grandContributionCell.addElement(gtc1);
                    costingInnerTable.addCell(grandContributionCell);
                }

                if(FeaturesUtil.isVisibleField("Costing Contribution Gap")){
                    PdfPCell contGap=new PdfPCell();
                    contGap.setBorder(0);
                    Paragraph contGap1=new Paragraph(postprocessText(TranslatorWorker.translateText("Contribution Gap"))+": ",titleFont);
                    contGap1.setAlignment(Element.ALIGN_RIGHT);
                    contGap.addElement(contGap1);
                    costingInnerTable.addCell(contGap);

                    PdfPCell contGapCell=new PdfPCell();
                    contGapCell.setBorder(0);
                    String contributionGap=formatter.format(grandCost.subtract(grandContribution));
                    Paragraph cg=new Paragraph(postprocessText(contributionGap),plainFont);
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
    }

    public final static String getLatinLettersEquivalent(String src)// returns text without diacritics should be called with a Printable Equivalent
    {
        src = src.replace('ţ', 'ţ').replace("ț", "ţ").replace('Ţ', 'Ţ').replace('î', 'î').replace('ş', 'ş').replace('Ş', 'Ş').replace("ş", "ş").replace("î", "î").replace("ă", "ă").replace("ţ", "ţ");
        src = src.replace("ș", "ş").replace("ț", "ţ").replace('Ț', 'Ţ');

        // this is the "paranoid" line
        src = src.replace('ă', 'a').replace('ş', 's').replace('ţ', 't').replace('î', 'i').replace('Ş', 'S').replace('Ţ', 'T').replace('ă', 'a');

        return src;

    }

    /**
     * entry point for post-processing
     * @param src
     * @return
     */
    public final static String postprocessText(String src)
    {
        if (src == null)
            return null;
        return getLatinLettersEquivalent(src);
    }

    private void buildContractsPart(EditActivityForm myForm, HttpServletRequest request, PdfPTable mainLayout)throws WorkerException{
        HttpSession session=request.getSession();
        ServletContext ampContext = getServlet().getServletContext();
        String columnName="";
        String output ="";
        if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts")){
            PdfPCell ipaContract1=new PdfPCell();
            ipaContract1.setBorder(0);
            ipaContract1.setBackgroundColor(new Color(244,244,242));
            Paragraph ipaContractP=new Paragraph(postprocessText(TranslatorWorker.translateText("IPA Contracting")),titleFont);
            ipaContractP.setAlignment(Element.ALIGN_RIGHT);
            ipaContract1.addElement(ipaContractP);
            mainLayout.addCell(ipaContract1);

            PdfPCell ipaContracting2=new PdfPCell();
            ipaContracting2.setBorder(1);
            //inner table with two cells
            PdfPTable ipaInnerTable = buildPdfTable(2);
            if(myForm.getContracts().getContracts()!=null){
                for (IPAContract contract : (List<IPAContract>)myForm.getContracts().getContracts()) {
                    //name
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Info/Contract Name")){
                        columnName=TranslatorWorker.translateText("Contract Name")+":";
                        createContractingTblRows(ipaInnerTable,columnName,contract.getContractName());
                    }

                    //description
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Info/Contract Description")){
                        columnName=TranslatorWorker.translateText("Contract Description")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getDescription());
                    }

                    //activity category
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Info/Activity Type")){
                        columnName=TranslatorWorker.translateText("Activity Category")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getActivityCategory()!=null?contract.getActivityCategory().getValue():"");
                    }

                    //type
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Info/Contract Type")){
                        columnName=TranslatorWorker.translateText("Type")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getType()!=null?contract.getType().getValue():"");
                    }

                    //start of tendering
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering")){
                        columnName=TranslatorWorker.translateText("Start of Tendering")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedStartOfTendering());
                    }

                    //Signature of Contract
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Details/Signature")){
                        columnName=TranslatorWorker.translateText("Signature of Contract")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedSignatureOfContract());
                    }

                    columnName=TranslatorWorker.translateText("Contract Organization")+":";
                    // Contract Organization
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Organizations")){
                        createContractingTblRows(ipaInnerTable, columnName,contract.getOrganization()!=null?contract.getOrganization().getName():"");
                    }

                    // Contract Organization
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name")){
                        createContractingTblRows(ipaInnerTable, columnName,contract.getContractingOrganizationText());
                    }

                    //Contract Completion
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Details/Completion")){
                        columnName=TranslatorWorker.translateText("Contract Completion")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getFormattedContractCompletion());
                    }

                    //Status
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Details/Status")){
                        columnName=TranslatorWorker.translateText("Status")+":";
                        createContractingTblRows(ipaInnerTable, columnName,contract.getStatus()!=null?contract.getStatus().getValue():"");
                    }

                    //Total Amount
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value")){
                        columnName=TranslatorWorker.translateText("Total Amount")+":";
                        output=contract.getTotalAmount()!=null? contract.getTotalAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode()  : " ";
                        createContractingTblRows(ipaInnerTable, columnName,output);
                    }

                    //Total EC Contribution
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract Total Amount")){
                        PdfPCell totalECCont=new PdfPCell();
                        totalECCont.setBorder(0);
                        totalECCont.setColspan(2);
                        Paragraph totalECContP=new Paragraph(postprocessText(TranslatorWorker.translateText("Total EC Contribution"))+":",titleFont);
                        totalECCont.addElement(totalECContP);
                        ipaInnerTable.addCell(totalECCont);
                    }

                    //IB
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount")){
                        columnName=TranslatorWorker.translateText("IB")+":";
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
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV Amount")){
                        columnName=TranslatorWorker.translateText("Contracting INV")+":";
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

                    PdfPCell totalNationalCont=new PdfPCell();
                    totalNationalCont.setBorder(0);
                    totalNationalCont.setColspan(2);
                    Paragraph totalNationalContP=new Paragraph(postprocessText(TranslatorWorker.translateText("Total National Contribution"))+":",titleFont);
                    totalNationalCont.addElement(totalNationalContP);
                    ipaInnerTable.addCell(totalNationalCont);

                    //Central
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central Amount")){
                        columnName=TranslatorWorker.translateText("Contracting Central Amount")+":";
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
                    if(FeaturesUtil.isVisibleField("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional Amount")){
                        columnName=TranslatorWorker.translateText("Regional")+":";
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
                    if(FeaturesUtil.isVisibleField("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI Amount")){
                        columnName=TranslatorWorker.translateText("IFIs")+":";
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
                    PdfPCell totalPrivateCont=new PdfPCell();
                    totalPrivateCont.setBorder(0);
                    totalPrivateCont.setColspan(2);
                    Paragraph totalPrivateContP=new Paragraph(postprocessText(TranslatorWorker.translateText("Total Private Contribution"))+":",titleFont);
                    totalPrivateCont.addElement(totalPrivateContP);
                    ipaInnerTable.addCell(totalPrivateCont);

                    //IB
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount")){
                        columnName=TranslatorWorker.translateText("IB")+":";
                        if(contract.getTotalPrivateContribAmount()!=null){
                            output=contract.getTotalPrivateContribAmount().floatValue()+" "+contract.getTotalAmountCurrency().getCurrencyCode();
                        }else if(contract.getTotalAmountCurrency()!=null){
                            output=" "+contract.getTotalAmountCurrency().getCurrencyCode();
                        }else{
                            output="";
                        }
                        createContractingTblRows(ipaInnerTable, columnName,output);
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements")){

                        Integer disbFieldsCount=0;
                        disbFieldsCount += FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type")?1:0;
                        disbFieldsCount += FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount")?1:0;
                        disbFieldsCount += FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency")?1:0;
                        disbFieldsCount += FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date")?1:0;

                        //Total disbursements
                        columnName=TranslatorWorker.translateText("Total disbursements")+":";
                        output=(contract.getTotalDisbursements()!=null ? contract.getTotalDisbursements().floatValue() : " ")+" ";
                        output+=contract.getDibusrsementsGlobalCurrency()!=null?contract.getDibusrsementsGlobalCurrency().getCurrencyCode():myForm.getCurrCode();
                        createContractingTblRows(ipaInnerTable, columnName,output);

                        columnName=TranslatorWorker.translateText("Total Funding Disbursements")+":";
                        output=(contract.getFundingTotalDisbursements()!=null? contract.getFundingTotalDisbursements().floatValue() : " ")+" ";
                        output+=contract.getDibusrsementsGlobalCurrency()!=null?contract.getDibusrsementsGlobalCurrency().getCurrencyCode():myForm.getCurrCode();
                        createContractingTblRows(ipaInnerTable, columnName,output);

                        //Contract Execution Rate
                        columnName=TranslatorWorker.translateText("Contract Execution Rate")+":";
                        createContractingTblRows(ipaInnerTable, columnName,(contract.getExecutionRate()!=null ? contract.getExecutionRate().floatValue() : " ")+"");

                        //Contract Execution Rate
                        columnName=TranslatorWorker.translateText("Contract Funding Execution Rate")+":";
                        createContractingTblRows(ipaInnerTable, columnName,(contract.getFundingExecutionRate()!=null ? contract.getFundingExecutionRate().floatValue() : " ")+"");
                        if(disbFieldsCount >0){
                            //Disbursements
                            PdfPCell disbs1=new PdfPCell();
                            disbs1.setBorder(0);
                            Paragraph disbsP = new Paragraph(postprocessText(
                                    TranslatorWorker.translateText("Disbursements")) + ":", titleFont);
                            disbs1.addElement(disbsP);
                            ipaInnerTable.addCell(disbs1);

                            PdfPCell disbs2=new PdfPCell();
                            disbs2.setBorder(0);
                            PdfPTable disbursmentsInnerTable = buildPdfTable(disbFieldsCount - 1);
                            if(contract.getDisbursements()!=null){
                                for (IPAContractDisbursement ipaDisb : (Set<IPAContractDisbursement>)contract.getDisbursements()) {
                                    if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type")){
                                        PdfPCell adjType=new PdfPCell();
                                        adjType.setBorder(0);
                                        adjType.addElement(new Paragraph(ipaDisb.getAdjustmentType().getValue(),plainFont));
                                        disbursmentsInnerTable.addCell(adjType);
                                    }

                                    if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount")){
                                        PdfPCell amount=new PdfPCell();
                                        amount.setBorder(0);
                                        String currency ="";
                                        if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency")){
                                            currency = ipaDisb.getCurrency().getCurrencyCode();
                                        }
                                        amount.addElement(new Paragraph(ipaDisb.getAmount().floatValue() + " "+ currency, plainFont));
                                        disbursmentsInnerTable.addCell(amount);
                                    }

                                    if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date")){
                                        PdfPCell disbDate=new PdfPCell();
                                        disbDate.setBorder(0);
                                        disbDate.addElement(new Paragraph(ipaDisb.getDisbDate(),plainFont));
                                        disbursmentsInnerTable.addCell(disbDate);
                                    }
                                }
                            }

                            disbs2.addElement(disbursmentsInnerTable);
                            ipaInnerTable.addCell(disbs2);


                            //Funding Disbursements
                            PdfPCell fundingDisbs1=new PdfPCell();
                            fundingDisbs1.setBorder(0);
                            Paragraph fundDisbsP = new Paragraph(postprocessText(
                                    TranslatorWorker.translateText("Funding Disbursements")) + ":", titleFont);
                            fundingDisbs1.addElement(fundDisbsP);
                            ipaInnerTable.addCell(fundingDisbs1);

                            PdfPCell fundingDisbs2=new PdfPCell();
                            fundingDisbs2.setBorder(0);
                            PdfPTable fundDisbursmentsInnerTable = buildPdfTable(disbFieldsCount);

                            if(myForm.getFunding()!=null){
                                if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type")){
                                    PdfPCell adjType=new PdfPCell();
                                    adjType.setBorder(0);
                                    adjType.addElement(new Paragraph(postprocessText(TranslatorWorker.translateText("Adj. Type Disb.")),plainFont));
                                    fundDisbursmentsInnerTable.addCell(adjType);
                                }

                                if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount")){
                                    PdfPCell ampuntDisb=new PdfPCell();
                                    ampuntDisb.setBorder(0);
                                    ampuntDisb.addElement(new Paragraph(postprocessText(TranslatorWorker.translateText("Amount Disb.")),plainFont));
                                    fundDisbursmentsInnerTable.addCell(ampuntDisb);
                                }

                                if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency")){
                                    PdfPCell currencyDisb=new PdfPCell();
                                    currencyDisb.setBorder(0);
                                    currencyDisb.addElement(new Paragraph(postprocessText(TranslatorWorker.translateText("Currency Disb.")),plainFont));
                                    fundDisbursmentsInnerTable.addCell(currencyDisb);
                                }

                                if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date")){
                                    PdfPCell dateDisb=new PdfPCell();
                                    dateDisb.setBorder(0);
                                    dateDisb.addElement(new Paragraph(postprocessText(TranslatorWorker.translateText("Date Disb.")),plainFont));
                                    fundDisbursmentsInnerTable.addCell(dateDisb);
                                }

                                for (FundingDetail fundingDetail : myForm.getFunding().getFundingDetails()) {
                                    if(fundingDetail.getContract()!=null && contract.getContractName().equals(fundingDetail.getContract().getContractName()) && fundingDetail.getTransactionType()==1){
                                        if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type")){
                                            PdfPCell adjType=new PdfPCell();
                                            adjType.setBorder(0);
                                            adjType.addElement(new Paragraph(fundingDetail.getAdjustmentTypeName().getValue(),plainFont));
                                            fundDisbursmentsInnerTable.addCell(adjType);
                                        }
                                        if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount")){
                                            PdfPCell amount=new PdfPCell();
                                            amount.setBorder(0);
                                            amount.addElement(new Paragraph(fundingDetail.getTransactionAmount(),plainFont));
                                            fundDisbursmentsInnerTable.addCell(amount);
                                        }
                                        if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency")){
                                            PdfPCell currency=new PdfPCell();
                                            currency.setBorder(0);
                                            currency.addElement(new Paragraph(fundingDetail.getCurrencyCode(),plainFont));
                                            fundDisbursmentsInnerTable.addCell(currency);
                                        }
                                        if (FeaturesUtil.isVisibleModule("/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date")){
                                            PdfPCell disbDate=new PdfPCell();
                                            disbDate.setBorder(0);
                                            disbDate.addElement(new Paragraph(fundingDetail.getTransactionDate(),plainFont));
                                            fundDisbursmentsInnerTable.addCell(disbDate);
                                        }
                                    }

                                }
                            }
                            fundingDisbs2.addElement(fundDisbursmentsInnerTable);
                            ipaInnerTable.addCell(fundingDisbs2);
                        }
                    }
                }
            }

            ipaContracting2.addElement(ipaInnerTable);
            mainLayout.addCell(ipaContracting2);
        }
    }

    private void buildRelatedDocsPart(EditActivityForm myForm,PdfPTable mainLayout, PdfPTableEvents event,ServletContext ampContext) throws WorkerException {
        Paragraph p1;
        PdfPCell relDocCell1=new PdfPCell();
        relDocCell1.setBackgroundColor(new Color(244,244,242));
        relDocCell1.setBorder(0);
        p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("Related Documents")), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        relDocCell1.addElement(p1);
        mainLayout.addCell(relDocCell1);

        PdfPCell relDocCell2=new PdfPCell();
        relDocCell2.setBackgroundColor(new Color(255,255,255));
        relDocCell2.setBorder(0);
        PdfPTable relatedDocnested = buildPdfTable(2);
        relatedDocnested.setTableEvent(event);
        //documents
        if(myForm.getDocuments().getCrDocuments()!=null && myForm.getDocuments().getCrDocuments().size()>0 || myForm.getDocuments().getDocuments() != null && myForm.getDocuments().getDocuments().size()>0)
        {
            for (Documents doc : myForm.getDocuments().getDocuments()) {
                if(doc.getIsFile()){
                    //document fields
                    PdfPCell docTableNameCell1=new PdfPCell(new Paragraph(new Phrase(postprocessText(doc.getTitle()))+"- \t",plainFont));
                    docTableNameCell1.setBackgroundColor(new Color(255,255,255));
                    docTableNameCell1.setBorder(0);
                    relatedDocnested.addCell(docTableNameCell1);
                    PdfPCell docTableNameCell2=new PdfPCell(new Paragraph(new Phrase(doc.getFileName(),titleFont)));
                    docTableNameCell2.setBackgroundColor(new Color(255,255,255));
                    docTableNameCell2.setBorder(0);
                    relatedDocnested.addCell(docTableNameCell2);

                    PdfPCell docTableDescCell1=new PdfPCell(new Paragraph(new Phrase(TranslatorWorker.translateText("Description")+":",titleFont)));
                    docTableDescCell1.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell1.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell1);
                    PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(postprocessText(doc.getDocDescription()),plainFont));
                    docTableDescCell2.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell2.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell2);

                    PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date")+":",titleFont));
                    docTableDateCell1.setBackgroundColor(new Color(255,255,255));
                    docTableDateCell1.setBorder(0);
                    relatedDocnested.addCell(docTableDateCell1);
                    PdfPCell docTableDateCell2=new PdfPCell(new Paragraph(new Phrase(doc.getDate(), plainFont)));
                    docTableDateCell2.setBackgroundColor(new Color(255,255,255));
                    docTableDateCell2.setBorder(0);
                    relatedDocnested.addCell(docTableDateCell2);

                    if(doc.getDocType()!=null){
                        PdfPCell docTabletypeCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Document Type")+":",titleFont));
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
                    PdfPCell docTableDescCell1=new PdfPCell(new Paragraph(new Phrase(TranslatorWorker.translateText("Description")+":",titleFont)));
                    docTableDescCell1.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell1.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell1);
                    PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(crDoc.getDescription(), plainFont));
                    docTableDescCell2.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell2.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell2);
                    //date
                    if(crDoc.getCalendar()!=null){
                        PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date")+":",titleFont));
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
        /*if(myForm.getDocuments().getLinksList()!=null && myForm.getDocuments().getLinksList().size()>0){              
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
                    
                    PdfPCell docTableDescCell1=new PdfPCell(new Phrase(TranslatorWorker.translateText("Description")+":",titleFont));
                    docTableDescCell1.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell1.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell1);
                    PdfPCell docTableDescCell2=new PdfPCell(new Paragraph(doc.getRelLink().getDescription(),plainFont));
                    docTableDescCell2.setBackgroundColor(new Color(255,255,255));
                    docTableDescCell2.setBorder(0);
                    relatedDocnested.addCell(docTableDescCell2);
                    
                    PdfPCell docTableDateCell1=new PdfPCell(new Paragraph(TranslatorWorker.translateText("Date")+":",titleFont));
                    docTableDateCell1.setBackgroundColor(new Color(255,255,255));
                    docTableDateCell1.setBorder(0);
                    relatedDocnested.addCell(docTableDateCell1);
                    PdfPCell docTableDateCell2=new PdfPCell(new Paragraph(new Phrase(doc.getRelLink().getDate(), plainFont)));
                    docTableDateCell2.setBackgroundColor(new Color(255,255,255));
                    docTableDateCell2.setBorder(0);
                    relatedDocnested.addCell(docTableDateCell2);                
            }               
        }*/
        relDocCell2.addElement(relatedDocnested);
        mainLayout.addCell(relDocCell2);
    }

    private void buildComponentsPart(EditActivityForm myForm, PdfPTable mainLayout, ServletContext ampContext,
                                     HttpSession session) throws WorkerException, DocumentException {
        Paragraph p1;
        if (GlobalSettings.getInstance().getShowComponentFundingByYear() != null && FeaturesUtil.isVisibleModule
                ("/Activity Form/Components")) {
            PdfPCell compCell1 = new PdfPCell();
            p1 = new Paragraph(TranslatorWorker.translateText("Components"), titleFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
            compCell1.addElement(p1);
            compCell1.setBackgroundColor(new Color(244, 244, 242));
            compCell1.setBorder(0);
            mainLayout.addCell(compCell1);
            //now we should create nested table and add it as second cell in mainLayout
            PdfPTable componentsNestedTable = buildPdfTable(2);
            componentsNestedTable.getDefaultCell().setBorder(1);

            boolean visibleModuleCompCommitments = FeaturesUtil.isVisibleModule("/Activity "
                    + "Form/Components/Component/Components Commitments");
            boolean visibleModuleCompDisbursements = FeaturesUtil.isVisibleModule("/Activity "
                    + "Form/Components/Component/Components Disbursements");
            boolean visibleModuleCompExpenditures = FeaturesUtil.isVisibleModule("/Activity "
                    + "Form/Components/Component/Components Expenditures");

            if (myForm.getComponents().getSelectedComponents() != null) {
                for (Components<FundingDetail> comp : myForm.getComponents().getSelectedComponents()) {
                    //first row- title
                    PdfPCell nestedCell1 = new PdfPCell();
                    nestedCell1.setBackgroundColor(new Color(255, 255, 255));
                    nestedCell1.setBorder(0);
                    nestedCell1.setColspan(2);
                    p1 = new Paragraph(TranslatorWorker.translateText(comp.getTitle()), titleFont);
                    nestedCell1.addElement(p1);
                    componentsNestedTable.addCell(nestedCell1);

                    if (!GlobalSettings.getInstance().getShowComponentFundingByYear()) { //false case
                        //Description
                        PdfPCell descNestedCell = new PdfPCell();
                        p1 = new Paragraph(TranslatorWorker.translateText("Description") + ":", plainFont);
                        descNestedCell.addElement(p1);
                        descNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        descNestedCell.setBorder(0);
                        componentsNestedTable.addCell(descNestedCell);

                        descNestedCell = new PdfPCell();
                        p1 = new Paragraph(new Phrase(TranslatorWorker.translateText(comp.getDescription()),
                                plainFont));
                        p1.setAlignment(Element.ALIGN_LEFT);
                        descNestedCell.addElement(p1);
                        descNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        descNestedCell.setBorder(0);
                        componentsNestedTable.addCell(descNestedCell);

                        descNestedCell = new PdfPCell();
                        p1 = new Paragraph(TranslatorWorker.translateText("Component Type") + ":", plainFont);
                        descNestedCell.addElement(p1);
                        descNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        descNestedCell.setBorder(0);
                        componentsNestedTable.addCell(descNestedCell);

                        descNestedCell = new PdfPCell();
                        p1 = new Paragraph(new Phrase(TranslatorWorker.translateText(comp.getTypeName()), plainFont));
                        p1.setAlignment(Element.ALIGN_LEFT);
                        descNestedCell.addElement(p1);
                        descNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        descNestedCell.setBorder(0);
                        componentsNestedTable.addCell(descNestedCell);


                        //third row - finanse of comp.
                        PdfPCell financeCompNestedCell = new PdfPCell();
                        financeCompNestedCell.setBackgroundColor(new Color(244, 244, 242));
                        financeCompNestedCell.setBorder(0);
                        financeCompNestedCell.setColspan(2);
                        p1 = new Paragraph(TranslatorWorker.translateText("Component Funding"), titleFont);
                        p1.setAlignment(Element.ALIGN_LEFT);
                        financeCompNestedCell.addElement(p1);
                        componentsNestedTable.addCell(financeCompNestedCell);
                        //commitments row
                        if (visibleModuleCompCommitments && comp.getCommitments() != null && comp.getCommitments()
                                .size() > 0) { //commitments row
                            PdfPCell financeCell = new PdfPCell();
                            financeCell.setBorder(0);
                            financeCell.setColspan(2);
                            PdfPTable financeTable = buildPdfTable(2);
                            financeTable.setWidths(new float[]{1f, 4f});
                            buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Commitment"), (List)
                                    comp.getCommitments(), componentCommitmentsFMfields, ampContext, session);
                            financeCell.addElement(financeTable);
                            componentsNestedTable.addCell(financeCell);
                        }
                        //disbursments row
                        if (visibleModuleCompDisbursements && comp.getDisbursements() != null && comp
                                .getDisbursements().size() > 0) {
                            PdfPCell financeCell = new PdfPCell();
                            financeCell.setBorder(0);
                            financeCell.setColspan(2);
                            PdfPTable financeTable = buildPdfTable(2);
                            financeTable.setWidths(new float[]{1f, 4f});
                            buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Disbursment"),
                                    (List) comp.getDisbursements(), componentDisbursementsFMfields, ampContext, session);
                            financeCell.addElement(financeTable);
                            componentsNestedTable.addCell(financeCell);
                        }
                        //expenditures row
                        if (visibleModuleCompExpenditures && comp.getExpenditures() != null && comp.getExpenditures()
                                .size() > 0) {
                            PdfPCell financeCell = new PdfPCell();
                            financeCell.setBorder(0);
                            financeCell.setColspan(2);
                            PdfPTable financeTable = buildPdfTable(2);
                            financeTable.setWidths(new float[]{1f, 4f});
                            buildFinanceInfoOutput(financeTable, TranslatorWorker.translateText("Expenditures"),
                                    (List) comp.getExpenditures(), componentExpendituresFMfields, ampContext, session);
                            financeCell.addElement(financeTable);
                            componentsNestedTable.addCell(financeCell);
                        }
                        //empty line
                        PdfPCell emptyCell = new PdfPCell();
                        emptyCell.addElement(new Paragraph("\n"));
                        emptyCell.setBackgroundColor(new Color(255, 255, 255));
                        emptyCell.setBorder(0);
                        emptyCell.setColspan(2);
                        componentsNestedTable.addCell(emptyCell);

                        int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue
                                (GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

                        //amounts in thousands
                        if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS) {
                            PdfPCell amountsInThousandsCell1 = new PdfPCell(new Paragraph(TranslatorWorker
                                    .translateText("The amount entered are in thousands (000)"), plainFont));
                            amountsInThousandsCell1.setBorder(0);
                            amountsInThousandsCell1.setBackgroundColor(new Color(255, 255, 204));
                            amountsInThousandsCell1.setColspan(2);
                            //fundingTable.addCell(amountsInThousandsCell1);
                            componentsNestedTable.addCell(amountsInThousandsCell1);
                        }

                        //amounts in millions
                        if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS) {
                            PdfPCell amountsInMillionsCell1 = new PdfPCell(new Paragraph(
                                    TranslatorWorker.translateText("The amount entered are in millions (000 000)"),
                                    plainFont));
                            amountsInMillionsCell1.setBorder(0);
                            amountsInMillionsCell1.setBackgroundColor(new Color(255, 255, 204));
                            amountsInMillionsCell1.setColspan(2);
                            //fundingTable.addCell(amountsInThousandsCell1);
                            componentsNestedTable.addCell(amountsInMillionsCell1);
                        }
                    } else if (GlobalSettings.getInstance().getShowComponentFundingByYear()
                            && FeaturesUtil.isVisibleModule("Components Resume")) { //true case
                        //comp code
                        PdfPCell compNestedCell = new PdfPCell();
                        p1 = new Paragraph(TranslatorWorker.translateText("Component Code") + ":", titleFont);
                        compNestedCell.addElement(p1);
                        compNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        compNestedCell.setBorder(0);
                        componentsNestedTable.addCell(compNestedCell);

                        compNestedCell = new PdfPCell();
                        p1 = new Paragraph(comp.getCode(), plainFont);
                        compNestedCell.addElement(p1);
                        compNestedCell.setBackgroundColor(new Color(255, 255, 255));
                        compNestedCell.setBorder(0);
                        componentsNestedTable.addCell(compNestedCell);

                        //finance of the comp
                        PdfPCell financeCompNestedCell = new PdfPCell();
                        financeCompNestedCell.setBackgroundColor(new Color(244, 244, 242));
                        financeCompNestedCell.setBorder(0);
                        financeCompNestedCell.setColspan(2);
                        p1 = new Paragraph(TranslatorWorker.translateText("Finance of the component"));
                        p1.setAlignment(Element.ALIGN_LEFT);
                        financeCompNestedCell.addElement(p1);
                        componentsNestedTable.addCell(financeCompNestedCell);
                        //nested
                        for (Integer key : comp.getFinanceByYearInfo().keySet()) {
                            //first cell in nested2 table
                            PdfPCell nestedCell5 = new PdfPCell();
                            p1 = new Paragraph(key.toString(), plainFont);
                            p1.setAlignment(Element.ALIGN_LEFT);
                            nestedCell5.addElement(p1);
                            nestedCell5.setBackgroundColor(new Color(255, 255, 255));
                            nestedCell5.setBorder(0);
                            componentsNestedTable.addCell(nestedCell5);
                            //second cell in nested2 table
                            PdfPTable financeNestedTable = buildPdfTable(2);
                            Map<String, Double> myMap = comp.getFinanceByYearInfo().get(key); //value of the  comp
                            // .getFinanceByYearInfo() Map
                            PdfPCell pcs1 = new PdfPCell();
                            p1 = new Paragraph(TranslatorWorker.translateText("Planned Commitments Sum"), plainFont);
                            p1.setAlignment(Element.ALIGN_LEFT);
                            pcs1.addElement(p1);
                            pcs1.setBackgroundColor(new Color(255, 255, 255));
                            pcs1.setBorder(0);
                            financeNestedTable.addCell(pcs1);
                            PdfPCell pcs2 = new PdfPCell();
                            Double a = myMap.get("MontoProgramado");
                            Double plannedCommSum = new Double(new DecimalFormat("#.##").format(a));
                            p1 = new Paragraph(plannedCommSum.toString(), plainFont);
                            p1.setAlignment(Element.ALIGN_LEFT);
                            pcs2.addElement(p1);
                            pcs2.setBackgroundColor(new Color(255, 255, 255));
                            pcs2.setBorder(0);
                            financeNestedTable.addCell(pcs2);


                            PdfPCell acs1 = new PdfPCell();
                            p1 = new Paragraph(TranslatorWorker.translateText("Actual Commitments Sum"), plainFont);
                            p1.setAlignment(Element.ALIGN_LEFT);
                            acs1.addElement(p1);
                            financeNestedTable.addCell(acs1);
                            PdfPCell acs2 = new PdfPCell();
                            acs2.setBackgroundColor(new Color(255, 255, 255));
                            acs2.setBorder(0);
                            Double actCommSum = new Double(new DecimalFormat("#.##").format(myMap.get
                                    ("MontoReprogramado")));
                            acs2.addElement(new Paragraph(actCommSum.toString(), plainFont));
                            financeNestedTable.addCell(acs2);


                            PdfPCell aes1 = new PdfPCell();
                            p1 = new Paragraph(TranslatorWorker.translateText("Actual Expenditures Sum"), plainFont);
                            p1.setAlignment(Element.ALIGN_LEFT);
                            aes1.addElement(p1);
                            aes1.setBackgroundColor(new Color(255, 255, 255));
                            aes1.setBorder(0);
                            financeNestedTable.addCell(aes1);

                            PdfPCell aes2 = new PdfPCell();
                            aes2.setBackgroundColor(new Color(255, 255, 255));
                            aes2.setBorder(0);
                            Double actExpSum = new Double(new DecimalFormat("#.##").format(myMap.get
                                    ("MontoEjecutado")));
                            p1 = new Paragraph(actExpSum.toString(), plainFont);
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

    private void addFundingRegion(EditActivityForm myForm, PdfPTable fundingTable, Funding funding, String subtotal,
                                  String fundingRegionName,
                                  int transactionType, String adjustmentType, String currencyCode, boolean visibleExchangeRate,
                                  String[] fmTemplate,HttpSession session) throws WorkerException
    {
        Collection<FundingDetail> details = funding.filterFundings(transactionType, adjustmentType);
        if (details.isEmpty())
            return;
        ServletContext ampContext = getServlet().getServletContext();

        String output = TranslatorWorker.translateText(fundingRegionName);
        if(myForm.getFunding().isFixerate() && visibleExchangeRate){
            output+=" \t"+ TranslatorWorker.translateText("Exchange Rate");
        }
        PdfPCell plCommCell1=new PdfPCell(new Paragraph(postprocessText(output),titleFont));
        plCommCell1.setBorder(0);
        plCommCell1.setBackgroundColor(new Color(255,255,204));
        plCommCell1.setColspan(4);
        fundingTable.addCell(plCommCell1);

        for (FundingDetail fd : details)
        {
            buildFundingInfoInnerTable(fundingTable, fd, fmTemplate, fundingTable, ampContext,session);
        }
        createSubtotalRow(fundingTable, "SUBTOTAL " + fundingRegionName + ":", subtotal, currencyCode);
    }

    /**
     * adds a (name, value) cell if value != null. If value == null, adds nothing
     * @param cellName
     * @param cellContents
     */
    private void addNewInfoCell(PdfPTable fundingTable, String cellName, String cellContents) {
        if (cellContents == null || cellContents.isEmpty()) {
            return;
        }

        PdfPCell foIdCell1=new PdfPCell();
        foIdCell1.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        foIdCell1.setBorder(0);
        foIdCell1.setColspan(2);
        Paragraph p1 = new Paragraph(postprocessText(TranslatorWorker.translateText(cellName)) + ":", plainFont);
        foIdCell1.addElement(p1);
        p1.setAlignment(Element.ALIGN_LEFT);
        fundingTable.addCell(foIdCell1);
        //meaning
        PdfPCell foIdCell3=new PdfPCell(new Paragraph(postprocessText(cellContents), plainFont));
        foIdCell3.setBorder(0);
        foIdCell3.setColspan(2);
        foIdCell3.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        fundingTable.addCell(foIdCell3);
    }

    /**
     * convenience method for accessing {@link #addNewInfoCell(PdfPTable, String, String)}. 
     * Translates ACV if not null, else passes null
     * @param fundingTable
     * @param cellName
     * @param cellContents
     */
    private void addNewInfoCell(PdfPTable fundingTable, String cellName, AmpCategoryValue cellContents) {
        String value = cellContents == null ? null : TranslatorWorker.translateText(cellContents.getValue());
        addNewInfoCell(fundingTable, cellName, value);
    }

    private void addTotalsOutput(PdfPTable fundingTable, String title, String value, String currencyCode) 
            throws WorkerException {
        
        if (value == null || value.isEmpty()) {
            return;
        }
        
        String totalAmountType = postprocessText(TranslatorWorker.translateText(title)) + ":";
        addTotalAmountsCellsToFundingTable(fundingTable, totalAmountType, value, currencyCode);
    }

    private void buildFundingInformationPart(EditActivityForm myForm,PdfPTable mainLayout,ServletContext ampContext,HttpSession session) throws WorkerException, DocumentException {
        Paragraph p1;
        PdfPCell fundingCell1 = new PdfPCell();
        p1 = new Paragraph(postprocessText(TranslatorWorker.translateText("Donor Funding")), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        fundingCell1.addElement(p1);
        fundingCell1.setBackgroundColor(new Color(244,244,242));
        fundingCell1.setBorder(0);
        mainLayout.addCell(fundingCell1);

        PdfPTable fundingTable = buildPdfTable(COLUMNS_4);
        fundingTable.setWidths(new float[]{2f, 1.5f, 2f, 2f});
        boolean drawTotals=false; //draw total planned commitment,total actual commitments e.t.c.
        if(myForm.getFunding().getFundingOrganizations()!=null){
            String currencyCode = myForm.getCurrCode() != null ? myForm.getCurrCode() : "";

            boolean visibleModuleCommitments = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Commitments");
            boolean visibleModuleDisbursements = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Disbursements");
            boolean visibleModuleExpenditures = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Expenditures");
            boolean visibleModuleArrears = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Arrears");
            boolean visibleModuleRoF = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Release of Funds");
            boolean visibleModuleEDD = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements");
            boolean visibleModuleDisbOrders = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders");
            boolean visibleModuleMTEFProjections = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections");

            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Total Number of Funding Sources")) {
                PdfPCell foIdCell1 = new PdfPCell();
                foIdCell1.setBackgroundColor(new Color(221,221,221));
                foIdCell1.setBorder(0);
                foIdCell1.setColspan(2);
                p1 = new Paragraph(TranslatorWorker.translateText("Total Number of Funding Sources")+":", plainFont);
                foIdCell1.addElement(p1);
                fundingTable.addCell(foIdCell1);

                Integer fundingSourcesNumber = myForm.getIdentification().getFundingSourcesNumber();
                PdfPCell foIdCell3 = new PdfPCell(new Paragraph(fundingSourcesNumber != null ? fundingSourcesNumber.toString(): ""));
                foIdCell3.setBorder(0);
                foIdCell3.setColspan(2);
                foIdCell3.setBackgroundColor(new Color(221,221,221));
                fundingTable.addCell(foIdCell3);

                //empty cell
                PdfPCell empty = new PdfPCell(new Paragraph("\n\n"));
                empty.setBorder(0);
                empty.setBackgroundColor(new Color(255,255,255));
                empty.setColspan(4);
                fundingTable.addCell(empty);
            }

            for (FundingOrganization fundingOrganisation : myForm.getFunding().getFundingOrganizations()) {
                if(fundingOrganisation.getFundings()!=null){
                    drawTotals=true;
                    for (Funding funding : (Collection<Funding>)fundingOrganisation.getFundings()) {
                        String output="";
                        //general info rows
                        /*if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification", ampContext))*/
                        {
                            //funding org id
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id"))
                            {
                                addNewInfoCell(fundingTable, "Funding Organization Id", funding.getOrgFundingId());
                            }

                            addNewInfoCell(fundingTable, "Funding Organization Name", fundingOrganisation.getOrgName());
                            addNewInfoCell(fundingTable, "Organization Role", TranslatorWorker.translateText(funding.getSourceRole()));
                            //DISCREPANCY: in Word export, ToA and FI are checked via the FM; here, they aren't
                            addNewInfoCell(fundingTable, "Type of Assistance", funding.getTypeOfAssistance());
                            addNewInfoCell(fundingTable, "Financing Instrument", funding.getFinancingInstrument());

                            //Funding status
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status"))
                            {
                                addNewInfoCell(fundingTable, "Funding Status", funding.getFundingStatus());
                            }

                            //Mode of Payment
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Mode of Payment"))
                            {
                                addNewInfoCell(fundingTable, "Mode of Payment", funding.getModeOfPayment());
                            }
                            
                            // Concessionality Level
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Concessionality Level"))
                            {
                                addNewInfoCell(fundingTable, "Concessionality Level", funding.getConcessionalityLevel());
                            }

                            //always display FundingClassification Date, if it has been entered
                            {
                                addNewInfoCell(fundingTable, "Funding Classification Date", funding.getFundingClassificationDate());
                            }
                            if(funding.getEffectiveFundingDate() != null)
                            {
                                addNewInfoCell(fundingTable, "Effective Funding Date", funding.getEffectiveFundingDate());
                            }
                            if(funding.getFundingClosingDate() != null)
                            {
                                addNewInfoCell(fundingTable, "Funding Closing Date", funding.getFundingClosingDate());
                            }
                            
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Loan Details/Ratification Date"))
                            {
                                addNewInfoCell(fundingTable, "Ratification Date", funding.getRatificationDate());
                            }
                            
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Loan Details/Maturity"))
                            {
                                addNewInfoCell(fundingTable, "Maturity", funding.getMaturity());
                            }
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Loan Details/Interest Rate"))
                            {
                                addNewInfoCell(fundingTable, "Interest Rate", funding.getInterestRate() != null ? String.valueOf(funding.getInterestRate()) : "");
                            }
                            if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Loan Details/Grace Period"))
                            {
                                addNewInfoCell(fundingTable, "Grace Period", funding.getGracePeriod() != null ? String.valueOf(funding.getGracePeriod()) : "");
                            }
                        }
                        //Donor objective
                        if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Donor Objective") && (funding.getDonorObjective() != null))
                        {                           
                            addNewInfoCell(fundingTable, "Donor Objective", funding.getDonorObjective());                           
                        }
                        //Funding conditions
                        if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Conditions") && (funding.getConditions() != null)){                          
                            addNewInfoCell(fundingTable, "Conditions", funding.getConditions());                                                       
                        }
                        //Agreement
                        if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement"))
                        {
                            addNewInfoCell(fundingTable, "Agreement Title", funding.getTitle());
                            addNewInfoCell(fundingTable, "Agreement Code", funding.getCode());
                        }

                        if (visibleModuleCommitments)
                        {
                            boolean visibleCommitmentsExchRate = true;
//                          boolean visibleCommitmentsExchRate = FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Exchange Rate", ampContext,session);
//
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedCommitments(), "PLANNED COMMITMENTS", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalActualCommitments(), "ACTUAL COMMITMENTS", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(),  currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineCommitments(), "PIPELINE COMMITMENTS", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);

                            // SSC: only commitments
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalOfficialDevelopmentAidCommitments(), "Official Development Aid SSC", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.getValueKey(), currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalBilateralSscCommitments(), "Bilateral SSC", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.getValueKey(), currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalTriangularSscCommitments(), "Triangular SSC", Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.getValueKey(), currencyCode, visibleCommitmentsExchRate, fundingCommitmentsFMfields,session);
                        }

                        if (visibleModuleDisbursements)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedDisbursements(), "PLANNED DISBURSEMENTS", Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingDisbursementsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalDisbursements(), "ACTUAL DISBURSEMENTS", Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingDisbursementsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineDisbursements(), "PIPELINE DISBURSEMENTS", Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingDisbursementsFMfields,session);
                        }
                        if (visibleModuleArrears)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedArrears(), "PLANNED ARREARS", Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingArrearsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalActualArrears(), "ACTUAL ARREARS", Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingArrearsFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineArrears(), "PIPELINE ARREARS", Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingArrearsFMfields,session);
                        }

                        if (visibleModuleExpenditures)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedExpenditures(), "PLANNED EXPENDITURES", Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingExpendituresFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalExpenditures(), "ACTUAL EXPENDITURES", Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingExpendituresFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineExpenditures(), "PIPELINE EXPENDITURES", Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingExpendituresFMfields,session);
                        }

                        if (visibleModuleRoF)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedRoF(), "PLANNED RELEASE OF FUNDS", Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingRoFFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalActualRoF(), "ACTUAL RELEASE OF FUNDS", Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingRoFFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineRoF(), "PIPELINE RELEASE OF FUNDS", Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingRoFFMfields,session);
                        }

                        if (visibleModuleEDD)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedEDD(), "PLANNED ESTIMATED DISBURSEMENTS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingEDDFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalActualEDD(), "ACTUAL ESTIMATED DISBURSEMENTS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingEDDFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineEDD(), "PIPELINE ESTIMATED DISBURSEMENTS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingEDDFMfields,session);
                        }

                        if (visibleModuleDisbOrders)
                        {
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPlannedDisbursementsOrders(), "PLANNED DISBURSMENT ORDERS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), currencyCode, true, fundingDisbOrdersFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalActualDisbursementsOrders(), "ACTUAL DISBURSMENT ORDERS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), currencyCode, true, fundingDisbOrdersFMfields,session);
                            addFundingRegion(myForm, fundingTable, funding, funding.getSubtotalPipelineDisbursementsOrders(), "PIPELINE DISBURSMENT ORDERS", Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey(), currencyCode, true, fundingDisbOrdersFMfields,session);
                        }

                        // MTEF Projections
                        if (visibleModuleMTEFProjections) {
                            renderMtefSection(fundingTable, funding, currencyCode);
                        }

                        //UNDISBURSED BALANCE
                        if (FeaturesUtil.isVisibleFeature("Funding", "Undisbursed Balance")) {
                            String title = TranslatorWorker.translateText("UNDISBURSED BALANCE:");
                            String amount = funding.getUndisbursementbalance();
                            createSubtotalRow(fundingTable, title, amount, currencyCode);
                        }

                        int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

                        //amounts in thousands
                        if(amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS){
                            PdfPCell amountsInThousandsCell1 = new PdfPCell(new Paragraph(TranslatorWorker.translateText("The amount entered are in thousands (000)"),plainFont));
                            amountsInThousandsCell1.setBorder(0);
                            amountsInThousandsCell1.setBackgroundColor(new Color(255,255,204));
                            amountsInThousandsCell1.setColspan(4);
                            //fundingTable.addCell(amountsInThousandsCell1);
                            fundingTable.addCell(amountsInThousandsCell1);
                        }

                        //amounts in millions
                        if(amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS){
                            PdfPCell amountsInMillionsCell1 = new PdfPCell(new Paragraph(TranslatorWorker.translateText("The amount entered are in millions (000 000)"),plainFont));
                            amountsInMillionsCell1.setBorder(0);
                            amountsInMillionsCell1.setBackgroundColor(new Color(255,255,204));
                            amountsInMillionsCell1.setColspan(4);
                            //fundingTable.addCell(amountsInThousandsCell1);
                            fundingTable.addCell(amountsInMillionsCell1);
                        }
                        //empty cell
                        PdfPCell empty=new PdfPCell(new Paragraph("\n\n"));
                        empty.setBorder(0);
                        empty.setBackgroundColor(new Color(255,255,255));
                        empty.setColspan(4);
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
                empty.setColspan(4);
                fundingTable.addCell(empty);

                if(visibleModuleCommitments)
                {
                    addTotalsOutput(fundingTable, "TOTAL PLANNED COMMITMENTS", myForm.getFunding().getTotalPlannedCommitments(), currencyCode);
                    addTotalsOutput(fundingTable, "TOTAL ACTUAL COMMITMENTS", myForm.getFunding().getTotalCommitments(), currencyCode);
                    addTotalsOutput(fundingTable, "TOTAL PIPELINE COMMITMENTS", myForm.getFunding().getTotalPipelineCommitments(), currencyCode);
                }


                if(visibleModuleDisbursements)
                {
                    addTotalsOutput(fundingTable, "TOTAL PLANNED DISBURSEMENT", myForm.getFunding().getTotalPlannedDisbursements(), currencyCode);
                    addTotalsOutput(fundingTable, "TOTAL ACTUAL DISBURSEMENT", myForm.getFunding().getTotalDisbursements(), currencyCode);
                }

                //TOTAL MTEF PROJECTIONS
                if (myForm.getFunding().getMtefDetails() != null)
                {
                    addTotalsOutput(fundingTable, "TOTAL MTEF PROJECTIONS", myForm.getFunding().getTotalMtefProjections(), currencyCode);
                }

                //TOTAL PLANNED EXPENDITURES
                if(visibleModuleExpenditures)
                {
                    addTotalsOutput(fundingTable, "TOTAL PLANNED EXPENDITURES", myForm.getFunding().getTotalPlannedExpenditures(), currencyCode);
                    addTotalsOutput(fundingTable, "TOTAL ACTUAL EXPENDITURES", myForm.getFunding().getTotalExpenditures(), currencyCode);
                }

                //TOTAL ARREARS
                if(visibleModuleExpenditures)
                {
                    addTotalsOutput(fundingTable, "TOTAL PLANNED ARREARS", myForm.getFunding().getTotalPlannedArrears(), currencyCode);
                    addTotalsOutput(fundingTable, "TOTAL ACTUAL ARREARS", myForm.getFunding().getTotalArrears(), currencyCode);
                }

                
                //TOTAL ACTUAL DISBURSEMENT ORDERS:
                if (visibleModuleDisbOrders) {
                    addTotalsOutput(fundingTable, "TOTAL ACTUAL DISBURSEMENT ORDERS", myForm.getFunding().getTotalActualDisbursementsOrders(), currencyCode);
                }

                //UNDISBURSED BALANCE
                if (FeaturesUtil.isVisibleFeature("Funding", "Undisbursed Balance")) {
                    addTotalsOutput(fundingTable, "UNDISBURSED BALANCE", 
                            myForm.getFunding().getUnDisbursementsBalance(), currencyCode);
                }

                if (myForm.getFunding().getDeliveryRate() != null) {
                    addTotalsOutput(fundingTable, "Delivery rate",
                        myForm.getFunding().getDeliveryRate().replace("%", ""), "%");
                }

            }
        }
        PdfPCell fundingCell=new PdfPCell(fundingTable);
        fundingCell.setBorder(0);
        mainLayout.addCell(fundingCell);
        //return fundingTable;
    }
    
    private void renderMtefSection(PdfPTable fundingTable, Funding funding, String currencyCode) 
            throws WorkerException {
        String output = TranslatorWorker.translateText("MTEF Projections") + ":";
        PdfPCell titleCell = new PdfPCell(new Paragraph(postprocessText(output), titleFont));
        titleCell.setBorder(0);
        titleCell.setBackgroundColor(new Color(255,255,204));
        titleCell.setColspan(4);

        ArrayList<PdfPCell> cells = new ArrayList<>();
        boolean anythingAdded = false;

        if (funding.getMtefDetails() != null) {
            List<FundingDetail> mtefPipeline = new ArrayList<FundingDetail>();
            List<FundingDetail> mtefProjection = new ArrayList<FundingDetail>();
            
            for (FundingDetail mtefFunding : funding.getMtefDetails()) {
                if ("pipeline".equals(mtefFunding.getProjectionTypeName().getValue())) {
                    mtefPipeline.add(mtefFunding);
                } else if ("projection".equals(mtefFunding.getProjectionTypeName().getValue())) {
                    mtefProjection.add(mtefFunding);
                }
            }
            
            anythingAdded |= renderMtefSubsection(cells, mtefPipeline, "MTEF Projections Pipeline", funding.getSubtotalMTEFsPipeline(), currencyCode);
            anythingAdded |= renderMtefSubsection(cells, mtefProjection, "MTEF Projections Projection", funding.getSubtotalMTEFsProjection(), currencyCode);
        }

        fundingTable.addCell(titleCell);
        
        if (anythingAdded) {
            renderMtefSubTotals(cells, "Total MTEF Projections", funding.getSubtotalMTEFs(), currencyCode);
            for (PdfPCell cell : cells) {
                fundingTable.addCell(cell);
            }
        } else {
            PdfPCell innerCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("No MTEF data"), plainFont));
            innerCell.setColspan(COLUMNS_4);
            innerCell.setBorder(0);
            fundingTable.addCell(innerCell);
        }
    }

    private boolean renderMtefSubsection(ArrayList<PdfPCell> cells, List<FundingDetail> mtefList, 
            String projectionType, String mtefSubTotal, String currencyCode) throws WorkerException {
        
        boolean addedData = false;
        for (FundingDetail mtefFunding : mtefList) {
            if (FeaturesUtil.isVisibleModule(mtefProjectionFields[0])) {
                addedData = true;
                String projectedType = mtefFunding.getProjectionTypeName().getValue();
                PdfPCell innerCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText(projectedType), plainFont));
                innerCell.setBorder(0);
                cells.add(innerCell);
            } else {
                cells.add(getEmptyCell());
            }

            if (FeaturesUtil.isVisibleModule(mtefProjectionFields[1])){
                addedData = true;
                PdfPCell innerCell = new PdfPCell(new Paragraph(mtefFunding.getFiscalYear(), plainFont));
                innerCell.setBorder(0);
                cells.add(innerCell);
            } else {
                cells.add(getEmptyCell());
            }

            if (FeaturesUtil.isVisibleModule(mtefProjectionFields[2])) {
                addedData = true;
                PdfPTable mtefTable = createAmountCurrencyTable(mtefFunding.getTransactionAmount(), 
                        mtefFunding.getCurrencyCode());

                PdfPCell innerCell = new PdfPCell(mtefTable);
                innerCell.setBorder(0);
                innerCell.setColspan(2);
                cells.add(innerCell);
            } else {
                cells.add(getEmptyCell(2));
            }

            PdfPCell roleCell = getRoleOrgForFundingFlows(mtefFunding, 
                    ActivityUtil.getFmForFundingFlows(mtefFunding.getTransactionType()));
            roleCell.setColspan(COLUMNS_4);
            cells.add(roleCell);
        }
        
        if (addedData) {
            renderMtefSubTotals(cells, "Sub-Total " + projectionType, mtefSubTotal, currencyCode);
        }
        
        return addedData;
    }
    
    private void renderMtefSubTotals(ArrayList<PdfPCell> cells, String titleTotal, String subtotalMTEFs, 
            String currencyCode) throws WorkerException {
        
        PdfPCell subTotalTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText(titleTotal), plainFont));
        subTotalTitle.setBackgroundColor(MTEF_BACKGROUND_COLOR);
        subTotalTitle.setColspan(2);
        subTotalTitle.setBorder(0);

        PdfPTable subTotalAmountTable = createAmountCurrencyTable(subtotalMTEFs, currencyCode);
        PdfPCell subTotalAmount = new PdfPCell(subTotalAmountTable);
        subTotalAmount.setBackgroundColor(MTEF_BACKGROUND_COLOR);
        subTotalAmount.setColspan(2);
        subTotalAmount.setBorder(0);
        
        cells.add(subTotalTitle);
        cells.add(subTotalAmount);
    }

    private void createSubtotalRow(PdfPTable fundingTable, String title, String amount, String currencyCode) 
            throws WorkerException {

        PdfPCell subTotal = new PdfPCell(new Paragraph(TranslatorWorker.translateText(title), plainFont));
        subTotal.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        subTotal.setColspan(2);
        subTotal.setBorderWidthBottom(0);
        subTotal.setBorderWidthTop(SUBTOTAL_BORDER_TOP_WIDTH);
        subTotal.setBorderWidthLeft(0);
        subTotal.setBorderWidthRight(0);
        fundingTable.addCell(subTotal);

        PdfPTable amountValueTable = createAmountCurrencyTable(amount, currencyCode);
        subTotal = new PdfPCell(amountValueTable);
        subTotal.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        subTotal.setColspan(2);
        subTotal.setBorderWidthBottom(0);
        subTotal.setBorderWidthTop(SUBTOTAL_BORDER_TOP_WIDTH);
        subTotal.setBorderWidthLeft(0);
        subTotal.setBorderWidthRight(0);
        fundingTable.addCell(subTotal);
    }

    /**
     * @param amount
     * @param currencyCode
     * @return
     * @throws WorkerException
     */
    private PdfPTable createAmountCurrencyTable(String amount, String currencyCode) throws WorkerException {
        PdfPTable valueTable = new PdfPTable(new float[] {AMOUNT_COLUMN_WIDTH, CURRENCY_COLUMN_WIDTH});
        if (SiteUtils.isEffectiveLangRTL()) {
            try {
                valueTable.setWidths(new float[] {CURRENCY_COLUMN_WIDTH, AMOUNT_COLUMN_WIDTH});
            } catch (DocumentException e) {
                throw new WorkerException(e);
            }
        }
        
        if (amount != null && !amount.isEmpty()) {
            if (SiteUtils.isEffectiveLangRTL()) {
                valueTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
           
            PdfPCell amountCell = new PdfPCell(new Paragraph(amount, plainFont));
            amountCell.setBorder(0);
            if (SiteUtils.isEffectiveLangRTL()) {
                amountCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                amountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            } else {
                amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            
            PdfPCell currencyCell = new PdfPCell(new Paragraph(currencyCode, plainFont));
            currencyCell.setBorder(0);
            valueTable.addCell(amountCell);
            valueTable.addCell(currencyCell);
        }
        
        return valueTable;
    }
    
    /**
     * @param amount
     * @param currencyCode
     * @return
     * @throws WorkerException
     */
    private PdfPTable createAmountCurrencyExchangeTable(String amount, String currencyCode, String exchangeRate, 
            boolean isVisibleExchangeRate) throws WorkerException {
        PdfPTable valueTable = new PdfPTable(new float[] {AMOUNT_COLUMN_WIDTH, CURRENCY_COLUMN_WIDTH});
        if (SiteUtils.isEffectiveLangRTL()) {
            try {
                valueTable.setWidths(new float[] {CURRENCY_COLUMN_WIDTH, AMOUNT_COLUMN_WIDTH});
            } catch (DocumentException e) {
                throw new WorkerException(e);
            }
        }
        
        if (amount != null && !amount.isEmpty()) {
            if (SiteUtils.isEffectiveLangRTL()) {
                valueTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
           
            PdfPCell amountCell = new PdfPCell(new Paragraph(amount, plainFont));
            amountCell.setBorder(0);
            if (SiteUtils.isEffectiveLangRTL()) {
                amountCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                amountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            } else {
                amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            
            PdfPCell currencyCell = new PdfPCell(new Paragraph(currencyCode, plainFont));
            currencyCell.setBorder(0);
            valueTable.addCell(amountCell);
            valueTable.addCell(currencyCell);
        }
        
        if (exchangeRate != null && isVisibleExchangeRate) {
            String exchange = TranslatorWorker.translateText("Exchange Rate:") + exchangeRate;
            PdfPCell exchangeRateCell = new PdfPCell(new Phrase(exchange));
            exchangeRateCell.setColspan(2);
            exchangeRateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueTable.addCell(exchangeRateCell);
        }
        
        return valueTable;
    }

    private void addTotalAmountsCellsToFundingTable(PdfPTable fundingTable, String totalAmountType, 
            String amount, String currencyCode) throws WorkerException {
        
        Paragraph p1;
        PdfPCell totalPC = new PdfPCell(new Paragraph(postprocessText(totalAmountType), plainFont));
        totalPC.setColspan(2);
        totalPC.setBorder(0);
        totalPC.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        fundingTable.addCell(totalPC);
        
        PdfPTable amountValueTable = createAmountCurrencyTable(amount, currencyCode);
        PdfPCell totalPCAmount = new PdfPCell(amountValueTable);
        totalPCAmount.setBorder(0);
        totalPCAmount.setColspan(2);
        totalPCAmount.setBackgroundColor(SUBTOTAL_BACKGROUND_COLOR);
        
        fundingTable.addCell(totalPCAmount);
        //empty cell
        buildEmptyCell(fundingTable);
    }

    private void buildEmptyCell(PdfPTable fundingTable) {
        PdfPCell lineCell;
        lineCell=new PdfPCell(new Paragraph(0));
        lineCell.setBorder(0);
        lineCell.setColspan(4);
        fundingTable.addCell(lineCell);
    }

    private void createGeneralInfoRowAid(PdfPTable mainLayout, String columnName, List<String[]> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        PdfPCell cell1 = new PdfPCell();
        Paragraph p2;
        Paragraph p1 = new Paragraph(postprocessText(columnName), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setBackgroundColor(new Color(244,244,242));
        cell1.setBorder(0);
        mainLayout.addCell(cell1);

        PdfPTable effectivenessTable = buildPdfTable(2);
        effectivenessTable.getDefaultCell().setBorder(0);

        for (String[] value : values) {
            PdfPCell c1 = new PdfPCell(new Paragraph(postprocessText(value[0]), titleFont));
            c1.setBorder(0);
            PdfPCell c2 = new PdfPCell(new Paragraph(postprocessText(value[1]), plainFont));
            c2.setBorder(0);

            effectivenessTable.addCell(c1);
            effectivenessTable.addCell(c2);
        }

        mainLayout.addCell(effectivenessTable);
    }
    
    /**
     * Used to create simple two columned row
     * @param mainLayout
     * @param columnName
     * @param value
     */
    private void createGeneralInfoRow(PdfPTable mainLayout, String columnName, String value) {
        createGeneralInfoRow(mainLayout, columnName, "", value, false);
    }
    
    private void createGeneralInfoRow(PdfPTable mainLayout, String columnName, List<PdfPTable> valuesTable) {
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1 = new Paragraph(postprocessText(columnName), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setBackgroundColor(BACKGROUND_COLOR);
        cell1.setBorder(0);
        cell1.setVerticalAlignment(Element.ALIGN_TOP);
        mainLayout.addCell(cell1);
        
        PdfPTable cell2Table = new PdfPTable(1);
        for (PdfPTable table : valuesTable) {
            PdfPCell cell = new PdfPCell(table);
            cell.setBorder(0);
            cell2Table.addCell(cell);
        }
        
        PdfPCell cell2 = new PdfPCell(cell2Table);
        cell2.setBorder(0);
        mainLayout.addCell(cell2);
    }
    
    /**
     * Used to create simple two columned row
     * @param mainLayout
     * @param columnName
     * @param label
     * @param value
     * @param isLtr
     */
    private void createGeneralInfoRow(PdfPTable mainLayout, String columnName, String label, String value, 
            boolean isLtr) {
        
        if (value == null || value.isEmpty()) {
            return;
        }
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1 = new Paragraph(postprocessText(columnName), titleFont);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setBackgroundColor(BACKGROUND_COLOR);
        cell1.setBorder(0);
        mainLayout.addCell(cell1);
        
        PdfPCell cell2 = new PdfPCell(createGeneralInfoTable(label, value, isLtr));
        cell2.setBorder(0);
        mainLayout.addCell(cell2);
    }
    
    /**
     * @param value
     * @return
     */
    private PdfPTable createGeneralInfoTable(String label, String value, boolean isLtr) {
        PdfPTable valueTable = new PdfPTable(2);
        
        if (value != null && !value.isEmpty()) {
            if (SiteUtils.isEffectiveLangRTL()) {
                valueTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
           
            PdfPCell labelCell = new PdfPCell(new Paragraph(label, plainFont));
            labelCell.setBorder(0);

            PdfPCell valueCell = new PdfPCell(new Paragraph(value, plainFont));
            valueCell.setBorder(0);
            
            valueTable.addCell(labelCell);
            valueTable.addCell(valueCell);
        }
        
        return valueTable;
    }

    private void buildAnnualProjectBudgetTable(EditActivityForm myForm, HttpServletRequest request, PdfPTable mainLayout) {
        if (myForm.getFunding().getProposedAnnualBudgets() != null
                && myForm.getFunding().getProposedAnnualBudgets().size() > 0) {
            PdfPCell innerCell = new PdfPCell();
            innerCell.setBorder(0);
            Paragraph p1 = new Paragraph(TranslatorWorker.translateText("Annual Proposed Project Budget"), titleFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
            innerCell.addElement(p1);
            innerCell.setBackgroundColor(BACKGROUND_COLOR);
            mainLayout.addCell(innerCell);
            PdfPCell dataCell = new PdfPCell();
            PdfPTable dataTable = buildPdfTable(2);
            innerCell = new PdfPCell();
            innerCell.setBorder(0);
            innerCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Costs"), titleFont));
            dataTable.addCell(innerCell);
            innerCell = new PdfPCell();
            innerCell.setBorder(0);
            innerCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Year"), titleFont));
            dataTable.addCell(innerCell);
            List <ProposedProjCost> proposedProjectCostList = myForm.getFunding().getProposedAnnualBudgets();
            Iterator<ProposedProjCost> it = proposedProjectCostList.iterator();
            while (it.hasNext()) {
                ProposedProjCost ppc = it.next();
                innerCell = new PdfPCell();
                innerCell.setBorder(0);
                innerCell = new PdfPCell(new Paragraph(ppc.getFunAmount() + " " + ppc.getCurrencyCode(), plainFont));
                dataTable.addCell(innerCell);
                innerCell = new PdfPCell();
                innerCell.setBorder(0);
                innerCell = new PdfPCell(new Paragraph(ppc.getFunDate(), plainFont));
                dataTable.addCell(innerCell);
            }
            dataCell.addElement(dataTable);
            mainLayout.addCell(dataCell);
        }
    }

    private void buildFundingInfoInnerTable(
            PdfPTable infoTable, FundingDetail fd,
            String[] fmFields, PdfPTable fundingTable, ServletContext ampContext,HttpSession session) throws WorkerException {

        PdfPCell innerCell = new PdfPCell();
        String disasterResponse=null;
        if (Boolean.TRUE.equals(fd.getDisasterResponse())) {
            if (FeaturesUtil.isVisibleModule(fmFields[1])) {
                disasterResponse = TranslatorWorker.translateText("Disaster Response");
            }
        }
        if (FeaturesUtil.isVisibleModule(fmFields[0])) {
            innerCell.setBorder(0);
            innerCell = new PdfPCell();
            
            Paragraph p = new Paragraph();
            p.add(new Phrase(TranslatorWorker.translateText(fd.getAdjustmentTypeName().getValue()), plainFont));
            if (disasterResponse != null) {
                p.add(new Phrase(" | " + disasterResponse, smallerFont));
            }
            p.setAlignment(Element.ALIGN_LEFT);
            innerCell.addElement(p);
            innerCell.setBorder(0);
            infoTable.addCell(innerCell);
        } else {
            addEmptyCell(infoTable);
        }

        if (FeaturesUtil.isVisibleModule(fmFields[2])){
            innerCell = new PdfPCell(new Paragraph(fd.getTransactionDate(), plainFont));
            innerCell.setBorder(0);
            infoTable.addCell(innerCell);
        } else {
            addEmptyCell(infoTable);
        }

        if (FeaturesUtil.isVisibleModule(fmFields[ARRAY_IDX_3])) {
            boolean isVisibleExchange = FeaturesUtil.isVisibleModule(fmFields[fmFields.length - 1]);
            PdfPTable amountExchangeTable = createAmountCurrencyExchangeTable(fd.getTransactionAmount(), 
                    fd.getCurrencyCode(), fd.getFormattedRate(), isVisibleExchange);

            innerCell = new PdfPCell(amountExchangeTable);
            innerCell.setColspan(2);
            innerCell.setBorder(0);
            infoTable.addCell(innerCell);
        } else {
            addEmptyCell(infoTable, 2);
        }
        
        PdfPCell roleCell = getRoleOrgForFundingFlows(fd, ActivityUtil.getFmForFundingFlows(fd.getTransactionType()));
        roleCell.setColspan(COLUMNS_4);
        infoTable.addCell(roleCell);
        
        if (fd.getAttachedPledgeName() != null) {

            PdfPCell plCommCell1=new PdfPCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Source Pledge") + ": " + fd.getAttachedPledgeName()), titleFont));
            plCommCell1.setBorder(0);
            plCommCell1.setBackgroundColor(new Color(255,255,204));
            plCommCell1.setColspan(4);
            fundingTable.addCell(plCommCell1);
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Expenditure Class") && 
                fd.getExpenditureClass() != null) {
            PdfPCell cell = new PdfPCell(new Paragraph(postprocessText(TranslatorWorker.translateText("Expenditure Class") + ": " + fd.getExpenditureClass()), plainFont));
            cell.setBorder(0);
            cell.setBackgroundColor(new Color(255,255,204));
            cell.setColspan(4);
            fundingTable.addCell(cell);
        }        
    }

    private PdfPCell getRoleOrgForFundingFlows(FundingDetail fd,String fm) {
        if (fd.getRecipientOrganisation() != null && fd.getRecipientOrganisationRole() != null
                && FeaturesUtil.isVisibleModule( fm)) {
            PdfPCell innerCell;
            String output = TranslatorWorker.translateText("Recipient:") + " ";
            output += fd.getRecipientOrganisation().getName() + "\n" + TranslatorWorker.translateText("as the") + " "
                    + fd.getRecipientOrganisationRole().getName();
            innerCell = new PdfPCell(new Paragraph(output, plainFont));
            innerCell.setBorder(0);
            return innerCell;
        } else {
            return getEmptyCell();
        }

    }

    private PdfPCell getEmptyCell() {
        PdfPCell innerCell = new PdfPCell();
        innerCell.setBorder(0);
        innerCell.setBorder(0);
        
        return innerCell;
    }
    
    private PdfPCell getEmptyCell(int colspan) {
        PdfPCell innerCell = getEmptyCell();
        innerCell.setColspan(colspan);
        
        return innerCell;
    }

    private void addEmptyCell(PdfPTable infoTable) {
        infoTable.addCell(getEmptyCell());
    }
    
    private void addEmptyCell(PdfPTable infoTable, int colspan) {
        infoTable.addCell(getEmptyCell(colspan));
    }

    private String  getEditTagValue(HttpServletRequest request,String editKey) throws Exception{
        Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, RequestUtils.getNavigationLanguage(request).getCode());
        if (editorBody == null) {
            editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, SiteUtils.getDefaultLanguages(site).getCode());
        }
        return editorBody;
    }

    /**
     * builds donor,MOFED,Sec.Ministry and Proj.Coord. Contacts info output
     */
    private void buildContactInfoOutput(PdfPTable mainLayout, String contactType, Collection<AmpActivityContact> contacts, ServletContext ampContext) throws WorkerException {

        if (!hasContents(contacts))
            return;

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setBackgroundColor(new Color(244, 244, 242));
        Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(contactType), titleFont);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(paragraph);
        mainLayout.addCell(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new Color(255, 255, 255));
        if (contacts != null && contacts.size() > 0) {
            String output = "";
            for (AmpActivityContact cont : contacts) {
                output += ExportUtil.getContactInformation(cont.getContact());
            }
            paragraph = new Paragraph(output, plainFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            cell2.addElement(paragraph);
        }
        mainLayout.addCell(cell2);
    }

    /**
     * builds GPI survey
     */
    private void buildGpiSurveyOutput(PdfPTable gpiTable, Set<AmpGPISurvey> surveys) throws WorkerException {
        if ((surveys != null && !surveys.isEmpty())) {
            PdfPCell surveyCell = new PdfPCell();
            surveyCell.setBorder(1);
            surveyCell.setBorderColor(new Color(201, 201, 199));
            com.lowagie.text.List surveyList = new com.lowagie.text.List(false); //not numbered list
            surveyList.setListSymbol(new Chunk("\u2022"));

            for (AmpGPISurvey survey : surveys) {
                List<AmpGPISurveyResponse> list = new ArrayList<>(survey.getResponses());
                Collections.sort(list, new AmpGPISurveyResponse.AmpGPISurveyResponseComparator());
                String indicatorName = "";
                for (AmpGPISurveyResponse response : list) {
                    if (!indicatorName.equals(response.getAmpQuestionId().getAmpIndicatorId().getName())) {
                        indicatorName = response.getAmpQuestionId().getAmpIndicatorId().getName();
                        Paragraph paragraph = new Paragraph(new Paragraph(new Phrase(postprocessText(TranslatorWorker.translateText(indicatorName)), titleFont)));
                        PdfPCell indicatorNameCell = new PdfPCell(paragraph);
                        indicatorNameCell.setBorder(0);
                        indicatorNameCell.setBackgroundColor(new Color(255, 255, 255));
                        gpiTable.addCell(indicatorNameCell);
                    }
                    String responseText = (response.getResponse() != null ? response.getResponse() : "");
                    if (ExportUtil.GPI_TYPE_YES_NO.equalsIgnoreCase(response.getAmpQuestionId().getAmpTypeId()
                            .getName())) {
                        responseText = TranslatorWorker.translateText(responseText);
                    }
                    Paragraph paragraph = new Paragraph(new Paragraph(new Phrase(postprocessText(
                            TranslatorWorker.translateText(response.getAmpQuestionId().getQuestionText()))
                            + "  " + responseText, plainFont)));
                    PdfPCell questionCell = new PdfPCell(paragraph);
                    questionCell.setBorder(0);
                    questionCell.setBackgroundColor(new Color(255, 255, 255));
                    gpiTable.addCell(questionCell);
                }
            }


            PdfPCell emptyCell = new PdfPCell(new Paragraph(" "));
            emptyCell.setBorder(0);
            gpiTable.addCell(emptyCell);
        }
    }


    /**
     * builds all related organizations Info that should be exported to PDF
     */
    private void buildRelatedOrganisationsOutput(PdfPTable relatedOrgsTable, String orgType,
                                                 Collection<AmpOrganisation> orgs,
                                                 Collection<FundingOrganization> fundingOrgs,
                                                 HashMap<String, String> percentages, ServletContext ampContext)
            throws WorkerException {
        if ((orgs != null && !orgs.isEmpty()) || (fundingOrgs != null && !fundingOrgs.isEmpty())) {
            Paragraph paragraph = new Paragraph(new Paragraph(new Phrase(postprocessText(TranslatorWorker
                    .translateText(orgType)) + ":", titleFont)));
            PdfPCell orgTypeCell=new PdfPCell(paragraph);
            orgTypeCell.setBorder(0);
            orgTypeCell.setBackgroundColor(BACKGROUND_COLOR_WHITE);
            relatedOrgsTable.addCell(orgTypeCell);
            PdfPCell respOrgCell=new PdfPCell();

            respOrgCell.setBorder(1);
            respOrgCell.setBorderColor(BORDER_COLOR);
            com.lowagie.text.List orgList=new com.lowagie.text.List(false); //not numbered list
            orgList.setListSymbol(new Chunk("\u2022"));
            orgList.setAlignindent(true);
            orgList.setIndentationLeft(INDENTATION_LEFT);
            if (fundingOrgs!=null && fundingOrgs.size()>0){
                for (FundingOrganization org : fundingOrgs) {
                    ListItem item=new ListItem(postprocessText(org.getOrgName()), plainFont);
                    orgList.add(item);
                }
                respOrgCell.addElement(orgList);
                relatedOrgsTable.addCell(respOrgCell);
            } else if(orgs!=null && orgs.size()>0){
                for (AmpOrganisation org : orgs) {
                    String percentage = percentages.get(org.getAmpOrgId().toString());
                    if ( percentage != null){
                        percentage += "%";
                    }else{
                        percentage = "";
                    }
                    ListItem item=new ListItem(postprocessText(org.getName()) + "  " + percentage, plainFont);
                    orgList.add(item);
                }
                respOrgCell.addElement(orgList);
                relatedOrgsTable.addCell(respOrgCell);
            }
            PdfPCell emptyCell=new PdfPCell(new Paragraph(" "));
            emptyCell.setBorder(0);
            relatedOrgsTable.addCell(emptyCell);
        }
    }
    
    protected PdfPCell buildPdfCell(String text, Font font, int colSpan)
    {
        PdfPCell result = new PdfPCell();
        result.setColspan(colSpan);
        Paragraph paragraph;

        if (font == null)
            font = plainFont;

        paragraph = new Paragraph(text, font);

        result.addElement(paragraph);
        result.setBorder(0);
        return result;
    }

    /**
     * builds commitments, expenditures, disbursement data output
     */
    private PdfPTable buildFinanceInfoOutput(PdfPTable nestedTable, String elemntName, List<FundingDetail>
            listToIterate, String[] fmFields, ServletContext ampContext, HttpSession session) throws WorkerException,
            DocumentException {

        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        Paragraph paragraph = new Paragraph(postprocessText(elemntName), plainFont);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(paragraph);
        cell.setBackgroundColor(new Color(255, 255, 255));
        cell.setBorder(0);
        nestedTable.addCell(cell);

        int visibleFmFieldsAmount = 0;
        for (int i = 0; i < fmFields.length - 3; i++) {
            if (FeaturesUtil.isVisibleModule(fmFields[i])) {
                visibleFmFieldsAmount++;
            }
        }

        if (visibleFmFieldsAmount > 0) {
            PdfPTable fdTable = buildPdfTable(visibleFmFieldsAmount);
            for (FundingDetail fd : listToIterate) {
                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_TYPE])) {
                    fdTable.addCell(buildPdfCell(postprocessText(TranslatorWorker.translateText(fd
                            .getAdjustmentTypeName().getValue())), plainFont, 1));
                }
                String output = "";
                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_AMOUNT])) {
                    output += fd.getTransactionAmount();
                }
                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_CURRENCY])) {
                    output += " " + fd.getCurrencyCode();
                }
                fdTable.addCell(buildPdfCell(postprocessText(output), plainFont, 1));

                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_TRANSCTION_DATE])) {
                    fdTable.addCell(buildPdfCell(fd.getTransactionDate(), plainFont, 1));
                }

                fdTable.addCell(buildPdfCell(fd.getFormattedRate() != null ? fd.getFormattedRate() : " ", plainFont,
                        1));

                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_ORGANISATION]) && (fd
                        .getComponentOrganisation() != null)) {
                    fdTable.completeRow();
                    fdTable.addCell(buildPdfCell("", null, 1));
                    //fdTable.addCell(buildPdfCell(TranslatorWorker.translateText("Organization"), titleFont, 1));
                    String orgNameTxt = fd.getComponentOrganisation() == null ? "" : fd.getComponentOrganisation()
                            .getName();
                    fdTable.addCell(buildPdfCell(TranslatorWorker.translateText("Organization") + ":" + orgNameTxt,
                            null,
                            fdTable.getNumberOfColumns() - 1));
                    fdTable.completeRow();
                }

                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_SECOND_REPORTING]) && (fd
                        .getComponentSecondResponsibleOrganization() != null)) {
                    fdTable.completeRow();
                    fdTable.addCell(buildPdfCell("", null, 1));
                    String orgNameTxt = fd.getComponentSecondResponsibleOrganization() == null ? "" : fd
                            .getComponentSecondResponsibleOrganization().getName();
                    fdTable.addCell(buildPdfCell(
                            TranslatorWorker.translateText("Component Second Responsible Organization")
                                    + ":" + orgNameTxt, null, fdTable.getNumberOfColumns() - 1));
                    fdTable.completeRow();
                }

                if (FeaturesUtil.isVisibleModule(fmFields[ExportUtil.COMPONENT_FM_FIELD_DESCRIPTION]) && (fd
                        .getComponentTransactionDescription() != null)
                        && (!fd.getComponentTransactionDescription().isEmpty())) {
                    fdTable.completeRow();
                    fdTable.addCell(buildPdfCell("", null, 1));
                    //fdTable.addCell(buildPdfCell(TranslatorWorker.translateText("Transaction Description"),
                    //        titleFont, 1));
                    fdTable.addCell(buildPdfCell(TranslatorWorker.translateText("Transaction Description") + ":" + fd
                            .getComponentTransactionDescription(), null, fdTable
                            .getNumberOfColumns() - 1));
                    fdTable.completeRow();
                }

            }
            nestedTable.addCell(fdTable);
        }
        return nestedTable;
    }

    private PdfPTable buildRegionalFundingInfoOutput(String elementName, String regionLocation, List<FundingDetail> listToIterate,ServletContext ampContext) throws Exception{
        PdfPTable regFundTable = buildPdfTable(2);
        regFundTable.getDefaultCell().setBorder(0);
        regFundTable.setWidths(new float[] {1f,3f});

        //commitment,disbursement,expenditure
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        Paragraph paragraph=new Paragraph(postprocessText(elementName),plainFont);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(paragraph);
        cell.setBorder(0);
        regFundTable.addCell(cell);

        //second cell for actual/planned
        PdfPTable fdTable = buildPdfTable(COLUMNS_3);
        fdTable.getDefaultCell().setBorder(0);
        for (FundingDetail fd : listToIterate) {

            cell=new PdfPCell();
            paragraph=new Paragraph(postprocessText(TranslatorWorker.translateText(fd.getAdjustmentTypeName().getValue())),plainFont);
            cell.addElement(paragraph);
            cell.setBorder(0);
            fdTable.addCell(cell);

            cell=new PdfPCell();
            paragraph=new Paragraph(fd.getTransactionDate(),plainFont);
            cell.addElement(paragraph);
            cell.setBorder(0);
            fdTable.addCell(cell);

            cell=new PdfPCell();
            paragraph=new Paragraph(fd.getTransactionAmount()+" "+fd.getCurrencyCode(),plainFont);
            cell.addElement(paragraph);
            cell.setBorder(0);
            fdTable.addCell(cell);
        }
        regFundTable.addCell(fdTable);
        return regFundTable;
    }

    private String buildProgramsOutput(List<AmpActivityProgram> programs) {
        String result = "";
        for (AmpActivityProgram pr : programs) {
            result += pr.getHierarchyNames() + " " + pr.getProgramPercentage() + "%\n";
        }
        return result;
    }

    private boolean hasContents(Collection<?> obj){
        return (obj != null) && (!obj.isEmpty());
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
