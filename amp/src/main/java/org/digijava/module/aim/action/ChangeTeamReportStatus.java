package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeTeamReportStatus extends Action {

    private static Logger logger = Logger
            .getLogger(ChangeTeamReportStatus.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;   
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }       

        SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
        String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                request.getServerPort(), request.getContextPath());

        if (request.getParameter("status") == null
                || request.getParameter("id") == null) {
            url += "/aim/viewMyDesktop.do";
            response.sendRedirect(url);
        }

        Long reportId = null;

        try {
            reportId = new Long(Long.parseLong(request.getParameter("id")));
        } catch (Exception e) {
            url += "/aim/viewMyDesktop.do";
            response.sendRedirect(url);
        }

        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        Long teamId = tm.getTeamId();
        String status = request.getParameter("status");
        AmpTeamReports teamReports = TeamUtil.getAmpTeamReport(teamId, reportId);

        if (teamReports == null) {
            // error. No report with id reportId exist for the team with id
            // teamId
            // Do handle it
        }

        if (status.equals("team")) {
            teamReports.setTeamView(true);
        } else if (status.equals("member")) {
            teamReports.setTeamView(false);
        }

        DbUtil.update(teamReports);

        String returnPage = null;
        if("teamReportList".equals(request.getParameter("returnPage"))){
            returnPage = "/aim/teamReportList.do";
        }else{ //if("teamDesktopTabList".equals(request.getParameter("returnPage"))){
            returnPage = "/aim/teamDesktopTabList.do";
        }

        String tempNumResultsParam = request.getParameter("tempNumResults");
        if (tempNumResultsParam != null && tempNumResultsParam.length() > 0) {
            returnPage += "~tempNumResults=" + tempNumResultsParam;
        }
        String currentPage = request.getParameter("currentPage");
        if (currentPage != null && currentPage.length() > 0) {
            returnPage += "~currentPage=" + currentPage;
        }
        url += returnPage;
        response.sendRedirect(url);

        return null;
    }
}
