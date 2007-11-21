package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.Iterator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.MEIndicatorsUtil;

import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.helper.ActivityIndicator;


public class AddIndicatorForStepNine extends Action{
	
private static Logger logger = Logger.getLogger(AddIndicatorForStepNine.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		HttpSession session = request.getSession();
		
		
		Collection colampMEIndValbox = (Collection)session.getAttribute("ampMEIndValbox");
		String name=null,code=null;
		Collection  tmpActivityIndicator = 	eaForm.getIndicatorsME();
		
	 if(colampMEIndValbox!=null && !colampMEIndValbox.isEmpty()){
		Iterator itr = colampMEIndValbox.iterator();
		while(itr.hasNext()){
			AmpMEIndicatorValue tmpObj = (AmpMEIndicatorValue)itr.next();
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setActivityId(tmpObj.getActivityId().getAmpActivityId());
			actInd.setIndicatorValId(new Long(-1));
			name = tmpObj.getMeIndicatorId().getName();
			code = tmpObj.getMeIndicatorId().getCode();
			actInd.setIndicatorName(tmpObj.getMeIndicatorId().getName());
			actInd.setIndicatorCode(tmpObj.getMeIndicatorId().getCode());
			actInd.setBaseVal(null);
			actInd.setBaseValDate(null);
			actInd.setBaseValComments("");
			actInd.setTargetVal(null);
			actInd.setTargetValDate(null);
			actInd.setTargetValComments("");
			actInd.setRevisedTargetVal(null);
			actInd.setRevisedTargetValDate(null);
			actInd.setRevisedTargetValComments("");
			actInd.setCurrentVal(null);
			actInd.setCurrentValDate("");
			actInd.setCurrentValComments("");
			actInd.setRisk(new Long(-1));
			actInd.setIndicatorId((MEIndicatorsUtil.findIndicatorId(name, code)).getAmpMEIndId());
			tmpActivityIndicator.add(actInd);
		}
		eaForm.setIndicatorsME(tmpActivityIndicator);
		}
	
		if(!eaForm.getIndicatorsME().isEmpty())
			eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
		eaForm.setIndicatorRisk(null);
		return mapping.findForward("successfull");
	}
}
