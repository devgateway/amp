package org.digijava.module.aim.action;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.FeaturesUtil;

public class GetActivityIndicatorValues extends Action 
{
	
	private static Logger logger = Logger.getLogger(GetActivityIndicatorValues.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		EditActivityForm eaForm = (EditActivityForm) form;		
		Long indValId = new Long(-1);
		String temp = request.getParameter("indValId");
		
		
		if(temp!=null)
		{
			if ("".equals(temp.trim())){
				temp="0";
			}
			
			try
			{
				indValId = new Long(Long.parseLong(temp));
				if(eaForm.getIndicator().getRiskCollection()!=null){
					Iterator iter=eaForm.getIndicator().getRiskCollection().iterator();
	            	 while (iter.hasNext()){
	    	       		 AmpIndicatorRiskRatings ampIndRisc=(AmpIndicatorRiskRatings) iter.next();
	    	       		 ampIndRisc.setTranslatedRatingName(ampIndRisc.getRatingName().replace(" ",""));
	    	       	 }
				} 
				if (eaForm.getIndicator().getIndicatorsME() != null) 
				{
					Iterator itr = eaForm.getIndicator().getIndicatorsME().iterator();
					while (itr.hasNext()) 
					{
						ActivityIndicator actInd = (ActivityIndicator) itr.next();

						if (actInd.getIndicatorId()!=null && actInd.getIndicatorId().equals(indValId)) 
						{
							//AllPrgIndicators actInd = IndicatorUtil.getAmpIndicator(actIndicator.getIndicatorId(),actIndicator.getActivityId());
							// AMP-2828 by mouhamad
					        String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
					        //TODO INDIC ??
					        dateFormat = dateFormat.replace("m", "M");
					          
					        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);							
							
							eaForm.getIndicator().setIndicatorId(actInd.getIndicatorId());
							eaForm.getIndicator().setExpIndicatorId(actInd.getIndicatorId());
							eaForm.getIndicator().setBaseVal(actInd.getBaseVal());
							if(actInd.getBaseValDate() != null){
					     		eaForm.getIndicator().setBaseValDate(actInd.getBaseValDate());
							}else{
								eaForm.getIndicator().setBaseValDate(null);
							}
							eaForm.getIndicator().setBaseValComments(actInd.getBaseValComments());
							eaForm.getIndicator().setTargetVal(actInd.getTargetVal());
							if(actInd.getTargetValDate() != null){
							     eaForm.getIndicator().setTargetValDate(actInd.getTargetValDate());
							}else{
								eaForm.getIndicator().setTargetValDate(null);
							}
							eaForm.getIndicator().setTargetValComments(actInd.getTargetValComments());
							eaForm.getIndicator().setRevTargetVal(actInd.getRevisedTargetVal());
							if(actInd.getRevisedTargetValDate() != null){
								eaForm.getIndicator().setRevTargetValDate(actInd.getRevisedTargetValDate());
							}else{
								eaForm.getIndicator().setRevTargetValDate(null);
							}
							eaForm.getIndicator().setRevTargetValComments(actInd.getRevisedTargetValComments());
							
							eaForm.getIndicator().setCurrentVal(actInd.getActualVal());
							if (actInd.getActualValDate() != null) {
								eaForm.getIndicator().setCurrentValDate(actInd.getActualValDate());
							} else {
								eaForm.getIndicator().setCurrentValDate(null);
							}														
							eaForm.getIndicator().setCurrentValComments(actInd.getActualValComments());
							
							//AmpCategoryValue acv = CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getIndicator().getLogframeCategory());							
							
							if(actInd.getIndicatorsCategory()!=null){
								eaForm.getIndicator().setLogframeCategory(actInd.getIndicatorsCategory().getId());
							} else {
								eaForm.getIndicator().setLogframeCategory(new Long(0));
							}
							if(actInd.getRisk() != null){
								eaForm.getIndicator().setIndicatorRisk(actInd.getRisk());
							}else{
								eaForm.getIndicator().setIndicatorRisk(new Long(0));
							}
							break;
						}
					}
				}
			}
			catch (NumberFormatException nfe) 
			{
				//TODO INDIC why is this exception hidden?
				logger.error(nfe.getMessage());
			}
		}
		else
		{
			eaForm.getIndicator().setExpIndicatorId(new Long(-1));
		}
		return mapping.findForward("forward");
	}
}
