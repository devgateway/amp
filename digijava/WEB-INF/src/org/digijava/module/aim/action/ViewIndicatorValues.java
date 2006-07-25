package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.ViewIndicatorForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class ViewIndicatorValues extends TilesAction {
	
	private static Logger logger = Logger.getLogger(ViewIndicatorValues.class);
	
	public ActionForward execute(ComponentContext ctx,ActionMapping mapping,
			ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ViewIndicatorForm viForm = (ViewIndicatorForm) form;
		viForm.setIndicators(new ArrayList());
		
		String ind = request.getParameter("ind");
		String risk = request.getParameter("risk");
		
		Collection col = MEIndicatorsUtil.getIndicatorsForActivity(new Long(
				viForm.getAmpActivityId()));
		Iterator itr = col.iterator();
		Long indId = null;
		if (ind != null) {
			while (itr.hasNext()) {
				try {
					long temp = Long.parseLong(ind);
					indId = new Long(temp);
				} catch (NumberFormatException nfe) {
					logger.error("Trying to parse " + ind + " to long");
				}
				
				ActivityIndicator ai = (ActivityIndicator) itr.next();
				//if (indId.equals(ai.getIndicatorId())) {
				if (ind.equalsIgnoreCase(ai.getIndicatorName())) {
					viForm.getIndicators().add(ai);
					break;
				}
			}
		} else if (risk != null) {
			while (itr.hasNext()) {
				ActivityIndicator ai = (ActivityIndicator) itr.next();
				if (risk.equalsIgnoreCase(ai.getRiskName())) {
					viForm.getIndicators().add(ai);
				}
			}			
		}
		
		return null;
	}
}