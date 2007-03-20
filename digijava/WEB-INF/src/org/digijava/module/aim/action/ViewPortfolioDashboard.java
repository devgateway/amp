package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class ViewPortfolioDashboard extends TilesAction {
	
	private static Logger logger = Logger.getLogger(ViewPortfolioDashboard.class);
	
	public ActionForward execute(ComponentContext context,ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		try {
		
		DynaActionForm pdForm = (DynaActionForm) form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		int pageListSize = 0;
		int page = 1;
		
		if (request.getParameter("pge") != null) {
			page = Integer.parseInt(request.getParameter("pge"));
		}
		
		
		Collection session_projects = DesktopUtil.getDesktopActivities(tm.getTeamId(),tm.getMemberId(),
					tm.getTeamHead());
			session.setAttribute(Constants.AMP_PROJECTS,session_projects);
		
		
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			Collection col = new ArrayList();
			ArrayList temp = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			
			ArrayList projects = new ArrayList();
			for (int i = 0;i < temp.size();i ++) {
				AmpProject tempP = (AmpProject) temp.get(i);
				col.add(tempP.getAmpActivityId());
				AmpProject proj = new AmpProject();
				proj.setAmpActivityId(tempP.getAmpActivityId());
				proj.setName(tempP.getName());
				/*
				if (tempP.getName().length() > 30) {
					proj.setName(tempP.getName().substring(0,30) + "...");
				} else {
					proj.setName(tempP.getName());
				}*/
				projects.add(proj);
			}
			Collections.sort(projects);
			
			int overallRisk = MEIndicatorsUtil.getOverallPortfolioRisk(col);
			
			logger.info("Overall risk = " + overallRisk);
			String risk = MEIndicatorsUtil.getRiskRatingName(overallRisk);
			
			pdForm.set("overallRisk",risk);
			pdForm.set("activityList",projects);
			pdForm.set("fontColor",MEIndicatorsUtil.getRiskColor(overallRisk));
			pageListSize = projects.size() / 10;
			pageListSize = (projects.size() % 10 == 0) ? pageListSize : pageListSize+1;
		}
		 
		Collection col = MEIndicatorsUtil.getAllDefaultIndicators();
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpMEIndicators meInd = (AmpMEIndicators) itr.next();
			meInd.setName(meInd.getName());
			/*
			if (meInd.getName().length() > 30) {
				meInd.setName(meInd.getName().substring(0,30) + "...");
			}*/
		}
		pdForm.set("indicatorList",col);
		
		Long actId = (Long) pdForm.get("actId");
		Long indId = (Long) pdForm.get("indId");

		session.setAttribute("activityId",actId);
		session.setAttribute("indicatorId",indId);
		session.setAttribute("page",new Integer(page));
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}