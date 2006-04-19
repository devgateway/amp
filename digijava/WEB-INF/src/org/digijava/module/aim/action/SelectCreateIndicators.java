package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.util.MEIndicatorsUtil;


public class SelectCreateIndicators extends Action 
{
	private static Logger logger = Logger.getLogger(SelectCreateIndicators.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		String searchkey = null;
		Collection nonDefaultInd = null;
		Collection activityInd = null;
		Collection nonDefActInd = new ArrayList();
		Collection searchInd = new ArrayList();
		Collection searchResult = null;
		boolean sameIndicator = false;

		IndicatorForm indForm = (IndicatorForm) form;
		
		nonDefaultInd = MEIndicatorsUtil.getAllNonDefaultIndicators();
		activityInd = MEIndicatorsUtil.getActivityIndicatorsList(indForm.getActivityId());

		Iterator nonDefaultItr = nonDefaultInd.iterator();
		
		while(nonDefaultItr.hasNext())
		{
			AmpMEIndicators tempNonDefaultInd = (AmpMEIndicators) nonDefaultItr.next();
			Iterator activityIndItr = activityInd.iterator();
			while(activityIndItr.hasNext() && sameIndicator == false)
			{
				ActivityIndicator tempActInd = (ActivityIndicator) activityIndItr.next();
				sameIndicator = false;
				if(tempNonDefaultInd.getAmpMEIndId().equals(tempActInd.getIndicatorId()))
					sameIndicator = true;
			}
			if(sameIndicator == false)
				nonDefActInd.add(tempNonDefaultInd);
		}
		
		indForm.setNondefaultindicators(nonDefActInd);
		indForm.setActivityId(indForm.getActivityId());

		if(indForm.getSearchkey() != null)
		{
			searchkey = indForm.getSearchkey().trim();
			searchResult = MEIndicatorsUtil.searchForIndicators(searchkey);

			Iterator searchResultItr = searchResult.iterator();
			while(searchResultItr.hasNext())
			{
				AmpMEIndicators tempSearchInd = (AmpMEIndicators) searchResultItr.next();
				Iterator activityIndItr = activityInd.iterator();
				while(activityIndItr.hasNext() && sameIndicator == false)
				{
					ActivityIndicator tempActInd = (ActivityIndicator) activityIndItr.next();
					sameIndicator = false;
					if(tempSearchInd.getAmpMEIndId().equals(tempActInd.getIndicatorId()))
						sameIndicator = true;
				}
				if(sameIndicator == false)
					searchInd.add(tempSearchInd);
			}
			indForm.setSearchReturn(searchInd);
		}
		return mapping.findForward("forward");
	}
}