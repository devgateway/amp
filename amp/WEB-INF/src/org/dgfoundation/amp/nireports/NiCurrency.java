package org.dgfoundation.amp.nireports;

/**
 * user-independent way of specifying a currency in NiReports
 * @author Dolghier Constantin
 *
 */
public interface NiCurrency {
	public long getId();
	public String getCurrencyName();
	public String getCurrencyCode();
}
