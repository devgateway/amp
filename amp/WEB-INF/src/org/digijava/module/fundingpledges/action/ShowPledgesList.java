package org.digijava.module.fundingpledges.action;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.ViewPledgesForm;

public class ShowPledgesList extends Action {

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
		ViewPledgesForm plForm = (ViewPledgesForm) form;
		
		List<FundingPledges> pledges = PledgesEntityHelper.getPledges();
		Collections.sort(pledges);
		
		for (FundingPledges pledge: pledges) {
			pledge.setYearsList(new TreeSet<String>());
			for(FundingPledgesDetails fpd:pledge.getFundingPledgesDetails()){
				pledge.getYearsList().add(fpd.getDatesDescription());
			}
		}
		plForm.setAllFundingPledges(pledges);
		return mapping.findForward("forward");
	}
	
}
