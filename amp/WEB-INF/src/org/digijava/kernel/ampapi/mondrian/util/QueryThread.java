package org.digijava.kernel.ampapi.mondrian.util;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.*;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class QueryThread {
	@SuppressWarnings("unused")
	private static String query;
	private static ThreadLocal<String> tl = new ThreadLocal<String>();
	private static ThreadLocal<Site> site = new ThreadLocal<Site>();
	private static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();
	private static ThreadLocal<String> currencycode = new ThreadLocal<String>();
	
	public static String getQuery() {
		return tl.get();
	}

	public static void setQuery(String query) {
		tl.set(query);
	}

	public static Site getSite() {
		return site.get();
	}

	public static void setSite(Site site) {
		QueryThread.site.set(site);
	}
	
	public static Locale getLocale() {
		return locale.get();
	}

	public static void setLocale(Locale locale) {
		QueryThread.locale.set(locale);
	}
	public static String getCurrency() {
		return currencycode.get();
	}

	public static void setCurrency(String currency) {
		QueryThread.currencycode.set(currency);
	}
}
