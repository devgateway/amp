package org.digijava.module.aim.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

public class SearchIndicators extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception 
	{
		
		Logger logger = Logger.getLogger(SearchIndicators.class);
		
		IndicatorForm indForm = (IndicatorForm) form;	
		//clear form if necessary
		if(request.getParameter("clear")!=null && request.getParameter("clear").equalsIgnoreCase("true")){
			indForm.setIndicatorCode(null);
			indForm.setIndicatorDesc(null);
			indForm.setIndicatorName(null);
			indForm.setIndId(null);
			indForm.setSearchKey(null);
			indForm.setSearchReturn(null);
			indForm.setSelectedSectorsForInd(null);
			indForm.setShowAddInd(false);
			indForm.setSelectedIndicators(null);
		}
		
		Collection searchResult = null;
		Collection activityInd = null;
		Collection searchInd = new ArrayList();
		boolean sameIndicator = false;
		String nsr = "yes";
		String searchkey = null;
		
		indForm.setActivityId(indForm.getActivityId());		
		indForm.setAllSectors(SectorUtil.getAllParentSectors());
		
		logger.info("indForm.getSearchkey() = " + indForm.getSearchkey()+" indForm.getActivityId() : "+indForm.getActivityId());
		
		if(indForm.getAction()!=null && indForm.getAction().equals("clear")){
			indForm.setSectorName("-1");
			indForm.setSearchkey("");			
			indForm.setAction("");
			
		}
		
		if(indForm.getAction() == null){
			indForm.setAction("selected");
			
		}	
		
		
		if((indForm.getSearchkey()!=null && indForm.getSearchkey().trim().length() > 0) || 
			(indForm.getSectorName()!=null && indForm.getSectorName().trim().length()>0 && !indForm.getSectorName().trim().equals("-1"))){
			searchInd = IndicatorUtil.searchIndicators(indForm.getSearchkey(),indForm.getSectorName());
		}else {
			searchInd = IndicatorUtil.getAmpIndicator();
		}
		
				indForm.setSearchReturn(searchInd);
		
		
		
//		if(indForm.getSearchkey().trim() != null)
//		{
//			logger.info("inside if.. :0");
//			activityInd = MEIndicatorsUtil.getActivityIndicatorsList(indForm.getActivityId());
//			searchkey = indForm.getSearchkey().trim();
//			searchResult = MEIndicatorsUtil.searchForIndicators(searchkey);
//			
//			Iterator searchResultItr = searchResult.iterator();
//			while(searchResultItr.hasNext())
//			{
//				logger.info("inside 1st while... :0");
//				AmpMEIndicators tempSearchInd = (AmpMEIndicators) searchResultItr.next();
//				Iterator activityIndItr = activityInd.iterator();
//				sameIndicator = false;				
//				while(activityIndItr.hasNext() && sameIndicator == false)
//				{
//					logger.info("inside 2nd while... :0");
//					ActivityIndicator tempActInd = (ActivityIndicator) activityIndItr.next();
//
//					if(tempSearchInd.getAmpMEIndId().equals(tempActInd.getIndicatorId()))
//						sameIndicator = true;
//				}
//				if(sameIndicator == false)
//					searchInd.add(tempSearchInd);
//			}
//			if(searchInd.isEmpty())
//			{
//				logger.info("yes its NULL......");
//				indForm.setNoSearchResult(true);
//			}
//		}		
				if(request.getParameter("addInd")!=null && request.getParameter("addInd").equalsIgnoreCase("true")){
					String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
			        SimpleDateFormat sdf = null;
			        //temporary workaround 
			        if (dateFormat!=null){
			        	//?????
			            dateFormat = dateFormat.replace("m", "M");
			            sdf = new SimpleDateFormat(dateFormat);
			        }else{
			        	sdf= new SimpleDateFormat();
			        }
					
			        indForm.setCreationDate(sdf.format(new Date()).toString());
			        indForm.setShowAddInd(true);
				}
				 		
				
		return mapping.findForward("forward");
	}
}
