package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.form.AmpActivityProgramSettingsForm;
import org.digijava.module.aim.util.ProgramUtil;

public class AmpActivityProgramSettingsAction
    extends Action {
        private static Logger logger = Logger.getLogger(
            AmpActivityProgramSettingsAction.class);

        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws java.
            lang.
            Exception {

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

                AmpActivityProgramSettingsForm ampActivityProgramSettingsForm = (
                    AmpActivityProgramSettingsForm) form;

                
                if (request.getParameter("save")!=null) {
                        ProgramUtil.saveAmpActivityProgramSettings(
                            ampActivityProgramSettingsForm.getSettingsList());
                        ampActivityProgramSettingsForm.setEvent(null);
                        return mapping.findForward("forward");
                }
                else {
                        //Load programs settings
                        ampActivityProgramSettingsForm.setProgramList(
                            ProgramUtil.getAllThemes());
                        ampActivityProgramSettingsForm.setSettingsList(
                            ProgramUtil.getAmpActivityProgramSettingsList());
                        return mapping.findForward("forward");

                }

        }
}
