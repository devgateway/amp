package org.digijava.module.search.action;

import java.util.ArrayList;
import java.util.Collection;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.form.SearchForm;
import org.digijava.module.search.util.SearchUtil;
import org.digijava.module.aim.helper.Constants;

public class Search extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        SearchForm searchForm = (SearchForm) form;
        HttpSession session = request.getSession();
        ServletContext ampContext = session.getServletContext();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");

        if (request.getParameter("desksearch") != null){
            String keyword = request.getParameter("keyword");
            int querytype = Integer.parseInt(request.getParameter("type"));
            searchForm.setKeyword(keyword);
            searchForm.setQueryType(querytype);
        }
        
        if (request.getParameter("reset") != null
                && ((String) request.getParameter("reset"))
                        .equalsIgnoreCase("true")) {
            searchForm.setKeyword("");
            searchForm.setQueryType(-1);
            searchForm.setResultsPerPage(-1L);
            searchForm.setSearchMode(0);
        } else {
            // If it's a tab we need to redirect
            if (request.getParameter("ampReportId") != null) {
                String ampReportParam =  request.getParameter("ampReportId");
                Long ampReportId = Long.parseLong(ampReportParam);
                
                if (ampReportId != null) {

                    AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));
                    session.setAttribute(Constants.CURRENT_TAB_REPORT, rep);
                    if (rep.getDrilldownTab())
                    {
                        return mapping.findForward("redirectTab");
                    }
                    else
                    {
                        return mapping.findForward("redirectReport");
                    }
                }
            }

            /*if (request.getParameter("ampActivityId") != null) {
                Long ampActivityId = Long.parseLong(request
                        .getParameter("ampActivityId"));
                
                if (ampActivityId != null) {
                    ActionForward forward = mapping.findForward("redirectActivity");
                    session.setAttribute("returnSearch", true);
                    return new ActionForward(forward.getPath() + "?activityId=" + ampActivityId,true);
                }
            }*/

            session.removeAttribute("returnSearch");
            if(tm != null){
                Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultActivities = new ArrayList<LoggerIdentifiable>();
                Collection<? extends LoggerIdentifiable> resultReports = new ArrayList<LoggerIdentifiable>();
                Collection<? extends LoggerIdentifiable> resultTabs = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultResources = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultActivitiesWithRespOrgs = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultActivitiesWithExeOrgs = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultActivitiesWithImpOrgs = new ArrayList<LoggerIdentifiable>();
                Collection<LoggerIdentifiable> resultPledges = new ArrayList<LoggerIdentifiable>();
    
                switch (searchForm.getQueryType()) {
                case SearchUtil.QUERY_ALL:
                    resultActivities = SearchUtil.getActivities(searchForm
                            .getKeyword(), request, tm);
                    resultPledges = SearchUtil.getPledges(searchForm.getKeyword(), request);
                    resultReports = SearchUtil.getReports(tm, searchForm
                            .getKeyword());
                    resultTabs = SearchUtil.getTabs(tm, searchForm.getKeyword());
                    resultResources = SearchUtil.getResources(searchForm
                            .getKeyword(), request, tm);
                    if ( FeaturesUtil.isVisibleField("Search Feature - Responsible Organization")) {
                        resultActivitiesWithRespOrgs.addAll(SearchUtil
                                .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                        tm, Constants.ROLE_CODE_RESPONSIBLE_ORG));
                    }
                    if ( FeaturesUtil.isVisibleField("Search Feature - Executing Agency")) {
                            resultActivitiesWithExeOrgs.addAll(SearchUtil
                                    .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                            tm, Constants.ROLE_CODE_EXECUTING_AGENCY));
                    }
                    if ( FeaturesUtil.isVisibleField("Search Feature - Implementing Agency")) {
                                resultActivitiesWithImpOrgs.addAll(SearchUtil
                                        .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                                tm, Constants.ROLE_CODE_IMPLEMENTING_AGENCY));
                    }
                    break;
                case SearchUtil.ACTIVITIES:
                    resultActivities = SearchUtil.getActivities(searchForm
                            .getKeyword(), request, tm);
                    break;
                case SearchUtil.PLEDGE:
                    resultPledges = SearchUtil.getPledges(searchForm
                            .getKeyword(), request);
                    break;                  
                case SearchUtil.REPORTS:
                    resultReports = SearchUtil.getReports(tm, searchForm
                            .getKeyword());
                    break;
                case SearchUtil.TABS:
                    resultTabs = SearchUtil.getTabs(tm, searchForm.getKeyword());
                    break;
                case SearchUtil.RESOURCES:
                    resultResources = SearchUtil.getResources(searchForm
                            .getKeyword(), request, tm);
                    break;
                case SearchUtil.RESPONSIBLE_ORGANIZATION:
                    resultActivitiesWithRespOrgs = SearchUtil
                            .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                    tm, Constants.ROLE_CODE_RESPONSIBLE_ORG);
                    break;
                case SearchUtil.EXECUTING_AGENCY:
                    resultActivitiesWithExeOrgs = SearchUtil
                            .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                    tm, Constants.ROLE_CODE_EXECUTING_AGENCY);
                    break;
                case SearchUtil.IMPLEMENTING_AGENCY:
                    resultActivitiesWithImpOrgs = SearchUtil
                            .getActivitiesUsingRelatedOrgs(searchForm.getKeyword(),
                                    tm, Constants.ROLE_CODE_IMPLEMENTING_AGENCY);
                    break;
                    
                }
    
                if (! searchForm.getKeyword().equals("")) {
                    resultList.addAll(resultActivities);
                    resultList.addAll(resultPledges);
                    resultList.addAll(resultReports);
                    resultList.addAll(resultTabs);
                    resultList.addAll(resultResources);
                    resultList.addAll(resultActivitiesWithRespOrgs);
                    resultList.addAll(resultActivitiesWithExeOrgs);
                    resultList.addAll(resultActivitiesWithImpOrgs);
                }
    
                if (resultList.size() > 0
                        || searchForm.getQueryType() != SearchUtil.QUERY_ALL)
                    request.setAttribute("resultList", resultList);
                if (resultActivities.size() > 0
                        || searchForm.getQueryType() == SearchUtil.ACTIVITIES)
                    request.setAttribute("resultActivities", resultActivities);
                if (resultPledges.size() > 0
                        || searchForm.getQueryType() == SearchUtil.PLEDGE)
                    request.setAttribute("resultPledges", resultPledges);               
                if (resultReports.size() > 0
                        || searchForm.getQueryType() == SearchUtil.REPORTS)
                    request.setAttribute("resultReports", resultReports);
                if (resultTabs.size() > 0
                        || searchForm.getQueryType() == SearchUtil.TABS)
                    request.setAttribute("resultTabs", resultTabs);
                if (resultResources.size() > 0
                        || searchForm.getQueryType() == SearchUtil.RESOURCES)
                    request.setAttribute("resultResources", resultResources);
                if (resultActivitiesWithRespOrgs.size() > 0
                        || searchForm.getQueryType() == SearchUtil.RESPONSIBLE_ORGANIZATION)
                    request.setAttribute("resultActivitiesWithRespOrgs",
                            resultActivitiesWithRespOrgs);
                if (resultActivitiesWithExeOrgs.size() > 0
                        || searchForm.getQueryType() == SearchUtil.EXECUTING_AGENCY)
                    request.setAttribute("resultActivitiesWithExeOrgs",
                            resultActivitiesWithExeOrgs);
                if (resultActivitiesWithImpOrgs.size() > 0
                        || searchForm.getQueryType() == SearchUtil.IMPLEMENTING_AGENCY)
                    request.setAttribute("resultActivitiesWithImpOrgs",
                            resultActivitiesWithImpOrgs);
            }
        }
        // TODO: searching documents

        return mapping.findForward("forward");
    }

}
