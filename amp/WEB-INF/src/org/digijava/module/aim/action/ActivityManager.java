package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;


public class ActivityManager extends Action
{
	private static Logger logger = Logger.getLogger(ActivityManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		if (session.getAttribute("ampAdmin") == null) 
			return mapping.findForward("index");
		else 
		{
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) 
				return mapping.findForward("index");
		}

		ActivityForm actForm = (ActivityForm) form;

		if(action != null && action.equals("delete"))
		{
			Long ampActId = new Long(Long.parseLong(request.getParameter("id")));
			ActivityUtil.deleteActivityAmpComments(DbUtil.getActivityAmpComments(ampActId));
			ActivityUtil.deleteActivityPhysicalComponentReport(DbUtil.getActivityPhysicalComponentReport(ampActId));
			ActivityUtil.deleteActivityAmpReportCache(DbUtil.getActivityReportCache(ampActId));
			ActivityUtil.deleteActivityReportLocation(DbUtil.getActivityReportLocation(ampActId));
			ActivityUtil.deleteActivityReportPhyPerformance(DbUtil.getActivityRepPhyPerformance(ampActId));
			ActivityUtil.deleteActivityReportSector(DbUtil.getActivityReportSector(ampActId));
			ActivityUtil.deleteActivityIndicatorVal(DbUtil.getActivityMEIndValue(ampActId));
			
			ActivityUtil.deleteActivity(ampActId);
			AmpActivity activity=ActivityUtil.getAmpActivity(ampActId);
			AuditLoggerUtil.logObject(session,request,activity,"delete");
		}
		actForm.setActivityList(ActivityUtil.getAllActivitiesList());
		
		return mapping.findForward("forward");
	}
}