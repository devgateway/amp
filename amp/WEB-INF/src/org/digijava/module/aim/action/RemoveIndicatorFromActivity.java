package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.ActivityUtil;

public class RemoveIndicatorFromActivity extends Action {	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		EditActivityForm actForm=(EditActivityForm)form;
		Long indicatorId=new Long(request.getParameter("indId"));
		Collection<IndicatorActivity> activityInd = null;
		
		HttpSession session = request.getSession();
		if(actForm.getActivityId()!=null){
			AmpActivity activity=ActivityUtil.loadActivity(actForm.getActivityId());
			if(activity!=null){
				activityInd = activity.getIndicators();
				if(activityInd!=null && activityInd.size()>0){
					for (IndicatorActivity indAct : activityInd) {
						if(indAct.getIndicator().getIndicatorId().equals(indicatorId)){
							activityInd.remove(indAct);
							break;
						}
					}				
				}
				if(actForm.getIndicator().getIndicatorsME()!=null && actForm.getIndicator().getIndicatorsME().size()>0){
					Collection<ActivityIndicator> actIndicators=actForm.getIndicator().getIndicatorsME();
					for (ActivityIndicator actInd : actIndicators) {
						if(actInd.getIndicatorId().equals(indicatorId)){
							actIndicators.remove(actInd);
							break;
						}
					}
				}
			}
		}			
		
		if(actForm.getIndicator().getIndicatorsME()!=null && actForm.getIndicator().getIndicatorsME().size()>0){
			Collection<ActivityIndicator> actIndicators=actForm.getIndicator().getIndicatorsME();
			for (ActivityIndicator actInd : actIndicators) {
				if(actInd.getIndicatorId().equals(indicatorId)){
					actIndicators.remove(actInd);
					break;
				}
			}
		}
		
		session.setAttribute("forStep9", "true");
		return mapping.findForward("forward");
	}
}
