package org.digijava.module.aim.form ;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
public class CommitmentbySectorForm extends ActionForm
{
	private String donor ;
	private String title ;
	private String startDate ;
	private String closeDate ;
	private Collection report ;
	private int forcastYear1 ;
	private int forcastYear2 ;
	private int forcastYear3 ;
	
	public String getDonor() 
		{
			return donor;
		}

		public void setDonor(String string) 
		{
			donor = string;
		}
	
		public String getTitle() 
		{
			return title;
		}
	
		public void setTitle(String string) 
		{
			title = string;
		}

		public String getStartDate() 
		{
			return startDate;
		}

		public void setStartDate(String string) 
		{
			startDate = string;
		}

		public String getCloseDate() 
		{
			return closeDate;
		}

		public void setCloseDate(String string) 
		{
			closeDate = string;
		}
		public Collection getReport() 
		{
			return report;
		}
		public void setReport(Collection collection) 
		{
			report = collection;
		}
	
	/**
	 * @return
	 */
	public int getForcastYear1() {
		return forcastYear1;
	}

	/**
	 * @return
	 */
	public int getForcastYear2() {
		return forcastYear2;
	}

	/**
	 * @return
	 */
	public int getForcastYear3() {
		return forcastYear3;
	}

	/**
	 * @param i
	 */
	public void setForcastYear1(int i) {
		forcastYear1 = i;
	}

	/**
	 * @param i
	 */
	public void setForcastYear2(int i) {
		forcastYear2 = i;
	}

	/**
	 * @param i
	 */
	public void setForcastYear3(int i) {
		forcastYear3 = i;
	}

}


