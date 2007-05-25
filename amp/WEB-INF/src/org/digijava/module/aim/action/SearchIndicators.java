package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.apache.log4j.Logger;

import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.dbentity.AmpMEIndicators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SearchIndicators extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception 
	{
		
		Logger logger = Logger.getLogger(SearchIndicators.class);
		
		IndicatorForm indForm = (IndicatorForm) form;
		Collection searchResult = null;
		Collection activityInd = null;
		Collection searchInd = new ArrayList();
		boolean sameIndicator = false;
		String nsr = "yes";
		String searchkey = null;
		
		indForm.setActivityId(indForm.getActivityId());			
		
		logger.info("indForm.getSearchkey() = " + indForm.getSearchkey()+" indForm.getActivityId() : "+indForm.getActivityId());
		if(indForm.getSearchkey().trim() != null)
		{
			logger.info("inside if.. :0");
			activityInd = MEIndicatorsUtil.getActivityIndicatorsList(indForm.getActivityId());
			searchkey = indForm.getSearchkey().trim();
			searchResult = MEIndicatorsUtil.searchForIndicators(searchkey);
			
			Iterator searchResultItr = searchResult.iterator();
			while(searchResultItr.hasNext())
			{
				logger.info("inside 1st while... :0");
				AmpMEIndicators tempSearchInd = (AmpMEIndicators) searchResultItr.next();
				Iterator activityIndItr = activityInd.iterator();
				sameIndicator = false;				
				while(activityIndItr.hasNext() && sameIndicator == false)
				{
					logger.info("inside 2nd while... :0");
					ActivityIndicator tempActInd = (ActivityIndicator) activityIndItr.next();

					if(tempSearchInd.getAmpMEIndId().equals(tempActInd.getIndicatorId()))
						sameIndicator = true;
				}
				if(sameIndicator == false)
					searchInd.add(tempSearchInd);
			}
			if(searchInd.isEmpty())
			{
				logger.info("yes its NULL......");
				indForm.setNoSearchResult(true);
			}
			indForm.setSearchReturn(searchInd);
		}
		return mapping.findForward("forward");
	}
}
