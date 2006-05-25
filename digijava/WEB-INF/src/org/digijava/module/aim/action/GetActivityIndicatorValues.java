package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;

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
			try
			{
				indValId = new Long(Long.parseLong(temp));
				if (eaForm.getIndicatorsME() != null) 
				{
					Iterator itr = eaForm.getIndicatorsME().iterator();
					while (itr.hasNext()) 
					{
						ActivityIndicator actInd = (ActivityIndicator) itr.next();
						if (actInd.getIndicatorValId().equals(indValId)) 
						{
							eaForm.setIndicatorId(actInd.getIndicatorId());
							eaForm.setIndicatorValId(actInd.getIndicatorValId());
							eaForm.setExpIndicatorId(actInd.getIndicatorId());
							eaForm.setBaseVal(actInd.getBaseVal());
							eaForm.setBaseValDate(actInd.getBaseValDate());
							eaForm.setTargetVal(actInd.getTargetVal());
							eaForm.setTargetValDate(actInd.getTargetValDate());
							eaForm.setActualVal(actInd.getActualVal());
							eaForm.setActualValDate(actInd.getActualValDate());
							eaForm.setIndicatorPriorValues(MEIndicatorsUtil.getPriorIndicatorValues(indValId));
							eaForm.setCurrentVal(actInd.getCurrentVal());
							eaForm.setCurrValueDate(actInd.getCurrentValDate());
							eaForm.setComments(actInd.getComments());
							eaForm.setIndicatorRisk(actInd.getRisk());
							break;
						}
					}
				}
			}
			catch (NumberFormatException nfe) 
			{
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
