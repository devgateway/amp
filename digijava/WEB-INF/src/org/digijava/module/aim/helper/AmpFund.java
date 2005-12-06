 package org.digijava.module.aim.helper ;


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
}