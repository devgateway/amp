package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.ReportsForm;
import javax.servlet.http.*;
import java.util.*;

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


