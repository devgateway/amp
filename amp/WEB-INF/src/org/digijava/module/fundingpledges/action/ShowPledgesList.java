package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
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
				pledge.getYearsList().add(getYearsDescription(fpd));
			}
		}
		plForm.setAllFundingPledges(pledges);
		return mapping.findForward("forward");
	}
	
	/**
	 * constructs a string of the form "undefined", "year" or "yearstart - yearend", describing a pledge detail
	 * @param fpd
	 * @return
	 */
	public static String getYearsDescription(FundingPledgesDetails fpd){
		if (fpd.getFundingYear() == null)
			return TranslatorWorker.translateText("unspecified");
		if (fpd.getFundingYearEnd() != null && !fpd.getFundingYearEnd().toString().equals(fpd.getFundingYear()))
			return String.format("%s - %s", fpd.getFundingYear(), fpd.getFundingYearEnd());
		return fpd.getFundingYear();
	}
}
