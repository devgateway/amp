/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.aim.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.ViewContractingForm;
import org.digijava.module.aim.util.ActivityUtil;

/**
 *
 * 
 */
public class ContractingBreakdown extends TilesAction {
    private static Logger logger = Logger.getLogger(ContractingBreakdown.class);
	
	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug("In get Contracting list action");
		
	
			ViewContractingForm contrForm =(ViewContractingForm) form;
			
			Long actId = null;
			
				contrForm.setTabIndex(request.getParameter("tabIndex"));
				String activityId = request.getParameter("ampActivityId");
				if (null != activityId && activityId.trim().length() > 0) {
					actId = Long.valueOf(activityId);
					logger.debug("actId : " + actId);
					contrForm.setAmpActivityId(actId);
				}
			
			
			
			List contracts = ActivityUtil.getIPAContracts(actId);

			contrForm.setContracts(contracts);
			
			return null;
			
		}
		
	}


