package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;


public abstract class AmpDateFormatter {
	
	
	protected final Locale locale;
	protected final String pattern;
	public AmpDateFormatter(String pattern, Locale locale) {
		this.locale = locale;
		this.pattern = pattern;
	}

	
	public abstract String format(Date date);
	public abstract String format(LocalDate date);
	
	public abstract LocalDate parseDate(String in);
	
	
}
