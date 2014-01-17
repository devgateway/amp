/**
 * ViewNewAdvancedReport.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;


import java.awt.image.renderable.RenderContext;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.utils.BoundedList;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpOrganisation;
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
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Query;
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
		String loadStatus = request.getParameter("loadstatus");

		if (loadStatus != null){
			// if AJAX query to get progress -> fast exit
			int progressValue = ReportContextData.getFromRequest().getProgressValue();
			ServletOutputStream os = response.getOutputStream();
			os.println(progressValue + "," + ReportContextData.getFromRequest().getProgressTotalRows());
			os.flush();
			return null;
		}
		
		AdvancedReportForm arf=(AdvancedReportForm) form;
		
		String ampReportId 	= request.getParameter("ampReportId");
		String cachedStr = request.getParameter("cached");
		String sortBy = request.getParameter("sortBy");
		String sortByAsc = request.getParameter("sortByAsc");
		String applySorter = request.getParameter("applySorter");

		boolean cached = false;
		if (cachedStr != null)
			cached = Boolean.parseBoolean(cachedStr);		

		boolean shouldRegenerateReport = (!cached && (applySorter == null && sortBy == null || ReportContextData.getFromRequest().getReportMeta() == null));
			
		if (shouldRegenerateReport)
		{
			//ReportContextData.cleanContextData();
			ReportContextData.cleanCurrentReportCaches();
			
			if ( ampReportId != null )
			{
				if (request.getSession().getAttribute(Constants.LAST_VIEWED_REPORTS) == null)
				{
					Comparator<AmpReports> ampReportsComparator = new Comparator<AmpReports>()
					{
						public int compare(AmpReports a, AmpReports b)
						{
							return a.getAmpReportId().compareTo(b.getAmpReportId());
						}
					};
					request.getSession().setAttribute(Constants.LAST_VIEWED_REPORTS, new BoundedList<AmpReports>(5, ampReportsComparator));
				}
				
				BoundedList<AmpReports> bList = (BoundedList<AmpReports>) request.getSession().getAttribute(Constants.LAST_VIEWED_REPORTS);
				if (AmpMath.isLong(ampReportId))
				{
					AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, Long.parseLong(ampReportId));
					if ((report != null) && (!report.getDrilldownTab()))
						bList.add(report);
				}
			}
		}

		request.setAttribute("widget", request.getParameter("widget"));
		request.setAttribute("debug", request.getParameter("debug"));
		
		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
		if (tm == null || tm.getTeamId() == null )
				tm = null;
		
		AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();				
		
		Integer recordsPerPage = new Integer(100);
		
		if (ampAppSettings != null){
			if( ampAppSettings.getDefaultRecordsPerPage().intValue() != 0){
				recordsPerPage = ampAppSettings.getDefaultRecordsPerPage();
			}else{
				recordsPerPage = Integer.MAX_VALUE;
			}
		}
		//check currency code:
		
		Map<Long, MetaInfo<String>> sorters = ReportContextData.getFromRequest(true).getReportSorters(true);
		//String ampReportId = request.getParameter("ampReportId");
		
		GroupReportData rd = ReportContextData.getFromRequest().getGeneratedReport();
		AmpReports ar;// =  ReportContextData.getFromRequest().getReportMeta();
		Session session = PersistenceManager.getRequestDBSession();
		
		request.setAttribute("ampReportId", ampReportId);
		
		String startRow = request.getParameter("startRow");
		String endRow = request.getParameter("endRow");
						
		AmpReports report = null;
	    try {
	    	report = ARUtil.getReferenceToReport();
	    } catch (Exception e) {
	    	response.setContentType("text/html");	
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String url = "/";
    		String alert = TranslatorWorker.translateText("Cant find report, please check if it was deleted.");
    		String script = "<script>" 
    			+ "alert('"+ alert +"');" 
    			+ "window.location=('"+ url +"');"
    			+ "</script>";
    		out.println(script);
			out.close();	
			outputStream.close();
			return null;
	    }
		
		// should we reload filters from the DB?
		boolean resetSettings = "true".equals(request.getParameter("resetSettings"));// || (!"true".equals(request.getParameter("cached")));
		AmpARFilter filter = ReportContextData.getFromRequest().loadOrCreateFilter(resetSettings, report);
		
		if (tm ==null)
		{
			filter.setPublicView(true);
		}
		
		int progressValue = 0;
		
		ReportContextData.getFromRequest().setProgressValue(++progressValue);
		ReportContextData.getFromRequest().setProgressTotalRows(recordsPerPage);
		
		if (resetSettings && (ampReportId==null/* || !ampReportId.equals(lastReportId) */)){
			//BOZO: isn't this "reset done wrong"?
			filter.setCalendarType(null); //reset the calendar type to take the type from ws settings by default.
			filter.setCurrency(null);
		}
		
		if (shouldRegenerateReport) 
		{
			progressValue = progressValue + 20;// 20 is the weight of this process on the progress bar
			ReportContextData.getFromRequest().setProgressValue(progressValue);

			rd = ARUtil.generateReport(report, filter, true, true);
			progressValue = progressValue + 10;// 20 is the weight of this process on the progress bar
			ReportContextData.getFromRequest().setProgressValue(progressValue);
	
			ar = null;
			if (ampReportId != null)
			{
				ar = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));
			}
			
			if (ar == null) {
				ar = ReportContextData.getFromRequest().getReportMeta();
			}
			ar.validateColumnsAndHierarchies();
			//This is for public views to avoid nullPointerException due to there is no logged user.
			if(tm != null){
				saveOrUpdateReportLog(tm, ar);
			}
			
			progressValue = progressValue + 3;// 3 is the weight of this process on the progress bar
			ReportContextData.getFromRequest().setProgressValue(progressValue);
		} else ar = ReportContextData.getFromRequest().getReportMeta();
		
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
		else if (sortBy != null){
				filter.setSortBy(sortBy);
				filter.setSortByAsc( Boolean.parseBoolean(sortByAsc) );
		}
		
		
		if ( applySorter == null && !cached) {
			if ( filter.getHierarchySorters() != null && filter.getHierarchySorters().size() > 0 ) {
				for(String str : filter.getHierarchySorters() ) {
					String [] sortingInfo		= str.split("_");
					sorters.put(Long.parseLong(sortingInfo[0]), new MetaInfo<String>(sortingInfo[1], sortingInfo[2]) );
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
			Boolean sortAscending = Boolean.parseBoolean(sortByAsc);
			
			rd.setSorterColumn(sortBy);
			rd.setSortAscending(sortAscending);
			
			ReportContextData.getFromRequest().setSortBy(sortBy);
			ReportContextData.getFromRequest().setSortAscending(sortAscending);
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
		
		if (rd==null) return mapping.findForward("index"); //BOZO: why would rd = null here?
		rd.setGlobalHeadingsDisplayed(new Boolean(false));
		rd.calculateReportHeadings();
		
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
			startRow = "0";
			endRow = Integer.toString(Integer.MAX_VALUE);
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
		ar.setSiteId( site.getId());
		ar.setLocale( navigationLanguage.getCode() );
		
		
		
		ReportContextData.getFromRequest().setGeneratedReport(rd);
		ReportContextData.getFromRequest().setReportMeta(ar);

		progressValue = progressValue + 10;// 10 is the weight of this process on the progress bar
		ReportContextData.getFromRequest().setProgressValue(progressValue);
		
		ReportContextData.getFromRequest().setProgressTotalRows(endRow == null ? null : Integer.parseInt(endRow));
				
		return mapping.findForward("forward");
	}

	static void setPaginationOfPagesParameters(HttpServletRequest request, Integer totalRows, Integer recordsPerPage, Integer numberOfPagesToDisplay ){
		//Pagination of pages
		if (numberOfPagesToDisplay == null || numberOfPagesToDisplay < 1 ){
			numberOfPagesToDisplay = 10;
		}
		String startRowStr = request.getParameter("startRow");
		Integer startRow = startRowStr == null ? 0 : Integer.parseInt(startRowStr);

		Integer totalPagesRemainder = totalRows % recordsPerPage;
		Integer totalPages = totalPagesRemainder > 0 ? (totalRows / recordsPerPage) + 1 : (totalRows / recordsPerPage);
		
		Integer currentPage = startRow / recordsPerPage;
		int startPage = Math.max(0, currentPage - numberOfPagesToDisplay / 2);
		int endPage = startPage + numberOfPagesToDisplay-1;//= Math.min(totalPages, currentPage + numberOfPagesToDisplay / 2 + numberOfPagesToDisplay % 2);
		if (endPage >= totalPages){
			endPage = totalPages-1;
			startPage = Math.max(0,  endPage - numberOfPagesToDisplay+1);
		}
			
		if (endPage + 1 - startPage <= numberOfPagesToDisplay) // if we are pegged, e.g. 10 pages allowed, showing 7...10 or 0..3
		{
			if (startPage == 0) // case 0...3 -> show the first numberOfPages pages
				endPage = Math.min(numberOfPagesToDisplay-1, totalPages);
			else
				if (endPage == totalPages) // case 7..10 -> show the last numberOfPages pages
					startPage = Math.max(0, endPage  - numberOfPagesToDisplay);
				
		}
		/*Integer currentPageOfPages = currentPage / numberOfPagesToDisplay;
		Integer startPage = currentPageOfPages * numberOfPagesToDisplay;
		Integer endPage = startPage + numberOfPagesToDisplay;
		if (endPage > totalPages){
			endPage = totalPages;
		}*/
		Integer startPageRow = startPage * recordsPerPage;
		Integer endPageRow = (endPage * recordsPerPage);
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
		
}
