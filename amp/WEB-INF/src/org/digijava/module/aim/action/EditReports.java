package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class EditReports extends Action {

          private static Logger logger = Logger.getLogger(EditReports.class);

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

                     logger.debug("In edit reports");

                     if (repForm.getReportId() != null) {
                                AmpReports ampReport = new AmpReports();
                                ampReport.setAmpReportId(repForm.getReportId());
                                ampReport.setName(repForm.getName());
                                ampReport.setDescription(repForm.getDescription());
                                ampReport.setUpdatedDate(new Date(System.currentTimeMillis()));
                                DbUtil.update(ampReport);
                                logger.debug("report updated");
                     }
                     return mapping.findForward("forward");
          }
}
