/**
 * 
 */
package org.digijava.module.aim.action.reportwizard;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.utils.MultiAction;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.module.aim.action.GlobalSettings;
import org.digijava.module.aim.action.ReportsFilterPicker;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Identificator;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.ar.util.FilterUtil;
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
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.form.reportwizard.ReportWizardForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 *
 */
public class ReportWizardAction extends MultiAction {
	
	public static final String SESSION_FILTER					= "reportWizardFilter";
	public static final String EXISTING_SESSION_FILTER			= "existingReportWizardFilter";
	public static final String REPORT_WIZARD_INIT_ON_FILTERS	= "rep_wiz_init";
	
	private static Logger logger 		= Logger.getLogger(ReportWizardAction.class);
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		ReportWizardForm myForm		= (ReportWizardForm) form;
		
		myForm.setDuplicateName(false);
		
		TeamMember teamMember		=(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		PermissionUtil.putInScope(request.getSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
		
		return this.modeSelect(mapping, form, request, response);
	}
	
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		ReportWizardForm myForm		= (ReportWizardForm) form;
		
		//if ( request.getParameter("onepager")!=null && "true".equals(request.getParameter("onepager")) )
		//	myForm.setOnePager(true);
		String onePagerGS	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.REPORT_GENERATOR_ONE_PAGER);
		if ("true".equals(onePagerGS) ) 
			myForm.setOnePager(true);
		else
			myForm.setOnePager(false);
		
		/* This gets called for new reports/tabs */
		if ( request.getParameter("reset")!=null && "true".equals(request.getParameter("reset")) )
			modeReset(mapping, form, request, response);
		
		if (request.getParameter("editReportId") != null ) {
			modeReset(mapping, form, request, response);
			return modeEdit(mapping, form, request, response);
		}
		
		
		/* If there's no report title in the request then we decide to show the wizard */
		if (request.getParameter("reportTitle") == null){ 
			if ( "true".equals( request.getParameter("tab") ) )
				myForm.setDesktopTab(true);
			else
				myForm.setDesktopTab(false);
			return this.modeShow(mapping, form, request, response);
		}
		else { // If there is a report title in the request then it means that the report should be saved
			try{
				if ( "true".equalsIgnoreCase( request.getParameter("dynamicSaveReport") ) ) 
					return this.modeDynamicSave(mapping, form, request, response);
				else
					return this.modeSave(mapping, form, request, response);
			}
			catch(DuplicateReportNameException e) {
				logger.info( e.getMessage() );
				return mapping.findForward("save");
			}
			catch (RuntimeException e) {
				logger.error( e.getMessage() );
				e.printStackTrace();
				return mapping.findForward("save");
			}
			catch (Exception e) {
				logger.error( e.getMessage() );
				e.printStackTrace();
				return mapping.findForward("save");
			}
		}
		
	}
	
	public void modeReset(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		ReportWizardForm myForm		= (ReportWizardForm) form;
		
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
		myForm.setPublicReport(false);
		myForm.setAllowEmptyFundingColumns(false);
		myForm.setUseFilters(false);
		myForm.setBudgetExporter(false);
		
		request.getSession().setAttribute( ReportWizardAction.EXISTING_SESSION_FILTER, null );
		request.getSession().setAttribute( ReportWizardAction.SESSION_FILTER, null );
		request.getSession().setAttribute( ArConstants.REPORTS_FILTER, null );

		/**
		 * The ReportsFilterPickerForm needs to be cleaned before using in the wizard
		 */
		ReportsFilterPicker rfp		= new ReportsFilterPicker();
		ReportsFilterPickerForm rfpForm	= (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
		if (rfpForm == null ) {
			rfpForm		= new ReportsFilterPickerForm();
			request.setAttribute(ReportWizardAction.REPORT_WIZARD_INIT_ON_FILTERS, "true");
			rfp.modePrepare(mapping, rfpForm, request, response);
			TagUtil.setForm(request, "aimReportsFilterPickerForm", rfpForm, true);
		}
		rfpForm.setIsnewreport(true);
		rfp.reset(rfpForm, request, mapping);
		rfpForm.setIsnewreport(false);
		
	}
	
	public ActionForward modeShow(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		ReportWizardForm myForm		= (ReportWizardForm) form;
		
		String onePager				= "";
		if ( myForm.getOnePager() )
			onePager	= "_onepager";

		//Add pledges reports support, the goals is to remove all not pledges columns
		Integer typereport=0;
		
		if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY)){
			myForm.setProjecttitle("Project Title");
		}else{
			myForm.setProjecttitle("");
		}
		
		if (request.getParameter("type")!=null){
			typereport = new Integer(request.getParameter("type"));
			if (typereport==ArConstants.PLEDGES_TYPE){
				myForm.setReportType("pledge");
			}
		}
		if (myForm.getReportType().equalsIgnoreCase("pledge") && typereport != ArConstants.PLEDGES_TYPE){
			typereport = ArConstants.PLEDGES_TYPE;
			request.getParameterMap().put("type", "5");
		}
		
		myForm.setAmpTreeColumns( this.buildAmpTreeColumnSimple(AdvancedReportUtil.getColumnList(),typereport,request.getSession()));
		if (typereport==ArConstants.PLEDGES_TYPE || myForm.getReportType().equalsIgnoreCase("pledge")){
			myForm.setAmpMeasures( AdvancedReportUtil.getMeasureListbyType("P"));
		}else{
			myForm.setAmpMeasures( AdvancedReportUtil.getMeasureListbyType("A") );
		}
		
		if ( request.getParameter("desktopTab")!=null && "true".equals(request.getParameter("desktopTab")) ) {
			myForm.setDesktopTab( true );
		}
		
		if ( ! "true".equals(request.getAttribute("editedBudgetExporter")) ) {
			String budgetExporter		= request.getParameter("budgetExporter");
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
	public ActionForward modeEdit(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		modeReset(mapping, form, request, response);
		
		ReportWizardForm myForm		= (ReportWizardForm) form;
		Session session				= PersistenceManager.getRequestDBSession();
		
		Long reportId		= Long.parseLong( request.getParameter("editReportId") );
		
		AmpReports ampReport	= (AmpReports) session.load(AmpReports.class, reportId );
		
		myForm.setReportId( reportId );
		
		myForm.setReportTitle( ampReport.getName() );
		myForm.setReportDescription( ampReport.getReportDescription() );
		myForm.setReportPeriod( ampReport.getOptions() );
		myForm.setDesktopTab( ampReport.getDrilldownTab() );
		myForm.setOriginalTitle( ampReport.getName() );
		myForm.setPublicReport( ampReport.getPublicReport() );
		myForm.setHideActivities( ampReport.getHideActivities() );
		myForm.setAllowEmptyFundingColumns( ampReport.getAllowEmptyFundingColumns() );
		
		TeamMember teamMember		=(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
		myForm.setAmpTeamMember(ampTeamMember);
		
		if ( ampReport.getBudgetExporter() != null && ampReport.getBudgetExporter() ) {
			myForm.setBudgetExporter(true);
			request.setAttribute("editedBudgetExporter", "true");
		}
		else
			myForm.setBudgetExporter(false);
		
		if ( new Long(ArConstants.DONOR_TYPE).equals(ampReport.getType()) )
			myForm.setReportType("donor");
		if ( new Long(ArConstants.REGIONAL_TYPE).equals(ampReport.getType()) )
			myForm.setReportType("regional");
		if ( new Long(ArConstants.COMPONENT_TYPE).equals(ampReport.getType()) )
			myForm.setReportType("component");
		if ( new Long(ArConstants.CONTRIBUTION_TYPE).equals(ampReport.getType()) )
			myForm.setReportType("contribution");
		if ( new Long(ArConstants.PLEDGES_TYPE).equals(ampReport.getType()) )
			myForm.setReportType("pledge");
		
		
		TreeSet<AmpReportColumn> cols		= new TreeSet<AmpReportColumn> ( new FieldsComparator() );
		TreeSet<AmpReportHierarchy> hiers	= new TreeSet<AmpReportHierarchy> ( new FieldsComparator() );
		TreeSet<AmpReportMeasures> meas		= new TreeSet<AmpReportMeasures> ( new FieldsComparator() );
		
		cols.addAll( ampReport.getColumns() );
		meas.addAll( ampReport.getMeasures() );
		if ( ampReport.getHierarchies()!=null )
			hiers.addAll( ampReport.getHierarchies() );
		
		myForm.setSelectedColumns( 		new Long[cols.size()] );
		myForm.setSelectedHierarchies( 	new Long[hiers.size()] );
		myForm.setSelectedMeasures( 	new Long[meas.size()] );
		
		this.getFieldIds(myForm.getSelectedColumns(), cols);
		this.getFieldIds(myForm.getSelectedHierarchies(), hiers);
		this.getFieldIds(myForm.getSelectedMeasures(), meas);
		
		Set<AmpFilterData> fdSet	= ampReport.getFilterDataSet();
		if ( fdSet != null && fdSet.size() > 0 ) {
			AmpARFilter filter		= new AmpARFilter();
			FilterUtil.populateFilter(ampReport, filter);
			FilterUtil.prepare(request, filter);
			request.getSession().setAttribute( ReportWizardAction.EXISTING_SESSION_FILTER , filter);
			ReportsFilterPickerForm rfpForm	= (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
			new ReportsFilterPicker().modeRefreshDropdowns(mapping, rfpForm, request, response, getServlet().getServletContext() );
			FilterUtil.populateForm(rfpForm, filter);
			myForm.setUseFilters(true);
			
		}
				
		
		return this.modeShow(mapping, form, request, response);
	}
	
	public ActionForward modeSave(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		ReportWizardForm myForm		= (ReportWizardForm) form;
		
		TeamMember teamMember		=(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
		
		if ( AdvancedReportUtil.checkDuplicateReportName(myForm.getReportTitle(), teamMember.getMemberId(), myForm.getReportId(), myForm.getDesktopTab()) ) {
			myForm.setDuplicateName(true);
			throw new DuplicateReportNameException("The name " + myForm.getReportTitle() + " is already used by another report");
		}
			
		Collection<AmpColumns> availableCols	= AdvancedReportUtil.getColumnList();
		Collection<AmpMeasures> availableMeas	= AdvancedReportUtil.getMeasureList();		
		
		AmpReports ampReport	= new AmpReports();
		if ( "donor".equals(myForm.getReportType()) ) 
				ampReport.setType( new Long(ArConstants.DONOR_TYPE) );
		if ( "regional".equals(myForm.getReportType()) ) 
				ampReport.setType( new Long(ArConstants.REGIONAL_TYPE) );
		if ( "component".equals(myForm.getReportType()) ) 
				ampReport.setType( new Long(ArConstants.COMPONENT_TYPE) );
		if ( "contribution".equals(myForm.getReportType()) ) 
				ampReport.setType( new Long(ArConstants.CONTRIBUTION_TYPE) );
		if ( "pledge".equals(myForm.getReportType()) ) 
			ampReport.setType( new Long(ArConstants.PLEDGES_TYPE) );
		
		ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
		ampReport.setHideActivities( myForm.getHideActivities() );
		ampReport.setOptions( myForm.getReportPeriod() );
		ampReport.setReportDescription( myForm.getReportDescription() );
		ampReport.setName( myForm.getReportTitle().trim() );
		ampReport.setDrilldownTab( myForm.getDesktopTab() );
		ampReport.setPublicReport(myForm.getPublicReport());
		
		if(myForm.getPublicReport()!=null&&myForm.getPublicReport()){
			if(myForm.getReportId()==null){
				ampReport.setPublishedDate(new Date(System.currentTimeMillis()));
			}
			else{
				boolean wasPublic = DbUtil.isPublicReport(myForm.getReportId());
				if(!wasPublic||(myForm.getOriginalTitle()!=null && !myForm.getOriginalTitle().equals(myForm.getReportTitle()))){
					ampReport.setPublishedDate(new Date(System.currentTimeMillis()));
				}
			}
			
		}
		ampReport.setAllowEmptyFundingColumns( myForm.getAllowEmptyFundingColumns() );
		
		if ( myForm.getBudgetExporter() != null && myForm.getBudgetExporter() )
			ampReport.setBudgetExporter(true);
		else
			ampReport.setBudgetExporter(false);
		
		if ( myForm.getReportId() != null ) {
				if ( myForm.getOriginalTitle()!=null && myForm.getOriginalTitle().equals(myForm.getReportTitle()) )
						ampReport.setAmpReportId( myForm.getReportId() );
		}
		
		if ( myForm.getAmpTeamMember() == null ) {
				ampReport.setOwnerId( ampTeamMember );
		}
		else
				ampReport.setOwnerId( myForm.getAmpTeamMember() );
		
		ampReport.setColumns( new HashSet<AmpReportColumn>() );
		ampReport.setHierarchies( new HashSet<AmpReportHierarchy>() );
		ampReport.setMeasures( new HashSet<AmpReportMeasures>() );
		
		AmpCategoryValue level1		= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
		
		this.addFields(myForm.getSelectedColumns(), availableCols, ampReport.getColumns(), AmpReportColumn.class, level1);
		this.addFields(myForm.getSelectedHierarchies(), availableCols, ampReport.getHierarchies(), AmpReportHierarchy.class, level1);
		this.addFields(myForm.getSelectedMeasures(), availableMeas, ampReport.getMeasures(), AmpReportMeasures.class, level1);
		
		/* If all columns are set as hierarchies we add the Project Title column */
		if (  ampReport.getColumns() != null && ampReport.getHierarchies() != null ) {
			int numOfCols		= ampReport.getColumns().size();
			int numOfHiers		= ampReport.getHierarchies().size();
			/* "Cumulative Commitment", and "Cumulative Disbursement" are not treated as columns so if they appear 
			 * we need to substract them from the total number of cols */
			for ( AmpReportColumn tempRepCol: ampReport.getColumns() ) {
				if ( ArConstants.COLUMN_CUMULATIVE_COMMITMENT.equals(tempRepCol.getColumn().getColumnName()) ) {
					numOfCols--;
					continue;
				}
				if ( ArConstants.COLUMN_CUMULATIVE_DISBURSEMENT.equals(tempRepCol.getColumn().getColumnName()) ) {
					numOfCols--;
					continue;
				}
				if ( ArConstants.COLUMN_UNDISB_CUMULATIVE_BALANCE.equals(tempRepCol.getColumn().getColumnName()) ) {
					numOfCols--;
					continue;
				}
				if ( ArConstants.COLUMN_UNCOMM_CUMULATIVE_BALANCE.equals(tempRepCol.getColumn().getColumnName()) ) {
					numOfCols--;
					continue;
				}
			}
			if ( numOfCols == numOfHiers && (ampReport.getHideActivities() == null || !ampReport.getHideActivities()) ) {
				for ( AmpColumns tempCol: availableCols ) {
					if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ) {
						if (!AdvancedReportUtil.isColumnAdded(ampReport.getColumns(), ArConstants.COLUMN_PROJECT_TITLE)) {
							AmpReportColumn titleCol			= new AmpReportColumn();
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
		
		Object filter	= request.getSession().getAttribute( ReportWizardAction.SESSION_FILTER );
		if ( filter == null )
			filter		= request.getSession().getAttribute( ReportWizardAction.EXISTING_SESSION_FILTER );
		if ( filter != null && myForm.getUseFilters()) {
			if ( ampReport.getAmpReportId()!=null )
				AmpFilterData.deleteOldFilterData( ampReport.getAmpReportId() );
			Set<AmpFilterData> fdSet	= AmpFilterData.createFilterDataSet(ampReport, filter);
			if ( ampReport.getFilterDataSet() == null )
				ampReport.setFilterDataSet(fdSet);
			else {
				ampReport.getFilterDataSet().clear();
				ampReport.getFilterDataSet().addAll(fdSet);
			}
				
		}
		else
			if ( ampReport.getAmpReportId()!=null )
				AmpFilterData.deleteOldFilterData( ampReport.getAmpReportId() );
			
		AdvancedReportUtil.saveReport(ampReport, teamMember.getTeamId(), teamMember.getMemberId(), teamMember.getTeamHead() );
		
		modeReset(mapping, form, request, response);
		
		return null;
	}
	
	public ActionForward modeDynamicSave(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception { 
		
		ReportWizardForm myForm		= (ReportWizardForm) form;
		
		TeamMember teamMember		=(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
		
		AmpARFilter	filter		= (AmpARFilter)request.getSession().getAttribute(ArConstants.REPORTS_FILTER);
		if (filter == null)
			throw new Exception ("No filter object found in http Session");
		String ampReportId			= request.getParameter("reportId");
		if ( ampReportId == null ||ampReportId.length() == 0 )
			throw new Exception ("No reportId found in request");
		
		Long reportId				= Long.parseLong(ampReportId);
		
		String ampReportTitle			= request.getParameter("reportTitle");
		if ( ampReportTitle == null || ampReportTitle.length() == 0 )
			throw new Exception ("No reportTitle found in request");
		
		AmpReports ampReport			= ReportWizardAction.duplicateReportData( reportId, request );
		if ( ampReport == null )
			throw new Exception ("There was a problem getting access to the old report");
		
		if ( ampReportTitle.equals(ampReport.getName()) ) { // we need to override the report
			ampReport.setAmpReportId( reportId );
			AmpFilterData.deleteOldFilterData( reportId );
		}
		
		if ( AdvancedReportUtil.checkDuplicateReportName(ampReportTitle, teamMember.getMemberId(), reportId, myForm.getDesktopTab() ) ) {
			myForm.setDuplicateName(true);
			throw new DuplicateReportNameException("The name " + ampReportTitle + " is already used by another report");
		}
		
		ampReport.setName( ampReportTitle );
		ampReport.setOwnerId( ampTeamMember );
		ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
		ampReport.setFilterDataSet( AmpFilterData.createFilterDataSet(ampReport, filter) );
		
		AdvancedReportUtil.saveReport(ampReport, teamMember.getTeamId(), teamMember.getMemberId(), teamMember.getTeamHead() );
		
		//modeReset(mapping, form, request, response);
		
		return null;
	}
	
	private void addFields (Long [] sourceVector, Collection availableFields, Collection container, 
						Class reportFieldClass, AmpCategoryValue level ) throws Exception {
		if ( sourceVector == null )
				return;
		for (int i=0; i<sourceVector.length; i++ ) {
			Object reportField			= reportFieldClass.newInstance();
			Object [] param1			= new Object[1];
			param1[0]					= level;
			invokeSetterForBeanPropertyWithAnnotation(reportField, Level.class, param1 );
			//rc.setLevel(level);
			Object [] param2			= new Object[1];
			param2[0]					=  new Long(i+1);
			invokeSetterForBeanPropertyWithAnnotation(reportField, Order.class, param2 );
			//rc.setOrderId(""+i);
			
			Iterator<?> iter	= availableFields.iterator();
			boolean foundCol	= false;
			while( iter.hasNext() ) {
				Object field			= iter.next();
				if ( sourceVector[i].equals( invokeGetterForBeanPropertyWithAnnotation(field, Identificator.class, new Object[0]) ) ) {
					Object [] param3			= new Object[1];
					param3[0]					= field;
					invokeSetterForBeanPropertyWithAnnotation(reportField, ColumnLike.class, param3);
					foundCol					= true;
					break;
				}
			}
			if (foundCol)
				container.add(reportField);
		}
	}
	
	private void getFieldIds (Long [] destVector, Collection container ) throws Exception {
			Iterator<?> iter	= container.iterator();
			int i				= 0;
			while ( iter.hasNext() ) {
				Object reportField	= iter.next();
				Object field		= invokeGetterForBeanPropertyWithAnnotation(reportField, ColumnLike.class, new Object[0]);
				Object id			= invokeGetterForBeanPropertyWithAnnotation(field, Identificator.class,new Object[0]);
				destVector[i++]		= (Long)id;
			}
	}
	
	private HashMap buildAmpTreeColumnSimple(Collection formColumns, Integer type, HttpSession httpSession)
	{
			
			ArrayList ampColumnsVisibles=new ArrayList();
			ServletContext ampContext;
			ampContext=getServlet().getServletContext();
			AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			Collection ampAllFields= FeaturesUtil.getAMPFieldsVisibility();
			Collection allAmpColumns=formColumns;
			Collection allAmpColumnsPrefixed=new ArrayList(); // put a "REPORTS " prefix
			TreeSet ampThemes=new TreeSet();
			TreeSet ampThemesOrdered=new TreeSet();
			ArrayList ampColumnsOrder =(ArrayList) ampContext.getAttribute("ampColumnsOrder");
			Map scope=PermissionUtil.getScope(httpSession);  
			/*
			for(Iterator it=allAmpColumns.iterator();it.hasNext();)
			{
				AmpColumns ampColumn=(AmpColumns) it.next();
				ampColumn.setColumnName("REPORTS "+ampColumn.getColumnName());
				allAmpColumnsPrefixed.add(ampColumn);
			}
			allAmpColumns.clear();
			allAmpColumns.addAll(allAmpColumnsPrefixed);
			*/			  
			
			for(Iterator it=allAmpColumns.iterator();it.hasNext();)
			{
				AmpColumns ampColumn=(AmpColumns) it.next();
				for(Iterator jt=ampAllFields.iterator();jt.hasNext();)
				{
					AmpFieldsVisibility ampFieldVisibility=(AmpFieldsVisibility) jt.next();
					if(ampColumn.getColumnName().compareTo(ampFieldVisibility.getName())==0)
					{
						if(ampFieldVisibility.isFieldActive(ampTreeVisibility)) {
							
							  //skip build columns with no rights
							  if(ampFieldVisibility.getPermission(false)!=null && !ampFieldVisibility.canDo(GatePermConst.Actions.VIEW,scope)) continue;
						
							AmpColumnsVisibility ampColumnVisibilityObj=new AmpColumnsVisibility();
							ampColumnVisibilityObj.setAmpColumn(ampColumn);
							ampColumnVisibilityObj.setAmpfield(ampFieldVisibility);
							ampColumnVisibilityObj.setParent((AmpFeaturesVisibility) ampFieldVisibility.getParent());
							ampColumnsVisibles.add(ampColumnVisibilityObj);
							ampThemes.add(ampFieldVisibility.getParent().getName());
							for(Iterator kt=ampColumnsOrder.iterator();kt.hasNext();)
							{
								AmpColumnsOrder aco=(AmpColumnsOrder) kt.next();
								//System.out.println("----------------"+aco.getColumnName()+":"+aco.getId()+":"+aco.getIndexOrder());
								if (type == ArConstants.PLEDGES_TYPE){
									if (aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_COLUMNS)|| 
											aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_1)||aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_2)){
										ampThemesOrdered.add(aco);
									}
								}else{
									if(ampFieldVisibility.getParent().getName().compareTo(aco.getColumnName())==0 &&
											!aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_COLUMNS) && !aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_1)
											&& !aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_2)){
												ampThemesOrdered.add(aco);
										//System.out.println("	----------------ADDED!");
									}
								}
							}
						}
					}
				}
			}
			LinkedHashMap ampTreeColumn=new LinkedHashMap();
			int jjj=0;
			for(Iterator it=ampThemesOrdered.iterator();it.hasNext();)
			{
				AmpColumnsOrder aco=(AmpColumnsOrder) it.next();
				String themeName=(String) aco.getColumnName();
				ArrayList aux=new ArrayList();
				boolean added=false;
				for(Iterator jt=ampColumnsVisibles.iterator();jt.hasNext();)
				{
					AmpColumnsVisibility acv=(AmpColumnsVisibility) jt.next();
					if(themeName.compareTo(acv.getParent().getName())==0)
					{
						aux.add( acv.getAmpColumn() );
						added	= true;
					}
					
				}
				if(added) {
					
					ampTreeColumn.put(themeName, aux);
				}
			}
			
			return ampTreeColumn;
	}
	
	public static void invokeSetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
		Class myClass		= beanObj.getClass();
		Field[] fields		= myClass.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			if ( fields[i].getAnnotation(annotationClass) != null) {
				PropertyDescriptor beanProperty	= new PropertyDescriptor(fields[i].getName(), myClass);
				beanProperty.getWriteMethod().invoke(beanObj, params);
				return;
			}
		}
		throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() + 
				"' with annotation '" + annotationClass.getCanonicalName() 
				+ "'");
	}
	
	public static Object invokeGetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
		Class myClass		= beanObj.getClass();
		Field[] fields		= myClass.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			if ( fields[i].getAnnotation(annotationClass) != null) {
				PropertyDescriptor beanProperty	= new PropertyDescriptor(fields[i].getName(), myClass);
				return beanProperty.getReadMethod().invoke(beanObj, params);
			}
		}
		throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() + 
				"' with annotation '" + annotationClass.getCanonicalName() 
				+ "'");
	}
	
	public static AmpReports duplicateReportData (Long ampReportId, HttpServletRequest request) {
		AmpReports ampReport	= null;
		try{
			Session session			= null;
			try {
				session				= PersistenceManager.openNewSession();
				if (ampReportId > 0)
					ampReport	=  (AmpReports) session.load(AmpReports.class, ampReportId );
				else 
					ampReport	= (AmpReports) request.getSession().getAttribute("reportMeta");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if ( session != null )
					session.close();
			}
			
			ampReport.setAmpReportId(null);
			//ampReport.setAmpPage(null);
			ampReport.setFilterDataSet(null);
			ampReport.setUpdatedDate(null);
			ampReport.setLogs(null);
			ampReport.setLocale(null);
			ampReport.setSiteId(null);
			ampReport.setMembers(null);
			ampReport.setNameTrn(null);
			ampReport.setOwnerId(null);
			ampReport.setDesktopTabSelections(null);
			
			HashSet columns		= new HashSet();
			columns.addAll( ampReport.getColumns() );
			
			HashSet hierarchies	= new HashSet();
			hierarchies.addAll( ampReport.getHierarchies() );
			
			HashSet measures	= new HashSet();
			measures.addAll( ampReport.getMeasures() );
			
			HashSet reportMeasures	= new HashSet();
			
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
	
}
