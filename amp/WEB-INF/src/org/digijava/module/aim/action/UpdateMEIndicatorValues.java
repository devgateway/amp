/*
 * SaveMEIndicatorValues.java
 * Created : 29-Mar-2006
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.UpdateIndicatorValuesForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class UpdateMEIndicatorValues extends Action {

	private static Logger logger = Logger.getLogger(UpdateMEIndicatorValues.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		UpdateIndicatorValuesForm uivForm = (UpdateIndicatorValuesForm) form;
        HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		String event = request.getParameter("event");
		if (event != null && event.equalsIgnoreCase("save")) {
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorId(uivForm.getIndicatorId());
			actInd.setIndicatorValId(uivForm.getIndicatorValId());
			actInd.setBaseVal(uivForm.getBaseVal());
			actInd.setBaseValDate(uivForm.getBaseValDate());
			actInd.setBaseValComments(uivForm.getBaseValComments());
			actInd.setTargetVal(uivForm.getTargetVal());
			actInd.setTargetValDate(uivForm.getTargetValDate());
			actInd.setTargetValComments(uivForm.getTargetValComments());
            actInd.setRisk(uivForm.getRiskId());
            if(uivForm.getLogFrameId()!=0){
                 actInd.setIndicatorsCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(uivForm.getLogFrameId()));
            }
			if (uivForm.getRevisedTargetValDate() != null &&
					uivForm.getRevisedTargetVal() != 0) {
				actInd.setRevisedTargetVal(uivForm.getRevisedTargetVal());
				actInd.setRevisedTargetValDate(uivForm.getRevisedTargetValDate());
				actInd.setRevisedTargetValComments(uivForm.getRevisedTargetValComments());
			} else {
				actInd.setRevisedTargetVal(uivForm.getTargetVal());
				actInd.setRevisedTargetValDate(uivForm.getTargetValDate());
				actInd.setRevisedTargetValComments(uivForm.getTargetValComments());
			}
            actInd.setCurrentVal(uivForm.getCurrentVal());
			actInd.setCurrentValDate(uivForm.getCurrValDate());
			actInd.setCurrentValComments(uivForm.getCurrValComments());

			actInd.setActivityId(uivForm.getActivityId());
            AmpActivityVersion updatedAct=IndicatorUtil.saveMEIndicatorValues(actInd,request);
			uivForm.setActivityId(updatedAct.getAmpActivityId());
		} else if (event != null && event.equalsIgnoreCase("delete")) {
			uivForm.setActivityId(IndicatorUtil.removeActivityIndCon(uivForm.getIndicatorConId(),request));
		}
        uivForm.setIndicators(MEIndicatorsUtil.getActivityIndicators(uivForm.getActivityId()));
        uivForm.setIndicatorId(null);
        uivForm.setIndicatorValId(null);
        uivForm.setExpIndicatorId(new Long(-1));
		return mapping.findForward("forward");
	}
}