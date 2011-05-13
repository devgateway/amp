/*
 * DeleteMemberLink.java
 * Created : 12-May-2007
 * 
 */

package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.MyDesktopForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;


public class DeleteMemberLink extends Action {
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        MyDesktopForm mdForm = (MyDesktopForm) form;
    	String id = request.getParameter("id");
    	Long[] selected = new Long[1];
    	selected[0] = new Long(Long.parseLong(id));
    	HttpSession session = request.getSession();
    	TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		if (tm != null) {
			TeamMemberUtil.removeMemberLinks(tm.getMemberId(), selected);
	    	Collection links = TeamMemberUtil.getMemberLinks(tm.getMemberId());
			session.setAttribute(Constants.MY_LINKS,links);
		}
		return mapping.findForward("forward");
       
    }
}