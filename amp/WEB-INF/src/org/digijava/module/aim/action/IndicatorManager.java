package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class IndicatorManager extends Action 
{
	private static Logger logger = Logger.getLogger(IndicatorManager.class);

	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		String viewPreference = request.getParameter("view");
		if(viewPreference!=null)
		{
			if(viewPreference.equals("meindicators"))
			{
				String action = request.getParameter("action");
				Collection indicators = new ArrayList();
				Collection colMeIndValIds = null;
				Collection ampMECurrValIds = null;
				
				IndicatorForm indForm = (IndicatorForm) form;

				if (action != null && action.equals("delete")) 
				{
					Long indId = new Long(Long.parseLong(request.getParameter("id")));
					if( indId != null )
					{
						AmpMEIndicators ampMEInd = new AmpMEIndicators();
						ampMEInd.setAmpMEIndId(indId);
						
						colMeIndValIds = MEIndicatorsUtil.getMeIndValIds(indId);
						AmpMEIndicatorValue ampMEIndVal = null;
						
						Iterator itr = colMeIndValIds.iterator();
						while(itr.hasNext())
						{
							ampMEIndVal = (AmpMEIndicatorValue) itr.next();
							ampMECurrValIds = MEIndicatorsUtil.getMeCurrValIds(ampMEIndVal.getAmpMeIndValId());
							
							if(ampMECurrValIds != null)
							{
								AmpMECurrValHistory ampMECurrVal = null;
								Iterator itrCurrVal = ampMECurrValIds.iterator();
								while(itrCurrVal.hasNext())
								{
									ampMECurrVal = (AmpMECurrValHistory) itrCurrVal.next();
									DbUtil.delete(ampMECurrVal);
								}
							}
							DbUtil.delete(ampMEIndVal);
						}
						DbUtil.delete(ampMEInd);
					}
				}
				indicators = MEIndicatorsUtil.getAllIndicators();
				indForm.setIndicators(indicators);
				
				return mapping.findForward("forward");
			}
			else if(viewPreference.equals("indicators"))
			{
				return mapping.findForward("gotoIndicators");
			}
			else if(viewPreference.equals("multiprogram"))
			{
				return mapping.findForward("gotoMultiProgram");
			}
			
			
			
		}
		
		String action = request.getParameter("action");
		Collection indicators = new ArrayList();
		Collection colMeIndValIds = null;
		Collection ampMECurrValIds = null;
		
		IndicatorForm indForm = (IndicatorForm) form;

		if (action != null && action.equals("delete")) 
		{
			Long indId = new Long(Long.parseLong(request.getParameter("id")));
			if( indId != null )
			{
				AmpMEIndicators ampMEInd = new AmpMEIndicators();
				ampMEInd.setAmpMEIndId(indId);
				
				colMeIndValIds = MEIndicatorsUtil.getMeIndValIds(indId);
				AmpMEIndicatorValue ampMEIndVal = null;
				
				Iterator itr = colMeIndValIds.iterator();
				while(itr.hasNext())
				{
					ampMEIndVal = (AmpMEIndicatorValue) itr.next();
					ampMECurrValIds = MEIndicatorsUtil.getMeCurrValIds(ampMEIndVal.getAmpMeIndValId());
					
					if(ampMECurrValIds != null)
					{
						AmpMECurrValHistory ampMECurrVal = null;
						Iterator itrCurrVal = ampMECurrValIds.iterator();
						while(itrCurrVal.hasNext())
						{
							ampMECurrVal = (AmpMECurrValHistory) itrCurrVal.next();
							DbUtil.delete(ampMECurrVal);
						}
					}
					DbUtil.delete(ampMEIndVal);
				}
				DbUtil.delete(ampMEInd);
			}
		}
		indicators = MEIndicatorsUtil.getAllIndicators();
		indForm.setIndicators(indicators);
		
		return mapping.findForward("forward");
	}
}