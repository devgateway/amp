package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ParisIndicatorForm extends ActionForm implements Serializable
{
	private Collection donorList;
	private Collection parisIndicatorsList;
	private Collection reportQuestions;

	
	
	/**
	 * @return Returns the reportQuestions.
	 */
	public Collection getReportQuestions() {
		return reportQuestions;
	}

	/**
	 * @param reportQuestions The reportQuestions to set.
	 */
	public void setReportQuestions(Collection reportQuestions) {
		this.reportQuestions = reportQuestions;
	}

	/**
	 * @return Returns the parisIndicatorsList.
	 */
	public Collection getParisIndicatorsList() {
		return parisIndicatorsList;
	}

	/**
	 * @param parisIndicatorsList The parisIndicatorsList to set.
	 */
	public void setParisIndicatorsList(Collection parisIndicatorsList) {
		this.parisIndicatorsList = parisIndicatorsList;
	}

	/**
	 * @return Returns the List of Donors
	 */

	public Collection getDonorList() {
		return donorList;
	}

	public void setDonorList(Collection donorList) {
		this.donorList = donorList;
	}
}