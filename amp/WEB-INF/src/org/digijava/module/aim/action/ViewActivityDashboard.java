/*
 * ViewActivityDashboard.java
 * Created : 18-Apr-2006
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class ViewActivityDashboard extends TilesAction {
	
	private static Logger logger = Logger.getLogger(
			ViewActivityDashboard.class);

	public ActionForward execute(ComponentContext context,ActionMapping mapping,
			ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long actId = null;
		
		DynaActionForm adForm = (DynaActionForm) form;
		
		if (request.getParameter("ampActivityId") != null) {
			actId = new Long(Long.parseLong(
					request.getParameter("ampActivityId")));			
			
			
			ArrayList<AmpIndicatorRiskRatings> risks=new ArrayList<AmpIndicatorRiskRatings>();
			Set<IndicatorActivity> valuesActivity=ActivityUtil.loadActivity(actId).getIndicators();
			if(valuesActivity!=null && valuesActivity.size()>0){
				Iterator<IndicatorActivity> it=valuesActivity.iterator();
				while(it.hasNext()){
					 IndicatorActivity indActivity=it.next();
					 Set<AmpIndicatorValue> values=indActivity.getValues();					
					 for(Iterator<AmpIndicatorValue> valuesIter=values.iterator();valuesIter.hasNext();){
						 AmpIndicatorValue val=valuesIter.next();
						 if(val.getRisk()!=null){
							 risks.add(val.getRisk());
							 break;//all values have same risk and this risk should go to connection.
						 }					 					
					}
				}
			}
			
			int risk = IndicatorUtil.getOverallRisk(risks);
			String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
			String rskColor = MEIndicatorsUtil.getRiskColor(risk);
			adForm.set("overallRisk",riskName);
			adForm.set("riskColor",rskColor);
		}
		request.setAttribute("actId",actId);
		
		
		return null;
	}
}