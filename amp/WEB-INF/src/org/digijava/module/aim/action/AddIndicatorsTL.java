package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

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
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.IndicatorUtil;

public class AddIndicatorsTL extends Action {
	private static Logger logger = Logger.getLogger(AddIndicatorsTL.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		IndicatorForm indForm = (IndicatorForm) form;
		
		HttpSession session = request.getSession();
		
		AmpMEIndicatorValue ampMEIndValbox = null;
		AmpMEIndicatorValue ampMEIndValsearch = null;
		AmpMEIndicators ampMEIndbox = null;
		AmpIndicator ampInd=null;
		AmpMEIndicators ampMEIndsearch = null;
		AmpActivity ampAct = new AmpActivity();
		
		
		ampAct.setAmpActivityId(indForm.getActivityId());
		Collection col = new ArrayList();
		if (indForm.getSelectedIndicators() != null) {
			if (indForm.getSelectedIndicators().length != 0) {
				Long selectedIndicators[] = indForm.getSelectedIndicators();
				for (int i = 0; i < selectedIndicators.length; i++) {
					ampMEIndValbox = new AmpMEIndicatorValue();
					ampMEIndbox = new AmpMEIndicators();
					ampInd=new AmpIndicator();
					ampMEIndbox.setAmpMEIndId(selectedIndicators[i]);
					ampMEIndbox.setName(IndicatorUtil.getIndicatorName(selectedIndicators[i]));
					ampMEIndbox.setCode(IndicatorUtil.getIndicatorCode(selectedIndicators[i]));
					ampInd.setIndicatorId(selectedIndicators[i]);
					ampInd.setName(IndicatorUtil.getIndicatorName(selectedIndicators[i]));
					ampInd.setCode(IndicatorUtil.getIndicatorCode(selectedIndicators[i]));
					ampMEIndValbox.setActivityId(ampAct);
					ampMEIndValbox.setMeIndicatorId(ampMEIndbox);
					ampMEIndValbox.setIndicator(ampInd);
					ampMEIndValbox.setBaseVal(null);
					ampMEIndValbox.setTargetVal(null);
					ampMEIndValbox.setActualVal(null);
					ampMEIndValbox.setBaseValDate(null);
					ampMEIndValbox.setTargetValDate(null);
					ampMEIndValbox.setActualValDate(null);
					ampMEIndValbox.setRisk(null);
					ampMEIndValbox.setComments(null);
					if("true".equalsIgnoreCase((String)session.getAttribute("forStep9"))){
						col.add(ampMEIndValbox);
					}
					else
					DbUtil.add(ampMEIndValbox);
				}
			}
		}

		if (indForm.getSelIndicators() != null) {
			if (indForm.getSelIndicators().length != 0) {
				Long selIndicators[] = indForm.getSelIndicators();
				for (int i = 0; i < selIndicators.length; i++) {
					ampMEIndValsearch = new AmpMEIndicatorValue();
					ampMEIndsearch = new AmpMEIndicators();
					ampMEIndsearch.setAmpMEIndId(selIndicators[i]);
					ampMEIndValsearch.setActivityId(ampAct);
					ampMEIndValsearch.setMeIndicatorId(ampMEIndsearch);
					ampMEIndValsearch.setBaseVal(null);
					ampMEIndValsearch.setTargetVal(null);
					ampMEIndValsearch.setActualVal(null);
					ampMEIndValsearch.setBaseValDate(null);
					ampMEIndValsearch.setTargetValDate(null);
					ampMEIndValsearch.setActualValDate(null);
					ampMEIndValsearch.setRisk(null);
					ampMEIndValsearch.setComments(null);
					if("true".equalsIgnoreCase((String)session.getAttribute("forStep9"))){
						col.add(ampMEIndValsearch);
					}
					else
					DbUtil.add(ampMEIndValsearch);
				}
			}
		}
		session.setAttribute("ampMEIndValbox",col);
		return mapping.findForward("forward");
	}
}