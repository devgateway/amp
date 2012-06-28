package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.gateperm.core.GatePermConst;

public class AddFundingPledgeDetail extends Action {

	private static Logger logger = Logger.getLogger(AddFundingPledgeDetail.class);

	private ArrayList<FundingPledgesDetails> fundingDetails = null;

	private String event;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		PledgeForm plForm = (PledgeForm) form;
		event = plForm.getFundingEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);

		AmpCurrency currency = null;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currency = CurrencyUtil.getAmpcurrency(appSettings.getCurrencyId());
			}
		}

		Long index[] = plForm.getSelectedFunding();
        if(event!=null){
		String subEvent = event.substring(0,3);
		FundingPledgesDetails fpd = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (plForm.getFundingPledgesDetails() == null) {
				fundingDetails = new ArrayList<FundingPledgesDetails>();
				fpd = getFundingDetail(currency);
				fundingDetails.add(fpd);
			} else {
				fundingDetails = new ArrayList<FundingPledgesDetails>(plForm.getFundingPledgesDetails());
				if (subEvent.equals("del")) {
					String deleteList[] = request.getParameter("deleteFunds").split("_");
					Iterator <FundingPledgesDetails> iter=fundingDetails.iterator();
					ArrayList<FundingPledgesDetails> fundingsToDelete = new ArrayList<FundingPledgesDetails>();
					Integer i = 1;
					while(iter.hasNext()){
                    	FundingPledgesDetails del = iter.next();
                    	for (int j = 0; j < deleteList.length; j++) {
							if (deleteList[j].equals(i.toString())){
								fundingsToDelete.add(del);
							}
						}	
                    	i++;
                    }
					if (fundingsToDelete.size()!=0) {
						fundingDetails.removeAll(fundingsToDelete);
					}
				} else {
					fpd = getFundingDetail(currency);
					fundingDetails.add(fpd);
				}
			}
			
			plForm.setFundingPledgesDetails(fundingDetails);
			
		}
        }
		plForm.setFundingEvent(null);
		request.getSession().removeAttribute("deleteFunds");
		return mapping.findForward("forward");
	}

	private FundingPledgesDetails getFundingDetail(AmpCurrency currency) {
		FundingPledgesDetails fundingDetail = new FundingPledgesDetails();

		fundingDetail.setCurrencycode(currency.getCurrencyCode());
		//fundingDetail.setPledgetype();
        return fundingDetail;
	}
}
