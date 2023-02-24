package org.digijava.module.aim.action;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

public class ShowAdminHome extends Action {

    public ActionForward execute(ActionMapping mapping,
                          ActionForm form,
                          javax.servlet.http.HttpServletRequest request,
                          javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
        
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
        } else {
                  return mapping.findForward("forward");
        }
    }
}
