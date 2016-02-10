package org.dgfoundation.amp.testmodels.nicolumns;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.dgfoundation.amp.nireports.TranslatedDate;

/**
 * Used by FundingColumnGenerator to create a TranslatedDate from a year+month
 * don't use outside of hardcoded testcases!
 * @author acartaleanu
 *
 */
public class GregorianTestDateGenerator {
	
	LocalDate date;

	public GregorianTestDateGenerator(int year, Month month) {
		date = LocalDate.of(year, month, 1);
	}
	
	public GregorianTestDateGenerator(int year, String month) {
		String dateString = String.format("%d-%s-01", year, month);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMMM-dd");
		date = LocalDate.parse(dateString, dtf);
	}
	
	int getQuarter(Month month) {
		switch (month.firstMonthOfQuarter()) {
		case JANUARY:
			return 1;
		case APRIL:
			return 2;
		case JULY:
			return 3;
		case OCTOBER:
			return 4;
		default:
			throw new RuntimeException(".firstMonthOfQuarter returned non-quarter month!");
		}
	}
	
	public TranslatedDate toTranslatedDate() {
		return new TranslatedDate(date.getYear(), String.format("%d", date.getYear()), getQuarter(date.getMonth()), date.getMonth().getValue(), 
				date.getMonth().getDisplayName(TextStyle.FULL, Locale.US));
	}
	
}
