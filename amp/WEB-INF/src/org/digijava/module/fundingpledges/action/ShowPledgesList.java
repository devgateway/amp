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
		TreeMap<FundingPledges, Boolean> map = new TreeMap<FundingPledges, Boolean>();
		
		for (Iterator iterator = pledges.iterator(); iterator.hasNext();) {
			FundingPledges pledge = (FundingPledges) iterator.next();
			pledge.setTotalAmount((double) 0);
			pledge.setYearsList(new TreeSet<String>());
			Collection<FundingPledgesDetails> fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			if (fpdl!=null) {
				for (Iterator iterator2 = fpdl.iterator(); iterator2.hasNext();) {
					FundingPledgesDetails fpd = (FundingPledgesDetails) iterator2.next();
					pledge.setTotalAmount(pledge.getTotalAmount() + fpd.getAmount());
					if (fpd.getFundingYear()!=null) {
						pledge.getYearsList().add(fpd.getFundingYear());
					} else {
						String unspecified = TranslatorWorker.translateText("unspecified");
						pledge.getYearsList().add(unspecified);
					}
				
				}
			}
			List<AmpFundingDetail> fundsRelated = PledgesEntityHelper.getFundingRelatedToPledges(pledge);
			if (fundsRelated == null || fundsRelated.size()==0) {
				map.put(pledge, false);
			} else {
				map.put(pledge, true);
			}
		}
		plForm.setAllFundingPledges(map);
		return mapping.findForward("forward");
    		
	}
}
