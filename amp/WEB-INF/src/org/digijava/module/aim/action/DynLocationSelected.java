/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.SelectLocationForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Mauricio Coria
 *
 */
public class DynLocationSelected extends Action {
	private static Logger logger = Logger.getLogger(DynLocationSelected.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = null;
		Object startSelection = request.getAttribute("StartSelection");
		if(startSelection != null &&  startSelection instanceof String){			
			session.setAttribute("StartSelection", startSelection);
			forward = mapping.findForward(mapping.getParameter());
		}else{
			String startSelectionReturn = (String) session.getAttribute("StartSelection");
			forward = mapping.findForward(startSelectionReturn);
			request.setAttribute("EndSelection", true);
		}
		return forward;
	}
}
