package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;


public class AmpSimpleDateFormatter extends AmpDateFormatter {
	
	
	public AmpSimpleDateFormatter(String pattern, String langCode) {
		super(pattern, Locale.forLanguageTag(langCode));
	}

	public AmpSimpleDateFormatter(String pattern, Locale loc) {
		super(pattern, loc);
	}
	
	
	@Override
	public String format(LocalDate date) {
		return dtf.format(date);
	}

	@Override
	public LocalDate parseDate(String in) {
		return dtf.parse(in, LocalDate::from);
	}
}
