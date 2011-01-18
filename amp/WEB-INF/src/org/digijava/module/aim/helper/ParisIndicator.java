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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((donor == null) ? 0 : donor.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParisIndicator other = (ParisIndicator) obj;
		if (donor == null) {
			if (other.donor != null)
				return false;
		} else if (!donor.equalsIgnoreCase(other.donor))
			return false;
		return true;
	}
}
