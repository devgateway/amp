package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamReports extends Action {

    private static Logger logger = Logger.getLogger(UpdateTeamReports.class);
    private final static int FIRST_PAGE = 1;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In update team reports");

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

        Long id = null;
        TeamMember tm = null;

        boolean tabs = !raForm.isShowReportList();
        
        if (session.getAttribute("currentMember") != null) {
            tm = (TeamMember) session.getAttribute("currentMember");
            id = tm.getTeamId();
        }
        if (raForm.getRemoveReports() != null) {
            /* remove all selected reports */
            Long selReports[] = raForm.getSelReports();
            DbUtil.removeTeamReports(selReports,tm.getTeamId());
            raForm.setRemoveReports(null);

            /** setting the addReport property so that the system
             * shows you the unassigned report list
             */
            raForm.setAddReport(null);
            if(!raForm.isShowReportList())
                return mapping.findForward("forwardDesktopTabList");
            return mapping.findForward("forward");
        }
        if (raForm.getAddReport() != null) {
            /* show all unassigned reports */
            int defReportsPerPage=0;
            if(tm.getAppSettings()!=null&&tm.getAppSettings().getDefReportsPerPage()!=0){
                defReportsPerPage=tm.getAppSettings().getDefReportsPerPage();
            }
            
            String reset = request.getParameter("reset");
            
            if(reset!=null && reset.equalsIgnoreCase("true")){
                //raForm.setTempNumResults(-1);
                if (defReportsPerPage!=0)
                    {
                    raForm.setTempNumResults(defReportsPerPage);
                    }
                raForm.setKeyword(null);
            }
            defReportsPerPage = raForm.getTempNumResults();
            
            
            Collection col = null;
            Double totalPages=null;
            
            col = TeamUtil.getAllUnassignedTeamReports(id, tabs);
            if(raForm.getKeyword() !=null && raForm.getKeyword().length()>0){
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    AmpReports report = (AmpReports) iterator.next();
                    if(!report.getName().toLowerCase().contains(raForm.getKeyword().toLowerCase())){
                        iterator.remove();
                    }                   
                }
            }
            
            if(defReportsPerPage!=0){
                int curPage=raForm.getCurrentPage()-1;              
                int size=col.size();
                int fromIndex = curPage*defReportsPerPage;
                int toIndex =fromIndex + defReportsPerPage;
                if(toIndex > col.size()){
                    toIndex = col.size();
                }
                if(fromIndex <= toIndex) {
                   col = ((List)col).subList(fromIndex, toIndex);
                   totalPages=Math.ceil(1.0*size/defReportsPerPage);
                }
                else
                    totalPages=new Double(FIRST_PAGE);
            }else{
                  //  col= TeamUtil.getAllUnassignedTeamReports(id, tabs);
                    totalPages=new Double(FIRST_PAGE);
            }
            
            raForm.setTotalPages(totalPages.intValue());
            raForm.setReports(col);
            raForm.setTeamId(tm.getTeamId());
            raForm.setAddReport(null);
            return mapping.findForward("showAddReport");
        }
        else if (raForm.getAssignReports() != null) {
            /* add the selected reports to the team list */

            logger.info("in assign reports");
            Long selReports[] = raForm.getSelReports();
            DbUtil.addTeamReports(selReports,tm.getTeamId(),tm.getMemberId());
            raForm.setAssignReports(null);

            if(!raForm.isShowReportList())
                return mapping.findForward("forwardDesktopTabList");
            return mapping.findForward("forward");
        } else {
            return mapping.findForward(null);
        }
    }

    public boolean canDelete(Long repId) {
        logger.debug("In can delete");
        Iterator itr = DbUtil.getMembersUsingReport(repId).iterator();
        if (itr.hasNext()) {
            logger.debug("return false");
            return false;
        } else {
            logger.debug("return true");
            return true;
        }

    }
}

