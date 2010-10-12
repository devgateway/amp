package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

public class SelectCreateIndicators extends Action {
	private static Logger logger = Logger
			.getLogger(SelectCreateIndicators.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Collection nonDefaultInd = null;
		Collection<IndicatorActivity> activityInd = null;
		Collection nonDefActInd = new ArrayList();
		boolean sameIndicator = false;
		
		HttpSession session = request.getSession();
		
		IndicatorForm indForm = (IndicatorForm) form;
		
		if(request.getParameter("clear")!=null){
            indForm.setSearchkey(null);
            indForm.setSelectedIndicators(null);
        }

		nonDefaultInd = IndicatorUtil.getAllNonDefaultIndicators();
		AmpActivity activity=null;
              
//		activityInd = IndicatorUtil.getActivityIndicatorsList(indForm.getActivityId());
		if(indForm.getActivityId() != null && indForm.getActivityId() != 0){
                    activity = ActivityUtil.loadActivity(indForm.getActivityId());
			activityInd = activity.getIndicators();

			Iterator nonDefaultItr = nonDefaultInd.iterator();

			while (nonDefaultItr.hasNext()) {
				AmpIndicator tempNonDefaultInd = (AmpIndicator) nonDefaultItr.next();
				Iterator<IndicatorActivity> activityIndItr = activityInd.iterator();
				sameIndicator = false;
				while (activityIndItr.hasNext() && sameIndicator == false) {
					IndicatorActivity tempActInd =  activityIndItr.next();

					if (tempNonDefaultInd.getIndicatorId().equals(tempActInd.getIndicator().getIndicatorId()))
						sameIndicator = true;
				}
				if (sameIndicator == false)
					nonDefActInd.add(tempNonDefaultInd);
			}
 
		}
		
		indForm.setNondefaultindicators(nonDefActInd);
		indForm.setActivityId(indForm.getActivityId());
		indForm.setAllSectors(SectorUtil.getAllParentSectors());
		if("true".equalsIgnoreCase(request.getParameter("addIndicatorForStep9"))){
			session.setAttribute("forStep9","true");
			return mapping.findForward("toAdditionOfIndiForStep9");
		}
		return mapping.findForward("forward");
	}
}