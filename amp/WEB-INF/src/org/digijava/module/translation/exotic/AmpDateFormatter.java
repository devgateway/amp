package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Class for formatting and parsing dates in locales used by AMP.
 * May support locales unsupported by Java8, 
 * e.g. Tetum (with the help of {@link ExoticDateFormatter}
 * 
 * @author acartaleanu
 *
 */
public abstract class AmpDateFormatter {
	
	
	protected final Locale locale;
	protected final String pattern;
	protected final DateTimeFormatter dtf;
	
	protected AmpDateFormatter(Locale locale, String pattern) {
		this.locale = locale;
		this.pattern = pattern;
		dtf = DateTimeFormatter.ofPattern(this.pattern).withLocale(locale);
	}
	
	public String format(Date date) {
		if (date == null) return null;
		LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return format(ld);
	}
	
	public abstract String format(LocalDate date);
	
	public abstract LocalDate parseDate(String in);
	
	
}
