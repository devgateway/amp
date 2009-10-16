/*
 * SearchDesktopActivities.java
 * Created : 09-jun-06
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DesktopUtil;

public class SearchDesktopActivities extends Action {
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		TeamMember tm  = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		DesktopForm dForm = (DesktopForm) form;
		
		ArrayList activities = new ArrayList();
		Collection temp = null;
		
		if (session.getAttribute(Constants.AMP_PROJECTS) == null) {
			temp = (Collection) DesktopUtil.getDesktopActivities(
					tm.getTeamId(),tm.getMemberId(),tm.getTeamHead());
			session.setAttribute(Constants.AMP_PROJECTS,temp);
		} else {
			temp = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
		}			
		

		Collection actIds = new ArrayList();
		if (dForm.getSearchKey() != null && 
				dForm.getSearchKey().trim().length() > 0) {
			
			String key = dForm.getSearchKey().trim().toLowerCase();
			Iterator itr = temp.iterator();
			while (itr.hasNext()) {
				AmpProject proj = (AmpProject) itr.next();
				if (proj.getName().toLowerCase().indexOf(key) > -1) {
					activities.add(proj);
				} else {
					actIds.add(proj.getAmpActivityId());	
				}
			}
			
			activities.addAll(DesktopUtil.searchActivities(actIds,key));
		}
		
		dForm.setActivities(activities);
		dForm.setTotalCalculated(false);
		
		return mapping.findForward("forward");
	}	
}
