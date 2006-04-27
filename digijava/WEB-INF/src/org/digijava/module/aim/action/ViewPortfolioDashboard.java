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
import org.apache.struts.action.DynaActionForm;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class ViewPortfolioDashboard extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		
		DynaActionForm pdForm = (DynaActionForm) form;
		
		HttpSession session = request.getSession();
		int pageListSize = 0;
		int page = 1;
		
		if (request.getParameter("pge") != null) {
			page = Integer.parseInt(request.getParameter("pge"));
		}
		
		if (session.getAttribute("ampProjects") != null) {
			ArrayList projects = (ArrayList) session.getAttribute("ampProjects");
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				if (proj.getName().length() > 30) {
					proj.setName(proj.getName().substring(0,30) + "...");
				}
			}
			pdForm.set("activityList",projects);
			pageListSize = projects.size() / 10;
			pageListSize = (projects.size() % 10 == 0) ? pageListSize : pageListSize+1;
		}
		 
		
		Collection col = MEIndicatorsUtil.getAllDefaultIndicators();
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpMEIndicators meInd = (AmpMEIndicators) itr.next();
			if (meInd.getName().length() > 30) {
				meInd.setName(meInd.getName().substring(0,30) + "...");
			}
		}
		pdForm.set("indicatorList",col);
		
		Long actId = (Long) pdForm.get("actId");
		Long indId = (Long) pdForm.get("indId");

		Collection pageList = new ArrayList();
		if (indId.longValue() > 0 && actId.longValue() <= 0) {
			if (pageListSize > 1) {
				for (int i = 0;i < pageListSize; i++) {
					pageList.add(new Integer(i+1));
				}
			}			
		}
		
		if (actId.longValue() > 0 && indId.longValue() <= 0) {
			pageListSize = col.size() / 10;
			pageListSize = (col.size() % 10 == 0) ? pageListSize : pageListSize + 1;
			if (pageListSize > 1) {
				for (int i = 0;i < pageListSize; i++) {
					pageList.add(new Integer(i+1));
				}
			}						
		}
		
		pdForm.set("pageList",pageList);
		
		request.setAttribute("activityId",actId);
		request.setAttribute("indicatorId",indId);
		request.setAttribute("page",new Integer(page));
		
		
		return mapping.findForward("forward");
	}
}