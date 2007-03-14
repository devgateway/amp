package org.digijava.module.aim.form ;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class FinancingBreakdownForm extends MainProjectDetailsForm
{
	public static Logger logger = Logger.getLogger(FinancingBreakdownForm.class);
	
	private Collection financingBreakdown ;
	private String totalCommitted ;
	private String totalDisbursed ;
	private String totalUnDisbursed ;
	private String totalExpended ;
	private String totalUnExpended ;
	private Collection fiscalYears;
	private long fiscalCalId;
	private String perpsectiveNameTrimmed;
		
	/**
	 * @return
	 */
	public Collection getFinancingBreakdown() {
		return financingBreakdown;
	}

	
	
	/**
	 * @return
	 */
	
	public String getTotalCommitted() {
		return totalCommitted;
	}

	/**
	 * @return
	 */
	public String getTotalDisbursed() {
		return totalDisbursed;
	}

	public String getTotalUnDisbursed() {
		return totalUnDisbursed;
	}
	
	/**
	 * @return
	 */
	public String getTotalExpended() {
		return totalExpended;
	}

	public String getTotalUnExpended() {
		return totalUnExpended;
	}

	/**
	 * @param collection
	 */
	public void setFinancingBreakdown(Collection collection) {
		financingBreakdown = collection;
	}

	/**
	 * @param string
	 */
	public void setTotalCommitted(String string) {
		totalCommitted = string;
	}

	/**
	 * @param string
	 */
	public void setTotalDisbursed(String string) {
		totalDisbursed = string;
	}

	public void setTotalUnDisbursed(String string) {
		totalUnDisbursed = string;
	}

	/**
	 * @param string
	 */
	public void setTotalExpended(String string) {
		totalExpended = string;
	}

	public void setTotalUnExpended(String string) {
		totalUnExpended = string;
	}

	public long getFiscalCalId() {
		return fiscalCalId;
	}

	/**
	 * @param i
	 */
	public void setFiscalCalId(long i) {
		fiscalCalId = i;
	}

	public void setFiscalYears(Collection c)
	{
		fiscalYears=c;
	}

	public Collection getFiscalYears()
	{
		return fiscalYears;
	}
	
	public ActionErrors validate(ActionMapping actionMapping,
					 			 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
	 	return errors;
	}



	public String getPerpsectiveNameTrimmed() {
		return super.getPerpsectiveName().replaceAll(" ","");
	}



	public void setPerpsectiveNameTrimmed(String perpsectiveNameTrimmed) {
		this.perpsectiveNameTrimmed = perpsectiveNameTrimmed;
	}	
}
	
