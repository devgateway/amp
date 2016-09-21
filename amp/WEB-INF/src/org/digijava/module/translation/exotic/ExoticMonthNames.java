package org.digijava.module.translation.exotic;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;


public class ExoticMonthNames {
	
	private final Locale locale;
	
	private ExoticMonthNames(Locale loc) {
		this.locale = loc;
	}
	
	static ExoticMonthNames forLocale(Locale loc) {
		return new ExoticMonthNames(loc);
	}
	
	private static ConcurrentHashMap<Locale, List<String>> localesToMonthNames = new ConcurrentHashMap<>();
	
	private List<String> loadMonthNamesForLocale(Locale loc) {
		List<String> res = new ArrayList<>();
		ResourceBundle rb = ResourceBundle.getBundle("org.digijava.module.translation.exotic.ExoticMonthNames", loc);
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			res.add(value);
		}
		return res;
	}
	
	List<String> getMonthNamesForLocale(Locale loc) {
		return localesToMonthNames.computeIfAbsent(loc, z -> loadMonthNamesForLocale(loc));
	}
	
	/**
	 * 
	 * @param monthNumber number of the month, starting from 1
	 * @return capitalized month name in that language
	 */
	public String getFullMonthName(int monthNumber) {
		return getMonthNamesForLocale(locale).get(monthNumber - 1);

	}
	
	private static String shortenMonthName(String in) {
		return in.substring(0, 3);
	}
	
	public String getShortMonthName(int monthNumber) {
		return shortenMonthName(getFullMonthName(monthNumber));
	}
	
	public int getMonthNumber(String shortMonthName) {
		List<String> monthNames = getMonthNamesForLocale(locale);
		for (int i = 0; i < monthNames.size(); i++) {
			if (shortenMonthName(monthNames.get(i)).equals(shortMonthName))
				return i + 1;
		}
		throw new RuntimeException("Couldn't find month with short name" + shortMonthName + " in locale " + locale.toString());
	}

}
