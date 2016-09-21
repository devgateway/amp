package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public abstract class AmpDateFormatter {
	
	
	protected final Locale locale;
	protected final String pattern;
	protected final DateTimeFormatter dtf;
	public AmpDateFormatter(String pattern, Locale locale) {
		this.locale = locale;
		this.pattern = pattern;
		dtf = DateTimeFormatter.ofPattern(this.pattern).withLocale(locale);
	}

	
	public abstract String format(Date date);
	public abstract String format(LocalDate date);
	
	public abstract LocalDate parseDate(String in);
	
	
}
