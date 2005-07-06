package org.digijava.module.aim.helper;

public class FinancialFilters {
	private boolean calendarPresent;
	private boolean currencyPresent;
	private boolean perspectivePresent;
	private boolean yearRangePresent;
	private boolean goButtonPresent; 
	
	/**
	 * @return
	 */
	public boolean isCalendarPresent() {
		return calendarPresent;
	}

	/**
	 * @return
	 */
	public boolean isCurrencyPresent() {
		return currencyPresent;
	}

	/**
	 * @return
	 */
	public boolean isPerspectivePresent() {
		return perspectivePresent;
	}

	/**
	 * @return
	 */
	public boolean isYearRangePresent() {
		return yearRangePresent;
	}

	/**
	 * @param b
	 */
	public void setCalendarPresent(boolean b) {
		calendarPresent = b;
	}

	/**
	 * @param b
	 */
	public void setCurrencyPresent(boolean b) {
		currencyPresent = b;
	}

	/**
	 * @param b
	 */
	public void setPerspectivePresent(boolean b) {
		perspectivePresent = b;
	}

	/**
	 * @param b
	 */
	public void setYearRangePresent(boolean b) {
		yearRangePresent = b;
	}

	/**
	 * @return
	 */
	public boolean isGoButtonPresent() {
		return goButtonPresent;
	}

	/**
	 * @param b
	 */
	public void setGoButtonPresent(boolean b) {
		goButtonPresent = b;
	}

}	