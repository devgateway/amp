/*
 * Created on 15/05/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.helper;

import java.util.ArrayList;

public class ParisIndicator {

	private String donor;			// acronym of donor - organisation
	private ArrayList answers;		// holds 1-D array of doubles for containing one year-row inside a donor-row
	
	/**
	 * @return Returns the answers.
	 */
	public ArrayList getAnswers() {
		return answers;
	}
	/**
	 * @param answers The answers to set.
	 */
	public void setAnswers(ArrayList answers) {
		this.answers = answers;
	}
	/**
	 * @return Returns the donor.
	 */
	public String getDonor() {
		return donor;
	}
	/**
	 * @param donor The donor to set.
	 */
	public void setDonor(String donor) {
		this.donor = donor;
	}
}
