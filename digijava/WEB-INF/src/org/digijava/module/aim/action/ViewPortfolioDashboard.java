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
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class ViewPortfolioDashboard extends TilesAction {
	
	private static Logger logger = Logger.getLogger(ViewPortfolioDashboard.class);
	
	public ActionForward execute(ComponentContext context,ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		try {
		
		DynaActionForm pdForm = (DynaActionForm) form;
		
		HttpSession session = request.getSession();
		int pageListSize = 0;
		int page = 1;
		
		if (request.getParameter("pge") != null) {
			page = Integer.parseInt(request.getParameter("pge"));
		}
		
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			Collection col = new ArrayList();
			ArrayList temp = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			
			ArrayList projects = new ArrayList();
			for (int i = 0;i < temp.size();i ++) {
				AmpProject tempP = (AmpProject) temp.get(i);
				col.add(tempP.getAmpActivityId());
				AmpProject proj = new AmpProject();
				proj.setAmpActivityId(tempP.getAmpActivityId());
				if (tempP.getName().length() > 30) {
					proj.setName(tempP.getName().substring(0,30) + "...");
				} else {
					proj.setName(tempP.getName());
				}
				projects.add(proj);
			}
			Collections.sort(projects);
			
			int overallRisk = MEIndicatorsUtil.getOverallPortfolioRisk(col);
			
			logger.info("Overall risk = " + overallRisk);
			
			String fntColor = "";
			int r = 0,g = 0,b = 0;
			switch (overallRisk) {
			case Constants.HIGHLY_SATISFACTORY:
				r = Constants.HIGHLY_SATISFACTORY_CLR.getRed();
				g = Constants.HIGHLY_SATISFACTORY_CLR.getGreen();
				b = Constants.HIGHLY_SATISFACTORY_CLR.getBlue();
				break;
			case Constants.VERY_SATISFACTORY:
				r = Constants.VERY_SATISFACTORY_CLR.getRed();
				g = Constants.VERY_SATISFACTORY_CLR.getGreen();
				b = Constants.VERY_SATISFACTORY_CLR.getBlue();				
				break;
			case Constants.SATISFACTORY:
				r = Constants.SATISFACTORY_CLR.getRed();
				g = Constants.SATISFACTORY_CLR.getGreen();
				b = Constants.SATISFACTORY_CLR.getBlue();				
				break;
			case Constants.UNSATISFACTORY:
				r = Constants.UNSATISFACTORY_CLR.getRed();
				g = Constants.UNSATISFACTORY_CLR.getGreen();
				b = Constants.UNSATISFACTORY_CLR.getBlue();				
				break;
			case Constants.VERY_UNSATISFACTORY:
				r = Constants.VERY_UNSATISFACTORY_CLR.getRed();
				g = Constants.VERY_UNSATISFACTORY_CLR.getGreen();
				b = Constants.VERY_UNSATISFACTORY_CLR.getBlue();				
				break;
			case Constants.HIGHLY_UNSATISFACTORY:
				r = Constants.HIGHLY_UNSATISFACTORY_CLR.getRed();
				g = Constants.HIGHLY_UNSATISFACTORY_CLR.getGreen();
				b = Constants.HIGHLY_UNSATISFACTORY_CLR.getBlue();				
			}
			
			String hexR = Integer.toHexString(r);
			String hexG = Integer.toHexString(g);
			String hexB = Integer.toHexString(b);
			if (hexR.equals("0"))
				fntColor += "00";
			else 
				fntColor += hexR;
			
			if (hexG.equals("0"))
				fntColor += "00";
			else 
				fntColor += hexG;
			
			if (hexB.equals("0"))
				fntColor += "00";
			else 
				fntColor += hexB;			
			
			String risk = MEIndicatorsUtil.getRiskRatingName(overallRisk);
			
			pdForm.set("overallRisk",risk);
			pdForm.set("activityList",projects);
			pdForm.set("fontColor",fntColor);
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

		session.setAttribute("activityId",actId);
		session.setAttribute("indicatorId",indId);
		session.setAttribute("page",new Integer(page));
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}