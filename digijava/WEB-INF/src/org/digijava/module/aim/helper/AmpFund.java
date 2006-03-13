 package org.digijava.module.aim.helper ;

import java.util.Collection;


public class AmpFund
{
	private String commAmount;
	private String disbAmount;
	private String plannedDisbAmount;
	private String unDisbAmount;
	private String expAmount;
	private String plCommAmount;
	private String plDisbAmount;
	private String plExpAmount;
	
	//added to support by funding Terms breakdown (below each AmpFund will appear
	//one row for each of the funding Termss and all funds will be summed by Terms
	//these collections contain AmpByTypeAmountS beans
	private Collection byTypeComm;
	private Collection byTypeDisb;
	private Collection byTypePlannedDisb;
	private Collection byTypeUnDisb;
	private Collection byTypeExp;
	private Collection byTypePlComm;
	private Collection byTypePlDisb;
	private Collection byTypePlExp;
	private Collection byTermsPlDisbForecast1;
	private Collection byTermsPlDisbForecast2;
	private Collection byTermsPlDisbForecast3;
	private Collection byTermsPlDisbForecast4;
	
	
	
	
	/**
	 * @return
	 */
	public String getCommAmount() {
		return commAmount;
	}

	/**
	 * @return
	 */
	public String getPlCommAmount() {
		return plCommAmount;
	}

	/**
	 * @return
	 */
	public String getDisbAmount() {
		return disbAmount;
	}

	public String getPlannedDisbAmount() {
		return plannedDisbAmount;
	}

	/**
	 * @return
	 */
	public String getPlDisbAmount() {
		return plDisbAmount;
	}

	/**
	 * @return
	 */
	public String getExpAmount() {
		return expAmount;
	}

	/**
	 * @return
	 */
	public String getPlExpAmount() {
		return plExpAmount;
	}

	/**
	 * @param string
	 */
	public void setCommAmount(String string) {
		commAmount = string;
	}

	/**
	 * @param string
	 */
	public void setPlCommAmount(String string) {
		plCommAmount = string;
	}

	/**
	 * @param string
	 */
	public void setDisbAmount(String string) {
		disbAmount = string;
	}

	public String getUnDisbAmount() {
		return unDisbAmount;
	}

	public void setPlannedDisbAmount(String string) {
		plannedDisbAmount = string;
	}

	/**
	 * @param string
	 */
	public void setPlDisbAmount(String string) {
		plDisbAmount = string;
	}

	/**
	 * @param string
	 */
	public void setExpAmount(String string) {
		expAmount = string;
	}

	/**
	 * @param string
	 */
	public void setPlExpAmount(String string) {
		plExpAmount = string;
	}

	public void setUnDisbAmount(String string) {
		unDisbAmount = string;
	}

	/**
	 * @return Returns the byTypeComm.
	 */
	public Collection getByTypeComm() {
		return byTypeComm;
	}

	/**
	 * @param byTypeComm The byTypeComm to set.
	 */
	public void setByTypeComm(Collection byTermsComm) {
		this.byTypeComm = byTermsComm;
	}

	/**
	 * @return Returns the byTypeDisb.
	 */
	public Collection getByTypeDisb() {
		return byTypeDisb;
	}

	/**
	 * @param byTypeDisb The byTypeDisb to set.
	 */
	public void setByTypeDisb(Collection byTermsDisb) {
		this.byTypeDisb = byTermsDisb;
	}

	/**
	 * @return Returns the byTypeExp.
	 */
	public Collection getByTypeExp() {
		return byTypeExp;
	}

	/**
	 * @param byTypeExp The byTypeExp to set.
	 */
	public void setByTypeExp(Collection byTermsExp) {
		this.byTypeExp = byTermsExp;
	}

	/**
	 * @return Returns the byTypePlannedDisb.
	 */
	public Collection getByTypePlannedDisb() {
		return byTypePlannedDisb;
	}

	/**
	 * @param byTypePlannedDisb The byTypePlannedDisb to set.
	 */
	public void setByTypePlannedDisb(Collection byTermsPlannedDisb) {
		this.byTypePlannedDisb = byTermsPlannedDisb;
	}

	/**
	 * @return Returns the byTypePlComm.
	 */
	public Collection getByTypePlComm() {
		return byTypePlComm;
	}

	/**
	 * @param byTypePlComm The byTypePlComm to set.
	 */
	public void setByTypePlComm(Collection byTermsPlComm) {
		this.byTypePlComm = byTermsPlComm;
	}

	/**
	 * @return Returns the byTypePlDisb.
	 */
	public Collection getByTypePlDisb() {
		return byTypePlDisb;
	}

	/**
	 * @param byTypePlDisb The byTypePlDisb to set.
	 */
	public void setByTypePlDisb(Collection byTypePlDisb) {
		this.byTypePlDisb = byTypePlDisb;
	}



	
	
	
	
	
	public Collection getByTermsPlDisbForecast1() 
	{
		return byTermsPlDisbForecast1;
	}
	public void setByTermsPlDisbForecast1(Collection byTermsPlDisbForecast1) 
	{
		this.byTermsPlDisbForecast1 = byTermsPlDisbForecast1;
	}
	public Collection getByTermsPlDisbForecast2() 
	{
		return byTermsPlDisbForecast2;
	}
	public void setByTermsPlDisbForecast2(Collection byTermsPlDisbForecast2) 
	{
		this.byTermsPlDisbForecast2 = byTermsPlDisbForecast2;
	}
	public Collection getByTermsPlDisbForecast3() 
	{
		return byTermsPlDisbForecast3;
	}
	public void setByTermsPlDisbForecast3(Collection byTermsPlDisbForecast3) 
	{
		this.byTermsPlDisbForecast3 = byTermsPlDisbForecast3;
	}
	public Collection getByTermsPlDisbForecast4() 
	{
		return byTermsPlDisbForecast4;
	}
	public void setByTermsPlDisbForecast4(Collection byTermsPlDisbForecast4) 
	{
		this.byTermsPlDisbForecast4 = byTermsPlDisbForecast4;
	}







	
	/**
	 * @return Returns the byTypePlExp.
	 */
	public Collection getByTypePlExp() {
		return byTypePlExp;
	}

	/**
	 * @param byTypePlExp The byTypePlExp to set.
	 */
	public void setByTypePlExp(Collection byTermsPlExp) {
		this.byTypePlExp = byTermsPlExp;
	}

	/**
	 * @return Returns the byTypeUnDisb.
	 */
	public Collection getByTypeUnDisb() {
		return byTypeUnDisb;
	}

	/**
	 * @param byTypeUnDisb The byTypeUnDisb to set.
	 */
	public void setByTypeUnDisb(Collection byTermsUnDisb) {
		this.byTypeUnDisb = byTermsUnDisb;
	}
}
