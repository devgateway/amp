package org.digijava.module.translation.exotic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver;

public class ExoticMonthNames {
	
	private final Locale locale;
	
	private ExoticMonthNames(Locale loc) {
		this.locale = loc;
	}
	
	static ExoticMonthNames forLocale(Locale loc) {
		return new ExoticMonthNames(loc);
	}
	
	/**
	 * 
	 * @param monthNumber number of the month, starting from 1
	 * @return capitalized month name in that language
	 */
	public String getFullMonthName(int monthNumber) {
		ResourceBundle rb = ResourceBundle.getBundle("org.digijava.module.translation.exotic.ExoticMonthNames", locale);
		return rb.getString(new Integer(monthNumber).toString());
	}
	
	private static String shortenMonthName(String in) {
		return in.toLowerCase().substring(0, 3);
	}
	
	public String getShortMonthName(int monthNumber) {
		return shortenMonthName(getFullMonthName(monthNumber));
	}
	
	public int getMonthNumber(String shortMonthName) {
		ResourceBundle rb = ResourceBundle.getBundle("org.digijava.module.translation.exotic.ExoticMonthNames", locale);
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			if (shortenMonthName(value).equals(shortMonthName.toLowerCase()))
				return Integer.parseInt(key);
		}
		throw new RuntimeException("No month with short name " + shortMonthName);
	}
}
