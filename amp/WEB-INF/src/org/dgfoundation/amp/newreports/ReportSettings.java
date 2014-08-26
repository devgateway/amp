/**
 * 
 */
package org.dgfoundation.amp.newreports;

/**
 * Stores Report Settings like currency to display, currency format, calendar type, year ranges to display and other. 
 * Any display filters (like year ranges) will affect only the columns, but not the totals. To filter the totals, please use {@link ReportFilters}
 * @author Nadejda Mandrescu
 */
public interface ReportSettings extends ReportFilters {
	
	/** returns currency code to be used for amounts display */ 
	public String getCurrencyCode();
	
	/** returns a String pattern for currency display format */ 
	public String getCurrencyFormat();
	
	/** returns the Calendar to be used for grouping by dates, filtering by dates, etc */ 
	//TODO: getCalendar();
}
