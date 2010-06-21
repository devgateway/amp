package org.digijava.module.fundingpledges.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

public class ViewPledgesForm extends ActionForm {
	
	private Collection<FundingPledges> allFundingPledges;

	/**
	 * @return the allFundingPledges
	 */
	public Collection<FundingPledges> getAllFundingPledges() {
		return allFundingPledges;
	}

	/**
	 * @param allFundingPledges the allFundingPledges to set
	 */
	public void setAllFundingPledges(Collection<FundingPledges> allFundingPledges) {
		this.allFundingPledges = allFundingPledges;
	}

}
