/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import org.dgfoundation.amp.newreports.ReportSettings;

/**
 * Report Settings 
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportSettings extends MondrianReportFilters implements ReportSettings {
	private String currencyCode = null;
	private String currencyFormat = null;
	
	@Override
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * Configures the currency to be used to display the amounts
	 * @param currencyCode 
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public String getCurrencyFormat() {
		return currencyFormat;
	}
	
	/**
	 * Configures the currency pattern to be used to display the amount 
	 * @param currencyFormat
	 */
	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}
	
}
