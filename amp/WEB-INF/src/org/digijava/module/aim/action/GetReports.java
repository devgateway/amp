package org.digijava.module.aim.action ;

import java.util.Iterator;

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
import org.digijava.module.aim.util.DbUtil;

public class GetReports extends Action {

          private static Logger logger = Logger.getLogger(GetReports.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }                    
                     
                     ReportsForm repForm = (ReportsForm) form;

                     logger.debug("In get reports");
                     String action = request.getParameter("action");
    
                     if (request.getParameter("id") != null) {
                                
                                Long repId = new Long(Long.parseLong(request.getParameter("id")));
                                AmpReports ampReports = DbUtil.getAmpReport(repId);
                                repForm.setReportId(ampReports.getAmpReportId());
                                repForm.setName(ampReports.getName());
                                repForm.setDescription(ampReports.getDescription());
                                logger.debug("values set.");
                     }

                     if (action != null && action.equals("edit")) {
                                return mapping.findForward("editReport");
                     } else if (action != null && action.equals("delete")) {
                                Iterator itr = DbUtil.getMembersUsingReport(repForm.getReportId()).iterator();
                                if (itr.hasNext()) {
                                          repForm.setFlag("membersUsing");
                                } else {
                                          repForm.setFlag("delete");
                                }
                                
                                return mapping.findForward("deleteReport");
                     } else if (action == null){
                                return mapping.findForward("forward");
                     } else {
                                return null;
                     }
          }
}
