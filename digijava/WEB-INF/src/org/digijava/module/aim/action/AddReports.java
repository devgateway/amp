package org.digijava.module.aim.action;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.Constants;
import javax.servlet.http.*;

public class AddReports extends Action {

	private static Logger logger = Logger.getLogger(AddReports.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		ReportsForm repForm = (ReportsForm) form;

		logger.debug("In add reports");

		if (repForm.getName() != null) {
			AmpReports ampReport = new AmpReports();
			ampReport.setName(repForm.getName());
			//ampReport.setDescription(repForm.getDescription());
			ampReport.setUpdatedDate(new Date(System.currentTimeMillis()));
			//ampReport.setOwner((AmpTeamMember)session.getAttribute(Constants.CURRENT_MEMBER));
			DbUtil.add(ampReport);
			logger.debug("reports added");

			return mapping.findForward("added");
		}
		return mapping.findForward("forward");
	}
}
