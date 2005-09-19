package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;


public class Logout extends TilesAction {

	private static Logger logger = Logger.getLogger(Logout.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.debug("logout");

		HttpSession session = request.getSession();

		if (session.getAttribute("currentMember") != null) {
			session.removeAttribute("currentMember");
		}

		if (session.getAttribute("teamLeadFlag") != null) {
			session.removeAttribute("teamLeadFlag");
		}

		HttpLoginManager.logout(request, response);
		
		
		return null;
	}
}
