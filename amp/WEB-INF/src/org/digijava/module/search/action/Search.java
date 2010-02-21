package org.digijava.module.search.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.search.form.SearchForm;
import org.digijava.module.search.util.SearchUtil;

public class Search extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		SearchForm searchForm = (SearchForm) form;
		HttpSession session = request.getSession();
		ServletContext ampContext = session.getServletContext();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		

		if (request.getParameter("reset") != null
				&& ((String) request.getParameter("reset"))
						.equalsIgnoreCase("true")) {
			searchForm.setKeyword("");
			searchForm.setQueryType(-1);
			searchForm.setResultsPerPage(-1L);
		} else {
			// If it's a tab we need to redirect
			if (request.getParameter("ampReportId") != null) {
				Long ampReportId = Long.parseLong(request
						.getParameter("ampReportId"));
				
				if (ampReportId != null) {

					AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));
					//Using the filter facility to show the tab in the desktop
					session.setAttribute("filterCurrentReport", rep);
					return mapping.findForward("redirectTab");
				}
			}

			if (request.getParameter("ampActivityId") != null) {
				Long ampActivityId = Long.parseLong(request
						.getParameter("ampActivityId"));
				
				if (ampActivityId != null) {
					ActionForward forward = mapping.findForward("redirectActivity");
					session.setAttribute("returnSearch", true);
					return new ActionForward(forward.getPath() + "?ampActivityId=" + ampActivityId);
				}
			}

			session.removeAttribute("returnSearch");

			Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();
			Collection<LoggerIdentifiable> resultActivities = new ArrayList<LoggerIdentifiable>();
			Collection<LoggerIdentifiable> resultReports = new ArrayList<LoggerIdentifiable>();
			Collection<LoggerIdentifiable> resultTabs = new ArrayList<LoggerIdentifiable>();
			Collection<LoggerIdentifiable> resultResources = new ArrayList<LoggerIdentifiable>();
			
			int dateSelection = searchForm.getDateSelection();
			String byDateSql = "";
			
			Date fromDate = null;
			Date toDate = null;
			
			if (searchForm.isSearchByDate() && searchForm.getFromDate()!=null && searchForm.getToDate()!=null){
				//if we want to filter by dates
				fromDate = DateTimeUtil.parseDate(searchForm.getFromDate());
				toDate = DateTimeUtil.parseDate(searchForm.getToDate());
				
			
				SimpleDateFormat  dateForMysql = new SimpleDateFormat("yyyy-MM-dd");	
				
			if (dateSelection==1 && searchForm.isSearchByDate()) {
					byDateSql = " AND (date_updated between '"+dateForMysql.format(fromDate)+" 00:00:00' and '"+dateForMysql.format(toDate)+" 23:59:59')";
				} else if (dateSelection==2  && searchForm.isSearchByDate()) {
					byDateSql = " AND (date_created between '"+dateForMysql.format(fromDate)+" 00:00:00' and '"+dateForMysql.format(toDate)+" 23:59:59')";
				} else {
					byDateSql="";
				} 
			}

			
			
			
			switch (searchForm.getQueryType()) {
			case SearchUtil.QUERY_ALL:
				resultActivities = SearchUtil.getActivities(searchForm.getKeyword(), searchForm.getActSearchKey(), request, tm, null);
				resultReports = SearchUtil.getReports(tm, searchForm.getKeyword(), null, null);
				resultTabs = SearchUtil.getTabs(tm, searchForm.getKeyword(), null, null);
				resultResources = SearchUtil.getResources(searchForm.getKeyword(), request, tm);
				break;
			case SearchUtil.ACTIVITIES:
				resultActivities = SearchUtil.getActivities(searchForm.getKeyword(), searchForm.getActSearchKey(),request, tm, byDateSql);
				break;
			case SearchUtil.REPORTS:
				resultReports = SearchUtil.getReports(tm, searchForm.getKeyword(),fromDate,toDate);
				break;
			case SearchUtil.TABS:
				resultTabs = SearchUtil.getTabs(tm, searchForm.getKeyword(), fromDate, toDate);
				break;
			case SearchUtil.RESOURCES:
				resultResources = SearchUtil.getResources(searchForm
						.getKeyword(), request, tm);
				break;
			}

			if (searchForm.getKeyword() != "") {
				resultList.addAll(resultActivities);
				resultList.addAll(resultReports);
				resultList.addAll(resultTabs);
				resultList.addAll(resultResources);
			}

			if (resultList.size() > 0
					|| searchForm.getQueryType() != SearchUtil.QUERY_ALL)
				request.setAttribute("resultList", resultList);
			if (resultActivities.size() > 0
					|| searchForm.getQueryType() == SearchUtil.ACTIVITIES)
				request.setAttribute("resultActivities", resultActivities);
			if (resultReports.size() > 0
					|| searchForm.getQueryType() == SearchUtil.REPORTS)
				request.setAttribute("resultReports", resultReports);
			if (resultTabs.size() > 0
					|| searchForm.getQueryType() == SearchUtil.TABS)
				request.setAttribute("resultTabs", resultTabs);
			if (resultResources.size() > 0
					|| searchForm.getQueryType() == SearchUtil.RESOURCES)
				request.setAttribute("resultResources", resultResources);
		}
		// TODO: searching documents
		
		searchForm.setSearchByDate(false);
		searchForm.setFromDate(null);
		searchForm.setToDate(null);
		return mapping.findForward("forward");
	}

}
