package org.digijava.module.translation.exotic;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class AmpSimpleDateFormatter extends AmpDateFormatter {
	
	private final DateFormat formatter;
	private final DateTimeFormatter dtf;
	
	public AmpSimpleDateFormatter(String pattern, String langCode) {
		super(pattern, Locale.forLanguageTag(langCode));
		this.formatter = new SimpleDateFormat(this.pattern, locale);
		dtf = DateTimeFormatter.ofPattern(this.pattern).withLocale(locale);
	}

	public AmpSimpleDateFormatter(String pattern, Locale loc) {
		super(pattern, loc);
		this.formatter = new SimpleDateFormat(this.pattern, locale);
		dtf = DateTimeFormatter.ofPattern(pattern).withLocale(locale);
	}
	
	public String format(Date date) {
		return formatter.format(date);
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
