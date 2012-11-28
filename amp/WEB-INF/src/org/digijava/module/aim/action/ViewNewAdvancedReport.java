/**
 * ViewNewAdvancedReport.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportLog;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 * 
 */
public class ViewNewAdvancedReport extends Action {
	private static Logger logger = Logger.getLogger(ViewNewAdvancedReport.class) ;
	
	public ViewNewAdvancedReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
			{
		AdvancedReportForm arf=(AdvancedReportForm) form;
		HttpSession httpSession = request.getSession();

		
		ReportsFilterPicker rfp		= new ReportsFilterPicker();
		ReportsFilterPickerForm rfpForm	= (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
		
		boolean resetSettings = request.getParameter("resetSettings")==null? false : ("true".equals(request.getParameter("resetSettings"))? true : false);
		String lastReportId	= (String)request.getSession().getAttribute("LAST_REPORT_ID") ; 
		String ampReportId 	= request.getParameter("ampReportId");
		if ( request.getParameter("queryEngine") == null || "false".equals(request.getParameter("queryEngine")) ){
			if ( ampReportId != null && lastReportId != null && !ampReportId.equals(lastReportId) )
				 request.getSession().setAttribute(ArConstants.REPORTS_FILTER, null);
		}
		
		if ( ampReportId != null )
			request.getSession().setAttribute("LAST_REPORT_ID", ampReportId);
		
		if ( lastReportId == null || !lastReportId.equals(ampReportId) ) { 
			// if it's the first time we load a report/tab OR if we are loading another report we should reset
			if (rfpForm == null || "reset".equals(request.getParameter("view")) ) {
				// if ampReportId parameter is in the request we need to reset the settings cause a new report was opened
				rfpForm		= new ReportsFilterPickerForm();
				request.setAttribute(ReportWizardAction.REPORT_WIZARD_INIT_ON_FILTERS, "true");
				rfp.modePrepare(mapping, rfpForm, request, response);
				TagUtil.setForm(request, "aimReportsFilterPickerForm", rfpForm, true);
			}	
		}
		

		String loadStatus=request.getParameter("loadstatus");
		Integer progressValue = (httpSession.getAttribute("progressValue") != null) ? (Integer)httpSession.getAttribute("progressValue") :null;
		if(progressValue == null)
			progressValue = 0;

		if(loadStatus != null){
			ServletOutputStream os = response.getOutputStream();
			os.println(progressValue + ","+httpSession.getAttribute("progressTotalRows"));
			os.flush();
			return null;
		}

		progressValue = 0;

		String widget=request.getParameter("widget");
		request.setAttribute("widget",widget);

		String debug=request.getParameter("debug");
		request.setAttribute("debug",debug);

		
		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
		if (tm == null || tm.getTeamId() == null )
				tm = null;
		AmpApplicationSettings ampAppSettings = null;				
		if(tm!=null)				
		ampAppSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
		if(ampAppSettings==null)
			if(tm!=null)
			ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
		
		Integer recordsPerPage = new Integer(100);
		
		if (ampAppSettings != null){
			if( ampAppSettings.getDefaultRecordsPerPage().intValue() != 0){
				recordsPerPage = ampAppSettings.getDefaultRecordsPerPage();
			}else{
				recordsPerPage = Integer.MAX_VALUE;
			}
		}
		//check currency code:
		if(httpSession.getAttribute("reportCurrencyCode")==null) httpSession.setAttribute("reportCurrencyCode",Constants.DEFAULT_CURRENCY);
		
		if(httpSession.getAttribute("reportSorters")==null) httpSession.setAttribute("reportSorters",new HashMap());
		Map sorters = (Map) httpSession.getAttribute("reportSorters");
		//String ampReportId = request.getParameter("ampReportId");
		
		GroupReportData rd = (GroupReportData) httpSession.getAttribute("report");
		AmpReports ar = (AmpReports) httpSession.getAttribute("reportMeta");
		Session session = PersistenceManager.getRequestDBSession();
		String sortBy = request.getParameter("sortBy");
		String sortByAsc = request.getParameter("sortByAsc");
		String applySorter = request.getParameter("applySorter");
		if (ampReportId == null || ampReportId.length() == 0 ) 
			ampReportId = ar.getAmpReportId().toString();
		Long reportId = new Long(ampReportId);
		request.setAttribute("ampReportId",ampReportId);
		
		String startRow = request.getParameter("startRow");
		String endRow = request.getParameter("endRow");
		String cachedStr = request.getParameter("cached");
		
		
		boolean cached=false;
		if (cachedStr != null) cached=Boolean.parseBoolean(cachedStr);
		
		AmpARFilter filter = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if (filter == null || !reportId.equals(filter.getAmpReportId())) {
			if(filter != null && filter.isPublicView())
			{
				//This is to avoid resetting the publicView status to allow the right redirection on Public Views
				filter=new AmpARFilter();
				httpSession.setAttribute(ArConstants.REPORTS_FILTER,filter);
				filter.readRequestData(request);
				String globalCurrency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
				filter.setCurrency(CurrencyUtil.getAmpcurrency(globalCurrency));
				filter.setPublicView(true);
			}
			else
			{
				filter=new AmpARFilter();
				httpSession.setAttribute(ArConstants.REPORTS_FILTER,filter);
				filter.readRequestData(request);
			}
			request.setAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB, "true");
		}

		if (tm !=null && (Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType()) ||
				"Donor".equalsIgnoreCase(tm.getTeamType()))){
			filter.setApproved(true);
			filter.setDraft(true);
		}else{
			filter.setApproved(false);
			filter.setDraft(false);
		}
		if (tm ==null)
		{
			filter.setPublicView(true);
			filter.setApproved(true);
			filter.setDraft(true);
		}
		/**
		 * Checks if the team is a computed workspace and in case it is
		 * it checks if it should hide the draft activities
		 */
		if ( tm != null && tm.getComputation() != null && tm.getComputation() ) {
			Workspace wrksp	= TeamUtil.getWorkspace( tm.getTeamId() );
			if ( wrksp != null && wrksp.getHideDraftActivities() != null && wrksp.getHideDraftActivities() ) {
				filter.setDraft(true);
			}
		}
		
		httpSession.setAttribute("progressValue", ++progressValue); 
		httpSession.setAttribute("progressTotalRows", recordsPerPage);
		
		if (resetSettings && (ampReportId==null || !ampReportId.equals(lastReportId) )){
			filter.setCalendarType(null); //reset the calendar type to take the type from ws settings by default.
			filter.setCurrency(null);
		}
		
		if( (!cached && (applySorter == null && sortBy == null || ar==null)) || 
			(ampReportId != null && ar != null && !ampReportId.equals(ar.getAmpReportId().toString()) )) 
		{
			
			progressValue = progressValue + 20;// 20 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 

			rd=ARUtil.generateReport(mapping, form, request, response);
			progressValue = progressValue + 10;// 20 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 
	
			ar = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));
			if (ar == null) {
				ar = (AmpReports) request.getSession().getAttribute("reportMeta");
			}
			validateColumnsAndHierarchies(ar);
			//This is for public views to avoid nullPointerException due to there is no logged user.
			if(tm != null){
				saveOrUpdateReportLog(tm, ar);
			}
			
			progressValue = progressValue + 3;// 3 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 
		}
		
		/* In case NO sorting info comes in request check to see if sorting has been saved */
		if ( sortBy == null && !cached ) {
			if ( filter.getSortBy() != null ) {
				sortBy	= filter.getSortBy();
				if ( filter.getSortByAsc() !=null  ){
					sortByAsc = filter.getSortByAsc().toString();
				}
			}
		}
		/* If sorting info comes in request save this info in the filter bean */
		else{
				filter.setSortBy(sortBy);
				filter.setSortByAsc( Boolean.parseBoolean(sortByAsc) );
		}
		
		
		if ( applySorter == null && !cached) {
			if ( filter.getHierarchySorters() != null && filter.getHierarchySorters().size() > 0 ) {
				for(String str : filter.getHierarchySorters() ) {
					String [] sortingInfo		= str.split("_");
					sorters.put(Long.parseLong(sortingInfo[0]), new MetaInfo(sortingInfo[1], sortingInfo[2]) );
				}
				rd.importLevelSorters(sorters,ar.getHierarchies().size());
				rd.applyLevelSorter();
			}
		}
		
		
		//test if the request was for hierarchy sorting purposes:
		if(applySorter!=null) {
			if(request.getParameter("levelPicked")!=null && request.getParameter("levelSorter")!=null) {
				sorters.put(Long.parseLong(request.getParameter("levelPicked")),new MetaInfo(request.getParameter("levelSorter"),request.getParameter("levelSortOrder")));
				filter.getHierarchySorters().add(request.getParameter("levelPicked") + "_" + request.getParameter("levelSorter") 
										+ "_" + request.getParameter("levelSortOrder") );
			}
			else{ 	
				sorters.put(Long.parseLong(arf.getLevelPicked()),new MetaInfo(arf.getLevelSorter(),arf.getLevelSortOrder()));
				filter.getHierarchySorters().add( arf.getLevelPicked() + "_" + arf.getLevelSorter() + "_" + arf.getLevelSortOrder() );
			}
			
			rd.importLevelSorters(sorters,ar.getHierarchies().size());
			rd.applyLevelSorter();
						
		}
		
		// test if the request was for column sorting purposes:
		if (sortBy != null) {
			httpSession.setAttribute("sortBy", sortBy);
			rd.setSorterColumn(sortBy);
			Boolean sortAscending		= Boolean.parseBoolean(sortByAsc);
			rd.setSortAscending(sortAscending);
			httpSession.setAttribute(ArConstants.SORT_ASCENDING, sortAscending);
			//if ( sortByAsc != null  && !sortByAsc.equals( rd.getSortAscending()+"" ) )
			//	rd.setSorterColumn(sortBy);
			
			if (applySorter == null && ar.getHierarchies() != null
					&& !ar.getHierarchies().isEmpty()) {
				List<AmountCell> trailCells = rd.getTrailCells();
				for (AmountCell cell : trailCells) {
					if (cell!=null){
					if (sortBy.equals(cell.getColumn().getNamePath())) {
						Set<AmpReportHierarchy> hierarchies = ar.getHierarchies();
						for (AmpReportHierarchy hierarchy : hierarchies) {
							sorters.put(hierarchy.getLevelId(), new MetaInfo(
									cell.getColumn().getAbsoluteColumnName(),
									rd.getSortAscending() ? "ascending": "descending"));
						}
						
						rd.importLevelSorters(sorters, ar.getHierarchies().size());
						rd.applyLevelSorter();
						break;
					}
					}
				}
			}
		}
		
		if (rd==null) return mapping.findForward("index");
		rd.setGlobalHeadingsDisplayed(new Boolean(false));
		
		String viewFormat=request.getParameter("viewFormat");
		if(viewFormat==null) viewFormat=GenericViews.HTML2;
		request.setAttribute("viewFormat",viewFormat);
	
		if(startRow==null && endRow==null) {
		    startRow="0";
		    Integer rpp = recordsPerPage;
		    rpp = rpp - 1; //Zero indexed array 
		    endRow=rpp.toString();
		}
		
//		apply pagination if exists
		boolean pagination=true;
		Cookie[] cookies=request.getCookies();
		if ( cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if ("report_scrolling".equalsIgnoreCase(cookies[i].getName())){
					pagination=false;
				}
			}
		}
		if (!pagination){
			startRow="0";
			endRow=Integer.MAX_VALUE+"";
			recordsPerPage = Integer.MAX_VALUE;
		}
		request.setAttribute("recordsPerPage", recordsPerPage);
		if(startRow!=null) rd.setStartRow(Integer.parseInt(startRow));
		if(endRow!=null) rd.setEndRow(Integer.parseInt(endRow));
		rd.setCurrentRowNumber(0);
		
		rd.computeRowSpan(0, Integer.parseInt(startRow), Integer.parseInt(endRow) );
	
		request.setAttribute("extraTitle",ar.getName());
		rd.setCurrentView(viewFormat);
		Integer numberOfPagesToDisplay = 10;
		if(ampAppSettings != null){
			numberOfPagesToDisplay = ampAppSettings.getNumberOfPagesToDisplay();
		}
		setPaginationOfPagesParameters(request, rd.getVisibleRows(), recordsPerPage, numberOfPagesToDisplay);
		
		// CHANGES FOR AMP-9649 => the siteid and locale are set for translation purposes
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		ar.setSiteId( site.getId().toString() );
		ar.setLocale( navigationLanguage.getCode() );
		
		
		
		httpSession.setAttribute("report",rd);
		httpSession.setAttribute("reportMeta",ar);

		progressValue = progressValue + 10;// 20 is the weight of this process on the progress bar
		httpSession.setAttribute("progressValue", progressValue);
		
		httpSession.setAttribute("progressTotalRows", endRow);
		
		httpSession.setAttribute("progressValue", progressValue);

		
		return mapping.findForward("forward");
	}

	private void setPaginationOfPagesParameters(HttpServletRequest request, Integer totalRows, Integer recordsPerPage, Integer numberOfPagesToDisplay ){
		//Pagination of pages
		if (numberOfPagesToDisplay == null || numberOfPagesToDisplay < 1 ){
			numberOfPagesToDisplay = 10;
		}
		String startRowStr = request.getParameter("startRow");
		Integer startRow = startRowStr == null ? 0 : Integer.parseInt(startRowStr);

		Integer totalPagesRemainder = totalRows % recordsPerPage;
		Integer totalPages = totalPagesRemainder > 0 ? (totalRows / recordsPerPage) + 1 : (totalRows / recordsPerPage);
		
		Integer currentPage = startRow / recordsPerPage; 
		Integer currentPageOfPages = currentPage / numberOfPagesToDisplay;
		Integer startPage = currentPageOfPages * numberOfPagesToDisplay;
		Integer endPage = startPage + numberOfPagesToDisplay;
		if (endPage > totalPages){
			endPage = totalPages;
		}
		Integer startPageRow = startPage * recordsPerPage;
		Integer endPageRow = (endPage * recordsPerPage) - 1;
		if (endPageRow > totalRows){
			endPageRow = totalRows;
		}
		
		request.setAttribute("startPageRow", startPageRow);
		request.setAttribute("endPageRow", endPageRow);
		request.setAttribute("lastPage", totalPages);
	}
	
	private void saveOrUpdateReportLog(TeamMember tm, AmpReports ar) {
		/* In case the report object is not a db object do nothing */
		if ( ar.getAmpReportId() <= 0 )
			return;
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
		AmpReportLog reportlog = DbUtil.getAmpReportLog(ar.getAmpReportId(), ampTeamMember.getAmpTeamMemId());
		if(reportlog!=null){
			reportlog.setLastView(new Date());
			DbUtil.update(reportlog);
		}else{
			reportlog = new AmpReportLog();
			reportlog.setReport(ar);
			reportlog.setMember(ampTeamMember);
			reportlog.setLastView(new Date());
			DbUtil.add(reportlog);				
		}
	}
	
	/**
	 * Checks if all columns in the report are added as 
	 * hierarchies, if it is, then add the column "Project Title".
	 * Also checks if the column "Project Title" is already added,
	 * if it is, then removes it from the hierarchies list.
	 * @param ampReport
	 */
	private void validateColumnsAndHierarchies (AmpReports ampReport){
		AdvancedReportUtil.removeDuplicatedColumns(ampReport);
		Collection<AmpColumns> availableCols	= AdvancedReportUtil.getColumnList();
		AmpCategoryValue level1		= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
		if (ampReport.getColumns().size() == ampReport.getHierarchies().size()) {
			for ( AmpColumns tempCol: availableCols ) {
				if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ) {
					if (!AdvancedReportUtil.isColumnAdded(ampReport.getColumns(), ArConstants.COLUMN_PROJECT_TITLE)) {
						AmpReportColumn titleCol= new AmpReportColumn();
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
