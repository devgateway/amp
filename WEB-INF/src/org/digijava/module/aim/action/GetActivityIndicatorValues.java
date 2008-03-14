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
				if(eaForm.getRiskCollection()!=null){
					Iterator iter=eaForm.getRiskCollection().iterator();
	            	 while (iter.hasNext()){
	    	       		 AmpIndicatorRiskRatings ampIndRisc=(AmpIndicatorRiskRatings) iter.next();
	    	       		 ampIndRisc.setTranslatedRatingName(ampIndRisc.getRatingName().replace(" ",""));
	    	       	 }
				} 
				if (eaForm.getIndicatorsME() != null) 
				{
					Iterator itr = eaForm.getIndicatorsME().iterator();
					while (itr.hasNext()) 
					{
						ActivityIndicator actInd = (ActivityIndicator) itr.next();

						//if (actInd.getConnectionId()!=null && actInd.getConnectionId().equals(indValId)) 

						if (actInd.getIndicatorId()!=null && actInd.getIndicatorId().equals(indValId)) 
						{
							//AllPrgIndicators actInd = IndicatorUtil.getAmpIndicator(actIndicator.getIndicatorId(),actIndicator.getActivityId());
							// AMP-2828 by mouhamad
					        String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
					        //TODO INDIC ??
					        dateFormat = dateFormat.replace("m", "M");
					          
					        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);							
							
							eaForm.setIndicatorId(actInd.getIndicatorId());
							eaForm.setExpIndicatorId(actInd.getIndicatorId());
							eaForm.setBaseVal(actInd.getBaseVal());
							if(actInd.getBaseValDate() != null){
					     		eaForm.setBaseValDate(actInd.getBaseValDate());
							}else{
								eaForm.setBaseValDate(null);
							}
							eaForm.setBaseValComments(actInd.getBaseValComments());
							eaForm.setTargetVal(actInd.getTargetVal());
							eaForm.setTargetVal(actInd.getTargetVal());
							if(actInd.getTargetValDate() != null){
							     eaForm.setTargetValDate(actInd.getTargetValDate());
							}else{
								eaForm.setTargetValDate(null);
							}
							eaForm.setTargetValComments(actInd.getTargetValComments());
							eaForm.setRevTargetVal(actInd.getRevisedTargetVal());
							if(actInd.getRevisedTargetValDate() != null){
							eaForm.setRevTargetValDate(actInd.getRevisedTargetValDate());
							}else{
								eaForm.setRevTargetValDate(null);
							}
							eaForm.setRevTargetValComments(actInd.getRevisedTargetValComments());
							if(actInd.getIndicatorsCategory()!=null){
							eaForm.setLogframeCategory(actInd.getIndicatorsCategory().getId());
							}
							if(actInd.getCurrentValDate() != null) {
								eaForm.setCurrentValDate(actInd.getCurrentValDate());
							}else{
							    eaForm.setCurrentValDate(null);
							}
  							   eaForm.setCurrentValComments(actInd.getCurrentValComments());
							
							if(actInd.getRisk()!= null){
								eaForm.setIndicatorRisk(actInd.getRisk());
							   
							}else{
								eaForm.setIndicatorRisk(null);
							}
							
							eaForm.setCurrentValComments(actInd.getActualValComments());
							if(actInd.getRisk() != null){
							eaForm.setIndicatorRisk(actInd.getRisk());
							}else{
								eaForm.setIndicatorRisk(new Long(0));
							}
							if(actInd.getIndicatorsCategory() != null){
							eaForm.setLogframeCategory(actInd.getIndicatorsCategory().getId());
							}else{
								eaForm.setLogframeCategory(new Long(0));
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
			eaForm.setExpIndicatorId(new Long(-1));
		}
		return mapping.findForward("forward");
	}
}
