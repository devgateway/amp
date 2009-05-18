package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.form.ViewAhSurveisForm;
import org.digijava.module.aim.util.DbUtil;

public class ViewAhSurveis extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		String str = (String) session.getAttribute("ampAdmin");

		if (str == null || str.equals("no")) {
			  SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

			  String url = SiteUtils.getSiteURL(currentDomain, request
									.getScheme(), request.getServerPort(), request
									.getContextPath());
			  url += "/aim/index.do";
			  response.sendRedirect(url);
			  return null;
		}
    	
    	ViewAhSurveisForm svform=(ViewAhSurveisForm)form;
        svform.setSurveis(DbUtil.getAllAhSurveyIndicators());
        return mapping.findForward("forward");
    }
}
