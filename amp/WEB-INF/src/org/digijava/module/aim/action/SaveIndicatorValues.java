package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class SaveIndicatorValues extends Action
{
	private static Logger logger = Logger.getLogger(SaveIndicatorValues.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{

		EditActivityForm eaForm = (EditActivityForm) form;

		if (eaForm.getIndicator().getIndicatorsME() != null &&
				eaForm.getIndicator().getIndicatorsME().size() > 0) {
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorId(eaForm.getIndicator().getIndicatorId());
			Iterator itr = eaForm.getIndicator().getIndicatorsME().iterator();
			while (itr.hasNext()) {
				ActivityIndicator temp = (ActivityIndicator) itr.next();
				if (temp.equals(actInd)) {				
						temp.setBaseVal(eaForm.getIndicator().getBaseVal());
						temp.setBaseValDate(eaForm.getIndicator().getBaseValDate());
						temp.setBaseValComments(eaForm.getIndicator().getBaseValComments());
						temp.setTargetVal(eaForm.getIndicator().getTargetVal());
						temp.setRevisedTargetVal(eaForm.getIndicator().getTargetVal());
						temp.setTargetValDate(eaForm.getIndicator().getTargetValDate());
						temp.setTargetValComments(eaForm.getIndicator().getTargetValComments());
						if (eaForm.getIndicator().getRevTargetValDate() != null) {
							temp.setRevisedTargetVal(eaForm.getIndicator().getRevTargetVal());
							temp.setRevisedTargetValDate(eaForm.getIndicator().getRevTargetValDate());
							temp.setRevisedTargetValComments(eaForm.getIndicator().getRevTargetValComments());
						} else {
                            eaForm.getIndicator().setRevTargetVal(eaForm.getIndicator().getTargetVal());
							temp.setRevisedTargetVal(eaForm.getIndicator().getTargetVal());
							temp.setRevisedTargetValDate(eaForm.getIndicator().getTargetValDate());
							temp.setRevisedTargetValComments(eaForm.getIndicator().getTargetValComments());
						}

						if (eaForm.getIndicator().getCurrentValDate() != null) {
							if( eaForm.getIndicator().getCurrentVal() != null) {
								temp.setCurrentVal(eaForm.getIndicator().getCurrentVal());
								temp.setActualVal(eaForm.getIndicator().getCurrentVal());
							}					     
							temp.setCurrentValDate(eaForm.getIndicator().getCurrentValDate());
							temp.setActualValDate(eaForm.getIndicator().getCurrentValDate());	                          
							temp.setCurrentValComments(eaForm.getIndicator().getCurrentValComments());
							temp.setActualValComments(eaForm.getIndicator().getCurrentValComments());							
						}
						if( eaForm.getIndicator().getCurrentVal() != null) {
							temp.setCurrentVal(eaForm.getIndicator().getCurrentVal());
							temp.setActualVal(eaForm.getIndicator().getCurrentVal());
						} else{
							temp.setCurrentVal(null);
							temp.setActualVal(null);
						}
						temp.setRisk(eaForm.getIndicator().getIndicatorRisk());						
						AmpCategoryValue acv = CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getIndicator().getLogframeCategory());
						temp.setIndicatorsCategory(acv);
						break;
					
				}
			}
		}
		request.getSession().setAttribute("filledIndicatorsME", eaForm.getIndicator().getIndicatorsME());
		return mapping.findForward("forward");
	}
}
