package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.util.DbUtil;

public class ReportsManager extends Action {

          private static Logger logger = Logger.getLogger(ReportsManager.class);

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
                     
                     Collection reports = new ArrayList();
                     ReportsForm repForm = (ReportsForm) form;
                     
                     logger.debug("In report manager");
                     reports = DbUtil.getAllReports(null);
                     repForm.setReports(reports);
                     return mapping.findForward("forward");
          }
}


