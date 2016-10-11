/**
 * 
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.util.DbUtil;

/**
 * @author mihai
 *
 */
public class ViewEUActivity extends Action {
	private static Logger logger = Logger.getLogger(ViewEUActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
			{

		Long activityId=new Long(request.getParameter("euActivityId"));
		
		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}
		
		EUActivity eua=(EUActivity) DbUtil.getEuActivity(activityId);
		
		request.setAttribute("euActivity",eua);
		return mapping.findForward("forward");
	}
	
}
