package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.security.HttpLoginManager;
import org.apache.struts.tiles.actions.TilesAction;
import org.apache.struts.tiles.ComponentContext;
import java.io.IOException;
import javax.servlet.ServletException;
//import org.digijava.kernel.request.SiteDomain;
//import org.digijava.kernel.util.RequestUtils;
//import org.digijava.kernel.util.SiteUtils;
import javax.servlet.http.*;

public class Logout extends TilesAction {

		  private static Logger logger = Logger.getLogger(Logout.class);

		 public ActionForward execute(ComponentContext context,
								 ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response) 
								 throws IOException,ServletException {

					 logger.debug("logout");

					 HttpSession session = request.getSession();

					 if (session.getAttribute("currentMember") != null) {
						  session.removeAttribute("currentMember");						   
					 }
					 
					 if (session.getAttribute("teamLeadFlag") != null) {
							   session.removeAttribute("teamLeadFlag");
					 }
					 
					 String sessionId = HttpLoginManager.logout(request, response);

					 logger.debug("forwarding success");
					 
					 return null;
		  }
}
