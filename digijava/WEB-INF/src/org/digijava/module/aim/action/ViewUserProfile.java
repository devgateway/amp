package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UserDetailForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;


public class ViewUserProfile extends Action
{
	private static Logger logger = Logger.getLogger(ViewUserProfile.class) ;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) 
	throws java.lang.Exception {

		UserDetailForm formBean = (UserDetailForm)form;
		HttpSession httpSession = request.getSession();
		TeamMember teamMember=(TeamMember)httpSession.getAttribute("currentMember");
		
		Long memId = null;
		if (request.getParameter("id") != null) {
			long id = Long.parseLong(request.getParameter("id"));
			memId = new Long(id);
		} else {
			memId = teamMember.getMemberId(); 
		}
		
		AmpTeamMember member = DbUtil.getAmpTeamMember(memId);
		
		formBean.setAddress(member.getUser().getAddress());
		formBean.setFirstNames(member.getUser().getFirstNames());
		formBean.setLastName(member.getUser().getLastName());
		formBean.setMailingAddress(member.getUser().getEmail());
		formBean.setOrganizationName(member.getUser().getOrganizationName());
		formBean.setInfo(DbUtil.getMemberInformation(member.getUser().getId()));

		return mapping.findForward("forward");
	}
}