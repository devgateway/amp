/*
 * FilterDesktopActivities.java
 * Created: 29-May-2006
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
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class FilterDesktopActivities extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		TeamMember tm  = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		DesktopForm dForm = (DesktopForm) form;
		
		ArrayList activities = null;
		Collection temp = null;
		
		if (session.getAttribute(Constants.AMP_PROJECTS) == null) {
			temp = (Collection) DesktopUtil.getDesktopActivities(
					tm.getTeamId(),tm.getMemberId(),tm.getTeamHead());
			session.setAttribute(Constants.AMP_PROJECTS,temp);
		} else {
			temp = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
		}			
		
		activities = new ArrayList(temp);
		if (dForm.getFltrFrmYear() > 0) {
			
		}
		if (dForm.getFltrToYear() > 0) {
			
		}
		boolean flag = false;
		if (dForm.getFltrDonor() > 0) {
			// Filter activities based on Donors
			if (activities != null && activities.size() > 0) {
				for (int i = 0;i < activities.size();i ++) {
					AmpProject proj = (AmpProject) activities.get(i);
					Iterator itr = proj.getDonor().iterator();
					while (itr.hasNext()) {
						AmpProjectDonor pDnr = (AmpProjectDonor) itr.next();
						if (pDnr.getAmpDonorId().longValue() == dForm.getFltrDonor()) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						activities.remove(proj);
						i--;
					}
					flag = false;
				}					
			}
		}
		if (dForm.getFltrStatus() > 0) {
			// Filter activities based on Status
			if (activities != null && activities.size() > 0) {
				for (int i = 0;i < activities.size();i ++) {
					AmpProject proj = (AmpProject) activities.get(i);
					if (proj.getStatusId().longValue() != dForm.getFltrStatus()) {
						activities.remove(proj);
						i--;
					}
				}
			}				
		}
		if (dForm.getFltrSector() > 0) {
			// Filter activities based on Sector
			if (activities != null && activities.size() > 0) {
				for (int i = 0;i < activities.size();i ++) {
					AmpProject proj = (AmpProject) activities.get(i);
					Iterator itr = proj.getSector().iterator();
					while (itr.hasNext()) {
						Sector sec = (Sector) itr.next();
						if (sec.getSectorId().longValue() == dForm.getFltrSector()) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						activities.remove(proj);
						i--;
					}
					flag = false;
				}
			}								
		}
		if (request.getParameter("risk") != null) {
			String risk = request.getParameter("risk");
			int riskValue = MEIndicatorsUtil.getRiskRatingValue(risk);
			dForm.setFltrActivityRisks(riskValue);
		}		
		if (dForm.getFltrActivityRisks() != 0) {
			// Filter activities based on activity risk
			if (activities != null && activities.size() > 0) {
				for (int i = 0;i < activities.size();i ++) {
					AmpProject proj = (AmpProject) activities.get(i);
					if (proj.getActivityRisk() != dForm.getFltrActivityRisks()) {
						activities.remove(proj);
						i--;
					}
				}
			}								
		}

		dForm.setActivities(activities);
		dForm.setTotalCalculated(false);
		dForm.setSearchKey(null);
		
		return mapping.findForward("forward");
	}
}