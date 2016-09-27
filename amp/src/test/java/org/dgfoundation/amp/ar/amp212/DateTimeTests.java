package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.junit.Test;

public class DateTimeTests extends AmpTestCase {
	
	public DateTimeTests() {
		super("Date/time conversion tests");
		
	}
	private final static List<String> patterns = generatePatterns();
	
	private final static List<String> limitedPatterns = Arrays.asList("dd/MMM/yyyy", "MMM/dd/yyyy", "yyyy/MMM/dd");
	
	private final static List<LocalDate> limitedDates = generateLimitedDates();
	
	private static List<LocalDate> generateLimitedDates() {
		List<LocalDate> res = new ArrayList<>();
		res.add(LocalDate.of(1990, 4, 11));
		res.add(LocalDate.of(1990, 11, 11));
		res.add(LocalDate.of(1990, 4, 2));
		res.add(LocalDate.of(1990, 1, 2));
		return res;
	}
	
	private static List<String> generatePatterns() {
		List<String> base = Arrays.asList("dd-MMM-yyyy", "dd-MM-yyyy", "MM-dd-yyyy", "MMM-dd-yyyy", "yyyy-MMM-dd", "yyyy-MM-dd");
		List<String> res = new ArrayList<>();
		for (String el : base) {
			res.add(el.replaceAll("-", "/"));
			res.add(el);
		}
		return res;
	}
	
	private final static List<LocalDate> dates = generateDates();
	private static List<LocalDate> generateDates() {
		List<LocalDate> res = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			res.add(LocalDate.of(1990, i, 1));
			res.add(LocalDate.of(2018, i, 11));
		}
		return res;
	}

	private void printCorrect(List<String> values, String message) {
		System.out.println(message);
		StringJoiner sj = new StringJoiner(",");
		
		for (String p : values) {
			sj.add("\"" + p + "\"");
		}
		System.out.println("Arrays.asList(" + sj.toString() +  ");");
	}
	
	@Test
	public void testLocalizedWithPattern() {
		for (LocalDate ld : dates) {
			for (String pattern : patterns) {
				AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern);
				String fm = formatter.format(ld);
				LocalDate defm = formatter.parseDate(fm);
				assertEquals(ld, defm);
			}
		}
	}

	private void runShortLocalizedWithPattern(Locale locale, List<String> cor) {
		List<String> res = new ArrayList<>();
		for (LocalDate ld : limitedDates) {
			for (String pattern : limitedPatterns) {
				AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern, locale);
				String fm = formatter.format(ld);
				res.add(fm);
			}
		}
		if (!cor.equals(res)) {
			printCorrect(res, "Correct output for " + locale.getLanguage() + " :");
		}
		assertEquals(cor, res);
	}
	
	private void runLocalizedWithPattern(Locale locale) {
		for (LocalDate ld : dates) {
			for (String pattern : patterns) {
				AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern, locale);
				String fm = formatter.format(ld);
				LocalDate defm = formatter.parseDate(fm);
				assertEquals(ld, defm);
			}
		}
	}
	
	@Test
	public void testLocalizedWithPatternRussian() {
		runLocalizedWithPattern(Locale.forLanguageTag("ru"));
	}
	
	@Test
	public void testLocalizedWithPatternTimor() {
		runLocalizedWithPattern(Locale.forLanguageTag("tm"));
	}
	
	@Test
	public void testLocalizedWithPatternFrench() {
		runLocalizedWithPattern(Locale.FRENCH);
	}
	
	@Test
	public void testLocalizedWithPatternOneWayRu() {
		List<String> cor = Arrays.asList("11/апр/1990","апр/11/1990","1990/апр/11","11/ноя/1990","ноя/11/1990","1990/ноя/11",
				"02/апр/1990","апр/02/1990","1990/апр/02","02/янв/1990","янв/02/1990","1990/янв/02");
		runShortLocalizedWithPattern(Locale.forLanguageTag("ru"), cor);
	}

	@Test
	public void testLocalizedWithPatternOneWayTm() {
		List<String> cor = Arrays.asList("11/Fev/1990","Fev/11/1990","1990/Fev/11","11/Sep/1990","Sep/11/1990","1990/Sep/11",
				"2/Fev/1990","Fev/2/1990","1990/Fev/2","2/Nov/1990","Nov/2/1990","1990/Nov/2");
		runShortLocalizedWithPattern(Locale.forLanguageTag("tm"), cor);
	}
	
	@Test
	public void testLocalizedWithPatternOneWayFr() {
		List<String> cor = Arrays.asList("11/avr./1990","avr./11/1990","1990/avr./11","11/nov./1990","nov./11/1990","1990/nov./11",
				"02/avr./1990","avr./02/1990","1990/avr./02","02/janv./1990","janv./02/1990","1990/janv./02");
		runShortLocalizedWithPattern(Locale.forLanguageTag("fr"), cor);
	}
	
	@Test
	public void testDefaultFormatter() {
		for (LocalDate ld : dates) {
			AmpDateFormatter formatter = AmpDateFormatterFactory.getDefaultFormatter();
			String fm = formatter.format(ld);
			LocalDate defm = formatter.parseDate(fm);
			assertEquals(ld, defm);
		}
	}
}
