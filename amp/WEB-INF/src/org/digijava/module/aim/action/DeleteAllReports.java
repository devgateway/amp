package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;


public class DeleteAllReports extends Action {

	  private static Logger logger = Logger.getLogger(DeleteAllReports.class);

	  public ActionForward execute(ActionMapping mapping,
							ActionForm form,
							HttpServletRequest request,
							HttpServletResponse response) throws java.lang.Exception {
		  	
		  	ReportsForm repForm = (ReportsForm) form;

		  	HttpSession session = request.getSession();
		  	TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		  	AmpReports ampReport=null;
		  	ActionErrors errors = new ActionErrors();
		  	//logger.info("In delete reports11111111");
		  	boolean lead = false;
				if (session.getAttribute("teamLeadFlag") == null) {
					logger.info("not a team Lead!");
					//return mapping.findForward("forward");
				} else {
					String str = (String)session.getAttribute("teamLeadFlag");
					logger.info("teamFlag ..... "+session.getAttribute("teamLeadFlag")+ " strrrr "+str);
					if (str.equals("true")) {
						logger.info("yes a team Lead!");
						lead=true;
						//return mapping.findForward("forward");
					}
					else
					{
						logger.info("NOPE!! team Lead!");
					}
				}
				 logger.info(repForm.getReportId());
				 String a = request.getParameter("rid");
				 logger.info(" this is rid...."+request.getParameter("rid")+" thisisa...."+a);
				 Long id = new Long(a);
				 if(id!=null){
					  ampReport=DbUtil.getAmpReport(id);
					  if(lead||(ampReport.getOwnerId()!=null && tm.getMemberId().equals(ampReport.getOwnerId().getAmpTeamMemId())))
						 {							
							 logger.info("In delete reports");							 
								 ReportsForm ampReportForm = new ReportsForm();								 
								 ampReportForm.setReportId(id);
								 //log the delete action for a report
								 //AmpReports aReportForLog=DbUtil.getAmpReport(ampReportForm.getReportId());
								 AuditLoggerUtil.logObject(session,request,ampReport,"delete");
								 DbUtil.updateAppSettingsReportDeleted(ampReportForm.getReportId());
								 boolean deleted = AdvancedReportUtil.deleteReportsCompletely(ampReportForm.getReportId());										 
										 if (deleted) {
											errors.add("title", new ActionError(
													"error.aim.deleteReports.reportDeleted"));
											saveErrors(request,errors);								
														logger.debug("Report deleted");
										 }												
						 }
				 }else {
					 errors.add("title", new ActionError(
						"error.aim.deleteReports.reportNotDeleted"));
					saveErrors(request,errors);
		
					logger.debug("Report could not be deleted! ");
				 }
				 
				 return mapping.findForward("forward");				
	  }
}


