package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExoticDateFormatter extends AmpDateFormatter {


	final private Pattern dayPattern;
	final private Pattern monthPattern;
	final private Pattern yearPattern;
	

	public ExoticDateFormatter(String pattern, Locale locale) {
		super(pattern, locale);
		dayPattern = Pattern.compile("^\\d+");
		monthPattern = Pattern.compile("[\\w&&[\\D]]{3}+");
		yearPattern = Pattern.compile("\\d{4}+");
	}
	
	@Override
	public String format(Date date) {
		LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return format(ld);
	}
	
	@Override
	public String format(LocalDate date) {
		if (this.pattern.contains("MMM")) {
			return formatWithMonthName(date);
		} else {
			return dtf.format(date);
		}
	}
	
	private String formatWithMonthName(LocalDate date) {
		String day = String.format("%d", date.getDayOfMonth());
		String monthName = ExoticMonthNames.forLocale(this.locale).getShortMonthName(date.getMonthValue());
		String year = String.format("%d", date.getYear());
		String res = pattern.replace("dd", day);
		res = res.replace("MMM", monthName);
		res = res.replace("yyyy", year);
		return res;
	}
	
	@Override
	public LocalDate parseDate(String in) {
		if (this.pattern.contains("MMM")) {
			return parseWithMonthName(in);
		} else {
			return DateTimeFormatter.ofPattern(pattern).withLocale(locale).parse(in, LocalDate::from);
		}
	}
	
	private LocalDate parseWithMonthName(String in) {
		return LocalDate.of(getYears(in), getMonths(in), getDays(in));
	}

	private int getDays(String in) {
		String patt = this.pattern.toLowerCase();
		int posD = patt.indexOf("dd");
		Matcher m = dayPattern.matcher(in.substring(posD));
		m.find();
		String day = m.group();
		return Integer.parseInt(day);
	}
	
	private int getMonths(String in) {
		Matcher m = monthPattern.matcher(in);
		m.find();
		String monthName = m.group();
		return ExoticMonthNames.forLocale(locale).getMonthNumber(monthName);
	}
	
	private int getYears(String in) {
		/* To avoid unnecessary complications with
		 * parsing 1/aug/2014, 1/2014/aug, and other corner cases, 
		 * will just grab the only 4-digit number in the string.
		 */
		Matcher m = yearPattern.matcher(in);
		m.find();
		String year = m.group();
		return Integer.parseInt(year);
	}
}
