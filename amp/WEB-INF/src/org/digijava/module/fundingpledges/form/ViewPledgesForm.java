package org.digijava.module.fundingpledges.form;

import java.util.TreeMap;

import org.apache.struts.action.ActionForm;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

public class ViewPledgesForm extends ActionForm {
	
	private TreeMap<FundingPledges, Boolean> allFundingPledges;

	/**
	 * @return the allFundingPledges
	 *
	public Collection<FundingPledges> getAllFundingPledges() {
		return allFundingPledges;
	}

	/**
	 * @param allFundingPledges the allFundingPledges to set
	 *
	public void setAllFundingPledges(Collection<FundingPledges> allFundingPledges) {
		this.allFundingPledges = allFundingPledges;
	}*/

	/**
	 * @return the allFundingPledges
	 */
	public TreeMap<FundingPledges, Boolean> getAllFundingPledges() {
		return allFundingPledges;
	}

	/**
	 * @param allFundingPledges the allFundingPledges to set
	 */
	public void setAllFundingPledges(TreeMap<FundingPledges, Boolean> allFundingPledges) {
		this.allFundingPledges = allFundingPledges;
	}

}
