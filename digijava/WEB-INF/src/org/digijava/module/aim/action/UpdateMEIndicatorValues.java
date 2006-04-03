/*
 * SaveMEIndicatorValues.java
 * Created : 29-Mar-2006
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateIndicatorValuesForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class UpdateMEIndicatorValues extends Action {
	
	private static Logger logger = Logger.getLogger(UpdateMEIndicatorValues.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		UpdateIndicatorValuesForm uivForm = (UpdateIndicatorValuesForm) form;
		String event = request.getParameter("event");
		if (event != null && event.equalsIgnoreCase("save")) {
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorValId(uivForm.getIndicatorValId());
			actInd.setBaseVal(uivForm.getBaseVal());
			actInd.setBaseValDate(uivForm.getBaseValDate());
			actInd.setTargetVal(uivForm.getTargetVal());
			actInd.setTargetValDate(uivForm.getTargetValDate());
			actInd.setRevTargetVal(uivForm.getRevTargetVal());
			actInd.setRevTargetValDate(uivForm.getRevTargetValDate());
			MEIndicatorsUtil.saveMEIndicatorValues(actInd);
		} else if (event != null && event.equalsIgnoreCase("delete")) {
			MEIndicatorsUtil.deleteMEIndicatorValues(uivForm.getIndicatorValId());			
		}
		return mapping.findForward("forward");

		
	}
}