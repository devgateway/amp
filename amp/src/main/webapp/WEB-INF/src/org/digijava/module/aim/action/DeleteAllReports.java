package org.digijava.module.aim.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;


public class DeleteAllReports extends Action {

      private static Logger logger = Logger.getLogger(DeleteAllReports.class);

      public ActionForward execute(ActionMapping mapping,
                            ActionForm form,
                            HttpServletRequest request,
                            HttpServletResponse response) throws java.lang.Exception {
            
            ReportsForm repForm = (ReportsForm) form;
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            AmpReports ampReport=null;
            boolean isTab           = false; 
            ActionMessages errors = new ActionMessages();
            //logger.info("In delete reports11111111");
            boolean lead = false;
                if (session.getAttribute("teamLeadFlag") == null) {
                    logger.info("not a team Lead!");
                    //return mapping.findForward("forward");
                } else {
                    String str = (String)session.getAttribute("teamLeadFlag");
                    logger.info("teamFlag ..... "+session.getAttribute("teamLeadFlag")+ " strrrr "+str);
                    if (str.equals("true")) {
                        logger.info("yes a team Lead!");
                        lead=true;
                        //return mapping.findForward("forward");
                    }
                    else
                    {
                        logger.info("NOPE!! team Lead!");
                    }
                }
                
                 String a = request.getParameter("rid");
                 logger.info(" this is rid...."+request.getParameter("rid")+" thisisa...."+a);
                 if (a != null){
                     Long id = new Long(a);
                    ampReport=DbUtil.getAmpReport(id);
                    if (ampReport!=null){
                        isTab       = (ampReport.getDrilldownTab()!=null) && ampReport.getDrilldownTab();
                        if(lead||(ampReport.getOwnerId()!=null && tm.getMemberId().equals(ampReport.getOwnerId().getAmpTeamMemId())))
                         {                          
                             logger.info("In delete reports");                           
                                 ReportsForm ampReportForm = new ReportsForm();                              
                                 ampReportForm.setReportId(id);
                                 //log the delete action for a report
                                 //AmpReports aReportForLog=DbUtil.getAmpReport(ampReportForm.getReportId());
                                 AuditLoggerUtil.logObject(session,request,ampReport,"delete");
                                 DbUtil.updateAppSettingsReportDeleted(ampReportForm.getReportId());
                                 removeFromSession(request.getSession(), ampReportForm.getReportId());
                                 boolean deleted = AdvancedReportUtil.deleteReportsCompletely(ampReportForm.getReportId());                                      
                                         if (deleted) {
                                             if (request.getParameter("isTab") != null) {
                                                 if(request.getParameter("isTab").equals("1")){
                                                     errors.add("title", new ActionMessage("error.aim.deleteTabs.tabDeleted", TranslatorWorker.translateText("Tab Deleted")));
                                                     saveErrors(request,errors);                                
                                                     logger.debug("Tab deleted"); 
                                                 } else {
                                                     errors.add("title", new ActionMessage("error.aim.deleteReports.reportDeleted", TranslatorWorker.translateText("Report Deleted")));
                                                     saveErrors(request,errors);                                
                                                     logger.debug("Report deleted");
                                                 } 
                                             } else { 
                                                 
                                             }
                                         }                                              
                         }
                      }
                 }else {
                     errors.add("title", new ActionMessage(
                        "error.aim.deleteReports.reportNotDeleted", TranslatorWorker.translateText("Report Not Deleted")));
                    saveErrors(request,errors);
        
                    logger.debug("Report could not be deleted! ");
                 }
                 if (isTab)
                     return mapping.findForward("forwardTabs");
                 else
                     return mapping.findForward("forwardReports");
      }
      
      public void removeFromSession(HttpSession session, Long reportId) {
         AmpReports defaultReport       = (AmpReports)session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
         AmpReports filterCurrentRep    = (AmpReports)session.getAttribute(Constants.CURRENT_TAB_REPORT);
         List myReports                 = (List)session.getAttribute(Constants.MY_REPORTS);
         TeamMember tm                  = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
         
         
         if (defaultReport != null && defaultReport.getAmpReportId().equals(reportId)) {
             session.setAttribute(Constants.DEFAULT_TEAM_REPORT, null);
         }
         
         if ( filterCurrentRep != null && filterCurrentRep.getAmpReportId().equals(reportId) ) {
             session.setAttribute(Constants.CURRENT_TAB_REPORT, null);
         }
         if ( tm!=null && tm.getAppSettings() != null) {
             AmpReports defTmReport = tm.getAppSettings().getDefaultAmpReport();
             if ( defTmReport!=null && defTmReport.getAmpReportId().equals(reportId) ) {
                 //tm.getAppSettings().setDefaultAmpReport(null);
             }
         }
         
         
         if (myReports != null) {
             Iterator iter  = myReports.iterator();
             while (iter.hasNext()) {
                 AmpReports rep     = (AmpReports) iter.next();
                 if ( rep.getAmpReportId().equals(reportId) ) 
                        iter.remove();
             }
         }
         
      }
}


