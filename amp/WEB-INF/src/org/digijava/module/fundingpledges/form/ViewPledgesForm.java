package org.digijava.module.fundingpledges.form;

import java.util.TreeMap;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

@Data
public class ViewPledgesForm extends ActionForm {
	
	private TreeMap<FundingPledges, Boolean> allFundingPledges;
}
