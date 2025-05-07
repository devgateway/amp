/*
 * Logout.java
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Clears all session variables associated with the current user
 *
 * @author Priyajith
 */

public class Logout extends TilesAction {

    private static Logger logger = Logger.getLogger(Logout.class);

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        // Code was moved to org.digijava.module.aim.auth.AmpLogoutHandler
/*
        logger.debug("logout");

        HttpSession session = request.getSession();

        if (session.getAttribute("currentMember") != null) {
            session.removeAttribute("currentMember");
        }

        if (session.getAttribute("teamLeadFlag") != null) {
            session.removeAttribute("teamLeadFlag");
        }

        if (session.getAttribute("ampAdmin") != null) {
            session.removeAttribute("ampAdmin");
        }

        HttpLoginManager.logout(request, response);
*/
        return null;
    }
}
