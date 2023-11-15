package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;

public class GetTeamReports extends Action {

    private static Logger logger = Logger.getLogger(GetTeamReports.class);
        private final static int FIRST_PAGE = 1;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In get reports activities");

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

        ReportsForm raForm = (ReportsForm) form;
        if (raForm.getCurrentPage() == 0) {
          raForm.setCurrentPage(FIRST_PAGE);
        }
        
        Boolean tabs = null; 
        if(mapping.getParameter().equals("reportList")){
            raForm.setShowReportList(true);
            tabs = false;
        }else{
            // mapping.getParameter().equals("desktopTabList")
            raForm.setShowReportList(false);
            tabs = true;
        }

        Long id = null;
        int defReportsPerPage=0;

        if (request.getParameter("id") != null) {
            id = Long.parseLong(request.getParameter("id"));
        } else if (request.getAttribute("teamId") != null) {
            id = (Long) request.getAttribute("teamId");
        } else if (session.getAttribute("currentMember") != null) {
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");

            if (tm.getAppSettings() != null) {
                id = tm.getTeamId();

                if (tm.getAppSettings().getDefReportsPerPage() != 0) {
                    raForm.setDefReportsPerPage(tm.getAppSettings().getDefReportsPerPage());
                }

                String reset = request.getParameter("reset");
                String tempNumResultsParam = request.getParameter("tempNumResults");

                raForm.setDefaultNumResults(tm.getAppSettings().getDefReportsPerPage());

                if (reset != null && reset.equalsIgnoreCase("true")) {
                    //raForm.setTempNumResults(-1);
                    defReportsPerPage = tm.getAppSettings().getDefReportsPerPage();
                    tempNumResultsParam = "" + defReportsPerPage;
                    raForm.setTempNumResults(defReportsPerPage == 0 ? -1 : defReportsPerPage);
                    raForm.setKeyword(null);
                }

                if (raForm.getTempNumResults() != -1) {
                    defReportsPerPage = raForm.getTempNumResults();
                }

                if (tempNumResultsParam != null && tempNumResultsParam.length() > 0) {
                    try {
                        defReportsPerPage = Integer.parseInt(tempNumResultsParam);
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }

        if (id != null) {
            
            AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
            Double totalPages = null;
            Collection col = null;
            if (defReportsPerPage > 0) {
                int curPage=raForm.getCurrentPage() - 1;
                col = TeamUtil.getTeamReportsCollection(id, curPage * defReportsPerPage, defReportsPerPage, tabs, raForm.getKeyword());
                int size = TeamUtil.getTeamReportsCollectionSize(id,tabs,raForm.getKeyword());
                totalPages = Math.ceil(1.0*size/defReportsPerPage);
            } else {
                col= TeamUtil.getTeamReportsCollection(id,tabs,raForm.getKeyword());
                totalPages = (double) FIRST_PAGE;
            }

            raForm.setTotalPages(totalPages.intValue());
            raForm.setReports(col);
            raForm.setTeamId(id);
            raForm.setTeamName(ampTeam.getName());          
            return mapping.findForward("forward");
        } else {
            return null;
        }
    }
}

