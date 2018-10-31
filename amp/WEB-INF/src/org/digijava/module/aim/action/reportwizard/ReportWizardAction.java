/**
 *
 */
package org.digijava.module.aim.action.reportwizard;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.collect.ImmutableMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.utils.MultiAction;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.util.MaxSizeLinkedHashMap;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.ReportsFilterPicker;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Identificator;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpColumnsVisibility;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.reportwizard.DuplicateReportNameException;
import org.digijava.module.aim.exception.reportwizard.NoReportNameSuppliedException;
import org.digijava.module.aim.form.reportwizard.ReportWizardForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;

import com.google.common.collect.HashBiMap;

/**
 * @author Alex Gartner
 *
 */
public class ReportWizardAction extends MultiAction {

    public static final String MULTILINGUAL_REPORT_PREFIX = "multilingual_report";

    private static final Map<String, Integer> ME_COLUMNS_ORDER = new HashMap<>();
    static {
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_NAME, 1);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_CODE, 2);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_SECTOR, 3);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_DESCRIPTION, 4);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_TYPE, 5);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_CREATION_DATE, 6);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_LOGFRAME_CATEGORY, 7);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_BASE_VALUE, 8);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_BASE_DATE, 9);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_BASE_COMMENT, 10);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_TARGET_VALUE, 11);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_TARGET_DATE, 12);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_TARGET_COMMENT, 13);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_REVISED_TARGET_VALUE, 14);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_REVISED_TARGET_DATE, 15);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_REVISED_TARGET_COMMENT, 16);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_ACTUAL_VALUE, 17);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_ACTUAL_DATE, 18);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_ACTUAL_COMMENT, 19);
        ME_COLUMNS_ORDER.put(ColumnConstants.INDICATOR_RISK, 20);
    }

    private static final Comparator<AmpColumns> ME_COLS_COMPARATOR =
            Comparator.comparing(c -> ME_COLUMNS_ORDER.getOrDefault(c.getColumnName(), 999));

    /**
     * This structure maps columns from different report schemas to FM entries. For example one FM entry 'Region'
     * controls the visibility of 'Region' column from donor reports and 'Regional Region' from regional reports.
     */
    private static final Map<String, String> COLUMN_TO_FM_FIELD_MAP = new ImmutableMap.Builder<String, String>()
            .put(ColumnConstants.REGIONAL_REGION, ColumnConstants.REGION)
            .build();

    private static Set<String> COLUMNS_IGNORED_IN_REPORT_WIZARD = new HashSet<>(Arrays.asList(ColumnConstants.EXPENDITURE_CLASS));
    
    private static Logger logger        = Logger.getLogger(ReportWizardAction.class);

    public final static Map<String, Long> reportTypesMap = new HashMap<String, Long>()
        {
            {
                put("donor", new Long(ArConstants.DONOR_TYPE));
                put("regional", new Long(ArConstants.REGIONAL_TYPE));
                put("component", new Long(ArConstants.COMPONENT_TYPE));
                put("contribution", new Long(ArConstants.CONTRIBUTION_TYPE));
                put("pledge", new Long(ArConstants.PLEDGES_TYPE));
            }
        };
        public static HashBiMap<String, Long> reportTypesBiMap = HashBiMap.create(reportTypesMap);    
    
    
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        //if not logged in we have to check if Reporting/Public Report Generator is enabled if not 
        //we redirect to login page
        if(request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null && !FeaturesUtil.isVisibleModule("Public Report Generator")) {
            return mapping.findForward("index");
        }
        
        if (request.getParameter("repType")!=null && request.getParameter("repType").length()>0)
            return this.getJSONrepType(mapping, form, request, response);

        ReportWizardForm myForm     = (ReportWizardForm) form;

        myForm.setDuplicateName(false);
        myForm.setnoReportNameSupplied(false);

        TeamMember teamMember       =(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
        PermissionUtil.putInScope(request.getSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);

        return this.modeSelect(mapping, form, request, response);
    }

    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ReportWizardForm myForm     = (ReportWizardForm) form;

        //if ( request.getParameter("onepager")!=null && "true".equals(request.getParameter("onepager")) )
        //  myForm.setOnePager(true);
        String onePagerGS   = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.REPORT_GENERATOR_ONE_PAGER);
        if ("true".equals(onePagerGS) )
            myForm.setOnePager(true);
        else
            myForm.setOnePager(false);
        
        /* This gets called for new reports/tabs */
        if ( request.getParameter("reset")!=null && "true".equals(request.getParameter("reset")) )
        {
            request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ReportContextData.REPORT_ID_REPORT_WIZARD);
            ReportContextData.createWithId(ReportContextData.REPORT_ID_REPORT_WIZARD, true);
            modeReset(mapping, form, request, response);
        }

        if (myForm.getReportId() != null && myForm.getReportId() != 0)
            request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, myForm.getReportId().toString());

        if (request.getParameter("editReportId") != null ) {
            request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, request.getParameter("editReportId"));
            request.getSession().setAttribute("report_wizard_current_id", request.getAttribute(ReportContextData.BACKUP_REPORT_ID_KEY));
            modeReset(mapping, form, request, response);
            return modeEdit(mapping, form, request, response);
        }

        /**
         * a little ugly hack, but saves lots of development time: ReportWizardAction only supports running a single
         * instance of it at the same time, else they will get mixed up at saving!
         * because the client-side saving code is a huge mess of copy-pasted JavaScript (at least 3 copies of save()
         * and saveReport() instances - probably some of them unused, but don't know which and under
         * which conditions - we save the "current ReportContextId" in the session when firstly opening the wizard (see
         * the two if's above). After that, this value will be used throughout the wizard
         * SIDE-EFFECT: ONLY ONE REPORTWIZARDACTION CAN BE RUN AT A MOMENT BY A USER, ELSE THE FILTERS WILL GET MIXED UP (just like in pre-2.4 AMP)
         */
        if (request.getAttribute(ReportContextData.BACKUP_REPORT_ID_KEY) != null)
            request.getSession().setAttribute("report_wizard_current_id", request.getAttribute(ReportContextData.BACKUP_REPORT_ID_KEY));
        else
        {
            Object reportWizardId = request.getSession().getAttribute("report_wizard_current_id");
            if (reportWizardId == null)
            {
                if (!ReportContextData.contextIdExists())
                    throw new RuntimeException("HUGE bug: ReportWizard lost its reportContextId!");
            }
            request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, reportWizardId);
        }
        
        /* If there's no report title in the request then we decide to show the wizard */
        if (request.getParameter("reportTitle") == null){
            if ( "true".equals( request.getParameter("tab") ) )
                myForm.setDesktopTab(true);
            else
                myForm.setDesktopTab(false);
            return this.modeShow(mapping, form, request, response);
        }
        else { // If there is a report title in the request then it means that the report should be saved.
            // NOTE since multilingual report names have been introduced, this is a dummy, ignored parameter - only used as a flag
            // any request without a "reportTitle" parameter will result in the wizard popin being rendered!
            try{
//              if ( "true".equalsIgnoreCase(request.getParameter("dynamicSaveReport")) || "true".equals(request.getParameter("forceNameOverwrite"))) 
//                  return this.modeDynamicSave(mapping, form, request, response);
//              else
                return this.modeSave(mapping, form, request, response, !"true".equals(request.getParameter("forceNameOverwrite")));
            }
            catch(DuplicateReportNameException e) {
                logger.info( e.getMessage() );
                myForm.setDuplicateName(true);
                return mapping.findForward("save");
            }
            catch(NoReportNameSuppliedException e) {
                logger.info( e.getMessage() );
                myForm.setnoReportNameSupplied(true);
                return mapping.findForward("save");
            }

            catch (RuntimeException e) {
                logger.error( e.getMessage() );
                e.printStackTrace();
                return mapping.findForward("save");
            }
            catch (Exception e)
            {
                // treat some special errors, so as not to add an Exception class for each and every type of error possible (like DuplicateReportNameException)
                if (myForm.getOverwritingForeignReport())
                {
                    logger.info(e.getMessage());
                    return mapping.findForward("save");
                }
                logger.error( e.getMessage() );
                e.printStackTrace();
                return mapping.findForward("save");
            }
        }

    }

    public void modeReset(ActionMapping mapping, ActionForm form,
                          HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ReportWizardForm myForm     = (ReportWizardForm) form;

        myForm.setReportId(null);
        myForm.setReportTitle( null );
        myForm.setOriginalTitle( null );
        myForm.setReportDescription( null );
        myForm.setReportType( "donor" );
        myForm.setReportPeriod("A");
        myForm.setHideActivities( null );
        myForm.setSelectedColumns( null );
        myForm.setSelectedHierarchies( null );
        myForm.setSelectedMeasures( null );
        myForm.setAmpTeamMember( null );
        myForm.setDesktopTab( false );
        myForm.setDuplicateName(false);
        myForm.setDuplicateName(true);
        myForm.setPublicReport(false);
        myForm.setWorkspaceLinked(false);
        myForm.setAllowEmptyFundingColumns(false);
        myForm.setSplitByFunding(false);
        myForm.setUseFilters(false);
        myForm.setBudgetExporter(false);
        myForm.setReportCategory(new Long(0));
        myForm.setAlsoShowPledges(false);
        myForm.setShowOriginalCurrency(false);
        myForm.setReportBeingEdited(false);

        ReportContextData.getFromRequest(true).resetFilters();
    }

    public ActionForward modeShow(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ReportWizardForm myForm     = (ReportWizardForm) form;

        String onePager = "";
        if ( myForm.getOnePager() )
            onePager = "_onepager";

        //Add pledges reports support, the goals is to remove all not pledges columns
        Integer typereport=0;

        if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY)){
            myForm.setProjecttitle("Project Title");
        }else{
            myForm.setProjecttitle("");
        }

        if (!ContentTranslationUtil.multilingualIsEnabled()){
            //If content translation is not enabled we need to use the default site language
            myForm.setDefaultLanguage(TLSUtils.getSite().getDefaultLanguage().getCode());
        } else {
            myForm.setDefaultLanguage(TLSUtils.getEffectiveLangCode());
        }

        if (request.getParameter("type")!=null){
            typereport = new Integer(request.getParameter("type"));
            if (typereport==ArConstants.PLEDGES_TYPE){
                myForm.setReportType("pledge");
            }
        }

        request.getSession().setAttribute(ReportsFilterPicker.PLEDGE_REPORT_REQUEST_ATTRIBUTE, Boolean.toString(typereport == ArConstants.PLEDGES_TYPE)); //WARNING: When merging with 2.4, using ReportContextData attribute instead of storing in the session
        request.setAttribute(MULTILINGUAL_REPORT_PREFIX + "_title", new MultilingualInputFieldValues(AmpReports.class, myForm.getReportId(), "name", null, null));

        myForm.setAmpTreeColumns( this.buildAmpTreeColumnSimple(AdvancedReportUtil.getColumnListFiltered(),typereport,request.getSession()));
        List<AmpMeasures> reportWizardMeasures = new ArrayList<>();
        if (typereport==ArConstants.PLEDGES_TYPE || myForm.getReportType().equalsIgnoreCase("pledge")){
            reportWizardMeasures = AdvancedReportUtil.getMeasureListbyTypeFiltered("P");
        } else if (myForm.getReportType().equalsIgnoreCase("donor")){
            reportWizardMeasures = AdvancedReportUtil.getMeasureListbyTypeFiltered("A");
            reportWizardMeasures.addAll(AdvancedReportUtil.getMeasureListbyTypeFiltered("D"));
        } else {
            reportWizardMeasures = AdvancedReportUtil.getMeasureListbyTypeFiltered("A");
        }
        
        boolean mtefColumnsEnabled = FeaturesUtil.
                getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT);
        
        if (mtefColumnsEnabled) {
            reportWizardMeasures = reportWizardMeasures.stream()
            .filter(m -> !isMTEFName(m.getMeasureName()))
            .collect(Collectors.toList());
        }
        
        myForm.setAmpMeasures(reportWizardMeasures);

        if ( request.getParameter("desktopTab")!=null && "true".equals(request.getParameter("desktopTab")) ) {
            myForm.setDesktopTab( true );
        }

        if ( ! "true".equals(request.getAttribute("editedBudgetExporter")) ) {
            String budgetExporter       = request.getParameter("budgetExporter");
            if ( "true".equals(budgetExporter) )
                myForm.setBudgetExporter(true);
            else
                myForm.setBudgetExporter(false);
        }

        if ( myForm.getDesktopTab() )
            return mapping.findForward("showTab" + onePager);
        else
            return mapping.findForward("showReport" + onePager);
    }

    public ActionForward getJSONrepType(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        ReportWizardForm repForm = (ReportWizardForm)form;

        String repType = request.getParameter("repType");

        JSONObject root = new JSONObject();
        JSONArray children = new JSONArray();

        if (repType.equalsIgnoreCase("donor")){
            repForm.setAmpMeasures( AdvancedReportUtil.getMeasureListbyTypeFiltered("A") );
            repForm.getAmpMeasures().addAll(AdvancedReportUtil.getMeasureListbyTypeFiltered("D") );
        } else {
            repForm.setAmpMeasures( AdvancedReportUtil.getMeasureListbyTypeFiltered("A") );
        }

        for (AmpMeasures measure: repForm.getAmpMeasures())
        {
            JSONObject child = new JSONObject();
            child.put("ID", measure.getMeasureName());
            child.put("name", measure.getMeasureId());
            String nameTrn = TranslatorWorker.translateText(measure.getMeasureName());
            child.put("nameTrn", nameTrn);
            if (measure.getDescription()!=null)
                child.put("description", measure.getDescription());
            else
                child.put("description", "");
            children.add(child);
        }
        root.put("children", children);

        response.setContentType("text/json-comment-filtered");
        OutputStreamWriter outputStream = null;

        try {
            outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
            outputStream.write(root.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return null;
    }

    public ActionForward modeEdit(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        modeReset(mapping, form, request, response);

        ReportWizardForm myForm     = (ReportWizardForm) form;
        Session session = PersistenceManager.getRequestDBSession();

        Long reportId = Long.parseLong( request.getParameter("editReportId") );

        AmpReports ampReport = (AmpReports) session.load(AmpReports.class, reportId );

        myForm.setReportId(reportId);

        myForm.setReportTitle( ampReport.getName() );
        myForm.setReportDescription( ampReport.getReportDescription() );
        myForm.setReportPeriod( ampReport.getOptions() );
        myForm.setDesktopTab( ampReport.getDrilldownTab() );
        myForm.setOriginalTitle( ampReport.getName() );
        myForm.setPublicReport( ampReport.getPublicReport() );
        myForm.setWorkspaceLinked(ampReport.getWorkspaceLinked());
        myForm.setHideActivities( ampReport.getHideActivities() );
        myForm.setAllowEmptyFundingColumns( ampReport.getAllowEmptyFundingColumns() );
        myForm.setSplitByFunding(ampReport.getSplitByFunding());
        myForm.setAlsoShowPledges(ampReport.getAlsoShowPledges());
        myForm.setShowOriginalCurrency(ampReport.getShowOriginalCurrency());
        if(ampReport.getReportCategory() !=null){
            myForm.setReportCategory(ampReport.getReportCategory().getId());
        }

        TeamMember teamMember = (TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
        AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
        myForm.setAmpTeamMember(ampTeamMember);

        if ( ampReport.getBudgetExporter() != null && ampReport.getBudgetExporter() ) {
            myForm.setBudgetExporter(true);
            request.setAttribute("editedBudgetExporter", "true");
        }
        else
            myForm.setBudgetExporter(false);
        myForm.setReportType(reportTypesBiMap.inverse().get(ampReport.getType()));
        myForm.setReportBeingEdited(true);

        TreeSet<AmpReportColumn> cols       = new TreeSet<AmpReportColumn> ( new FieldsComparator() );
        TreeSet<AmpReportHierarchy> hiers   = new TreeSet<AmpReportHierarchy> ( new FieldsComparator() );
        TreeSet<AmpReportMeasures> meas     = new TreeSet<AmpReportMeasures> ( new FieldsComparator() );

        cols.addAll( ampReport.getColumns() );
        meas.addAll( ampReport.getMeasures() );
        if ( ampReport.getHierarchies()!=null )
            hiers.addAll( ampReport.getHierarchies() );

        myForm.setSelectedColumns(      new Long[cols.size()] );
        myForm.setSelectedHierarchies(  new Long[hiers.size()] );
        myForm.setSelectedMeasures(     new Long[meas.size()] );

        this.getFieldIds(myForm.getSelectedColumns(), cols);
        this.getFieldIds(myForm.getSelectedHierarchies(), hiers);
        this.getFieldIds(myForm.getSelectedMeasures(), meas);

        AmpARFilter filter = ReportContextData.getFromRequest().loadOrCreateFilter(true, ampReport);
        logger.info("loaded filters: " + filter.toString());

        Set<AmpFilterData> fdSet    = ampReport.getFilterDataSet();
        if ( fdSet != null && fdSet.size() > 0 ) {
//          AmpARFilter filter      = new AmpARFilter();
//          FilterUtil.populateFilter(ampReport, filter);
//          FilterUtil.prepare(request, filter);
//          ReportContextData.getFromRequest().setSerializedFilter(filter);
//          ReportsFilterPickerForm rfpForm = (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
//          ReportsFilterPicker.modeRefreshDropdowns(mapping, rfpForm, request, response, getServlet().getServletContext() );
//          FilterUtil.populateForm(rfpForm, filter, null);
            myForm.setUseFilters(true);
        }

        return this.modeShow(mapping, form, request, response);
    }

    /**
     * handles the following 3 cases:
     *  1. save a brand new report (myForm.reportId == null)
     *  2. "save" over an existing report (myForm.reportId != null, saveACopy = false)
     *  3. "save as" (myForm.reportId != null, saveACopy = true)
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward modeSave(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response, boolean saveACopy) throws java.lang.Exception {

        ReportWizardForm myForm     = (ReportWizardForm) form;

        boolean dynamicSaveReport = Boolean.valueOf( request.getParameter("dynamicSaveReport") );
        boolean noReportNameSupplied = Boolean.valueOf( request.getParameter("noReportNameSupplied") );
        if (noReportNameSupplied)
        {
            if(myForm.getRunReport()){
                //If we are running the report with out being saved we use a generic translatable name
                myForm.setReportTitle(TranslatorWorker.translateText("Dynamic report"));
            }else{
                throw new NoReportNameSuppliedException("No report name supplied");
            }
        }
        myForm.setWorkspaceLinked(Boolean.valueOf(request.getParameter("workspaceLinked"))); //Struts for some reason ignores this field and I am tired of it
        myForm.setAlsoShowPledges(Boolean.valueOf(request.getParameter("alsoShowPledges")));
        myForm.setSplitByFunding(Boolean.valueOf(request.getParameter("splitByFunding")));
        myForm.setShowOriginalCurrency(Boolean.valueOf(request.getParameter("showOriginalCurrency")));

        TeamMember teamMember       =(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
        
        AmpTeamMember ampTeamMember = null;
        if (teamMember != null) {
            ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
        }
        Collection<AmpColumns> availableCols    = AdvancedReportUtil.getColumnList();
        Collection<AmpMeasures> availableMeas   = AdvancedReportUtil.getMeasureList();

        AmpReports ampReport = null;
        AmpReports oldReport = loadSourceReport(request);
        boolean createReportFromScratch = (oldReport == null || saveACopy);
        if (createReportFromScratch){
            ampReport = new AmpReports();
            ampReport.setType(reportTypesBiMap.get(myForm.getReportType()));
            ampReport.setDrilldownTab( myForm.getDesktopTab() );
        } else
            ampReport = oldReport;
        
        String newName ;
        if (!myForm.getRunReport()){
            newName = MultilingualInputFieldValues.getDefaultName(AmpReports.class, "name", null, request);
            if (otherReportsWithSameNameExist(ampReport, newName)) {
                throw new DuplicateReportNameException("a different report with the same name exists");
            }
        }else{
            newName = myForm.getReportTitle();
        }
        

        if (myForm.getPublicReport() != null && myForm.getPublicReport()){
            boolean updatingPublishedDate = ampReport.getPublicReport() == null || (!ampReport.getPublicReport()); // report was NOT public but is public now
            if (updatingPublishedDate){
                ampReport.setPublishedDate(new Date(System.currentTimeMillis()));
            }
        }

        ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
        
        ampReport.setName(newName); // set the default
        ampReport.setWorkspaceLinked(myForm.getWorkspaceLinked());
        ampReport.setAlsoShowPledges(myForm.getAlsoShowPledges());
        ampReport.setShowOriginalCurrency(myForm.getShowOriginalCurrency());
        ampReport.setOwnerId(myForm.getAmpTeamMember() == null ? ampTeamMember : myForm.getAmpTeamMember());
        if ((!dynamicSaveReport) || createReportFromScratch) {
            ampReport.setHideActivities( myForm.getHideActivities() );
            ampReport.setOptions( myForm.getReportPeriod() );
            ampReport.setReportDescription( myForm.getReportDescription() );
            ampReport.setPublicReport(myForm.getPublicReport());
            if (myForm.getReportCategory() != null && myForm.getReportCategory() != 0){
                ampReport.setReportCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getReportCategory()));
            }else{
                ampReport.setReportCategory(null);
            }

            ampReport.setAllowEmptyFundingColumns( myForm.getAllowEmptyFundingColumns());
            ampReport.setSplitByFunding(myForm.getSplitByFunding());
            ampReport.setBudgetExporter(myForm.getBudgetExporter() != null && myForm.getBudgetExporter());

            ampReport.setColumns( new HashSet<AmpReportColumn>() );
            ampReport.setHierarchies( new HashSet<AmpReportHierarchy>() );
            ampReport.setMeasures( new HashSet<AmpReportMeasures>() );

            AmpCategoryValue level1     = CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);

            this.addFields(myForm.getSelectedColumns(), availableCols, ampReport.getColumns(), AmpReportColumn.class, level1);
            this.addFields(myForm.getSelectedHierarchies(), availableCols, ampReport.getHierarchies(), AmpReportHierarchy.class, level1);
            this.addFields(myForm.getSelectedMeasures(), availableMeas, ampReport.getMeasures(), AmpReportMeasures.class, level1);
            
            /* If all columns are set as hierarchies we add the Project Title column */
            if (  ampReport.getColumns() != null && ampReport.getHierarchies() != null ) {
                int numOfCols       = ampReport.getColumns().size();
                int numOfHiers      = ampReport.getHierarchies().size();
                if ( numOfCols == numOfHiers && (ampReport.getHideActivities() == null || !ampReport.getHideActivities()) ) {
                    for ( AmpColumns tempCol: availableCols ) {
                        if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ) {
                            if (!AdvancedReportUtil.isColumnAdded(ampReport.getColumns(), ArConstants.COLUMN_PROJECT_TITLE)) {
                                AmpReportColumn titleCol            = new AmpReportColumn();
                                titleCol.setLevel(level1);
                                titleCol.setOrderId( new Long((ampReport.getColumns().size()+1)));
                                titleCol.setColumn(tempCol);
                                ampReport.getColumns().add(titleCol);
                                break;
                            }else{
                                /*if Project Title column is already added then remove it from hierarchies list*/
                                if(!FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY).equalsIgnoreCase("true"))
                                    AdvancedReportUtil.removeColumnFromHierarchies(ampReport.getHierarchies(), ArConstants.COLUMN_PROJECT_TITLE);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (ampReport.getAmpReportId() != null && ampReport.getAmpReportId() < 0) // query engine etc: must nullify id so that subsequent code creates report from scratch
            ampReport.setAmpReportId(null);

        if ( ampReport.getAmpReportId() != null )
            AmpFilterData.deleteOldFilterData(ampReport.getAmpReportId());

        AmpARFilter filter = ReportContextData.getFromRequest().getFilter();
        if ( filter != null && myForm.getUseFilters()) {
            Set<AmpFilterData> fdSet    = AmpFilterData.createFilterDataSet(ampReport, filter);
            if ( ampReport.getFilterDataSet() == null )
                ampReport.setFilterDataSet(fdSet);
            else {
                ampReport.getFilterDataSet().clear();
                ampReport.getFilterDataSet().addAll(fdSet);
            }
        }

        modeReset(mapping, form, request, response);
        if ((request.getParameter("runReport") != null) && request.getParameter("runReport").equals("true")) {
            return runReport(ampReport,request,response);
        }else{
            return serializeReportAndOpen(ampReport, teamMember, request, response);
        }
    }
/**
 * Will store the current report with a generated GUID in session's LinkedHashMap(the list is limited to {@link#Constants.MAX_REPORTS_IN_SESSION}
 * @param ampReport
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
    private ActionForward runReport(AmpReports ampReport, HttpServletRequest request, HttpServletResponse response)throws Exception {
        Integer reportToken = UUID.randomUUID().toString().hashCode();
        //Even is quite not possible to have a reportId with such a big number
        //we use a negative number just in case
        if (reportToken > 0) {
            reportToken = reportToken * (-1);
        }
        MaxSizeLinkedHashMap<Integer, AmpReports> reportsList = (MaxSizeLinkedHashMap<Integer, AmpReports>)request.getSession().getAttribute("reportStack");
        if (reportsList == null) {
            reportsList = new MaxSizeLinkedHashMap<Integer, AmpReports>(Constants.MAX_REPORTS_IN_SESSION);
        }
        reportsList.put(reportToken, ampReport);
        request.getSession().setAttribute("reportStack", reportsList);
        callSaikuReport(reportToken, response, "runReportToken");
        return null;
        
    }
    /**
     * returns true if a report with the same name exists in the database AND that report is not going to be overwritten by the in-memory representation
     * @param ampReport
     * @return
     */
    public boolean otherReportsWithSameNameExist(AmpReports ampReport, String newName) {
        // IMPORTANT NOTE (AMP-18515): Since in some countries (ie: Timor) the
        // db has lots of duplicated report/tabs names then this method fails often even
        // when the user is saving some changes without changing the report/tab
        // name, for those cases is that we added first a check to see if the
        // user didnt change the report/tab name, in that case we assume is all
        // ok.
        boolean nameChanged = true;
        if (ampReport.getAmpReportId() != null) {
            if (newName.equals(ampReport.getName())) {
                nameChanged = false;
            }
        }

        if (nameChanged) {
            String queryStr = "select r FROM " + AmpReports.class.getName() + " r where " + AmpReports.hqlStringForName("r")
                    + "=:reportName";
            List<AmpReports> conflicts = PersistenceManager.getSession().createQuery(queryStr).setString("reportName", newName).list();

            // the sole report in the DB which is supposed to have the same name
            // (because we are overwriting it)
            long allowedOtherId = ampReport.getAmpReportId() == null ? -999 : ampReport.getAmpReportId();
            for (AmpReports oth : conflicts) {
                if (oth.getAmpReportId().longValue() != allowedOtherId) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * serializes report to the database. If multilingual is enabled, also saves multilingual translations
     * @param ampReport
     * @param teamMember
     * @param mapping passed to {@link #modeReset(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
     * @param form passed to {@link #modeReset(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward serializeReportAndOpen(AmpReports ampReport, TeamMember teamMember,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (ampReport.getOwnerId() == null)
            throw new RuntimeException("should not save a report without an ownerId!");

        if (ampReport.getHideActivities() == null)
            throw new RuntimeException("should not save a report with a null hideActivities");

        AdvancedReportUtil.saveReport(ampReport, teamMember.getTeamId(), teamMember.getMemberId(), teamMember.getTeamHead() );

        //public static void serialize(Object obj, String prefix, String propertyName, Session session, HttpServletRequest request)
        MultilingualInputFieldValues.serialize(ampReport, "name", null, null, request);

        if ((request.getParameter("openReport") != null) && request.getParameter("openReport").equals("true")) {
            callSaikuReport(ampReport.getAmpReportId().intValue(), response, "openReportId");
        }
        return null;
    }

    private void callSaikuReport(Integer reportId, HttpServletResponse response, String varName) throws IOException {
        PrintWriter out = response.getWriter();
        StringBuilder responseString = new StringBuilder();
        responseString.append(varName + "=" + reportId);
        responseString.append(",");
        responseString.append("saiku=" + true);
        
        out.write(responseString.toString());
        out.flush();
        out.close();
    }

    private AmpReports loadSourceReport(HttpServletRequest request) {
        String ampReportId          = request.getParameter("reportId");
        String backupAmpReportId = (String) request.getSession().getAttribute("report_wizard_current_id");

        if (ampReportId == null || ampReportId.isEmpty())
            ampReportId = backupAmpReportId;
        if (ampReportId == null || ampReportId.isEmpty())
            throw new RuntimeException("No reportId found in request");

        if (!AmpMath.isLong(ampReportId))
            return null;

        request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ampReportId);

        ReportContextData rcd = ReportContextData.getFromRequest();
        AmpARFilter filter = rcd.getFilter();

        if (filter == null)
            throw new RuntimeException("No filter object found in http Session");

        Long reportId               = Long.parseLong(ampReportId);

        AmpReports sourceReport = reportId > 0 ? (AmpReports) PersistenceManager.getSession().load(AmpReports.class, reportId) : rcd.getReportMeta();
        return sourceReport;
    }
//  /**
//   * saves a report based on an another one. In short, copies a report into a new one (with different filters, name and maybe owner). If the "new report" has the same name as the new one, it is overwritten - subject to ownership not changing
//   * @param mapping
//   * @param form
//   * @param request
//   * @param response
//   * @return
//   * @throws java.lang.Exception in case of an error or forbidden operation (like overwriting a different user's report)
//   */
//  public ActionForward modeDynamicSave(ActionMapping mapping, ActionForm form, 
//          HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception { 
//      
//      ReportWizardForm myForm     = (ReportWizardForm) form;
//      
//      AmpReports sourceReport = loadSourceReport();
//      if ( sourceReport == null )
//          throw new Exception ("There was a problem getting access to the old report");
//      
//      AmpReports ampReport = ReportWizardAction.duplicateReportData(reportId, request); // make a detached copy
//      if ( ampReport == null )
//          throw new Exception ("There was a problem duplicating report");
//
//      
//      if ( ampReportTitle.equals(ampReport.getName()) ) { // we need to override the report
//          if (sourceReport.getOwnerId() == null)
//              throw new RuntimeException("unknown owner id of source report");
//          if (!sourceReport.getOwnerId().getAmpTeamMemId().equals(ampTeamMember.getAmpTeamMemId()))
//          {
//              myForm.setOverwritingForeignReport(true);
//              throw new Exception("you are not allowed to override someone else's report");
//          }
//          ampReport.setAmpReportId( reportId );
//          AmpFilterData.deleteOldFilterData( reportId );
//      }
//      
//      if ( AdvancedReportUtil.checkDuplicateReportName(ampReportTitle, teamMember.getMemberId(), reportId, myForm.getDesktopTab() ) ) {
//          myForm.setDuplicateName(true);
//          throw new DuplicateReportNameException("The name " + ampReportTitle + " is already used by another report");
//      }
//      
//      ampReport.setName( ampReportTitle );
//      ampReport.setOwnerId( ampTeamMember );
//      ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
//      ampReport.setFilterDataSet( AmpFilterData.createFilterDataSet(ampReport, filter) );
//      
//      return serializeReportAndOpen(ampReport, teamMember, mapping, myForm, false, request, response);
//  }

    private void addFields (Long [] sourceVector, Collection<?> availableFields, Collection container,
                            Class<?> reportFieldClass, AmpCategoryValue level ) throws Exception {
        if ( sourceVector == null )
            return;
        for (int i=0; i<sourceVector.length; i++ ) {
            Object reportField          = reportFieldClass.newInstance();
            Object [] param1            = new Object[1];
            param1[0]                   = level;
            invokeSetterForBeanPropertyWithAnnotation(reportField, Level.class, param1 );
            //rc.setLevel(level);
            Object [] param2            = new Object[1];
            param2[0]                   =  new Long(i+1);
            invokeSetterForBeanPropertyWithAnnotation(reportField, Order.class, param2 );
            //rc.setOrderId(""+i);

            Iterator<?> iter    = availableFields.iterator();
            boolean foundCol    = false;
            while( iter.hasNext() ) {
                Object field            = iter.next();
                if ( sourceVector[i].equals( invokeGetterForBeanPropertyWithAnnotation(field, Identificator.class, new Object[0]) ) ) {
                    Object [] param3            = new Object[1];
                    param3[0]                   = field;
                    invokeSetterForBeanPropertyWithAnnotation(reportField, ColumnLike.class, param3);
                    foundCol                    = true;
                    break;
                }
            }
            if (foundCol)
                container.add(reportField);
        }
    }

    private void getFieldIds (Long [] destVector, Collection container ) throws Exception {
        Iterator<?> iter    = container.iterator();
        int i               = 0;
        while ( iter.hasNext() ) {
            Object reportField  = iter.next();
            Object field        = invokeGetterForBeanPropertyWithAnnotation(reportField, ColumnLike.class, new Object[0]);
            Object id           = invokeGetterForBeanPropertyWithAnnotation(field, Identificator.class,new Object[0]);
            destVector[i++]     = (Long)id;
        }
    }

    private Map<String, List<AmpColumns>> buildAmpTreeColumnSimple(Collection<AmpColumns> formColumns, Integer type, HttpSession httpSession)
    {
        ArrayList<AmpColumnsVisibility> ampColumnsVisibles = new ArrayList<AmpColumnsVisibility>();
        ServletContext ampContext;
        ampContext = getServlet().getServletContext();
        AmpTreeVisibility ampTreeVisibility =FeaturesUtil.getAmpTreeVisibility(ampContext, httpSession);

        Collection<AmpFieldsVisibility> ampAllFields = FeaturesUtil.getAMPFieldsVisibility();
        Collection<AmpColumns> allAmpColumns = formColumns;

        TreeSet<AmpColumnsOrder> ampThemesOrdered = new TreeSet<AmpColumnsOrder>();

        ArrayList<AmpColumnsOrder> ampColumnsOrder = (ArrayList<AmpColumnsOrder>) ampContext.getAttribute("ampColumnsOrder");
        Map<String, AmpColumnsOrder> ampColumnsOrderByName = new HashMap<String, AmpColumnsOrder>();
        for(AmpColumnsOrder order:ampColumnsOrder)
            ampColumnsOrderByName.put(order.getColumnName(), order);

        Map scope = PermissionUtil.getScope(httpSession);

        Map<String, AmpFieldsVisibility> ampAllFieldsByName = new HashMap<String, AmpFieldsVisibility>();
        for(AmpFieldsVisibility field:ampAllFields)
            ampAllFieldsByName.put(field.getName(), field);
        
        boolean mtefColumnsEnabled = FeaturesUtil.
                getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT);
        
        for(AmpColumns ampColumn:allAmpColumns)
        {
            String columnName = ampColumn.getColumnName();
            if (columnIgnoredInReportWizard(columnName)) {
                continue;
            }
            
            if (!mtefColumnsEnabled && isMTEFName(columnName)) {
                continue;
            }

            String fmFieldName = COLUMN_TO_FM_FIELD_MAP.getOrDefault(columnName, columnName);
            AmpFieldsVisibility ampFieldVisibility = ampAllFieldsByName.get(fmFieldName);
            if(ampFieldVisibility == null)
                continue;

            if (!ampFieldVisibility.isFieldActive(ampTreeVisibility))
                continue; // negative "continue" instead of if/else, so as to reduce the depth of blocks

            //skip build columns with no rights
            if(ampFieldVisibility.getPermission(false) != null && !ampFieldVisibility.canDo(GatePermConst.Actions.VIEW,scope))
                continue;

            AmpColumnsVisibility ampColumnVisibilityObj = new AmpColumnsVisibility();
            ampColumnVisibilityObj.setAmpColumn(ampColumn);
            ampColumnVisibilityObj.setAmpfield(ampFieldVisibility);
            ampColumnVisibilityObj.setParent((AmpFeaturesVisibility) ampFieldVisibility.getParent());
            ampColumnsVisibles.add(ampColumnVisibilityObj);

            if (type == ArConstants.PLEDGES_TYPE)
            {
                // TODO-CONSTANTIN: looks stupid, should be moved out of the loop completely
                for(AmpColumnsOrder aco:ampColumnsOrder)
                    if (aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_COLUMNS) ||
                            aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_1)||aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_2))
                    {
                        ampThemesOrdered.add(aco);
                    }
            }
            else
            {
                AmpColumnsOrder aco = ampColumnsOrderByName.get(getThemeName(ampFieldVisibility.getParent().getName()));
                if (aco == null)
                    continue;

                if (!aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_COLUMNS) && !aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_1)
                        && !aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_2)){
                     ampThemesOrdered.add(aco);
                }
            }
        }

        Map<String, List<AmpColumns>> ampTreeColumn = new LinkedHashMap<String, List<AmpColumns>>();

        for(AmpColumnsOrder aco:ampThemesOrdered)
        {
            String themeName = aco.getColumnName();
            ArrayList<AmpColumns> aux = new ArrayList<AmpColumns>();
            boolean added = false;
            for (AmpColumnsVisibility acv:ampColumnsVisibles)
            {
                //iterations2 ++;
                if(themeName.equals(getThemeName(acv.getParent().getName())))
                {
                    aux.add( acv.getAmpColumn() );
                    added   = true;
                }

            }
            if(added)
            {
                if (themeName.equals("M & E")) {
                    aux.sort(ME_COLS_COMPARATOR);
                }
                ampTreeColumn.put(themeName, aux);
            }
        }
        //System.err.println("iterations1 = " + iterations1 + ", iterations 2 = " + iterations2);
        return ampTreeColumn;
    }

    /**
     * Get theme name from feature name. Usually theme name matches feature name, but in some cases it was not
     * possible to do so. Indicator columns are grouped under /Monitoring & Evaluation/M & E/Reports, in this case
     * this mechanism allows to swap Reports with M & E.
     *
     * @param featureName feature name
     * @return theme name
     */
    private String getThemeName(String featureName) {
        return "Reports".equals(featureName) ? "M & E" : featureName;
    }

    public static void invokeSetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
        Class myClass       = beanObj.getClass();
        Field[] fields      = myClass.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if ( fields[i].getAnnotation(annotationClass) != null) {
                PropertyDescriptor beanProperty = new PropertyDescriptor(fields[i].getName(), myClass);
                beanProperty.getWriteMethod().invoke(beanObj, params);
                return;
            }
        }
        throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() +
                "' with annotation '" + annotationClass.getCanonicalName()
                + "'");
    }

    public static Object invokeGetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
        Class myClass       = beanObj.getClass();
        Field[] fields      = myClass.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if ( fields[i].getAnnotation(annotationClass) != null) {
                PropertyDescriptor beanProperty = new PropertyDescriptor(fields[i].getName(), myClass);
                return beanProperty.getReadMethod().invoke(beanObj, params);
            }
        }
        throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() +
                "' with annotation '" + annotationClass.getCanonicalName()
                + "'");
    }

    /**
     * loads an AmpReport by id; returns null on any error
     * returns the in-memory report if ampReportId < 0
     * @param ampReportId
     * @param request
     * @return
     */
    public static AmpReports loadAmpReport(Long ampReportId, HttpServletRequest request)
    {
        AmpReports ampReport    = null;
        try {
            if (ampReportId > 0)
                ampReport   =  (AmpReports) PersistenceManager.getSession().load(AmpReports.class, ampReportId );
            else 
                ampReport   = ReportContextData.getFromRequest().getReportMeta();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ampReport;
    }

    /**
     * detaches a report from the DB, effectively creating a new, identical one
     * @param ampReportId
     * @param request
     * @return
     */
    public static AmpReports duplicateReportData (Long ampReportId, HttpServletRequest request) {
        AmpReports ampReport = loadAmpReport(ampReportId, request);
        try{
            if (ampReport == null)
                throw new RuntimeException("report not found: " + ampReportId);
            ampReport.setAmpReportId(null);
            //ampReport.setAmpPage(null);
            ampReport.setFilterDataSet(null);
            ampReport.setUpdatedDate(null);
            ampReport.setLogs(null);
            ampReport.setLocale(null);
            ampReport.setSiteId(null);
            ampReport.setMembers(null);
            ampReport.setOwnerId(null);
            ampReport.setDesktopTabSelections(null);

            HashSet<AmpReportColumn> columns = new HashSet<AmpReportColumn>();
            columns.addAll( ampReport.getColumns() );

            HashSet<AmpReportHierarchy> hierarchies = new HashSet<AmpReportHierarchy>();
            hierarchies.addAll( ampReport.getHierarchies() );

            HashSet<AmpReportMeasures> measures = new HashSet<AmpReportMeasures>();
            measures.addAll( ampReport.getMeasures() );

            HashSet<AmpMeasures> reportMeasures = new HashSet<AmpMeasures>();

            if ( ampReport.getReportMeasures() != null )
                reportMeasures.addAll( ampReport.getReportMeasures() );

            ampReport.setColumns( columns );
            ampReport.setHierarchies( hierarchies );
            ampReport.setMeasures( measures );
            ampReport.setReportMeasures( reportMeasures );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ampReport;
    }

    private class FieldsComparator implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            try{
                Long order1                   = (Long)invokeGetterForBeanPropertyWithAnnotation(o1, Order.class, new Object[0]);
                Long order2                   = (Long)invokeGetterForBeanPropertyWithAnnotation(o2, Order.class, new Object[0]);
                return order1.compareTo(order2);
            }
            catch (RuntimeException e) {
                e.printStackTrace();
                return -1;
            }
            catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

    }
    
    public static boolean columnIgnoredInReportWizard(String columnName) {
        return COLUMNS_IGNORED_IN_REPORT_WIZARD.contains(columnName);
    }
    
    public static boolean isMTEFName(String name) {
        String regex = "^(" + MeasureConstants.MTEF 
                + "|" + MeasureConstants.REAL_MTEF
                + "|" + MeasureConstants.MTEF_PROJECTIONS 
                + "|" + MeasureConstants.PIPELINE_MTEF_PROJECTIONS 
                + "|" + MeasureConstants.PROJECTION_MTEF_PROJECTIONS 
                + ").*$";
        
        return name.matches(regex);
    }

}
