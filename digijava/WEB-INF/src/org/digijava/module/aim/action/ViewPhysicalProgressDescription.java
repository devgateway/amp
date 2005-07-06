package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.form.PhysicalProgressForm ;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.COverSubString ;
import org.digijava.module.aim.helper.TeamMember ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpSession ;
import javax.servlet.http.HttpServletResponse ;
import java.util.Collection ;
import java.util.Iterator ;
import java.util.ArrayList ;

public class ViewPhysicalProgressDescription extends Action
{
	private static Logger logger = Logger.getLogger(ViewChannelOverview.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		PhysicalProgressForm formBean = (PhysicalProgressForm) form ; 

		if(teamMember==null)
		{
			formBean.setValidLogin(false);
		}
		else
		{
			formBean.setValidLogin(true);
			logger.debug("PP ID is below:" );
			Long pid=new Long(request.getParameter("pid"));
			logger.debug("PP ID is :" + pid.toString());
			AmpPhysicalPerformance progress=DbUtil.getAmpPhysicalProgressDescription(pid);
			formBean.setTitle(progress.getTitle());
			formBean.setDescription(progress.getDescription());
		}
		return mapping.findForward("forward");
	}
}

